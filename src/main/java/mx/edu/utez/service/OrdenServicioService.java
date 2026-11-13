package mx.edu.utez.service;

import mx.edu.utez.dto.ActualizarCostoRequest;
import mx.edu.utez.dto.CrearOrdenRequest;
import mx.edu.utez.exception.CustomException;
import mx.edu.utez.model.EstadoOrden;
import mx.edu.utez.model.OrdenServicio;
import mx.edu.utez.model.Vehiculo;
import mx.edu.utez.repository.OrdenServicioRepository;
import mx.edu.utez.repository.VehiculoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdenServicioService {
    private final OrdenServicioRepository ordenRepository;

    private final VehiculoRepository vehiculoRepository;

    private final List<EstadoOrden> pendientes = List.of(EstadoOrden.RECIBIDA, EstadoOrden.EN_REPARACION);

    public OrdenServicioService(OrdenServicioRepository ordenRepository, VehiculoRepository vehiculoRepository) {

        this.ordenRepository = ordenRepository;
        this.vehiculoRepository = vehiculoRepository;
    }

    public OrdenServicio crearOrden(CrearOrdenRequest request) {
        Vehiculo vehiculo = buscarVehiculo(request.vehiculoId());

        if (!vehiculo.isActivo()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "El vehiculo esta inactivo y no puede recibir ordenes");
        }

        if (ordenRepository.existsByVehiculoIdAndEstadoIn(vehiculo.getId(), pendientes)) {

            throw new CustomException(HttpStatus.CONFLICT, "El vehiculo ya tiene una orden RECIBIDA o EN_REPARACION");
        }

        OrdenServicio orden = new OrdenServicio();
        orden.setDescripcion(request.descripcion());
        orden.setTipo(request.tipo());

        orden.setEstado(EstadoOrden.RECIBIDA);

        orden.setCosto(0);
        orden.setVehiculo(vehiculo);

        return ordenRepository.save(orden);
    }

    public OrdenServicio iniciarReparacion(Long ordenId) {
        OrdenServicio orden = buscarOrden(ordenId);

        if (orden.getEstado() != EstadoOrden.RECIBIDA) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Solo se pueden iniciar ordenes en estado RECIBIDA");
        }

        orden.setEstado(EstadoOrden.EN_REPARACION);
        return ordenRepository.save(orden);
    }

    public OrdenServicio finalizarOrden(Long ordenId) {
        OrdenServicio orden = buscarOrden(ordenId);

        if (orden.getEstado() != EstadoOrden.EN_REPARACION) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Solo se pueden finalizar ordenes en estado EN_REPARACION");
        }

        if (orden.getCosto() <= 0) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "El costo debe ser mayor a cero para finalizar la orden");
        }

        orden.setEstado(EstadoOrden.FINALIZADA);
        return ordenRepository.save(orden);
    }

    public OrdenServicio actualizarCosto(Long ordenId, ActualizarCostoRequest request) {
        OrdenServicio orden = buscarOrden(ordenId);

        if (!pendientes.contains(orden.getEstado())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Solo se puede modificar el costo de ordenes RECIBIDA o EN_REPARACION");
        }

        orden.setCosto(request.costo());
        return ordenRepository.save(orden);
    }

    public List<OrdenServicio> cancelarOrdenesPendientes(Long vehiculoId) {

        buscarVehiculo(vehiculoId);

        List<OrdenServicio> ordenes = ordenRepository.findByVehiculoIdAndEstadoIn(vehiculoId, pendientes);
        for (OrdenServicio orden : ordenes) {

            orden.setEstado(EstadoOrden.CANCELADA);
        }

        return ordenRepository.saveAll(ordenes);
    }

    public List<OrdenServicio> listarOrdenes() {
        return ordenRepository.findAll();



    }

    private Vehiculo buscarVehiculo(Long id) {
        return vehiculoRepository.findById(id)


                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "El vehiculo con id " + id + " no existe"));
    }

    private OrdenServicio buscarOrden(Long id) {
        return ordenRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "La orden con id " + id + " no existe"));
    }
}
