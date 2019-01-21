<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>测试表</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="jformTestController.do?doAdd" >
					<input id="id" name="id" type="hidden" value="${jformTestPage.id }"/>
					<input id="createName" name="createName" type="hidden" value="${jformTestPage.createName }"/>
					<input id="createBy" name="createBy" type="hidden" value="${jformTestPage.createBy }"/>
					<input id="createDate" name="createDate" type="hidden" value="${jformTestPage.createDate }"/>
					<input id="updateName" name="updateName" type="hidden" value="${jformTestPage.updateName }"/>
					<input id="updateBy" name="updateBy" type="hidden" value="${jformTestPage.updateBy }"/>
					<input id="updateDate" name="updateDate" type="hidden" value="${jformTestPage.updateDate }"/>
					<input id="sysOrgCode" name="sysOrgCode" type="hidden" value="${jformTestPage.sysOrgCode }"/>
					<input id="sysCompanyCode" name="sysCompanyCode" type="hidden" value="${jformTestPage.sysCompanyCode }"/>
					<input id="bpmStatus" name="bpmStatus" type="hidden" value="${jformTestPage.bpmStatus }"/>
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							用户ID:
						</label>
					</td>
					<td class="value">
							<input id="userid" name="userid" type="text" style="width: 150px" class="searchbox-inputtext"  datatype="*"  ignore="checked"   onclick="popupClick(this,'id','userid','user_select')"  />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">用户ID</label>
						</td>
				</tr>
				
				
			</table>
		</t:formvalid>
 </body>
