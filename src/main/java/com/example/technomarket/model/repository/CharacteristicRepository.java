package com.example.technomarket.model.repository;

import com.example.technomarket.model.pojo.Characteristic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharacteristicRepository extends JpaRepository<Characteristic, Long> {
     Optional<Characteristic> findCharacteristicByCharacteristicName(String name);
}
