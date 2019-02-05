<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>绩效统计</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
		<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="jformAssessanalyController.do?doUpdate" >
					  <input id="id" name="id" type="hidden" value="${jformAssessanalyPage.id }"/>
					  <input id="createName" name="createName" type="hidden" value="${jformAssessanalyPage.createName }"/>
					  <input id="createBy" name="createBy" type="hidden" value="${jformAssessanalyPage.createBy }"/>
					  <input id="createDate" name="createDate" type="hidden" value="${jformAssessanalyPage.createDate }"/>
					  <input id="updateName" name="updateName" type="hidden" value="${jformAssessanalyPage.updateName }"/>
					  <input id="updateBy" name="updateBy" type="hidden" value="${jformAssessanalyPage.updateBy }"/>
					  <input id="updateDate" name="updateDate" type="hidden" value="${jformAssessanalyPage.updateDate }"/>
					  <input id="sysOrgCode" name="sysOrgCode" type="hidden" value="${jformAssessanalyPage.sysOrgCode }"/>
					  <input id="sysCompanyCode" name="sysCompanyCode" type="hidden" value="${jformAssessanalyPage.sysCompanyCode }"/>
					  <input id="bpmStatus" name="bpmStatus" type="hidden" value="${jformAssessanalyPage.bpmStatus }"/>
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
					<tr>
						<td align="right">
							<label class="Validform_label">
								用户名称:
							</label>
						</td>
						<td class="value">
						    <input id="empId" name="empId" type="text" maxlength="32" style="width: 150px" class="inputxt"  ignore="ignore"  value='${jformAssessanalyPage.empId}'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">用户名称</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								统计年份:
							</label>
						</td>
						<td class="value">
						    <input id="analyYear" name="analyYear" type="text" maxlength="32" style="width: 150px" class="inputxt"  datatype="n"  ignore="ignore"  value='${jformAssessanalyPage.analyYear}'/>
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
						    <input id="analyMonth" name="analyMonth" type="text" maxlength="32" style="width: 150px" class="inputxt"  datatype="n"  ignore="ignore"  value='${jformAssessanalyPage.analyMonth}'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">统计月份</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								任务总量:
							</label>
						</td>
						<td class="value">
						    <input id="totalCount" name="totalCount" type="text" maxlength="32" style="width: 150px" class="inputxt"  datatype="n"  ignore="ignore"  value='${jformAssessanalyPage.totalCount}'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">任务总量</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								任务总评分:
							</label>
						</td>
						<td class="value">
						    <input id="totalScore" name="totalScore" type="text" maxlength="32" style="width: 150px" class="inputxt"  ignore="ignore"  value='${jformAssessanalyPage.totalScore}'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">任务总评分</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								计划安排数:
							</label>
						</td>
						<td class="value">
						    <input id="planCount" name="planCount" type="text" maxlength="32" style="width: 150px" class="inputxt"  datatype="n"  ignore="ignore"  value='${jformAssessanalyPage.planCount}'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">计划安排数</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								计划总评分:
							</label>
						</td>
						<td class="value">
						    <input id="planScore" name="planScore" type="text" maxlength="32" style="width: 150px" class="inputxt"  ignore="ignore"  value='${jformAssessanalyPage.planScore}'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">计划总评分</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								个人任务数:
							</label>
						</td>
						<td class="value">
						    <input id="personCount" name="personCount" type="text" maxlength="32" style="width: 150px" class="inputxt"  datatype="n"  ignore="ignore"  value='${jformAssessanalyPage.personCount}'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">个人任务数</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								个人总评分 :
							</label>
						</td>
						<td class="value">
						    <input id="personScore" name="personScore" type="text" maxlength="32" style="width: 150px" class="inputxt"  ignore="ignore"  value='${jformAssessanalyPage.personScore}'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">个人总评分 </label>
						</td>
					</tr>
				
			</table>
		</t:formvalid>
 </body>
