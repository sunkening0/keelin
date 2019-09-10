package com.skn.keelin.task.quartz.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.DateBuilder;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 类名称：ScheduleJobFactoryManage 类描述： 任务操作服务 创建人：skn 创建时间：2019年8月26日 下午11:16:10
 * 
 * @version
 */
@Service
@Slf4j
public class ScheduleJobManage {
	@Autowired
	private Scheduler scheduler;
	@PostConstruct
	public void startScheduler() {
		try {
			// 开启调度器
			scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 使用simpleTrigger触发器触发简单任务
	 * 
	 * @param jobClass
	 * @param jobName
	 * @param jobGroupName
	 * @param jobTime
	 *            每隔多长时间执行一次任务
	 * @param jobTimes
	 *            执行几次 <0时不限制次数
	 * @param jobData
	 */
	public void saveSimpleJob(Class<? extends QuartzJobBean> jobClass, String jobName, String jobGroupName, int jobTime,
			int jobTimes, Map jobData) {
		log.info("新增任务");
		try {
			// 任务名称和组构成任务key
			JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();
			// 设置job参数
			if (jobData != null && jobData.size() > 0) {
				jobDetail.getJobDataMap().putAll(jobData);
			}
			// 使用simpleTrigger规则
			Trigger trigger = null;
			if (jobTimes < 0) {
				trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroupName)
						.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(1).withIntervalInSeconds(jobTime))
						.startNow().build();
			} else {
				trigger = TriggerBuilder
						.newTrigger().withIdentity(jobName, jobGroupName).withSchedule(SimpleScheduleBuilder
								.repeatSecondlyForever(1).withIntervalInSeconds(jobTime).withRepeatCount(jobTimes))
						.startNow().build();
			}
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 使用cornTrigger触发器
	 * 
	 * @param jobClass
	 * @param jobName
	 * @param jobGroupName
	 * @param cron
	 * @param jobData
	 */
	public void saveCronJob(Class<? extends QuartzJobBean> jobClass, String jobName, String jobGroupName, String cron,
			Map jobData) {
		log.info("新增任务");
		try {
			// 任务名称和组构成任务key
			JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();
			// 设置job参数
			if (jobData != null && jobData.size() > 0) {
				jobDetail.getJobDataMap().putAll(jobData);
			}
			// 使用cornTrigger规则
			// 触发器key
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroupName)
					.startAt(DateBuilder.futureDate(1, IntervalUnit.SECOND))
					.withSchedule(CronScheduleBuilder.cronSchedule(cron)).startNow().build();
			// 把作业和触发器注册到任务调度中
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 修改cron时间表达式
	 * 
	 * @param jobName
	 * @param jobGroupName
	 * @param jobTime
	 */
	public void updateJob(String jobName, String jobGroupName, String cron) {
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
					.withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
			// 重启触发器
			scheduler.rescheduleJob(triggerKey, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 删除job
	 * @param jobName
	 * @param jobGroupName
	 */
	public void deleteJob(String jobName, String jobGroupName) {
		log.info("删除任务");
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
			// 停止触发器
			scheduler.pauseTrigger(triggerKey);
			// 移除触发器
			scheduler.unscheduleJob(triggerKey);
			// 删除任务
			scheduler.deleteJob(new JobKey(jobName, jobGroupName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 暂停任务
	 * 
	 * @param jobName
	 * @param jobGroupName
	 */
	public void pause(String jobName, String jobGroupName) {
		log.info("暂停任务");
		try {
			JobKey key = new JobKey(jobName, jobGroupName);
			scheduler.pauseJob(key);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 恢复任务
	 * 
	 * @param jobName
	 * @param jobGroupName
	 */
	public void resume(String jobName, String jobGroupName) {
		log.info("恢复任务");
		try {
			JobKey key = new JobKey(jobName, jobGroupName);
			scheduler.resumeJob(key);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 触发任务 立即执行
	 * 
	 * @param jobName
	 * @param jobGroupName
	 */
	public void trigger(String jobName, String jobGroupName) {
		log.info("触发任务");
		try {
			JobKey key = new JobKey(jobName, jobGroupName);
			scheduler.triggerJob(key);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取所有计划中的任务列表
	 *
	 * @return
	 */
	public List<Map<String, Object>> queryAllJob() {
		List<Map<String, Object>> jobList = null;
		try {
			GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
			Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
			jobList = new ArrayList<Map<String, Object>>();
			for (JobKey jobKey : jobKeys) {
				List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
				for (Trigger trigger : triggers) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("jobName", jobKey.getName());
					map.put("jobGroupName", jobKey.getGroup());
					map.put("description", "触发器:" + trigger.getKey());
					Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
					map.put("jobStatus", triggerState.name());
					if (trigger instanceof CronTrigger) {
						CronTrigger cronTrigger = (CronTrigger) trigger;
						String cronExpression = cronTrigger.getCronExpression();
						map.put("jobTime", cronExpression);
					}
					jobList.add(map);
				}
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return jobList;
	}
	/**
	 * 获取所有正在运行的job
	 *
	 * @return
	 */
	public List<Map<String, Object>> queryRunJob() {
		List<Map<String, Object>> jobList = null;
		try {
			List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
			jobList = new ArrayList<Map<String, Object>>(executingJobs.size());
			for (JobExecutionContext executingJob : executingJobs) {
				Map<String, Object> map = new HashMap<String, Object>();
				JobDetail jobDetail = executingJob.getJobDetail();
				JobKey jobKey = jobDetail.getKey();
				Trigger trigger = executingJob.getTrigger();
				map.put("jobName", jobKey.getName());
				map.put("jobGroupName", jobKey.getGroup());
				map.put("description", "触发器:" + trigger.getKey());
				Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
				map.put("jobStatus", triggerState.name());
				if (trigger instanceof CronTrigger) {
					CronTrigger cronTrigger = (CronTrigger) trigger;
					String cronExpression = cronTrigger.getCronExpression();
					map.put("jobTime", cronExpression);
				}
				jobList.add(map);
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return jobList;
	}
}
