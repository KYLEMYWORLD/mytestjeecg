package com.jeecg.jform_assessanaly.controller;
import com.jeecg.jform_assessanaly.entity.JformAssessanalyEntity;
import com.jeecg.jform_assessanaly.service.JformAssessanalyServiceI;
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

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.jeecgframework.core.beanvalidator.BeanValidators;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;
import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.jwt.util.GsonUtil;
import org.jeecgframework.jwt.util.ResponseMessage;
import org.jeecgframework.jwt.util.Result;
import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**   
 * @Title: Controller  
 * @Description: 绩效统计
 * @author onlineGenerator
 * @date 2019-01-29 14:19:02
 * @version V1.0   
 *
 */
@Api(value="JformAssessanaly",description="绩效统计",tags="jformAssessanalyController")
@Controller
@RequestMapping("/jformAssessanalyController")
public class JformAssessanalyController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(JformAssessanalyController.class);

	@Autowired
	private JformAssessanalyServiceI jformAssessanalyService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	


	/**
	 * 绩效统计列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/jform_assessanaly/jformAssessanalyList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(JformAssessanalyEntity jformAssessanaly,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(JformAssessanalyEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, jformAssessanaly, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.jformAssessanalyService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 删除绩效统计
	 * @param jformAssessanaly 实体类
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(JformAssessanalyEntity jformAssessanaly, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		jformAssessanaly = systemService.getEntity(JformAssessanalyEntity.class, jformAssessanaly.getId());
		message = "绩效统计删除成功";
		try{
			jformAssessanalyService.delete(jformAssessanaly);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "绩效统计删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除绩效统计
	 * @param ids
	 * @param request
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "绩效统计删除成功";
		try{
			for(String id:ids.split(",")){
				JformAssessanalyEntity jformAssessanaly = systemService.getEntity(JformAssessanalyEntity.class, 
				id
				);
				jformAssessanalyService.delete(jformAssessanaly);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "绩效统计删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加绩效统计
	 * @param jformAssessanaly
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(JformAssessanalyEntity jformAssessanaly, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "绩效统计添加成功";
		try{
			jformAssessanalyService.save(jformAssessanaly);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "绩效统计添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新绩效统计
	 * @param jformAssessanaly
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(JformAssessanalyEntity jformAssessanaly, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "绩效统计更新成功";
		JformAssessanalyEntity t = jformAssessanalyService.get(JformAssessanalyEntity.class, jformAssessanaly.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(jformAssessanaly, t);
			jformAssessanalyService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "绩效统计更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
 	/**
	 * 自定义按钮-[重新统计]业务
	 * @param jformAssessanaly
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doAnaly")
	@ResponseBody
	public AjaxJson doAnaly(JformAssessanalyEntity jformAssessanaly, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "重新统计成功";
		JformAssessanalyEntity t = jformAssessanalyService.get(JformAssessanalyEntity.class, jformAssessanaly.getId());
		try{
			jformAssessanalyService.doAnalyBus(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "重新统计失败";
		}
		j.setMsg(message);
		return j;
	}
 	/**
	 * 自定义按钮-[统计全部]业务
	 * @param jformAssessanaly
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doAnalyall")
	@ResponseBody
	public AjaxJson doAnalyall(JformAssessanalyEntity jformAssessanaly, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "统计全部成功";
		try{
			if(jformAssessanaly.getAnalyYear()==null || jformAssessanaly.getAnalyYear()==0){
				throw new BusinessException("请选择统计年份！");
			}

			if(jformAssessanaly.getAnalyMonth()==null || jformAssessanaly.getAnalyMonth()==0){
				throw new BusinessException("请选择统计月份！");
			}

			jformAssessanalyService.doAnalyallBus(jformAssessanaly);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "统计全部失败";
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 绩效统计新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(JformAssessanalyEntity jformAssessanaly, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(jformAssessanaly.getId())) {
			jformAssessanaly = jformAssessanalyService.getEntity(JformAssessanalyEntity.class, jformAssessanaly.getId());
			req.setAttribute("jformAssessanalyPage", jformAssessanaly);
		}
		return new ModelAndView("com/jeecg/jform_assessanaly/jformAssessanaly-add");
	}

	/**
	 * 绩效统计新增页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "goAnalyAll")
	public ModelAndView goAnalyAll(JformAssessanalyEntity jformAssessanaly, HttpServletRequest req) {
		return new ModelAndView("com/jeecg/jform_assessanaly/jformAssessanaly-analyall");
	}
	/**
	 * 绩效统计编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(JformAssessanalyEntity jformAssessanaly, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(jformAssessanaly.getId())) {
			jformAssessanaly = jformAssessanalyService.getEntity(JformAssessanalyEntity.class, jformAssessanaly.getId());
			req.setAttribute("jformAssessanalyPage", jformAssessanaly);
		}
		return new ModelAndView("com/jeecg/jform_assessanaly/jformAssessanaly-update");
	}
	
	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","jformAssessanalyController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(JformAssessanalyEntity jformAssessanaly,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(JformAssessanalyEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, jformAssessanaly, request.getParameterMap());
		List<JformAssessanalyEntity> jformAssessanalys = this.jformAssessanalyService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"绩效统计");
		modelMap.put(NormalExcelConstants.CLASS,JformAssessanalyEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("绩效统计列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,jformAssessanalys);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(JformAssessanalyEntity jformAssessanaly,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"绩效统计");
    	modelMap.put(NormalExcelConstants.CLASS,JformAssessanalyEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("绩效统计列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
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
				List<JformAssessanalyEntity> listJformAssessanalyEntitys = ExcelImportUtil.importExcel(file.getInputStream(),JformAssessanalyEntity.class,params);
				for (JformAssessanalyEntity jformAssessanaly : listJformAssessanalyEntitys) {
					jformAssessanalyService.save(jformAssessanaly);
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
	
	
	@RequestMapping(value="/list/{pageNo}/{pageSize}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="绩效统计列表信息",produces="application/json",httpMethod="GET")
	public ResponseMessage<List<JformAssessanalyEntity>> list(@PathVariable("pageNo") int pageNo, @PathVariable("pageSize") int pageSize, HttpServletRequest request) {
		if(pageSize > Globals.MAX_PAGESIZE){
			return Result.error("每页请求不能超过" + Globals.MAX_PAGESIZE + "条");
		}
		CriteriaQuery query = new CriteriaQuery(JformAssessanalyEntity.class);
		query.setCurPage(pageNo<=0?1:pageNo);
		query.setPageSize(pageSize<1?1:pageSize);
		List<JformAssessanalyEntity> listJformAssessanalys = this.jformAssessanalyService.getListByCriteriaQuery(query,true);
		return Result.success(listJformAssessanalys);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="根据ID获取绩效统计信息",notes="根据ID获取绩效统计信息",httpMethod="GET",produces="application/json")
	public ResponseMessage<?> get(@ApiParam(required=true,name="id",value="ID")@PathVariable("id") String id) {
		JformAssessanalyEntity task = jformAssessanalyService.get(JformAssessanalyEntity.class, id);
		if (task == null) {
			return Result.error("根据ID获取绩效统计信息为空");
		}
		return Result.success(task);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value="创建绩效统计")
	public ResponseMessage<?> create(@ApiParam(name="绩效统计对象")@RequestBody JformAssessanalyEntity jformAssessanaly, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<JformAssessanalyEntity>> failures = validator.validate(jformAssessanaly);
		if (!failures.isEmpty()) {
			return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
		}

		//保存
		try{
			jformAssessanalyService.save(jformAssessanaly);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("绩效统计信息保存失败");
		}
		return Result.success(jformAssessanaly);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value="更新绩效统计",notes="更新绩效统计")
	public ResponseMessage<?> update(@ApiParam(name="绩效统计对象")@RequestBody JformAssessanalyEntity jformAssessanaly) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<JformAssessanalyEntity>> failures = validator.validate(jformAssessanaly);
		if (!failures.isEmpty()) {
			return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
		}

		//保存
		try{
			jformAssessanalyService.saveOrUpdate(jformAssessanaly);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("更新绩效统计信息失败");
		}

		//按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
		return Result.success("更新绩效统计信息成功");
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value="删除绩效统计")
	public ResponseMessage<?> delete(@ApiParam(name="id",value="ID",required=true)@PathVariable("id") String id) {
		logger.info("delete[{}]" , id);
		// 验证
		if (StringUtils.isEmpty(id)) {
			return Result.error("ID不能为空");
		}
		try {
			jformAssessanalyService.deleteEntityById(JformAssessanalyEntity.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("绩效统计删除失败");
		}

		return Result.success();
	}
}
