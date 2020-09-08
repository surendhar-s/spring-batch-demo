package com.cognizant.springbatchdemo.config;

import javax.sql.DataSource;

import com.cognizant.springbatchdemo.model.Product;
import com.cognizant.springbatchdemo.model.ProductBackUp;
import com.cognizant.springbatchdemo.repository.ProductBackUpRepository;
import com.cognizant.springbatchdemo.repository.ProductRepository;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchConfigurationSaveDbToDb {

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    ProductBackUpRepository productBackUpRepository;

    @Autowired
    ProductRepository productRepository;

    @Bean
    public Job saveDbToDb(Step step3, JobExecutionListener listener3) {
        return jobBuilderFactory.get("saveDbToDb").incrementer(new RunIdIncrementer()).listener(listener3).start(step3)
                .build();
    }

    @Bean
    public Step step3(ItemReader<Product> reader3, ItemProcessor<Product, ProductBackUp> processor3,
            ItemWriter<ProductBackUp> writer3) {
        return stepBuilderFactory.get("step3").<Product, ProductBackUp>chunk(500).reader(reader3).processor(processor3)
                .writer(writer3).build();
    }

    @Bean
    public ItemReader<Product> reader3(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Product>().name("jdbc_reader").dataSource(dataSource).fetchSize(500)
                .sql("Select * from products_batch").beanRowMapper(Product.class).build();
    }

    @Bean
    public ItemProcessor<Product, ProductBackUp> processor3() {
        return (p) -> {
            ProductBackUp productBackUp = new ProductBackUp();
            productBackUp.setName(p.getName());
            productBackUp.setQuantity(p.getQuantity());
            productBackUp.setPrice(p.getPrice());
            productBackUp.setAvailability(p.getAvailability());
            return productBackUp;
        };
    }

    @Bean
    public ItemWriter<ProductBackUp> writer3() {
        return (data) -> {
            productBackUpRepository.saveAll(data);
        };
    }
}
