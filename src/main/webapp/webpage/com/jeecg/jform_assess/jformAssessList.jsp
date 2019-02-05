<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="jformAssessList" checkbox="false" pagination="true" fitColumns="true" title="绩效考核" sortName="createDate" actionUrl="jformAssessController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="主键"  field="id"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="项目名称" field="projectId" query="true" queryMode="single"  width="120"  dictionary="jform_project,id,project_name" searchpopdic="project_select,projectId@cname,id@project_name"></t:dgCol>
   <t:dgCol title="负责人"  field="responderId"  query="true"  queryMode="single"  width="120" dictionary="t_s_base_user,username,realname"></t:dgCol>
   <t:dgCol title="任务名称"  field="planName"  query="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="任务等级"  query="true"  field="planLevel"  queryMode="single" dictionary="userPlanT"  width="80"></t:dgCol>
   <t:dgCol title="开始时间"  field="startDate" query="true"  formatter="yyyy-MM-dd"  queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="任务完成时间"  field="finishDate"  query="true"  formatter="yyyy-MM-dd"  queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="实际完成时间"  field="rfinishDate"  formatter="yyyy-MM-dd"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="完成状态"  field="planIssucc" dictionary="planSucc"  query="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="进度评分"  field="systemScore"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="质量评分"  field="expertScore"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="总评分"  field="totalScore"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="评分状态"  field="assessStatus"  queryMode="single"  width="120" replace="未评分_0,已评分_1"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
	<t:dgFunOpt id="score" funname="doScore(id)" exp="assessStatus#eq#0" title="评分" urlclass="ace_button" urlfont="fa-wrench" />
   <t:dgToolBar id="detail" title="查看" icon="icon-search" url="jformAssessController.do?goUpdate" funname="detail"></t:dgToolBar>
   <t:dgToolBar id="export" title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
 $(document).ready(function(){

 });
   
//自定义按钮-评分
function doScore(id,index){
	var url = "jformAssessController.do?goUpdate";
	url = url+"&id="+id;
    createwindow('评分',url,'80%','80%');
}

//导出
function ExportXls() {
	JeecgExcelExport("jformAssessController.do?exportXls","jformAssessList");
}

//模板下载
function ExportXlsByT() {
	JeecgExcelExport("jformAssessController.do?exportXlsByT","jformAssessList");
}

 </script>
