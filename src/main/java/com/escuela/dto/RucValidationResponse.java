package com.escuela.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RucValidationResponse {
    private boolean valido;
    private String mensaje;
}