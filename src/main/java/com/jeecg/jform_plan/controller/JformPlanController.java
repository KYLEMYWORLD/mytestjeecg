package com.jeecg.jform_plan.controller;
import com.jeecg.ConstSetBA;
import com.jeecg.jform_plan.entity.JformPlanEntity;
import com.jeecg.jform_plan.service.JformPlanServiceI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeecg.jform_project.entity.JformProjectEntity;
import com.jeecg.jform_task.entity.JformTaskEntity;
import jodd.servlet.HttpServletContextMap;
import org.jeecgframework.codegenerate.extcommon.onetomany.CgformCodeOne2ManyExtCommonGenerate;
import org.jeecgframework.core.common.service.CommonService;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.tag.vo.datatable.SortDirection;
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
 * @Description: 项目任务计划表
 * @author onlineGenerator
 * @date 2018-12-15 08:20:55
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/jformPlanController")
public class JformPlanController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(JformPlanController.class);

	@Autowired
	private JformPlanServiceI jformPlanService;
	@Autowired
	private SystemService systemService;
	


	/**
	 * 项目任务计划表列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/jform_plan/jformPlanList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(JformPlanEntity jformPlan,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(JformPlanEntity.class, dataGrid);
		if(StringUtil.isEmpty(jformPlan.getId())){
			cq.isNull("planId");
		}else{
			cq.eq("planId", jformPlan.getId());
			jformPlan.setId(null);
		}
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, jformPlan, request.getParameterMap());
		try{
			cq.addOrder("startDate", SortDirection.asc);
		//自定义追加查询条件
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.jformPlanService.getDataGridReturn(cq, true);
		TagUtil.treegrid(response, dataGrid);
	}
	
	/**
	 * 删除项目任务计划表
	 * @param jformPlan 实体类
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(JformPlanEntity jformPlan, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		jformPlan = systemService.getEntity(JformPlanEntity.class, jformPlan.getId());
		message = "项目任务计划表删除成功";
		try{
			if(jformPlan.getPlanStatus()!=ConstSetBA.PlanStatus_Draft &&
				jformPlan.getPlanStatus()!=ConstSetBA.PlanStatus_SendDivide &&
				jformPlan.getPlanStatus()!=ConstSetBA.PlanStatus_SendDivideFinish){
				throw new BusinessException("任务不是草稿，下发细分，细分完成状态，不能删除！");
			}
			if(jformPlan.getPlanLevel()==ConstSetBA.PlanLevel_First){
				//恢复分配任务的下发状态
				JformTaskEntity task = systemService.getEntity(JformTaskEntity.class,jformPlan.getTaskId());
				task.setTaskStatus(ConstSetBA.TaskStatus_Send);
				systemService.save(task);
			}
			jformPlanService.delete(jformPlan);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "项目任务计划表删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除项目任务计划表
	 * @param ids
	 * @param request
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "项目任务计划表删除成功";
		try{
			for(String id:ids.split(",")){
				JformPlanEntity jformPlan = systemService.getEntity(JformPlanEntity.class, id);
				if(jformPlan.getPlanStatus()!=ConstSetBA.PlanStatus_Draft &&
						jformPlan.getPlanStatus()!=ConstSetBA.PlanStatus_SendDivide &&
						jformPlan.getPlanStatus()!=ConstSetBA.PlanStatus_SendDivideFinish){
					continue;
				}
				if(jformPlan.getPlanLevel()==ConstSetBA.PlanLevel_First){
					//恢复分配任务的下发状态
					JformTaskEntity task = systemService.getEntity(JformTaskEntity.class,jformPlan.getTaskId());
					task.setTaskStatus(ConstSetBA.TaskStatus_Send);
					systemService.save(task);
				}
				jformPlanService.delete(jformPlan);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "项目任务计划表删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加项目任务计划表
	 * @param jformPlan
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(JformPlanEntity jformPlan, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "项目任务计划表添加成功";
		try{
			if(StringUtil.isEmpty(jformPlan.getPlanId())){
				jformPlan.setPlanId(null);
			}
			//一级任务将分配任务下发状态变成分配状态
			if(jformPlan.getPlanLevel() == ConstSetBA.PlanLevel_First){
				JformTaskEntity task = systemService.getEntity(JformTaskEntity.class,jformPlan.getTaskId());
				if(task.getTaskStatus()==ConstSetBA.TaskStatus_Send){
					task.setTaskStatus(ConstSetBA.TaskStatus_Assign);
				}else{
					throw new BusinessException("该分配任务状态不是下发状态不能进行分配！");
				}
				systemService.save(task);
			}
			jformPlanService.save(jformPlan);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "项目任务计划表添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新项目任务计划表
	 * @param jformPlan
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(JformPlanEntity jformPlan, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "项目任务计划表更新成功";
		JformPlanEntity t = jformPlanService.get(JformPlanEntity.class, jformPlan.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(jformPlan, t);
			if(StringUtil.isEmpty(t.getPlanId())){
				t.setPlanId(null);
			}
			jformPlanService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "项目任务计划表更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
 	/**
	 * 自定义按钮-[下发细分]业务
	 * @param jformPlan
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doSendDivide")
	@ResponseBody
	public AjaxJson doSendDivide(JformPlanEntity jformPlan, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "下发细分操作成功";
		JformPlanEntity t = jformPlanService.get(JformPlanEntity.class, jformPlan.getId());
		try{
			if(t.getPlanLevel()!=ConstSetBA.PlanLevel_Second){
				throw new BusinessException("非二级任务不能下发细分操作！");
			}
			t.setPlanStatus(ConstSetBA.PlanStatus_SendDivide);
			Map map = new HashMap();
			map.put("plan_name",t.getPlanName());
			//下发细分通知
			for (String name:t.getPlanResponderid().split(",")) {
				TuiSongMsgUtil.sendMessage("SendDivide",map,ResourceUtil.getSessionUser().getUserName(),name);
			}
			jformPlanService.doSendDivideBus(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "下发细分失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
 	/**
	 * 自定义按钮-[细分完成]业务
	 * @param jformPlan
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doDivideFinish")
	@ResponseBody
	public AjaxJson doDivideFinish(JformPlanEntity jformPlan, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "细分完成成功";
		JformPlanEntity t = jformPlanService.get(JformPlanEntity.class, jformPlan.getId());
		try{
			if(t.getPlanLevel()!=ConstSetBA.PlanLevel_Second){
				throw new BusinessException("非二级任务不能下发细分完成操作！");
			}
			Long count = systemService.getCountForJdbc("select count(*) from jform_plan t where t.plan_id='"+jformPlan.getId()+"';");
			if(count==0){
				throw new BusinessException("当前二级任务下面没有细分的三级任务，不能进行细分完成！");
			}
			t.setPlanStatus(ConstSetBA.PlanStatus_SendDivideFinish);
			jformPlanService.doDivideFinishBus(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "细分完成失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
 	/**
	 * 自定义按钮-[提交审批]业务
	 * 将计划状态调整为提交审批同时发送提示消息给部门经理
	 * @param jformPlan
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doSendApprove")
	@ResponseBody
	public AjaxJson doSendApprove(JformPlanEntity jformPlan, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "提交审批成功";
		JformPlanEntity t = jformPlanService.get(JformPlanEntity.class, jformPlan.getId());
		if(t.getPlanLevel()!=ConstSetBA.PlanLevel_First){
			throw new BusinessException("不是一级任务不能提交审批！");
		}else if(t.getPlanStatus()!= ConstSetBA.PlanStatus_Draft && t.getPlanStatus() !=ConstSetBA.PlanStatus_Disapprove){
			throw new BusinessException("该任务状态不能提交审批！");
		}else{
			try{
				//如果一级任务下的二级任务都是细分完成状态则可以提交审批
				CheckUnderPlanStatus(t.getId());//检查任务状态是否满足提交审批
				t.setPlanStatus(ConstSetBA.PlanStatus_SendApprove);

				JformProjectEntity project = systemService.getEntity(JformProjectEntity.class,t.getProjectId());
				Map map = new HashMap();
				map.put("plan_name",t.getPlanName());
				map.put("project_name",project.getProjectName());
				//发送审批信息
				for (String name :project.getProjectResponderid().split(",")) {
					TuiSongMsgUtil.sendMessage("SendApprove",map,ResourceUtil.getSessionUser().getUserName(),name);
				}
				UpdatePlanStatus(t.getId(),ConstSetBA.PlanStatus_SendApprove);
				jformPlanService.doSendApproveBus(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			}catch(Exception e){
				e.printStackTrace();
				message = "提交审批失败";
				throw new BusinessException(e.getMessage());
			}
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 检查一级任务下的二级任务是否都是细分完成状态。
	 * @param planid
	 */
	public void CheckUnderPlanStatus(String planid){
		List<JformPlanEntity> jformlist = systemService.findByQueryString("from JformPlanEntity where planId = '"+planid+"'");
		if(jformlist==null || jformlist.size() ==0) throw new BusinessException("找不到一级任务下面的二级任务列表！");
		for (JformPlanEntity plan:jformlist) {
			if(plan.getPlanStatus()!=ConstSetBA.PlanStatus_SendDivideFinish
					&& plan.getPlanStatus()!=ConstSetBA.PlanStatus_Disapprove){
				throw new BusinessException("二级任务:"+plan.getPlanName()+",不是细分完成状态，不能进行审批！");
			}
		}
	}

	/**
	 * 更新一级任务ID下面的所有二级，三级任务的状态为 status
	 * @param id		任务ID
	 * @param status	任务状态
	 */
	public void UpdatePlanStatus(String id,int status){
		String sql = "update jform_plan"+
						" set plan_status = "+status+
						" where plan_id in (select a.id from(select p.id from jform_plan p where p.plan_id='"+id+"')a)"+
						" or plan_id = '"+id+"'";
		systemService.updateBySqlString(sql);
	}

	@RequestMapping(params = "doApprove")
	@ResponseBody
	public AjaxJson doApprove(String content,String id,String approvetype) {
		logger.info("-------审核意见:"+content);//demo简单作打印,实际项目可酌情处理
		String message = null;
		AjaxJson j = new AjaxJson();
		JformPlanEntity t = systemService.getEntity(JformPlanEntity.class, id);
		message = "审核成功";
		try{
			String msgCode;
			Map map = new HashMap();
			map.put("plan_name",t.getPlanName());
			map.put("content",content);
			if(approvetype.equals("2")){
				t.setPlanStatus(ConstSetBA.PlanStatus_Disapprove);
				msgCode = "DisApprove";
				UpdatePlanStatus(t.getId(),ConstSetBA.PlanStatus_Disapprove);
			}else{
				t.setPlanStatus(ConstSetBA.PlanStatus_Execution);
				msgCode = "Approve";
				UpdatePlanStatus(t.getId(),ConstSetBA.PlanStatus_Execution);
			}
			JformProjectEntity project = systemService.getEntity(JformProjectEntity.class,t.getProjectId());
			TuiSongMsgUtil.sendMessage(msgCode,map,ResourceUtil.getSessionUser().getUserName(),project.getProjectManagerid());
			t.setPlanRejectmsg(content);

			this.jformPlanService.updateEntitie(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "审核失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 项目任务计划新增二级，三级任务
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(JformPlanEntity jformPlan, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(jformPlan.getId())) {
			jformPlan = jformPlanService.getEntity(JformPlanEntity.class, jformPlan.getId());
			req.setAttribute("jformPlanPage", jformPlan);
		}
		return new ModelAndView("com/jeecg/jform_plan/jformPlan-add");
	}


	/**
	 * 项目任务计划表新增页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "goAddFirst")
	public ModelAndView goAddFirst(JformPlanEntity jformPlan, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(jformPlan.getId())) {
			jformPlan = jformPlanService.getEntity(JformPlanEntity.class, jformPlan.getId());
			req.setAttribute("jformPlanPage", jformPlan);
		}
		return new ModelAndView("com/jeecg/jform_plan/jformPlan-addfirst");
	}

	/**
	 * 项目任务计划表编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(JformPlanEntity jformPlan, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(jformPlan.getId())) {
			jformPlan = jformPlanService.getEntity(JformPlanEntity.class, jformPlan.getId());
			req.setAttribute("jformPlanPage", jformPlan);
		}
		return new ModelAndView("com/jeecg/jform_plan/jformPlan-update");
	}

	/**
	 * 项目任务计划表编辑页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "goApprove")
	public ModelAndView goApprove(JformPlanEntity jformPlan, HttpServletRequest req) {
		logger.info("----审核-----");
		String id=req.getParameter("id");
		if (StringUtil.isNotEmpty(id)) {
			JformPlanEntity t = systemService.getEntity(JformPlanEntity.class, id);
			req.setAttribute("jformPlanPage", t);
		}
		return new ModelAndView("com/jeecg/jform_plan/jformPlan-approve");
	}



	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","jformPlanController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(JformPlanEntity jformPlan,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(JformPlanEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, jformPlan, request.getParameterMap());
		List<JformPlanEntity> jformPlans = this.jformPlanService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"项目任务计划表");
		modelMap.put(NormalExcelConstants.CLASS,JformPlanEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("项目任务计划表列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,jformPlans);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(JformPlanEntity jformPlan,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"项目任务计划表");
    	modelMap.put(NormalExcelConstants.CLASS,JformPlanEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("项目任务计划表列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
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
				List<JformPlanEntity> listJformPlanEntitys = ExcelImportUtil.importExcel(file.getInputStream(),JformPlanEntity.class,params);
				for (JformPlanEntity jformPlan : listJformPlanEntitys) {
					jformPlanService.save(jformPlan);
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
