package com.jeecg.jform_plan.service.impl;
import com.jeecg.jform_plan.service.JformPlanServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import com.jeecg.jform_plan.entity.JformPlanEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Service("jformPlanService")
@Transactional
public class JformPlanServiceImpl extends CommonServiceImpl implements JformPlanServiceI {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
 	public void delete(JformPlanEntity entity) throws Exception{
 		super.delete(entity);
 	}
 	
 	public Serializable save(JformPlanEntity entity) throws Exception{
 		Serializable t = super.save(entity);
 		return t;
 	}
 	
 	public void saveOrUpdate(JformPlanEntity entity) throws Exception{
 		super.saveOrUpdate(entity);
 	}
 	/**
	 * 自定义按钮-[下发细分]业务处理
	 * @param t
	 * @return
	 */
	 public void doSendDivideBus(JformPlanEntity t) throws Exception{
	 	//-----------------sql增强 start----------------------------
	 	//-----------------sql增强 end------------------------------
		 Serializable s = super.save(t);
	 	//-----------------java增强 start---------------------------
	 	//-----------------java增强 end-----------------------------
	 }
 	/**
	 * 自定义按钮-[细分完成]业务处理
	 * @param t
	 * @return
	 */
	 public void doDivideFinishBus(JformPlanEntity t) throws Exception{
	 	//-----------------sql增强 start----------------------------
	 	//-----------------sql增强 end------------------------------
		 Serializable s = super.save(t);
	 	//-----------------java增强 start---------------------------
	 	//-----------------java增强 end-----------------------------
	 }
 	/**
	 * 自定义按钮-[提交审批]业务处理
	 * @param t
	 * @return
	 */
	 public void doSendApproveBus(JformPlanEntity t) throws Exception{
	 	//-----------------sql增强 start----------------------------
	 	//-----------------sql增强 end------------------------------
		 Serializable s = super.save(t);
	 	//-----------------java增强 start---------------------------
	 	//-----------------java增强 end-----------------------------
	 }
	 /**
	 * 自定义按钮-[审批]业务处理
	 * @param t
	 * @return
	 */
	 public void doApproveBus(JformPlanEntity t) throws Exception{
	 	//-----------------sql增强 start----------------------------
	 	//-----------------sql增强 end------------------------------
		 Serializable s = super.save(t);
	 	//-----------------java增强 start---------------------------
	 	//-----------------java增强 end-----------------------------
	 }
 	
}