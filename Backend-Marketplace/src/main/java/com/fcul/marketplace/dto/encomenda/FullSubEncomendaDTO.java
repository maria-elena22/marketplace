package com.fcul.marketplace.dto.encomenda;

import com.fcul.marketplace.model.Encomenda;
import com.fcul.marketplace.model.Fornecedor;
import com.fcul.marketplace.model.Item;
import com.fcul.marketplace.model.enums.EstadoEncomenda;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.sql.Date;
import java.util.List;

@Data
public class FullSubEncomendaDTO {

    private Integer idSubEncomenda;

    private Fornecedor fornecedor;

    private Double preco;

    private EstadoEncomenda estadoEncomenda;

    private Date dataEncomenda;

    private List<SimpleItemDTO> items;


}
