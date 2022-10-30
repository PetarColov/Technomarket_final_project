package com.example.technomarket.model.repository;

import com.example.technomarket.model.pojo.Chars;
import com.example.technomarket.model.pojo.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharsRepository extends JpaRepository<Chars, Long> {
    List<Chars> findAllByProduct(Product product);

}
