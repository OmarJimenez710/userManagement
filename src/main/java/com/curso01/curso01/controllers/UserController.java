
package com.curso01.curso01.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import com.curso01.curso01.models.UserModel;
import com.curso01.curso01.dao.UsuarioDao;
import com.curso01.curso01.utils.JWTUtil;
import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired /* lo que hace esta notación, es cargar el daoImp en esta variable */
    private UsuarioDao userDao;

    @Autowired
    private JWTUtil jwtUtil;

    @RequestMapping(value = "api/user/{id}")
    public UserModel getUser(@PathVariable Long id) {
        UserModel user = new UserModel();
        user.setId(id);
        user.setName("Omar");
        user.setLastname("Jimenez");
        user.setPhone("7222867512");
        user.setEmail("omar@gmail.com");

        return user;
    }

    @RequestMapping(value = "api/user", method = RequestMethod.GET)
    public List<UserModel> getUserList(@RequestHeader(value = "Authorization") String token) {
        /* Manejo del token */
        String userId = jwtUtil.getKey(token);

        if (userId == null) {
            return new ArrayList<>();
        }

        return userDao.getUserList();
    }

    @RequestMapping(value = "api/user", method = RequestMethod.POST)
    public String createUser(@RequestBody UserModel user, @RequestHeader(value = "Authorization") String token) {
        String userId = jwtUtil.getKey(token);

        if (userId != null) {
            Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
            String passwordHash = argon2.hash(1, 1024, 1, user.getPassword());
            user.setPassword(passwordHash);

            userDao.createUser(user);

            return "Ok";
        } else {
            return "Token inválido";
        }
    }

    @RequestMapping(value = "api/user/{id}", method = RequestMethod.DELETE)
    public String deleteUser(@PathVariable Long id, @RequestHeader(value = "Authorization") String token) {
        String userId = jwtUtil.getKey(token);

        if (userId != null) {
            userDao.deleteUser(id);

            return "Ok";
        } else {
            return "Token inválido";
        }
    }

}
