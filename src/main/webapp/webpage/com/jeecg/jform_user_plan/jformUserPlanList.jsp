<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="jformUserPlanList" checkbox="false" pagination="true" treegrid="true" treeField="planName" fitColumns="true" title="个人计划安排" sortName="createDate" actionUrl="jformUserPlanController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="id"  field="id"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="任务名称"  query="true" field="planName"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="项目名称"  field="projectId"  queryMode="single" query="true"  width="120"  dictionary="jform_project,id,project_name"   searchpopdic="project_select,projectId@cname,id@project_name"></t:dgCol>
   <t:dgCol title="任务等级"  query="true"  field="planLevel"  queryMode="single" dictionary="userPlanT"  width="65"></t:dgCol>
   <t:dgCol title="任务顺序"  field="planOrder"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="任务父ID" hidden="true"  field="planId"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="任务状态"  query="true" field="planStatus" dictionary="planStatus"  queryMode="single"  width="40"></t:dgCol>
   <t:dgCol title="任务开始时间" query="true" field="startDate"  formatter="yyyy-MM-dd"  queryMode="group"  width="80"></t:dgCol>
   <t:dgCol title="任务完成时间"  query="true" field="finishDate"  formatter="yyyy-MM-dd"  queryMode="group"  width="80"></t:dgCol>
   <t:dgCol title="实际完成时间"  query="false" field="realfinishDate"  formatter="yyyy-MM-dd"  queryMode="group"  width="80"></t:dgCol>
   <t:dgCol title="负责人ID"  field="planResponderid"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="负责人"  field="planResponder"  queryMode="single"  width="80"  query="true"  dictionary="user_select_single,planResponderid@planResponder,account@realname"  popup="true" ></t:dgCol>
   <t:dgCol title="全部负责人"  field="planResponders"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="任务说明"  field="planInfo"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="审批内容"  field="planRejectmsg"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="完成状态"  hidden="true"  field="planIssucc"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="延时原因"  hidden="true"  field="planLatemsg"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="预警状态"  hidden="true"  field="planIsalert"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="预警原因"  hidden="true"  field="planAlertmsg"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="分配任务ID"  field="taskId"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt title="删除" exp="planLevel#eq#3,4&&planStatus#eq#0,1,2" url="jformUserPlanController.do?doDel&id={id}" urlclass="ace_button"  urlfont="fa-trash-o"/>
   <t:dgFunOpt title="细分完成"  exp="planLevel#eq#2&&planStatus#eq#1" funname="doDividefinish(id)" urlclass="ace_button" urlfont="fa-wrench" />
   <t:dgToolBar title="细分任务" icon="icon-add"  url="jformUserPlanController.do?goAdd" funname="doDivideAdd" id="adduserplan"  ></t:dgToolBar>
   <t:dgToolBar title="添加个人任务" icon="icon-add" url="jformUserPlanController.do?goAdd" funname="doAdduserplan" id="add"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="jformUserPlanController.do?goUpdate" funname="beforeUpdatetree" width="100%" height="100%" id="updatetree"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="jformUserPlanController.do?goUpdate" funname="detailtree" width="640" height="450" id="detailtree"></t:dgToolBar>
   <t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls" id="export"></t:dgToolBar>

  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
 $(document).ready(function(){
		$("#jformUserPlanList").treegrid({
 				 onExpand : function(row){
 					var children = $("#jformUserPlanList").treegrid('getChildren',row.id);
 					 if(children.length<=0){
 					 	row.leaf=true;
 					 	$("#jformUserPlanList").treegrid('refresh', row.id);
 					 }
 				}
 		});
 });

 function  beforeUpdatetree(title,url, id,width,height,isRestful) {
  var row = $('#' + id).treegrid('getSelections');
  if (row.length > 0) {
   if (row[0].planLevel == '2') {
    tip("二级下发任务，不能编辑！");
    return;
   }
  }
  updatetree(title,url, id,'60%','50%',isRestful);
 }
 
 
 function doAdduserplan(title,url,id,wwith,wheight){
  var row = $('#'+id).treegrid('getSelections');
  if(row.length > 0){
   $('#'+id).treegrid("clearChecked");
   $('#'+id).treegrid("clearSelections");
  }
  url+="&operate=usertype";
  add(title,url,id,wwith,wheight);
 }

 //自定义按钮-细分任务
 function doDivideAdd(title,url,id,wwith,wheight) {
  var row = $('#' + id).treegrid('getSelections');
  if (row.length > 0) {
   if (row[0].planLevel == '4') {
    tip("不能在个人任务下面添加任务！");
    return;
   }
   if (row[0].planLevel == '3') {
    tip("不能在三级任务下添加任务了！");
    return;
   } else if (row[0].planStatus != "0" && row[0].planStatus != "1" && row[0].planStatus != "2") {
    tip("当前任务状态不是草稿，下发细分，细分完成状态，不能继续新增！")
    return;
   }
   url+="&operate=dividetype";
   add(title, url, id, wwith, wheight);
  } else {
   tip("请先选择二级任务！");
   return;
  }
 }
   
//自定义按钮-细分完成
function doDividefinish(id,index){
	var url = "jformUserPlanController.do?doDividefinish";
	url = url+"&id="+id;
	createdialog('确认 ', '确定细分完成吗 ?', url,'jformUserPlanList');
}
 
//导入
function ImportXls() {
	openuploadwin('Excel导入', 'jformUserPlanController.do?upload', "jformUserPlanList");
}

//导出
function ExportXls() {
	JeecgExcelExport("jformUserPlanController.do?exportXls","jformUserPlanList");
}

//模板下载
function ExportXlsByT() {
	JeecgExcelExport("jformUserPlanController.do?exportXlsByT","jformUserPlanList");
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
