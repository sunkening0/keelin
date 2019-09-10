package com.skn.keelin.task.quartz.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import lombok.extern.slf4j.Slf4j;

/**
 * 
* 类名称：Job   
* 类描述：   
* 创建人：skn   
* 创建时间：2019年8月26日 下午10:46:47   
* @version
 */
@Slf4j
@DisallowConcurrentExecution   //@DisallowConcurrentExecution 保证上一个任务执行完后，再去执行下一个任务，这里的任务是同一个任务,所以这里不必担心线程安全问题了
public class Job extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 获取参数
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        log.info("定时任务执行中。。。参数："+jobDataMap.get("name").toString());
    } 
}
