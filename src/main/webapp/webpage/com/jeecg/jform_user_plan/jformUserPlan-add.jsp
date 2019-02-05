<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>个人计划安排</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <style type="text/css">
  	.combo_self{height: 22px !important;width: 150px !important;}
  	.layout-header .btn {
	    margin:0;
	   float: none !important;
	}
	.btn-default {
	    height: 35px;
	    line-height: 35px;
	    font-size:14px;
	}
  </style>
  
  <script type="text/javascript">
	$(function(){
		$(".combo").removeClass("combo").addClass("combo combo_self");
		$(".combo").each(function(){
			$(this).parent().css("line-height","0px");
		});   
	});

	/**树形列表数据转换**/
  function convertTreeData(rows, textField) {
      for(var i = 0; i < rows.length; i++) {
          var row = rows[i];
          row.text = row[textField];
          if(row.children) {
          	row.state = "open";
              convertTreeData(row.children, textField);
          }
      }
  }
  /**树形列表加入子元素**/
  function joinTreeChildren(arr1, arr2) {
      for(var i = 0; i < arr1.length; i++) {
          var row1 = arr1[i];
          for(var j = 0; j < arr2.length; j++) {
              if(row1.id == arr2[j].id) {
                  var children = arr2[j].children;
                  if(children) {
                      row1.children = children;
                  }
                  
              }
          }
      }
  }
  </script>
  <script type="text/javascript">
  //编写自定义JS代码
  $(document).ready(function(){
	  var userid = '${userid}';
	  var username = '${username}';
	  var operate = '${operate}';
	  if(operate!='' && operate=='dividetype'){
	  	//细分任务
		  $("#planLevel").val(3);
	  }else{
	  	//个人任务
		  $("#planLevel").val(4);
	  }
	  $("#planId").attr("disabled",true);
	  $(".combo-arrow").off("click");
	  $("#planLevel").attr("disabled",true);

	  $("#planResponderid").val(userid);
	  $("#planResponder").val(username);
	  $("#planResponders").val(username);
	  $("#planResponder").attr("disabled",true);
  });

  //提交表单前恢复可编辑状态，才会把值传到后台
  function enableInput() {
	  $("#planResponder").attr("disabled",false);
	  $("#planLevel").attr("disabled",false);
	  $("#planId").attr("disabled",false);
  }

  </script>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="jformUserPlanController.do?doAdd" beforeSubmit="enableInput">
	  <input id="id" name="id" type="hidden" value="${jformUserPlanPage.id }"/>
	  <input id="planOrder" name="planOrder" type="hidden" value="${jformUserPlanPage.planOrder }"/>
	  <input id="planResponderid" name="planResponderid" type="hidden" value="${jformUserPlanPage.planResponderid }"/>
	  <input id="planResponders" name="planResponders" type="hidden" value="${jformUserPlanPage.planResponders }"/>
	  <input id="planRejectmsg" name="planRejectmsg" type="hidden" value="${jformUserPlanPage.planRejectmsg }"/>
	  <input id="projectId" name="projectId" type="hidden" value="${jformUserPlanPage.projectId }"/>
	  <input id="taskId" name="taskId" type="hidden" value="${jformUserPlanPage.taskId }"/>
	  <input id="planIssucc" name="planIssucc" type="hidden"  value="0"/>
	  <input id="planStatus" name="planStatus" type="hidden"  value="0"/>
	  <input id="planLatemsg" name="planLatemsg" type="hidden"  value="${jformUserPlanPage.planLatemsg }"/>
	  <input id="planIsalert" name="planIsalert" type="hidden" value="1"/>
	  <input id="planAlertmsg" name="planAlertmsg" type="hidden"  value="${jformUserPlanPage.planAlertmsg }"/>

	  <table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							任务名称:
						</label>
					</td>
					<td class="value">
					     	 <input id="planName" name="planName" type="text" maxlength="50" style="width: 150px" class="inputxt"  ignore="ignore" />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">任务名称</label>
						</td>
					<td align="right">
						<label class="Validform_label">
							任务等级:
						</label>
					</td>
					<td class="value">
							<t:dictSelect id="planLevel" field="planLevel" type="list" datatype="n"  typeGroupCode="userPlanT"   defaultVal="${jformPlanPage.planLevel}" hasLabel="false"  title="任务等级" ></t:dictSelect>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">任务等级</label>
						</td>
				</tr>
				<tr>

					<td align="right">
						<label class="Validform_label">
							任务开始时间:
						</label>
					</td>
					<td class="value">
							   <input id="startDate" readonly name="startDate" type="text" style="width: 150px" class="Wdate" onClick="WdatePicker()"  ignore="ignore" />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">任务开始时间</label>
						</td>
					<td align="right">
						<label class="Validform_label">
							任务完成时间:
						</label>
					</td>
					<td class="value">
						<input id="finishDate" readonly name="finishDate" type="text" style="width: 150px" class="Wdate" onClick="WdatePicker()"  ignore="ignore" />
						<span class="Validform_checktip"></span>
						<label class="Validform_label" style="display: none;">任务完成时间</label>
					</td>
				</tr>
				<tr>

					<td align="right">
						<label class="Validform_label">
							负责人:
						</label>
					</td>
					<td class="value">
						<input id="planResponder"  name="planResponder" type="text" style="width: 150px" class="searchbox-inputtext"    datatype="*"  ignore="checked" />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">负责人</label>
						</td>
					<td align="right">
						<label class="Validform_label">
							任务父ID:
						</label>
					</td>
					<td class="value">
						<input id="planId" name="planId" type="text" style="width: 150px" class="inputxt easyui-combotree"  ignore="ignore"
							   data-options="panelHeight:'220',
				                    url: 'jformUserPlanController.do?datagrid&field=id,planName',
				                    loadFilter: function(data) {
				                    	var rows = data.rows || data;
				                    	var win = frameElement.api.opener;
				                    	var listRows = win.getDataGrid().treegrid('getData');
				                    	joinTreeChildren(rows, listRows);
				                    	convertTreeData(rows, 'planName');
				                    	return rows;
				                    },
				                    onSelect:function(node){
				                    	$('#planId').val(node.id);
				                    },
				                    onLoadSuccess: function() {
				                    	var win = frameElement.api.opener;
				                    	var currRow = win.getDataGrid().treegrid('getSelected');
				                    	if(!'${jformUserPlanPage.id}') {
				                    		//增加时，选择当前父菜单
				                    		if(currRow) {
				                    			$('#planId').combotree('setValue', currRow.id);
				                    		}
				                    	}else {
				                    		//编辑时，选择当前父菜单
				                    		if(currRow) {
				                    			$('#planId').combotree('setValue', currRow.planId);
				                    		}
				                    	}
				                    }"/>
						<span class="Validform_checktip"></span>
						<label class="Validform_label" style="display: none;">任务父ID</label>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							任务说明:
						</label>
					</td>
					<td class="value"  colspan="3" >
						  	 <textarea style="height:auto;width:95%" class="inputxt" rows="6" id="planInfo" name="planInfo"  ignore="ignore" ></textarea>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">任务说明</label>
						</td>
				</tr>
			</table>
		</t:formvalid>
 </body>
