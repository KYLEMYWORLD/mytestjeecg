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

        /*  .top-navigation .wrapper.wrapper-content{padding:20px 5px !important;}
         .container {
              width:99% !important; margin:10px;
              padding-right: 1px !important;
              padding-left: 1px !important;
         }
         .color_red{color:#e55555;}
         .col-cs-2 {
             width: 10%;
             padding-left: 5px;
             padding-right: 5px;
             float: left;
         }*/

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
                            <button type="button" class="btn btn-xs btn-white active">天</button>
                            <button type="button" class="btn btn-xs btn-white">月</button>
                            <button type="button" class="btn btn-xs btn-white">年</button>
                        </div>
                    </div>
                </div>
                <div class="ibox-content">
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="flot-chart" style="height:315px;">
                                <div class="flot-chart-content" id="chart4"></div>
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
    <script type="text/javascript" src="plug-in/echart/echarts.min.js"></script>
    <script type="text/javascript" src="plug-in/jquery-plugs/i18n/jquery.i18n.properties.js"></script>
    <t:base type="tools"></t:base>
    <script type="text/javascript" src="plug-in/login/js/getMsgs.js"></script>
    <script>
        $(document).ready(function () {


            var chart4 = echarts.init(document.getElementById('chart4'));
            $.ajax({
                type: "POST",
                url: "jeecgListDemoController.do?broswerCount&reportType=line",
                success: function (jsondata) {
                    jsondata = JSON.parse(jsondata);
                    var data = jsondata[0].data;
                    var xAxisData = [];
                    var seriesData = [];
                    for (var i in data) {
                        xAxisData.push(data[i].name);
                        seriesData.push(data[i].percentage);
                    }
                    var option4 = {
                        color: ['#3398DB'],
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                                type: 'line'        // 默认为直线，可选为：'line' | 'shadow'
                            }
                        },
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '10%',
                            containLabel: true
                        },
                        xAxis: [
                            {
                                type: 'category',
                                data: xAxisData,
                                axisTick: {
                                    alignWithLabel: true
                                },
                                axisLabel: {
                                    interval: 0,//横轴信息全部显示
                                    rotate: -30,//-10角度倾斜展示
                                }
                            }
                        ],
                        yAxis: [
                            {
                                type: 'value'
                            }
                        ],
                        series: [
                            {
                                name: '用户人数',
                                type: 'line',
                                barWidth: '60%',
                                data: seriesData
                            }
                        ]
                    };
                    chart4.setOption(option4, true);
                }
            });
            $(window).resize(chart4.resize);
        });
    </script>
</body>
</html>