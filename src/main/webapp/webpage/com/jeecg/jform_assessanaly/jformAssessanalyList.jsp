<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="jformAssessanalyList" checkbox="false" pagination="true" fitColumns="true" title="绩效统计" sortName="createDate" actionUrl="jformAssessanalyController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="主键"  field="id"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="用户名称"  field="empId"  query="true"  queryMode="single"  width="120" dictionary="t_s_base_user,username,realname" searchpopdic="user_select_single,empId@cname,account@realname"></t:dgCol>
   <t:dgCol title="统计年份"  field="analyYear"  query="true" formatter="yyyy"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="统计月份"  field="analyMonth"  query="true" formatter="MM"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="任务总量"  field="totalCount"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="任务总评分"  field="totalScore"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="计划安排数"  field="planCount"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="计划总评分"  field="planScore"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="个人任务数"  field="personCount"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="个人总评分 "  field="personScore"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新日期"  field="updateDate"  formatter="yyyy-MM-dd hh:mm:ss" queryMode="single"  width="120"></t:dgCol>
   <t:dgToolBar title="录入" icon="icon-add" url="jformAssessanalyController.do?goAdd" funname="add" id="add"></t:dgToolBar>
   <t:dgToolBar id="update" title="编辑" icon="icon-edit" url="jformAssessanalyController.do?goUpdate" funname="update"></t:dgToolBar>
   <t:dgToolBar id="detail" title="查看" icon="icon-search" url="jformAssessanalyController.do?goUpdate" funname="detail"></t:dgToolBar>
   <t:dgToolBar id="export" title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
   <t:dgToolBar id="analy" title="重新统计" icon="icon-edit"  url="jformAssessanalyController.do?doAnaly" funname="doAnaly"></t:dgToolBar>
   <t:dgToolBar id="analyall" title="统计全部" icon="icon-edit"  url="jformAssessanalyController.do?doAnalyall" funname="doAnalyall"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
 $(document).ready(function(){
 });
 
//自定义按钮-重新统计
function doAnaly(title,url,gridname){
	 var rowData = $('#'+gridname).datagrid('getSelected');
 		if (!rowData) {
	 		tip('请选择重新统计记录');
	 		return;
 	    }
 	 url = url+"&id="+rowData['id'];
 	 createdialog('确认 ', '确定'+title+'吗 ?', url,gridname);
}
//自定义按钮-统计全部
function doAnalyall(title,url,gridname){
 createwindow('统计全部', 'jformAssessanalyController.do?goAnalyAll', 420, 280);
}

//导出
function ExportXls() {
	JeecgExcelExport("jformAssessanalyController.do?exportXls","jformAssessanalyList");
}

//模板下载
function ExportXlsByT() {
	JeecgExcelExport("jformAssessanalyController.do?exportXlsByT","jformAssessanalyList");
}
 </script>
