package com.aims.solum.spring_batch.configuration;

import com.aims.solum.spring_batch.listener.JobListener;
import com.aims.solum.spring_batch.listener.StepListener;
import com.aims.solum.spring_batch.service.ScheduleTest;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JobConfig {

    @Bean
    public ScheduleTest scheduleTest(){
      return new ScheduleTest();
    }

    @Bean
    @Qualifier("1stJob")
    public Job firstJob(JobRepository jobRepository,
                        @Qualifier("1stStep") Step firstStep,
                        @Qualifier("2ndStep") Step secondStep,
                        JobListener jobListener) {
        final Job first_job = new JobBuilder("First Job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(jobListener)
                .start(firstStep)
                .next(secondStep)
                .build();
        return first_job;
    }

    @Bean
    @Qualifier("1stStep")
    public Step firstStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, StepListener stepListener) {
        final TaskletStep taskletStep = new StepBuilder("First Step", jobRepository)
                .listener(stepListener)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("First tasklet step");
                    System.out.println("Job Execution Context" + chunkContext.getStepContext().getJobExecutionContext());
                    System.out.println("Step Execution Context" + chunkContext.getStepContext().getStepExecutionContext());
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
        return taskletStep;
    }

    @Bean
    @Qualifier("2ndStep")
    public Step SecondStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        final TaskletStep taskletStep = new StepBuilder("Second Step", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("Second tasklet step");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
        return taskletStep;
    }

}