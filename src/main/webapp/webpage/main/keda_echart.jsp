<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!--360浏览器优先以webkit内核解析-->
    <title>项目管理系统</title>
    <link rel="shortcut icon" href="images/favicon.ico">
    <link href="plug-in/hplus/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="plug-in/hplus/css/font-awesome.css?v=4.4.0" rel="stylesheet">
    <link href="plug-in/hplus/css/animate.css" rel="stylesheet">
    <link href="plug-in/hplus/css/style.css?v=4.1.0" rel="stylesheet">
    <link rel="stylesheet" href="plug-in/themes/fineui/main/iconfont.css">
    <script src="plug-in/laydate/laydate.js"></script>
    <style type="text/css">
        .gray-bg {
            background-color: #e9ecf3;
        }

        .col-sm-2 {
            width: 10%;
            padding-left: 5px;
            padding-right: 5px;
            float: left;
        }

        .p-lg {
            padding: 0px 0px 10px 0px;
        }

        .widget {
            margin-top: 0px;
        }

        .iconfont {
            font-size: 30px;
            color: white;
        }

        h2 {
            font-size: 20px;
        }

        .echart_div {
            height: 240px;
            width: 100%;
        }

        .ibtn {
            cursor: pointer;
        }

        .flot-chart {
            height: 400px;
        }

        @media (min-width: 992px) {
            .col-cs-2 {
                width: 11.11%;
                padding-left: 5px;
                padding-right: 5px;
                float: left;
            }
        }
        li{
            font-size: 18px;
        }

    </style>
    <script type="text/javascript">
        Date.prototype.Format = function (fmt) { //author: meizz
            var o = {
                "M+": this.getMonth() + 1, //月份
                "d+": this.getDate(), //日
                "H+": this.getHours(), //小时
                "m+": this.getMinutes(), //分
                "s+": this.getSeconds(), //秒
                "q+": Math.floor((this.getMonth() + 3) / 3), //季度
                "S": this.getMilliseconds() //毫秒
            };
            if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
        }
    </script>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content">
    <div class="row">
        <div class="col-sm-9">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5>项目进度概览</h5>
                    <div class="pull-right">
                        <div class="btn-group">
                            <%--<button type="button" class="btn btn-xs btn-white active">天</button>
                            <button type="button" class="btn btn-xs btn-white">月</button>
                            <button type="button" class="btn btn-xs btn-white">年</button>--%>
                        </div>
                    </div>
                </div>
                <div class="ibox-content">
                    <div class="row">
                        <div class="col-sm-12">
                            <!--4:700-->
                            <div id="projectChart" class="flot-chart" style="height:200px;">
                                <div class="flot-chart-content" id="chart"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-sm-3  border-bottom white-bg dashboard-header">
            <h2>通知及备注：</h2>
            <ol>
                <li>会议通知</li>
                <li>物料达到情况通知</li>
                <li>任务变更通知</li>
                <li>关键任务节点通知</li>
                <li>临时任务通知</li>
            </ol>
        </div>
    </div>
    <!-- 全局js -->
    <script src="plug-in/hplus/js/jquery.min.js?v=2.1.4"></script>
    <script src="plug-in/hplus/js/bootstrap.min.js?v=3.3.6"></script>
    <!-- 自定义js -->
    <script src="plug-in/hplus/js/content.js"></script>
    <script type="text/javascript" src="plug-in/echart/user/echarts.min.js"></script>

    <script type="text/javascript" src="plug-in/echart/user/projectJS.js"></script>
    <script type="text/javascript" src="plug-in/jquery-plugs/i18n/jquery.i18n.properties.js"></script>
    <t:base type="tools"></t:base>
    <script type="text/javascript" src="plug-in/login/js/getMsgs.js"></script>
    <script>
        function GetProjectName(data,id){
            for(var i =0;i<data.length;i++){
                if(data[i].id==id){
                    return data[i].name;
                }
            }
            return "PROJECT"
        }

        $(document).ready(function () {
            $.ajax({
                type: "POST",
                url: "jformEchartController.do?GetProjectCount",
                success: function (jsondata) {
                    jsondata = JSON.parse(jsondata);
                    if(jsondata.count!=null){
                        var heightCss = jsondata.count * 300;
                        $("#projectChart").attr("style","height:"+heightCss+"px;");
                    }

                    var chart = echarts.init(document.getElementById('chart'));
                    $.ajax({
                        type: "POST",
                        url: "jformEchartController.do?GetProjectEchart",
                        data:{
                            // "finishDate_begin":"2018-01-30",
                            // "finishDate_end":"2019-02-20"
                        },
                        success: function (jsondata) {
                            jsondata = JSON.parse(jsondata);
                            var nowdate = new Date().Format("yyyy-MM-dd");
                            var seriesArray = new Array();//报表数据
                            var yAxisData = new Array();//y轴数据

                            if(jsondata!=null){
                                //分出绿，红，白 线
                                //绿0 任务是正常完成的
                                //红1 任务未完成，完成时间已经过了
                                //白2 任务未完成，完成时间位到

                                var lineData = new Array();
                                var projectindex = 0;
                                var projectname = '';
                                var lasttaskstatus,nowtaskstatus;
                                //节点颜色 0未完成(时间过了，红色，时间未到白色)  1按时完成(绿色)  2延时完成(橙)
                                $.each(jsondata.data,function(index,data){
                                    //任务节点
                                    if(data.taskStatus==0){//未完成
                                        if(data.finishDate>=nowdate){
                                            nowtaskstatus = 0;//未完成，还没到时间
                                        }else{
                                            nowtaskstatus = 3;//未完成，时间过了
                                        }
                                    }else if(data.taskStatus==1){
                                        nowtaskstatus = 1;//正常完成
                                    }else if(data.taskStatus==2){
                                        nowtaskstatus = 2;//延时完成
                                    }

                                    if(projectname==null || projectname==''){
                                        projectname = data.projectId;
                                        //添加y轴信息列表
                                        yAxisData.push({
                                            value:GetProjectName(jsondata.project,data.projectId),
                                            textStyle:yAxisDataStyle
                                        });
                                    }else if(projectname==data.projectId){
                                        //继续组装当前项目信息
                                        if(lasttaskstatus!=nowtaskstatus){//如果当前任务状态和上一个状态不同
                                            lineData.push([data.finishDate,projectindex,'',-1]);
                                            //分出绿0，红1，白2 (线)
                                            seriesArray.push(GetSeries(projectname,lineData,(lasttaskstatus==1||lasttaskstatus==2)?0:(lasttaskstatus==3?1:2)));
                                            lineData = new Array();
                                        }
                                    }else if(projectname!=data.projectId){
                                        //结束上一个项目的信息组装
                                        seriesArray.push(GetSeries(projectname,lineData,(lasttaskstatus==1||lasttaskstatus==2)?0:(lasttaskstatus==3?1:2)));
                                        projectindex++;
                                        lineData = new Array();
                                        //下一个项目信息开始
                                        projectname = data.projectId;
                                        //添加y轴信息列表
                                        yAxisData.push({
                                            value:GetProjectName(jsondata.project,data.projectId),
                                            textStyle:yAxisDataStyle
                                        });
                                    }
                                    lineData.push([data.finishDate,projectindex,data.taskShortname,nowtaskstatus]);
                                    if(data.finishDate>=nowdate &&data.taskStatus==1){
                                        lasttaskstatus =  0;
                                    }else{
                                        lasttaskstatus=nowtaskstatus;
                                    }
                                });
                                seriesArray.push(GetSeries(projectname,lineData,(lasttaskstatus==1||lasttaskstatus==2)?0:(lasttaskstatus==3?1:2)));
                            }
                            //debugger;
                            var option = {
                                title: {
                                    text: '项目进度看板',
                                    subtext: 'Keda'
                                },
                                tooltip: {
                                    trigger: 'item',
                                    formatter: '<b>{b0}</b>: {c0}'
                                },
                                grid: {
                                    left: '3%',
                                    right: '4%',
                                    bottom: '3%',
                                    containLabel: true,
                                    height:'auto'
                                },
                                xAxis:  {
                                    show:true,
                                    position:'top',
                                    type: 'time',
                                    nameGap:300,
                                    minInterval: 3600 * 24 * 1000 * 30,
                                    maxInterval: 3600 * 24 * 1000 * 60,
                                    interval:3600 * 24 * 1000 * 30,
                                    splitLine:{
                                        show:false
                                    }
                                },
                                yAxis: {
                                    type: 'category',//坐标轴类型。'value' 数值轴 'category' 类目轴 'time' 时间轴 'log' 对数轴
                                    nameGap:500,//坐标轴名称与轴线之间的距离。
                                    boundaryGap: [0.2, 0.2],
                                    lineHeight:400,
                                    data: yAxisData,
                                    axisLine:{
                                        show:false
                                    },
                                    splitLine:{
                                        show:true
                                    },
                                    axisTick:{
                                        show:false
                                    },
                                    offset:50,
                                    nameTextStyle:{
                                        fontSize:20,
                                        align:'center',
                                        verticalAlign:'middle',
                                        lineHeight:150,
                                        backgroundColor:'red'
                                    }
                                },
                                series: seriesArray
                            };
                            chart.setOption(option, true);
                            chart.on('click', function (params) {
                                if (params.componentType === 'markPoint') {
                                    // 点击到了 markPoint 上
                                    if (params.seriesIndex === 5) {
                                        // 点击到了 index 为 5 的 series 的 markPoint 上。
                                    }
                                }
                                else if (params.componentType === 'series') {
                                    console.log(params);
                                    // if (params.seriesType === 'graph') {
                                    //     if (params.dataType === 'edge') {
                                    //         // 点击到了 graph 的 edge（边）上。
                                    //     }
                                    //     else {
                                    //         // 点击到了 graph 的 node（节点）上。
                                    //     }
                                    // }
                                }
                            });

                        }
                    });
                    $(window).resize(chart.resize);

                }});


        });
    </script>
</body>
</html>