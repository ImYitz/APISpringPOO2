package com.proyecto.vehiculos.dto;

import lombok.Data;
import java.util.List;

@Data
public class UploadPdfDTO {

    // Lista de documentos a los que se les carga el PDF
    // Se puede enviar uno o varios a la vez
    private List<PdfItemDTO> documentos;

    @Data
    public static class PdfItemDTO {
        private Long documentoVehiculoId;  // ID de la tabla documento_vehiculo
        private String pdfBase64;          // PDF codificado en Base64
    }
}