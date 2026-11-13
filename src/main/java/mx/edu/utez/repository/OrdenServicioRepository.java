package mx.edu.utez.repository;

import mx.edu.utez.model.EstadoOrden;
import mx.edu.utez.model.OrdenServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenServicioRepository extends JpaRepository<OrdenServicio, Long> {

    boolean existsByVehiculoIdAndEstadoIn(Long vehiculoId, List<EstadoOrden> estados);

    List<OrdenServicio> findByVehiculoIdAndEstadoIn(Long vehiculoId, List<EstadoOrden> estados);

    List<OrdenServicio> findByEstado(EstadoOrden estado);
}
