package com.aims.solum.spring_batch.configuration;

import com.aims.solum.spring_batch.service.processor.FirstItemProcessor;
import com.aims.solum.spring_batch.service.reader.FirstItemReader;
import com.aims.solum.spring_batch.service.writer.FirstItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Qualifier("2ndJob")
    public Job chunkJob(JobRepository jobRepository,
                        @Qualifier("chunkStep") Step firstChunkStep,
                        @Qualifier("taskletStep") Step taskletStep) {
        final Job chunk_job = new JobBuilder("Chunk Job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(firstChunkStep)
                .next(taskletStep)
                .build();
        return chunk_job;
    }

    @Bean
    @Qualifier("chunkStep")
    public Step chunkStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        final TaskletStep chunk_step = new StepBuilder("Chunk Step", jobRepository)
                .<Integer, Long>chunk(3, transactionManager)
                .reader(firstItemReader)
                .processor(firstItemProcessor)
                .writer(firstItemWriter)
                .build();
        return chunk_step;
    }

    @Bean
    @Qualifier("taskletStep")
    public Step taskletStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        final TaskletStep taskletStep = new StepBuilder("Second Step", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("Second tasklet step");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
        return taskletStep;
    }

}