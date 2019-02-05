<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>考勤信息表</title>
    <t:base type="jquery,easyui,tools,DatePicker"></t:base>
    <script type="text/javascript">
        //编写自定义JS代码
        $(document).ready(function(){
            //上午不请 变化
            $("#morningFree").click(function () {
                if ($("#morningFree").prop("checked")){
                    $("#morningFree").val(1);
                }else{
                    $("#morningFree").val(0);
                }
                checkDateTime();
            });

            //下午不请 变化
            $("#afternoonFree").click(function () {
                if ($("#afternoonFree").prop("checked")){
                    $("#afternoonFree").val(1);
                }else {
                    $("#afternoonFree").val(0);
                }
                checkDateTime();
            });
        });

        //日期改变的时候检查是否是同一天，如果是同一天则改变复选框的可选不可选
        function checkDateTime() {
            var startDate = $("#beginDate").val();
            var endDate = $("#endDate").val();
            var isEq = false;
            if(startDate!=null && endDate!=null && startDate!="" && endDate!=""){
                if(startDate==endDate){
                    isEq = true;
                    if($("#morningFree").prop("checked")&&$("#afternoonFree").prop("checked")){
                        $("#morningFree").attr("checked",false);
                        $("#afternoonFree").attr("checked",false);
                    }else{
                        if($("#morningFree").prop("checked")){
                            $("#afternoonFree").attr("disabled",true);
                        }else if($("#afternoonFree").prop("checked")){
                            $("#morningFree").attr("disabled",true);
                        }else{
                            $("#morningFree").attr("disabled",false);
                            $("#afternoonFree").attr("disabled",false);
                        }
                    }
                }
            }

            if(startDate==null || startDate==""){
                $("#morningFree").attr("disabled",true);
                $("#morningFree").attr("checked",false);
            }else if(!isEq){
                $("#afternoonFree").attr("disabled",false);
            }
            if(endDate==null || endDate==""){
                $("#afternoonFree").attr("disabled",true);
                $("#afternoonFree").attr("checked",false);
            }else if(!isEq){
                $("#afternoonFree").attr("disabled",false);
            }

            countTimeLong();
        }

        //计算时长
        function countTimeLong(){
            var startDate = $("#beginDate").val();
            var endDate = $("#endDate").val();

            if(startDate==null || startDate==""){
                //tip("请先选择开始日期");
                return;
            }

            //如果结束时间为空则不计算时长
            if(endDate == null || endDate==""){
                return;
            }

            //将开始时间由字符串格式转换为日期格式
            var sDate = new Date(Date.parse(startDate.replace(/-/g, "/")));
            var eDate = new Date(Date.parse(endDate.replace(/-/g, "/")));

            var times = parseInt((eDate-sDate)/1000/3600/24)+1; //结束日期减去开始日期后转换成天数
            if($("#morningFree").prop("checked")){
                times-=0.5;
            }
            if ($("#afternoonFree").prop("checked")){
                times-=0.5;
            }
            $("#attenDays").val(times);
            $("#attenStatus").val(1);
        }

        //根据开始，结束时间选择
        function showPickDialog(position){
            if(position==1){
                if($("#endDate").val()==null ||$("#endDate").val()==""){
                    WdatePicker({minDate:'2019-01-01',oncleared:ClearDate})
                }else{
                    WdatePicker({maxDate:$("#endDate").val(),onpicked:checkDateTime,oncleared:ClearDate});
                }
            }else{
                if($("#beginDate").val()==null ||$("#beginDate").val()==""){
                    tip("请先选择开始日期！");
                    return;
                }
                WdatePicker({minDate:$("#beginDate").val(),onpicked:checkDateTime,oncleared:ClearDate});
            }
        }
        //清除日期重置时长为零
        function ClearDate(){
            $("#attenDays").val(0);
            $("#attenStatus").val(0);
        }

        function enableInput() {
            $("#morningFree").attr("disabled",false);
            $("#afternoonFree").attr("disabled",false);
        }

        //检查是否有未完成的记录
        function checkHaveUnfinish() {
            var username = $("#empId").val();
            console.log(username);
            if(username==null || username=='') return;
            $.ajax({
                url:'jformAttendanceController.do?checkHaveUnfinish&username='+username,
                type:"GET",
                dataType:"JSON",
                success:function(res){
                    if(res.success){
                        layer.msg("该用户有未结束的调整信息，请注意！",{icon:6});
                    }else{

                    }
                }
            });
        }
    </script>
</head>
<body>
<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table"
             action="jformAttendanceController.do?doAdd" beforeSubmit="enableInput">
    <input id="id" name="id" type="hidden" value="${jformAttendancePage.id }"/>
    <input id="empId" name="empId" type="hidden" value="${jformAttendancePage.empId }"/>
    <input id="attenStatus" name="attenStatus" type="hidden" value="0"/>
    <table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
        <tr>
            <td align="right">
                <label class="Validform_label">
                    员工名称:
                </label>
            </td>
            <td class="value">
                <input id="empName" readonly name="empName" type="text" style="width: 150px" class="searchbox-inputtext"
                       datatype="*" ignore="checked"
                       onclick="popupClick(this,'account,realname','empId,empName','user_select_single',checkHaveUnfinish)"/>
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
                <t:dictSelect id="attenType" field="attenType" type="list" datatype="n" typeGroupCode="atteType"
                              defaultVal="${jformAttendancePage.attenType}" hasLabel="false"
                              title="考勤类型"></t:dictSelect>
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
                <input id="beginDate" readonly name="beginDate" type="text" style="width: 150px" class="Wdate"
                       onClick="showPickDialog(1)" datatype="*" ignore="checked"/>
                不包上午: <input id="morningFree" name="morningFree" type="checkbox" value="0"/>
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
                <input id="endDate" readonly name="endDate" type="text" style="width: 150px" class="Wdate"
                       onClick="showPickDialog(2)" ignore="ignore"/>
                不包下午: <input id="afternoonFree" name="afternoonFree" type="checkbox" value="0"/>
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
                <input id="attenDays" readonly name="attenDays" type="text" maxlength="16" style="width: 150px" class="inputxt"
                       datatype="*" ignore="ignore"/>
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
            <td class="value">
                <textarea style="height:auto;width:95%" class="inputxt" rows="6" id="memo" name="memo"
                          ignore="ignore"></textarea>
                <span class="Validform_checktip"></span>
                <label class="Validform_label" style="display: none;">备注</label>
            </td>
        </tr>
    </table>
</t:formvalid>
</body>
