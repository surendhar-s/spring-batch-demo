package com.cognizant.springbatchdemo.config;

import javax.activation.DataSource;

import com.cognizant.springbatchdemo.model.Product;
import com.cognizant.springbatchdemo.repository.ProductRepository;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class BatchConfigurationSaveDbToFile {

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    ProductRepository productRepository;

    @Bean
    public ConfigJobListener listener() {
        return new ConfigJobListener();
    }

    @Bean
    public Step step1(ItemReader<Product> reader, ItemProcessor<Product, Product> processor,
            ItemWriter<Product> writer) {
        return stepBuilderFactory.get("step1").<Product, Product>chunk(500).reader(reader).processor(processor)
                .writer(writer).build();
    }

    @Bean
    public Job saveFileToDb(Step step1) {
        return jobBuilderFactory.get("saveFileToDb").incrementer(new RunIdIncrementer()).listener(listener())
                .start(step1).build();
    }

    @Bean
    public ItemReader<Product> reader() {
        FlatFileItemReader<Product> fileItemReader = new FlatFileItemReader<>();
        fileItemReader.setResource(new ClassPathResource("csv_data.csv"));
        fileItemReader.setLinesToSkip(1);
        DefaultLineMapper<Product> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames("name", "quantity", "price", "availability");
        BeanWrapperFieldSetMapper<Product> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Product.class);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        lineMapper.setLineTokenizer(lineTokenizer);
        fileItemReader.setLineMapper(lineMapper);
        fileItemReader.setSaveState(true);
        return fileItemReader;
    }

    @Bean
    public ItemProcessor<Product, Product> processor() {
        return p -> {
            return p;
        };
    }

    @Bean
    public ItemWriter<Product> writer() {
        return (data) -> {
            productRepository.saveAll(data);
        };
    }

    @Bean
    public JobLauncher jobLauncher1(JobRepository jobRepository) throws Exception {
        SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
        simpleJobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        simpleJobLauncher.setJobRepository(jobRepository);
        simpleJobLauncher.afterPropertiesSet();
        return simpleJobLauncher;
    }
}
