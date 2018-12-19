package com.jeecg.jform_project.service;
import com.jeecg.jform_project.entity.JformProjectEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface JformProjectServiceI extends CommonService{
	
 	public void delete(JformProjectEntity entity) throws Exception;
 	
 	public Serializable save(JformProjectEntity entity) throws Exception;
 	
 	public void saveOrUpdate(JformProjectEntity entity) throws Exception;

	/***
	 * 自定义按钮-[激活]业务处理
	 * @param t
	 * @throws Exception
	 */
	 public void doActivateBus(JformProjectEntity t) throws Exception;
}
