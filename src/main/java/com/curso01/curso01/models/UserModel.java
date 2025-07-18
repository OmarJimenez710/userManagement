package com.curso01.curso01.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Entity /* Hace referencia a que es una entidad de la base de datos */
@Table(name = "user") /* se usa para indicar que tabla (de la bd) debe consultar */
@AllArgsConstructor /* Crea un construtor de todos los atributos y uno vacio */
@NoArgsConstructor /* Crea un constructor sin parametros */
@Data /* Crea los setter y getter */

public class UserModel {
    @Id /* Le indicamos que va a ser la clave primaria */
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // para generar el id automaticamente
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "lastname")
    private String lastname;
    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phone;
    @Column(name = "password")
    private String password;
}
