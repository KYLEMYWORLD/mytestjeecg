package com.jeecg.jform_user_plan.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.lang.String;
import java.lang.Double;
import java.lang.Integer;
import java.math.BigDecimal;
import javax.xml.soap.Text;
import java.sql.Blob;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.SequenceGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;

/**   
 * @Title: Entity
 * @Description: 个人计划安排
 * @author onlineGenerator
 * @date 2019-01-14 10:05:04
 * @version V1.0   
 *
 */
@Entity
@Table(name = "jform_user_plan", schema = "")
@SuppressWarnings("serial")
public class JformUserPlanEntity implements java.io.Serializable {
	/**id*/
	private java.lang.String id;
	/**任务名称*/
	@Excel(name="任务名称",width=15)
	private java.lang.String planName;
	/**任务等级*/
	@Excel(name="任务等级",width=15)
	private java.lang.Integer planLevel;
	/**任务顺序*/
	private java.lang.Integer planOrder;
	/**任务父ID*/
	@Excel(name="任务父ID",width=15)
	private java.lang.String planId;
	/**任务开始时间*/
	@Excel(name="任务开始时间",width=15,format = "yyyy-MM-dd")
	private java.util.Date startDate;
	/**任务完成时间*/
	@Excel(name="任务完成时间",width=15,format = "yyyy-MM-dd")
	private java.util.Date finishDate;
	/**任务完成时间*/
	@Excel(name="实际完成时间",width=15,format = "yyyy-MM-dd")
	private java.util.Date realfinishDate;
	/**任务说明*/
	@Excel(name="任务说明",width=15)
	private java.lang.String planInfo;
	/**负责人ID*/
	private java.lang.String planResponderid;
	/**负责人*/
	@Excel(name="负责人",width=15)
	private java.lang.String planResponder;
	/**负责人*/
	@Excel(name="负责人",width=15)
	private java.lang.String planResponders;
	/**任务状态*/
	@Excel(name="任务状态",width=15)
	private java.lang.Integer planStatus;
	/**审批内容*/
	@Excel(name="审批内容",width = 15)
	private java.lang.String planRejectmsg;
	/**完成状态*/
	@Excel(name="完成状态",width=15)
	private java.lang.Integer planIssucc;
	/**延时原因*/
	@Excel(name="延时原因",width=15)
	private java.lang.String planLatemsg;
	/**预警状态*/
	@Excel(name="预警状态",width=15)
	private java.lang.Integer planIsalert;
	/**预警原因*/
	@Excel(name="预警原因",width=15)
	private java.lang.String planAlertmsg;
	/**项目ID*/
	private java.lang.String projectId;
	/**分配任务ID*/
	private java.lang.String taskId;
	/**创建人名称*/
	private java.lang.String createName;
	/**创建人登录名称*/
	private java.lang.String createBy;
	/**更新人登录名称*/
	private java.lang.String updateBy;
	/**更新人名称*/
	private java.lang.String updateName;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新日期*/
	private java.util.Date updateDate;
	/**所属部门*/
	private java.lang.String sysOrgCode;
	/**所属公司*/
	private java.lang.String sysCompanyCode;
	/**流程状态*/
	private java.lang.String bpmStatus;
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  id
	 */
	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")

