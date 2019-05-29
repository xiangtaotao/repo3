package com.project.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.pojo.Collection;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

}
