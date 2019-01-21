<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="jformAttendanceList" checkbox="false" pagination="true" fitColumns="true" title="考勤信息表" sortName="createDate" actionUrl="jformAttendanceController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="主键"  field="id"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="员工ID"  field="empId"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="员工名称"  field="empName"  query="true"  queryMode="single"  dictionary="user_select_single,empId@empName,id@realname"  popup="true"  width="120"></t:dgCol>
   <t:dgCol title="考勤类型"  field="attenType"  query="true"  queryMode="single"  dictionary="atteType"  width="120"></t:dgCol>
   <t:dgCol title="开始时间"  field="beginDate"  formatter="yyyy-MM-dd"  query="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="结束时间"  field="endDate"  formatter="yyyy-MM-dd"  query="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="时长"  field="attenDays"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="考勤状态"  field="attenStatus"  query="true"  queryMode="single"  dictionary="atteStatus"  width="120"></t:dgCol>
   <t:dgCol title="备注"  field="memo"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建人名称"  field="createName"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建人登录名称"  field="createBy"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建日期"  field="createDate"  formatter="yyyy-MM-dd"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新人名称"  field="updateName"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新人登录名称"  field="updateBy"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新日期"  field="updateDate"  formatter="yyyy-MM-dd"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="所属部门"  field="sysOrgCode"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="所属公司"  field="sysCompanyCode"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="流程状态"  field="bpmStatus"  hidden="true"  queryMode="single"  dictionary="bpm_status"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt title="删除" url="jformAttendanceController.do?doDel&id={id}" urlclass="ace_button"  urlfont="fa-trash-o"/>
   <t:dgToolBar title="录入考勤" icon="icon-add" url="jformAttendanceController.do?goAdd" funname="add" id="add"></t:dgToolBar>
	<t:dgToolBar title="编辑考勤" icon="icon-edit" url="jformAttendanceController.do?goUpdate" funname="update" id="update"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="jformAttendanceController.do?goUpdate" funname="detail" id="detail"></t:dgToolBar>
   <t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls" id="export"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
 $(document).ready(function(){
 });
 
   
 
//导入
function ImportXls() {
	openuploadwin('Excel导入', 'jformAttendanceController.do?upload', "jformAttendanceList");
}

//导出
function ExportXls() {
	JeecgExcelExport("jformAttendanceController.do?exportXls","jformAttendanceList");
}

//模板下载
function ExportXlsByT() {
	JeecgExcelExport("jformAttendanceController.do?exportXlsByT","jformAttendanceList");
}

 </script>
