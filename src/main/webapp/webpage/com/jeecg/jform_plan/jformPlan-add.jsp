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
    </script>
    <script type="text/javascript">
        //编写自定义JS代码
        $(document).ready(function () {
            //debugger;
            var win = frameElement.api.opener;
            var currRow = win.getDataGrid().treegrid('getSelected');
            // var listRows = win.getDataGrid().treegrid('getData');
            //有选择行则取需要的数据回填
            if (currRow != null) {
                if (currRow.planLevel == 1) {
                    $("#planLevel").val(2);//二级
                    $("#planResponder").attr("onclick", "popupClick(this,'account,realname','planResponderid,planResponder','user_select')");
                } else if (currRow.planLevel == 2) {
                    $("#planLevel").val(3);//三级
                    $("#planResponder").attr("onclick", "popupClick(this,'account,realname','planResponderid,planResponder','user_select_single')");
                }
                //如果选中了任务添加，则设置父任务信息
                $("#parPlanName").val(currRow.planName);
                $("#planId").val(currRow.id);
                $("#projectId").val(currRow.projectId);
                $("#projectname").val(currRow.projectname);
                setNeedInput(true);
                setProjectName();
            } else {
                //没有选择东西则是一级
                $("#planLevel").val(1);
                setNeedInput(false);
            }

            //等级改变，则负责人选择单数和多数改变
            $("#planLevel").change(function () {
                $("#planId").val("");//父级任务
                $("#parPlanName").val("");//父级任务名称
                $("#planResponderid").val("");//负责人ID
                $("#planResponder").val("");//负责人
                if($(this).val()==1){
                    //是一级任务则不需要父ID
                    setNeedInput(false);
                }else if($(this).val()==2){
                    setNeedInput(true);
                    $("#planResponder").attr("onclick", "popupClick(this,'account,realname','planResponderid,planResponder','user_select')");
                }else if($(this).val()==3){
                    setNeedInput(true);
                    $("#planResponder").attr("onclick", "popupClick(this,'account,realname','planResponderid,planResponder','user_select_single')");
                }
            });

            //值改变这重新选择上级任务等信息
            $("#projectId").change(function(){

                $("#planId").val("");
                $("#parPlanName").val("");
            });

            $("#planIssucc").attr("disabled",true);
            $("#planStatus").attr("disabled",true);
            $("#planIsalert").attr("disabled",true);

        });

        //form提交前需要将Disabled的控件放开，要不要不会传值到后台
        function enableInput() {
            $("#planIssucc").attr("disabled",false);
            $("#planStatus").attr("disabled",false);
            $("#planIsalert").attr("disabled",false);
        }

        //设置父任务输入框是否需要输入校验
        function setNeedInput(needinput){
            if(needinput){
                //二级三级任务
                $("#parPlanName").attr("nullmsg","选择父任务！");
                $("#parPlanName").attr("ignore","checked");
                $("#parPlanName").attr("datatype","*");
            }else{
                //一级任务
                $("#parPlanName").removeClass("Validform_error");
                $("#parPlanName").removeAttr("nullmsg");
                $("#parPlanName").removeAttr("ignore");
                $("#parPlanName").removeAttr("datatype");
                $("#contentCheckTip").removeClass("Validform_wrong");
                $("#contentCheckTip").html('');
                $("#planResponder").attr("onclick", "popupClick(this,'account,realname','planResponderid,planResponder','user_select')");
            }
        }

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
                tip("请先选择项目！");
                return;
            }
            var level = $("#planLevel").val();
            level--;//二级任务查询一级父任务   三级任务查询二级父任务
            popupClick(pobj,'id,plan_name','planId,parPlanName','jform_plan_select','&p_a='+level+'&p_b='+projectid);
        }

        function setProjectName() {
            var id = $("#projectId").val();
            if($("#projectname").val()==null ||$("#projectname").val()=='' ){
                $.ajax({
                    url:'jformProjectController.do?getProjectName&id='+id,
                    type:'POST',
                    dataType:'JSON',
                    success:function(res){
                        if(res.success){
                            $("#projectname").val(res.msg);
                        }
                    }
                });
            }
        }
    </script>
