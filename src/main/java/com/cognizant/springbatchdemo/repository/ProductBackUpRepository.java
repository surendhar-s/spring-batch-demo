package com.cognizant.springbatchdemo.repository;

import com.cognizant.springbatchdemo.model.ProductBackUp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductBackUpRepository extends JpaRepository<ProductBackUp, Integer> {

}
