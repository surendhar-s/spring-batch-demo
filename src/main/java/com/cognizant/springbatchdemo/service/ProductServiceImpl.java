package com.cognizant.springbatchdemo.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    Job job1;

    @Autowired
    Job job2;

    @Autowired
    JobLauncher jobLauncher;

    @Override
    public String saveFileToDb() throws JobExecutionAlreadyRunningException, JobRestartException,
            JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        Map<String, JobParameter> parameters = new HashMap<>();
        parameters.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(parameters);
        JobExecution jobExecution = jobLauncher.run(job1, jobParameters);
        System.out.println("From file -> db: " + jobExecution.getStatus());
        return "<h3>Job Running</h3>";
    }

    @Override
    public String saveDbToFile() throws JobExecutionAlreadyRunningException, JobRestartException,
            JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        Map<String, JobParameter> parameters = new HashMap<>();
        parameters.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(parameters);
        JobExecution jobExecution = jobLauncher.run(job2, jobParameters);
        System.out.println(jobExecution.getStatus());
        return "<h3>Job Is running.... Check result in target/classes/output.csv file in project directory.</h3>";
    }

}
