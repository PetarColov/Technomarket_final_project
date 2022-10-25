package com.example.technomarket.model.repository;

import com.example.technomarket.model.pojo.CharacteristicKey;
import com.example.technomarket.model.pojo.Chars;
import com.example.technomarket.model.pojo.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductsWithCharacteristicsRepository extends JpaRepository<Chars, CharacteristicKey> {
    public List<Chars> findAllByProduct(Product product);
}
