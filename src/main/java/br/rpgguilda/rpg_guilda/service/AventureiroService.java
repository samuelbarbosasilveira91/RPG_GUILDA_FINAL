package br.rpgguilda.rpg_guilda.service;

import br.rpgguilda.rpg_guilda.dto.AtualizarAventureiroDTO;
import br.rpgguilda.rpg_guilda.dto.AventureiroResumoDTO;
import br.rpgguilda.rpg_guilda.dto.RegistrarAventureiroDTO;
import br.rpgguilda.rpg_guilda.exception.RecursoNaoEncontradoException;
import br.rpgguilda.rpg_guilda.exception.SolicitacaoInvalidaException;
import br.rpgguilda.rpg_guilda.model.Aventureiro;
import br.rpgguilda.rpg_guilda.model.Classe;
import br.rpgguilda.rpg_guilda.model.Companheiro;
import br.rpgguilda.rpg_guilda.repository.AventureiroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AventureiroService {

    @Autowired
    private AventureiroRepository repo;

    public Aventureiro registrar(RegistrarAventureiroDTO dto) {
        List<String> erros = new ArrayList<>();

        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            erros.add("nome obrigatório");
        }

        if (dto.getClasse() == null) {
            erros.add("classe inválida");
        }

        if (dto.getNivel() < 1) {
            erros.add("nivel deve ser maior ou igual a 1");
        }

        if (!erros.isEmpty()) {
            throw new SolicitacaoInvalidaException(erros);
        }

        Aventureiro a = new Aventureiro();
        a.setNome(dto.getNome());
        a.setClasse(dto.getClasse());
        a.setNivel(dto.getNivel());
        a.setAtivo(true);
        a.setCompanheiro(null);

        return repo.salvar(a);
    }

    public List<Aventureiro> listarPaginado(int page, int size, Classe classe, Boolean ativo, Integer nivelMinimo) {
        List<String> erros = new ArrayList<>();

        if (page < 0) {
            erros.add("page não pode ser negativa");
        }

        if (size < 1 || size > 50) {
            erros.add("size deve estar entre 1 e 50");
        }

        if (nivelMinimo != null && nivelMinimo < 1) {
            erros.add("nivelMinimo deve ser maior ou igual a 1");
        }

        if (!erros.isEmpty()) {
            throw new SolicitacaoInvalidaException(erros);
        }

        List<Aventureiro> filtrados = repo.listar().stream()
                .filter(a -> classe == null || a.getClasse() == classe)
                .filter(a -> ativo == null || a.isAtivo() == ativo)
                .filter(a -> nivelMinimo == null || a.getNivel() >= nivelMinimo)
                .sorted(Comparator.comparing(Aventureiro::getId))
                .collect(Collectors.toList());

        int inicio = page * size;
        int fim = Math.min(inicio + size, filtrados.size());

        if (inicio >= filtrados.size()) {
            return new ArrayList<>();
        }

        return filtrados.subList(inicio, fim);
    }

    public List<AventureiroResumoDTO> listarResumo(int page, int size, Classe classe, Boolean ativo, Integer nivelMinimo) {
        List<Aventureiro> lista = listarPaginado(page, size, classe, ativo, nivelMinimo);

        List<AventureiroResumoDTO> resultado = new ArrayList<>();

        for (Aventureiro a : lista) {
            resultado.add(new AventureiroResumoDTO(a));
        }

        return resultado;
    }

    public int totalFiltrado(Classe classe, Boolean ativo, Integer nivelMinimo) {
        return (int) repo.listar().stream()
                .filter(a -> classe == null || a.getClasse() == classe)
                .filter(a -> ativo == null || a.isAtivo() == ativo)
                .filter(a -> nivelMinimo == null || a.getNivel() >= nivelMinimo)
                .count();
    }

    public Aventureiro buscarPorId(Long id) {
        return repo.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aventureiro não encontrado"));
    }

    public Aventureiro atualizar(Long id, AtualizarAventureiroDTO dto) {
        Aventureiro existente = buscarPorId(id);

        List<String> erros = new ArrayList<>();

        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            erros.add("nome obrigatório");
        }

        if (dto.getClasse() == null) {
            erros.add("classe inválida");
        }

        if (dto.getNivel() < 1) {
            erros.add("nivel deve ser maior ou igual a 1");
        }

        if (!erros.isEmpty()) {
            throw new SolicitacaoInvalidaException(erros);
        }

        existente.setNome(dto.getNome());
        existente.setClasse(dto.getClasse());
        existente.setNivel(dto.getNivel());

        return existente;
    }

    public Aventureiro desativar(Long id) {
        Aventureiro aventureiro = buscarPorId(id);
        aventureiro.setAtivo(false);
        return aventureiro;
    }

    public Aventureiro reativar(Long id) {
        Aventureiro aventureiro = buscarPorId(id);
        aventureiro.setAtivo(true);
        return aventureiro;
    }

    public Aventureiro definirOuSubstituirCompanheiro(Long id, Companheiro companheiro) {
        Aventureiro aventureiro = buscarPorId(id);

        List<String> erros = new ArrayList<>();

        if (companheiro == null) {
            erros.add("companheiro obrigatório");
        } else {
            if (companheiro.getNome() == null || companheiro.getNome().trim().isEmpty()) {
                erros.add("nome do companheiro obrigatório");
            }

            if (companheiro.getEspecie() == null) {
                erros.add("especie inválida");
            }

            if (companheiro.getLealdade() < 0 || companheiro.getLealdade() > 100) {
                erros.add("lealdade deve estar entre 0 e 100");
            }
        }

        if (!erros.isEmpty()) {
            throw new SolicitacaoInvalidaException(erros);
        }

        aventureiro.setCompanheiro(companheiro);
        return aventureiro;
    }

    public Aventureiro removerCompanheiro(Long id) {
        Aventureiro aventureiro = buscarPorId(id);
        aventureiro.setCompanheiro(null);
        return aventureiro;
    }
}
