package org.aston.service;

import org.aston.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    @Query(value = "SELECT * FROM users ORDER BY created_at DESC LIMIT :count", nativeQuery = true)
    List<UserModel> findUsersCount(@Param("count") int count);
}
