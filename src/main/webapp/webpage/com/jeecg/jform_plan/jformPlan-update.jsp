<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>项目任务计划表</title>
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
		  var status ='${jformPlanPage.planStatus}';
		  //草稿	下发细分	细分完成
		  if(status!='0' && status!='1' && status!='2'){
		  	$("#planName").attr("disabled","disabled");
		  	$("#planId").attr("disabled","disabled");
		  	$("#startDate").attr("readonly","readonly");
		  	$("#planInfo").attr("disabled","disabled");
		  	$("#finishDate").attr("disabled","disabled");
		  }

		  //草稿
		  if(status!='0'){// || '${jformPlanPage.planLevel}'=='2'){
			  $("#planResponder").attr("disabled","disabled");
		  }
		  $(".combo-arrow").off("click");


		  var level = ${jformPlanPage.planLevel};
		  if(level == 2){
			  $("#planLevelS").val("2");//二级
			  $("#planLevel").val("2");//二级
			  $("#planLevelS").find("option[value='2']").attr("selected",true);
		  }else {
			  $("#planLevelS").val("3");//三级
			  $("#planLevel").val("3");//三级
			  $("#planLevelS").find("option[value='3']").attr("selected", true);
			  $("#planResponder").attr("onclick", "popupClick(this,'account,realname','planResponderid,planResponder','user_select_single')");
		  }
	  });

  </script>
 </head>
 <body>
		<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="jformPlanController.do?doUpdate" >
			<input id="id" name="id" type="hidden" value="${jformPlanPage.id }"/>
			<input id="planResponderid" name="planResponderid" type="hidden" value="${jformPlanPage.planResponderid }"/>

			<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
					<tr>
						<td align="right">
							<label class="Validform_label">
								任务名称:
							</label>
						</td>
						<td class="value">
						    <input id="planName" name="planName" type="text" maxlength="50" style="width: 150px" class="inputxt"  ignore="ignore"  value='${jformPlanPage.planName}'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">任务名称</label>
						</td>
						<td align="right">
							<label class="Validform_label">
								任务等级:
							</label>
						</td>
						<td class="value">
							<t:dictSelect field="planLevel" readonly="readonly" type="list"  datatype="n"  typeGroupCode="planLevel"   defaultVal="${jformPlanPage.planLevel}" hasLabel="false"  title="任务等级" ></t:dictSelect>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">任务等级</label>
						</td>
					</tr>
					<tr>
						<!--
						<td align="right">
							<label class="Validform_label">
								任务顺序:
							</label>
						</td>
						<td class="value">
						    <input id="planOrder" name="planOrder" type="text" maxlength="32" style="width: 150px" class="inputxt"  datatype="n"  ignore="ignore"  value='${jformPlanPage.planOrder}'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">任务顺序</label>
						</td>-->
						<td align="right">
							<label class="Validform_label">
								任务父ID:
							</label>
						</td>
						<td class="value" colspan="3">
							<input id="planId" name="planId" readonly type="text" style="width: 150px" class="inputxt easyui-combotree"  ignore="ignore"
							value='${jformPlanPage.planId}'
							data-options="
				                    panelHeight:'220',
				                    url: 'jformPlanController.do?datagrid&field=id,planName',  
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
				                    	if(!'${jformPlanPage.id}') {
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
								任务开始时间:
							</label>
						</td>
						<td class="value">
							<input id="startDate" name="startDate" readonly type="text" style="width: 150px"  class="Wdate" onClick="WdatePicker()"  ignore="ignore" value='<fmt:formatDate value='${jformPlanPage.startDate}' type="date" pattern="yyyy-MM-dd"/>'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">任务开始时间</label>
						</td>
						<td align="right">
							<label class="Validform_label">
								任务完成时间:
							</label>
						</td>
						<td class="value">
							<input id="finishDate" name="finishDate" readonly type="text" style="width: 150px"  class="Wdate" onClick="WdatePicker()"  ignore="ignore" value='<fmt:formatDate value='${jformPlanPage.finishDate}' type="date" pattern="yyyy-MM-dd"/>'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">任务完成时间</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								完成状态:
							</label>
						</td>
						<td class="value">
							<t:dictSelect field="planIssucc" type="list" readonly="readonly" datatype="n"  typeGroupCode="planSucc"   defaultVal="${jformPlanPage.planIssucc}" hasLabel="false"  title="完成状态" ></t:dictSelect>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">完成状态</label>
						</td>
						<td align="right">
							<label class="Validform_label">
								负责人:
							</label>
						</td>
						<td class="value">
							<input id="planResponder" name="planResponder" type="text" style="width: 150px" class="searchbox-inputtext"  ignore="ignore"  onclick="popupClick(this,'account,realname','planResponderid,planResponder','user_select')" value='${jformPlanPage.planResponder}'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">负责人</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								任务状态:
							</label>
						</td>
						<td class="value">
							<t:dictSelect field="planStatus" type="list" readonly="readonly"  datatype="n"  typeGroupCode="planStatus"   defaultVal="${jformPlanPage.planStatus}" hasLabel="false"  title="任务状态" ></t:dictSelect>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">任务状态</label>
						</td>
						<td align="right">
							<label class="Validform_label">
								预警状态:
							</label>
						</td>
						<td class="value">
							<t:dictSelect field="planIsalert" type="list" readonly="readonly"  datatype="n"  typeGroupCode="planAlert"   defaultVal="${jformPlanPage.planIsalert}" hasLabel="false"  title="预警状态" ></t:dictSelect>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">预警状态</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								任务说明:
							</label>
						</td>
						<td class="value" colspan="3">
							<textarea style="height:auto;width:95%" class="inputxt" rows="6" id="planInfo" name="planInfo"  ignore="ignore" maxlength="300">${jformPlanPage.planInfo}</textarea>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">任务说明</label>
						</td>
					</tr>
				
			</table>
		</t:formvalid>
 </body>
