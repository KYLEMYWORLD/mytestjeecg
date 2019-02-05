<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="jformProjectList" checkbox="false" pagination="true" fitColumns="true" title="项目信息表" sortName="createDate" actionUrl="jformProjectController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="主键"  field="id"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="项目名称"  field="projectName"  query="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="项目类型"  field="projectType"  query="true"  queryMode="single"  dictionary="projeType"  width="120"></t:dgCol>
   <t:dgCol title="开始时间"  field="startDate"  formatter="yyyy-MM-dd"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="完成时间"  field="finishDate"  formatter="yyyy-MM-dd"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="项目经理ID"  field="projectManagerid"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="项目经理"  field="projectManager"  queryMode="single" dictionary="user_select,projectManagerid@projectManager,account@realname"  popup="true"   width="120"></t:dgCol>
   <t:dgCol title="计划表完成时间"  field="planfinishDate"  formatter="yyyy-MM-dd"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="项目描述"  field="projectInfo"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="项目状态"  field="projectStatus"  query="true"  queryMode="single"  dictionary="projStatus"  width="120"></t:dgCol>
   <t:dgCol title="项目负责人ID"  field="projectResponderid"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="项目负责人"  field="projectResponder"  queryMode="single"  dictionary="user_select,projectResponderid@projectResponder,account@realname"  popup="true"  width="120"></t:dgCol>
   <t:dgCol title="创建人名称"  field="createName"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建人登录名称"  field="createBy"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建日期"  field="createDate"  formatter="yyyy-MM-dd"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新人名称"  field="updateName"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新人登录名称"  field="updateBy"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新日期"  field="updateDate"  formatter="yyyy-MM-dd"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="流程状态"  field="bpmStatus"  hidden="true"  queryMode="single"  dictionary="bpm_status"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt id="delete" exp="projectStatus#eq#0" title="删除" url="jformProjectController.do?doDel&id={id}" urlclass="ace_button"  urlfont="fa-trash-o" operationCode="delete"/>
   <t:dgToolBar id="add" title="添加项目" icon="icon-add" url="jformProjectController.do?goAdd" funname="add"></t:dgToolBar>
	<t:dgToolBar id="update" title="编辑项目" icon="icon-edit" url="jformProjectController.do?goUpdate" funname="update"></t:dgToolBar>
   <t:dgToolBar id="detail" title="查看" icon="icon-search" url="jformProjectController.do?goUpdate" funname="detail"></t:dgToolBar>
   <t:dgToolBar id="export" title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
   <t:dgToolBar id="activate" title="激活" icon="icon-edit"  url="jformProjectController.do?doActivate" funname="doActivate"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
 $(document).ready(function(){
 });
 
 //自定义按钮-激活
 function doActivate(title,url,gridname){
  var rowData = $('#'+gridname).datagrid('getSelected');
  if (!rowData) {
   tip('请选择激活项目');
   return;
  }
  url = url+"&id="+rowData['id'];
  createdialog('确认 ', '确定'+title+'吗 ?', url,gridname);
 }
   
 
//导入
function ImportXls() {
	openuploadwin('Excel导入', 'jformProjectController.do?upload', "jformProjectList");
}

//导出
function ExportXls() {
	JeecgExcelExport("jformProjectController.do?exportXls","jformProjectList");
}

//模板下载
function ExportXlsByT() {
	JeecgExcelExport("jformProjectController.do?exportXlsByT","jformProjectList");
}

 </script>
