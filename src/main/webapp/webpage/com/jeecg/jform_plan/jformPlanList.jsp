<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="jformPlanList" checkbox="false" pagination="true" treegrid="true" treeField="planName" fitColumns="true" title="项目任务计划表" sortName="createDate" actionUrl="jformPlanController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="主键"  field="id"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="项目名称"  field="projectId" query="true"  queryMode="single"  width="60"  dictionary="jform_project,id,project_name"></t:dgCol>
   <t:dgCol title="任务名称"  field="planName"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="任务等级"  field="planLevel"  queryMode="single"  dictionary="planLevel"  width="60"></t:dgCol>
   <t:dgCol title="任务状态"  field="planStatus"  query="true"  queryMode="single"  dictionary="planStatus"  width="60"></t:dgCol>
   <t:dgCol title="任务顺序"  field="planOrder"  queryMode="single" hidden="true"  width="60"></t:dgCol>
   <t:dgCol title="任务父ID"  field="planId"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="任务开始时间"  field="startDate"  formatter="yyyy-MM-dd"  query="true"  queryMode="single"  width="80"></t:dgCol>
   <t:dgCol title="任务完成时间"  field="finishDate"  formatter="yyyy-MM-dd"  query="true"  queryMode="single"  width="80"></t:dgCol>
   <t:dgCol title="负责人ID"  field="planResponderid"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="负责人"  field="planResponder"  queryMode="single"  dictionary="user_select,planResponderid@planResponder,account@realname"  popup="true"  width="80"></t:dgCol>
   <t:dgCol title="审批内容" hidden="true"  field="planRejectmsg"  queryMode="single"  width="60"></t:dgCol>
   <t:dgCol title="完成状态"  field="planIssucc"  query="true"  queryMode="single"  dictionary="planSucc"  width="60"></t:dgCol>
   <t:dgCol title="延时原因" hidden="true"  field="planLatemsg"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="预警状态"  field="planIsalert"  query="true"  queryMode="single"  dictionary="planAlert"  width="60"></t:dgCol>
   <t:dgCol title="预警原因" hidden="true"  field="planAlertmsg"  queryMode="single"  width="60"></t:dgCol>
   <t:dgCol title="任务分配名称" field="taskId" queryMode="single" width="60" dictionary="jform_task,id,task_name"></t:dgCol>
   <t:dgCol title="任务说明"  field="planInfo"  queryMode="single"  width="80"></t:dgCol>
   <t:dgCol title="创建人名称"  field="createName"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建人登录名称"  field="createBy"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建日期"  field="createDate"  formatter="yyyy-MM-dd"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新人名称"  field="updateName"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新人登录名称"  field="updateBy"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新日期"  field="updateDate"  formatter="yyyy-MM-dd"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="所属部门"  field="sysOrgCode"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="所属公司"  field="sysCompanyCode"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="流程状态"  field="bpmStatus"  hidden="true"  queryMode="single"  dictionary="bpm_status"  width="120"></t:dgCol>

   <t:dgCol title="操作" field="opt" width="180"></t:dgCol>
   <t:dgDelOpt exp="planStatus#eq#0,1" title="删除" url="jformPlanController.do?doDel&id={id}" urlclass="ace_button"  urlfont="fa-trash-o"/>
   <t:dgFunOpt id="doSendApprove" exp="planLevel#eq#1&&planStatus#eq#0,4" funname="doSendApprove(id,index)" title="提交审批" urlclass="ace_button" urlfont="fa-wrench" />
   <t:dgFunOpt id="doApprove"  exp="planLevel#eq#1&&planStatus#eq#3" funname="doApprove(id,index)" title="审批" urlclass="ace_button" urlfont="fa-wrench" />
   <t:dgFunOpt id="doApproveMsg"  exp="planLevel#eq#1&&planStatus#eq#4" funname="doApproveMsg(id,index)" title="查看审批信息" urlclass="ace_button" urlfont="fa-wrench" />
   <t:dgFunOpt id="doSendDivide"  exp="planLevel#eq#2&&planStatus#eq#0" funname="doSendDivide(id)" title="下发细分" urlclass="ace_button" urlfont="fa-wrench" />
   <t:dgFunOpt id="doDivideFinish"  exp="planLevel#eq#2&&planStatus#eq#1" funname="doDivideFinish(id)" title="细分完成" urlclass="ace_button" urlfont="fa-wrench" />
   <t:dgToolBar title="添加一级任务" icon="icon-add" url="jformPlanController.do?goAddFirst" funname="BeforeFirstAdd" id="addfirsttree"  width="635" height="470" ></t:dgToolBar>
   <t:dgToolBar title="添加任务" icon="icon-add" url="jformPlanController.do?goAdd" funname="BeforeAdd" id="addtree"  width="635" height="470" ></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="jformPlanController.do?goUpdate" width="635" height="470" funname="updatetree" id="updatetree"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="jformPlanController.do?goUpdate" funname="detailtree" id="detailtree"  width="635" height="470" ></t:dgToolBar>
   <t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls" id="export"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
 $(document).ready(function(){
		$("#jformPlanList").treegrid({
 				 onExpand : function(row){
 					var children = $("#jformPlanList").treegrid('getChildren',row.id);
 					 if(children.length<=0){
 					 	row.leaf=true;
 					 	$("#jformPlanList").treegrid('refresh', row.id);
 					 }
 				}
 		});
 });
 //添加一级任务
 //url="jformPlanController.do?goAddFirst"
 function BeforeFirstAdd(title,url,id,wwith,wheight){
   var row = $('#'+id).treegrid('getSelections');
   if(row.length > 0){
    $('#'+id).treegrid("clearChecked");
    $('#'+id).treegrid("clearSelections");
   }

   add(title,url,id,wwith,wheight);
 }

 //二三级任务添加方法
 //url="jformPlanController.do?goAdd"
 function BeforeAdd(title,url,id,wwith,wheight){
   var row = $('#'+id).treegrid('getSelections');
   if(row.length > 0){
    if(row[0].planLevel == '3'){
     tip("不能在三级任务下添加任务了！");
     return;
    }else if(row[0].planStatus != "0" &&row[0].planStatus != "1" &&row[0].planStatus != "2" ){
     tip("当前任务状态不是草稿，下发细分，细分完成状态，不能继续新增！")
     return;
    }
    add(title,url,id,wwith,wheight);
   }else{
    tip("请先选择一级或二级任务！");
    return;
   }
 }
   
 
