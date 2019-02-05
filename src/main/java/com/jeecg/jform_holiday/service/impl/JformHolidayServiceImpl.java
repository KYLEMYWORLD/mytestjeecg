package com.jeecg.jform_holiday.service.impl;
import com.jeecg.ConstSetBA;
import com.jeecg.jform_holiday.service.JformHolidayServiceI;
import com.sun.star.util.DateTime;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import com.jeecg.jform_holiday.entity.JformHolidayEntity;
import org.jeecgframework.core.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.io.Serializable;

import org.jeecgframework.web.cgform.enhance.CgformEnhanceJavaInter;

import org.jeecgframework.minidao.util.FreemarkerParseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Service("jformHolidayService")
@Transactional
public class JformHolidayServiceImpl extends CommonServiceImpl implements JformHolidayServiceI {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
 	public void delete(JformHolidayEntity entity) throws Exception{
 		super.delete(entity);
 	}
 	
 	public Serializable save(JformHolidayEntity entity) throws Exception{
 		Serializable t = super.save(entity);
 		return t;
 	}
 	
 	public void saveOrUpdate(JformHolidayEntity entity) throws Exception{
 		super.saveOrUpdate(entity);
 	}

	/**
	 * 初始化全年的周末 周六0.5 周日1 的节假日
	 *每年的一月一号0点执行
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void initYearHoliday() throws Exception {
		LogUtil.info("===============初始化全年的休息日开始=================");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.MONTH,0);
		calendar.set(Calendar.DAY_OF_MONTH,1);
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		calendar.set(Calendar.MILLISECOND,0);
		int year = calendar.get(Calendar.YEAR);
		List<JformHolidayEntity> jformHolidayEntityList = findHql("from JformHolidayEntity e where e.holiday > ?", calendar.getTime());
		if(jformHolidayEntityList!=null && jformHolidayEntityList.size()>0){
			//找到有节假日信息则不生成
			return;
		}

		int  i = 1;
		while (calendar.get(Calendar.YEAR) < year + 1) {
			calendar.set(Calendar.WEEK_OF_YEAR, i++);
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			if (calendar.get(Calendar.YEAR) == year) {
				//System.out.printf("星期天：%tF%n", calendar);
				JformHolidayEntity entity = new JformHolidayEntity();
				entity.setHoliday(calendar.getTime());
				entity.setTimes(ConstSetBA.HolidayType_WholeDay);//周日 全天
				saveOrUpdate(entity);
			}
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			if (calendar.get(Calendar.YEAR) == year) {
				//System.out.printf("星期六：%tF%n", calendar);
				JformHolidayEntity entity = new JformHolidayEntity();
				entity.setHoliday(calendar.getTime());
				entity.setTimes(ConstSetBA.HolidayType_Afternoon);//周六 下午
				saveOrUpdate(entity);
			}
		}
		LogUtil.info("===============初始化全年的休息日结束=================");
	}
}