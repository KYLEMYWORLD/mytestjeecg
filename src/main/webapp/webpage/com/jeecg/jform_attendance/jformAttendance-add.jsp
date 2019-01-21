<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>考勤信息表</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="jformAttendanceController.do?doAdd" >
					<input id="id" name="id" type="hidden" value="${jformAttendancePage.id }"/>
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
							考勤类型:
						</label>
					</td>
					<td class="value">
							  <t:dictSelect id="attenType" field="attenType" type="list"  datatype="n"  typeGroupCode="atteType"  defaultVal="${jformAttendancePage.attenType}" hasLabel="false"  title="考勤类型" ></t:dictSelect>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">考勤类型</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							开始时间:
						</label>
					</td>
					<td class="value">
							   <input id="beginDate" name="beginDate" type="text" style="width: 150px" class="Wdate" onClick="WdatePicker()"  datatype="*"  ignore="checked" />    
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
							   <input id="endDate" name="endDate" type="text" style="width: 150px" class="Wdate" onClick="WdatePicker()"  ignore="ignore" />    
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">结束时间</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							时长:
						</label>
					</td>
					<td class="value">
					     	 <input id="attenDays" name="attenDays" type="text" maxlength="16" style="width: 150px" class="inputxt"  datatype="n" ignore="ignore" />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">时长</label>
						</td>
				</tr>
				
				
				<tr>
					<td align="right">
						<label class="Validform_label">
							备注:
						</label>
					</td>
					<td class="value" >
						  	 <textarea style="height:auto;width:95%" class="inputxt" rows="6" id="memo" name="memo"  ignore="ignore" ></textarea>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">备注</label>
						</td>
					</tr>
			</table>
		</t:formvalid>
 </body>