</head>
<body>
<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="jformPlanController.do?doAdd" beforeSubmit="enableInput">
    <input id="id" name="id" type="hidden" value="${jformPlanPage.id }"/>
    <input id="planId" name="planId" type="hidden" value="0"/>
    <input id="planResponderid" name="planResponderid" type="hidden" value="" datatype="*" ignore="checked"/>

    <table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
        <tr>
            <td align="right">
                <label class="Validform_label">
                    项目名称:
                </label>
            </td>
            <td class="value">
                <input id="projectId" name="projectId" value="" type="hidden"/>
                <input id="projectname" name="projectname" type="text" style="width: 150px" class="searchbox-inputtext"
                       onclick="popupClick(this,'id,project_name','projectId,projectname','project4plan')"
                       datatype="*" ignore="checked" readonly/>
                <span class="Validform_checktip"></span>
                <label class="Validform_label" style="display: none;">项目名称</label>
            </td>
            <td align="right">
                <label class="Validform_label">
                    任务等级:
                </label>
            </td>
            <td class="value">
                <t:dictSelect field="planLevel" id="planLevel" type="list" datatype="n" typeGroupCode="planLevel"
                              defaultVal="${jformPlanPage.planLevel}" hasLabel="false" title="任务等级"></t:dictSelect>
                <span class="Validform_checktip"></span>
                <label class="Validform_label" style="display: none;">任务等级</label>
            </td>

        </tr>
        <tr>
            <td align="right">
                <label class="Validform_label">
                    上级任务:
                </label>
            </td>
            <td class="value">
                <input id="parPlanName" readonly name="parPlanName" type="text" style="width: 150px"
                       class="searchbox-inputtext"
                       onclick="selectParenPlanID(this)"/>
                <span class="Validform_checktip"></span>
                <label id="contentCheckTip" class="Validform_label" style="display: none;">上级任务</label>
            </td>
            <td align="right">
                <label class="Validform_label">
                    任务名称:
                </label>
            </td>
            <td class="value">
                <input id="planName" name="planName" type="text" maxlength="50" style="width: 150px" class="inputxt"
                       datatype="*" ignore="checked"/>
                <span class="Validform_checktip"></span>
                <label class="Validform_label" style="display: none;">任务名称</label>
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
                       onClick="WdatePicker()" datatype="*" ignore="checked"/>
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
                       onClick="WdatePicker()" datatype="*" ignore="checked"/>
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
                       class="searchbox-inputtext" datatype="*" ignore="checked"
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
                <t:dictSelect readonly="true" field="planIssucc" id="planIssucc" type="list" datatype="n" typeGroupCode="planSucc"
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
                <t:dictSelect readonly="true" field="planStatus" id="planStatus" type="list" datatype="n" typeGroupCode="planStatus"
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
                <t:dictSelect readonly="true" id="planIsalert" field="planIsalert" type="list" datatype="n" typeGroupCode="planAlert"
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
<script type="text/javascript">

    // data-options="panelHeight:'220',
    // url: 'jformPlanController.do?datagrid&field=id,planName',
    // 	 loadFilter: function(data) {
    //  var rows = data.rows || data;
    //  var win = frameElement.api.opener;
    //  var listRows = win.getDataGrid().treegrid('getData');
    //  joinTreeChildren(rows, listRows);
    //  convertTreeData(rows, 'planName');
    //  return rows;
    // },
    // onSelect:function(node){
    //  $('#planId').val(node.id);
    // },
    // onLoadSuccess: function() {
    //  var win = frameElement.api.opener;
    //  var currRow = win.getDataGrid().treegrid('getSelected');
    <%--if(!'${jformPlanPage.id}') {&ndash;%&gt;--%>
    //增加时，选择当前父菜单
    // if(currRow) {
    //  $('#planId').combotree('setValue', currRow.id);
    // }
    // }else {
    //编辑时，选择当前父菜单
    // if(currRow) {
    //  $('#planId').combotree('setValue', currRow.planId);
    // }
    // }
    // }"

    /*
    *
    <input id="planIssucc" name="planIssucc" type="hidden" value="0"/>
    <input id="planIsalert" name="planIsalert" type="hidden" value="1"/>
    <input id="planStatus" name="planStatus" type="hidden" value="0"/>
    * */

</script>
</body>
