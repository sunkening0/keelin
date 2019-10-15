package com.skn.keelin.annotation;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.sun.corba.se.spi.orbutil.threadpool.ThreadPool;

@Component
public class TestRepeatAnnotation implements CommandLineRunner {

	
	@Override
	public void run(String... args) throws Exception {

		System.out.println("执行多线程测试");
		String url = "http://localhost:8083/keelin/redis?token=123ssss";
		CountDownLatch countDownLatch = new CountDownLatch(1); 
		CountDownLatch await = new CountDownLatch(10);
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		RestTemplate restTemplate = new RestTemplate();
		for (int i = 0; i < 10; i++) {
			String userId = "userId" + i;
			HttpEntity request = buildRequest(userId);
			executorService.submit(() -> {
				try {
					countDownLatch.await();//等待主线程执行完毕，获得开始执行信号...  即等待主线程执行countDownLatch.countDown()之后才开始执行
					System.out.println(
							"Thread:" + Thread.currentThread().getName() + ", time:" + System.currentTimeMillis());
					ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
					System.out.println("Thread:" + Thread.currentThread().getName() + "," + response.getBody());
					await.countDown();  //await减1  减到0时 唤醒阻塞的主线程  主线程执行bingo！
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
		System.out.println("用于触发处于等待状态的线程开始工作......");
		countDownLatch.countDown();  //countDownLatch值减1  由初始值1  变为0  此时处于阻塞状态的子线程被唤醒
		await.await();  //主线程被阻塞
		System.out.println("Bingo!");
	}

	private HttpEntity buildRequest(String userId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "yourToken");
		Map<String, Object> body = new HashMap<>();
		body.put("userId", userId);
		return new HttpEntity<>(body, headers);
	}

}
