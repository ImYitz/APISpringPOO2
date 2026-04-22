package com.proyecto.vehiculos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuario")
@Data @NoArgsConstructor @AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login")
    private String login;

    @ManyToOne
    @JoinColumn(name = "id_persona", nullable = false)
    private Persona idPersona;

    @Column(name = "password")
    private String password;

    @Column(name = "apikey")
    private String apikey;
}

