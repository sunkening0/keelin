package com.skn.keelin.task;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 使用spring自带的定时任务组件
* 类名称：TestTask   
* 类描述：   
* 创建人：skn   
* 创建时间：2019年8月26日 下午4:07:35   
* @version
 */
@Component
@Configurable
@EnableScheduling
public class TestTask {


    /*@Scheduled(cron = "0/1 * * * * ?")
    public void test(){
        System.out.println("spring定时任务方法");
    }*/
}
