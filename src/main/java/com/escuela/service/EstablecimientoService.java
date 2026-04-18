package com.escuela.service;

import com.escuela.dto.EstablecimientoRequestDTO;
import com.escuela.dto.EstablecimientoResponseDTO;
import com.escuela.entity.Establecimiento;
import com.escuela.entity.TipoEstablecimiento;
import com.escuela.entity.Ubigeo;
import com.escuela.exception.ResourceNotFoundException;
import com.escuela.repository.EstablecimientoRepository;
import com.escuela.repository.UbigeoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstablecimientoService {

    private final EstablecimientoRepository establecimientoRepository;
    private final UbigeoRepository ubigeoRepository;

    private EstablecimientoResponseDTO convertToDTO(Establecimiento entity) {
        EstablecimientoResponseDTO dto = new EstablecimientoResponseDTO();
        dto.setId(entity.getId());
        dto.setTipo(entity.getTipo().name());
        dto.setRuc(entity.getRuc());
        dto.setNombre(entity.getNombre());
        dto.setDireccion(entity.getDireccion());
        if (entity.getUbigeo() != null) {
            dto.setDepartamento(entity.getUbigeo().getDepartamento());
            dto.setProvincia(entity.getUbigeo().getProvincia());
            dto.setDistrito(entity.getUbigeo().getDistrito());
        }
        dto.setEstado(entity.getEstado());
        return dto;
    }

    private Establecimiento convertToEntity(EstablecimientoRequestDTO dto) {
        Establecimiento entity = new Establecimiento();
        entity.setTipo(TipoEstablecimiento.valueOf(dto.getTipo()));
        entity.setRuc(dto.getRuc());
        entity.setNombre(dto.getNombre());
        entity.setDireccion(dto.getDireccion());
        entity.setEstado(dto.getEstado());

        Ubigeo ubigeo = ubigeoRepository.findById(dto.getCodigoUbigeo())
                .orElseThrow(() -> new ResourceNotFoundException("Ubigeo no encontrado con código: " + dto.getCodigoUbigeo()));
        entity.setUbigeo(ubigeo);

        return entity;
    }

    @Transactional(readOnly = true)
    public Page<EstablecimientoResponseDTO> listar(String tipo, Pageable pageable) {
        Page<Establecimiento> page;
        if (tipo != null && !tipo.isEmpty()) {
            TipoEstablecimiento tipoEnum = TipoEstablecimiento.valueOf(tipo.toUpperCase());
            page = establecimientoRepository.findByTipoAndActivoTrue(tipoEnum, pageable);
        } else {
            page = establecimientoRepository.findAllActivos(pageable);
        }
        return page.map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public EstablecimientoResponseDTO obtenerPorId(Long id) {
        Establecimiento entity = establecimientoRepository.findById(id)
                .filter(Establecimiento::isActivo)
                .orElseThrow(() -> new ResourceNotFoundException("Establecimiento no encontrado con id: " + id));
        return convertToDTO(entity);
    }

    @Transactional
    public EstablecimientoResponseDTO crear(EstablecimientoRequestDTO request) {
        Establecimiento entity = convertToEntity(request);
        entity.setActivo(true);
        Establecimiento saved = establecimientoRepository.save(entity);
        return convertToDTO(saved);
    }

    @Transactional
    public EstablecimientoResponseDTO actualizar(Long id, EstablecimientoRequestDTO request) {
        Establecimiento entity = establecimientoRepository.findById(id)
                .filter(Establecimiento::isActivo)
                .orElseThrow(() -> new ResourceNotFoundException("Establecimiento no encontrado con id: " + id));

        entity.setTipo(TipoEstablecimiento.valueOf(request.getTipo()));
        entity.setRuc(request.getRuc());
        entity.setNombre(request.getNombre());
        entity.setDireccion(request.getDireccion());
        entity.setEstado(request.getEstado());

        if (request.getCodigoUbigeo() != null && !request.getCodigoUbigeo().equals(entity.getUbigeo().getCodigo())) {
            Ubigeo ubigeo = ubigeoRepository.findById(request.getCodigoUbigeo())
                    .orElseThrow(() -> new ResourceNotFoundException("Ubigeo no encontrado"));
            entity.setUbigeo(ubigeo);
        }

        Establecimiento updated = establecimientoRepository.save(entity);
        return convertToDTO(updated);
    }

    @Transactional
    public EstablecimientoResponseDTO actualizarParcial(Long id, Map<String, Object> updates) {
        Establecimiento entity = establecimientoRepository.findById(id)
                .filter(Establecimiento::isActivo)
                .orElseThrow(() -> new ResourceNotFoundException("Establecimiento no encontrado con id: " + id));

        updates.forEach((key, value) -> {
            switch (key) {
                case "nombre" -> entity.setNombre((String) value);
                case "direccion" -> entity.setDireccion((String) value);
                case "estado" -> entity.setEstado((String) value);
                case "codigoUbigeo" -> {
                    Ubigeo ubigeo = ubigeoRepository.findById((String) value)
                            .orElseThrow(() -> new ResourceNotFoundException("Ubigeo no encontrado"));
                    entity.setUbigeo(ubigeo);
                }
            }
        });

        Establecimiento updated = establecimientoRepository.save(entity);
        return convertToDTO(updated);
    }

    @Transactional
    public void eliminarLogico(Long id) {
        Establecimiento entity = establecimientoRepository.findById(id)
                .filter(Establecimiento::isActivo)
                .orElseThrow(() -> new ResourceNotFoundException("Establecimiento no encontrado con id: " + id));
        entity.marcarComoEliminado();
        establecimientoRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<EstablecimientoResponseDTO> buscarPorUbicacion(String nombreUbicacion) {
        List<Establecimiento> resultados = establecimientoRepository.buscarPorUbicacion(nombreUbicacion);
        return resultados.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<EstablecimientoResponseDTO> listarEnMemoria(int page, int size, String sortField, String sortDir) {
        // Simulamos datos en memoria (para cumplir con el requisito de paginación en memoria)
        List<Establecimiento> listaSimulada = Arrays.asList(
                // Aquí podrías tener datos precargados, pero usaremos los de BD
        );
        if (listaSimulada.isEmpty()) {
            listaSimulada = establecimientoRepository.findAll(); // todos, incluidos inactivos
        }

        java.util.Comparator<Establecimiento> comparator =
                (a, b) -> sortDir.equalsIgnoreCase("asc") ?
                        a.getNombre().compareTo(b.getNombre()) :
                        b.getNombre().compareTo(a.getNombre());

        List<Establecimiento> sorted = listaSimulada.stream()
                .sorted(comparator)
                .collect(Collectors.toList());

        int start = page * size;
        int end = Math.min(start + size, sorted.size());
        List<EstablecimientoResponseDTO> contenido = sorted.subList(start, end)
                .stream().map(this::convertToDTO).collect(Collectors.toList());

        return new PageImpl<>(contenido, PageRequest.of(page, size), sorted.size());
    }
}