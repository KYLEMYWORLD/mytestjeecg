package com.jeecg.jform_holiday.service;
import com.jeecg.jform_holiday.entity.JformHolidayEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface JformHolidayServiceI extends CommonService{
	
 	public void delete(JformHolidayEntity entity) throws Exception;
 	
 	public Serializable save(JformHolidayEntity entity) throws Exception;
 	
 	public void saveOrUpdate(JformHolidayEntity entity) throws Exception;

	/**
	 * 初始化全年的周末 周六0.5 周日1 的节假日
	 * @throws Exception
	 */
 	public void initYearHoliday() throws Exception;
}
