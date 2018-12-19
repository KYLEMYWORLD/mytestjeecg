<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>项目信息表</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript">
  //编写自定义JS代码
  var status = ${jformProjectPage.projectStatus};
  $(document).ready(function(){
	  if(status!=0){
		  $("#projectName").attr("disabled","disabled");
		  $("[name='projectType']").attr("disabled","disabled");
		  $("#startDate").attr("disabled","disabled");
		  $("#finishDate").attr("disabled","disabled");
	  }
  });
  </script>
 </head>
 <body>
		<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="jformProjectController.do?doUpdate" >
			<input id="id" name="id" type="hidden" value="${jformProjectPage.id }"/>
			<input id="projectManagerid" name="projectManagerid" type="hidden" value="${jformProjectPage.projectManagerid }"/>
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
					<tr>
						<td align="right">
							<label class="Validform_label">
								项目名称:
							</label>
						</td>
						<td class="value">
						    <input id="projectName" name="projectName" type="text" maxlength="32" style="width: 150px" class="inputxt"  datatype="*"  ignore="checked"  value='${jformProjectPage.projectName}'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">项目名称</label>
						</td>
						<td align="right">
							<label class="Validform_label">
								项目类型:
							</label>
						</td>
						<td class="value">
									<t:dictSelect field="projectType" type="list"  datatype="n"  typeGroupCode="projeType"   defaultVal="${jformProjectPage.projectType}" hasLabel="false"  title="项目类型" ></t:dictSelect>     
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">项目类型</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								开始时间:
							</label>
						</td>
						<td class="value">
							<input readonly id="startDate" name="startDate" type="text" style="width: 150px"  class="Wdate" onClick="WdatePicker()"  datatype="*"  ignore="checked" value='<fmt:formatDate value='${jformProjectPage.startDate}' type="date" pattern="yyyy-MM-dd"/>'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">开始时间</label>
						</td>
						<td align="right">
							<label class="Validform_label">
								完成时间:
							</label>
						</td>
						<td class="value">
							<input readonly id="finishDate" name="finishDate" type="text" style="width: 150px"  class="Wdate" onClick="WdatePicker()"  datatype="*"  ignore="checked" value='<fmt:formatDate value='${jformProjectPage.finishDate}' type="date" pattern="yyyy-MM-dd"/>'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">完成时间</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								项目经理:
							</label>
						</td>
						<td class="value">
						    <input readonly id="projectManager" name="projectManager" type="text" maxlength="32" style="width: 150px" class="searchbox-inputtext"  datatype="*"  ignore="checked" onclick="popupClick(this,'account,realname','projectManagerid,projectManager','user_select')"  value='${jformProjectPage.projectManager}'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">项目经理</label>
						</td>
						<td align="right">
							<label class="Validform_label">
								项目负责人:
							</label>
						</td>
						<td class="value">
							<input id="projectResponderid" name="projectResponderid" type="hidden" value="'${jformProjectPage.projectResponderid}'"/>
							<input id="projectResponder" readonly name="projectResponder" type="text" style="width: 150px" class="searchbox-inputtext"  datatype="*"  ignore="checked"   onclick="popupClick(this,'account,realname','projectResponderid,projectResponder','user_select')"    value='${jformProjectPage.projectResponder}'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">项目负责人</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								计划表完成时间:
							</label>
						</td>
						<td class="value" colspan="3">
							<input readonly id="planfinishDate" name="planfinishDate" type="text" style="width: 150px"  class="Wdate" onClick="WdatePicker()"  ignore="ignore" value='<fmt:formatDate value='${jformProjectPage.planfinishDate}' type="date" pattern="yyyy-MM-dd"/>'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">计划表完成时间</label>
						</td>
					</tr>
				
					<tr>
						<td align="right">
							<label class="Validform_label">
								项目描述:
							</label>
						</td>
						<td class="value" colspan="3">
						  	 	<textarea id="projectInfo" style="height:auto;width:95%;" class="inputxt" rows="6" name="projectInfo"  ignore="ignore" >${jformProjectPage.projectInfo}</textarea>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">项目描述</label>
						</td>
					</tr>
			</table>
		</t:formvalid>
 </body>
