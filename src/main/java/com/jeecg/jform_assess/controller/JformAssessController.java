package com.jeecg.jform_assess.controller;
import com.jeecg.jform_assess.entity.JformAssessEntity;
import com.jeecg.jform_assess.service.JformAssessServiceI;
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
 * @Description: 绩效考核
 * @author onlineGenerator
 * @date 2019-01-28 17:12:01
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/jformAssessController")
public class JformAssessController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(JformAssessController.class);

	@Autowired
	private JformAssessServiceI jformAssessService;
	@Autowired
	private SystemService systemService;
	


	/**
	 * 绩效考核列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/jform_assess/jformAssessList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(JformAssessEntity jformAssess,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(JformAssessEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, jformAssess, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.jformAssessService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 删除绩效考核
	 * @param jformAssess 实体类
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(JformAssessEntity jformAssess, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		jformAssess = systemService.getEntity(JformAssessEntity.class, jformAssess.getId());
		message = "绩效考核删除成功";
		try{
			jformAssessService.delete(jformAssess);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "绩效考核删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除绩效考核
	 * @param ids
	 * @param request
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "绩效考核删除成功";
		try{
			for(String id:ids.split(",")){
				JformAssessEntity jformAssess = systemService.getEntity(JformAssessEntity.class, 
				id
				);
				jformAssessService.delete(jformAssess);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "绩效考核删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加绩效考核
	 * @param jformAssess
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(JformAssessEntity jformAssess, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "绩效考核添加成功";
		try{
			jformAssessService.save(jformAssess);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "绩效考核添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新绩效考核
	 * @param jformAssess
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(JformAssessEntity jformAssess, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "绩效考核更新成功";
		JformAssessEntity t = jformAssessService.get(JformAssessEntity.class, jformAssess.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(jformAssess, t);
			jformAssessService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "绩效考核更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
 	/**
	 * 自定义按钮-[评分]业务
	 * @param jformAssess
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doScore")
	@ResponseBody
	public AjaxJson doScore(JformAssessEntity jformAssess, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "评分成功";
		JformAssessEntity t = jformAssessService.get(JformAssessEntity.class, jformAssess.getId());
		try{
			jformAssessService.doScoreBus(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "评分失败";
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 绩效考核新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(JformAssessEntity jformAssess, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(jformAssess.getId())) {
			jformAssess = jformAssessService.getEntity(JformAssessEntity.class, jformAssess.getId());
			req.setAttribute("jformAssessPage", jformAssess);
		}
		return new ModelAndView("com/jeecg/jform_assess/jformAssess-add");
	}
	/**
	 * 绩效考核编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(JformAssessEntity jformAssess, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(jformAssess.getId())) {
			jformAssess = jformAssessService.getEntity(JformAssessEntity.class, jformAssess.getId());
			req.setAttribute("jformAssessPage", jformAssess);
		}
		return new ModelAndView("com/jeecg/jform_assess/jformAssess-update");
	}
	
	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","jformAssessController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(JformAssessEntity jformAssess,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(JformAssessEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, jformAssess, request.getParameterMap());
		List<JformAssessEntity> jformAssesss = this.jformAssessService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"绩效考核");
		modelMap.put(NormalExcelConstants.CLASS,JformAssessEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("绩效考核列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,jformAssesss);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(JformAssessEntity jformAssess,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"绩效考核");
    	modelMap.put(NormalExcelConstants.CLASS,JformAssessEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("绩效考核列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
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
				List<JformAssessEntity> listJformAssessEntitys = ExcelImportUtil.importExcel(file.getInputStream(),JformAssessEntity.class,params);
				for (JformAssessEntity jformAssess : listJformAssessEntitys) {
					jformAssessService.save(jformAssess);
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
