package com.skn.keelin.async;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * 扩展ThreadPoolTaskExecutor   实现查看线程运行情况（每次提交线程的时候都会将当前线程池的运行状况打印出来）
* 类名称：VisiableThreadPoolTaskExecutor   
* 类描述：   
* 创建人：skn   
* 创建时间：2019年8月21日 上午11:02:49   
* @version
 */
public class VisiableThreadPoolTaskExecutor extends ThreadPoolTaskExecutor{
	private static final Logger logger = LoggerFactory.getLogger(VisiableThreadPoolTaskExecutor.class);

	private void showThreadPoolInfo(String prefix){
        ThreadPoolExecutor threadPoolExecutor = getThreadPoolExecutor();

        if(null==threadPoolExecutor){
            return;
        }

        logger.info("{}, {},taskCount [{}], completedTaskCount [{}], activeCount [{}], queueSize [{}]",
                this.getThreadNamePrefix(),
                prefix,
                threadPoolExecutor.getTaskCount(),
                threadPoolExecutor.getCompletedTaskCount(),
                threadPoolExecutor.getActiveCount(),
                threadPoolExecutor.getQueue().size());
    }

    @Override
    public void execute(Runnable task) {
        showThreadPoolInfo("1. do execute");
        super.execute(task);
    }

    @Override
    public void execute(Runnable task, long startTimeout) {
        showThreadPoolInfo("2. do execute");
        super.execute(task, startTimeout);
    }

    @Override
    public Future<?> submit(Runnable task) {
        showThreadPoolInfo("1. do submit");
        return super.submit(task);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        showThreadPoolInfo("2. do submit");
        return super.submit(task);
    }

    @Override
    public ListenableFuture<?> submitListenable(Runnable task) {
        showThreadPoolInfo("1. do submitListenable");
        return super.submitListenable(task);
    }

    @Override
    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
        showThreadPoolInfo("2. do submitListenable");
        return super.submitListenable(task);
    }
}
