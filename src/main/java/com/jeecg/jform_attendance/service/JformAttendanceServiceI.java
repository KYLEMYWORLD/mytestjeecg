package com.jeecg.jform_attendance.service;
import com.jeecg.jform_attendance.entity.JformAttendanceEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface JformAttendanceServiceI extends CommonService{
	
 	public void delete(JformAttendanceEntity entity) throws Exception;
 	
 	public Serializable save(JformAttendanceEntity entity) throws Exception;
 	
 	public void saveOrUpdate(JformAttendanceEntity entity) throws Exception;
 	
}
