package com.orktek.quebragalho.controller.controller_views;

import com.orktek.quebragalho.model.Apelo;
import com.orktek.quebragalho.model.Denuncia;
import com.orktek.quebragalho.model.Usuario;
import com.orktek.quebragalho.service.ApeloService;
import com.orktek.quebragalho.service.DenunciaService;
import com.orktek.quebragalho.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para moderação
 */
@RestController
@RequestMapping("/api/moderacao")
@Tag(name = "Funcionalidades da moderação", description = "Operações relacionadas à moderação")
public class ModeracaoController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private DenunciaService denunciaService;

    @Autowired
    private ApeloService apeloService;

    @PutMapping("tornarModerador/{idUsuario}")
    @Operation(summary = "Tornar um usuario moderador", description = "Dar a um usuario o status de moderador")
    public ResponseEntity<String> TornarModerador(@PathVariable Long idUsuario) {

        String retorno = usuarioService.tornarModerador(idUsuario);

        return ResponseEntity.ok(retorno);
    }

    @PutMapping("removerModerador/{idUsuario}")
    @Operation(summary = "Remover moderador", description = "Revogar o status de moderador de um usuario")
    public ResponseEntity<String> RemoverModerador(@PathVariable Long idUsuario) {

        String retorno = usuarioService.removerModerador(idUsuario);

        return ResponseEntity.ok(retorno);
    }

    @PutMapping("aceitarDenuncia/{idDenuncia}")
    @Operation(summary = "Aceitar uma denúncia", description = "Aceita uma denúncia e ")
    public ResponseEntity<String> AceitarDenuncia(@PathVariable Long idDenuncia) {
        // Busca a denúncia pelo ID
        Denuncia denuncia = denunciaService.buscarPorId(idDenuncia)
                .orElseThrow(() -> new RuntimeException("Denúncia não encontrada"));

        // Aceita a denúncia
        denunciaService.atualizarStatusDenuncia(idDenuncia, true);

        // Busca o usuário denunciado
        Usuario usuario = denuncia.getDenunciado();

        // Adiciona um strike ao usuário denunciado
        usuarioService.adicionarStrike(usuario.getId());

        return ResponseEntity.ok("Denuncia aceita com sucesso");
    }

    @PutMapping("recusarDenuncia/{idDenuncia}")
    @Operation(summary = "Recusar uma denúncia", description = "Recusa uma denúncia")
    public ResponseEntity<String> RecusarDenuncia(@PathVariable Long idDenuncia) {

        // Recusa a denúncia
        denunciaService.atualizarStatusDenuncia(idDenuncia, false);

        return ResponseEntity.ok("Denuncia recusada com sucesso");
    }

    @PutMapping("aceitarApelo/{idApelo}")
    @Operation(summary = "Aceitar um apelo", description = "Aceita um apelo de um usuário")
    public ResponseEntity<String> AceitarApelo(@PathVariable Long idApelo) {
        // Aceita o apelo
        apeloService.atualizarStatusApelo(idApelo, true);

        // Busca o usuário do apelo
        Apelo apelo = apeloService.buscarPorId(idApelo)
                .orElseThrow(() -> new RuntimeException("Apelo não encontrado"));
        Usuario usuario = apelo.getDenuncia().getDenunciado();

        // Remove o strike do usuário
        usuarioService.removerStrike(usuario.getId());

        return ResponseEntity.ok("Apelo aceito com sucesso");
    }

    @PutMapping("recusarApelo/{idApelo}")
    @Operation(summary = "Recusar um apelo", description = "Recusa um apelo de um usuário")
    public ResponseEntity<String> RecusarApelo(@PathVariable Long idApelo) {

        // Recusa o apelo
        apeloService.atualizarStatusApelo(idApelo, false);

        return ResponseEntity.ok("Apelo recusado com sucesso");
    }
}
