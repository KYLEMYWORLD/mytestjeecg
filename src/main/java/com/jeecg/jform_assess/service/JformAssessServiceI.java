package com.jeecg.jform_assess.service;
import com.jeecg.jform_assess.entity.JformAssessEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface JformAssessServiceI extends CommonService{
	
 	public void delete(JformAssessEntity entity) throws Exception;
 	
 	public Serializable save(JformAssessEntity entity) throws Exception;
 	
 	public void saveOrUpdate(JformAssessEntity entity) throws Exception;
 	
 	/**
	 * 自定义按钮-[评分]业务处理
	 * @param t table-kyle
	 * @return
	 */
	 public void doScoreBus(JformAssessEntity t) throws Exception;
}
