package com.jeecg.jform_user_plan.service.impl;

import com.jeecg.ConstSetBA;
import com.jeecg.jform_user_plan.service.JformUserPlanServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import com.jeecg.jform_user_plan.entity.JformUserPlanEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.io.Serializable;

import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.core.util.MyClassLoader;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.cgform.enhance.CgformEnhanceJavaInter;

import org.jeecgframework.minidao.util.FreemarkerParseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.jeecgframework.core.util.ResourceUtil;

@Service("jformUserPlanService")
@Transactional
public class JformUserPlanServiceImpl extends CommonServiceImpl implements JformUserPlanServiceI {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void delete(JformUserPlanEntity entity) throws Exception {
        super.delete(entity);
    }

    public Serializable save(JformUserPlanEntity entity) throws Exception {
        Serializable t = super.save(entity);
        return t;
    }

    public void saveOrUpdate(JformUserPlanEntity entity) throws Exception {
        super.saveOrUpdate(entity);
    }

    /**
     * 自定义按钮-[添加任务]业务处理
     *
     * @param t
     * @return
     */
    public void doAdduserplanBus(JformUserPlanEntity t) throws Exception {
        //-----------------sql增强 start----------------------------
        //-----------------sql增强 end------------------------------

        //-----------------java增强 start---------------------------
        //-----------------java增强 end-----------------------------
    }

    /**
     * 自定义按钮-[细分完成]业务处理
     *
     * @param t
     * @return
     */
    public void doDividefinishBus(JformUserPlanEntity t) throws Exception {
        //-----------------sql增强 start----------------------------
        //-----------------sql增强 end------------------------------

        //-----------------java增强 start---------------------------
        //-----------------java增强 end-----------------------------
    }

    /**
     * 判断是否二级任务下面的三级都完成了，是则返回true,否则返回false
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public boolean isAllThirdFinish(String id) throws Exception {
        long count = getCountForJdbcParam("select count(*) from jform_user_plan t where t.plan_id = ? and t.plan_issucc = ? "
                , id, ConstSetBA.PlanIsSuccess_NO);
        if (count == 0) return true;
        return false;
    }

    /**
     * 判断同二级任务是否都已经完成，是则返回true,否则返回false
     * 如果当前个人任务中 所有同工作计划中的二级任务都完成了，则更新工作计划中的二级任务为完成
     *
     * @param planid
     * @return
     * @throws Exception
     */
    @Override
    public boolean isAllSecondFinish(String planid) throws Exception {
        long count = getCountForJdbcParam("select count(*) from jform_plan t where t.plan_id = ? and t.plan_issucc = ?",
                planid, ConstSetBA.PlanIsSuccess_NO);
        return count > 0 ? false : true;
    }

    /**
     * 判断用户二级任务下的三级任务的完成状态是否都是及时完成（如果有延时完成的则不算是及时完成）
     *
     * @param taskid
     * @return
     * @throws Exception
     */
    @Override
    public boolean isUSecconFinishInTime(String taskid) throws Exception {
        long count = getCountForJdbcParam("select count(1) from jform_user_plan t where t.task_id = ? and t.plan_issucc = ?",
                taskid, ConstSetBA.PlanIsSuccess_DELAY);
        return count > 0 ? false : true;
    }

    /**
     * 判断用户三级任务下的完成状态是否都是及时完成
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public boolean isUThirdFinishInTime(String id) throws Exception {
        long count = getCountForJdbcParam("select count(1) from jform_user_plan t where t.plan_id = ? and t.plan_issucc = ?",
                id, ConstSetBA.PlanIsSuccess_DELAY);
        return count > 0 ? false : true;
    }

    /**
     * 判断是否二级任务下面的三级都完成了，是则返回true,否则返回false
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public boolean isUAllThirdFinish(String id) throws Exception {
        long count = getCountForJdbcParam("select count(*) from jform_user_plan t where t.plan_id = ? and t.plan_issucc = ? "
                , id, ConstSetBA.PlanIsSuccess_NO);
        if (count == 0) return true;
        return false;
    }

    /**
     * 判断同二级任务是否都已经完成，是则返回true,否则返回false
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public boolean isUAllSecondFinish(String id) throws Exception {
        long count = getCountForJdbcParam("select count(*) from jform_user_plan t where t.task_id = ? and t.plan_issucc = ?",
                id, ConstSetBA.PlanIsSuccess_NO);
        return count > 0 ? false : true;
    }

    /**
     * 判断工作任务计划中二级任务下的完成状态是否都是及时完成
     *
     * @param planid
     * @return
     * @throws Exception
     */
    @Override
    public boolean isSecconFinishInTime(String planid) throws Exception {
        long count = getCountForJdbcParam("select count(*) from jform_plan t where t.plan_id = ? and t.plan_issucc = ?",
                planid, ConstSetBA.PlanIsSuccess_DELAY);
        return count > 0 ? false : true;
    }

    /**
     * 判断工作任务计划中二级任务下的完成状态是否都是及时完成
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public boolean isThirdFinishInTime(String id) throws Exception {
        long count = getCountForJdbcParam("select count(*) from jform_plan t where t.plan_id = ? and t.plan_issucc = ?",
                id, ConstSetBA.PlanIsSuccess_DELAY);
        return count > 0 ? false : true;
    }

    /**
     * 获取用户二级任务完成的最大时间
     *
     * @param taskid
     * @return
     * @throws Exception
     */
    @Override
    public Date getUSecconFinishMaxTime(String taskid) throws Exception {
        String sql = "select  max(t.rfinish_date) maxdate from jform_user_plan t where t.task_id = ?";
        Map<String,Object> result = findOneForJdbc(sql,taskid);
        if(result.get("maxdate") != null) { // 当前基本有编码时
            Object date = result.get("maxdate");
            if(date instanceof Date){
                return (Date)date;
            }
        }
        return null;
    }

    /**
     * 获取用户三级任务完成的最大时间
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public Date getUThirdFinishMaxTime(String id) throws Exception {
        String sql = "select max(t.rfinish_date) maxdate from jform_user_plan t where t.plan_id = ?";
        Map<String,Object> result = findOneForJdbc(sql,id);
        if(result.get("maxdate") != null) { // 当前基本有编码时
            Object date = result.get("maxdate");
            if(date instanceof Date){
                return (Date)date;
            }
        }
        return null;
    }

    /**
     * 获取工作任务计划二级任务完成的最大时间
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public Date getSecconFinishMaxTime(String id) throws Exception {
        String sql = "select  max(t.rfinish_date) from jform_plan t where t.plan_id = ?";
        Map<String,Object> result = findOneForJdbc(sql,id);
        if(result.get("maxdate") != null) { // 当前基本有编码时
            Object date = result.get("maxdate");
            if(date instanceof Date){
                return (Date)date;
            }
        }
        return null;
    }

    /**
     * 获取工作任务计划三级任务完成的最大时间
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public Date getThirdFinishMaxTime(String id) throws Exception {
        String sql = "select  max(t.rfinish_date) from jform_plan t where t.plan_id = ?";
        Map<String,Object> result = findOneForJdbc(sql,id);
        if(result.get("maxdate") != null) { // 当前基本有编码时
            Object date = result.get("maxdate");
            if(date instanceof Date){
                return (Date)date;
            }
        }
        return null;
    }
}