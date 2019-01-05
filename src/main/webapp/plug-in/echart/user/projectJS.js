var yAxisDataStyle = {
    fontWeight:600,
    color:'black',
    fontSize:16,
    align:'center',//'left''center''right'
    verticalAlign:'middle',//'top''middle''bottom'
    height: 600,
    lineHeight:600,
    backgroundColor:'#e4e4e4'
};
var labelStyle_P = {
    normal: {
        position:'bottom',
        formatter:function (dataItem) {
            return dataItem.data[2] ? dataItem.data[2].split("").join('\n') : ""; //竖排文字
        },
        show: true,
        color:'#000000'//节点名称颜色
    }
};

var itemStyle_P ={
    normal: {
        color: function (dataItem) {
            if(dataItem.data[3]==0){//未完成 正常任务时间还没到(白) 0
                return '#ffffff';//节点颜色
            }else if(dataItem.data[3]==1){//正常完成（绿) 1
                return '#30ca11';//
            }else if(dataItem.data[3]==2){//延时完成(橙) 2
                return '#eeab2d';
            }else if(dataItem.data[3]==3){
                return '#de1f08';//未完成 时间过了(红)  3
            }else if(dataItem.data[3]==-1){
                return 'transparent';//透明
            }
        },
        borderColor: '#000000'
    }
};

var lineStyle_Green_P ={
    normal: {
        color: '#38e013',//线条颜色
        width: 4,
        type: 'solid'
    }
};
var lineStyle_Red_P ={
    normal: {
        color: '#ff4b11',//线条颜色
        width: 4,
        type: 'solid'
    }
};

var lineStyle_White_P ={
    normal: {
        color: '#a3aba5',//线条颜色
        width: 4,
        type: 'dotted'
    }
};


var tooltip_P ={
    formatter: //'<b>{c0}</b>'
    //data [日期,项目INDEX，任务名称,任务情况] 任务情况:0,时间还没到(白) 1，正常完成(绿) 2，延时完成(橙) 3，未完成(红)
        function (dataItem) {
            var title;
            if(dataItem.data[3]==0){
                title="计划时间:";
            }else if(dataItem.data[3]==1){
                title="完成时间:"
            }else if(dataItem.data[3]==2){
                title="延时时间:"
            }else if(dataItem.data[3]==3){
                title ="计划时间:"
            }
            return title+dataItem.data[0]+'<br/>'
            +"任务："+dataItem.data[2];
        }
};
var IsMakeLine = true;
var MakeLine_P = {
    silent:true,
    symbol:'none',
    animation:false,
    label:{
        show:false,
        //formatter: '{b}',
        position:'middle',
        color:'#d68fe8'
    },
    data:[{
        name: '',//new Date().Format("yyyy-MM-dd"),
        xAxis:  new Date().Format("yyyy-MM-dd"),
        label:{
            show:false,
            color:'#e81d1a'
        }
    }],
    lineStyle: {
        color: '#56e8e5',
        width:5,
        type:'dotted',
        shadowColor: 'rgba(0, 0, 0, 0.5)',
        shadowBlur: 10
    },
    itemStyle: {
        normal: {
            label: {
                show: false,
                color:'#e81d1a'
            },
            labelLine: {
                show: false,
                formatter: '',//'{b}',
                position:'middle',
            }
        } ,
        emphasis: {
            label: {
                show: false,
                position: 'outer',
                color:'#e81d1a'
            },
            labelLine: {
                show: false,
                color:'#e81d1a',
                lineStyle: {
                    color: 'red'
                }
            }
        }
    }
};


function GetSeries(_name,_data,_linestyle){
    if(!IsMakeLine){MakeLine_P={}}
    IsMakeLine=false;
    if(_linestyle != null &&_linestyle ==0) _linestyle = lineStyle_Green_P;
    else if(_linestyle != null &&_linestyle == 1) _linestyle = lineStyle_Red_P;
    else if(_linestyle != null &&_linestyle == 2) _linestyle = lineStyle_White_P;
    else{_linestyle = lineStyle_White_P}
    return {
            name:_name,
            type:'line',
            //[日期,项目INDEX，任务名称,任务情况] 任务情况:0,时间还没到(白) 1，正常完成(绿) 2，延时完成(橙) 3，未完成(红)
            data:_data,
            smooth: true,
            symbol: 'circle',     // 系列级个性化拐点图形
            symbolSize: 15,
            //设置圈点和任务文字
            label:labelStyle_P,
            itemStyle: itemStyle_P,
            lineStyle: _linestyle,
            tooltip:tooltip_P,
            markLine : MakeLine_P
        };
}
