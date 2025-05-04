package org.aston.repository;

import org.aston.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Integer> {
    UserModel save(UserModel user);
    Optional<UserModel> findById (Integer id);
    void deleteById(Integer id);

}
