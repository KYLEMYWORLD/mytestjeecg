<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="jformUserPlanFeedbackList" checkbox="false" pagination="true" fitColumns="true" title="个人计划反馈" sortName="createDate" actionUrl="jformUserPlanController.do?feedbackdatagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="id"  field="id"  hidden="true"  queryMode="single"  width="80"></t:dgCol>
   <t:dgCol title="项目名称"  field="projectId"  queryMode="single" query="true"  width="120"  dictionary="jform_project,id,project_name"></t:dgCol>
   <t:dgCol title="上级任务" dictionary="jform_user_plan,id,plan_name"  field="planId"  queryMode="single"  width="80"></t:dgCol>
   <t:dgCol title="任务名称"  query="true" field="planName"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="任务等级"  query="true"  field="planLevel"  queryMode="single" dictionary="userPlanT"  width="60"></t:dgCol>
   <t:dgCol title="任务状态" query="true"  field="planStatus" dictionary="planStatus"  queryMode="single"  width="60"></t:dgCol>
   <t:dgCol title="任务顺序"  field="planOrder"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="任务开始时间" query="true" field="startDate"  formatter="yyyy-MM-dd"  queryMode="group"  width="60"></t:dgCol>
   <t:dgCol title="任务完成时间"  query="true" field="finishDate"  formatter="yyyy-MM-dd"  queryMode="group"  width="60"></t:dgCol>
   <t:dgCol title="负责人"  field="planResponderid"  hidden="true"  queryMode="single"  width="80"></t:dgCol>
   <t:dgCol title="负责人"  field="planResponder"  queryMode="single"  width="80"  query="true"  dictionary="user_select_single,planResponderid@planResponder,account@realname"  popup="true" ></t:dgCol>
   <t:dgCol title="全部负责人"  field="planResponders"  queryMode="single"  width="60"></t:dgCol>
   <t:dgCol title="任务说明"  field="planInfo"  queryMode="single"  width="80"></t:dgCol>
   <t:dgCol title="审批内容"  field="planRejectmsg"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="完成状态"  hidden="true"  field="planIssucc"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="延时原因"  hidden="true"  field="planLatemsg"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="预警状态"  hidden="true"  field="planIsalert"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="预警原因"  hidden="true"  field="planAlertmsg"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgFunOpt exp="planStatus#eq#5" funname="dofinishTask(id)" title="完成任务" urlclass="ace_button" urlfont="fa-wrench" />
   <t:dgFunOpt funname="doelertTask(id)" title="预警" urlclass="ace_button" urlfont="fa-wrench" />
   <t:dgToolBar title="查看" icon="icon-search" url="jformUserPlanController.do?goUpdate" funname="detail" width="80%" height="80%" id="detail"></t:dgToolBar>
   <t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls" id="export"></t:dgToolBar>

  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
 $(document).ready(function(){

 });

//导出
function ExportXls() {
	JeecgExcelExport("jformUserPlanController.do?exportXls","jformUserPlanList");
}

//弹框完成任务
function dofinishTask(id){
   createwindow('完成任务', 'jformUserPlanController.do?goFeedbackFinish&id=' + id,"70%","80%");
}

//预警任务
function doelertTask() {
  tip("功能正在逐步开发中，尽情期待！");
}

/**
 * 获取表格对象
 * @return 表格对象
 */
function getDataGrid(){
	var datagrid = $('#'+gridname);
	return datagrid;
}
 </script>
