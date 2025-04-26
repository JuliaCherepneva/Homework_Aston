package org.aston.service;

import org.aston.model.UserModel;

import java.util.List;

public interface ServiceImpl {
    void create(UserModel user);
    UserModel read(int id);
    void update(UserModel user);
    void delete(int id);
    List<UserModel> getAll();
}
