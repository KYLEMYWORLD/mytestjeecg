package com.jeecg.jform_attenstatistic.service;
import com.jeecg.jform_attenstatistic.entity.JformAttenstatisticEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface JformAttenstatisticServiceI extends CommonService{
	
 	public void delete(JformAttenstatisticEntity entity) throws Exception;
 	
 	public Serializable save(JformAttenstatisticEntity entity) throws Exception;
 	
 	public void saveOrUpdate(JformAttenstatisticEntity entity) throws Exception;
 	
 	/**
	 * 自定义按钮-[重新计算]业务处理
	 * @param t table-kyle
	 * @return
	 */
	 public void doRecountBus(JformAttenstatisticEntity t) throws Exception;
 	/**
	 * 自定义按钮-[重算全部]业务处理
	 * @param t table-kyle
	 * @return
	 */
	 public void doRecountallBus(JformAttenstatisticEntity t) throws Exception;

	/**
	 * 统计所有用户的考勤信息
	 * @throws Exception
	 */
	public void doCountAll() throws Exception;

}
