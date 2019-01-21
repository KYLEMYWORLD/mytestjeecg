package com.jeecg.jform_user_plan.controller;

import com.jeecg.ConstSetBA;
import com.jeecg.jform_echart.entity.JformEchartEntity;
import com.jeecg.jform_plan.entity.JformPlanEntity;
import com.jeecg.jform_user_plan.entity.JformUserPlanEntity;
import com.jeecg.jform_user_plan.service.JformUserPlanServiceI;

import java.util.*;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.xml.internal.bind.v2.TODO;
import org.jeecgframework.tag.vo.datatable.SortDirection;
import org.jeecgframework.web.system.pojo.base.TSUser;
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
import org.jeecgframework.core.util.ExceptionUtil;


/**
 * @author onlineGenerator
 * @version V1.0
 * @Title: Controller
 * @Description: 个人计划安排
 * @date 2019-01-14 10:05:04
 */
@Controller
@RequestMapping("/jformUserPlanController")
public class JformUserPlanController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(JformUserPlanController.class);

    @Autowired
    private JformUserPlanServiceI jformUserPlanService;
    @Autowired
    private SystemService systemService;


    /**
     * 个人计划安排列表 页面跳转
     *
     * @return
     */
    @RequestMapping(params = "list")
    public ModelAndView list(HttpServletRequest request) {
        return new ModelAndView("com/jeecg/jform_user_plan/jformUserPlanList");
    }

    /**
     * 个人计划反馈 页面跳转
     *
     * @return
     */
    @RequestMapping(params = "feedbacklist")
    public ModelAndView feedbacklist(HttpServletRequest request) {
        return new ModelAndView("com/jeecg/jform_user_plan/jformUserPlanFeedbackList");
    }

    /**
     * easyui AJAX请求数据
     *
     * @param request
     * @param response
     * @param dataGrid
     */

    @RequestMapping(params = "datagrid")
    public void datagrid(JformUserPlanEntity jformUserPlan, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(JformUserPlanEntity.class, dataGrid);
        if (StringUtil.isEmpty(jformUserPlan.getId())) {
            cq.isNull("planId");
        } else {
            cq.eq("planId", jformUserPlan.getId());
            jformUserPlan.setId(null);
        }
        //查询条件组装器
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, jformUserPlan, request.getParameterMap());
        try {
            //自定义追加查询条件
            cq.addOrder("startDate", SortDirection.asc);
            cq.addOrder("finishDate", SortDirection.asc);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        cq.add();
        this.jformUserPlanService.getDataGridReturn(cq, true);
        TagUtil.treegrid(response, dataGrid);
    }

    /**
     * 个人任务反馈状态过滤
     */
    Integer[] planStatusFilter = new Integer[]{ConstSetBA.PlanStatus_Execution, ConstSetBA.PlanStatus_Finish};

    /**
     * 个人任务反馈级别过滤
     */
    Integer[] levelFilter = new Integer[]{ConstSetBA.PlanLevel_Third, ConstSetBA.PlanLevel_User};

    /**
     * easyui AJAX请求数据
     *
     * @param request
     * @param response
     * @param dataGrid
     */
    @RequestMapping(params = "feedbackdatagrid")
    public void feedbackdatagrid(JformUserPlanEntity jformUserPlan, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(JformUserPlanEntity.class, dataGrid);
		if(StringUtil.isEmpty(jformUserPlan.getId())){
			cq.isNull("planId");
		}else{
			cq.eq("planId", jformUserPlan.getId());
			jformUserPlan.setId(null);
		}
        //查询条件组装器
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, jformUserPlan, request.getParameterMap());
        try {
            //自定义追加查询条件
            cq.addOrder("startDate", SortDirection.asc);
            cq.addOrder("finishDate", SortDirection.asc);
            cq.in("planLevel", levelFilter);
            cq.in("planStatus", planStatusFilter);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        cq.add();
        this.jformUserPlanService.getDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * 删除个人计划安排
     *
     * @param jformUserPlan 实体类
     * @param request
     * @return
     */
    @RequestMapping(params = "doDel")
    @ResponseBody
    public AjaxJson doDel(JformUserPlanEntity jformUserPlan, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        jformUserPlan = systemService.getEntity(JformUserPlanEntity.class, jformUserPlan.getId());
        message = "个人计划安排删除成功";
        try {
            if (jformUserPlan.getPlanStatus() != ConstSetBA.PlanStatus_Draft) {
                throw new BusinessException("任务不是草稿状态不能删除!");
            }

            if (jformUserPlan.getPlanLevel() == ConstSetBA.PlanLevel_Second) {
                throw new BusinessException("二级任务只能由项目经理删除！");
            }

            if (jformUserPlan.getPlanLevel() == ConstSetBA.PlanLevel_Third) {
                //三级任务
                List<JformPlanEntity> entity = systemService.findHql("from JformPlanEntity e where e.userPlanid = ?", jformUserPlan.getId());
                systemService.deleteAllEntitie(entity);
            }

            jformUserPlanService.delete(jformUserPlan);
            systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 批量删除个人计划安排
     *
     * @param ids
     * @param request
     * @return
     */
    @RequestMapping(params = "doBatchDel")
    @ResponseBody
    public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "个人计划安排删除成功";
        try {
            for (String id : ids.split(",")) {
                JformUserPlanEntity jformUserPlan = systemService.getEntity(JformUserPlanEntity.class, id);
                jformUserPlanService.delete(jformUserPlan);
                systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "个人计划安排删除失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }


    /**
     * 添加个人计划安排
     *
     * @param jformUserPlan
     * @param request
     * @return
     */
    @RequestMapping(params = "doAdd")
    @ResponseBody
    public AjaxJson doAdd(JformUserPlanEntity jformUserPlan, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "个人计划安排添加成功";
        try {
            if (StringUtil.isEmpty(jformUserPlan.getPlanId())) {
                jformUserPlan.setPlanId(null);
            }
            if (jformUserPlan.getPlanLevel() == ConstSetBA.PlanLevel_Third) {
                //三级任务 同步到项目计划中
                JformUserPlanEntity seconPlan = jformUserPlanService.get(JformUserPlanEntity.class, jformUserPlan.getPlanId());
                jformUserPlan.setProjectId(seconPlan.getProjectId());
                jformUserPlan.setPlanStatus(ConstSetBA.PlanStatus_Draft);//草稿
                jformUserPlan.setPlanIssucc(ConstSetBA.PlanIsSuccess_NO);//未完成
                jformUserPlan.setPlanIsalert(ConstSetBA.PlanIsAlert_NO);//未预警
                JformPlanEntity entity = new JformPlanEntity();
                entity.setPlanId(seconPlan.getTaskId());
                entity.setUserPlanid(jformUserPlan.getId());
                CopyUserPlanToPlan(jformUserPlan, entity);//复制给jform_plan
                systemService.save(entity);
                jformUserPlan.setTaskId(entity.getId());
                jformUserPlanService.save(jformUserPlan);

                //更新UserPlanId
                entity.setUserPlanid(jformUserPlan.getId());
                systemService.saveOrUpdate(entity);
            } else {
                //个人任务
                jformUserPlan.setPlanStatus(ConstSetBA.PlanStatus_Execution);
                jformUserPlan.setPlanIsalert(ConstSetBA.PlanIsAlert_NO);
                jformUserPlan.setPlanIssucc(ConstSetBA.PlanIsSuccess_NO);
                jformUserPlanService.save(jformUserPlan);
            }

            systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "个人计划安排添加失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 保存个人计划细分的任务到工作计划中
     *
     * @param userPlanEntity
     * @param planEntity
     */
    public void CopyUserPlanToPlan(JformUserPlanEntity userPlanEntity, JformPlanEntity planEntity) {
        planEntity.setPlanLevel(userPlanEntity.getPlanLevel());
        planEntity.setPlanName(userPlanEntity.getPlanName());
        planEntity.setStartDate(userPlanEntity.getStartDate());
        planEntity.setFinishDate(userPlanEntity.getFinishDate());
        planEntity.setPlanStatus(userPlanEntity.getPlanStatus());
        planEntity.setPlanInfo(userPlanEntity.getPlanInfo());
        planEntity.setPlanIsalert(userPlanEntity.getPlanIsalert());
        planEntity.setPlanIssucc(userPlanEntity.getPlanIssucc());
        planEntity.setProjectId(userPlanEntity.getProjectId());
        planEntity.setUserPlanid(userPlanEntity.getId());//保存userplan_id三级任务 到plan 的userPlanid字段
        planEntity.setPlanResponder(userPlanEntity.getPlanResponder());//负责人名称
        planEntity.setPlanResponderid(userPlanEntity.getPlanResponderid());//负责人账号
    }

    /**
     * 更新个人计划安排
     *
     * @param jformUserPlan
     * @param request
     * @return
     */
    @RequestMapping(params = "doUpdate")
    @ResponseBody
    public AjaxJson doUpdate(JformUserPlanEntity jformUserPlan, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "个人计划安排更新成功";
        JformUserPlanEntity t = jformUserPlanService.get(JformUserPlanEntity.class, jformUserPlan.getId());
        try {
            if (t.getPlanLevel() == ConstSetBA.PlanLevel_Second) {
                throw new BusinessException("二级任务有项目经理进行修改！");
            }
            MyBeanUtils.copyBeanNotNull2Bean(jformUserPlan, t);

            if (t.getPlanLevel() == ConstSetBA.PlanLevel_Third) {
                //同时更新工作任务计划里面的信息
                StringBuffer sql = new StringBuffer();
                sql.append("update jform_plan t set");
                sql.append(" t.plan_name = ? ,");
                sql.append(" t.start_date = ? ,");
                sql.append(" t.finish_date = ? ,");
                sql.append(" t.plan_info = ?");
                sql.append(" where t.id = ?");
                systemService.executeSql(sql.toString(), t.getPlanName(), t.getStartDate(), t.getFinishDate(), t.getPlanInfo(), t.getTaskId());
            }
            if (StringUtil.isEmpty(t.getPlanId())) {
                t.setPlanId(null);
            }
            jformUserPlanService.saveOrUpdate(t);
            systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "个人计划安排更新失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 个人任务完成
     *
     * @param jformUserPlan
     * @param request
     * @return
     */
    @RequestMapping(params = "doTaskFinish")
    @ResponseBody
    public AjaxJson doTaskFinish(JformUserPlanEntity jformUserPlan, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "个人计划任务完成";
        JformUserPlanEntity t = jformUserPlanService.get(JformUserPlanEntity.class, jformUserPlan.getId());
        try {
            if (t.getPlanLevel() == ConstSetBA.PlanLevel_First) {
                throw new BusinessException("一级任务不能手动完成！");

            }
            if (t.getPlanLevel() == ConstSetBA.PlanLevel_Second) {
                throw new BusinessException("二级任务不能手动完成！");
            }
            jformUserPlan.setPlanStatus(ConstSetBA.PlanStatus_Finish);
            MyBeanUtils.copyBeanNotNull2Bean(jformUserPlan, t);

            if (t.getRealfinishDate().getTime() > t.getFinishDate().getTime()) {
                t.setPlanIssucc(ConstSetBA.PlanIsSuccess_DELAY);
            } else {
                t.setPlanIssucc(ConstSetBA.PlanIsSuccess_YES);
            }

            //同时更新工作任务计划里面的信息
            StringBuffer sql = new StringBuffer();
            sql.append("update jform_plan t set");
            sql.append(" t.rfinish_date = ? ,");
            sql.append(" t.plan_issucc = ? ,");
            sql.append(" t.plan_status = ? ,");
            sql.append(" t.plan_latemsg = ? ");
            sql.append(" where t.userplan_id = ?");
            systemService.executeSql(sql.toString(), t.getRealfinishDate(), t.getPlanIssucc(), t.getPlanStatus(), t.getPlanLatemsg(), t.getId());

            jformUserPlanService.saveOrUpdate(t);
            systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);

            if (!StringUtil.isEmpty(t.getPlanId())) {
                //如果有对应的二级任务，则判断是否该二级任务下面的三级都已经完成了
                if (jformUserPlanService.isUAllThirdFinish(t.getPlanId())) {
                    JformUserPlanEntity uSecondTntity = jformUserPlanService.getEntity(JformUserPlanEntity.class, t.getPlanId());//个人 二级任务
                    uSecondTntity.setPlanStatus(ConstSetBA.PlanStatus_Finish);
                    Date mdate = jformUserPlanService.getUThirdFinishMaxTime(t.getPlanId());
                    if (mdate != null) {
                        uSecondTntity.setRealfinishDate(mdate);
                    } else {
                        uSecondTntity.setRealfinishDate(uSecondTntity.getFinishDate());
                    }
                    boolean finishInTime = uSecondTntity.getRealfinishDate().getTime() <= uSecondTntity.getFinishDate().getTime();//jformUserPlanService.isUThirdFinishInTime(t.getPlanId());
                    uSecondTntity.setPlanIssucc(finishInTime ? ConstSetBA.PlanIsSuccess_YES : ConstSetBA.PlanIsSuccess_DELAY);


                    //更新Ehcart三级任务
                    JformEchartEntity thridEchart = systemService.getEntity(JformEchartEntity.class, t.getTaskId());//图表 三级任务
                    thridEchart.setRfinishDate(uSecondTntity.getRealfinishDate());
                    thridEchart.setTaskStatus(uSecondTntity.getPlanIssucc());
                    jformUserPlanService.saveOrUpdate(uSecondTntity);
                    systemService.saveOrUpdate(thridEchart);
                    // TODO 需要根据三级任务的所有实际完成事件和二级完成事件做对比，确定是否是正常完成状态
                    //如果个人任务的所有同根 二级都完成，则更新工作任务中二级任务
                    if (jformUserPlanService.isUAllSecondFinish(uSecondTntity.getTaskId())) {
                        JformPlanEntity planSecondEntity = systemService.getEntity(JformPlanEntity.class, uSecondTntity.getTaskId());//工作 二级任务
                        //判断 个人 二级任务（多个负责人的情况下)所有二级任务是否都是及时完成

                        planSecondEntity.setPlanStatus(ConstSetBA.PlanStatus_Finish);
                        Date mdate2 = jformUserPlanService.getUSecconFinishMaxTime(uSecondTntity.getTaskId());
                        if (mdate2 != null) {
                            planSecondEntity.setRealfinishDate(mdate2);
                        } else {
                            planSecondEntity.setRealfinishDate(planSecondEntity.getFinishDate());
                        }
                        boolean finishInTime2 = planSecondEntity.getRealfinishDate().getTime() <= planSecondEntity.getFinishDate().getTime();//jformUserPlanService.isUSecconFinishInTime(planSecondEntity.getId());
                        planSecondEntity.setPlanIssucc(finishInTime2 ? ConstSetBA.PlanIsSuccess_YES : ConstSetBA.PlanIsSuccess_DELAY);

                        //更新Echart二级任务
                        JformEchartEntity seconEchart = systemService.getEntity(JformEchartEntity.class, planSecondEntity.getId());
                        seconEchart.setRfinishDate(planSecondEntity.getRealfinishDate());
                        seconEchart.setTaskStatus(planSecondEntity.getPlanIssucc());
                        systemService.saveOrUpdate(planSecondEntity);
                        systemService.saveOrUpdate(seconEchart);
                        //TODO 如果所有工作计划中的二级任务都已经完成则更新对应的一级任务为完成
                        if (jformUserPlanService.isAllSecondFinish(planSecondEntity.getPlanId())) {
                            JformPlanEntity planFirstEntity = systemService.getEntity(JformPlanEntity.class, planSecondEntity.getPlanId());//工作 一级任务

                            planFirstEntity.setPlanStatus(ConstSetBA.PlanStatus_Finish);
                            Date mdate3 = jformUserPlanService.getSecconFinishMaxTime(planFirstEntity.getId());
                            if (mdate3 != null) {
                                planFirstEntity.setRealfinishDate(mdate3);
                            } else {
                                planFirstEntity.setRealfinishDate(planFirstEntity.getFinishDate());
                            }
                            boolean finishInTime3 = planFirstEntity.getRealfinishDate().getTime() <= planFirstEntity.getFinishDate().getTime();//jformUserPlanService.isSecconFinishInTime(planSecondEntity.getPlanId());
                            planFirstEntity.setPlanIssucc(finishInTime3 ? ConstSetBA.PlanIsSuccess_YES : ConstSetBA.PlanIsSuccess_DELAY);

                            //更新Echart一级任务
                            JformEchartEntity firstEchart = systemService.get(JformEchartEntity.class, planFirstEntity.getId());
                            firstEchart.setRfinishDate(planFirstEntity.getRealfinishDate());
                            firstEchart.setTaskStatus(planFirstEntity.getPlanIssucc());

                            systemService.saveOrUpdate(planFirstEntity);
                            systemService.saveOrUpdate(firstEchart);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "个人计划安排更新失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }


    /**
     * 自定义按钮-[添加任务]业务
     *
     * @param jformUserPlan
     * @param request
     * @return
     */
    @RequestMapping(params = "doAdduserplan")
    @ResponseBody
    public AjaxJson doAdduserplan(JformUserPlanEntity jformUserPlan, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "添加任务成功";
        JformUserPlanEntity t = jformUserPlanService.get(JformUserPlanEntity.class, jformUserPlan.getId());
        try {
            jformUserPlanService.doAdduserplanBus(t);
            systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "添加任务失败";
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 自定义按钮-[细分完成]业务
     *
     * @param jformUserPlan
     * @param request
     * @return
     */
    @RequestMapping(params = "doDividefinish")
    @ResponseBody
    public AjaxJson doDividefinish(JformUserPlanEntity jformUserPlan, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "细分完成成功";
        JformUserPlanEntity t = jformUserPlanService.get(JformUserPlanEntity.class, jformUserPlan.getId());
        try {
            //1.判断当前登陆用户细分的二级任务下是否已经细分任务
            //2.细分完成当前的二级任务
            //3.是否二级任务的全部负责人都完成了任务细分，是：则将工作任务计划的二级任务状态置为细分完成
            if (t.getPlanLevel() != ConstSetBA.PlanLevel_Second) {
                throw new BusinessException("当前任务不是二级任务不能进行细分完成！");
            }

            long thridCount = systemService.getCountForJdbcParam("select count(1) from jform_user_plan t where t.plan_id = ?", t.getId());
            if (thridCount <= 0) {
                throw new BusinessException("请先细分当前二级任务后再进行操作！");
            }
            boolean isAllDivideFinish = true;
            List<JformUserPlanEntity> entities = systemService.findHql("from JformUserPlanEntity e where e.taskId = ?", t.getTaskId());
            for (JformUserPlanEntity entity : entities) {
                if (entity.getId().equals(t.getId())) continue;
                if (entity.getPlanStatus() != ConstSetBA.PlanStatus_SendDivideFinish) {
                    isAllDivideFinish = false;
                    break;
                }
            }
            //除了当前细分完成二级任务，其他同细分完成二级都已经细分完成，则将工作任务计划中的二级任务执行细分完成操作
            if (isAllDivideFinish) {
                JformPlanEntity entity = systemService.get(JformPlanEntity.class, t.getTaskId());
                entity.setPlanStatus(ConstSetBA.PlanStatus_SendDivideFinish);
                systemService.save(entity);
            }

            t.setPlanStatus(ConstSetBA.PlanStatus_SendDivideFinish);

            // TODO 二级任务下面的三级任务状态也更新为细分完成
            StringBuffer sql = new StringBuffer();
            sql.append("update jform_user_plan t set");
            sql.append(" t.plan_status = ?");
            sql.append(" where t.plan_id = ?");
            systemService.executeSql(sql.toString(), ConstSetBA.PlanStatus_SendDivideFinish, t.getId());

            jformUserPlanService.doDividefinishBus(t);
            systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "细分完成失败";
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 个人计划安排新增页面跳转
     *
     * @return
     */
    @RequestMapping(params = "goAdd")
    public ModelAndView goAdd(JformUserPlanEntity jformUserPlan, HttpServletRequest req) {
        if (StringUtil.isNotEmpty(jformUserPlan.getId())) {
            jformUserPlan = jformUserPlanService.getEntity(JformUserPlanEntity.class, jformUserPlan.getId());
            req.setAttribute("jformUserPlanPage", jformUserPlan);
        }
        req.setAttribute("operate", req.getParameter("operate"));
        TSUser user = ResourceUtil.getSessionUser();
        req.setAttribute("userid", user.getUserName());
        req.setAttribute("username", user.getRealName());
        return new ModelAndView("com/jeecg/jform_user_plan/jformUserPlan-add");
    }

    /**
     * 个人计划安排编辑页面跳转
     *
     * @return
     */
    @RequestMapping(params = "goUpdate")
    public ModelAndView goUpdate(JformUserPlanEntity jformUserPlan, HttpServletRequest req) {
        if (StringUtil.isNotEmpty(jformUserPlan.getId())) {
            jformUserPlan = jformUserPlanService.getEntity(JformUserPlanEntity.class, jformUserPlan.getId());
            req.setAttribute("jformUserPlanPage", jformUserPlan);
        }

        return new ModelAndView("com/jeecg/jform_user_plan/jformUserPlan-update");
    }

    /**
     * 个人计划安排编辑页面跳转
     *
     * @return
     */
    @RequestMapping(params = "goFeedbackFinish")
    public ModelAndView goFeedbackFinish(JformUserPlanEntity jformUserPlan, HttpServletRequest req) {
        if (StringUtil.isNotEmpty(jformUserPlan.getId())) {
            jformUserPlan = jformUserPlanService.getEntity(JformUserPlanEntity.class, jformUserPlan.getId());
            if (jformUserPlan.getPlanIssucc() == null) jformUserPlan.setPlanIssucc(ConstSetBA.PlanIsSuccess_NO);
            req.setAttribute("jformUserPlanPage", jformUserPlan);
        }

        return new ModelAndView("com/jeecg/jform_user_plan/jformUserPlanFeedback-update");
    }


    /**
     * 导入功能跳转
     *
     * @return
     */
    @RequestMapping(params = "upload")
    public ModelAndView upload(HttpServletRequest req) {
        req.setAttribute("controller_name", "jformUserPlanController");
        return new ModelAndView("common/upload/pub_excel_upload");
    }

    /**
     * 导出excel
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "exportXls")
    public String exportXls(JformUserPlanEntity jformUserPlan, HttpServletRequest request, HttpServletResponse response
            , DataGrid dataGrid, ModelMap modelMap) {
        CriteriaQuery cq = new CriteriaQuery(JformUserPlanEntity.class, dataGrid);
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, jformUserPlan, request.getParameterMap());
        List<JformUserPlanEntity> jformUserPlans = this.jformUserPlanService.getListByCriteriaQuery(cq, false);
        modelMap.put(NormalExcelConstants.FILE_NAME, "个人计划安排");
        modelMap.put(NormalExcelConstants.CLASS, JformUserPlanEntity.class);
        modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("个人计划安排列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(),
                "导出信息"));
        modelMap.put(NormalExcelConstants.DATA_LIST, jformUserPlans);
        return NormalExcelConstants.JEECG_EXCEL_VIEW;
    }

    /**
     * 导出excel 使模板
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "exportXlsByT")
    public String exportXlsByT(JformUserPlanEntity jformUserPlan, HttpServletRequest request, HttpServletResponse response
            , DataGrid dataGrid, ModelMap modelMap) {
        modelMap.put(NormalExcelConstants.FILE_NAME, "个人计划安排");
        modelMap.put(NormalExcelConstants.CLASS, JformUserPlanEntity.class);
        modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("个人计划安排列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(),
                "导出信息"));
        modelMap.put(NormalExcelConstants.DATA_LIST, new ArrayList());
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
                List<JformUserPlanEntity> listJformUserPlanEntitys = ExcelImportUtil.importExcel(file.getInputStream(), JformUserPlanEntity.class, params);
                for (JformUserPlanEntity jformUserPlan : listJformUserPlanEntitys) {
                    jformUserPlanService.save(jformUserPlan);
                }
                j.setMsg("文件导入成功！");
            } catch (Exception e) {
                j.setMsg("文件导入失败！");
                logger.error(e.getMessage());
            } finally {
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
