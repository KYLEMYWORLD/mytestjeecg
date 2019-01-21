package com.jeecg.jform_holiday.task;

import com.jeecg.jform_holiday.service.JformHolidayServiceI;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 初始化节假日
 */
@Service("holidayYearInitTask")
public class HolidayYearInitTask implements Job {

    @Autowired
    private JformHolidayServiceI jformHolidayService;

    public void run(){
        long start = System.currentTimeMillis();
        org.jeecgframework.core.util.LogUtil.info("===================初始化节假日定时任务开始===================");
        try {
            jformHolidayService.initYearHoliday();
        } catch (Exception e) {
            e.printStackTrace();
        }
        org.jeecgframework.core.util.LogUtil.info("===================初始化节假日定时任务结束===================");
        long end = System.currentTimeMillis();
        long times = end - start;
        org.jeecgframework.core.util.LogUtil.info("总耗时"+times+"毫秒");
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        run();
    }
}
