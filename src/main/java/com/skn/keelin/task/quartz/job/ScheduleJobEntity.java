package com.skn.keelin.task.quartz.job;

import java.util.Map;

import org.springframework.scheduling.quartz.QuartzJobBean;

import lombok.Data;

@Data
public class ScheduleJobEntity {
	private String jobName;//任务名称
	private String jobGroup;//任务分组
	private int jobTime;//每隔多长时间执行一次任务  SimpleTrigger-简单触发器  使用此字段
	private int jobTimes;//SimpleTrigger-简单触发器  使用此字段   执行几次  <0时不限制次数
	private String description;//任务描述
	private Class<? extends QuartzJobBean> jobClass;//执行类
	private String cronExpression;// CronTriger-Cron触发器  使用此字段
	private String triggerName;//执行时间
	private String triggerState;//任务状态
	private Map jobData;//任务参数
}
