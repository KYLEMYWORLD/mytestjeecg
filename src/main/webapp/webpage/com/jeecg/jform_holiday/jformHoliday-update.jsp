<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>休息日信息表</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
		<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="jformHolidayController.do?doUpdate" >
					<input id="id" name="id" type="hidden" value="${jformHolidayPage.id }"/>
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
					<tr>
						<td align="right">
							<label class="Validform_label">
								节假日:
							</label>
						</td>
						<td class="value">
									  <input id="holiday" name="holiday" type="text" style="width: 150px"  class="Wdate" onClick="WdatePicker()"  datatype="*"  ignore="checked" value='<fmt:formatDate value='${jformHolidayPage.holiday}' type="date" pattern="yyyy-MM-dd"/>'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">节假日</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								时长:
							</label>
						</td>
						<td class="value">
									<t:dictSelect id="times" field="times" type="list"  datatype="/^(-?\d+)(\.\d+)?$/"  typeGroupCode="holidayT"   defaultVal="${jformHolidayPage.times}" hasLabel="false"  title="时长" ></t:dictSelect>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">时长</label>
						</td>
					</tr>
				
			</table>
		</t:formvalid>
 </body>
