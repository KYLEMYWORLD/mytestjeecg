<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="jformEchartList" checkbox="false" pagination="true" fitColumns="true" title="项目看板信息表" sortName="id" actionUrl="jformEchartController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="主键"  field="id"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="项目名称" query="true"  field="projectId"  queryMode="single"  width="120" dictionary="jform_project,id,project_name"></t:dgCol>
   <t:dgCol title="任务ID"  hidden="true" field="taskId"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="父任务ID" hidden="true"  field="pretaskId"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="任务等级" query="true"  field="taskLevel"  queryMode="single"  dictionary="planLevel"  width="120"></t:dgCol>
   <t:dgCol title="任务名称" query="true"  field="taskName"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="任务短名称"  field="taskShortname"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="预警状态"  field="alertStatus"  queryMode="single"  dictionary="planAlert"  width="120"></t:dgCol>
   <t:dgCol title="预警信息"  field="alertMsg"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="任务状态"  field="taskStatus"  queryMode="single"  dictionary="planSucc"  width="120"></t:dgCol>
   <t:dgCol title="开始时间"  field="startDate"  formatter="yyyy-MM-dd" query="true" queryMode="group" editor="datebox"  width="120"></t:dgCol>
   <t:dgCol title="结束时间"  field="finishDate"  formatter="yyyy-MM-dd" query="true" queryMode="group" editor="datebox"  width="120"></t:dgCol>
   <t:dgCol title="实际完成时间"  field="rfinishDate"  formatter="yyyy-MM-dd"  queryMode="single"  width="120"></t:dgCol>
   <t:dgToolBar title="编辑" icon="icon-edit" url="jformEchartController.do?goUpdate" funname="update" id="update"></t:dgToolBar>

  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
  <%--<t:dgCol title="操作" field="opt" width="100"></t:dgCol>--%>
  <%--<t:dgDelOpt title="删除" url="jformEchartController.do?doDel&id={id}" urlclass="ace_button"  urlfont="fa-trash-o"/>--%>
  <%--<t:dgToolBar title="查看" icon="icon-search" url="jformEchartController.do?goUpdate" funname="detail" id="detail"></t:dgToolBar>--%>
  <%--<t:dgToolBar title="导入" icon="icon-put" funname="ImportXls" id="import"></t:dgToolBar>--%>
  <%--<t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls" id="export"></t:dgToolBar>--%>
  <%--<t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT" id="excel"></t:dgToolBar>--%>

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
