package com.jeecg.jform_attendance.controller;
import com.jeecg.jform_attendance.entity.JformAttendanceEntity;
import com.jeecg.jform_attendance.service.JformAttendanceServiceI;
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
 * @Description: 考勤信息表
 * @author onlineGenerator
 * @date 2019-01-09 08:24:41
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/jformAttendanceController")
public class JformAttendanceController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(JformAttendanceController.class);

	@Autowired
	private JformAttendanceServiceI jformAttendanceService;
	@Autowired
	private SystemService systemService;
	


	/**
	 * 考勤信息表列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/jform_attendance/jformAttendanceList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(JformAttendanceEntity jformAttendance,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(JformAttendanceEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, jformAttendance, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.jformAttendanceService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 删除考勤信息表
	 * @param jformAttendance 实体类
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(JformAttendanceEntity jformAttendance, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		jformAttendance = systemService.getEntity(JformAttendanceEntity.class, jformAttendance.getId());
		message = "考勤信息表删除成功";
		try{
			jformAttendanceService.delete(jformAttendance);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "考勤信息表删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除考勤信息表
	 * @param ids
	 * @param request
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "考勤信息表删除成功";
		try{
			for(String id:ids.split(",")){
				JformAttendanceEntity jformAttendance = systemService.getEntity(JformAttendanceEntity.class, 
				id
				);
				jformAttendanceService.delete(jformAttendance);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "考勤信息表删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加考勤信息表
	 * @param jformAttendance
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(JformAttendanceEntity jformAttendance, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "考勤信息表添加成功";
		try{
			jformAttendanceService.save(jformAttendance);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "考勤信息表添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新考勤信息表
	 * @param jformAttendance
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(JformAttendanceEntity jformAttendance, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "考勤信息表更新成功";
		JformAttendanceEntity t = jformAttendanceService.get(JformAttendanceEntity.class, jformAttendance.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(jformAttendance, t);
			jformAttendanceService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "考勤信息表更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * 考勤信息表新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(JformAttendanceEntity jformAttendance, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(jformAttendance.getId())) {
			jformAttendance = jformAttendanceService.getEntity(JformAttendanceEntity.class, jformAttendance.getId());
			req.setAttribute("jformAttendancePage", jformAttendance);
		}
		return new ModelAndView("com/jeecg/jform_attendance/jformAttendance-add");
	}
	/**
	 * 考勤信息表编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(JformAttendanceEntity jformAttendance, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(jformAttendance.getId())) {
			jformAttendance = jformAttendanceService.getEntity(JformAttendanceEntity.class, jformAttendance.getId());
			req.setAttribute("jformAttendancePage", jformAttendance);
		}
		return new ModelAndView("com/jeecg/jform_attendance/jformAttendance-update");
	}
	
	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","jformAttendanceController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(JformAttendanceEntity jformAttendance,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(JformAttendanceEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, jformAttendance, request.getParameterMap());
		List<JformAttendanceEntity> jformAttendances = this.jformAttendanceService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"考勤信息表");
		modelMap.put(NormalExcelConstants.CLASS,JformAttendanceEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("考勤信息表列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,jformAttendances);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(JformAttendanceEntity jformAttendance,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"考勤信息表");
    	modelMap.put(NormalExcelConstants.CLASS,JformAttendanceEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("考勤信息表列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
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
				List<JformAttendanceEntity> listJformAttendanceEntitys = ExcelImportUtil.importExcel(file.getInputStream(),JformAttendanceEntity.class,params);
				for (JformAttendanceEntity jformAttendance : listJformAttendanceEntitys) {
					jformAttendanceService.save(jformAttendance);
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
