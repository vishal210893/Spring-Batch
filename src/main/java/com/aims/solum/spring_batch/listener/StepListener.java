package com.aims.solum.spring_batch.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class StepListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("Before Step " + stepExecution.getStepName());
        System.out.println("Job Exec Cont " + stepExecution.getJobExecution().getExecutionContext());
        System.out.println("Step Exec Cont " + stepExecution.getExecutionContext());

        stepExecution.getExecutionContext().put("sec", "sec value");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println("After Step " + stepExecution.getStepName());
        System.out.println("Job Exec Cont " + stepExecution.getJobExecution().getExecutionContext());
        System.out.println("Step Exec Cont " + stepExecution.getExecutionContext());
        stepExecution.getJobExecution().getExecutionContext().put("test", "hello");
        return null;
    }

}