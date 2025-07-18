package com.curso01.curso01.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import com.curso01.curso01.models.UserModel;
import com.curso01.curso01.dao.UsuarioDao;
import com.curso01.curso01.utils.JWTUtil;

@RestController
public class AuthController {
    @Autowired
    UsuarioDao userDao;

    @Autowired
    JWTUtil jwtUtil;

    @RequestMapping(value = "api/login", method = RequestMethod.POST)
    public String setLogin(@RequestBody UserModel user) {
        UserModel userLogged = userDao.getUserLogged(user);

        if (userLogged != null) {
            String token = jwtUtil.create(String.valueOf(userLogged.getId()), userLogged.getEmail());
            return token;
        }

        return "Usuario invalido";
    }

}
