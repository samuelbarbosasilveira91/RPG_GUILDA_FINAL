package br.rpgguilda.rpg_guilda.exception;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SolicitacaoInvalidaException.class)
    public ResponseEntity<ErroResponse> tratarSolicitacaoInvalida(SolicitacaoInvalidaException ex) {
        ErroResponse erro = new ErroResponse();
        erro.setMensagem("Solicitação inválida");
        erro.setDetalhes(ex.getDetalhes());
        return ResponseEntity.badRequest().body(erro);
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> tratarNaoEncontrado(RecursoNaoEncontradoException ex) {
        ErroResponse erro = new ErroResponse();
        erro.setMensagem("Recurso não encontrado");
        List<String> detalhes = new ArrayList<>();
        detalhes.add(ex.getMessage());
        erro.setDetalhes(detalhes);
        return ResponseEntity.status(404).body(erro);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> tratarJsonInvalido(HttpMessageNotReadableException ex) {
        ErroResponse erro = new ErroResponse();
        erro.setMensagem("Solicitação inválida");

        List<String> detalhes = new ArrayList<>();
        String mensagem = ex.getMostSpecificCause().getMessage();

        if (mensagem != null && mensagem.contains("Classe")) {
            detalhes.add("classe inválida");
        } else if (mensagem != null && mensagem.contains("Especie")) {
            detalhes.add("especie inválida");
        } else {
            detalhes.add("corpo da requisição inválido");
        }

        erro.setDetalhes(detalhes);
        return ResponseEntity.badRequest().body(erro);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> tratarGenerico(Exception ex) {
        ErroResponse erro = new ErroResponse();
        erro.setMensagem("Solicitação inválida");

        List<String> detalhes = new ArrayList<>();
        detalhes.add("erro interno na solicitação");

        erro.setDetalhes(detalhes);
        return ResponseEntity.badRequest().body(erro);
    }
}
