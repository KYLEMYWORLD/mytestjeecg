package com.jeecg.jform_task.controller;
import com.jeecg.ConstSetBA;
import com.jeecg.jform_project.entity.JformProjectEntity;
import com.jeecg.jform_task.entity.JformTaskEntity;
import com.jeecg.jform_task.service.JformTaskServiceI;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.web.system.sms.util.TuiSongMsgUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.common.TreeChildCount;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.core.util.MyBeanUtils;

import java.io.OutputStream;
import org.jeecgframework.core.util.BrowserUtils;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.TemplateExportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.vo.TemplateExcelConstants;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jeecgframework.core.util.ResourceUtil;
import java.io.IOException;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.util.Map;
import java.util.HashMap;
import org.jeecgframework.core.util.ExceptionUtil;


/**   
 * @Title: Controller  
 * @Description: 工作任务分配表
 * @author onlineGenerator
 * @date 2018-12-09 17:43:56
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/jformTaskController")
public class JformTaskController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(JformTaskController.class);

	@Autowired
	private JformTaskServiceI jformTaskService;
	@Autowired
	private SystemService systemService;


	/**
	 * 工作任务分配表列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/jform_task/jformTaskList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(JformTaskEntity jformTask,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(JformTaskEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, jformTask, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.jformTaskService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 删除工作任务分配表
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(JformTaskEntity jformTask, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		jformTask = systemService.getEntity(JformTaskEntity.class, jformTask.getId());
		message = "工作任务分配表删除成功";
		if(jformTask.getTaskStatus()!= ConstSetBA.TaskStatus_Draft){
			throw new BusinessException("工作任务不是草稿状态不能进行删除!");
		}
		try{
			jformTaskService.delete(jformTask);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "工作任务分配表删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除工作任务分配表
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "工作任务分配表删除成功";
		String msg ="";
		try{
			for(String id:ids.split(",")){
				JformTaskEntity jformTask = systemService.getEntity(JformTaskEntity.class,id);
				if(jformTask.getTaskStatus()!=ConstSetBA.TaskStatus_Draft){
					msg.concat(","+jformTask.getTaskName());
					continue;
				}
				jformTaskService.delete(jformTask);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "工作任务分配表删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message+ (msg.length()>0? msg+"任务不是草稿状态不能删除！":""));
		return j;
	}


	/**
	 * 添加工作任务分配表
	 * @param jformTask
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(JformTaskEntity jformTask, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "工作任务分配表添加成功";
		try{
			jformTask.setTaskStatus(ConstSetBA.TaskStatus_Draft);//设置草稿状态
			jformTaskService.save(jformTask);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "工作任务分配表添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新工作任务分配表
	 * 
	 * @param jformTask
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(JformTaskEntity jformTask, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "工作任务分配表更新成功";
		JformTaskEntity t = jformTaskService.get(JformTaskEntity.class, jformTask.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(jformTask, t);
			jformTaskService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "工作任务分配表更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
 	/**
	 * 自定义按钮-[下发]业务
	 * @param jformTask
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doActivate")
	@ResponseBody
	public AjaxJson doActivate(JformTaskEntity jformTask, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "下发成功";
		JformTaskEntity t = jformTaskService.get(JformTaskEntity.class, jformTask.getId());
		if(t.getTaskStatus()!=ConstSetBA.TaskStatus_Draft){
			message = "工作任务不是草稿状态不能下发！";
		}else{
			try{
				String alertCode;
				if(t.getTaskType()==ConstSetBA.TaskType_Project){//项目任务
					t.setTaskStatus(ConstSetBA.TaskStatus_Send);//下发
					alertCode="SendTask";
				}else{//临时任务
					t.setTaskStatus(ConstSetBA.TaskStatus_Execution);
					alertCode ="SendTaskTemp";
				}
				Map map = new HashMap();
				map.put("task_name",t.getTaskName());
				JformProjectEntity project = systemService.getEntity(JformProjectEntity.class,t.getTaskProjectid());
				//通知项目经理-计划任务
				TuiSongMsgUtil.sendMessage("SendTaskManager",map,ResourceUtil.getSessionUser().getUserName(),project.getProjectManagerid());
				if(t.getTaskResponderid()!=null){
					//任务负责人
					for (String name : t.getTaskResponderid().split(",")) {
						TuiSongMsgUtil.sendMessage(alertCode,map,ResourceUtil.getSessionUser().getUserName(),name);
					}
				}else{
					throw new BusinessException("任务的负责人不能为空！下发失败！");
				}

				if(t.getTaskNotifierid()!=null){
					//任务抄送人
					for (String name : t.getTaskNotifierid().split(",")) {
						TuiSongMsgUtil.sendMessage(alertCode,map,ResourceUtil.getSessionUser().getUserName(),name);
					}
				}

				jformTaskService.doActivateBus(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			}catch(Exception e){
				e.printStackTrace();
				message = "下发失败";
				throw new BusinessException(e.getMessage());
			}
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 工作任务分配表新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(JformTaskEntity jformTask, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(jformTask.getId())) {
			jformTask = jformTaskService.getEntity(JformTaskEntity.class, jformTask.getId());
			req.setAttribute("jformTaskPage", jformTask);
		}
		return new ModelAndView("com/jeecg/jform_task/jformTask-add");
	}
	/**
	 * 工作任务分配表编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(JformTaskEntity jformTask, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(jformTask.getId())) {
			jformTask = jformTaskService.getEntity(JformTaskEntity.class, jformTask.getId());
			req.setAttribute("jformTaskPage", jformTask);
		}
		return new ModelAndView("com/jeecg/jform_task/jformTask-update");
	}
	
	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","jformTaskController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(JformTaskEntity jformTask,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(JformTaskEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, jformTask, request.getParameterMap());
		List<JformTaskEntity> jformTasks = this.jformTaskService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"工作任务分配表");
		modelMap.put(NormalExcelConstants.CLASS,JformTaskEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("工作任务分配表列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,jformTasks);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(JformTaskEntity jformTask,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"工作任务分配表");
    	modelMap.put(NormalExcelConstants.CLASS,JformTaskEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("工作任务分配表列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
    	"导出信息"));
    	modelMap.put(NormalExcelConstants.DATA_LIST,new ArrayList());
    	return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "importExcel", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson importExcel(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile file = entity.getValue();// 获取上传文件对象
			ImportParams params = new ImportParams();
			params.setTitleRows(2);
			params.setHeadRows(1);
			params.setNeedSave(true);
			try {
				List<JformTaskEntity> listJformTaskEntitys = ExcelImportUtil.importExcel(file.getInputStream(),JformTaskEntity.class,params);
				for (JformTaskEntity jformTask : listJformTaskEntitys) {
					jformTaskService.save(jformTask);
				}
				j.setMsg("文件导入成功！");
			} catch (Exception e) {
				j.setMsg("文件导入失败！");
				logger.error(e.getMessage());
			}finally{
				try {
					file.getInputStream().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return j;
	}
	
	
}
