package com.example.demo.repository;

import com.example.demo.entity.Reader;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by hzlvxiaojia on 2017-12-15.
 */
public interface ReaderRepository extends JpaRepository<Reader, String>{
    Reader findOne(String username);
}
