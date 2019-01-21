package com.jeecg.jform_test.service.impl;
import com.jeecg.jform_test.service.JformTestServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import com.jeecg.jform_test.entity.JformTestEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.io.Serializable;
import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.core.util.MyClassLoader;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.cgform.enhance.CgformEnhanceJavaInter;

import org.jeecgframework.minidao.util.FreemarkerParseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.jeecgframework.core.util.ResourceUtil;

@Service("jformTestService")
@Transactional
public class JformTestServiceImpl extends CommonServiceImpl implements JformTestServiceI {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
 	public void delete(JformTestEntity entity) throws Exception{
 		super.delete(entity);
 	}
 	
 	public Serializable save(JformTestEntity entity) throws Exception{
 		Serializable t = super.save(entity);
 		return t;
 	}
 	
 	public void saveOrUpdate(JformTestEntity entity) throws Exception{
 		super.saveOrUpdate(entity);
 	}
 	/**
	 * 自定义按钮-[测试1]业务处理
	 * @param t
	 * @return
	 */
	 public void doTest1Bus(JformTestEntity t) throws Exception{
	 	//-----------------sql增强 start----------------------------
	 	//-----------------sql增强 end------------------------------
	 	
	 	//-----------------java增强 start---------------------------
	 	//-----------------java增强 end-----------------------------
	 }
 	/**
	 * 自定义按钮-[测试2]业务处理
	 * @param t
	 * @return
	 */
	 public void doTest2Bus(JformTestEntity t) throws Exception{
	 	//-----------------sql增强 start----------------------------
	 	//-----------------sql增强 end------------------------------
	 	
	 	//-----------------java增强 start---------------------------
	 	//-----------------java增强 end-----------------------------
	 }
 	
}