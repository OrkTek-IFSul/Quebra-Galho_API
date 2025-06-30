package com.orktek.quebragalho.dto.PrestadorDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "CriarPrestadorDTO", description = "DTO para criação de prestador")
public class CriarPrestadorUsuarioExistenteDTO {

    @Schema(description = "Descrição do prestador", example = "Especialista em encanamento residencial")
    private String descricao;
}