package com.jeecg.jform_user_plan.service;
import com.jeecg.jform_user_plan.entity.JformUserPlanEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;
import java.util.Date;

public interface JformUserPlanServiceI extends CommonService{
	
 	public void delete(JformUserPlanEntity entity) throws Exception;
 	
 	public Serializable save(JformUserPlanEntity entity) throws Exception;
 	
 	public void saveOrUpdate(JformUserPlanEntity entity) throws Exception;
 	
 	/**
	 * 自定义按钮-[添加任务]业务处理
	 * @param t table-kyle
	 * @return
	 */
	 public void doAdduserplanBus(JformUserPlanEntity t) throws Exception;
 	/**
	 * 自定义按钮-[细分完成]业务处理
	 * @param t table-kyle
	 * @return
	 */
	 public void doDividefinishBus(JformUserPlanEntity t) throws Exception;

	/**
	 * 判断是否二级任务下面的三级都完成了，是则返回true,否则返回false
	 * @param id
	 * @return
	 * @throws Exception
	 */
	 public boolean isUAllThirdFinish(String id) throws Exception;

	/**
	 * 判断同二级任务是否都已经完成，是则返回true,否则返回false
	 * @param id
	 * @return
	 * @throws Exception
	 */
	 public boolean isUAllSecondFinish(String id) throws Exception;

	 /**
	 * 判断用户二级任务下的完成状态是否都是及时完成
	 * @param id
	 * @return
	 * @throws Exception
	 */
	 public boolean isUSecconFinishInTime(String id) throws Exception;


	/**
	 * 获取用户二级任务完成的最大时间
	 * @param taskid
	 * @return
	 * @throws Exception
	 */
	public Date getUSecconFinishMaxTime(String taskid) throws Exception;


	/**
	 * 判断用户二级任务下的完成状态是否都是及时完成
	 * @param id
	 * @return
	 * @throws Exception
	 */
	 public boolean isUThirdFinishInTime(String id) throws Exception;


	/**
	 * 获取用户三级任务完成的最大时间
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Date getUThirdFinishMaxTime(String id) throws Exception;


	/**
	 * 判断是否二级任务下面的三级都完成了，是则返回true,否则返回false
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean isAllThirdFinish(String id) throws Exception;

	/**
	 * 判断同二级任务是否都已经完成，是则返回true,否则返回false
	 * @param planid
	 * @return
	 * @throws Exception
	 */
	public boolean isAllSecondFinish(String planid) throws Exception;

	/**
	 * 判断工作任务计划中二级任务下的完成状态是否都是及时完成
	 * @param planid
	 * @return
	 * @throws Exception
	 */
	public boolean isSecconFinishInTime(String planid) throws Exception;

	/**
	 * 判断工作任务计划中三级任务下的完成状态是否都是及时完成
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean isThirdFinishInTime(String id) throws Exception;


	/**
	 * 获取工作任务计划二级任务完成的最大时间
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Date getSecconFinishMaxTime(String id) throws Exception;



	/**
	 * 获取工作任务计划三级任务完成的最大时间
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Date getThirdFinishMaxTime(String id) throws Exception;
}
