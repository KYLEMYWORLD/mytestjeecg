package com.jeecg.jform_attenstatistic.entity;

import io.swagger.models.auth.In;
import org.hibernate.annotations.GenericGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.*;
import java.util.Date;

/**   
 * @Title: Entity
 * @Description: 考勤统计表
 * @author onlineGenerator
 * @date 2019-01-10 16:01:11
 * @version V1.0   
 *
 */
@Entity
@Table(name = "jform_attenstatistic", schema = "")
@SuppressWarnings("serial")
public class JformAttenstatisticEntity implements java.io.Serializable {
	/**主键*/
	private String id;
	/**员工ID*/
	private String empId;
	/**员工名称*/
	@Excel(name="员工名称",width=15)
	private String empName;
	/**在岗天数*/
	@Excel(name="在岗天数",width=15)
	private Double dutyDays;
	/**出差天数*/
	@Excel(name="出差天数",width=15)
	private Double gooutDays;
	/**请假天数*/
	@Excel(name="请假天数",width=15)
	private Double leaveDays;
	/**调休天数*/
	@Excel(name="调休天数",width=15)
	private Double offworkDays;
	/**统计年份*/
	@Excel(name="统计月份",width=15)
	private Integer dateMouth;
	/**统计年份*/
	@Excel(name="统计年份",width=15)
	private Integer dateYear;
	/**创建人名称*/
	private String createName;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private Date createDate;
	/**更新人名称*/
	private String updateName;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private Date updateDate;
	/**所属部门*/
	private String sysOrgCode;
	/**所属公司*/
	private String sysCompanyCode;
	/**流程状态*/
	private String bpmStatus;

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  主键
	 */
	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")

	@Column(name ="ID",nullable=false,length=36)
	public String getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  主键
	 */
	public void setId(String id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  员工ID
	 */

	@Column(name ="EMP_ID",nullable=false,length=36)
	public String getEmpId(){
		return this.empId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  员工ID
	 */
	public void setEmpId(String empId){
		this.empId = empId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  员工名称
	 */

	@Column(name ="EMP_NAME",nullable=false,length=36)
	public String getEmpName(){
		return this.empName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  员工名称
	 */
	public void setEmpName(String empName){
		this.empName = empName;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  在岗天数
	 */

	@Column(name ="DUTY_DAYS",nullable=false,scale=1,length=16)
	public Double getDutyDays(){
		return this.dutyDays;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  在岗天数
	 */
	public void setDutyDays(Double dutyDays){
		this.dutyDays = dutyDays;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  出差天数
	 */

	@Column(name ="GOOUT_DAYS",nullable=false,scale=1,length=16)
	public Double getGooutDays(){
		return this.gooutDays;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  出差天数
	 */
	public void setGooutDays(Double gooutDays){
		this.gooutDays = gooutDays;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  请假天数
	 */

	@Column(name ="LEAVE_DAYS",nullable=false,scale=1,length=16)
	public Double getLeaveDays(){
		return this.leaveDays;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  请假天数
	 */
	public void setLeaveDays(Double leaveDays){
		this.leaveDays = leaveDays;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  调休天数
	 */

	@Column(name ="OFFWORK_DAYS",nullable=false,scale=1,length=16)
	public Double getOffworkDays(){
		return this.offworkDays;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  调休天数
	 */
	public void setOffworkDays(Double offworkDays){
		this.offworkDays = offworkDays;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  统计年份
	 */

	@Column(name ="DATE_MOUTH",nullable=false,length=32)
	public Integer getDateMouth(){
		return this.dateMouth;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  统计年份
	 */
	public void setDateMouth(Integer dateMouth){
		this.dateMouth = dateMouth;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  统计年份
	 */

	@Column(name ="DATE_YEAR",nullable=false,length=32)
	public Integer getDateYear(){
		return this.dateYear;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  统计年份
	 */
	public void setDateYear(Integer dateYear){
		this.dateYear = dateYear;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人名称
	 */

	@Column(name ="CREATE_NAME",nullable=true,length=50)
	public String getCreateName(){
		return this.createName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人名称
	 */
	public void setCreateName(String createName){
		this.createName = createName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人登录名称
	 */

	@Column(name ="CREATE_BY",nullable=true,length=50)
	public String getCreateBy(){
		return this.createBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人登录名称
	 */
	public void setCreateBy(String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建日期
	 */

	@Column(name ="CREATE_DATE",nullable=true,length=20)
	public Date getCreateDate(){
		return this.createDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建日期
	 */
	public void setCreateDate(Date createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人名称
	 */

	@Column(name ="UPDATE_NAME",nullable=true,length=50)
	public String getUpdateName(){
		return this.updateName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人名称
	 */
	public void setUpdateName(String updateName){
		this.updateName = updateName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人登录名称
	 */

	@Column(name ="UPDATE_BY",nullable=true,length=50)
	public String getUpdateBy(){
		return this.updateBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人登录名称
	 */
	public void setUpdateBy(String updateBy){
		this.updateBy = updateBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新日期
	 */

	@Column(name ="UPDATE_DATE",nullable=true,length=20)
	public Date getUpdateDate(){
		return this.updateDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新日期
	 */
	public void setUpdateDate(Date updateDate){
		this.updateDate = updateDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所属部门
	 */

	@Column(name ="SYS_ORG_CODE",nullable=true,length=50)
	public String getSysOrgCode(){
		return this.sysOrgCode;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所属部门
	 */
	public void setSysOrgCode(String sysOrgCode){
		this.sysOrgCode = sysOrgCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所属公司
	 */

	@Column(name ="SYS_COMPANY_CODE",nullable=true,length=50)
	public String getSysCompanyCode(){
		return this.sysCompanyCode;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所属公司
	 */
	public void setSysCompanyCode(String sysCompanyCode){
		this.sysCompanyCode = sysCompanyCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  流程状态
	 */

	@Column(name ="BPM_STATUS",nullable=true,length=32)
	public String getBpmStatus(){
		return this.bpmStatus;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  流程状态
	 */
	public void setBpmStatus(String bpmStatus){
		this.bpmStatus = bpmStatus;
	}
}
