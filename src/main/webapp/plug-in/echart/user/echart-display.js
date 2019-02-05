var refreshTims = 10000;
$(document).ready(function () {
    setTokenInterval();//定时更新token
    if(sessionToken == undefined || sessionToken =='' ){
        //初始化的时候获取token并开始加载echar表格数据
        getToken(getProjectCount);
    }
    // $('#myCarousel').carousel({
    //     interval: refreshTims
    // });

    $('#myCarousel').on('slide.bs.carousel', function (event) {
        var $hoder = $('#myCarousel').find('.item');
        $items = $(event.relatedTarget);
        //getIndex就是轮播到当前位置的索引
        var getIndex= $hoder.index($items);
        console.log(getIndex);

        if(intEchartArray.indexOf(getIndex+'')==-1){
            window.setTimeout("setEchartOnIndex("+getIndex+")",150);//使用字符串执行方法
        }
    });
});
var intEchartArray = new Array();

//1.get Token by using username and pass
var sessionToken = '';
var echart_u = 'echart';
var echart_p = 'kedajieneng';

var getTokenDate;
/**
 *
 * 每个token的有效时间为两小时
 */
function getTokens(){
    if(sessionToken == null || sessionToken ==""){
        getToken();
    }
    if(getTokenDate !=null && getInervalHour(getTokenDate) >= 1.9){
        getToken();
    }
    return sessionToken;
}
function getInervalHour(startDate) {
    var endDate = new Date();
    var ms = endDate.getTime() - startDate.getTime();
    if (ms < 0) return 0;
    return Math.floor(ms/1000/60/60);
}

/**
 * 更新Token值
 */
function getToken(func) {

    $.ajax({
        url:'rest/tokens?username='+echart_u+"&password="+echart_p,
        type:'POST',
        dataType:'JSON',
        success:function(res){
            sessionToken = res;
            getTokenDate = new Date();
            if(func!=null && typeof(func) == "function"){
                func();
            }
        },error:function(e){
            //获取token失败
            onTokenErrorCount ++;
        }

    });
}
function setTokenInterval(){
    window.setInterval(getToken, 10000*60*60);
}
var onTokenErrorCount =0;
function doError(e,func){
    getToken(func);
}

/**
 * 获取激活的项目数量
 */
function getProjectCount(){
    $.ajax({
        url:'rest/jformEchartController/GetProjectCount',
        headers: {
            'X-AUTH-TOKEN': getTokens()
        },
        type:'GET',
        dataType:'JSON',
        success:function(res){
            if(res.ok == true){
                var tmePoint = '';
                var tmeItem = '';
                for(var i=0; i< res.data ; i++){
                    if(i == 0){
                        tmePoint += pointHtml1_A+i+pointHtml2;
                        tmeItem += slideItem1_A+echartTemp+'chart'+i+echartTemp2 +slideItem2;
                    }else{
                        tmePoint += pointHtml1+i+pointHtml2;
                        tmeItem += slideItem1+echartTemp+'chart'+i+echartTemp2 +slideItem2;
                    }
                }
                $("#carousel-indicators").html($("#carousel-indicators").html()+tmePoint);
                $("#carousel-inner").html($("#carousel-inner").html()+tmeItem);
                projectCount = res.data;
                getEchartData(init);
            }
        },error:function (e) {
            doError(e,getProjectCount);

        }
    });

}

function GetProjectName(data,id){
    for(var i =0;i<data.length;i++){
        if(data[i].id==id){
            return data[i].name;
        }
    }
    return "PROJECT"
}
function GetProjectData(data,id){
    for(var i =0;i<data.length;i++){
        if(data[i].id==id){
            return data[i];
        }
    }
    return {mana :'',resp:''};
}


var echartTemp ="\<div class=\"ibox-content\"\>\<div class=\"row\">" +
    "\<div class=\"col-sm-12\"\><div class=\"flot-chart\" style=\"height:300px;\"> " +
    "<div class=\"flot-chart-content\" id=\"";

var echartTemp2 = "\"\>\<\/div\>\<\/div\>\</div\>\<\/div\>\<\/div\>";


//轮播HTML点
var pointHtml1_A = "\<li data-target=\"#myCarousel\" class=\"active\" data-slide-to=\"";
var pointHtml1 = "\<li data-target=\"#myCarousel\" data-slide-to=\"";
var pointHtml2 = "\"\>\<\/li\>"

//放置EchartDiv
var slideItem1_A ="\<div class=\"item active\"\>";
var slideItem1 ="\<div class=\"item\"\>";
var slideItem2 = "\<\/div\>";

function getEchartData_Internal() {
    if(projectCount<=0)return;
    if(chartArray.length<=0)return;
    if(optionsData.length<=0)return;
    getEchartData(updateEchart);
}

/**
 * 获取项目的详细数据
 * @pcount 项目的数量
 */
