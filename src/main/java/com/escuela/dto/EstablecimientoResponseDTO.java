package com.escuela.dto;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class EstablecimientoResponseDTO extends RepresentationModel<EstablecimientoResponseDTO> {
    private Long id;
    private String tipo;
    private String ruc;
    private String nombre;
    private String direccion;
    private String departamento;
    private String provincia;
    private String distrito;
    private String estado;
}