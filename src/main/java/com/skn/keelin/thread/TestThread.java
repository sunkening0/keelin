package com.skn.keelin.thread;

import com.skn.keelin.async.impl.AsyncServiceImpl;

public class TestThread {
	
	public static class ThreadTest1 implements Runnable {
		
		public void run() {
			System.out.println(Thread.currentThread().getName());
			try {
				Thread.sleep(1000);   //线程没睡醒的时候   被interrupt了  就会抛出InterruptedException
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println(Thread.currentThread().isInterrupted());
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * interrupted()是静态方法：内部实现是调用的当前线程的isInterrupted()，并且会重置当前线程的中断状态
	 * isInterrupted()是实例方法，是调用该方法的对象所表示的那个线程的isInterrupted()，不会重置当前线程的中断状态
	 * @param args
	 */
	public static void main(String[] args) {
		Thread thread = new Thread(new TestThread.ThreadTest1());
		thread.start();
		//thread.interrupt();
		Thread.currentThread().interrupt();
		//System.out.println(Thread.currentThread().isInterrupted());  //true  说明isInterrupted不会重置中断状态
		//System.out.println(Thread.currentThread().isInterrupted());  //true
		
		//System.out.println(Thread.currentThread().interrupted());  //true  说明interrupted会重置中断状态
		//System.out.println(Thread.currentThread().interrupted());  //false
		
		//System.out.println(thread.isInterrupted());  //说明main线程中断了  子线程也不会中断
		//System.out.println(thread.isInterrupted());
	}

}
