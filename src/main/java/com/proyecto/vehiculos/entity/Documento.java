package com.proyecto.vehiculos.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "documentos")
@Data @NoArgsConstructor @AllArgsConstructor
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_documento", nullable = false)
    private String codigoDocumento;

    @Column(nullable = false)
    private String nombre;  // SOAT, Técnico Mecánica, Seguro Todo Riesgo...

    // FK → documento_aplica (A, M, AM)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "aplica_a")
    private DocumentoAplica aplicaA;

    // FK → documento_obligatorio (RA, RM, RR)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "obligatorio")
    private DocumentoObligatorio obligatorio;

    private String descripcion;
}

