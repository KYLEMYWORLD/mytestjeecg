<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="jformAttenstatisticList" checkbox="false" pagination="true" fitColumns="true" title="考勤统计表" sortName="createDate" actionUrl="jformAttenstatisticController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="主键"  field="id"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="员工ID"  field="empId"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="员工名称"  field="empName"  query="true"  queryMode="single"  dictionary="user_select_single,empId@empName,id@realname"  popup="true"  width="120"></t:dgCol>
   <t:dgCol title="在岗天数"  field="dutyDays"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="出差天数"  field="gooutDays"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="请假天数"  field="leaveDays"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="调休天数"  field="offworkDays"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="统计月份"  field="dateMouth"  query="true" formatter="MM"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="统计年份"  field="dateYear" query="true"  formatter="yyyy"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgToolBar title="查看" icon="icon-search" url="jformAttenstatisticController.do?goUpdate" funname="detail" id="detail"></t:dgToolBar>
   <t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls" id="export"></t:dgToolBar>
   <t:dgToolBar title="重新计算" icon="icon-edit"  url="jformAttenstatisticController.do?doRecount" funname="doRecount" id="Recount"  ></t:dgToolBar>
   <t:dgToolBar title="重算全部" icon="icon-edit"  url="jformAttenstatisticController.do?doRecountall" funname="doRecountall" id="Recountall"  ></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
 $(document).ready(function(){
 });
 
//自定义按钮-重新计算
function doRecount(title,url,gridname){
	 var rowData = $('#'+gridname).datagrid('getSelected');
 		if (!rowData) {
	 		tip('请选择重新计算人员');
	 		return;
 	    }
 	 url = url+"&id="+rowData['id'];
 	 createdialog('确认 ', '确定'+title+'吗 ?', url,gridname);
}
//自定义按钮-重算全部
function doRecountall(title,url,gridname){
	 var rowData = $('#'+gridname).datagrid('getSelected');
 		if (!rowData) {
	 		tip('请选择重算全部项目');
	 		return;
 	    }
 	 url = url+"&id="+rowData['id'];
 	 createdialog('确认 ', '确定'+title+'吗 ?', url,gridname);
}
   
 
//导入
function ImportXls() {
	openuploadwin('Excel导入', 'jformAttenstatisticController.do?upload', "jformAttenstatisticList");
}

//导出
function ExportXls() {
	JeecgExcelExport("jformAttenstatisticController.do?exportXls","jformAttenstatisticList");
}

//模板下载
function ExportXlsByT() {
	JeecgExcelExport("jformAttenstatisticController.do?exportXlsByT","jformAttenstatisticList");
}
 </script>
