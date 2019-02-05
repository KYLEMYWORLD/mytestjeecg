<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="jformAttendanceList" checkbox="false" pagination="true" fitColumns="true" title="考勤信息表" sortName="createDate" actionUrl="jformAttendanceController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="主键"  field="id"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="员工ID"  field="empId"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="员工名称"  field="empName"  query="true"  queryMode="single"  dictionary="user_select_single,empId@empName,id@realname"  popup="true"  width="80"></t:dgCol>
   <t:dgCol title="考勤类型"  field="attenType"  query="true"  queryMode="single"  dictionary="atteType"  width="80"></t:dgCol>
   <t:dgCol title="开始时间"  field="beginDate"  formatter="yyyy-MM-dd"  query="true"  queryMode="single"  width="80"></t:dgCol>
   <t:dgCol title="结束时间"  field="endDate"  formatter="yyyy-MM-dd"  query="true"  queryMode="single"  width="80"></t:dgCol>
   <t:dgCol title="时长"  field="attenDays"  queryMode="single"  width="60"></t:dgCol>
   <t:dgCol title="考勤状态"  field="attenStatus"  query="true"  queryMode="single"  dictionary="atteStatus"  width="80"></t:dgCol>
   <t:dgCol title="上午不请"  field="morningFree"  queryMode="single"  width="60" replace="否_0,是_1"></t:dgCol>
   <t:dgCol title="下午不请"  field="afternoonFree" queryMode="single"  width="60" replace="否_0,是_1"></t:dgCol>
   <t:dgCol title="备注"  field="memo"  queryMode="single"  width="120"></t:dgCol>
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
