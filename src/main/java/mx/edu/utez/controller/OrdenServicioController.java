package mx.edu.utez.controller;

import jakarta.validation.Valid;
import mx.edu.utez.dto.ActualizarCostoRequest;
import mx.edu.utez.dto.CrearOrdenRequest;
import mx.edu.utez.model.EstadoOrden;
import mx.edu.utez.model.OrdenServicio;
import mx.edu.utez.service.OrdenServicioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordenes")
@CrossOrigin("*")
public class OrdenServicioController {

    private final OrdenServicioService servicio;

    public OrdenServicioController(OrdenServicioService servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    public ResponseEntity<OrdenServicio> crearOrden(@RequestBody @Valid CrearOrdenRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(servicio.crearOrden(request));
    }

    @PatchMapping("/{id}/iniciar")
    public ResponseEntity<OrdenServicio> iniciarReparacion(@PathVariable Long id) {
        return ResponseEntity.ok(servicio.iniciarReparacion(id));
    }

    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<OrdenServicio> finalizarOrden(@PathVariable Long id) {
        return ResponseEntity.ok(servicio.finalizarOrden(id));
    }

    @PatchMapping("/{id}/costo")
    public ResponseEntity<OrdenServicio> actualizarCosto(@PathVariable Long id, @RequestBody @Valid ActualizarCostoRequest request) {
        return ResponseEntity.ok(servicio.actualizarCosto(id, request));
    }

    @PatchMapping("/vehiculo/{vehiculoId}/cancelar")
    public ResponseEntity<List<OrdenServicio>> cancelarOrdenesPendientes(@PathVariable Long vehiculoId) {
        return ResponseEntity.ok(servicio.cancelarOrdenesPendientes(vehiculoId));
    }

    @GetMapping
    public ResponseEntity<List<OrdenServicio>> listarOrdenes() {
        return ResponseEntity.ok(servicio.listarOrdenes());
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<OrdenServicio>> listarOrdenesPorEstado(@PathVariable EstadoOrden estado) {
        return ResponseEntity.ok(servicio.listarOrdenesPorEstado(estado));
    }
}
