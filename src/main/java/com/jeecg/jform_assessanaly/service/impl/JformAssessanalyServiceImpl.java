package com.jeecg.jform_assessanaly.service.impl;
import com.jeecg.ConstSetBA;
import com.jeecg.jform_assess.entity.JformAssessEntity;
import com.jeecg.jform_assessanaly.service.JformAssessanalyServiceI;
import com.jeecg.jform_user_plan.entity.JformUserPlanEntity;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import com.jeecg.jform_assessanaly.entity.JformAssessanalyEntity;
import org.jeecgframework.minidao.util.SimpleFormat;
import org.jeecgframework.web.system.pojo.base.TSBaseUser;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

@Service("jformAssessanalyService")
@Transactional
public class JformAssessanalyServiceImpl extends CommonServiceImpl implements JformAssessanalyServiceI {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
 	public void delete(JformAssessanalyEntity entity) throws Exception{
 		super.delete(entity);
 	}
 	
 	public Serializable save(JformAssessanalyEntity entity) throws Exception{
 		Serializable t = super.save(entity);
 		return t;
 	}
 	
 	public void saveOrUpdate(JformAssessanalyEntity entity) throws Exception{
 		super.saveOrUpdate(entity);
 	}
 	/**
	 * 自定义按钮-[重新统计]业务处理
	 * 根据用户，统计年份，统计月份重新统计
	 * @param t
	 * @return
	 */
	 public void doAnalyBus(JformAssessanalyEntity t) throws Exception{
		 List<TSBaseUser> userList = getUser(t.getEmpId());
		 doAnaly(userList,t.getAnalyYear(),t.getAnalyMonth()-1);
	 }
 	/**
	 * 自定义按钮-[统计全部]业务处理
	 * @param t
	 * @return
	 */
	 public void doAnalyallBus(JformAssessanalyEntity t) throws Exception{
		List<TSBaseUser> userList = getUser(null);
		doAnaly(userList,t.getAnalyYear(),t.getAnalyMonth()-1);
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
	 *
	 * @param userList	统计用户
	 * @param analyyear		统计年份
	 * @param analymonth	统计月份(0-11)
	 */
	 private void doAnaly(List<TSBaseUser> userList, int analyyear, int analymonth)throws Exception{
		 if(userList==null || userList.size()<=0)return;
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		calendar.set(Calendar.MILLISECOND,0);

		calendar.set(Calendar.YEAR,analyyear);
		calendar.set(Calendar.MONTH,analymonth);
		calendar.add(Calendar.MONTH,ConstSetBA.Cal_LastMonth);
		calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		//时间区间左
		Date smallerDate = calendar.getTime();

		calendar.set(Calendar.YEAR,analyyear);
		calendar.set(Calendar.MONTH,analymonth);
		calendar.add(Calendar.MONTH,ConstSetBA.Cal_NextMonth);
		calendar.set(Calendar.DAY_OF_MONTH,ConstSetBA.Cal_FirstDay);
		//时间区间右
		 Date biggerDate = calendar.getTime();

	 	/*
	 	1.根据统计人员，统计年份，统计月份，获取任务数据信息
	 	2.根据任务信息，计算出 总任务数，评分等信息
	 	3.更新或信息统计信息
	 	 */
	 	BigDecimal totalScore = BigDecimal.ZERO;
	 	BigDecimal planScore = BigDecimal.ZERO;
	 	BigDecimal personScore = BigDecimal.ZERO;
	 	int totalCount =0;
	 	int planCount =0;
	 	int personCount =0;


	 	totalScore.setScale(2);
	 	planScore.setScale(2);
	 	personScore.setScale(2);

	 	String sql = "from JformAssessEntity t where t.responderId = ? and t.finishDate BETWEEN ? and ?";
	 	for(TSBaseUser user : userList){
			totalScore = BigDecimal.ZERO;
			planScore = BigDecimal.ZERO;
			personScore = BigDecimal.ZERO;
			totalCount = 0;
			planCount = 0;
			personCount = 0;

			//获取用户任务数据并统计数据更新数据
	 		List<JformAssessEntity> planEntities = findHql(sql,user.getUserName(),smallerDate,biggerDate);
	 		totalCount = planEntities!=null ? planEntities.size():0;
	 		for(JformAssessEntity entity:planEntities){
	 			if(entity.getPlanLevel() == ConstSetBA.PlanLevel_Third){//工作计划安排
	 				planCount++;
					planScore = planScore.add(entity.getTotalScore());
					totalScore = totalScore.add(entity.getTotalScore());
				}else if(entity.getPlanLevel() == ConstSetBA.PlanLevel_User){//个人任务
	 				personCount++;
					personScore = personScore.add(entity.getTotalScore());
					totalScore = totalScore.add(entity.getTotalScore());
				}
			}
			planScore =  planScore.divide(new BigDecimal(planCount <= 0 ? 1:planCount));
			personScore = personScore.divide(new BigDecimal(personCount <= 0 ? 1:personCount));
	 		totalScore =  totalScore.divide(new BigDecimal(totalCount <= 0 ? 1:totalCount));
			JformAssessanalyEntity analyEntity = getAnalyEntity(user.getUserName(),analyyear,analymonth+1);
			analyEntity.setEmpId(user.getUserName());//用户账号
			analyEntity.setTotalCount(totalCount);//总任务数量
			analyEntity.setPlanCount(planCount);//工作计划任务数
			analyEntity.setPersonCount(personCount);//个人任务数量
			analyEntity.setTotalScore(totalScore);//总评分
			analyEntity.setPlanScore(planScore);//工作计划总评分
			analyEntity.setPersonScore(personScore);//个人任务总评分
			analyEntity.setAnalyYear(analyyear);//统计年份
			analyEntity.setAnalyMonth(analymonth+1);//统计月份//(0-11)->(1-12)
			analyEntity.setUpdateDate(new Date());//更新日期
	 		saveOrUpdate(analyEntity);
		}
	 }

	 private JformAssessanalyEntity getAnalyEntity(String username,int analyyear,int analymonth){
	 	List<JformAssessanalyEntity> list = findHql("from JformAssessanalyEntity where empId = ? " +
				"and analyYear = ? and analyMonth =?",username,analyyear,analymonth);
	 	if(list!=null && list.size()>0){
	 		return list.get(0);
		}
	 	return new JformAssessanalyEntity();
	 }
 	
}