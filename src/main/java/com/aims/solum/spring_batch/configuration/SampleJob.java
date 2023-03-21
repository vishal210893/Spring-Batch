package com.aims.solum.spring_batch.configuration;

import com.aims.solum.spring_batch.listener.JobListener;
import com.aims.solum.spring_batch.model.StudentCsv;
import com.aims.solum.spring_batch.service.writer.FileItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;

@Configuration
public class SampleJob {

	@Autowired
	private FileItemWriter fileItemWriter;

	@Bean
	@Qualifier("fileJob")
	public Job fileJob(JobRepository jobRepository,
						@Qualifier("fileStep") Step fileStep,
						JobListener jobListener) {
		final Job fileJob = new JobBuilder("File Job", jobRepository)
				.incrementer(new RunIdIncrementer())
				.start(fileStep)
				.build();
		return fileJob;
	}

	@Bean
	@Qualifier("fileStep")
	public Step fileStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws FileNotFoundException {
		final TaskletStep fileStep = new StepBuilder("File Step", jobRepository)
				.<StudentCsv, StudentCsv>chunk(3, transactionManager)
				.reader(flatFileItemReader())
				//.processor(firstItemProcessor)
				.writer(fileItemWriter)
				.build();
		return fileStep;
	}

	public FlatFileItemReader<StudentCsv> flatFileItemReader() throws FileNotFoundException {
		FlatFileItemReader<StudentCsv> flatFileItemReader = new FlatFileItemReader<>();
		flatFileItemReader.setResource(new FileSystemResource(ResourceUtils.getFile("classpath:students.csv")));

		final DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
		delimitedLineTokenizer.setNames("ID", "First Name", "Last Name", "Email");
		final BeanWrapperFieldSetMapper<StudentCsv> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
		beanWrapperFieldSetMapper.setTargetType(StudentCsv.class);

		final DefaultLineMapper<StudentCsv> lineMapper = new DefaultLineMapper<>();
		lineMapper.setLineTokenizer(delimitedLineTokenizer);
		lineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);

		flatFileItemReader.setLineMapper(lineMapper);
		flatFileItemReader.setLinesToSkip(1);
		return flatFileItemReader;
	}

}