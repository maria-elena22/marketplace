package com.fcul.marketplace.repository;

import com.fcul.marketplace.model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Integer> {

    List<Notificacao> findByDestinatarioIdUtilizadorAndEntregueFalse(Integer userId);
}
