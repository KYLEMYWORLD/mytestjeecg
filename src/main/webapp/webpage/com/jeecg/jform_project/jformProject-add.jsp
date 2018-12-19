<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>项目信息表</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="jformProjectController.do?doAdd" >
	  <input id="id" name="id" type="hidden" value="${jformProjectPage.id }"/>
	  <input type="hidden" id="projectManagerid" name="projectManagerid" value="">
	  <table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							项目名称:
						</label>
					</td>
					<td class="value">
					     	 <input id="projectName"  validType="jform_project,project_name,id" name="projectName" type="text" maxlength="32" style="width: 150px" class="inputxt"  datatype="*"  ignore="checked" />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">项目名称</label>
					</td>
					<td align="right">
						<label class="Validform_label">
							项目类型:
						</label>
					</td>
					<td class="value">
						<t:dictSelect field="projectType" type="list"  datatype="n"  typeGroupCode="projeType"  defaultVal="${jformProjectPage.projectType}" hasLabel="false"  title="项目类型" ></t:dictSelect>
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
							<input readonly id="startDate" name="startDate" type="text" style="width: 150px" class="Wdate" onClick="WdatePicker()"  datatype="*"  ignore="checked" />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">开始时间</label>
					</td>
					<td align="right">
						<label class="Validform_label">
							完成时间:
						</label>
					</td>
					<td class="value">
						<input readonly id="finishDate" name="finishDate" type="text" style="width: 150px" class="Wdate" onClick="WdatePicker()"  datatype="*"  ignore="checked"/>
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
						<input readonly id="projectManager" name="projectManager" type="text" maxlength="32" style="width: 150px" class="searchbox-inputtext"  datatype="*" onclick="popupClick(this,'account,realname','projectManagerid,projectManager','user_select_single')"  ignore="checked" />
						<span class="Validform_checktip"></span>
						<label class="Validform_label" style="display: none;">项目经理</label>
					</td>
					<td align="right">
						<label class="Validform_label">
							项目负责人:
						</label>
					</td>
					<td class="value">
						<input id="projectResponderid" name="projectResponderid" type="hidden"/>
						<input readonly id="projectResponder" name="projectResponder" type="text" style="width: 150px" class="searchbox-inputtext"  datatype="*"  ignore="checked"   onclick="popupClick(this,'account,realname','projectResponderid,projectResponder','user_select')"  />
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
							<input readonly id="planfinishDate" name="planfinishDate" type="text" style="width: 150px" class="Wdate" onClick="WdatePicker()"  ignore="ignore" />
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
					<td class="value"  colspan="3">
						 	<textarea style="height:auto;width:95%" class="inputxt" rows="6" id="projectInfo" name="projectInfo"  ignore="ignore" ></textarea>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">项目描述</label>
						</td>
					</tr>
			</table>
		</t:formvalid>
 </body>
