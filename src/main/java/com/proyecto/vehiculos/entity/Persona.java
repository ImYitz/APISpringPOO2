package com.proyecto.vehiculos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "persona")
@Data @NoArgsConstructor @AllArgsConstructor
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "documento")
    private String documento;

    @ManyToOne
    @JoinColumn(name = "tipo_id", nullable = false)
    private TipoDocumento tipoDocumento;

    @Column(name = "nombres")
    private String nombres;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name = "correo")
    private String correo;

    @ManyToOne
    @JoinColumn(name = "tipo_persona", nullable = false)
    private TipoPersona tipoPersona;

    @Lob
    @Column(name = "licencia", columnDefinition = "LONGBLOB")
    private byte[] licencia;

    @Column(name = "fecha_vigencia_licencia")
    private java.time.LocalDate fechaVigenciaLicencia;
}
