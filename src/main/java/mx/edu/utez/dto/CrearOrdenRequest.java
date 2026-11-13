package mx.edu.utez.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import mx.edu.utez.model.TipoServicio;

public record CrearOrdenRequest(
        @NotNull(message = "El id del vehiculo es obligatorio")
        Long vehiculoId,

        @NotBlank(message = "La descripcion no puede estar vacia")
        String descripcion,

        @NotNull(message = "El tipo de servicio es obligatorio")
        TipoServicio tipo
) {
}
