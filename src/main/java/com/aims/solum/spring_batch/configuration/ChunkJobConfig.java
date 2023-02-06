package com.aims.solum.spring_batch.configuration;

import com.aims.solum.spring_batch.listener.JobListener;
import com.aims.solum.spring_batch.listener.StepListener;
import com.aims.solum.spring_batch.processor.FirstItemProcessor;
import com.aims.solum.spring_batch.reader.FirstItemReader;
import com.aims.solum.spring_batch.writer.FirstItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ChunkJobConfig {

    @Autowired
    private FirstItemReader firstItemReader;

    @Autowired
    private FirstItemProcessor firstItemProcessor;

    @Autowired
    private FirstItemWriter firstItemWriter;

    @Bean
    public Job chunkJob(JobRepository jobRepository,
                        Step step,
                        JobListener jobListener) {
        final Job chunk_job = new JobBuilder("Chunk Job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
        return chunk_job;
    }

    @Bean
    public Step createStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, StepListener stepListener) {
        final TaskletStep chunk_step = new StepBuilder("Chunk Step", jobRepository)
                .<Integer, Long>chunk(3, transactionManager)
                .reader(firstItemReader)
                .processor(firstItemProcessor)
                .writer(firstItemWriter)
                .build();
        return chunk_step;
    }

}