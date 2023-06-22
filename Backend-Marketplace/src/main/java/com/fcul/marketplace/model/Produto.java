package com.fcul.marketplace.model;

import com.fcul.marketplace.dto.categoria.PropriedadeDTO;
import com.fcul.marketplace.dto.categoria.SubCategoriaDTO;
import com.fcul.marketplace.dto.produto.FullProdutoDTO;
import com.fcul.marketplace.dto.produto.ProdutoFornecedorInfoDTO;
import com.fcul.marketplace.dto.uniProd.ProdutoUniProdDTO;
import com.fcul.marketplace.model.enums.IVA;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idProduto;

    private String nome;

    private String descricao;

    @Enumerated(EnumType.STRING)
    private IVA iva;

    @ManyToMany()
    @JoinTable(joinColumns = @JoinColumn(name = "id_produto"),
            inverseJoinColumns = @JoinColumn(name = "id_subcategoria"))
    private List<SubCategoria> subCategorias;

    @ManyToMany()
    @JoinTable(joinColumns = @JoinColumn(name = "id_produto"),
            inverseJoinColumns = @JoinColumn(name = "id_unidade"))
    private List<UniProd> uniProds;

    @ElementCollection
    @MapKeyColumn(name = "propriedade")
    @Column(name = "valor_propriedade")
    @CollectionTable(name = "produto_propriedades", joinColumns = {@JoinColumn(name = "produto_id",
            referencedColumnName = "idProduto")})
    private Map<Propriedade, String> propriedades;

    @ElementCollection
    private List<ProdutoFornecedorInfo> precoFornecedores;

    public void addPrecoFornecedor(ProdutoFornecedorInfo precoFornecedor) {
        if (precoFornecedores == null) {
            precoFornecedores = new ArrayList<>();
        }
        precoFornecedores.add(precoFornecedor);
    }

    public void removePrecoFornecedor(ProdutoFornecedorInfo precoFornecedor) {
        if (precoFornecedores != null) {
            precoFornecedores.remove(precoFornecedor);
        }
    }

    public void removePropriedades(Collection<Propriedade> propriedadesARemover) {
        for (Propriedade propriedade : propriedadesARemover) {
            propriedades.remove(propriedade);
        }
    }

    public Double getPreco(Fornecedor fornecedor) throws Exception {
        return this.precoFornecedores.stream().filter(precoFornecedores -> precoFornecedores.getFornecedor().equals(fornecedor)).findFirst().orElseThrow(Exception::new).getPreco();
    }

    public FullProdutoDTO convert(ModelMapper modelMapper) {
        FullProdutoDTO fullProdutoDTO = new FullProdutoDTO();
        fullProdutoDTO.setNome(this.getNome());
        fullProdutoDTO.setDescricao(this.getDescricao());
        fullProdutoDTO.setIva(this.getIva());
        fullProdutoDTO.setIdProduto(this.getIdProduto());
        fullProdutoDTO.setSubCategorias(this.getSubCategorias().stream().map(subCategoria -> modelMapper.map(subCategoria, SubCategoriaDTO.class)).toList());
        fullProdutoDTO.setPrecoFornecedores(this.getPrecoFornecedores().stream().map(precoFornecedores -> modelMapper.map(precoFornecedores, ProdutoFornecedorInfoDTO.class)).toList());
        Map<PropriedadeDTO, String> propriedadeDTOStringMap = new HashMap<>();
        this.getPropriedades().forEach((key, value) -> {
            propriedadeDTOStringMap.put(modelMapper.map(key, PropriedadeDTO.class), value);
        });
        fullProdutoDTO.setPropriedades(propriedadeDTOStringMap);
        List<ProdutoUniProdDTO> uniProds1 = new ArrayList<>();
        for (UniProd uniProd : this.getUniProds()) {
            ProdutoUniProdDTO uniProdDTO = new ProdutoUniProdDTO();
            uniProdDTO.setNomeUniProd(uniProd.getNomeUniProd());
            uniProdDTO.setIdUnidade(uniProd.getIdUnidade());
            Produto finalProduto = this;
            uniProdDTO.setStock(uniProd.getStocks().stream().filter(stock1 -> stock1.getProduto().equals(finalProduto)).findFirst().get().getQuantidade());
            uniProds1.add(uniProdDTO);
        }


        fullProdutoDTO.setUniProds(uniProds1);
        return fullProdutoDTO;
    }


}
