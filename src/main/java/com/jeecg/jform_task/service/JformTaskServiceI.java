package com.jeecg.jform_task.service;
import com.jeecg.jform_task.entity.JformTaskEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface JformTaskServiceI extends CommonService{
	
 	public void delete(JformTaskEntity entity) throws Exception;
 	
 	public Serializable save(JformTaskEntity entity) throws Exception;
 	
 	public void saveOrUpdate(JformTaskEntity entity) throws Exception;
 	
 	/**
	 * 自定义按钮-[下发]业务处理
	 * @param t table
	 * @return
	 */
	 public void doActivateBus(JformTaskEntity t) throws Exception;
}
