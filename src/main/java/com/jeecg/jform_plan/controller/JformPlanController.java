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
import com.jeecg.jform_user_plan.entity.JformUserPlanEntity;
import org.jeecgframework.tag.vo.datatable.SortDirection;
import org.jeecgframework.web.system.pojo.base.TSUser;
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
 * @author onlineGenerator
 * @version V1.0
 * @Title: Controller
 * @Description: 项目任务计划表
 * @date 2018-12-15 08:20:55
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
    public void datagrid(JformPlanEntity jformPlan, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(JformPlanEntity.class, dataGrid);
        if (StringUtil.isEmpty(jformPlan.getId())) {
            cq.isNull("planId");
        } else {
            cq.eq("planId", jformPlan.getId());
            jformPlan.setId(null);
        }
        //查询条件组装器
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, jformPlan, request.getParameterMap());
        try {
            cq.addOrder("startDate", SortDirection.asc);
            cq.addOrder("finishDate", SortDirection.asc);
            TSUser user = ResourceUtil.getSessionUser();
            //非管理员账号需要过滤项目
            if(!"admin".equals(user.getUserName())){
                String[] projectId = getProjectId(user.getUserName());
                if(projectId!=null){
                    cq.in("projectId",projectId);
                }
            }
            //自定义追加查询条件
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        cq.add();
        this.jformPlanService.getDataGridReturn(cq, true);
        TagUtil.treegrid(response, dataGrid);
    }

    private String[] getProjectId(String username){
        List<String> projectid = systemService.findHql("select id from JformProjectEntity t where t.projectResponderid like ? ","%"+username+"%");
        if(projectid!=null && projectid.size()>0){
            return projectid.toArray(new String[projectid.size()]);
        }
        return null;
    }

    /**
     * 删除项目任务计划表
     *
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
        try {
            if (jformPlan.getPlanStatus() != ConstSetBA.PlanStatus_Draft &&
                    jformPlan.getPlanStatus() != ConstSetBA.PlanStatus_SendDivide &&
                    jformPlan.getPlanStatus() != ConstSetBA.PlanStatus_SendDivideFinish) {
                throw new BusinessException("任务不是草稿，下发细分，细分完成状态，不能删除！");
            }

            long count = systemService.getCountForJdbcParam("select count(1) from jform_plan e where e.plan_id = ?",jformPlan.getId());
            if(count>0){
                throw new BusinessException("该任务下还有子任务，不能删除！");
            }
            //如果是分配的任务则恢复分配的状态
            if(StringUtil.isNotEmpty(jformPlan.getTaskId())){
                //恢复分配任务的下发状态
                JformTaskEntity task = systemService.getEntity(JformTaskEntity.class, jformPlan.getTaskId());
                task.setTaskStatus(ConstSetBA.TaskStatus_Send);
                systemService.save(task);
            }

            if (jformPlan.getPlanLevel() == ConstSetBA.PlanLevel_First) {

            }else if(jformPlan.getPlanLevel()==ConstSetBA.PlanLevel_Second){
                //删除二级任务
                List<JformUserPlanEntity> userPlanEntities = systemService.findHql("from JformUserPlanEntity e where e.taskId = ?",jformPlan.getId());
                systemService.deleteAllEntitie(userPlanEntities);
            }else if(jformPlan.getPlanLevel() == ConstSetBA.PlanLevel_Third){
                //删除三级任务
                if(StringUtil.isNotEmpty(jformPlan.getUserPlanid())){
                    systemService.deleteEntityById(JformUserPlanEntity.class,jformPlan.getUserPlanid());
                }
            }
            jformPlanService.delete(jformPlan);
            systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "项目任务计划表删除失败";
            throw new BusinessException("该任务下还有子任务，不能删除！");
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 批量删除项目任务计划表
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
        message = "项目任务计划表删除成功";
        try {
            for (String id : ids.split(",")) {
                JformPlanEntity jformPlan = systemService.getEntity(JformPlanEntity.class, id);
                if (jformPlan.getPlanStatus() != ConstSetBA.PlanStatus_Draft &&
                        jformPlan.getPlanStatus() != ConstSetBA.PlanStatus_SendDivide &&
                        jformPlan.getPlanStatus() != ConstSetBA.PlanStatus_SendDivideFinish) {
                    continue;
                }
                if (jformPlan.getPlanLevel() == ConstSetBA.PlanLevel_First) {
                    //恢复分配任务的下发状态
                    JformTaskEntity task = systemService.getEntity(JformTaskEntity.class, jformPlan.getTaskId());
                    task.setTaskStatus(ConstSetBA.TaskStatus_Send);
                    systemService.save(task);
                }
                jformPlanService.delete(jformPlan);
                systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "项目任务计划表删除失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }


    /**
     * 添加项目任务计划表
     *
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
        try {
            if (StringUtil.isEmpty(jformPlan.getPlanId())) {
                jformPlan.setPlanId(null);
            }

            if(StringUtil.isEmpty(jformPlan.getStartDate())){
                throw new BusinessException("开始时间不能为空!");
            }else if(StringUtil.isEmpty(jformPlan.getFinishDate())){
                throw new BusinessException("结束时间不能为空!");
            }else if(StringUtil.isEmpty(jformPlan.getProjectId())){
                throw new BusinessException("项目ID不能为空!");
            }

            if(jformPlan.getPlanLevel()==ConstSetBA.PlanLevel_First){
                if(StringUtil.isNotEmpty(jformPlan.getPlanId())){
                    //throw new BusinessException("一级任务不应该有父ID!")
                    logger.error("一级任务不应该有父ID!");
                    jformPlan.setPlanId(null);
                }
            }else{
                if(StringUtil.isEmpty(jformPlan.getPlanId())){
                    throw new BusinessException("二级，三级任务的父任务ID不能为空！");
                }
            }


            //将分配任务下发状态变成分配状态
            if(jformPlan.getTaskId()!=null){
                JformTaskEntity task = systemService.getEntity(JformTaskEntity.class,jformPlan.getTaskId());
                if (task.getTaskStatus() == ConstSetBA.TaskStatus_Send) {
                    task.setTaskStatus(ConstSetBA.TaskStatus_Assign);
                } else {
                    throw new BusinessException("该分配任务状态不是下发状态不能进行分配！");
                }
                systemService.save(task);
            }


            jformPlanService.save(jformPlan);
            jformPlan = jformPlanService.get(JformPlanEntity.class,jformPlan.getId());

            //一级任务
            if (jformPlan.getPlanLevel() == ConstSetBA.PlanLevel_First) {

            } else {
                //二级 三级任务新增，同步个人任务计划
                JformPlanEntity task = systemService.getEntity(JformPlanEntity.class, jformPlan.getPlanId());
                jformPlan.setProjectId(task.getProjectId());
                if(jformPlan.getPlanLevel()==ConstSetBA.PlanLevel_Second){
                    //二级任务
                    String[] username = jformPlan.getPlanResponder().split(",");
                    int i =0;
                    for (String account : jformPlan.getPlanResponderid().split(",")) {
                        JformUserPlanEntity userPlanEntity = new JformUserPlanEntity();
                        CopyPlanToUserPlan(jformPlan,userPlanEntity,account,username[i]);
                        systemService.save(userPlanEntity);
                        i++;
                    }
                }else if(jformPlan.getPlanLevel() == ConstSetBA.PlanLevel_Third){
                    JformUserPlanEntity userPlanEntity = new JformUserPlanEntity();
                    //判断三级任务的负责人，是否存在于二级任务中，如果存在，则需要找到个人任务中的二级任务，将三级任务置于该二级任务下，否则不用
                    if(task.getPlanResponderid().contains(jformPlan.getPlanResponderid())){
                        List<JformUserPlanEntity> entityList = systemService.findHql("from JformUserPlanEntity t where t.taskId = ? and t.planResponderid = ?"
                                ,task.getId(),jformPlan.getPlanResponderid());
                        if(entityList.size()>=1){
                            JformUserPlanEntity entity = entityList.get(0);
                            userPlanEntity.setPlanId(entity.getId());
                        }
                    }
                    CopyPlanToUserPlan(jformPlan,userPlanEntity,jformPlan.getPlanResponderid(),jformPlan.getPlanResponder());
                    systemService.save(userPlanEntity);
                    jformPlan.setUserPlanid(userPlanEntity.getId());//三级任务才会有userPlanid
                }
            }
            jformPlanService.saveOrUpdate(jformPlan);
            systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "项目任务计划表添加失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 更新项目任务计划表
     *
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
            String oldResponderId = t.getPlanResponderid();
            MyBeanUtils.copyBeanNotNull2Bean(jformPlan, t);
            if (StringUtil.isEmpty(t.getPlanId())) {
                t.setPlanId(null);
            }
            //二级任务更新
            if(t.getPlanLevel()==ConstSetBA.PlanLevel_Second){
                if(!oldResponderId.equals(t.getPlanResponderid())){
                    UpdateUserSecanPlan(t,oldResponderId);
                }
                StringBuffer sql = new StringBuffer();
                sql.append("update jform_user_plan t set");
                sql.append(" t.plan_name = ? ,");
                sql.append(" t.start_date = ? ,");
                sql.append(" t.finish_date = ? ,");
                sql.append(" t.plan_info = ?");
                sql.append(" where t.task_id = ?");
                systemService.executeSql(sql.toString(),t.getPlanName(),t.getStartDate(),t.getFinishDate(),t.getPlanInfo(),t.getId());
            }else if(t.getPlanLevel() == ConstSetBA.PlanLevel_Third){
                //三级任务

                JformUserPlanEntity uThridPlanEntity = systemService.getEntity(JformUserPlanEntity.class,t.getUserPlanid());
                uThridPlanEntity.setPlanName(t.getPlanName());
                uThridPlanEntity.setStartDate(t.getStartDate());
                uThridPlanEntity.setFinishDate(t.getFinishDate());
                uThridPlanEntity.setPlanInfo(t.getPlanInfo());
                uThridPlanEntity.setPlanResponder(t.getPlanResponder());
                uThridPlanEntity.setPlanResponders(t.getPlanResponder());
                uThridPlanEntity.setPlanResponderid(t.getPlanResponderid());

                if(uThridPlanEntity.getPlanId()!=null){
                    JformUserPlanEntity uSecondPlanEntity = systemService.getEntity(JformUserPlanEntity.class,uThridPlanEntity.getPlanId());
                    if(!uSecondPlanEntity.getPlanResponderid().equals(uThridPlanEntity.getPlanResponderid())){
                        uThridPlanEntity.setPlanId(null);
                    }
                }
                List<JformUserPlanEntity> list = systemService.findHql("from JformUserPlanEntity t where t.taskId = ? and t.planResponderid = ?",
                            t.getPlanId(),t.getPlanResponderid());
                if(list.size()>0){
                    uThridPlanEntity.setPlanId(list.get(0).getId());
                }
                systemService.saveOrUpdate(uThridPlanEntity);
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
     * 修改了负责人信息，将同步修改个人任务中的数据
     * @param entity
     * @param oldResponderid
     */
    private void UpdateUserSecanPlan(JformPlanEntity entity, String oldResponderid) {
        //之前的负责人信息
        String[] oldArrayId = oldResponderid.split(",");
        int newArrayIndex = 0;
        //现在的负责人ID
        String[] newArrayId = entity.getPlanResponderid().split(",");
        //现在的负责人名称
        String[] newArrayName = entity.getPlanResponder().split(",");
        Map nameMap = new HashMap();

        //找出删掉的负责人ID
        StringBuffer oldUpdateId = new StringBuffer();
        for (String str : oldArrayId) {
            if (!entity.getPlanResponderid().contains(str)) {
                if (oldUpdateId.length() > 0) oldUpdateId.append(",");
                oldUpdateId.append(str);
            }
        }
        String[] oldUpdateIdS = oldUpdateId.toString().split(",");

        //找出新增的负责人ID
        StringBuffer newUpdateId = new StringBuffer();
        int index = 0;
        for (String str : newArrayId) {
            nameMap.put(str, newArrayName[index]);
            index++;
            if (!oldResponderid.contains(str)) {
                if (newUpdateId.length() > 0) newUpdateId.append(",");
                newUpdateId.append(str);
            }
        }
        Map map = new HashMap();
        //二级任务负责人:以前的负责人不变,新增了负责人
        if (oldUpdateId.length() == 0 && newUpdateId.length() > 0) {
            //1.新增个人二级任务
            Map newUserPlanMap = new HashMap();
            for (String user : newUpdateId.toString().split(",")) {
                JformUserPlanEntity plan = new JformUserPlanEntity();
                CopyPlanToUserPlan(entity, plan, user, (String) nameMap.get(user));
                systemService.save(plan);
                newUserPlanMap.put(user,plan.getId());
            }
            //2.找出个人三级任务是空的:只能通过查找工具计划中三级任务的负责人不在二级负责人里面的三级任务
            map.put("ARRAY_1",oldArrayId);
            List<String> oldUserPlanId = systemService.findHqlWithArray("select userPlanid from JformPlanEntity t where t.planId = ? " +
                    " and t.planResponderid not in(:ARRAY_1)",map,entity.getId(),"ARRAY");
            for(String id : oldUserPlanId){
                JformUserPlanEntity e = systemService.get(JformUserPlanEntity.class,id);
                String plan_id = (String)newUserPlanMap.get(e.getPlanResponderid());
                if(plan_id!=null && !plan_id.equals("")){
                    e.setPlanId(plan_id);
                    systemService.saveOrUpdate(e);
                }
            }
        } else if (oldUpdateId.length() > 0 && newUpdateId.length() == 0) {
            //二级任务负责人:没有新增，删掉了
            //找出删掉的负责人信息
            map.put("ARRAY_1",oldUpdateIdS);
            List<JformUserPlanEntity> entities = systemService.findHqlWithArray("from JformUserPlanEntity t where t.taskId = ? and" +
                    " t.planResponderid in (:ARRAY_1)",map, entity.getId(), "ARRAY");
            StringBuffer planId = new StringBuffer();
            for (JformUserPlanEntity e : entities) {
                if (planId.length() > 0) planId.append(",");
                planId.append(e.getId());
            }
            if(planId.length()>0){
                String[] planIdArray = planId.toString().split(",");
                map.clear();
                map.put("ARRAY_0",planIdArray);
                //找出需要更新，修改的三级任务的父ID为空
                List<JformUserPlanEntity> thridEntity = systemService.findHqlWithArray("from JformUserPlanEntity t where t.planId in(:ARRAY_0)",
                        map,"ARRAY");
                for (JformUserPlanEntity e : thridEntity) {
                    e.setPlanId(null);
                    systemService.saveOrUpdate(e);
                }
            }
            systemService.deleteAllEntitie(entities);
        } else if (oldUpdateId.length() > 0 && newUpdateId.length() > 0) {
            map.put("ARRAY_1",oldUpdateIdS);
            //二级任务负责人:同时新增和删除了负责人
            List<JformUserPlanEntity> entities = systemService.findHqlWithArray("from JformUserPlanEntity t where t.taskId = ? and" +
                    " t.planResponderid in (:ARRAY_1)",map, entity.getId(),"ARRAY" );
            StringBuffer planId = new StringBuffer();
            for (JformUserPlanEntity e : entities) {
                if (planId.length() > 0) planId.append(",");
                planId.append( e.getId() );
            }
            String[] planIdS = planId.toString().split(",");
            int oldL = oldUpdateIdS.length;
            int newL = newUpdateId.toString().split(",").length;
            List<JformUserPlanEntity> needDeleteList = new ArrayList<>();
            boolean needDoDelete = false;//需要执行删除操作
            Map newUId = new HashMap();
            //将旧的信息更新为新的信息
            if (oldL < newL) {
                //删的少，增的多，需要添加旧的
                int need = newL - oldL;
                for (; need > 0; need--) {
                    try {
                        JformUserPlanEntity e = new JformUserPlanEntity();
                        MyBeanUtils.copyBeanNotNull2Bean((JformUserPlanEntity) entities.get(0), e);
                        e.setPlanId(null);
                        entities.add(e);
                    } catch (Exception e) {
                        throw new BusinessException(e.getMessage());
                    }
                }
            } else if (oldL > newL) {
                //删的多，增的少。需要删除旧的
                int needDel = oldL - newL;
                for (; needDel > 0; needDel--) {
                    needDeleteList.add(entities.get(0));
                    entities.remove(entities.get(0));
                }
                needDoDelete = true;
            }
            newArrayIndex = 0;
            for (JformUserPlanEntity e : entities) {
                e.setPlanResponders(entity.getPlanResponder());//全部负责人
                e.setPlanResponder(newArrayName[newArrayIndex]);
                e.setPlanResponderid(newArrayId[newArrayIndex]);
                newUId.put(newArrayId[newArrayIndex], e.getId());
                newArrayIndex++;
                systemService.saveOrUpdate(e);
            }
            map.clear();
            map.put("ARRAY_0",planIdS);

            //找出需要更新，修改的三级任务的父ID为空
            List<JformUserPlanEntity> thridEntity = systemService.findHqlWithArray("from JformUserPlanEntity t where t.planId in(:ARRAY_0)",
                    map,"ARRAY");
            for (JformUserPlanEntity e : thridEntity) {
                if (newUId.get(e.getPlanResponderid()) != null) {
                    e.setPlanId((String) newUId.get(e.getPlanResponderid()));
                } else {
                    e.setPlanId(null);
                }
                systemService.saveOrUpdate(e);
            }
            if (needDoDelete) {
                systemService.deleteAllEntitie(needDeleteList);
            }
        }

        //更新负责人信息
        List<JformUserPlanEntity> entities = systemService.findHql("from JformUserPlanEntity t where t.taskId = ? ", entity.getId());
        for (JformUserPlanEntity e : entities) {
            e.setPlanResponders(entity.getPlanResponder());
            systemService.saveOrUpdate(e);
        }
    }

    /**
     * 自定义按钮-[下发细分]业务
     *
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
        try {
            if (t.getPlanLevel() != ConstSetBA.PlanLevel_Second) {
                throw new BusinessException("非二级任务不能下发细分操作！");
            }
            t.setPlanStatus(ConstSetBA.PlanStatus_SendDivide);
            Map map = new HashMap();
            map.put("plan_name", t.getPlanName());

            jformPlanService.doSendDivideBus(t);
            UpdateUserPlanStatus(t.getId(),ConstSetBA.PlanStatus_SendDivide);
            systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
            //下发细分通知
            TSUser user = ResourceUtil.getSessionUser();
            for (String name : t.getPlanResponderid().split(",")) {
                TuiSongMsgUtil.sendMessage("SendDivide", map, user.getUserName(), name);
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "下发细分失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 更新个人任务计划的状态为指定的状态
     * 同时更新自己下级任务为指定状态
     * @param id
     * @param status
     */
    private void UpdateUserPlanStatus(String id,int status){

        //更新三级任务的状态
        StringBuffer sql = new StringBuffer();
        sql.append("update jform_user_plan set");
        sql.append(" plan_status = ? ");
        if(status==ConstSetBA.PlanStatus_SendDivide){
            sql.append(", plan_show =" +ConstSetBA.PlanUser_Show);
        }
        sql.append(" where task_id = ? ");
        sql.append(" or plan_id in ( select a.id from (select id from jform_user_plan  where task_id = ?) a)");
        systemService.executeSql(sql.toString(),status,id,id);

        //更新当前任务下的三级任务的状态
        sql.setLength(0);
        sql.append("update jform_plan set");
        sql.append(" plan_status = ? ");
        sql.append(" where plan_id = ? ");
        systemService.executeSql(sql.toString(),status,id);
    }

    /**
     * 创建个人任务
     * @param planEntity
     * @param userPlanEntity
     * @param reponderid
     * @param responder
     */
    public void CopyPlanToUserPlan(JformPlanEntity planEntity,JformUserPlanEntity userPlanEntity,String reponderid,String responder){
        userPlanEntity.setPlanLevel(planEntity.getPlanLevel());
        userPlanEntity.setPlanName(planEntity.getPlanName());
        userPlanEntity.setStartDate(planEntity.getStartDate());
        userPlanEntity.setFinishDate(planEntity.getFinishDate());
        userPlanEntity.setPlanStatus(planEntity.getPlanStatus());
        userPlanEntity.setPlanInfo(planEntity.getPlanInfo());
        userPlanEntity.setPlanIsalert(planEntity.getPlanIsalert());
        userPlanEntity.setPlanIssucc(planEntity.getPlanIssucc());
        userPlanEntity.setProjectId(planEntity.getProjectId());
        userPlanEntity.setTaskId(planEntity.getId());//将jform_plan的id 保存到jform_user_plan 的taskID中
        userPlanEntity.setPlanResponders(planEntity.getPlanResponder());//全部负责人
        userPlanEntity.setPlanResponder(responder);//负责人名称
        userPlanEntity.setPlanResponderid(reponderid);//负责人账号
        userPlanEntity.setPlanShow(ConstSetBA.PlanUser_NotShow);//不展示，下发后再展示
    }

    /**
     * 自定义按钮-[细分完成]业务
     *
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
        try {
            if (t.getPlanLevel() != ConstSetBA.PlanLevel_Second) {
                throw new BusinessException("非二级任务不能下发细分完成操作！");
            }
            Long count = systemService.getCountForJdbcParam("select count(*) from jform_plan t where t.plan_id = ?",t.getId());
            if (count == 0) {
                throw new BusinessException("当前二级任务下面没有细分的三级任务，不能进行细分完成！");
            }
            t.setPlanStatus(ConstSetBA.PlanStatus_SendDivideFinish);
            jformPlanService.doDivideFinishBus(t);
            UpdateUserPlanStatus(t.getId(),ConstSetBA.PlanStatus_SendDivideFinish);
            systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
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
     *
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
        if (t.getPlanLevel() != ConstSetBA.PlanLevel_First) {
            throw new BusinessException("不是一级任务不能提交审批！");
        } else if (t.getPlanStatus() != ConstSetBA.PlanStatus_Draft && t.getPlanStatus() != ConstSetBA.PlanStatus_Disapprove) {
            throw new BusinessException("该任务状态不能提交审批！");
        } else {
            try {
                //如果一级任务下的二级任务都是细分完成状态则可以提交审批
                CheckUnderPlanStatus(t.getId());//检查任务状态是否满足提交审批
                t.setPlanStatus(ConstSetBA.PlanStatus_SendApprove);

                JformProjectEntity project = systemService.getEntity(JformProjectEntity.class, t.getProjectId());
                Map map = new HashMap();
                map.put("plan_name", t.getPlanName());
                map.put("project_name", project.getProjectName());
                //发送审批信息
                for (String name : project.getProjectResponderid().split(",")) {
                    TuiSongMsgUtil.sendMessage("SendApprove", map, ResourceUtil.getSessionUser().getUserName(), name);
                }
                UpdatePlanStatus(t.getId(), ConstSetBA.PlanStatus_SendApprove);
                //更新个人任务状态为提交审批
                UpdateUserPlanStatusOnApprove(t.getId(),ConstSetBA.PlanStatus_SendApprove);
                jformPlanService.doSendApproveBus(t);
                systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
            } catch (Exception e) {
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
     *
     * @param planid
     */
    public void CheckUnderPlanStatus(String planid) {
        List<JformPlanEntity> jformlist = systemService.findByQueryString("from JformPlanEntity where planId = '" + planid + "'");
        if (jformlist == null || jformlist.size() == 0) throw new BusinessException("找不到一级任务下面的二级任务列表！");
        for (JformPlanEntity plan : jformlist) {
            if (plan.getPlanStatus() != ConstSetBA.PlanStatus_SendDivideFinish
                    && plan.getPlanStatus() != ConstSetBA.PlanStatus_Disapprove) {
                throw new BusinessException("二级任务:" + plan.getPlanName() + ",不是细分完成状态，不能进行审批！");
            }
        }
    }

    /**
     * 更新一级任务ID下面的所有二级，三级任务的状态为 status
     *
     * @param id     任务ID
     * @param status 任务状态
     */
    public void UpdatePlanStatus(String id, int status) {
        String sql = "update jform_plan" +
                " set plan_status = ?"+
                " where plan_id in (select a.id from(select p.id from jform_plan p where p.plan_id= ? )a)" +
                " or plan_id = ? ";
        int count = systemService.executeSql(sql,status,id,id);
        systemService.addLog("审批通过添加" + count + "数据到报表中。", Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
    }

    /**
     * 审批通过的时候复制审批通过的给到Echart报表数据中
     *
     * @param id
     */
    public void CopyPlanToEchart(String id) {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("INSERT INTO jform_echart (id, project_id, task_id, task_level, task_name, task_shortname,");
            sql.append(" alert_status, alert_msg, task_status, start_date, finish_date, pretask_id )");
            sql.append(" SELECT p.id,p.project_id,p.id,p.plan_level, p.plan_name, SUBSTRING(p.plan_name,1,4),");
            sql.append(" p.plan_isalert, p.plan_alertmsg, p.plan_issucc, p.start_date, p.finish_date, p.plan_id");
            sql.append(" from jform_plan p where p.id = ?");
            sql.append(" or p.plan_id = ?");
            sql.append(" or p.plan_id in (SELECT id from jform_plan where plan_id = ?)");

            System.out.println(sql.toString());

            systemService.executeSql(sql.toString(), id, id, id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new BusinessException(e.getMessage());
        }

    }

    /**
     * 更新个人任务里面的三级任务和二级任务
     * @param id    一级任务的id
     */
    public void UpdateUserPlanStatusOnApprove(String id,int status){
        //1.获取工作计划里面的二级任务ID
        StringBuffer sql = new StringBuffer();
        StringBuffer planId = new StringBuffer();
        sql.append("select id from JformPlanEntity t where t.planId = ?");
        List<String> seconPlans = systemService.findHql(sql.toString(),id);
        for(String planid : seconPlans){
            if(planId.length()>0)planId.append(",");
            planId.append("'"+planid+"'");
        }
        sql.setLength(0);//清空内容

        //更新个人二级任务
        sql.append("update jform_user_plan t set ");
        sql.append(" t.plan_status = "+status);
        sql.append(" where t.task_id in ("+planId.toString()+")");

        systemService.updateBySqlString(sql.toString());
        //更新个人三级任务 (通过找到工作计划：二级任务下的所有三级计划userplan_id等于个人计划三级任务的ID)
        sql.setLength(0);
        sql.append("update jform_user_plan t set ");
        sql.append(" t.plan_status = "+status);
        sql.append(" where t.id in (");
        sql.append(" select p.userplan_id from jform_plan p where p.plan_id in ("+planId.toString()+") )");

        systemService.updateBySqlString(sql.toString());
    }

    /***
     * 一级计划审批方法
     * @param content
     * @param id
     * @param approvetype 1 审批通过 2 审批驳回
     * @return
     */
    @RequestMapping(params = "doApprove")
    @ResponseBody
    public AjaxJson doApprove(String content, String id, String approvetype) {
        logger.info("-------审核意见:" + content);//demo简单作打印,实际项目可酌情处理
        String message = null;
        AjaxJson j = new AjaxJson();
        JformPlanEntity t = systemService.getEntity(JformPlanEntity.class, id);
        message = "审核成功";
        try {
            String msgCode;
            Map map = new HashMap();
            map.put("plan_name", t.getPlanName());
            map.put("content", content);
            t.setPlanStatus(approvetype.equals("2") ? ConstSetBA.PlanStatus_Disapprove : ConstSetBA.PlanStatus_Execution);
            JformProjectEntity project = systemService.getEntity(JformProjectEntity.class, t.getProjectId());
            t.setPlanRejectmsg(content);
            this.jformPlanService.updateEntitie(t);
            systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
            //更新当前一级菜单下面的所有计划状态
            UpdatePlanStatus(t.getId(), approvetype.equals("2") ? ConstSetBA.PlanStatus_Disapprove : ConstSetBA.PlanStatus_Execution);
            //UpdateUserPlanStatus(t.getId(),approvetype.equals("2") ? ConstSetBA.PlanStatus_Disapprove : ConstSetBA.PlanStatus_Execution);
            UpdateUserPlanStatusOnApprove(t.getId(),approvetype.equals("2") ? ConstSetBA.PlanStatus_Disapprove : ConstSetBA.PlanStatus_Execution);

            if (approvetype.equals("1")) {
                //审批通过，将审批通过的项目信息添加到报表数据中
                CopyPlanToEchart(t.getId());
            }
            TuiSongMsgUtil.sendMessage(approvetype.equals("2") ? "DisApprove" : "Approve", map, ResourceUtil.getSessionUser().getUserName(), project.getProjectManagerid());
        } catch (Exception e) {
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
        return new ModelAndView("com/jeecg/jform_plan/jformPlan-addtask");
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
        String id = req.getParameter("id");
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
        req.setAttribute("controller_name", "jformPlanController");
        return new ModelAndView("common/upload/pub_excel_upload");
    }

    /**
     * 导出excel
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "exportXls")
    public String exportXls(JformPlanEntity jformPlan, HttpServletRequest request, HttpServletResponse response
            , DataGrid dataGrid, ModelMap modelMap) {
        CriteriaQuery cq = new CriteriaQuery(JformPlanEntity.class, dataGrid);
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, jformPlan, request.getParameterMap());
        List<JformPlanEntity> jformPlans = this.jformPlanService.getListByCriteriaQuery(cq, false);
        modelMap.put(NormalExcelConstants.FILE_NAME, "项目任务计划表");
        modelMap.put(NormalExcelConstants.CLASS, JformPlanEntity.class);
        modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("项目任务计划表列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(),
                "导出信息"));
        modelMap.put(NormalExcelConstants.DATA_LIST, jformPlans);
        return NormalExcelConstants.JEECG_EXCEL_VIEW;
    }

    /**
     * 导出excel 使模板
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "exportXlsByT")
    public String exportXlsByT(JformPlanEntity jformPlan, HttpServletRequest request, HttpServletResponse response
            , DataGrid dataGrid, ModelMap modelMap) {
        modelMap.put(NormalExcelConstants.FILE_NAME, "项目任务计划表");
        modelMap.put(NormalExcelConstants.CLASS, JformPlanEntity.class);
        modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("项目任务计划表列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(),
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
                List<JformPlanEntity> listJformPlanEntitys = ExcelImportUtil.importExcel(file.getInputStream(), JformPlanEntity.class, params);
                for (JformPlanEntity jformPlan : listJformPlanEntitys) {
                    jformPlanService.save(jformPlan);
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
