package com.jeecg.jform_attenstatistic.service.impl;
import com.jeecg.ConstSetBA;
import com.jeecg.jform_attendance.entity.JformAttendanceEntity;
import com.jeecg.jform_attenstatistic.service.JformAttenstatisticServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import com.jeecg.jform_attenstatistic.entity.JformAttenstatisticEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.Serializable;
import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.core.util.MyClassLoader;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.cgform.enhance.CgformEnhanceJavaInter;

import org.jeecgframework.minidao.util.FreemarkerParseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.jeecgframework.core.util.ResourceUtil;

@Service("jformAttenstatisticService")
@Transactional
public class JformAttenstatisticServiceImpl extends CommonServiceImpl implements JformAttenstatisticServiceI {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
 	public void delete(JformAttenstatisticEntity entity) throws Exception{
 		super.delete(entity);
 	}
 	
 	public Serializable save(JformAttenstatisticEntity entity) throws Exception{
 		Serializable t = super.save(entity);
 		return t;
 	}
 	
 	public void saveOrUpdate(JformAttenstatisticEntity entity) throws Exception{
 		super.saveOrUpdate(entity);
 	}
 	/**
	 * 自定义按钮-[重新计算]业务处理
	 * @param t
	 * @return
	 */
	 public void doRecountBus(JformAttenstatisticEntity t) throws Exception{
	 	//-----------------sql增强 start----------------------------
	 	//-----------------sql增强 end------------------------------
	 	
	 	//-----------------java增强 start---------------------------
	 	//-----------------java增强 end-----------------------------
	 }
 	/**
	 * 自定义按钮-[重算全部]业务处理
	 * @param t
	 * @return
	 */
	 public void doRecountallBus(JformAttenstatisticEntity t) throws Exception{
	 	//-----------------sql增强 start----------------------------
	 	//-----------------sql增强 end------------------------------
	 	
	 	//-----------------java增强 start---------------------------
	 	//-----------------java增强 end-----------------------------
	 }

	/**
	 * 统计所有用户的考勤信息
	 *	只统计当月的考勤信息
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void doCountAll() throws Exception {
		//1.获取当月的节假日
		//2.获取人员信息
		//3.获取人员当前的考勤调整记录
		//4.计算出当月的统计信息

		//1.第一步	获取当月的节假日
		Calendar calendar = Calendar.getInstance();

		Date today = calendar.getTime();//今天

		calendar.set(Calendar.DAY_OF_MONTH,0);
		calendar.add(Calendar.DAY_OF_MONTH,-1);
		Date firstDayDate = calendar.getTime();//上个月最后一天
		calendar.add(Calendar.MONTH,1);
		calendar.set(Calendar.DAY_OF_MONTH,0);
		Date lastDayDate = calendar.getTime();//下个月的第一天
		calendar.add(Calendar.MONTH,-1);
		calendar.set(Calendar.DAY_OF_MONTH,0);
		Date firstDayOfTheMouth = calendar.getTime();

		List<Date> holidayList = findHql("from JformHolidayEntity e where e.holiday BETWEEN ? and ?",firstDayDate,lastDayDate);


		//2.第二步	获取人员信息
		short status =1;//激活的用户
		List<String> userList = findHql("select id from TSBaseUser e where e.status = ?",status);
		if(userList==null || userList.size()<=0) return;

		Double dutyDays = 0.0;//在岗天数
		Double gooutDays = 0.0;//出差天数
		Double leaveDays = 0.0;//请假天数
		Double offworkDays = 0.0;//调试天数

		for (String id: userList) {

			//3.第三步 获取人员当前的考勤调整记录
			//找到调整记录还没完成的(结束时间还没确定的)
			List<JformAttendanceEntity> attendance = findHql("from JformAttendanceEntity e where e.empId = ? and e.attenStatus = ?",id,
					ConstSetBA.AttendanceStatus_NotFinish);
			//如果找到还没结束的
			if(attendance.size()>0){
				JformAttendanceEntity atten = attendance.get(0);
				//1.出差的类型不算休息日
				//2.请假/休假的要剔除休息日

				int dayBetween =0;
				int dayCountOfMonth = daysBetween(firstDayOfTheMouth,today);
				//开始时间小于等于当前月的第一天
				if(atten.getBeginDate().compareTo(firstDayOfTheMouth)<=0){
					dayBetween = dayCountOfMonth;
				}else{
					//开始时间大于当前的第一天
					dayBetween = daysBetween(atten.getBeginDate(),today);
				}
				//出差	不剔除休息日
				if(atten.getAttenType()==ConstSetBA.AttendanceType_GoOut){
					gooutDays = new Double(dayBetween);

				}else if (atten.getAttenType() == ConstSetBA.AttendanceType_Leave || atten.getAttenType() == ConstSetBA.AttendanceType_OffWork){
					//调休或请假的剔除休息日
				}
			}
		}


	}

	/**
	 * 字符串日期格式和date日期格式的计算
	 * @param smdate
	 * @param bdate
	 * @return
	 * @throws ParseException
	 */
	public int daysBetween(Date smdate,Date bdate) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days=(time2-time1)/(1000*3600*24);
		return Integer.parseInt(String.valueOf(between_days));
	}


}