<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>审批</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript">
  //编写自定义JS代码
	$(document).ready(function(){
	    var date = new Date();
        $("#analyYear").val(date.getFullYear());
        $("#analyMonth").val(date.getMonth()+1);
	});
  </script>
 </head>
 <body>
		<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="jformAssessanalyController.do?doAnalyall" >
		<table style="width:320px;" cellpadding="0" cellspacing="1" class="formtable">
					<tr>
						<td align="right">
							<label class="Validform_label">
								统计年份:
							</label>
						</td>
						<td class="value">
							<input id="analyYear" name="analyYear" class="Wdate" onClick="WdatePicker({dateFmt:'yyyy',minDate:'2019-01-01'})"
								   type="text" maxlength="32" style="width: 150px" class="inputxt"    datatype="*"  ignore="checked"  />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">统计年份</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								统计月份:
							</label>
						</td>
						<td class="value">
							<input id="analyMonth" name="analyMonth" class="Wdate" onClick="WdatePicker({dateFmt:'MM'})"
								   type="text" maxlength="32" style="width: 150px" class="inputxt"    datatype="*"  ignore="checked"  />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">统计月份</label>
						</td>
					</tr>
			</table>
		</t:formvalid>
 </body>
