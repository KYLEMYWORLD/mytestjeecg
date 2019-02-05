package com.jeecg.jform_assessanaly.entity;

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
 * @Description: 绩效统计
 * @author onlineGenerator
 * @date 2019-01-29 14:19:02
 * @version V1.0   
 *
 */
@Entity
@Table(name = "jform_assessanaly", schema = "")
@SuppressWarnings("serial")
public class JformAssessanalyEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.String id;
	/**用户名称*/
	@Excel(name="用户名称",width=15)
	private java.lang.String empId;
	/**统计年份*/
	@Excel(name="统计年份",width=15)
	private java.lang.Integer analyYear;
	/**统计月份*/
	@Excel(name="统计月份",width=15)
	private java.lang.Integer analyMonth;
	/**任务总量*/
	@Excel(name="任务总量",width=15)
	private java.lang.Integer totalCount;
	/**任务总评分*/
	@Excel(name="任务总评分",width=15)
	private java.math.BigDecimal totalScore;
	/**计划安排数*/
	@Excel(name="计划安排数",width=15)
	private java.lang.Integer planCount;
	/**计划总评分*/
	@Excel(name="计划总评分",width=15)
	private java.math.BigDecimal planScore;
	/**个人任务数*/
	@Excel(name="个人任务数",width=15)
	private java.lang.Integer personCount;
	/**个人总评分 */
	@Excel(name="个人总评分 ",width=15)
	private java.math.BigDecimal personScore;
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
	 *@return: java.lang.String  用户名称
	 */

	@Column(name ="EMP_ID",nullable=true,length=32)
	public java.lang.String getEmpId(){
		return this.empId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  用户名称
	 */
	public void setEmpId(java.lang.String empId){
		this.empId = empId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  统计年份
	 */

	@Column(name ="ANALY_YEAR",nullable=true,length=32)
	public java.lang.Integer getAnalyYear(){
		return this.analyYear;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  统计年份
	 */
	public void setAnalyYear(java.lang.Integer analyYear){
		this.analyYear = analyYear;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  统计月份
	 */

	@Column(name ="ANALY_MONTH",nullable=true,length=32)
	public java.lang.Integer getAnalyMonth(){
		return this.analyMonth;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  统计月份
	 */
	public void setAnalyMonth(java.lang.Integer analyMonth){
		this.analyMonth = analyMonth;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  任务总量
	 */

	@Column(name ="TOTAL_COUNT",nullable=true,length=32)
	public java.lang.Integer getTotalCount(){
		return this.totalCount;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  任务总量
	 */
	public void setTotalCount(java.lang.Integer totalCount){
		this.totalCount = totalCount;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  任务总评分
	 */

	@Column(name ="TOTAL_SCORE",nullable=true,scale=2,length=32)
	public java.math.BigDecimal getTotalScore(){
		return this.totalScore;
	}

	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  任务总评分
	 */
	public void setTotalScore(java.math.BigDecimal totalScore){
		this.totalScore = totalScore;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  计划安排数
	 */

	@Column(name ="PLAN_COUNT",nullable=true,length=32)
	public java.lang.Integer getPlanCount(){
		return this.planCount;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  计划安排数
	 */
	public void setPlanCount(java.lang.Integer planCount){
		this.planCount = planCount;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  计划总评分
	 */

	@Column(name ="PLAN_SCORE",nullable=true,scale=2,length=32)
	public java.math.BigDecimal getPlanScore(){
		return this.planScore;
	}

	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  计划总评分
	 */
	public void setPlanScore(java.math.BigDecimal planScore){
		this.planScore = planScore;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  个人任务数
	 */

	@Column(name ="PERSON_COUNT",nullable=true,length=32)
	public java.lang.Integer getPersonCount(){
		return this.personCount;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  个人任务数
	 */
	public void setPersonCount(java.lang.Integer personCount){
		this.personCount = personCount;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  个人总评分 
	 */

	@Column(name ="PERSON_SCORE",nullable=true,scale=2,length=32)
	public java.math.BigDecimal getPersonScore(){
		return this.personScore;
	}

	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  个人总评分 
	 */
	public void setPersonScore(java.math.BigDecimal personScore){
		this.personScore = personScore;
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
