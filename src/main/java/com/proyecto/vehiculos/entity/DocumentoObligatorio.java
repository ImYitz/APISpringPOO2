package com.proyecto.vehiculos.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "documento_obligatorio")
@Data @NoArgsConstructor @AllArgsConstructor
public class DocumentoObligatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;  // RA (req. Automóvil), RM (req. Moto), RR (req. Ambos)
}

