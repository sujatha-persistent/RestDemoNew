package com.psl.rest;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.quartz.SchedulerException;



public class MainClass{

	/**
	 * @param args
	 * @throws SchedulerException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws SchedulerException, IOException {
		final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(new RunnableClass(), 5, 21600,TimeUnit.SECONDS);
	}
}
