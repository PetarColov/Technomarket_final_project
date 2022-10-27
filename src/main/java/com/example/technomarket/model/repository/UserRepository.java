package com.example.technomarket.model.repository;

import com.example.technomarket.model.pojo.Product;
import com.example.technomarket.model.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    //Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Query("select p.usersSubscribed FROM Product p")
    Set<User> findUsersSubscibedForProduct(Long id);

    //Optional<User> findByEmailAndPassword(String email, String password);
}