//导入
function ImportXls() {
	openuploadwin('Excel导入', 'jformPlanController.do?upload', "jformPlanList");
}

//导出
function ExportXls() {
	JeecgExcelExport("jformPlanController.do?exportXls","jformPlanList");
}

//模板下载
function ExportXlsByT() {
	JeecgExcelExport("jformPlanController.do?exportXlsByT","jformPlanList");
}

 //自定义按钮-下发细分
 function doSendDivide(id,index){
     var url = "jformPlanController.do?doSendDivide";
     url = url+"&id="+id;
     createdialog('确认 ', '确定下发细分吗 ?', url,'jformPlanList');
 }
 //自定义按钮-细分完成
 function doDivideFinish(id,index){
     var url = "jformPlanController.do?doDivideFinish";
     url = url+"&id="+id;
     createdialog('确认 ', '确定细分完成吗 ?', url,'jformPlanList');
 }
 //自定义按钮-提交审批
 function doSendApprove(id,index){
     var url = "jformPlanController.do?doSendApprove";
     url = url+"&id="+id;
     createdialog('确认 ', '确定提交审批吗 ?', url,'jformPlanList');
 }

 //自定义按钮-审批
 function doApprove(id,index){
     createwindow('审核计划', 'jformPlanController.do?goApprove&id=' + id,420,280);
 }
 //自定义按钮-查看审批信息
 function doApproveMsg(_id,index){
  //debugger;
  // var rowdate = $('#jformPlanList').treegrid('getData');
  // var row = rowdate.find(function(x) {return x.id = _id;});
  var row=$('#jformPlanList').treegrid("find",_id);
  alertTipTop(row.planRejectmsg,'50%',"审批意见");
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
