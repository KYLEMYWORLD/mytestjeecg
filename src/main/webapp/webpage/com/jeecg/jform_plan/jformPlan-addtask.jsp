<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>项目任务计划表</title>
    <t:base type="jquery,easyui,tools,DatePicker"></t:base>
    <style type="text/css">
        .combo_self {
            height: 22px !important;
            width: 150px !important;
        }

        .layout-header .btn {
            margin: 0;
            float: none !important;
        }

        .btn-default {
            height: 35px;
            line-height: 35px;
            font-size: 14px;
        }
    </style>

    <script type="text/javascript">
        $(function () {
            $(".combo").removeClass("combo").addClass("combo combo_self");
            $(".combo").each(function () {
                $(this).parent().css("line-height", "0px");
            });
        });

        /**树形列表数据转换**/
        function convertTreeData(rows, textField) {
            for (var i = 0; i < rows.length; i++) {
                var row = rows[i];
                row.text = row[textField];
                if (row.children) {
                    row.state = "open";
                    convertTreeData(row.children, textField);
                }
            }
        }

        /**树形列表加入子元素**/
        function joinTreeChildren(arr1, arr2) {
            for (var i = 0; i < arr1.length; i++) {
                var row1 = arr1[i];
                for (var j = 0; j < arr2.length; j++) {
                    if (row1.id == arr2[j].id) {
                        var children = arr2[j].children;
                        if (children) {
                            row1.children = children;
                        }

                    }
                }
            }
        }

        //编写自定义JS代码
        $(document).ready(function () {
            $("#parent_name").attr("disabled",true);
            $("#planIssucc").attr("disabled", true);//完成状态
            $("#planStatus").attr("disabled", true);//任务状态
            $("#planIsalert").attr("disabled", true);//预警状态
            $("#planResponder").attr("disabled", true);

            $("#planLevel").change(function(){
                $("#planId").val("");
                $("#parent_name").val("");
                if( $(this).val()=="1"){
                    //一级任务不用选择
                    $("#parent_name").attr("disabled",true);
                    $("#parent_name").removeClass("searchbox-inputtext Validform_error");
                    $("#parent_name").removeAttr("nullmsg");
                    $("#parent_name").removeAttr("ignore");
                    $("#parent_name").removeAttr("datatype");
                    $("#contentCheckTip").removeClass("Validform_wrong");
                    $("#contentCheckTip").html('');
                }else{
                    //二级，三级任务
                    $("#parent_name").attr("disabled",false);
                    $("#parent_name").addClass("searchbox-inputtext Validform_error")
                    $("#parent_name").attr("nullmsg","选择父任务！");
                    $("#parent_name").attr("ignore","checked");
                    $("#parent_name").attr("datatype","*");
                    $("#contentCheckTip").addClass("Validform_wrong");
                    $("#contentCheckTip").html("选择父任务！");
                }
            });
        });

        /**
         * 选择父任务
         * @param pobj
         */
        function selectParenPlanID(pobj){
            if ($("#planLevel").val()==1){
                tip("一级任务不需要父任务。");
                return;
            }
            var projectid = $("#projectId").val();
            if(projectid==undefined || projectid == null || projectid==''){
                tip("请先选择分配的任务！");
                return;
            }
            var level = $("#planLevel").val();
            level--;//二级任务查询一级父任务   三级任务查询二级父任务
            //根据任务等级，项目来查找对应的任务
            popupClick(pobj,'id,plan_name','planId,parent_name','jform_plan_select','&p_a='+level+'&p_b='+projectid);
        }

        function enabelInput() {
            $("#parent_name").attr("disabled",false);
            $("#planIssucc").attr("disabled", false);//完成状态
            $("#planStatus").attr("disabled", false);//任务状态
            $("#planIsalert").attr("disabled", false);//预警状态
            $("#planResponder").attr("disabled", false);
        }
    </script>
</head>
<body>


