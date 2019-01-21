package com.jeecg.jform_attenstatistic.task;

import com.jeecg.jform_attenstatistic.service.JformAttenstatisticServiceI;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName:AttenstatisticCountTask
 * @Description:人员考勤数据统计定时任务
 * @Date:2019-01-10
 */
@Service("attenstatisticCountTask")
public class AttenstatisticCountTask implements Job {

    @Autowired
    private JformAttenstatisticServiceI jformAttendanceService;

    public void run(){
        long start = System.currentTimeMillis();
        org.jeecgframework.core.util.LogUtil.info("===================人员考勤数据统计定时任务开始===================");
        try {
            jformAttendanceService.doCountAll();//调用服务的统计所有用户的方法
        } catch (Exception e) {
            e.printStackTrace();
        }
        org.jeecgframework.core.util.LogUtil.info("===================人员考勤数据统计定时任务结束===================");
        long end = System.currentTimeMillis();
        long times = end - start;
        org.jeecgframework.core.util.LogUtil.info("总耗时"+times+"毫秒");
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        run();
    }
}
