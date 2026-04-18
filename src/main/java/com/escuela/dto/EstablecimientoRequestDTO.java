package com.escuela.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EstablecimientoRequestDTO {
    @NotBlank(message = "El tipo es obligatorio")
    private String tipo; // ESCUELA, CENTRO_MEDICO, CENTRO_EVALUACION

    @NotBlank(message = "El RUC es obligatorio")
    @Size(min = 11, max = 11, message = "El RUC debe tener 11 dígitos")
    private String ruc;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String direccion;

    @NotBlank(message = "El código de ubigeo es obligatorio")
    private String codigoUbigeo;

    private String estado;
}