package com.escuela.repository;

import com.escuela.entity.Ubigeo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UbigeoRepository extends JpaRepository<Ubigeo, String> {
}