package mx.edu.utez.controller;

import jakarta.validation.Valid;
import mx.edu.utez.dto.RegistrarVehiculoRequest;
import mx.edu.utez.model.Vehiculo;
import mx.edu.utez.service.VehiculoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehiculos")
@CrossOrigin("*")
public class VehiculoController {

    private final VehiculoService servicio;

    public VehiculoController(VehiculoService servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    public ResponseEntity<Vehiculo> registrarVehiculo(@RequestBody @Valid RegistrarVehiculoRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(servicio.registrarVehiculo(request));
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Vehiculo> desactivarVehiculo(@PathVariable Long id) {
        return ResponseEntity.ok(servicio.desactivarVehiculo(id));
    }
}
