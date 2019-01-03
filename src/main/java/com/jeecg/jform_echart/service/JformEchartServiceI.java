package com.jeecg.jform_echart.service;
import com.jeecg.jform_echart.entity.JformEchartEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface JformEchartServiceI extends CommonService{
	
 	public void delete(JformEchartEntity entity) throws Exception;
 	
 	public Serializable save(JformEchartEntity entity) throws Exception;
 	
 	public void saveOrUpdate(JformEchartEntity entity) throws Exception;
 	
}
