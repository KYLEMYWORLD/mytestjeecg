<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>考勤统计表</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="jformAttenstatisticController.do?doAdd" >
					<input id="id" name="id" type="hidden" value="${jformAttenstatisticPage.id }"/>
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							员工名称:
						</label>
					</td>
					<td class="value">
							<input id="empName" name="empName" type="text" style="width: 150px" class="searchbox-inputtext"  datatype="*"  ignore="checked"   onclick="popupClick(this,'id,realname','empId,empName','user_select_single')"  />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">员工名称</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							在岗天数:
						</label>
					</td>
					<td class="value">
					     	 <input id="dutyDays" name="dutyDays" type="text" maxlength="16" style="width: 150px" class="inputxt"  datatype="/^(-?\d+)(\.\d+)?$/"  ignore="checked" />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">在岗天数</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							出差天数:
						</label>
					</td>
					<td class="value">
					     	 <input id="gooutDays" name="gooutDays" type="text" maxlength="16" style="width: 150px" class="inputxt"  datatype="/^(-?\d+)(\.\d+)?$/"  ignore="checked" />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">出差天数</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							请假天数:
						</label>
					</td>
					<td class="value">
					     	 <input id="leaveDays" name="leaveDays" type="text" maxlength="16" style="width: 150px" class="inputxt"  datatype="/^(-?\d+)(\.\d+)?$/"  ignore="checked" />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">请假天数</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							调休天数:
						</label>
					</td>
					<td class="value">
					     	 <input id="offworkDays" name="offworkDays" type="text" maxlength="16" style="width: 150px" class="inputxt"  datatype="/^(-?\d+)(\.\d+)?$/"  ignore="checked" />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">调休天数</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							统计年份:
						</label>
					</td>
					<td class="value">
							  <t:dictSelect id="dateMouth" field="dateMouth" type="list"  datatype="n"  typeGroupCode=""  defaultVal="${jformAttenstatisticPage.dateMouth}" hasLabel="false"  title="统计年份" ></t:dictSelect>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">统计年份</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							统计年份:
						</label>
					</td>
					<td class="value">
							   <input id="dateYear" name="dateYear" type="text" style="width: 150px" class="Wdate" onClick="WdatePicker()"  datatype="*"  ignore="checked" />    
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">统计年份</label>
						</td>
				</tr>
				
				
			</table>
		</t:formvalid>
 </body>
