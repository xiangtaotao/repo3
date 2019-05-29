package com.project.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import com.project.pojo.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
