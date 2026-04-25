package com.proyecto.vehiculos.dto;

import lombok.Data;

@Data
public class PersonaRequestDTO {

    private String documento;
    private Long tipoDocumentoId;   // ID de tipo_documentos (CC=1, TI=2, PP=3, PAS=4)
    private String nombres;
    private String apellidos;
    private String correo;
    private Long tipoPersonaId;     // 1=C (Conductor), 2=A (Administrador)
}