	@Column(name ="ID",nullable=false,length=36)
	public java.lang.String getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  id
	 */
	public void setId(java.lang.String id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  任务名称
	 */

	@Column(name ="PLAN_NAME",nullable=true,length=50)
	public java.lang.String getPlanName(){
		return this.planName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  任务名称
	 */
	public void setPlanName(java.lang.String planName){
		this.planName = planName;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  任务等级
	 */

	@Column(name ="PLAN_LEVEL",nullable=true,length=10)
	public java.lang.Integer getPlanLevel(){
		return this.planLevel;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  任务等级
	 */
	public void setPlanLevel(java.lang.Integer planLevel){
		this.planLevel = planLevel;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  任务顺序
	 */

	@Column(name ="PLAN_ORDER",nullable=true,length=10)
	public java.lang.Integer getPlanOrder(){
		return this.planOrder;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  任务顺序
	 */
	public void setPlanOrder(java.lang.Integer planOrder){
		this.planOrder = planOrder;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  任务父ID
	 */

	@Column(name ="PLAN_ID",nullable=true,length=36)
	public java.lang.String getPlanId(){
		return this.planId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  任务父ID
	 */
	public void setPlanId(java.lang.String planId){
		this.planId = planId;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  任务开始时间
	 */

	@Column(name ="START_DATE",nullable=true)
	public java.util.Date getStartDate(){
		return this.startDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  任务开始时间
	 */
	public void setStartDate(java.util.Date startDate){
		this.startDate = startDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  任务完成时间
	 */

	@Column(name ="FINISH_DATE",nullable=true)
	public java.util.Date getFinishDate(){
		return this.finishDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  任务完成时间
	 */
	public void setFinishDate(java.util.Date finishDate){
		this.finishDate = finishDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  任务说明
	 */

	@Column(name ="PLAN_INFO",nullable=true)
	public java.lang.String getPlanInfo(){
		return this.planInfo;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  任务说明
	 */
	public void setPlanInfo(java.lang.String planInfo){
		this.planInfo = planInfo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  负责人ID
	 */

	@Column(name ="PLAN_RESPONDERID",nullable=true)
	public java.lang.String getPlanResponderid(){
		return this.planResponderid;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  负责人ID
	 */
	public void setPlanResponderid(java.lang.String planResponderid){
		this.planResponderid = planResponderid;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  负责人
	 */

	@Column(name ="PLAN_RESPONDER",nullable=true,length=32)
	public java.lang.String getPlanResponder(){
		return this.planResponder;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  负责人
	 */
	public void setPlanResponder(java.lang.String planResponder){
		this.planResponder = planResponder;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  任务状态
	 */

	@Column(name ="PLAN_STATUS",nullable=true,length=10)
	public java.lang.Integer getPlanStatus(){
		return this.planStatus;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  任务状态
	 */
	public void setPlanStatus(java.lang.Integer planStatus){
		this.planStatus = planStatus;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  审批内容
	 */

	@Column(name ="PLAN_REJECTMSG",nullable=true,length=32)
	public java.lang.String getPlanRejectmsg(){
		return this.planRejectmsg;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  审批内容
	 */
	public void setPlanRejectmsg(java.lang.String planRejectmsg){
		this.planRejectmsg = planRejectmsg;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  完成状态
	 */

	@Column(name ="PLAN_ISSUCC",nullable=true,length=10)
	public java.lang.Integer getPlanIssucc(){
		return this.planIssucc;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  完成状态
	 */
	public void setPlanIssucc(java.lang.Integer planIssucc){
		this.planIssucc = planIssucc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  延时原因
	 */

	@Column(name ="PLAN_LATEMSG",nullable=true,length=32)
	public java.lang.String getPlanLatemsg(){
		return this.planLatemsg;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  延时原因
	 */
	public void setPlanLatemsg(java.lang.String planLatemsg){
		this.planLatemsg = planLatemsg;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  预警状态
	 */

	@Column(name ="PLAN_ISALERT",nullable=true,length=10)
	public java.lang.Integer getPlanIsalert(){
		return this.planIsalert;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  预警状态
	 */
	public void setPlanIsalert(java.lang.Integer planIsalert){
		this.planIsalert = planIsalert;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  预警原因
	 */

	@Column(name ="PLAN_ALERTMSG",nullable=true,length=32)
	public java.lang.String getPlanAlertmsg(){
		return this.planAlertmsg;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  预警原因
	 */
	public void setPlanAlertmsg(java.lang.String planAlertmsg){
		this.planAlertmsg = planAlertmsg;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  项目ID
	 */

	@Column(name ="PROJECT_ID",nullable=true,length=36)
	public java.lang.String getProjectId(){
		return this.projectId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  项目ID
	 */
	public void setProjectId(java.lang.String projectId){
		this.projectId = projectId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  分配任务ID
	 */

	@Column(name ="TASK_ID",nullable=true,length=36)
	public java.lang.String getTaskId(){
		return this.taskId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  分配任务ID
	 */
	public void setTaskId(java.lang.String taskId){
		this.taskId = taskId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人名称
	 */

	@Column(name ="CREATE_NAME",nullable=true,length=50)
	public java.lang.String getCreateName(){
		return this.createName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人名称
	 */
	public void setCreateName(java.lang.String createName){
		this.createName = createName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人登录名称
	 */

	@Column(name ="CREATE_BY",nullable=true,length=50)
	public java.lang.String getCreateBy(){
		return this.createBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人登录名称
	 */
	public void setCreateBy(java.lang.String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人登录名称
	 */

	@Column(name ="UPDATE_BY",nullable=true,length=50)
	public java.lang.String getUpdateBy(){
		return this.updateBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人登录名称
	 */
	public void setUpdateBy(java.lang.String updateBy){
		this.updateBy = updateBy;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人名称
	 */

	@Column(name ="UPDATE_NAME",nullable=true,length=50)
	public java.lang.String getUpdateName(){
		return this.updateName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人名称
	 */
	public void setUpdateName(java.lang.String updateName){
		this.updateName = updateName;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建日期
	 */

	@Column(name ="CREATE_DATE",nullable=true)
	public java.util.Date getCreateDate(){
		return this.createDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建日期
	 */
	public void setCreateDate(java.util.Date createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新日期
	 */

	@Column(name ="UPDATE_DATE",nullable=true)
	public java.util.Date getUpdateDate(){
		return this.updateDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新日期
	 */
	public void setUpdateDate(java.util.Date updateDate){
		this.updateDate = updateDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所属部门
	 */

	@Column(name ="SYS_ORG_CODE",nullable=true,length=50)
	public java.lang.String getSysOrgCode(){
		return this.sysOrgCode;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所属部门
	 */
	public void setSysOrgCode(java.lang.String sysOrgCode){
		this.sysOrgCode = sysOrgCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所属公司
	 */

	@Column(name ="SYS_COMPANY_CODE",nullable=true,length=50)
	public java.lang.String getSysCompanyCode(){
		return this.sysCompanyCode;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所属公司
	 */
	public void setSysCompanyCode(java.lang.String sysCompanyCode){
		this.sysCompanyCode = sysCompanyCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  流程状态
	 */

	@Column(name ="BPM_STATUS",nullable=true,length=32)
	public java.lang.String getBpmStatus(){
		return this.bpmStatus;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  流程状态
	 */
	public void setBpmStatus(java.lang.String bpmStatus){
		this.bpmStatus = bpmStatus;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  流程状态
	 */

	@Column(name ="PLAN_RESPONDERS",nullable=true,length=32)
	public java.lang.String getPlanResponders(){
		return this.planResponders;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  流程状态
	 */
	public void setPlanResponders(java.lang.String planResponders){
		this.planResponders = planResponders;
	}

	/**
	 * 方法：取得java.util.Date
	 * @return java.util.Date 实际完成时间
	 */
	@Column(name ="RFINISH_DATE",nullable=true,length=32)
	public java.util.Date getRealfinishDate(){
		return this.realfinishDate;
	}

	/**
	 * 方法: 设置java.util.Date
	 * @param realfinishDate java.util.Date 实际完成时间
	 */
	public void setRealfinishDate(java.util.Date realfinishDate){
		this.realfinishDate = realfinishDate;
	}
}
