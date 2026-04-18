package com.escuela;

import com.escuela.dto.EstablecimientoRequestDTO;
import com.escuela.entity.Establecimiento;
import com.escuela.entity.TipoEstablecimiento;
import com.escuela.entity.Ubigeo;
import com.escuela.exception.ResourceNotFoundException;
import com.escuela.repository.EstablecimientoRepository;
import com.escuela.repository.UbigeoRepository;
import com.escuela.service.EstablecimientoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EstablecimientoServiceTest {

    @Mock
    private EstablecimientoRepository establecimientoRepository;

    @Mock
    private UbigeoRepository ubigeoRepository;

    @InjectMocks
    private EstablecimientoService service;

    private Ubigeo ubigeo;
    private Establecimiento establecimiento;

    @BeforeEach
    void setUp() {
        ubigeo = new Ubigeo();
        ubigeo.setCodigo("150101");
        ubigeo.setDepartamento("LIMA");
        ubigeo.setProvincia("LIMA");
        ubigeo.setDistrito("LIMA");

        establecimiento = new Establecimiento();
        establecimiento.setId(1L);
        establecimiento.setTipo(TipoEstablecimiento.ESCUELA);
        establecimiento.setRuc("12345678901");
        establecimiento.setNombre("Escuela de Prueba");
        establecimiento.setUbigeo(ubigeo);
        establecimiento.setActivo(true);
    }

    @Test
    void testObtenerPorId_Encontrado() {
        when(establecimientoRepository.findById(1L)).thenReturn(Optional.of(establecimiento));

        var dto = service.obtenerPorId(1L);

        assertNotNull(dto);
        assertEquals("Escuela de Prueba", dto.getNombre());
    }

    @Test
    void testObtenerPorId_NoEncontrado() {
        when(establecimientoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.obtenerPorId(99L));
    }

    @Test
    void testCrear() {
        EstablecimientoRequestDTO request = new EstablecimientoRequestDTO();
        request.setTipo("ESCUELA");
        request.setRuc("12345678901");
        request.setNombre("Nueva Escuela");
        request.setCodigoUbigeo("150101");

        when(ubigeoRepository.findById("150101")).thenReturn(Optional.of(ubigeo));
        when(establecimientoRepository.save(any(Establecimiento.class))).thenReturn(establecimiento);

        var dto = service.crear(request);

        assertNotNull(dto);
        verify(establecimientoRepository, times(1)).save(any(Establecimiento.class));
    }
}