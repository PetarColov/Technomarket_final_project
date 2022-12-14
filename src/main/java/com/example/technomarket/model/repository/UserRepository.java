package com.example.technomarket.model.repository;

import com.example.technomarket.model.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    //Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

//    @Query("select p.usersSubscribed FROM Product p")
//    Set<User> findUsersSubscribedForProduct(Long id);

    //Optional<User> findByEmailAndPassword(String email, String password);
}
