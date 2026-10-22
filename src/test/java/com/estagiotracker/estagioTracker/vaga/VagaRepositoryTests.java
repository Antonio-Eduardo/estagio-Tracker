package com.estagiotracker.estagioTracker.vaga;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class VagaRepositoryTests {

    @Autowired
    private VagaRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void persisteVagaComStatusInicial() {
        Vaga vaga = novaVaga();
        LocalDateTime dataColeta = LocalDateTime.of(2026, 10, 22, 10, 30);
        vaga.setDataColeta(dataColeta);

        Long id = repository.saveAndFlush(vaga).getId();
        entityManager.clear();

        Vaga salva = repository.findById(id).orElseThrow();
        assertThat(salva.getTitulo()).isEqualTo(vaga.getTitulo());
        assertThat(salva.getEmpresa()).isEqualTo(vaga.getEmpresa());
        assertThat(salva.getUrl()).isEqualTo(vaga.getUrl());
        assertThat(salva.getDescricao()).isEqualTo(vaga.getDescricao());
        assertThat(salva.getStatus()).isEqualTo(StatusVaga.NOVA);
        assertThat(salva.getDataColeta()).isEqualTo(dataColeta);
    }

    @Test
    void rejeitaUrlDuplicada() {
        repository.saveAndFlush(novaVaga());

        assertThatThrownBy(() -> repository.saveAndFlush(novaVaga()))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    private Vaga novaVaga() {
        Vaga vaga = new Vaga();
        vaga.setTitulo("Estágio em desenvolvimento");
        vaga.setEmpresa("Empresa de teste");
        vaga.setUrl("https://example.com/vagas/1");
        vaga.setDescricao("Desenvolvimento de aplicações Java.");
        return vaga;
    }
}
