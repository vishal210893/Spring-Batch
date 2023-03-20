package com.aims.solum.spring_batch.controller;

import com.aims.solum.spring_batch.service.StartJobService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class JobLauncherController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobOperator jobOperator;

    @Autowired
    @Qualifier("2ndJob")
    private Job job;

    @Autowired
    @Qualifier("fileJob")
    private Job fileJob;

    @Autowired
    private StartJobService startJobService;

    @GetMapping("/startJob")
    public ResponseEntity<String> startJob() throws Exception {
        final JobExecution jobExecution = jobLauncher.run(job, new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters());
        return ResponseEntity.ok().body(jobExecution.getStatus().name() + " " + jobExecution.getJobId());
    }

    @GetMapping("/async/startJob")
    public ResponseEntity<String> startasyncJob() throws Exception {
        startJobService.startAsyncJob();
        return ResponseEntity.ok().body("job started");
    }

    @GetMapping("/stopJob")
    public ResponseEntity<String> stopJob(@RequestParam long id) throws Exception {
        jobOperator.stop(id);
        return ResponseEntity.ok().body("job with id " + id + " stopped");
    }

    @GetMapping("/fileJob")
    public ResponseEntity<String> staartFileJob() throws Exception {
        final JobExecution jobExecution = jobLauncher.run(fileJob, new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters());
        return ResponseEntity.ok().body(jobExecution.getStatus().name() + " " + jobExecution.getJobId());
    }

}