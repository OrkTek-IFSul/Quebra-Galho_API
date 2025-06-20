package com.orktek.quebragalho.controller.controller_views;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.orktek.quebragalho.dto.DenunciaDTO.CarregarDenunciaDTO;
import com.orktek.quebragalho.dto.DenunciaDTO.CriarDenunciaDTO;
import com.orktek.quebragalho.service.DenunciaService;

@RestController
@RequestMapping("/api/denuncia")
@Tag(name = "Operações relacionadas a denúncias", description = "Operações relacionadas a denúncias")
public class GerenciarDenunciasController {

        @Autowired
        private DenunciaService denunciaService;

        @PostMapping
        @Operation(summary = "Criar denuncia", description = "Cria uma nova denúncia")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Denuncia feita com sucesso"),
                        @ApiResponse(responseCode = "409", description = "Falha ao criar a denuncia")
        })
        public ResponseEntity<CarregarDenunciaDTO> CriarDenuncia(
                        @Parameter(description = "Informações do agendamento", required = true) @RequestBody CriarDenunciaDTO CriarDenunciaDTO) {
                
                CarregarDenunciaDTO denunciaRetorno = denunciaService.criarDenuncia(CriarDenunciaDTO);
                return ResponseEntity.status(HttpStatus.CREATED).body(denunciaRetorno);
        }
}
