package com.fcul.marketplace.service;

import com.fcul.marketplace.exceptions.ForbiddenActionException;
import com.fcul.marketplace.model.*;
import com.fcul.marketplace.repository.CategoriaRepository;
import com.fcul.marketplace.repository.NotificacaoRepository;
import com.fcul.marketplace.repository.PropriedadeRepository;
import com.fcul.marketplace.repository.SubCategoriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificacaoServiceTest {

    @Mock
    NotificacaoRepository notificacaoRepository;

    @Mock
    UtilizadorService utilizadorService;

    @Mock
    EncomendaService encomendaService;

    @Mock
    ViagemService viagemService;

    @InjectMocks
    NotificacaoService notificacaoService;

    @Test
    public void testGetNotificacoes() {
        Utilizador utilizador = new Utilizador();
        utilizador.setIdUtilizador(1);

        List<Notificacao> notificacoes = new ArrayList<>();
        Notificacao notificacao = new Notificacao();
        notificacoes.add(notificacao);

        when(utilizadorService.getUtilizadorByEmail(anyString())).thenReturn(utilizador);
        when(notificacaoRepository.findByDestinatarioIdUtilizadorAndEntregueFalse(anyInt())).thenReturn(notificacoes);

        assertEquals(notificacoes,notificacaoService.getNotificacoes("test@test.com"));
    }

    @Test
    public void testAddNotificacao() {
        Notificacao notificacao = new Notificacao();

        when(notificacaoRepository.save(any())).thenReturn(notificacao);

        assertEquals(notificacao,notificacaoService.insertNotificacao(null,null,null,null,null));
    }

    
}
