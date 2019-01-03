package com.jeecg.jform_echart.service.impl;
import com.jeecg.jform_echart.service.JformEchartServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import com.jeecg.jform_echart.entity.JformEchartEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Service("jformEchartService")
@Transactional
public class JformEchartServiceImpl extends CommonServiceImpl implements JformEchartServiceI {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
 	public void delete(JformEchartEntity entity) throws Exception{
 		super.delete(entity);
 	}
 	
 	public Serializable save(JformEchartEntity entity) throws Exception{
 		Serializable t = super.save(entity);
 		return t;
 	}
 	
 	public void saveOrUpdate(JformEchartEntity entity) throws Exception{
 		super.saveOrUpdate(entity);
 	}
 	
}