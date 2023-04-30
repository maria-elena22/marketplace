package com.fcul.marketplace.dto.encomenda;

import com.fcul.marketplace.dto.item.ItemDTO;
import com.fcul.marketplace.dto.utilizador.SimpleUtilizadorDTO;
import com.fcul.marketplace.model.enums.EstadoEncomenda;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class FullSubEncomendaDTO {

    private Integer idSubEncomenda;

    private SimpleUtilizadorDTO fornecedor;

    private SimpleEncomendaDTO encomenda;

    private Double preco;

    private EstadoEncomenda estadoEncomenda;

    private Date dataEncomenda;

    private List<ItemDTO> items;


}
