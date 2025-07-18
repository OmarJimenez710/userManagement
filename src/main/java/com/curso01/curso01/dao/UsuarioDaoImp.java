package com.curso01.curso01.dao;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;
import jakarta.persistence.PersistenceContext;
import com.curso01.curso01.models.UserModel;
import de.mkammerer.argon2.Argon2Factory;
import jakarta.persistence.EntityManager;
import de.mkammerer.argon2.Argon2;

import java.util.List;

@Repository
@Transactional
public class UsuarioDaoImp implements UsuarioDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UserModel> getUserList() {
        /*
         * UserModel es una clase, que es su interior,por medio de anotaciones
         * sabe a que tabla de la base de datos, va a ser referencia
         */
        String query = "FROM UserModel";
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public void deleteUser(Long id) {
        // Busca el usuario por el id
        UserModel user = entityManager.find(UserModel.class, id);
        // una vez que encuentra el usuario, lo elimina
        entityManager.remove(user);
    }

    @Override
    public void createUser(UserModel user) {
        // crea un usuario
        entityManager.merge(user);
    }

    @Override
    public UserModel getUserLogged(UserModel user) {
        /*
         * Ejemplo de error --> al generar una consulta a una base
         * de datos, ya que pude haber una inyección de dependencias
         * 
         * String query = "FROM UserModel WHERE email='" + user.getEmail() +
         * "' AND password='" + user.getPassword() + "'";
         * 
         * ya que algún hacker, puede concatenar algo como "+ 1 or 1 ---"
         * esto siempre da true y el demas codigo se comenta
         * 
         * mejor hacerlo de la siguiente manera:
         */

        String query = "FROM UserModel WHERE email=:email";
        List<UserModel> userLogged = entityManager.createQuery(query)
                .setParameter("email", user.getEmail())
                .getResultList();

        if (userLogged.isEmpty()) {
            return null;
        }

        String passwordHashed = userLogged.get(0).getPassword();
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

        if (argon2.verify(passwordHashed, user.getPassword())) {
            return userLogged.get(0);
        }

        return null;
    }

}
