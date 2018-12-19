package com.jeecg;

/**
 * 公共参数配置文件
 */
public class ConstSetBA {


    //项目状态
    public static int ProjectStatus_Unactivate = 0;//项目未激活
    public static int ProjectStatus_Activate = 1;//项目已经激活

    //任务类型
    public static int TaskType_Project = 1;
    public static int TaskType_Temporary = 2;


    //任务分配状态    0 草稿 1下发 2分配 3未审批 4审批中 5审批驳回 6执行中 7完成
    public static int TaskStatus_Draft = 0;//草稿
    public static int TaskStatus_Send = 1;//下发
    public static int TaskStatus_Assign = 2;//分配
    public static int TaskStatus_Execution  = 3;//执行中
    public static int TaskStatus_Finish  = 4;//完成

    //任务等级
    public static int PlanLevel_First = 1 ;//一级任务
    public static int PlanLevel_Second = 2;//二级任务
    public static int PlanLevel_Third = 3;//三级任务
    public static int PlanLevel_Temp = 4;//临时任务


    //任务计划状态 0 草稿 1下发细分 2细分完成 3提交审批 4审批驳回 5执行中 6完成
    public static int PlanStatus_Draft = 0;//草稿
    public static int PlanStatus_SendDivide = 1;//下发细分
    public static int PlanStatus_SendDivideFinish = 2;//细分完成
    public static int PlanStatus_SendApprove = 3;//提交审批
    public static int PlanStatus_Disapprove = 4;//审批驳回
    public static int PlanStatus_Execution = 5;//执行中
    public static int PlanStatus_Finish = 6;//完成



}
