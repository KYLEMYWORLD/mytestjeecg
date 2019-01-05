package com.jeecg.jform_project.controller;
import com.jeecg.ConstSetBA;
import com.jeecg.jform_echart.entity.JformEchartEntity;
import com.jeecg.jform_project.entity.JformProjectEntity;
import com.jeecg.jform_project.service.JformProjectServiceI;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
 * @Description: 项目信息表
 * @author onlineGenerator
 * @date 2018-12-05 16:45:59
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/jformProjectController")
public class JformProjectController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(JformProjectController.class);

	@Autowired
	private JformProjectServiceI jformProjectService;
	@Autowired
	private SystemService systemService;

	/**
	 * 项目信息表列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/jform_project/jformProjectList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(JformProjectEntity jformProject,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(JformProjectEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, jformProject, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.jformProjectService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 删除项目信息表
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(JformProjectEntity jformProject, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		jformProject = systemService.getEntity(JformProjectEntity.class, jformProject.getId());
		message = "项目信息表删除成功";
		if(jformProject.getProjectStatus()== ConstSetBA.ProjectStatus_Activate) {
			message = "项目信息已激活，不能删除！";
			throw new BusinessException("项目信息已激活，不能删除！");
		}else{
			try{
				jformProjectService.delete(jformProject);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}catch(Exception e){
				e.printStackTrace();
				message = "项目信息表删除失败";
				throw new BusinessException(e.getMessage());
			}
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除项目信息表
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "项目信息表删除成功";
		try{
			for(String id:ids.split(",")){
				JformProjectEntity jformProject = systemService.getEntity(JformProjectEntity.class,id);
				if(jformProject.getProjectStatus()==ConstSetBA.ProjectStatus_Activate){
					systemService.addLog(jformProject.getProjectName()+"已经激活不能删除！", Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
					continue;
				}
				jformProjectService.delete(jformProject);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "项目信息表删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/***
	 * 添加项目信息表
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(JformProjectEntity jformProject, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "项目信息表添加成功";
		try{
			jformProject.setProjectStatus(ConstSetBA.ProjectStatus_Unactivate);
			jformProjectService.save(jformProject);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "项目信息表添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/***
	 * 更新项目信息表
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(JformProjectEntity jformProject, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "项目信息表更新成功";
		JformProjectEntity t = jformProjectService.get(JformProjectEntity.class, jformProject.getId());
//		if(t.getProjectStatus()==ConstSetBA.ProjectStatus_Activate){
//			message = "项目信息已激活不能更改";
//			throw new BusinessException("项目信息已激活不能更改");
//		}else{
			try {
				MyBeanUtils.copyBeanNotNull2Bean(jformProject, t);
				jformProjectService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = "项目信息表更新失败";
				throw new BusinessException(e.getMessage());
			}
//		}
		j.setMsg(message);
		return j;
	}

	/***
	 * 自定义按钮-[激活]业务
	 * @param jformProject
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doActivate")
	@ResponseBody
	public AjaxJson doActivate(JformProjectEntity jformProject, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "激活成功";
		JformProjectEntity t = jformProjectService.get(JformProjectEntity.class, jformProject.getId());
		try{
			if(t.getProjectStatus()==ConstSetBA.ProjectStatus_Unactivate){
				t.setProjectStatus(ConstSetBA.ProjectStatus_Activate);
				jformProjectService.doActivateBus(t);
				JformEchartEntity e_begin = new JformEchartEntity();
				e_begin.setProjectId(t.getId());
				e_begin.setTaskId(t.getId());
				e_begin.setTaskLevel(1);
				e_begin.setAlertStatus(1);
				e_begin.setTaskStatus(1);//任务状态
				e_begin.setTaskName("项目开始");
				e_begin.setTaskShortname("项目开始");
				e_begin.setStartDate(t.getStartDate());
				e_begin.setFinishDate(t.getStartDate());
				e_begin.setRfinishDate(t.getStartDate());

				JformEchartEntity e_end = new JformEchartEntity();
				e_end.setProjectId(t.getId());
				e_end.setTaskId(t.getId());
				e_end.setTaskLevel(1);
				e_end.setAlertStatus(1);
				e_end.setTaskStatus(0);//任务状态
				e_end.setTaskName("项目结束");
				e_end.setTaskShortname("项目结束");
				e_end.setStartDate(t.getFinishDate());
				e_end.setFinishDate(t.getFinishDate());
				e_end.setRfinishDate(t.getFinishDate());

				systemService.save(e_begin);
				systemService.save(e_end);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			}else{
				message = "项目已经激活！";
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "激活失败";
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 项目信息表新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(JformProjectEntity jformProject, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(jformProject.getId())) {
			jformProject = jformProjectService.getEntity(JformProjectEntity.class, jformProject.getId());
			req.setAttribute("jformProjectPage", jformProject);
		}
		return new ModelAndView("com/jeecg/jform_project/jformProject-add");
	}
	/**
	 * 项目信息表编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(JformProjectEntity jformProject, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(jformProject.getId())) {
			jformProject = jformProjectService.getEntity(JformProjectEntity.class, jformProject.getId());
			req.setAttribute("jformProjectPage", jformProject);
		}
		return new ModelAndView("com/jeecg/jform_project/jformProject-update");
	}
	
	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","jformProjectController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(JformProjectEntity jformProject,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(JformProjectEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, jformProject, request.getParameterMap());
		List<JformProjectEntity> jformProjects = this.jformProjectService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"项目信息表");
		modelMap.put(NormalExcelConstants.CLASS,JformProjectEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("项目信息表列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,jformProjects);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(JformProjectEntity jformProject,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"项目信息表");
    	modelMap.put(NormalExcelConstants.CLASS,JformProjectEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("项目信息表列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
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
				List<JformProjectEntity> listJformProjectEntitys = ExcelImportUtil.importExcel(file.getInputStream(),JformProjectEntity.class,params);
				for (JformProjectEntity jformProject : listJformProjectEntitys) {
					jformProjectService.save(jformProject);
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
