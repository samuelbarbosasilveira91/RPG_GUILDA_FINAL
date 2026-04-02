package br.rpgguilda.rpg_guilda.repository;

import br.rpgguilda.rpg_guilda.model.Aventureiro;
import br.rpgguilda.rpg_guilda.model.Classe;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AventureiroRepository {

    private List<Aventureiro> lista = new ArrayList<>();
    private Long contadorId = 1L;

    public AventureiroRepository() {
        for (int i = 0; i < 100; i++) {
            Aventureiro a = new Aventureiro();
            a.setId(contadorId++);
            a.setNome("Aventureiro " + i);
            a.setClasse(Classe.values()[i % Classe.values().length]);
            a.setNivel((i % 10) + 1);
            a.setAtivo(true);
            a.setCompanheiro(null);
            lista.add(a);
        }
    }

    public List<Aventureiro> listar() {
        return lista;
    }

    public Optional<Aventureiro> buscarPorId(Long id) {
        return lista.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }

    public Aventureiro salvar(Aventureiro a) {
        a.setId(contadorId++);
        lista.add(a);
        return a;
    }
}
