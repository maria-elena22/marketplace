package com.fcul.marketplace.dto.encomenda;

import com.fcul.marketplace.dto.item.ItemDTO;
import com.fcul.marketplace.dto.utilizador.SimpleUtilizadorDTO;
import lombok.Data;

import java.util.List;

@Data
public class SimpleSubEncomendaDTO {

    private SimpleUtilizadorDTO fornecedor;

    private List<ItemDTO> items;

}
