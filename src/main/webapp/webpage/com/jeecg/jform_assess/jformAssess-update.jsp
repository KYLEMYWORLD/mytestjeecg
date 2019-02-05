<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>绩效考核</title>
    <t:base type="jquery,easyui,tools,DatePicker"></t:base>
    <script type="text/javascript">
        //编写自定义JS代码
        $(document).ready(function () {
            $("#planIssucc").attr("disabled",true);
            $("#expertScore").change(function () {
                var expertscore = $("#expertScore").val();
                var systemscore = $("#systemScore").val();
                var totalscore = expertscore * 0.7 + systemscore * 0.3;
                $("#totalScore").val(totalscore);
            });
        });
        function enableInput() {
            $("#planIssucc").attr("disabled",false);
            $("#assessStatus").val(1);
        }
    </script>
</head>
<body>
<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table"
             action="jformAssessController.do?doUpdate" beforeSubmit="enableInput">
    <input id="id" name="id" type="hidden" value="${jformAssessPage.id }"/>
    <input id="assessStatus" name="assessStatus" type="hidden" value="${jformAssessPage.assessStatus }"/>

    <table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
        <tr>
            <td align="right">
                <label class="Validform_label">
                    项目名称:
                </label>
            </td>
            <td class="value">
                <input id="projectId" readonly name="projectId" type="text" maxlength="36" style="width: 150px" class="inputxt"
                       ignore="ignore" value='${jformAssessPage.projectId}'/>
                <span class="Validform_checktip"></span>
                <label class="Validform_label" style="display: none;">项目名称</label>
            </td>
            <td align="right">
                <label class="Validform_label">
                    完成状态:
                </label>
            </td>
            <td class="value">
                <t:dictSelect readonly="true" field="planIssucc" id="planIssucc" type="list" datatype="n" typeGroupCode="planSucc"
                              defaultVal='${jformAssessPage.planIssucc}' hasLabel="false" title="完成状态"></t:dictSelect>
                <span class="Validform_checktip"></span>
                <label class="Validform_label" style="display: none;">完成状态</label>
            </td>
        </tr>
        <tr>
            <td align="right">
                <label class="Validform_label">
                    任务名称:
                </label>
            </td>
            <td class="value">
                <input id="planName" readonly name="planName" type="text" maxlength="50" style="width: 150px" class="inputxt"
                       datatype="*" ignore="checked" value='${jformAssessPage.planName}'/>
                <span class="Validform_checktip"></span>
                <label class="Validform_label" style="display: none;">任务名称</label>
            </td>
            <td align="right">
                <label class="Validform_label">
                    负责人:
                </label>
            </td>
            <td class="value">
                <input id="responderId" readonly name="responderId" type="text" maxlength="32" style="width: 150px"
                       class="inputxt" datatype="*" ignore="checked" value='${jformAssessPage.responderId}'/>
                <span class="Validform_checktip"></span>
                <label class="Validform_label" style="display: none;">负责人</label>
            </td>
        </tr>
        <tr>
            <td align="right">
                <label class="Validform_label">
                    开始时间:
                </label>
            </td>
            <td class="value">
                <input id="startDate" readonly name="startDate" type="text" style="width: 150px" class="Wdate"
                       datatype="*" ignore="checked"
                       value='<fmt:formatDate value='${jformAssessPage.startDate}' type="date" pattern="yyyy-MM-dd"/>'/>
                <span class="Validform_checktip"></span>
                <label class="Validform_label" style="display: none;">开始时间</label>
            </td>
            <td align="right">
                <label class="Validform_label">
                    完成时间:
                </label>
            </td>
            <td class="value">
                <input id="finishDate" readonly name="finishDate" type="text" style="width: 150px" class="Wdate"
                       datatype="*" ignore="checked"
                       value='<fmt:formatDate value='${jformAssessPage.finishDate}' type="date" pattern="yyyy-MM-dd"/>'/>
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
                <input id="rfinishDate" readonly name="rfinishDate" type="text" style="width: 150px" class="Wdate"
                       datatype="*" ignore="checked"
                       value='<fmt:formatDate value='${jformAssessPage.rfinishDate}' type="date" pattern="yyyy-MM-dd"/>'/>
                <span class="Validform_checktip"></span>
                <label class="Validform_label" style="display: none;">实际完成时间</label>
            </td>
            <td align="right">
                <label class="Validform_label">
                    进度评分:
                </label>
            </td>
            <td class="value">
                <input id="systemScore" name="systemScore" type="text" maxlength="32" style="width: 150px"
                       class="inputxt" datatype="*" ignore="checked" value='${jformAssessPage.systemScore}'/>
                <span class="Validform_checktip"></span>
                <label class="Validform_label" style="display: none;">进度评分</label>
            </td>
        </tr>
        <tr>
            <td align="right">
                <label class="Validform_label">
                    质量评分:
                </label>
            </td>
            <td class="value">
                <input id="expertScore" name="expertScore" type="text" maxlength="32" style="width: 150px"
                       class="inputxt" datatype="*"  ignore="checked"  value='${jformAssessPage.expertScore}'/>
                <span class="Validform_checktip"></span>
                <label class="Validform_label" style="display: none;">质量评分</label>
            </td>
            <td align="right">
                <label class="Validform_label">
                    总评分:
                </label>
            </td>
            <td class="value">
                <input id="totalScore" name="totalScore" type="text" maxlength="32" style="width: 150px" class="inputxt"
                       datatype="*"  ignore="checked"  value='${jformAssessPage.totalScore}'/>
                <span class="Validform_checktip"></span>
                <label class="Validform_label" style="display: none;">总评分</label>
            </td>
        </tr>

    </table>
</t:formvalid>
</body>