function getEchartData(func){
    $.ajax({
        url: "rest/jformEchartController/GetProjectEchart",
        headers: {
            'X-AUTH-TOKEN': getTokens()
        },
        type: "GET",
        data:{
            // "finishDate_begin":"2018-01-30",
            // "finishDate_end":"2019-02-20"
        },
        success: function (jsondata) {
            //debugger;
            jsondata = JSON.parse(jsondata);
            var nowdate = new Date().Format("yyyy-MM-dd");
            var seriesArray = new Array();//报表数据
            var seriesArrays = new Array();//不同项目数据分开
            var yAxisData = new Array();//y轴数据
            var yAxisDatas = new Array();//y轴数据

            jsondata = jsondata.data;
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
                        yAxisData.push([{
                            value:GetProjectName(jsondata.project,data.projectId),
                            textStyle:yAxisDataStyle
                        },{"data":GetProjectData(jsondata.project,data.projectId)}]);
                        ///yAxisDatas.push(yAxisData);
                        //yAxisData =[];
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
                        seriesArrays.push(seriesArray);
                        seriesArray = [];//重新装载下一个项目的series
                        projectindex++;
                        lineData = new Array();
                        //下一个项目信息开始
                        projectname = data.projectId;
                        //添加y轴信息列表
                        yAxisData.push([{
                            value:GetProjectName(jsondata.project,data.projectId),
                            textStyle:yAxisDataStyle
                        },{"data":GetProjectData(jsondata.project,data.projectId)}]);
                        //yAxisDatas.push(yAxisData);
                        //yAxisData =[];
                    }
                    lineData.push([data.finishDate,projectindex,data.taskShortname,nowtaskstatus]);
                    if(data.finishDate>=nowdate &&data.taskStatus==1){
                        lasttaskstatus =  0;
                    }else{
                        lasttaskstatus=nowtaskstatus;
                    }
                });
                seriesArray.push(GetSeries(projectname,lineData,(lasttaskstatus==1||lasttaskstatus==2)?0:(lasttaskstatus==3?1:2)));
                seriesArrays.push(seriesArray);
            }
            //组装Option
            initOption(yAxisData,seriesArrays,func);
        }
    });
}

/**
 * 组装Echart Option数据
 * @param yAxisData Y轴数据
 * @param seriesArrays  X轴数据
 */
function initOption(yAxisDatas,seriesArrays,func){
   // debugger;
    var options = new Array();
    for(var i = 0; i< yAxisDatas.length;i++){
        var option = {
            title: {
                text: yAxisDatas[i][0].value,
                subtext: "项目经理("+yAxisDatas[i][1].data.mana+"), 负责人("+yAxisDatas[i][1].data.resp+")",
                textStyle:{

                }
            },
            tooltip: {
                trigger: 'item',
                formatter: '<b>{b0}</b>: {c0}'
            },
            grid: {
                left: '2%',
                right: '4%',
                bottom: '2%',
                containLabel: true,
                height:'auto'
            },
            xAxis:  {
                show:true,
                position:'top',
                type: 'time',
                nameGap:300,
                minInterval: 3600 * 24 * 1000 * 30,
                maxInterval: 3600 * 24 * 1000 * 365,
                interval:3600 * 24 * 1000 * 365,
                splitLine:{
                    show:false
                }
            },
            yAxis: {
                type: 'category',//坐标轴类型。'value' 数值轴 'category' 类目轴 'time' 时间轴 'log' 对数轴
                nameGap:500,//坐标轴名称与轴线之间的距离。
                boundaryGap: ['10%', '10%'],
                lineHeight:400,
                data:[] ,//yAxisDatas[i]
                axisLine:{
                    show:false
                },
                splitLine:{
                    show:false
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
            series: seriesArrays[i]
        };
        options.push(option);
    }

    //初始化Echart组件
    // setChartAll(pcount,options);
    optionsData = options;
    if(typeof(func) == "function"){
        func();
    }
}

function init(){
    setEchartOnIndex(0);
    intEchartArray.push(0+'');
    window.setInterval(getEchartData_Internal, refreshTims * projectCount );//15秒
}

//保存Option数据
var optionsData = new Array();
//保存项目的数量
var projectCount;

/**
 * 设置全部echart
 * @param pcount    项目数量
 * @param option    Option数据组
 */
function setChartAll(pcount,option){
    for (var i = 0; i < pcount; i++) {
        setChart(i,option[i])
    }
}

function setEchartOnIndex(index) {
    if(index <0)return;
    if(projectCount<=index)return;
    if(optionsData.length <= index)return;
    setChart(index,optionsData[index]);
    intEchartArray.push(index+'');
}

/**
 * 初始化Echart视图
 * @param i
 * @param option
 */
function setChart(i, option) {
    var chart = echarts.init(document.getElementById('chart' + i));
    chart.setOption(option, true);
    chart.on('click', function (params) {
        if (params.componentType === 'markPoint') {
            // 点击到了 markPoint 上
            if (params.seriesIndex === 5) {
                // 点击到了 index 为 5 的 series 的 markPoint 上。
            }
        } else if (params.componentType === 'series') {
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
    $(window).resize(chart.resize);
    chartArray.push(chart);
}

//保存Echart对象 后面用于更新
var chartArray = new Array();

function updateEchart(){
    if(projectCount<=0)return;
    if(optionsData.length <=0)return;
    if(chartArray.length <=0) return;
    for (var i = 0 ; i< optionsData.length; i++){
        if(chartArray.length <= i)return;
        chartArray[i].setOption(optionsData[i]);
    }
}