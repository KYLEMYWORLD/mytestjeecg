package com.jeecg.jform_echart.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;

/**   
 * @Title: Entity
 * @Description: 项目看板信息表
 * @author onlineGenerator
 * @date 2019-01-21 14:15:57
 * @version V1.0   
 *
 */
@Entity
@Table(name = "jform_echart", schema = "")
@SuppressWarnings("serial")
public class JformEchartEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.String id;
	/**项目ID*/
	@Excel(name="项目ID",width=15)
	private java.lang.String projectId;
	/**任务ID*/
	@Excel(name="任务ID",width=15)
	private java.lang.String taskId;
	/**父任务ID*/
	@Excel(name="父任务ID",width=15)
	private java.lang.String pretaskId;
	/**任务等级*/
	@Excel(name="任务等级",width=15,dicCode="planLevel")
	private java.lang.Integer taskLevel;
	/**任务名称*/
	@Excel(name="任务名称",width=15)
	private java.lang.String taskName;
	/**任务短名称*/
	@Excel(name="任务短名称",width=15)
	private java.lang.String taskShortname;
	/**预警状态*/
	@Excel(name="预警状态",width=15,dicCode="planAlert")
	private java.lang.Integer alertStatus;
	/**预警信息*/
	@Excel(name="预警信息",width=15)
	private java.lang.String alertMsg;
	/**任务状态*/
	@Excel(name="任务状态",width=15,dicCode="planSucc")
	private java.lang.Integer taskStatus;
	/**开始时间*/
	@Excel(name="开始时间",width=15,format = "yyyy-MM-dd")
	private java.util.Date startDate;
	/**结束时间*/
	@Excel(name="结束时间",width=15,format = "yyyy-MM-dd")
	private java.util.Date finishDate;
	/**实际完成时间*/
	@Excel(name="实际完成时间",width=15,format = "yyyy-MM-dd")
	private java.util.Date rfinishDate;
	
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
	 *@return: java.lang.String  项目ID
	 */

	@Column(name ="PROJECT_ID",nullable=false,length=36)
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
	 *@return: java.lang.String  任务ID
	 */

	@Column(name ="TASK_ID",nullable=false,length=36)
	public java.lang.String getTaskId(){
		return this.taskId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  任务ID
	 */
	public void setTaskId(java.lang.String taskId){
		this.taskId = taskId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  父任务ID
	 */

	@Column(name ="PRETASK_ID",nullable=true,length=36)
	public java.lang.String getPretaskId(){
		return this.pretaskId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  父任务ID
	 */
	public void setPretaskId(java.lang.String pretaskId){
		this.pretaskId = pretaskId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  任务等级
	 */

	@Column(name ="TASK_LEVEL",nullable=false,length=32)
	public java.lang.Integer getTaskLevel(){
		return this.taskLevel;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  任务等级
	 */
	public void setTaskLevel(java.lang.Integer taskLevel){
		this.taskLevel = taskLevel;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  任务名称
	 */

	@Column(name ="TASK_NAME",nullable=false,length=200)
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
	 *@return: java.lang.String  任务短名称
	 */

	@Column(name ="TASK_SHORTNAME",nullable=false,length=50)
	public java.lang.String getTaskShortname(){
		return this.taskShortname;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  任务短名称
	 */
	public void setTaskShortname(java.lang.String taskShortname){
		this.taskShortname = taskShortname;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  预警状态
	 */

	@Column(name ="ALERT_STATUS",nullable=false,length=32)
	public java.lang.Integer getAlertStatus(){
		return this.alertStatus;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  预警状态
	 */
	public void setAlertStatus(java.lang.Integer alertStatus){
		this.alertStatus = alertStatus;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  预警信息
	 */

	@Column(name ="ALERT_MSG",nullable=true,length=200)
	public java.lang.String getAlertMsg(){
		return this.alertMsg;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  预警信息
	 */
	public void setAlertMsg(java.lang.String alertMsg){
		this.alertMsg = alertMsg;
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
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  开始时间
	 */

	@Column(name ="START_DATE",nullable=false,length=32)
	public java.util.Date getStartDate(){
		return this.startDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  开始时间
	 */
	public void setStartDate(java.util.Date startDate){
		this.startDate = startDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  结束时间
	 */

	@Column(name ="FINISH_DATE",nullable=false,length=32)
	public java.util.Date getFinishDate(){
		return this.finishDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  结束时间
	 */
	public void setFinishDate(java.util.Date finishDate){
		this.finishDate = finishDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  实际完成时间
	 */

	@Column(name ="RFINISH_DATE",nullable=true,length=32)
	public java.util.Date getRfinishDate(){
		return this.rfinishDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  实际完成时间
	 */
	public void setRfinishDate(java.util.Date rfinishDate){
		this.rfinishDate = rfinishDate;
	}
}
