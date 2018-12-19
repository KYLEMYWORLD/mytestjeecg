package com.jeecg.jform_project.entity;

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
 * @Description: 项目信息表
 * @author onlineGenerator
 * @date 2018-12-18 09:43:28
 * @version V1.0
 *
 */
@Entity
@Table(name = "jform_project", schema = "")
@SuppressWarnings("serial")
public class JformProjectEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.String id;
	/**项目名称*/
	@Excel(name="项目名称",width=15)
	private java.lang.String projectName;
	/**项目类型*/
	@Excel(name="项目类型",width=15,dicCode="projeType")
	private java.lang.Integer projectType;
	/**开始时间*/
	@Excel(name="开始时间",width=15,format = "yyyy-MM-dd")
	private java.util.Date startDate;
	/**完成时间*/
	@Excel(name="完成时间",width=15,format = "yyyy-MM-dd")
	private java.util.Date finishDate;
	/**项目经理ID*/
	private java.lang.String projectManagerid;
	/**项目经理*/
	@Excel(name="项目经理",width=15)
	private java.lang.String projectManager;
	/**计划表完成时间*/
	@Excel(name="计划表完成时间",width=15,format = "yyyy-MM-dd")
	private java.util.Date planfinishDate;
	/**项目描述*/
	@Excel(name="项目描述",width=15)
	private java.lang.String projectInfo;
	/**项目状态*/
	private java.lang.Integer projectStatus;
	/**项目负责人ID*/
	private java.lang.String projectResponderid;
	/**项目负责人*/
	@Excel(name="项目负责人",width=15)
	private java.lang.String projectResponder;
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
	/**流程状态*/
	private java.lang.String bpmStatus;

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
	 *@return: java.lang.String  项目名称
	 */

	@Column(name ="PROJECT_NAME",nullable=false,length=32)
	public java.lang.String getProjectName(){
		return this.projectName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  项目名称
	 */
	public void setProjectName(java.lang.String projectName){
		this.projectName = projectName;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  项目类型
	 */

	@Column(name ="PROJECT_TYPE",nullable=false,length=32)
	public java.lang.Integer getProjectType(){
		return this.projectType;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  项目类型
	 */
	public void setProjectType(java.lang.Integer projectType){
		this.projectType = projectType;
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
	 *@return: java.util.Date  完成时间
	 */

	@Column(name ="FINISH_DATE",nullable=false,length=32)
	public java.util.Date getFinishDate(){
		return this.finishDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  完成时间
	 */
	public void setFinishDate(java.util.Date finishDate){
		this.finishDate = finishDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  项目经理ID
	 */

	@Column(name ="PROJECT_MANAGERID",nullable=false,length=500)
	public java.lang.String getProjectManagerid(){
		return this.projectManagerid;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  项目经理ID
	 */
	public void setProjectManagerid(java.lang.String projectManagerid){
		this.projectManagerid = projectManagerid;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  项目经理
	 */

	@Column(name ="PROJECT_MANAGER",nullable=false,length=500)
	public java.lang.String getProjectManager(){
		return this.projectManager;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  项目经理
	 */
	public void setProjectManager(java.lang.String projectManager){
		this.projectManager = projectManager;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  计划表完成时间
	 */

	@Column(name ="PLANFINISH_DATE",nullable=true,length=32)
	public java.util.Date getPlanfinishDate(){
		return this.planfinishDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  计划表完成时间
	 */
	public void setPlanfinishDate(java.util.Date planfinishDate){
		this.planfinishDate = planfinishDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  项目描述
	 */

	@Column(name ="PROJECT_INFO",nullable=true,length=200)
	public java.lang.String getProjectInfo(){
		return this.projectInfo;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  项目描述
	 */
	public void setProjectInfo(java.lang.String projectInfo){
		this.projectInfo = projectInfo;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  项目状态
	 */

	@Column(name ="PROJECT_STATUS",nullable=false,length=32)
	public java.lang.Integer getProjectStatus(){
		return this.projectStatus;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  项目状态
	 */
	public void setProjectStatus(java.lang.Integer projectStatus){
		this.projectStatus = projectStatus;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  项目负责人ID
	 */

	@Column(name ="PROJECT_RESPONDERID",nullable=false,length=100)
	public java.lang.String getProjectResponderid(){
		return this.projectResponderid;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  项目负责人ID
	 */
	public void setProjectResponderid(java.lang.String projectResponderid){
		this.projectResponderid = projectResponderid;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  项目负责人
	 */

	@Column(name ="PROJECT_RESPONDER",nullable=false,length=100)
	public java.lang.String getProjectResponder(){
		return this.projectResponder;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  项目负责人
	 */
	public void setProjectResponder(java.lang.String projectResponder){
		this.projectResponder = projectResponder;
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
}
