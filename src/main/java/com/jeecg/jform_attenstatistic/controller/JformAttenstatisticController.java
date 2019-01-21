package com.jeecg.jform_attenstatistic.controller;
import com.jeecg.jform_attenstatistic.entity.JformAttenstatisticEntity;
import com.jeecg.jform_attenstatistic.service.JformAttenstatisticServiceI;
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
 * @Description: 考勤统计表
 * @author onlineGenerator
 * @date 2019-01-09 14:06:29
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/jformAttenstatisticController")
public class JformAttenstatisticController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(JformAttenstatisticController.class);

	@Autowired
	private JformAttenstatisticServiceI jformAttenstatisticService;
	@Autowired
	private SystemService systemService;
	


	/**
	 * 考勤统计表列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/jform_attenstatistic/jformAttenstatisticList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(JformAttenstatisticEntity jformAttenstatistic,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(JformAttenstatisticEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, jformAttenstatistic, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.jformAttenstatisticService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 删除考勤统计表
	 * @param jformAttenstatistic 实体类
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(JformAttenstatisticEntity jformAttenstatistic, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		jformAttenstatistic = systemService.getEntity(JformAttenstatisticEntity.class, jformAttenstatistic.getId());
		message = "考勤统计表删除成功";
		try{
			jformAttenstatisticService.delete(jformAttenstatistic);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "考勤统计表删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除考勤统计表
	 * @param ids
	 * @param request
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "考勤统计表删除成功";
		try{
			for(String id:ids.split(",")){
				JformAttenstatisticEntity jformAttenstatistic = systemService.getEntity(JformAttenstatisticEntity.class, 
				id
				);
				jformAttenstatisticService.delete(jformAttenstatistic);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "考勤统计表删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加考勤统计表
	 * @param jformAttenstatistic
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(JformAttenstatisticEntity jformAttenstatistic, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "考勤统计表添加成功";
		try{
			jformAttenstatisticService.save(jformAttenstatistic);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "考勤统计表添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新考勤统计表
	 * @param jformAttenstatistic
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(JformAttenstatisticEntity jformAttenstatistic, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "考勤统计表更新成功";
		JformAttenstatisticEntity t = jformAttenstatisticService.get(JformAttenstatisticEntity.class, jformAttenstatistic.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(jformAttenstatistic, t);
			jformAttenstatisticService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "考勤统计表更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
 	/**
	 * 自定义按钮-[重新计算]业务
	 * @param jformAttenstatistic
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doRecount")
	@ResponseBody
	public AjaxJson doRecount(JformAttenstatisticEntity jformAttenstatistic, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "重新计算成功";
		JformAttenstatisticEntity t = jformAttenstatisticService.get(JformAttenstatisticEntity.class, jformAttenstatistic.getId());
		try{
			jformAttenstatisticService.doRecountBus(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "重新计算失败";
		}
		j.setMsg(message);
		return j;
	}
 	/**
	 * 自定义按钮-[重算全部]业务
	 * @param jformAttenstatistic
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doRecountall")
	@ResponseBody
	public AjaxJson doRecountall(JformAttenstatisticEntity jformAttenstatistic, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "重算全部成功";
		JformAttenstatisticEntity t = jformAttenstatisticService.get(JformAttenstatisticEntity.class, jformAttenstatistic.getId());
		try{
			jformAttenstatisticService.doRecountallBus(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "重算全部失败";
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 考勤统计表新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(JformAttenstatisticEntity jformAttenstatistic, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(jformAttenstatistic.getId())) {
			jformAttenstatistic = jformAttenstatisticService.getEntity(JformAttenstatisticEntity.class, jformAttenstatistic.getId());
			req.setAttribute("jformAttenstatisticPage", jformAttenstatistic);
		}
		return new ModelAndView("com/jeecg/jform_attenstatistic/jformAttenstatistic-add");
	}
	/**
	 * 考勤统计表编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(JformAttenstatisticEntity jformAttenstatistic, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(jformAttenstatistic.getId())) {
			jformAttenstatistic = jformAttenstatisticService.getEntity(JformAttenstatisticEntity.class, jformAttenstatistic.getId());
			req.setAttribute("jformAttenstatisticPage", jformAttenstatistic);
		}
		return new ModelAndView("com/jeecg/jform_attenstatistic/jformAttenstatistic-update");
	}
	
	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","jformAttenstatisticController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(JformAttenstatisticEntity jformAttenstatistic,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(JformAttenstatisticEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, jformAttenstatistic, request.getParameterMap());
		List<JformAttenstatisticEntity> jformAttenstatistics = this.jformAttenstatisticService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"考勤统计表");
		modelMap.put(NormalExcelConstants.CLASS,JformAttenstatisticEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("考勤统计表列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,jformAttenstatistics);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(JformAttenstatisticEntity jformAttenstatistic,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"考勤统计表");
    	modelMap.put(NormalExcelConstants.CLASS,JformAttenstatisticEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("考勤统计表列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
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
				List<JformAttenstatisticEntity> listJformAttenstatisticEntitys = ExcelImportUtil.importExcel(file.getInputStream(),JformAttenstatisticEntity.class,params);
				for (JformAttenstatisticEntity jformAttenstatistic : listJformAttenstatisticEntitys) {
					jformAttenstatisticService.save(jformAttenstatistic);
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
