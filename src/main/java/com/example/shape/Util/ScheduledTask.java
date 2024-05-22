package com.example.shape.Util;
import cn.hutool.core.date.DateTime;
import com.example.shape.Service.dataExtract.DataExtractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {
    @Autowired
    DataExtractService dataExtractService;
    @Scheduled(cron = "${spring.execute.corn}")
    public void scheduledTaskByCorn() {
       System.out.print("定时任务开始 ByCorn：" + DateTime.now());
        for (int i = 0; i < 10; i++) {
            boolean result=dataExtractService.dataExtract(Integer.toString(i));
        }

        System.out.print("定时任务结束 ByCorn：" +  DateTime.now());
    }
}