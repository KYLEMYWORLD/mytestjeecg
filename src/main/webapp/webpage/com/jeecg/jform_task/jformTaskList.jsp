<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="jformTaskList" checkbox="false" pagination="true" fitColumns="true" title="工作任务分配表" sortName="createDate" actionUrl="jformTaskController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="主键"  field="id"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="项目ID"  field="taskProjectid"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="项目名称"  field="taskProjectname" query="true"  queryMode="single"  dictionary="project_select,taskProjectid@taskProjectname,id@project_name"  popup="true"  width="120"></t:dgCol>
   <t:dgCol title="任务类型"  field="taskType" query="true"  queryMode="single"  dictionary="taskType"  width="120"></t:dgCol>
   <t:dgCol title="任务名称"  field="taskName"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="负责人ID"  field="taskResponderid"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="负责人"  field="taskResponder"  queryMode="single"  dictionary="user_select,taskResponderid@taskResponder,account@realname"  popup="true"  width="120"></t:dgCol>
   <t:dgCol title="抄送人id"  field="taskNotifierid"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="抄送人"  field="taskNotifier"  queryMode="single"  dictionary="user_select,taskNotifierid@taskNotifier,id@realname"  popup="true"  width="120"></t:dgCol>
   <t:dgCol title="完成时间"  field="taskFinishdate"  formatter="yyyy-MM-dd"  query="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="计划表时间"  field="taskPlanfinishdate"  formatter="yyyy-MM-dd"  query="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="任务说明"  field="taskInfo"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="任务状态"  field="taskStatus"  queryMode="single"  dictionary="taskStatus"  width="120"></t:dgCol>
   <t:dgCol title="创建人名称"  field="createName"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建人登录名称"  field="createBy"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建日期"  field="createDate"  formatter="yyyy-MM-dd"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新人名称"  field="updateName"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新人登录名称"  field="updateBy"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新日期"  field="updateDate"  formatter="yyyy-MM-dd"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="所属部门"  field="sysOrgCode"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="所属公司"  field="sysCompanyCode"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="流程状态"  field="bpmStatus" hidden="true" queryMode="single"  dictionary="bpm_status"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt exp="taskStatus#eq#0" title="删除" url="jformTaskController.do?doDel&id={id}" urlclass="ace_button"  urlfont="fa-trash-o" operationCode="delete"/>
   <t:dgFunOpt exp="taskStatus#eq#0" funname="doActivate(id)" title="下发" urlclass="ace_button" urlfont="fa-wrench"/>
   <t:dgToolBar title="录入" icon="icon-add" url="jformTaskController.do?goAdd" funname="add" id="add"></t:dgToolBar>
	<t:dgToolBar title="编辑" icon="icon-edit" url="jformTaskController.do?goUpdate" funname="update" id="update"></t:dgToolBar>
   <t:dgToolBar title="批量删除"  icon="icon-remove" url="jformTaskController.do?doBatchDel" funname="deleteALLSelect" id="deleteselect"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="jformTaskController.do?goUpdate" funname="detail" id="detail"></t:dgToolBar>
   <t:dgToolBar title="导入" icon="icon-put" funname="ImportXls" id="import"></t:dgToolBar>
   <t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls" id="export"></t:dgToolBar>
   <t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT" id="excel"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
 $(document).ready(function(){
 });
 
   
 //自定义按钮-下发
 function doActivate(id,index){
  var url = "jformTaskController.do?doActivate";
  url = url+"&id="+id;
  createdialog('确认 ', '确定下发任务吗 ?', url,'jformTaskList');
 }
 
//导入
function ImportXls() {
	openuploadwin('Excel导入', 'jformTaskController.do?upload', "jformTaskList");
}

//导出
function ExportXls() {
	JeecgExcelExport("jformTaskController.do?exportXls","jformTaskList");
}

//模板下载
function ExportXlsByT() {
	JeecgExcelExport("jformTaskController.do?exportXlsByT","jformTaskList");
}

 </script>
