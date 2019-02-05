package com.jeecg.jform_assess.entity;

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
 * @Description: 绩效考核
 * @author onlineGenerator
 * @date 2019-01-28 17:12:01
 * @version V1.0   
 *
 */
@Entity
@Table(name = "jform_assess", schema = "")
@SuppressWarnings("serial")
public class JformAssessEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.String id;
	/**任务等级*/
	@Excel(name="任务等级",width=15)
	private java.lang.Integer planLevel;
	/**项目名称*/
	@Excel(name="项目ID",width=15)
	private java.lang.String projectId;
	/**负责人*/
	@Excel(name="负责人",width=15)
	private java.lang.String responderId;
	/**任务名称*/
	@Excel(name="任务名称",width=15)
	private java.lang.String planName;
	/**开始时间*/
	@Excel(name="开始时间",width=15,format = "yyyy-MM-dd")
	private java.util.Date startDate;
	/**任务完成时间*/
	@Excel(name="任务完成时间",width=15,format = "yyyy-MM-dd")
	private java.util.Date finishDate;
	/**实际完成时间*/
	@Excel(name="实际完成时间",width=15,format = "yyyy-MM-dd")
	private java.util.Date rfinishDate;
	/**完成状态*/
	@Excel(name="完成状态",width=15)
	private java.lang.Integer planIssucc;
	/**进度评分*/
	@Excel(name="进度评分",width=15)
	private java.math.BigDecimal systemScore;
	/**质量评分*/
	@Excel(name="质量评分",width=15)
	private java.math.BigDecimal expertScore;
	/**总评分*/
	@Excel(name="总评分",width=15)
	private java.math.BigDecimal totalScore;
	/**评分状态*/
	private java.lang.Integer assessStatus;
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
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  主键
	 */
	@Id
//	@GeneratedValue(generator = "paymentableGenerator")
//	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")

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
	 *@return: java.lang.String  负责人
	 */

	@Column(name ="RESPONDER_ID",nullable=false,length=32)
	public java.lang.String getResponderId(){
		return this.responderId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  负责人
	 */
	public void setResponderId(java.lang.String responderId){
		this.responderId = responderId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  任务名称
	 */

	@Column(name ="PLAN_NAME",nullable=false,length=50)
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
	 *@return: java.util.Date  任务完成时间
	 */

	@Column(name ="FINISH_DATE",nullable=false,length=32)
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
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  实际完成时间
	 */

	@Column(name ="RFINISH_DATE",nullable=false,length=32)
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
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  完成状态
	 */

	@Column(name ="PLAN_ISSUCC",nullable=false,length=32)
	public java.lang.Integer getPlanIssucc(){
		return this.planIssucc;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.String  完成状态
	 */
	public void setPlanIssucc(java.lang.Integer planIssucc){
		this.planIssucc = planIssucc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  进度评分
	 */

	@Column(name ="SYSTEM_SCORE",nullable=false,length=32)
	public java.math.BigDecimal getSystemScore(){
		return this.systemScore;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  进度评分
	 */
	public void setSystemScore(java.math.BigDecimal systemScore){
		this.systemScore = systemScore;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  质量评分
	 */

	@Column(name ="EXPERT_SCORE",nullable=true,length=32)
	public java.math.BigDecimal getExpertScore(){
		return this.expertScore;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  质量评分
	 */
	public void setExpertScore(java.math.BigDecimal expertScore){
		this.expertScore = expertScore;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  总评分
	 */

	@Column(name ="TOTAL_SCORE",nullable=true,length=32)
	public java.math.BigDecimal getTotalScore(){
		return this.totalScore;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  总评分
	 */
	public void setTotalScore(java.math.BigDecimal totalScore){
		this.totalScore = totalScore;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  评分状态
	 */

	@Column(name ="ASSESS_STATUS",nullable=false,length=32)
	public java.lang.Integer getAssessStatus(){
		return this.assessStatus;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  评分状态
	 */
	public void setAssessStatus(java.lang.Integer assessStatus){
		this.assessStatus = assessStatus;
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
}
