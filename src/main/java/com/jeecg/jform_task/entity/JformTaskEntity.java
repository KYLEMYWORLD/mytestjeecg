package com.jeecg.jform_task.entity;

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
 * @Description: 工作任务分配表
 * @author onlineGenerator
 * @date 2018-12-09 17:43:56
 * @version V1.0   
 *
 */
@Entity
@Table(name = "jform_task", schema = "")
@SuppressWarnings("serial")
public class JformTaskEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.String id;
	/**创建人名称*/
	private java.lang.String createName;
	/**创建人登录名称*/
	private java.lang.String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人名称*/
	private java.lang.String updateName;
	/**更新人登录名称*/
	private java.lang.String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	/**所属部门*/
	private java.lang.String sysOrgCode;
	/**所属公司*/
	private java.lang.String sysCompanyCode;
	/**流程状态*/
	private java.lang.String bpmStatus;
	/**项目ID*/
	private java.lang.String taskProjectid;
	/**项目名称*/
	@Excel(name="项目名称",width=15)
	private java.lang.String taskProjectname;
	/**任务类型*/
	@Excel(name="任务类型",width=15,dicCode="taskType")
	private java.lang.Integer taskType;
	/**任务名称*/
	@Excel(name="任务名称",width=15)
	private java.lang.String taskName;
	/**负责人ID*/
	private java.lang.String taskResponderid;
	/**负责人*/
	@Excel(name="负责人",width=15)
	private java.lang.String taskResponder;
	/**抄送人id*/
	private java.lang.String taskNotifierid;
	/**抄送人*/
	@Excel(name="抄送人",width=15)
	private java.lang.String taskNotifier;
	/**要求完成时间*/
	@Excel(name="要求完成时间",width=15,format = "yyyy-MM-dd")
	private java.util.Date taskFinishdate;
	/**计划表时间*/
	@Excel(name="计划表时间",width=15,format = "yyyy-MM-dd")
	private java.util.Date taskPlanfinishdate;
	/**任务说明*/
	@Excel(name="任务说明",width=15)
	private java.lang.String taskInfo;
	/**任务状态*/
	private java.lang.Integer taskStatus;
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  主键
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
	 *@param: java.lang.String  主键
	 */
	public void setId(java.lang.String id){
		this.id = id;
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
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建日期
	 */

	@Column(name ="CREATE_DATE",nullable=true,length=20)
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
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新日期
	 */

	@Column(name ="UPDATE_DATE",nullable=true,length=20)
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
	 *@return: java.lang.String  项目ID
	 */

	@Column(name ="TASK_PROJECTID",nullable=false,length=200)
	public java.lang.String getTaskProjectid(){
		return this.taskProjectid;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  项目ID
	 */
	public void setTaskProjectid(java.lang.String taskProjectid){
		this.taskProjectid = taskProjectid;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  项目名称
	 */

	@Column(name ="TASK_PROJECTNAME",nullable=false,length=200)
	public java.lang.String getTaskProjectname(){
		return this.taskProjectname;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  项目名称
	 */
	public void setTaskProjectname(java.lang.String taskProjectname){
		this.taskProjectname = taskProjectname;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  任务类型
	 */

	@Column(name ="TASK_TYPE",nullable=false,length=32)
	public java.lang.Integer getTaskType(){
		return this.taskType;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  任务类型
	 */
	public void setTaskType(java.lang.Integer taskType){
		this.taskType = taskType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  任务名称
	 */

	@Column(name ="TASK_NAME",nullable=false,length=100)
	public java.lang.String getTaskName(){
		return this.taskName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  任务名称
	 */
	public void setTaskName(java.lang.String taskName){
		this.taskName = taskName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  负责人ID
	 */

	@Column(name ="TASK_RESPONDERID",nullable=false,length=500)
	public java.lang.String getTaskResponderid(){
		return this.taskResponderid;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  负责人ID
	 */
	public void setTaskResponderid(java.lang.String taskResponderid){
		this.taskResponderid = taskResponderid;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  负责人
	 */

	@Column(name ="TASK_RESPONDER",nullable=false,length=200)
	public java.lang.String getTaskResponder(){
		return this.taskResponder;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  负责人
	 */
	public void setTaskResponder(java.lang.String taskResponder){
		this.taskResponder = taskResponder;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  抄送人id
	 */

	@Column(name ="TASK_NOTIFIERID",nullable=true,length=500)
	public java.lang.String getTaskNotifierid(){
		return this.taskNotifierid;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  抄送人id
	 */
	public void setTaskNotifierid(java.lang.String taskNotifierid){
		this.taskNotifierid = taskNotifierid;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  抄送人
	 */

	@Column(name ="TASK_NOTIFIER",nullable=true,length=200)
	public java.lang.String getTaskNotifier(){
		return this.taskNotifier;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  抄送人
	 */
	public void setTaskNotifier(java.lang.String taskNotifier){
		this.taskNotifier = taskNotifier;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  要求完成时间
	 */

	@Column(name ="TASK_FINISHDATE",nullable=false,length=32)
	public java.util.Date getTaskFinishdate(){
		return this.taskFinishdate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  要求完成时间
	 */
	public void setTaskFinishdate(java.util.Date taskFinishdate){
		this.taskFinishdate = taskFinishdate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  计划表时间
	 */

	@Column(name ="TASK_PLANFINISHDATE",nullable=true,length=32)
	public java.util.Date getTaskPlanfinishdate(){
		return this.taskPlanfinishdate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  计划表时间
	 */
	public void setTaskPlanfinishdate(java.util.Date taskPlanfinishdate){
		this.taskPlanfinishdate = taskPlanfinishdate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  任务说明
	 */

	@Column(name ="TASK_INFO",nullable=true,length=500)
	public java.lang.String getTaskInfo(){
		return this.taskInfo;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  任务说明
	 */
	public void setTaskInfo(java.lang.String taskInfo){
		this.taskInfo = taskInfo;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  任务状态
	 */

	@Column(name ="TASK_STATUS",nullable=false,length=32)
	public java.lang.Integer getTaskStatus(){
		return this.taskStatus;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  任务状态
	 */
	public void setTaskStatus(java.lang.Integer taskStatus){
		this.taskStatus = taskStatus;
	}
}
