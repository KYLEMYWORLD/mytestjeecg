package com.jeecg.jform_plan.service;
import com.jeecg.jform_plan.entity.JformPlanEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface JformPlanServiceI extends CommonService{
	
 	public void delete(JformPlanEntity entity) throws Exception;
 	
 	public Serializable save(JformPlanEntity entity) throws Exception;
 	
 	public void saveOrUpdate(JformPlanEntity entity) throws Exception;
 	
 	/**
	 * 自定义按钮-[下发细分]业务处理
	 * @param t table-kyle
	 * @return
	 */
	 public void doSendDivideBus(JformPlanEntity t) throws Exception;
 	/**
	 * 自定义按钮-[细分完成]业务处理
	 * @param t table-kyle
	 * @return
	 */
	 public void doDivideFinishBus(JformPlanEntity t) throws Exception;
 	/**
	 * 自定义按钮-[提交审批]业务处理
	 * @param t table-kyle
	 * @return
	 */
	 public void doSendApproveBus(JformPlanEntity t) throws Exception;

	 /**
	 * 自定义按钮-[审批]业务处理
	 * @param t table-kyle
	 * @return
	 */
	 public void doApproveBus(JformPlanEntity t) throws Exception;
}
