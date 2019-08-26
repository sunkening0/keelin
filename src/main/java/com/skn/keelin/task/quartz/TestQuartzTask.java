package com.skn.keelin.task.quartz;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.skn.keelin.task.quartz.job.Job;
import com.skn.keelin.task.quartz.job.ScheduleJobEntity;
import com.skn.keelin.task.quartz.service.ScheduleJobManage;

@Component
public class TestQuartzTask implements CommandLineRunner {
	@Autowired
	ScheduleJobManage scheduleJobManage;
	
	public void run(String... args) throws Exception {
		System.out.println(">>>>>>>>>>>>>>>服务启动执行，执行加载数据等操作<<<<<<<<<<<<<");
		
		HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("name",1);
        
        ScheduleJobEntity scheduleJobEntity = new ScheduleJobEntity();
        
        scheduleJobManage.deleteJob("job", "test");
        scheduleJobManage.saveCronJob(Job.class, "job", "test", "0 * * * * ?", map);

        map.put("name",2);
        scheduleJobManage.deleteJob("job2", "test");
        scheduleJobManage.saveCronJob(Job.class, "job2", "test", "10 * * * * ?", map);

        map.put("name",3);
        scheduleJobManage.deleteJob("job3", "test2");
        scheduleJobManage.saveCronJob(Job.class, "job3", "test2", "15 * * * * ?", map);


	}
}