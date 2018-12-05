package com.jeecg.project.service;
import com.jeecg.project.entity.JformProjectEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface JformProjectServiceI extends CommonService{
	
 	public void delete(JformProjectEntity entity) throws Exception;
 	
 	public Serializable save(JformProjectEntity entity) throws Exception;
 	
 	public void saveOrUpdate(JformProjectEntity entity) throws Exception;
 	
}
