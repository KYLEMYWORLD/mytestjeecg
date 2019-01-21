<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="jformTestList" checkbox="false" pagination="true" fitColumns="true" title="测试表" sortName="createDate" actionUrl="jformTestController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="主键"  field="id"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建人名称"  field="createName"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建人登录名称"  field="createBy"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建日期"  field="createDate"  formatter="yyyy-MM-dd"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新人名称"  field="updateName"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新人登录名称"  field="updateBy"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新日期"  field="updateDate"  formatter="yyyy-MM-dd"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="所属部门"  field="sysOrgCode"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="所属公司"  field="sysCompanyCode"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="流程状态"  field="bpmStatus"  queryMode="single"  dictionary="bpm_status"  width="120"></t:dgCol>
   <t:dgCol title="用户ID"  field="userid"  queryMode="single"  dictionary="user_select,userid,id"  popup="true"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt id="del" title="删除" url="jformTestController.do?doDel&id={id}" urlclass="ace_button"  urlfont="fa-trash-o"/>
	<t:dgFunOpt id="test_2" funname="doTest2(id)" title="测试2" urlclass="ace_button" urlfont="fa-wrench" />
	<t:dgFunOpt id="test_js" exp="bpmStatus#eq#1" funname="doTest_js(id)" title="测试JS" urlclass="ace_button" urlfont="fa-wrench" />
   <t:dgToolBar title="录入" icon="icon-add" url="jformTestController.do?goAdd" funname="add" id="add"></t:dgToolBar>
	<t:dgToolBar title="编辑" icon="icon-edit" url="jformTestController.do?goUpdate" funname="update" id="update"></t:dgToolBar>
   <t:dgToolBar title="批量删除"  icon="icon-remove" url="jformTestController.do?doBatchDel" funname="deleteALLSelect" id="deleteselect"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="jformTestController.do?goUpdate" funname="detail" id="detail"></t:dgToolBar>
   <t:dgToolBar title="导入" icon="icon-put" funname="ImportXls" id="import"></t:dgToolBar>
   <t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls" id="export"></t:dgToolBar>
   <t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT" id="excel"></t:dgToolBar>
     	<t:dgToolBar title="测试1" icon="icon-edit"  url="jformTestController.do?doTest1" funname="doTest1" id="Test1"  ></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
 $(document).ready(function(){
 });
 
//自定义按钮-测试1
function doTest1(title,url,gridname){
	 var rowData = $('#'+gridname).datagrid('getSelected');
 		if (!rowData) {
	 		tip('请选择测试1项目');
	 		return;
 	    }
 	 url = url+"&id="+rowData['id'];
 	 createdialog('确认 ', '确定'+title+'吗 ?', url,gridname);
}
   
//自定义按钮-测试2
function doTest2(id,index){
	var url = "jformTestController.do?doTest2";
	url = url+"&id="+id;
	createdialog('确认 ', '确定测试2吗 ?', url,'jformTestList');
}
//自定义按钮-测试JS
function doTest_js(id,index){
	 test_js(id);
}
 
//导入
function ImportXls() {
	openuploadwin('Excel导入', 'jformTestController.do?upload', "jformTestList");
}

//导出
function ExportXls() {
	JeecgExcelExport("jformTestController.do?exportXls","jformTestList");
}

//模板下载
function ExportXlsByT() {
	JeecgExcelExport("jformTestController.do?exportXlsByT","jformTestList");
}

 </script>
