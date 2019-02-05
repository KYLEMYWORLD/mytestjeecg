<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="jformEchartList" checkbox="false" pagination="true" fitColumns="true" title="项目看板信息表" sortName="id" actionUrl="jformEchartController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="主键"  field="id"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="项目ID" query="true"  field="projectId"  queryMode="single"  width="120" dictionary="jform_project,id,project_name"  searchpopdic="project_select,projectId@cname,id@project_name"></t:dgCol>
   <t:dgCol title="任务ID"  field="taskId"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="父任务ID"  field="pretaskId"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="任务等级"  field="taskLevel" query="true"  queryMode="single"  dictionary="planLevel"  width="120"></t:dgCol>
   <t:dgCol title="任务名称"  field="taskName"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="任务短名称"  field="taskShortname"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="预警状态"  field="alertStatus"  queryMode="single"  dictionary="planAlert"  width="120"></t:dgCol>
   <t:dgCol title="预警信息"  field="alertMsg"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="完成状态"  field="taskStatus" query="true"  queryMode="single"  dictionary="planSucc"  width="120"></t:dgCol>
   <t:dgCol title="开始时间"  field="startDate"  formatter="yyyy-MM-dd"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="结束时间"  field="finishDate"  formatter="yyyy-MM-dd"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="实际完成时间"  field="rfinishDate"  formatter="yyyy-MM-dd"  queryMode="single"  width="120"></t:dgCol>
   <t:dgToolBar id="add" title="录入" icon="icon-add" url="jformEchartController.do?goAdd" funname="add"></t:dgToolBar>
   <t:dgToolBar id="update" title="编辑" icon="icon-edit" url="jformEchartController.do?goUpdate" funname="update"></t:dgToolBar>
   <t:dgToolBar id="detail" title="查看" icon="icon-search" url="jformEchartController.do?goUpdate" funname="detail"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
 $(document).ready(function(){
 });
 
   
 
//导入
function ImportXls() {
	openuploadwin('Excel导入', 'jformEchartController.do?upload', "jformEchartList");
}

//导出
function ExportXls() {
	JeecgExcelExport("jformEchartController.do?exportXls","jformEchartList");
}

//模板下载
function ExportXlsByT() {
	JeecgExcelExport("jformEchartController.do?exportXlsByT","jformEchartList");
}

 </script>
