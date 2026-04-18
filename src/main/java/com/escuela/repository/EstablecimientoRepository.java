package com.escuela.repository;

import com.escuela.entity.Establecimiento;
import com.escuela.entity.TipoEstablecimiento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstablecimientoRepository extends JpaRepository<Establecimiento, Long> {

    @Query("SELECT e FROM Establecimiento e WHERE e.tipo = :tipo AND e.activo = true")
    Page<Establecimiento> findByTipoAndActivoTrue(@Param("tipo") TipoEstablecimiento tipo, Pageable pageable);

    @Query("SELECT e FROM Establecimiento e WHERE e.activo = true")
    Page<Establecimiento> findAllActivos(Pageable pageable);

    @Procedure(name = "Establecimiento.buscarPorUbicacion")
    List<Establecimiento> buscarPorUbicacion(@Param("p_nombre_ubicacion") String nombreUbicacion);
}