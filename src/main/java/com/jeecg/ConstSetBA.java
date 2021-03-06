package com.jeecg;

/**
 * 公共参数配置文件
 */
public final class ConstSetBA {

    //Calendar
    public final static int Cal_FirstDay = 1;//第一天
    public final static int Cal_LastMonth = -1;//计算上个月
    public final static int Cal_NextMonth = 1;//计算下个月

    public final static int One = 1;
    //是否判断
    public final static int YesNo_No = 0;//否
    public final static int YesNo_Yes = 1;//是

    //项目状态
    public final static int ProjectStatus_Unactivate = 0;//项目未激活
    public final static int ProjectStatus_Activate = 1;//项目已经激活

    //任务类型
    public final static int TaskType_Project = 1;
    public final static int TaskType_Temporary = 2;


    //任务分配状态    0 草稿 1下发 2分配 3未审批 4审批中 5审批驳回 6执行中 7完成
    public final static int TaskStatus_Draft = 0;//草稿
    public final static int TaskStatus_Send = 1;//下发
    public final static int TaskStatus_Assign = 2;//分配
    public final static int TaskStatus_Execution  = 3;//执行中
    public final static int TaskStatus_Finish  = 4;//完成

    //任务等级
    public final static int PlanLevel_First = 1 ;//一级任务
    public final static int PlanLevel_Second = 2;//二级任务
    public final static int PlanLevel_Third = 3;//三级任务
    public final static int PlanLevel_User = 4;//个人任务

    //任务计划状态 0 草稿 1下发细分 2细分完成 3提交审批 4审批驳回 5执行中 6完成
    public final static int PlanStatus_Draft = 0;//草稿
    public final static int PlanStatus_SendDivide = 1;//下发细分
    public final static int PlanStatus_SendDivideFinish = 2;//细分完成
    public final static int PlanStatus_SendApprove = 3;//提交审批
    public final static int PlanStatus_Disapprove = 4;//审批驳回
    public final static int PlanStatus_Execution = 5;//执行中
    public final static int PlanStatus_Finish = 6;//完成

    //个人任务是否查看
    public final static int PlanUser_NotShow= 0;//不展示
    public final static int PlanUser_Show =1;//展示

    //完成状态
    public final static int PlanIsSuccess_NO = 0;//未完成
    public final static int PlanIsSuccess_YES = 1;//正常完成
    public final static int PlanIsSuccess_DELAY = 2;//延时完成

    //预警状态
    public final static int PlanIsAlert_NO = 1;//正常
    public final static int PlanIsAlert_YES = 2;//预警

    //节假日类型
    public final static int HolidayType_Morning = 1;//上半天
    public final static int HolidayType_Afternoon = 2;//下半天
    public final static int HolidayType_WholeDay = 3;//全天


    //个人考勤调整类型
    public final static int AttendanceType_GoOut = 1;//出差
    public final static int AttendanceType_Leave = 2;//请假
    public final static int AttendanceType_OffWork = 3;//调休

    //个人考勤调整状态
    public final static int AttendanceStatus_NotFinish = 0;//未结束
    public final static int AttendanceStatus_Finish =1;//已结束


    //绩效考勤
    public final static int AssessStatus_NotDone = 0;//为评估
    public final static int AssessStatus_Done = 1;//已评估
}
