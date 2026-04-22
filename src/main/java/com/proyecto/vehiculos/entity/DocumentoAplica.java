package com.proyecto.vehiculos.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "documento_aplica")
@Data @NoArgsConstructor @AllArgsConstructor
public class DocumentoAplica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;  // A (Automóvil), M (Motocicleta), AM (Ambos)
}

