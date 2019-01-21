package com.jeecg.jform_test.service;
import com.jeecg.jform_test.entity.JformTestEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface JformTestServiceI extends CommonService{
	
 	public void delete(JformTestEntity entity) throws Exception;
 	
 	public Serializable save(JformTestEntity entity) throws Exception;
 	
 	public void saveOrUpdate(JformTestEntity entity) throws Exception;
 	
 	/**
	 * 自定义按钮-[测试1]业务处理
	 * @param t table-kyle
	 * @return
	 */
	 public void doTest1Bus(JformTestEntity t) throws Exception;
 	/**
	 * 自定义按钮-[测试2]业务处理
	 * @param t table-kyle
	 * @return
	 */
	 public void doTest2Bus(JformTestEntity t) throws Exception;
}
