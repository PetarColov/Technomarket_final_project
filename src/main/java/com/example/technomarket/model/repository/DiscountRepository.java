package com.example.technomarket.model.repository;

import com.example.technomarket.model.pojo.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
     Optional<Discount> findByDiscountDescriptionAndDiscountPercent(String description, int percent);
     Optional<Discount> findByDiscountId(Long id);
}
