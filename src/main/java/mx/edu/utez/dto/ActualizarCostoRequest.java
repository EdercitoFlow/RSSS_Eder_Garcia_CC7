package mx.edu.utez.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ActualizarCostoRequest(
        @NotNull(message = "El costo es obligatorio")
        @PositiveOrZero(message = "El costo debe ser mayor o igual a cero")
        Double costo
) {
}
