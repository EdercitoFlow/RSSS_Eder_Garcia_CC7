package mx.edu.utez.service;

import mx.edu.utez.dto.RegistrarVehiculoRequest;
import mx.edu.utez.exception.CustomException;
import mx.edu.utez.model.EstadoOrden;
import mx.edu.utez.model.Vehiculo;
import mx.edu.utez.repository.OrdenServicioRepository;
import mx.edu.utez.repository.VehiculoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehiculoService {
    private final VehiculoRepository vehiculoRepository;
    private final OrdenServicioRepository ordenRepository;

    public VehiculoService(VehiculoRepository vehiculoRepository, OrdenServicioRepository ordenRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.ordenRepository = ordenRepository;
    }

    // Servicio: registrarVehiculo
    public Vehiculo registrarVehiculo(RegistrarVehiculoRequest request) {
        if (vehiculoRepository.existsByPlaca(request.placa())) {
            throw new CustomException(HttpStatus.CONFLICT, "Ya existe un vehiculo con la placa " + request.placa());
        }

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca(request.placa());
        vehiculo.setMarca(request.marca());
        vehiculo.setModelo(request.modelo());
        vehiculo.setPropietario(request.propietario());
        vehiculo.setActivo(true);

        return vehiculoRepository.save(vehiculo);
    }

    // Servicio: desactivarVehiculo
    public Vehiculo desactivarVehiculo(Long vehiculoId) {
        Vehiculo vehiculo = buscarVehiculo(vehiculoId);

        List<EstadoOrden> pendientes = List.of(EstadoOrden.RECIBIDA, EstadoOrden.EN_REPARACION);
        if (ordenRepository.existsByVehiculoIdAndEstadoIn(vehiculoId, pendientes)) {
            throw new CustomException(HttpStatus.CONFLICT, "No se puede desactivar un vehiculo con ordenes RECIBIDA o EN_REPARACION");
        }

        vehiculo.setActivo(false);
        return vehiculoRepository.save(vehiculo);
    }

    // Servicio: listarVehiculosActivos
    public List<Vehiculo> listarVehiculosActivos() {
        return vehiculoRepository.findByActivoTrue();
    }

    private Vehiculo buscarVehiculo(Long id) {
        return vehiculoRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "El vehiculo con id " + id + " no existe"));
    }
}
