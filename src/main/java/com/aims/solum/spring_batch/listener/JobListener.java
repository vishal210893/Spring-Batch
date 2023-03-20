package com.aims.solum.spring_batch.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class JobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("Before Job");
        System.out.println(jobExecution.getJobParameters());
        System.out.println(jobExecution.getExecutionContext());
        jobExecution.getExecutionContext().put("jec", "jec value");
        System.out.println("----------------------------------------");

    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("After Job");
        System.out.println(jobExecution.getJobParameters());
        System.out.println(jobExecution.getExecutionContext());
        System.out.println("----------------------------------------");
    }

}