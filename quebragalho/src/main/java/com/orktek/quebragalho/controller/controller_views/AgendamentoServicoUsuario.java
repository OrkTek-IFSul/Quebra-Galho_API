package com.orktek.quebragalho.controller.controller_views;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.orktek.quebragalho.dto.AgendamentoDTO.AgendamentoRetornoDTO;
import com.orktek.quebragalho.dto.AgendamentoDTO.CriarAgendamentoDTO;
import com.orktek.quebragalho.model.Agendamento;
import com.orktek.quebragalho.model.Prestador;
import com.orktek.quebragalho.model.Servico;
import com.orktek.quebragalho.model.Usuario;
import com.orktek.quebragalho.service.AgendamentoService;
import com.orktek.quebragalho.service.DisponibilidadeService;
import com.orktek.quebragalho.service.PrestadorService;
import com.orktek.quebragalho.service.ServicoService;
import com.orktek.quebragalho.service.UsuarioService;

@RestController
@RequestMapping("/api/usuario/homepage/agendamento")
@Tag(name = "Tela de pedidos de agendamentos do usuario", description = "Operações relacionadas à Tela de pedidos de agendamentos do usuario")
public class AgendamentoServicoUsuario {

        @Autowired
        private UsuarioService usuarioService;

        @Autowired
        private DisponibilidadeService disponibilidadeService;

        @Autowired
        private ServicoService servicoService;

        @Autowired
        private PrestadorService prestadorService;

        @Autowired
        private AgendamentoService agendamentoService;
        
        @Operation(
            summary = "Carrega Horários Disponíveis",
            description = "Carrega horários disponíveis do prestador para uma data",
            parameters = {
                @Parameter(
                    name = "servicoId",
                    description = "ID do serviço para o qual se deseja consultar os horários disponíveis.",
                    example = "2"
                ),
                @Parameter(
                    name = "data",
                    description = "Data desejada para o agendamento (formato ISO: yyyy-MM-dd).",
                    example = "2025-06-16"
                )
            }
        )
        @GetMapping("/{servicoId}/horarios-disponiveis")
        public ResponseEntity<List<LocalDateTime>> getDisponibilidade(
            @PathVariable Long servicoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {

        List<LocalDateTime> horarios = disponibilidadeService.getHorariosDisponiveis(servicoId, data);
        return ResponseEntity.ok(horarios);
    }

        @PostMapping
        @Operation(summary = "Cadastrar Agendamento", description = "Cadastra um novo Agendamento e retorna os dados do agendamento")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Agendamento cadastrado com sucesso"),
                        @ApiResponse(responseCode = "409", description = "Falha ao cadastrar o agendamento")
        })
        public ResponseEntity<AgendamentoRetornoDTO> CadastrarAgendamento(
                        @Parameter(description = "Informações do agendamento", required = true) @RequestBody CriarAgendamentoDTO criarAgendamentoDTO) {

                // CriarAgendamentoDTO agendamentoDTO = new CriarAgendamentoDTO();
                // agendamentoDTO.setId_usuario(id_usuario);
                // agendamentoDTO.setId_servico(id_servico);

                // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd
                // HH:mm:ss");
                // LocalDateTime horarioCorreto = LocalDateTime.parse(horario, formatter);
                // agendamentoDTO.setHorario(horarioCorreto);

                agendamentoService.criarAgendamento(criarAgendamentoDTO);

                Usuario usuario = usuarioService.buscarPorId(criarAgendamentoDTO.getId_usuario())
                                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
                Servico servico = servicoService.buscarPorId(criarAgendamentoDTO.getId_servico())
                                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
                Agendamento agendamento = agendamentoService.buscarPorId(criarAgendamentoDTO.getId_usuario())
                                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));
                Prestador prestador = prestadorService.buscarPorId(servico.getPrestador().getId())
                                .orElseThrow(() -> new RuntimeException("Prestador não encontrado"));

                AgendamentoRetornoDTO agendamentoRetornoDTO = new AgendamentoRetornoDTO();
                agendamentoRetornoDTO.setUsuario(usuario.getNome());
                agendamentoRetornoDTO.setPrestador(prestador.getUsuario().getNome());
                agendamentoRetornoDTO.setServico(servico.getNome());
                agendamentoRetornoDTO.setDescricao_servico(servico.getDescricao());
                agendamentoRetornoDTO.setPreco_servico(servico.getPreco());
                agendamentoRetornoDTO.setHorario(agendamento.getDataHora());

                System.out.println("TESTE TESTE" + agendamentoRetornoDTO.toString());
                return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoRetornoDTO);
        }

        @PutMapping("/finalizar/{agendamentoId}")
        @Operation(summary = "Finalizar Agendamento", description = "Finaliza um agendamento")
        
        public ResponseEntity<String> FinalizarAgendamento(
                        @Parameter(description = "id do agendamento", required = true) @PathVariable Long agendamentoId) {

                Agendamento agendamento = agendamentoService.buscarPorId(agendamentoId)
                                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));
  
                agendamentoService.atualizarStatusAgendamento(agendamentoId, true);

                
                return ResponseEntity.status(HttpStatus.CREATED).body("Agendamento "+agendamento.getId()+" finalizado com sucesso");
        }

}
