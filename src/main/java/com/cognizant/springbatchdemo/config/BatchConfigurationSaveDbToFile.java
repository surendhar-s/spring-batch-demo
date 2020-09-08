package com.cognizant.springbatchdemo.config;


import javax.sql.DataSource;

import com.cognizant.springbatchdemo.model.Product;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class BatchConfigurationSaveDbToFile {

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Bean
    public Job saveDbToFile(Step step2, JobExecutionListener listener2) {
        return jobBuilderFactory.get("saveDbToFile").incrementer(new RunIdIncrementer()).listener(listener2)
                .start(step2).build();
    }

    @Bean
    public Step step2(ItemReader<Product> reader2, ItemProcessor<Product, Product> processor2,
            ItemWriter<Product> writer2) {
        return stepBuilderFactory.get("step2").<Product, Product>chunk(500).reader(reader2).processor(processor2)
                .writer(writer2).build();
    }

    @Bean
    public ItemReader<Product> reader2(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Product>().name("jdbc_reader").dataSource(dataSource).fetchSize(500)
                .sql("Select * from products_batch").beanRowMapper(Product.class).build();
    }

    @Bean
    public ItemProcessor<Product, Product> processor2() {
        return (p) -> {
            return p;
        };
    }

    @Bean
    public ItemWriter<Product> writer2() throws Exception {
        return new FlatFileItemWriterBuilder<Product>().name("csv_writer").delimited()
                .names("name", "quantity", "price", "availability").resource(new ClassPathResource("data_from_db.csv"))
                .build();
    }
}


