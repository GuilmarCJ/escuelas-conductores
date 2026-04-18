package com.escuela.controller;

import com.escuela.dto.EstablecimientoRequestDTO;
import com.escuela.dto.EstablecimientoResponseDTO;
import com.escuela.service.EstablecimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/establecimientos")
@RequiredArgsConstructor
public class EstablecimientoController {

    private final EstablecimientoService service;
    private final PagedResourcesAssembler<EstablecimientoResponseDTO> pagedResourcesAssembler;

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<EstablecimientoResponseDTO>>> listar(
            @RequestParam(required = false) String tipo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortField);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<EstablecimientoResponseDTO> pageResult = service.listar(tipo, pageable);

        PagedModel<EntityModel<EstablecimientoResponseDTO>> pagedModel =
                pagedResourcesAssembler.toModel(pageResult, establecimiento ->
                        EntityModel.of(establecimiento,
                                linkTo(methodOn(EstablecimientoController.class).obtenerPorId(establecimiento.getId())).withSelfRel()));

        // Agregar enlace a búsqueda rápida
        pagedModel.add(linkTo(methodOn(EstablecimientoController.class).busquedaRapida("")).withRel("busqueda-rapida"));

        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<EstablecimientoResponseDTO>> obtenerPorId(@PathVariable Long id) {
        EstablecimientoResponseDTO dto = service.obtenerPorId(id);
        EntityModel<EstablecimientoResponseDTO> resource = EntityModel.of(dto);
        resource.add(linkTo(methodOn(EstablecimientoController.class).obtenerPorId(id)).withSelfRel());
        resource.add(linkTo(methodOn(EstablecimientoController.class).listar(null, 0, 10, "nombre", "asc")).withRel("todos"));
        if (dto.getTipo() != null) {
            resource.add(linkTo(methodOn(EstablecimientoController.class).listar(dto.getTipo(), 0, 10, "nombre", "asc")).withRel("mismo-tipo"));
        }
        return ResponseEntity.ok(resource);
    }

    @PostMapping
    public ResponseEntity<EstablecimientoResponseDTO> crear(@Valid @RequestBody EstablecimientoRequestDTO request) {
        EstablecimientoResponseDTO dto = service.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstablecimientoResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody EstablecimientoRequestDTO request) {
        EstablecimientoResponseDTO dto = service.actualizar(id, request);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EstablecimientoResponseDTO> actualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        EstablecimientoResponseDTO dto = service.actualizarParcial(id, updates);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminarLogico(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/busqueda-rapida")
    public ResponseEntity<List<EstablecimientoResponseDTO>> busquedaRapida(@RequestParam String nombreUbicacion) {
        List<EstablecimientoResponseDTO> resultados = service.buscarPorUbicacion(nombreUbicacion);
        return ResponseEntity.ok(resultados);
    }

    @GetMapping("/en-memoria")
    public ResponseEntity<Page<EstablecimientoResponseDTO>> listarEnMemoria(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "nombre") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Page<EstablecimientoResponseDTO> pageResult = service.listarEnMemoria(page, size, sortField, sortDir);
        return ResponseEntity.ok(pageResult);
    }
}