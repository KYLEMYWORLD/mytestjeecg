<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>项目看板信息表</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="jformEchartController.do?doAdd" >
					<input id="id" name="id" type="hidden" value="${jformEchartPage.id }"/>
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							项目ID:
						</label>
					</td>
					<td class="value">
					     	 <input id="projectId" name="projectId" type="text" maxlength="36" style="width: 150px" class="inputxt"  datatype="*"  ignore="checked" />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">项目ID</label>
						</td>
					<td align="right">
						<label class="Validform_label">
							任务ID:
						</label>
					</td>
					<td class="value">
					     	 <input id="taskId" name="taskId" type="text" maxlength="36" style="width: 150px" class="inputxt"  datatype="*"  ignore="checked" />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">任务ID</label>
						</td>
					</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							父任务ID:
						</label>
					</td>
					<td class="value">
					     	 <input id="pretaskId" name="pretaskId" type="text" maxlength="36" style="width: 150px" class="inputxt"  ignore="ignore" />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">父任务ID</label>
						</td>
					<td align="right">
						<label class="Validform_label">
							任务等级:
						</label>
					</td>
					<td class="value">
							  <t:dictSelect id="taskLevel" field="taskLevel" type="list"  datatype="n"  typeGroupCode="planLevel"  defaultVal="${jformEchartPage.taskLevel}" hasLabel="false"  title="任务等级" ></t:dictSelect>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">任务等级</label>
						</td>
					</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							任务名称:
						</label>
					</td>
					<td class="value">
					     	 <input id="taskName" name="taskName" type="text" maxlength="200" style="width: 150px" class="inputxt"  datatype="*"  ignore="checked" />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">任务名称</label>
						</td>
					<td align="right">
						<label class="Validform_label">
							任务短名称:
						</label>
					</td>
					<td class="value">
					     	 <input id="taskShortname" name="taskShortname" type="text" maxlength="50" style="width: 150px" class="inputxt"  datatype="*"  ignore="checked" />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">任务短名称</label>
						</td>
					</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							预警状态:
						</label>
					</td>
					<td class="value">
							  <t:dictSelect id="alertStatus" field="alertStatus" type="list"  datatype="n"  typeGroupCode="planAlert"  defaultVal="${jformEchartPage.alertStatus}" hasLabel="false"  title="预警状态" ></t:dictSelect>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">预警状态</label>
						</td>
					<td align="right">
						<label class="Validform_label">
							预警信息:
						</label>
					</td>
					<td class="value">
					     	 <input id="alertMsg" name="alertMsg" type="text" maxlength="200" style="width: 150px" class="inputxt"  ignore="ignore" />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">预警信息</label>
						</td>
					</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							任务状态:
						</label>
					</td>
					<td class="value">
							  <t:dictSelect id="taskStatus" field="taskStatus" type="list"  datatype="n"  typeGroupCode="planSucc"  defaultVal="${jformEchartPage.taskStatus}" hasLabel="false"  title="任务状态" ></t:dictSelect>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">任务状态</label>
						</td>
					<td align="right">
						<label class="Validform_label">
							开始时间:
						</label>
					</td>
					<td class="value">
							   <input id="startDate" name="startDate" type="text" style="width: 150px" class="Wdate" onClick="WdatePicker()"  datatype="*"  ignore="checked" />    
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">开始时间</label>
						</td>
					</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							结束时间:
						</label>
					</td>
					<td class="value">
							   <input id="finishDate" name="finishDate" type="text" style="width: 150px" class="Wdate" onClick="WdatePicker()"  datatype="*"  ignore="checked" />    
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">结束时间</label>
						</td>
					<td align="right">
						<label class="Validform_label">
							实际完成时间:
						</label>
					</td>
					<td class="value">
							   <input id="rfinishDate" name="rfinishDate" type="text" style="width: 150px" class="Wdate" onClick="WdatePicker()"  ignore="ignore" />    
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">实际完成时间</label>
						</td>
					</tr>
				
				
			</table>
		</t:formvalid>
 </body>
