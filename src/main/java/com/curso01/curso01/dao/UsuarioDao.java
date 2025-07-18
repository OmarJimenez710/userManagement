package com.curso01.curso01.dao;

import com.curso01.curso01.models.UserModel;
import java.util.List;

public interface UsuarioDao {

    List<UserModel> getUserList();

    void deleteUser(Long id);

    void createUser(UserModel user);

    UserModel getUserLogged(UserModel user);
}