<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="jformPlanController.do?doAdd" beforeSubmit="enabelInput">
    <input id="id" name="id" type="hidden" value="${jformPlanPage.id }"/>
    <input id="planId" name="planId" type="hidden" value=""/>
    <input id="taskId" name="taskId" type="hidden" value=""/>
    <input id="planResponderid" name="planResponderid" type="hidden" value=""/>
    <table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
        <tr>
            <td align="right">
                <label class="Validform_label">
                    选择分配任务:
                </label>
            </td>
            <td class="value">
                <input id="taskName" readonly name="taskName" type="text" style="width: 150px" class="searchbox-inputtext"
                       datatype="*" ignore="checked"
                       onclick="popupClick(this,'id,task_name,task_name,task_projectid,project_name,responder,responderid,task_finishdate,task_info','taskId,planName,taskName,projectId,projectname,planResponder,planResponderid,finishDate,planInfo','activeTask_select')"/>
                <span class="Validform_checktip"></span>
                <label class="Validform_label" style="display: none;">分配任务名称</label>
            </td>
            <td align="right">
                <label class="Validform_label">
                    项目名称:
                </label>
            </td>
            <td class="value">
                <input id="projectId" name="projectId" value="" type="hidden"/>
                <input id="projectname" name="projectname" type="text" style="width: 150px" class="searchbox-inputtext"
                       datatype="*" ignore="checked" readonly/>
                <span class="Validform_checktip"></span>
                <label class="Validform_label" style="display: none;">项目名称</label>
            </td>
        </tr>
        <tr>
            <td align="right">
                <label class="Validform_label">
                    任务名称:
                </label>
            </td>
            <td class="value">
                <input id="planName" name="planName" type="text" maxlength="100" class="inputxt"
                       datatype="*" ignore="checked"/>
                <span class="Validform_checktip"></span>
                <label class="Validform_label" style="display: none;">任务名称</label>
            </td>
            <td align="right">
                <label class="Validform_label">
                    任务等级:
                </label>
            </td>
            <td class="value">
                <t:dictSelect field="planLevel" id="planLevel" type="list" datatype="n" typeGroupCode="planLevel"
                              defaultVal="1" hasLabel="false" title="任务等级"></t:dictSelect>
                <span class="Validform_checktip"></span>
                <label class="Validform_label" style="display: none;">任务等级</label>
            </td>
        </tr>
        <tr id="hiddenspace" display="none">
            <td align="right">
                <label class="Validform_label">
                    父任务:
                </label>
            </td>
            <td class="value" colspan="3">
                <input id="parent_name" readonly name="parent_name" type="text" style="width: 150px"
                       class="searchbox-inputtext"
                       onclick="selectParenPlanID(this)"/>
                <span class="Validform_checktip" id="contentCheckTip" ></span>
                <label class="Validform_label" style="display: none;">父任务</label>
            </td>
        </tr>
        <tr>
            <td align="right">
                <label class="Validform_label">
                    任务开始时间:
                </label>
            </td>
            <td class="value">
                <input id="startDate" readonly name="startDate" type="text" style="width: 150px" class="Wdate"
                       onClick="WdatePicker()"  datatype="*" ignore="checked"/>
                <span class="Validform_checktip"></span>
                <label class="Validform_label" style="display: none;">任务开始时间</label>
            </td>
            <td align="right">
                <label class="Validform_label">
                    任务完成时间:
                </label>
            </td>
            <td class="value">
                <input id="finishDate" readonly name="finishDate" type="text" style="width: 150px" class="Wdate"
                       onClick="WdatePicker()"  datatype="*" ignore="checked"/>
                <span class="Validform_checktip"></span>
                <label class="Validform_label" style="display: none;">任务完成时间</label>
            </td>
        </tr>
        <tr>
            <td align="right">
                <label class="Validform_label">
                    负责人:
                </label>
            </td>
            <td class="value">
                <input id="planResponder" readonly name="planResponder" type="text" style="width: 150px"
                       class="searchbox-inputtext"  datatype="*" ignore="checked"
                       onclick="popupClick(this,'account,realname','planResponderid,planResponder','user_select')"/>
                <span class="Validform_checktip"></span>
                <label class="Validform_label" style="display: none;">负责人</label>
            </td>
            <td align="right">
                <label class="Validform_label">
                    完成状态:
                </label>
            </td>
            <td class="value">
                <t:dictSelect id="planIssucc" field="planIssucc" type="list" datatype="n" typeGroupCode="planSucc"
                              defaultVal="0" hasLabel="false" title="完成状态"></t:dictSelect>
                <span class="Validform_checktip"></span>
                <label class="Validform_label" style="display: none;">完成状态</label>
            </td>
        </tr>
        <tr>
            <td align="right">
                <label class="Validform_label">
                    任务状态:
                </label>
            </td>
            <td class="value">
                <t:dictSelect id="planStatus" field="planStatus" type="list" datatype="n" typeGroupCode="planStatus"
                              defaultVal="0" hasLabel="false" title="任务状态"></t:dictSelect>
                <span class="Validform_checktip"></span>
                <label class="Validform_label" style="display: none;">任务状态</label>
            </td>
            <td align="right">
                <label class="Validform_label">
                    预警状态:
                </label>
            </td>
            <td class="value">
                <t:dictSelect id="planIsalert" field="planIsalert" type="list" datatype="n" typeGroupCode="planAlert"
                              defaultVal="1" hasLabel="false" title="预警状态"></t:dictSelect>
                <span class="Validform_checktip"></span>
                <label class="Validform_label" style="display: none;">预警状态</label>
            </td>
        </tr>
        <tr>
            <td align="right">
                <label class="Validform_label">
                    任务说明:
                </label>
            </td>
            <td class="value" colspan="3">
                <textarea style="height:auto;width:95%" class="inputxt" rows="6" id="planInfo" name="planInfo"
                          ignore="ignore" maxlength="300"></textarea>
                <span class="Validform_checktip"></span>
                <label class="Validform_label" style="display: none;">任务说明</label>
            </td>
        </tr>
    </table>
</t:formvalid>
</body>
