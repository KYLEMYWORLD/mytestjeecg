package com.jeecg.jform_attenstatistic.service.impl;
import com.jeecg.ConstSetBA;
import com.jeecg.jform_attendance.entity.JformAttendanceEntity;
import com.jeecg.jform_attenstatistic.service.JformAttenstatisticServiceI;
import com.jeecg.jform_holiday.entity.JformHolidayEntity;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import com.jeecg.jform_attenstatistic.entity.JformAttenstatisticEntity;
import org.jeecgframework.web.system.pojo.base.TSBaseUser;
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
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

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
	@Override
	@Transactional
	 public void doRecountBus(JformAttenstatisticEntity t) throws Exception{
		 List<TSBaseUser> userList = getUser(t.getEmpId());
		 doCount(userList,t.getDateYear(),t.getDateMouth()-1);
	 }
 	/**
	 * 自定义按钮-[重算全部]业务处理
	 * @param t
	 * @return
	 */
 	@Override
	@Transactional
	 public void doRecountallBus(JformAttenstatisticEntity t) throws Exception{
		 List<TSBaseUser> userList = getUser(null);
		 Calendar calendar = Calendar.getInstance();
		 doCount(userList,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH));
	 }

	/**
	 * 统计所有用户的考勤信息
	 *	只统计当月的考勤信息
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void doCountAll() throws Exception {
		List<TSBaseUser> userList = getUser(null);
		Calendar calendar = Calendar.getInstance();
		doCount(userList,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH));
	}

	private void count() throws Exception{
		//1.获取当月的节假日
		//2.获取人员信息
		//3.获取人员当前的考勤调整记录
		//4.计算出当月的统计信息

		//1.第一步	获取当月的节假日
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		calendar.set(Calendar.HOUR,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		calendar.set(Calendar.MILLISECOND,0);
		Date today = calendar.getTime();//今天

		int year4now = calendar.get(Calendar.YEAR);//当前年份
		int month4now = calendar.get(Calendar.MONTH)+1;//当前月份


		calendar.set(Calendar.DAY_OF_MONTH,1);//设置为这个月的第一天
		Date firstDayOfTheMouth = calendar.getTime();//这个月的第一天

		calendar.add(Calendar.MONTH,-1);
		calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));//通过减一获得上个月的最后一天
		Date lastDay4LastMH = calendar.getTime();//上个月最后一天


		calendar = Calendar.getInstance();//恢复当前日期
		//下个月的第一天
		calendar.add(Calendar.MONTH,1);//月份加一
		calendar.set(Calendar.DAY_OF_MONTH,1);//日期设置为第一天
		Date firstDay4NextMH = calendar.getTime();//下个月的第一天

		int times = daysBetween(firstDayOfTheMouth,today);

		//1.第一步	获取当月的节假日
		List<JformHolidayEntity> holidayList = getHolidayBetwwenDate(lastDay4LastMH,firstDay4NextMH);

		double holidayTimes = getHolidayTimes(holidayList,today);
		//2.第二步	获取人员信息
		short status =1;//激活的用户
		List<TSBaseUser> userList = getUser(null);//获取所有用户

		Double dutyDays = 0.0;//在岗天数
		Double gooutDays = 0.0;//出差天数
		Double ingogoutHoliday = 0.0;//出差期间的节假日天数
		Double leaveDays = 0.0;//请假天数
		Double offworkDays = 0.0;//调休天数
		Double temp = 0.0;
		//循环每个用户，计算考勤统计
		for (TSBaseUser user: userList) {
			dutyDays = 0.0;
			gooutDays = 0.0;
			ingogoutHoliday = 0.0;
			leaveDays = 0.0;
			offworkDays = 0.0;
			//3.第三步 获取人员当前的考勤调整记录
			//3.1首先处理考勤记录结束的数据
			List<JformAttendanceEntity> attendanceFinish = findHql("from JformAttendanceEntity e where e.empId = ? and e.attenStatus = ?"
					,user.getUserName(), ConstSetBA.AttendanceStatus_Finish);
			for(JformAttendanceEntity entity:attendanceFinish){
				if(entity.getAttenType()==ConstSetBA.AttendanceType_GoOut){
					//出差
					//1.根据出差日期计算中间的节假日天数
					//2.出差天数等于调整
					ingogoutHoliday += getHolidayBetwwenDate(holidayList,entity.getBeginDate(),entity.getMorningFree(),entity.getEndDate(),entity.getAfternoonFree());
					gooutDays +=entity.getAttenDays();//出差直接是两日期之间的时长
				}else if(entity.getAttenType() == ConstSetBA.AttendanceType_Leave){
					//请假
					//1.计算请假期间的天数
					//2.调整单天数减去节假日就是请假天数
					temp = getHolidayBetwwenDate(holidayList,entity.getBeginDate(),entity.getMorningFree(),entity.getEndDate(),entity.getAfternoonFree());
					leaveDays += (entity.getAttenDays()-temp);
				}else if(entity.getAttenType() ==ConstSetBA.AttendanceType_OffWork){
					//休假
					//1.计算请假期间的天数
					//2.调整单天数减去节假日就是请假天数
					temp = getHolidayBetwwenDate(holidayList,entity.getBeginDate(),entity.getMorningFree(),entity.getEndDate(),entity.getAfternoonFree());
					offworkDays += (entity.getAttenDays()-temp);
				}
			}

			//3.2 再调整记录还没完成的(结束时间还没确定的)
			List<JformAttendanceEntity> attendanceNotFinish = findHql("from JformAttendanceEntity e where e.empId = ? and e.attenStatus = ?"
					,user.getUserName(), ConstSetBA.AttendanceStatus_NotFinish);
			if(attendanceNotFinish.size()>0){
				JformAttendanceEntity atten = attendanceNotFinish.get(0);
				Date begindate;
				//3.2.1 如果开始时间小于等于当月第一天
				if(atten.getBeginDate().getTime() <= firstDayOfTheMouth.getTime()){
					begindate = firstDayOfTheMouth;
				}else{
					//3.2.2 如果开始时间大于当月第一天
					begindate = atten.getBeginDate();
				}
				Double attenDays = daysBetween(begindate,today)+0.0;
				if(atten.getMorningFree()==ConstSetBA.YesNo_Yes) attenDays += 0.5;
				if(atten.getAttenType()==ConstSetBA.AttendanceType_GoOut){
					//出差
					//1.根据出差日期计算中间的节假日天数
					//2.出差天数等于调整
					ingogoutHoliday += getHolidayBetwwenDate(holidayList,begindate,atten.getMorningFree(),today,ConstSetBA.YesNo_No);
					gooutDays += attenDays;//出差直接是两日期之间的时长
				}else if(atten.getAttenType() == ConstSetBA.AttendanceType_Leave){
					//请假
					//1.计算请假期间的天数
					//2.调整单天数减去节假日就是请假天数
					temp = getHolidayBetwwenDate(holidayList,begindate,atten.getMorningFree(),today,ConstSetBA.YesNo_No);
					leaveDays += (attenDays-temp);
				}else if(atten.getAttenType() ==ConstSetBA.AttendanceType_OffWork){
					//休假
					//1.计算请假期间的天数
					//2.调整单天数减去节假日就是请假天数
					temp = getHolidayBetwwenDate(holidayList,begindate,atten.getMorningFree(),today,ConstSetBA.YesNo_No);
					offworkDays += (attenDays-temp);
				}
			}
			//在岗天数 = 当月到现在的天数-（到今天之间的节假日)-请假-调休-外出+外出中间的假期
			dutyDays = times-holidayTimes-leaveDays-offworkDays-gooutDays+ingogoutHoliday ;
			//4. 如果没记录，新增，如果有，修改
			JformAttenstatisticEntity entity = getRecentAttenstaticEntity(user.getUserName(),year4now,month4now);
			entity.setEmpId(user.getUserName());
			entity.setEmpName(user.getRealName());
			entity.setDutyDays(dutyDays);
			entity.setGooutDays(gooutDays);
			entity.setLeaveDays(leaveDays);
			entity.setOffworkDays(offworkDays);
			entity.setDateMouth(month4now);
			entity.setDateYear(year4now);
			entity.setUpdateDate(new Date());
			saveOrUpdate(entity);
		}
	}

	/**
	 * 获取人员信息
	 * @param username null 获取所有人员，非Null则获取指定人员
	 * @return
	 */
	private List<TSBaseUser> getUser(String username){
		StringBuffer sql = new StringBuffer();
		sql.append("from TSBaseUser e where e.status = ? ");
		short status =1;//激活的用户
		if(username!=null){
			sql.append(" and e.userName = ? ");
			return findHql(sql.toString(),status,username);
		}else{
			return findHql(sql.toString(),status);
		}
	}


	/**
	 * 	 * 统计考勤记录
	 * @param userList	需要统计的用户数据
	 * @param countYear	统计的年份
	 * @param countMonth	统计的月份(0-11)
	 * @throws Exception
	 */
	private void doCount(List<TSBaseUser> userList,int countYear,int countMonth) throws Exception{

		if(userList==null || userList.size()<=0) return;
		if(countMonth==-1 || countYear <=0 ) return;

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		calendar.set(Calendar.MILLISECOND,0);

		//现在 年份，月份
		int nowYear = calendar.get(Calendar.YEAR);
		int nowMonth = calendar.get(Calendar.MONTH);

		//设置当前需要统计的年份和月份
		calendar.set(Calendar.YEAR,countYear);
		calendar.set(Calendar.MONTH,countMonth);
		calendar.set(Calendar.DAY_OF_MONTH,ConstSetBA.Cal_FirstDay);//第一天

		//统计月份的第一天
		Date calBeginDate = calendar.getTime();


		//需要获取 指定月份的第一天，这个月份上个月最后一天，和下个月的第一天
		calendar.add(Calendar.MONTH,ConstSetBA.Cal_LastMonth);
		calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));//通过减一获得上个月的最后一天
		Date smallerDate = calendar.getTime();//上个月最后一天

		Date biggerDate;//统计区间的后一天
		Date calEndDate;//统计的最后一天
		if(nowYear == countYear && nowMonth == countMonth){
			//计算当月的数据
			calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR,0);
			calendar.set(Calendar.MINUTE,0);
			calendar.set(Calendar.SECOND,0);
			calendar.set(Calendar.MILLISECOND,0);
			calEndDate = calendar.getTime();//统计的最后一天

			calendar.add(Calendar.DAY_OF_MONTH,ConstSetBA.One);
			biggerDate = calendar.getTime();//明天

		}else{
			//非当月统计 则计算月区间
			calendar.set(Calendar.MONTH,countMonth);
			calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			calEndDate = calendar.getTime();//统计的最后一天

			calendar.add(Calendar.MONTH,ConstSetBA.Cal_NextMonth);//月份加一
			calendar.set(Calendar.DAY_OF_MONTH,ConstSetBA.Cal_FirstDay);//日期设置为第一天
			biggerDate = calendar.getTime();//下个月的第一天
		}

		//计算统计期间的天数
		int times = daysBetween(calBeginDate,calEndDate);

		//1.第一步	获取当月的节假日
		List<JformHolidayEntity> holidayList = getHolidayBetwwenDate(smallerDate,biggerDate);

		//获取统计期间的 节假总天数
		double holidayTimes = getHolidayTimes(holidayList,biggerDate);


		Double dutyDays = 0.0;//在岗天数
		Double gooutDays = 0.0;//出差天数
		Double ingogoutHoliday = 0.0;//出差期间的节假日天数
		Double leaveDays = 0.0;//请假天数
		Double offworkDays = 0.0;//调休天数
		Double temp = 0.0;
		//循环每个用户，计算考勤统计
		for (TSBaseUser user: userList) {
			dutyDays = 0.0;
			gooutDays = 0.0;
			ingogoutHoliday = 0.0;
			leaveDays = 0.0;
			offworkDays = 0.0;
			//3.第三步 获取人员当前的考勤调整记录
			//3.1首先处理考勤记录结束的数据
			List<JformAttendanceEntity> attendanceFinish = findHql("from JformAttendanceEntity e where e.empId = ? and e.attenStatus = ? and" +
							" ( e.beginDate BETWEEN ? and ?  or e.endDate  BETWEEN ? and ? )",user.getUserName(), ConstSetBA.AttendanceStatus_Finish,
							smallerDate,biggerDate,smallerDate,biggerDate);
			for(JformAttendanceEntity entity:attendanceFinish){
				//如果调整日期小于上个月的最后一天，则将开始时间设置为月初
				if(entity.getBeginDate().getTime() <= smallerDate.getTime()){
					entity.setBeginDate(calBeginDate);
				}
				if(entity.getEndDate().getTime() >=  biggerDate.getTime()){
					entity.setEndDate(calEndDate);
				}
				Double attenDays = daysBetween(entity.getBeginDate(),entity.getEndDate())+0.0;

				if(entity.getAttenType()==ConstSetBA.AttendanceType_GoOut){
					//出差
					//1.根据出差日期计算中间的节假日天数
					//2.出差天数等于调整
					ingogoutHoliday += getHolidayBetwwenDate(holidayList,entity.getBeginDate(),entity.getMorningFree(),entity.getEndDate(),entity.getAfternoonFree());
					gooutDays += attenDays;//entity.getAttenDays();//出差直接是两日期之间的时长
				}else if(entity.getAttenType() == ConstSetBA.AttendanceType_Leave){
					//请假
					//1.计算请假期间的天数
					//2.调整单天数减去节假日就是请假天数
					temp = getHolidayBetwwenDate(holidayList,entity.getBeginDate(),entity.getMorningFree(),entity.getEndDate(),entity.getAfternoonFree());
					leaveDays += (attenDays - temp);//(entity.getAttenDays()-temp);
				}else if(entity.getAttenType() ==ConstSetBA.AttendanceType_OffWork){
					//休假
					//1.计算请假期间的天数
					//2.调整单天数减去节假日就是请假天数
					temp = getHolidayBetwwenDate(holidayList,entity.getBeginDate(),entity.getMorningFree(),entity.getEndDate(),entity.getAfternoonFree());
					offworkDays += (attenDays - temp);//(entity.getAttenDays()-temp);
				}
			}

			//3.2 再调整记录还没完成的(结束时间还没确定的)
			List<JformAttendanceEntity> attendanceNotFinish = findHql("from JformAttendanceEntity e where e.empId = ? and e.attenStatus = ? and " +
							" e.beginDate BETWEEN ? and ? "
					,user.getUserName(), ConstSetBA.AttendanceStatus_NotFinish,smallerDate,biggerDate);
			if(attendanceNotFinish.size()>0){
				JformAttendanceEntity atten = attendanceNotFinish.get(0);

				//3.2.1 如果开始时间小于等于当月第一天
				if(atten.getBeginDate().getTime() <= calBeginDate.getTime()){
					atten.setBeginDate(calBeginDate);
				}

				Double attenDays = daysBetween(atten.getBeginDate(),calEndDate)+0.0;

				if(atten.getMorningFree()==ConstSetBA.YesNo_Yes) attenDays += 0.5;
				if(atten.getAttenType()==ConstSetBA.AttendanceType_GoOut){
					//出差
					//1.根据出差日期计算中间的节假日天数
					//2.出差天数等于调整
					ingogoutHoliday += getHolidayBetwwenDate(holidayList,atten.getBeginDate(),atten.getMorningFree(),calEndDate,ConstSetBA.YesNo_No);
					gooutDays += attenDays;//出差直接是两日期之间的时长
				}else if(atten.getAttenType() == ConstSetBA.AttendanceType_Leave){
					//请假
					//1.计算请假期间的天数
					//2.调整单天数减去节假日就是请假天数
					temp = getHolidayBetwwenDate(holidayList,atten.getBeginDate(),atten.getMorningFree(),calEndDate,ConstSetBA.YesNo_No);
					leaveDays += (attenDays-temp);
				}else if(atten.getAttenType() ==ConstSetBA.AttendanceType_OffWork){
					//休假
					//1.计算请假期间的天数
					//2.调整单天数减去节假日就是请假天数
					temp = getHolidayBetwwenDate(holidayList,atten.getBeginDate(),atten.getMorningFree(),calEndDate,ConstSetBA.YesNo_No);
					offworkDays += (attenDays-temp);
				}
			}
			//在岗天数 = 当月到现在的天数-（到今天之间的节假日)-请假-调休-外出+外出中间的假期
			dutyDays = times-holidayTimes-leaveDays-offworkDays-gooutDays+ingogoutHoliday ;
			//4. 如果没记录，新增，如果有，修改
			JformAttenstatisticEntity entity = getRecentAttenstaticEntity(user.getUserName(),countYear,countMonth);
			entity.setEmpId(user.getUserName());
			entity.setEmpName(user.getRealName());
			entity.setDutyDays(dutyDays);
			entity.setGooutDays(gooutDays);
			entity.setLeaveDays(leaveDays);
			entity.setOffworkDays(offworkDays);
			entity.setDateMouth(countMonth+1);
			entity.setDateYear(countYear);
			entity.setUpdateDate(new Date());
			saveOrUpdate(entity);
		}
	}


	private List<JformHolidayEntity> getHolidayBetwwenDate(Date begin,Date end){
		return findHql("from JformHolidayEntity e where e.holiday BETWEEN ? and ? ",begin,end);
	}

	public int differentDayMillisecond (Date date1,Date date2)
	{
		int day = (int)((date2.getTime()-date1.getTime())/(3600*1000*24));
		return day;
	}

	/**
	 *
	 * @param username	用户民
	 * @param year	统计年份
	 * @param month	统计月份(需要加1，s数据库中保存的月份是1-12)
	 * @return
	 */
	private JformAttenstatisticEntity getRecentAttenstaticEntity(String username,int year,int month){
		List<JformAttenstatisticEntity> entities = findHql("from JformAttenstatisticEntity where empId = ? " +
				"and dateYear = ? and dateMouth = ?",username,year,month+1);
		if(entities!=null && entities.size()>0){
			return entities.get(0);
		}
		return new JformAttenstatisticEntity();
	}

	//获取月初到今天之间的 节假日天数
	private double getHolidayTimes(List<JformHolidayEntity> holiday,Date date){
		double tims = 0.0;
		for(JformHolidayEntity entity : holiday){
			if(entity.getHoliday().getTime()>date.getTime())continue;
			if(entity.getTimes() == ConstSetBA.HolidayType_WholeDay){
				tims +=1.0;
			}else{
				tims +=0.5;
			}
		}
		return tims;
	}

	/**
	 * 计算两个日期之间的节假日天数
	 * @param holiday
	 * @param begindate
	 * @param enddate
	 * @return
	 */
	private Double getHolidayBetwwenDate(List<JformHolidayEntity> holiday,Date begindate,int morningfree,Date enddate,int afternonnfree){

		Double holidays = 0.0;//调整日期期间的节假日天数

		//循环结日 判断考勤开始结束之间是否有节假日的时间
		for(JformHolidayEntity entity : holiday) {
			long tims = entity.getHoliday().getTime();
//			if (tims == begindate.getTime()) {
//				//节假日 等于 调整开始日期
//				holidays +=getTims(entity,morningfree,-1);
//			} else if (tims == enddate.getTime()) {
//				//同时 小于等于 调整结束日期
//				holidays +=getTims(entity,-1,afternonnfree);
//			} else
			if(tims >= begindate.getTime() && tims <= enddate.getTime()){
				//在 调整日期期间
				holidays += getTims(entity,morningfree,afternonnfree);
			}
		}
		return holidays;
	}

	//判断结日和调整的日期是否有交集

	/**
	 * 返回调整日期之间的节假日天数
	 * @param entity
	 * @param morningfree
	 * @param afternoonfree
	 * @return
	 */
	public Double getTims(JformHolidayEntity entity ,int morningfree,int afternoonfree){


		//节假日是上午
		if(entity.getTimes() == ConstSetBA.HolidayType_Morning){
			//如果调整日期不包括上午:morningfree=1 则返回0.0
			//如果调整日期包括上午:morningfree=0 则返回0.5
			return morningfree == ConstSetBA.YesNo_Yes ? 0.0 : 0.5;
		}

		//1.26 times 2
		//节假日是下午
		if(entity.getTimes() == ConstSetBA.HolidayType_Afternoon){
			//如果调整日期不包括下午:afternoonfree=1 则返回0.0
			//如果调整日期包括下午:afternoonfree=0 则返回0.5
			return  afternoonfree == ConstSetBA.YesNo_Yes ? 0.0 : 0.5;
		}

		//节假日是全天
		if(entity.getTimes() == ConstSetBA.HolidayType_WholeDay){
			if(morningfree==ConstSetBA.YesNo_Yes) return 0.5;
			if(afternoonfree==ConstSetBA.YesNo_Yes) return 0.5;
			return 1.0;
		}
		return 0.0;
	}


	/**
	 * 字符串日期格式和date日期格式的计算
	 * @param smdate
	 * @param bdate
	 * @return
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate,Date bdate) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days=(time2-time1)/(1000*3600*24);
		return Integer.parseInt(String.valueOf(between_days))+1;//1.1 到 1.1 算一天
	}

	public static void main(String[] agvs) throws Exception{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
		Date date = simpleDateFormat.parse("2019-01-01");
		Date date1 = simpleDateFormat.parse("2019-01-02");
		System.out.println(daysBetween(date,date1));
	}

}