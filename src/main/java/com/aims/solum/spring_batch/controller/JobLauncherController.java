package com.aims.solum.spring_batch.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class JobLauncherController {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job;

    @RequestMapping("/startJob")
    public ResponseEntity<String> handle() throws Exception {
        final JobExecution jobExecution = jobLauncher.run(job, new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis()).toJobParameters());
        return ResponseEntity.ok().body(jobExecution.getStatus().name());
    }

}