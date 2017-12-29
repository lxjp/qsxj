package com.example.demo.repository;

import com.example.demo.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by hzlvxiaojia on 2017-12-14.
 */
public interface BookRepository extends JpaRepository<Book, Long>{
    List<Book> findByReader(String reader);
}
