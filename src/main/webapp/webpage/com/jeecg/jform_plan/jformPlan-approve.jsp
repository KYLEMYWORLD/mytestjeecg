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
		$('input:radio').click(function(){
			if( $(this).val()=="1"){
				$("#content").removeClass("searchbox-inputtext Validform_error");
				$("#content").removeAttr("nullmsg");
				$("#content").removeAttr("ignore");
				$("#content").removeAttr("datatype");
				$("#contentCheckTip").removeClass("Validform_wrong");
				$("#contentCheckTip").html('');
			}else{
				//审批驳回
				$("#content").addClass("searchbox-inputtext Validform_error")
				$("#content").attr("nullmsg","填写审批意见！");
				$("#content").attr("ignore","checked");
				$("#content").attr("datatype","*");
				$("#contentCheckTip").addClass("Validform_wrong");
				$("#contentCheckTip").html("填写审批意见！");
			}
		});

	});
  </script>
 </head>
 <body>
		<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="jformPlanController.do?doApprove" >
			<input id="id" name="id" type="hidden" value="${jformPlanPage.id }">
		<table style="width:320px;" cellpadding="0" cellspacing="1" class="formtable">
					<tr>
						<td align="right">
							<label class="Validform_label">
								审批结果:
							</label>
						</td>
						<td class="value">
							<t:dictSelect id="approvetype" field="approvetype" type="radio" typeGroupCode="approveTy" defaultVal="1" hasLabel="false"  title="审批结果:" ></t:dictSelect>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">审批结果</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								审核意见:
							</label>
						</td>
						<td class="value">
							<textarea id="content" name="content" rows="6" cols="36"></textarea>
							<span id="contentCheckTip" class="Validform_checktip"></span>
						</td>
					</tr>
			</table>
		</t:formvalid>
 </body>
