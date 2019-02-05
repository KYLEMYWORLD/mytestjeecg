package com.jeecg.jform_echart.controller;
import com.jeecg.jform_echart.entity.JformEchartEntity;
import com.jeecg.jform_echart.service.JformEchartServiceI;
import java.util.ArrayList;
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
import java.util.HashMap;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.jeecgframework.core.beanvalidator.BeanValidators;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;
import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.jwt.util.ResponseMessage;
import org.jeecgframework.jwt.util.Result;
import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**   
 * @Title: Controller  
 * @Description: 项目看板信息表
 * @author onlineGenerator
 * @date 2019-01-21 14:15:57
 * @version V1.0   
 *
 */
@Api(value="JformEchart",description="项目看板信息表",tags="jformEchartController")
@Controller
@RequestMapping("/jformEchartController")
public class JformEchartController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(JformEchartController.class);

	@Autowired
	private JformEchartServiceI jformEchartService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;

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
		String sql = "select id,project_name name, project_responder resp,project_manager mana from jform_project";
		List<Map<String, Object>> result = systemService.findForJdbc(sql);

		Map map = new HashMap();
		map.put("data",list);
		map.put("project",result);
		return map;
	}
	/**
	 * 获取激活的项目数量
	 * @param jformEchart
	 * @param request
	 * @param dataGrid
	 * @return
	 */
	@RequestMapping(params = "GetProjectCount")
	@ResponseBody
	public Map GetProjectCount(JformEchartEntity jformEchart,HttpServletRequest request, DataGrid dataGrid){
		long count = systemService.getCountForJdbc("select count(id) from jform_project where project_status = 1");
		Map map = new HashMap();
		map.put("count",count);
		return map;
	}
	/**
	 * 项目看板信息表列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "echart")
	public ModelAndView echart(HttpServletRequest request) {
		return new ModelAndView("main/keda_echart");
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
		//自定义追加查询条件
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

	/**
	 * 获取激活的项目数量
	 * @return
	 */
	@RequestMapping(value = "/GetProjectCount", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="获取激活的项目数量")
	public ResponseMessage<?> GetProjectCount(){
		long count = systemService.getCountForJdbc("select count(id) from jform_project where project_status = 1");
		return Result.success(count);
	}

	/**
	 * 获取项目看板信息内容
	 * @return
	 */
	@RequestMapping(value = "/GetProjectEchart", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="获取激活的项目内容")
	public ResponseMessage<Map> GetProjectEchart(HttpServletRequest request){
		List<JformEchartEntity> list;
		CriteriaQuery query = new CriteriaQuery(JformEchartEntity.class);
		//指定任务
		if(!request.getParameterMap().containsKey("pretaskId")){
			//如果没有指定父任务节点则查询所有项目的一级任务
			query.eq("taskLevel", 1);
		}
		query.addOrder("projectId", SortDirection.desc);//项目ID
		query.addOrder("finishDate", SortDirection.desc);//完成时间
		query.add();
		list = this.jformEchartService.getListByCriteriaQuery(query, true);

		String sql = "select id,project_name name, project_responder resp,project_manager mana from jform_project";
		List<Map<String, Object>> result = systemService.findForJdbc(sql);

		Map map = new HashMap();
		map.put("data",list);
		map.put("project",result);
		return Result.success(map);
	}
	
	@RequestMapping(value="/list/{pageNo}/{pageSize}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="项目看板信息表列表信息",produces="application/json",httpMethod="GET")
	public ResponseMessage<List<JformEchartEntity>> list(@PathVariable("pageNo") int pageNo, @PathVariable("pageSize") int pageSize, HttpServletRequest request) {
		if(pageSize > Globals.MAX_PAGESIZE){
			return Result.error("每页请求不能超过" + Globals.MAX_PAGESIZE + "条");
		}
		CriteriaQuery query = new CriteriaQuery(JformEchartEntity.class);
		query.setCurPage(pageNo<=0?1:pageNo);
		query.setPageSize(pageSize<1?1:pageSize);
		List<JformEchartEntity> listJformEcharts = this.jformEchartService.getListByCriteriaQuery(query,true);
		return Result.success(listJformEcharts);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="根据ID获取项目看板信息表信息",notes="根据ID获取项目看板信息表信息",httpMethod="GET",produces="application/json")
	public ResponseMessage<?> get(@ApiParam(required=true,name="id",value="ID")@PathVariable("id") String id) {
		JformEchartEntity task = jformEchartService.get(JformEchartEntity.class, id);
		if (task == null) {
			return Result.error("根据ID获取项目看板信息表信息为空");
		}
		return Result.success(task);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value="创建项目看板信息表")
	public ResponseMessage<?> create(@ApiParam(name="项目看板信息表对象")@RequestBody JformEchartEntity jformEchart, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<JformEchartEntity>> failures = validator.validate(jformEchart);
		if (!failures.isEmpty()) {
			return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
		}

		//保存
		try{
			jformEchartService.save(jformEchart);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("项目看板信息表信息保存失败");
		}
		return Result.success(jformEchart);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value="更新项目看板信息表",notes="更新项目看板信息表")
	public ResponseMessage<?> update(@ApiParam(name="项目看板信息表对象")@RequestBody JformEchartEntity jformEchart) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<JformEchartEntity>> failures = validator.validate(jformEchart);
		if (!failures.isEmpty()) {
			return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
		}

		//保存
		try{
			jformEchartService.saveOrUpdate(jformEchart);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("更新项目看板信息表信息失败");
		}

		//按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
		return Result.success("更新项目看板信息表信息成功");
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value="删除项目看板信息表")
	public ResponseMessage<?> delete(@ApiParam(name="id",value="ID",required=true)@PathVariable("id") String id) {
		logger.info("delete[{}]" , id);
		// 验证
		if (StringUtils.isEmpty(id)) {
			return Result.error("ID不能为空");
		}
		try {
			jformEchartService.deleteEntityById(JformEchartEntity.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("项目看板信息表删除失败");
		}

		return Result.success();
	}
}
