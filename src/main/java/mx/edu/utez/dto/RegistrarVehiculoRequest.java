package mx.edu.utez.dto;

import jakarta.validation.constraints.NotBlank;

public record RegistrarVehiculoRequest(
        @NotBlank(message = "La placa es obligatoria")
        String placa,

        @NotBlank(message = "La marca es obligatoria")
        String marca,

        @NotBlank(message = "El modelo es obligatorio")
        String modelo,

        @NotBlank(message = "El propietario es obligatorio")
        String propietario
) {
}
