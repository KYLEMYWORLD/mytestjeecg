package com.jeecg.jform_assess.service.impl;
import com.jeecg.jform_assess.service.JformAssessServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import com.jeecg.jform_assess.entity.JformAssessEntity;
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

@Service("jformAssessService")
@Transactional
public class JformAssessServiceImpl extends CommonServiceImpl implements JformAssessServiceI {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
 	public void delete(JformAssessEntity entity) throws Exception{
 		super.delete(entity);
 	}
 	
 	public Serializable save(JformAssessEntity entity) throws Exception{
 		Serializable t = super.save(entity);
 		return t;
 	}
 	
 	public void saveOrUpdate(JformAssessEntity entity) throws Exception{
 		super.saveOrUpdate(entity);
 	}
 	/**
	 * 自定义按钮-[评分]业务处理
	 * @param t
	 * @return
	 */
	 public void doScoreBus(JformAssessEntity t) throws Exception{
		super.saveOrUpdate(t);
	 }
 	
}