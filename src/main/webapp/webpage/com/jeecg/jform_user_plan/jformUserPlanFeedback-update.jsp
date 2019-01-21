<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>个人计划安排</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
		<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="jformUserPlanController.do?doTaskFinish">
			<input id="id" name="id" type="hidden" value="${jformUserPlanPage.id }"/>

		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
					<tr>
						<td align="right">
							<label class="Validform_label">
								任务名称:
							</label>
						</td>
						<td class="value">
						    <input id="planName" disabled name="planName" type="text" maxlength="50" style="width: 150px" class="inputxt"  ignore="ignore"  value='${jformUserPlanPage.planName}'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">任务名称</label>
						</td>
						<td align="right">
							<label class="Validform_label">
								任务等级:
							</label>
						</td>
						<td class="value">
							<t:dictSelect field="planLevel" readonly="readonly" type="list"  datatype="n"  typeGroupCode="userPlanT"   defaultVal="${jformUserPlanPage.planLevel}" hasLabel="false"  title="任务等级" ></t:dictSelect>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">任务等级</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								任务开始时间:
							</label>
						</td>
						<td class="value">
							<input id="startDate" disabled readonly name="startDate" type="text" style="width: 150px"  class="Wdate" onClick="WdatePicker()"  ignore="ignore" value='<fmt:formatDate value='${jformUserPlanPage.startDate}' type="date" pattern="yyyy-MM-dd"/>'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">任务开始时间</label>
						</td>
						<td align="right">
							<label class="Validform_label">
								任务完成时间:
							</label>
						</td>
						<td class="value">
							<input id="finishDate" disabled name="finishDate" type="text" style="width: 150px"  class="Wdate" onClick="WdatePicker()"  ignore="ignore" value='<fmt:formatDate value='${jformUserPlanPage.finishDate}' type="date" pattern="yyyy-MM-dd"/>'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">任务完成时间</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								实际完成时间:
							</label>
						</td>
						<td class="value">
							<input id="realfinishDate" readonly name="realfinishDate" type="text" style="width: 150px"  class="Wdate" onClick="WdatePicker({onpicked:function(){onRealFinishDateSelect();}})" datatype="*"  ignore="checked" value='<fmt:formatDate value='${jformUserPlanPage.realfinishDate}' type="date" pattern="yyyy-MM-dd"/>'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">实际完成时间</label>
						</td>
						<td align="right">
							<label class="Validform_label">
								完成状态:
							</label>
						</td>
						<td class="value">
							<t:dictSelect field="planIssucc" id="planIssucc" readonly="readonly" type="list"  datatype="n"  typeGroupCode="planSucc"   defaultVal="${jformUserPlanPage.planIssucc}" hasLabel="false"  title="完成状态" ></t:dictSelect>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">任务等级</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								完成情况反馈:
							</label>
						</td>
						<td class="value"  colspan="3" >
							<textarea id="planLatemsg" style="height:auto;width:95%;" class="inputxt" rows="6" name="planLatemsg"  ignore="ignore" >${jformUserPlanPage.planLatemsg}</textarea>
							<span class="Validform_checktip"></span>
							<label id="planLatemsgTip" class="Validform_label" style="display: none;">	完成情况反馈</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								任务说明:
							</label>
						</td>
						<td class="value"  colspan="3" >
							<textarea id="planInfo" disabled style="height:auto;width:95%;" class="inputxt" rows="6" name="planInfo"  ignore="ignore" >${jformUserPlanPage.planInfo}</textarea>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">任务说明</label>
						</td>
					</tr>
			</table>
		</t:formvalid>
 </body>
<script type="text/javascript">
	function onRealFinishDateSelect() {
		debugger;
		var realfinishDate = document.getElementById("realfinishDate").value;
		var finishDate = document.getElementById("finishDate").value;
		if(realfinishDate > finishDate){
			$("#planLatemsg").addClass("searchbox-inputtext Validform_error")
			$("#planLatemsg").attr("nullmsg","延时完成原因！");
			$("#planLatemsg").attr("ignore","checked");
			$("#planLatemsg").attr("datatype","*");
			$("#planLatemsgTip").addClass("Validform_wrong");
			$("#planLatemsgTip").html("延时完成原因！");
			$("#planIssucc").val(2);//延时完成
		}else{
			$("#planLatemsg").removeClass("searchbox-inputtext Validform_error");
			$("#planLatemsg").removeAttr("nullmsg");
			$("#planLatemsg").removeAttr("ignore");
			$("#planLatemsg").removeAttr("datatype");
			$("#planLatemsgTip").removeClass("Validform_wrong");
			$("#planLatemsgTip").html('');
			$("#planIssucc").val(1);//正常完成
		}
	}
</script>