package com.escuela.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ubigeo")
@Data
public class Ubigeo {
    @Id
    @Column(name = "codigo_ubigeo", length = 6)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String departamento;

    @Column(nullable = false, length = 100)
    private String provincia;

    @Column(nullable = false, length = 100)
    private String distrito;

    @Column(name = "nombre_completo", insertable = false, updatable = false)
    private String nombreCompleto;
}	