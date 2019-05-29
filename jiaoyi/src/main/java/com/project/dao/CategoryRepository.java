package com.project.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.pojo.CateGory;

public interface CategoryRepository extends JpaRepository<CateGory, Long> {

}
