package com.cognizant.springbatchdemo.service;

import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

public interface ProductService {

        public String saveDbToFile() throws JobExecutionAlreadyRunningException, JobRestartException,
                        JobInstanceAlreadyCompleteException, JobParametersInvalidException;

        public String saveFileToDb() throws JobExecutionAlreadyRunningException, JobRestartException,
                        JobInstanceAlreadyCompleteException, JobParametersInvalidException;

}
