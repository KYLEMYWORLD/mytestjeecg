package com.jeecg.jform_echart.controller;
import com.jeecg.jform_echart.entity.JformEchartEntity;
import com.jeecg.jform_echart.service.JformEchartServiceI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.tag.vo.datatable.SortDirection;
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
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.core.util.MyBeanUtils;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.core.util.ResourceUtil;
import java.io.IOException;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.util.Map;


/**   
 * @Title: Controller  
 * @Description: 项目看板信息表
 * @author onlineGenerator
 * @date 2018-12-28 15:58:35
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/jformEchartController")
public class JformEchartController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(JformEchartController.class);

	@Autowired
	private JformEchartServiceI jformEchartService;
	@Autowired
	private SystemService systemService;

	/**
	 * 获取项目看板信息内容
	 * @param jformEchart
	 * @param request
	 * @param dataGrid
	 * @return
	 */
	@RequestMapping(params = "GetProjectEchart")
	@ResponseBody
	//public List<JformEchartEntity> GetProjectEchart(String projectid,String partaskid){
	public Map GetProjectEchart(JformEchartEntity jformEchart,HttpServletRequest request, DataGrid dataGrid){
		List<JformEchartEntity> list;
		CriteriaQuery cq = new CriteriaQuery(JformEchartEntity.class,dataGrid);
		//指定任务
		if(!request.getParameterMap().containsKey("pretaskId")){
			//如果没有指定父任务节点则查询所有项目的一级任务
			cq.eq("taskLevel", 1);
		}
		cq.addOrder("projectId", SortDirection.desc);//项目ID
		cq.addOrder("finishDate", SortDirection.desc);//完成时间
		// 查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq,jformEchart, request.getParameterMap());
		list = this.jformEchartService.getListByCriteriaQuery(cq, false);

		Map map = new HashMap();
		map.put("data",list);
		return map;
	}




	/**
	 * 项目看板信息表列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/jform_echart/jformEchartList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(JformEchartEntity jformEchart,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(JformEchartEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, jformEchart, request.getParameterMap());
		try{
			//任务完成时间降序
			cq.addOrder("projectId", SortDirection.asc);//项目ID
//			cq.addOrder("taskId", SortDirection.asc);//任务ID
			cq.addOrder("finishDate", SortDirection.asc);//完成时间
			cq.addOrder("taskLevel", SortDirection.asc);//任务等级
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.jformEchartService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 删除项目看板信息表
	 * @param jformEchart 实体类
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(JformEchartEntity jformEchart, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		jformEchart = systemService.getEntity(JformEchartEntity.class, jformEchart.getId());
		message = "项目看板信息表删除成功";
		try{
			jformEchartService.delete(jformEchart);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "项目看板信息表删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除项目看板信息表
	 * @param ids
	 * @param request
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "项目看板信息表删除成功";
		try{
			for(String id:ids.split(",")){
				JformEchartEntity jformEchart = systemService.getEntity(JformEchartEntity.class, 
				id
				);
				jformEchartService.delete(jformEchart);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "项目看板信息表删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加项目看板信息表
	 * @param jformEchart
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(JformEchartEntity jformEchart, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "项目看板信息表添加成功";
		try{
			jformEchartService.save(jformEchart);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "项目看板信息表添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新项目看板信息表
	 * @param jformEchart
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(JformEchartEntity jformEchart, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "项目看板信息表更新成功";
		JformEchartEntity t = jformEchartService.get(JformEchartEntity.class, jformEchart.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(jformEchart, t);
			jformEchartService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "项目看板信息表更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * 项目看板信息表新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(JformEchartEntity jformEchart, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(jformEchart.getId())) {
			jformEchart = jformEchartService.getEntity(JformEchartEntity.class, jformEchart.getId());
			req.setAttribute("jformEchartPage", jformEchart);
		}
		return new ModelAndView("com/jeecg/jform_echart/jformEchart-add");
	}
	/**
	 * 项目看板信息表编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(JformEchartEntity jformEchart, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(jformEchart.getId())) {
			jformEchart = jformEchartService.getEntity(JformEchartEntity.class, jformEchart.getId());
			req.setAttribute("jformEchartPage", jformEchart);
		}
		return new ModelAndView("com/jeecg/jform_echart/jformEchart-update");
	}
	
	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","jformEchartController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(JformEchartEntity jformEchart,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(JformEchartEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, jformEchart, request.getParameterMap());
		List<JformEchartEntity> jformEcharts = this.jformEchartService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"项目看板信息表");
		modelMap.put(NormalExcelConstants.CLASS,JformEchartEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("项目看板信息表列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,jformEcharts);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(JformEchartEntity jformEchart,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"项目看板信息表");
    	modelMap.put(NormalExcelConstants.CLASS,JformEchartEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("项目看板信息表列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
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
				List<JformEchartEntity> listJformEchartEntitys = ExcelImportUtil.importExcel(file.getInputStream(),JformEchartEntity.class,params);
				for (JformEchartEntity jformEchart : listJformEchartEntitys) {
					jformEchartService.save(jformEchart);
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
