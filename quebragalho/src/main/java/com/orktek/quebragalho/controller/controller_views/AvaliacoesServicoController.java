package com.orktek.quebragalho.controller.controller_views;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import com.orktek.quebragalho.service.RespostaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.orktek.quebragalho.dto.AvaliacaoDTO.CarregarAvaliacaoDTO;
import com.orktek.quebragalho.dto.AvaliacaoDTO.CriarAvaliacaoDTO;
import com.orktek.quebragalho.dto.RespostaDTO.CarregarRespostaDTO;
import com.orktek.quebragalho.dto.RespostaDTO.CriarRespostaDTO;
import com.orktek.quebragalho.model.Resposta;
import com.orktek.quebragalho.service.AvaliacaoService;

@RestController
@RequestMapping("/api/avaliacoesservico")
@Tag(name = "Tela de avaliacoes de um servico", description = "Operações relacionadas à Tela de avaliacoes de um servico")
public class AvaliacoesServicoController {

    @Autowired
    private RespostaService respostaService;

    @Autowired
    private AvaliacaoService avaliacaoService;

    AvaliacoesServicoController(RespostaService respostaService) {
        this.respostaService = respostaService;
    }

    @GetMapping("/{servicoId}")
    @Operation(summary = "Carrega avaliacoes de um servico", description = "Carrega avaliacoes de um servico")
    public ResponseEntity<List<CarregarAvaliacaoDTO>> ListaAvaliacoesDoServico(
            @Parameter(description = "Id do Servico") @PathVariable Long servicoId) {

        List<CarregarAvaliacaoDTO> avaliacaoDTOs = avaliacaoService
                .listarPorServico(servicoId)
                .stream().map(CarregarAvaliacaoDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(avaliacaoDTOs);
    }

    @PostMapping("/{avaliacaoId}")
    @Operation(summary = "Responde uma avaliacao(SÓ O PRESTADOR PODE RESPONDER)", description = "Permite que o usuário responda uma avaliacao pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resposta criada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CriarAvaliacaoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Avaliacao não encontrado")
    })
    public ResponseEntity<CarregarRespostaDTO> criarResposta(
            @Parameter(description = "ID da avaliacao que será respondida", required = true, example = "1") @PathVariable Long avaliacaoId,
            @Parameter(description = "Informações da resposta", required = true) @RequestBody CriarRespostaDTO respostaDTO) {
        Resposta resposta = new Resposta();

        resposta.setAvaliacao(avaliacaoService.buscarPorId(avaliacaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));

        resposta.setResposta(respostaDTO.getResposta());
        resposta.setData(LocalDate.now());

        respostaService.criarResposta(resposta);

        CarregarRespostaDTO retornoRespostaDTO = CarregarRespostaDTO.fromEntity(resposta);

        return ResponseEntity.ok(retornoRespostaDTO);
    }

    
    @PutMapping("/{avaliacaoId}")
    @Operation(summary = "Altera uma avaliacao", description = "Permite que o usuário altere uma avaliacao pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "avaliacao alterada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CriarAvaliacaoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "avaliacao não encontrado")
    })
    public ResponseEntity<CarregarAvaliacaoDTO> alterarAvaliacao(
            @Parameter(description = "ID da avaliacao que será alterada", required = true, example = "1") @PathVariable Long avaliacaoId,
            @Parameter(description = "Novas informações da resposta", required = true) @RequestBody String novaAvaliacao) {

        CarregarAvaliacaoDTO retornoAvaliacaoDTO = CarregarAvaliacaoDTO.fromEntity(avaliacaoService.atualizarAvaliacao(avaliacaoId, novaAvaliacao));
        return ResponseEntity.ok(retornoAvaliacaoDTO);
    }

    @PutMapping("/{respostaId}")
    @Operation(summary = "Altera uma resposta(SÓ O PRESTADOR PODE RESPONDER)", description = "Permite que o usuário altere uma resposta pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resposta alterada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CriarAvaliacaoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Resposta não encontrado")
    })
    public ResponseEntity<CarregarRespostaDTO> alterarResposta(
            @Parameter(description = "ID da resposta que será alterada", required = true, example = "1") @PathVariable Long respostaId,
            @Parameter(description = "Novas informações da resposta", required = true) @RequestBody String novaResposta) {

        CarregarRespostaDTO retornoRespostaDTO = CarregarRespostaDTO.fromEntity(respostaService.atualizarResposta(respostaId, novaResposta));
        return ResponseEntity.ok(retornoRespostaDTO);
    }

    @DeleteMapping("/{avaliacaoId}")
    @Operation(summary = "Deleta uma avaliacao", description = "Permite que o usuário delete uma avaliacao pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "avaliacao deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "avaliacao não encontrado")
    })
    public ResponseEntity<Void> deletarAvaliacao(
            @Parameter(description = "ID da avaliacao que será deletada", required = true, example = "1") @PathVariable Long avaliacaoId) {

        avaliacaoService.deletarAvaliacao(avaliacaoId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{respostaId}")
    @Operation(summary = "Deleta uma resposta(SÓ O PRESTADOR PODE RESPONDER)", description = "Permite que o usuário delete uma resposta pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Resposta deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Resposta não encontrado")
    })
    public ResponseEntity<Void> deletarResposta(
            @Parameter(description = "ID da resposta que será deletada", required = true, example = "1") @PathVariable Long respostaId) {

        respostaService.deletarResposta(respostaId);
        return ResponseEntity.noContent().build();
    }
}
