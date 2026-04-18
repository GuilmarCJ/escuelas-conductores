package com.escuela.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "establecimiento")
@Data
@NamedStoredProcedureQuery(
    name = "Establecimiento.buscarPorUbicacion",
    procedureName = "sp_buscar_por_ubicacion",
    parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_nombre_ubicacion", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.REF_CURSOR, name = "p_cursor", type = void.class)
    },
    resultClasses = Establecimiento.class
)
public class Establecimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_establecimiento")
    @SequenceGenerator(name = "seq_establecimiento", sequenceName = "seq_establecimiento", allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoEstablecimiento tipo;

    @Column(nullable = false, length = 11)
    private String ruc;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(length = 300)
    private String direccion;

    @ManyToOne
    @JoinColumn(name = "codigo_ubigeo")
    private Ubigeo ubigeo;

    @Column(length = 20)
    private String estado;

    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_registro", updatable = false)
    private Date fechaRegistro;

    @UpdateTimestamp
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_actualizacion")
    private Date fechaActualizacion;

    @Column(nullable = false)
    private boolean activo = true;

    // Método auxiliar para borrado lógico
    public void marcarComoEliminado() {
        this.activo = false;
    }
}