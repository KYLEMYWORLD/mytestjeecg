<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>工作任务分配表</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="jformTaskController.do?doAdd" >
	  <input id="id" name="id" type="hidden" value="${jformTaskPage.id }"/>
	  <input id="taskProjectid" name="taskProjectid" type="hidden" value=""/>
	  <input id="taskResponderid" name="taskResponderid" type="hidden" value=""/>
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							项目名称:
						</label>
					</td>
					<td class="value">
							<input id="taskProjectname" name="taskProjectname" type="text" style="width: 150px" class="searchbox-inputtext"  datatype="*"  ignore="checked"   onclick="popupClick(this,'id,project_name','taskProjectid,taskProjectname','project_select')"  />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">项目名称</label>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							任务类型:
						</label>
					</td>
					<td class="value">
							  <t:dictSelect field="taskType" type="list"  datatype="n"  typeGroupCode="taskType"  defaultVal="${jformTaskPage.taskType}" hasLabel="false"  title="任务类型" ></t:dictSelect>     
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">任务类型</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							任务名称:
						</label>
					</td>
					<td class="value">
					     	 <input id="taskName" name="taskName" type="text" maxlength="100" style="width: 150px" class="inputxt"  datatype="*"  ignore="checked" />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">任务名称</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							负责人:
						</label>
					</td>
					<td class="value">
							<input id="taskResponder" name="taskResponder" type="text" style="width: 150px" class="searchbox-inputtext"  datatype="*"  ignore="checked"   onclick="popupClick(this,'account,realname','taskResponderid,taskResponder','user_select')"  />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">负责人</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							抄送人:
						</label>
					</td>
					<td class="value">
							<input type="hidden" id="taskNotifierid" name="taskNotifierid"/>
							<input id="taskNotifier" name="taskNotifier" type="text" style="width: 150px" class="searchbox-inputtext"  ignore="ignore"   onclick="popupClick(this,'account,realname','taskNotifierid,taskNotifier','user_select')"  />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">抄送人</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							要求完成时间:
						</label>
					</td>
					<td class="value">
							   <input id="taskFinishdate" name="taskFinishdate" type="text" style="width: 150px" class="Wdate" onClick="WdatePicker()"  datatype="*"  ignore="checked" />    
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">要求完成时间</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							计划表时间:
						</label>
					</td>
					<td class="value">
							   <input id="taskPlanfinishdate" name="taskPlanfinishdate" type="text" style="width: 150px" class="Wdate" onClick="WdatePicker()"  ignore="ignore" />    
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">计划表时间</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							任务说明:
						</label>
					</td>
					<td class="value">
							<textarea style="height:auto;width:95%" class="inputxt" rows="6" id="taskInfo" name="taskInfo"  ignore="ignore" ></textarea>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">任务说明</label>
						</td>
				</tr>
				
				
			</table>
		</t:formvalid>
 </body>
