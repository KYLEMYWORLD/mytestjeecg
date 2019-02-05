package com.jeecg.jform_assessanaly.service;
import com.jeecg.jform_assessanaly.entity.JformAssessanalyEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface JformAssessanalyServiceI extends CommonService{
	
 	public void delete(JformAssessanalyEntity entity) throws Exception;
 	
 	public Serializable save(JformAssessanalyEntity entity) throws Exception;
 	
 	public void saveOrUpdate(JformAssessanalyEntity entity) throws Exception;
 	
 	/**
	 * 自定义按钮-[重新统计]业务处理
	 * @param t table-kyle
	 * @return
	 */
	 public void doAnalyBus(JformAssessanalyEntity t) throws Exception;
 	/**
	 * 自定义按钮-[统计全部]业务处理
	 * @param t table-kyle
	 * @return
	 */
	 public void doAnalyallBus(JformAssessanalyEntity t) throws Exception;

}
