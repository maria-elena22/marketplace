package com.fcul.marketplace.controller.api;

import com.fcul.marketplace.dto.UtilizadorDTO;
import com.fcul.marketplace.model.Consumidor;
import com.fcul.marketplace.model.Fornecedor;
import com.fcul.marketplace.model.Utilizador;
import com.fcul.marketplace.service.UtilizadorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.events.Event;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/utilizador")
public class UtilizadorControllerAPI {

    @Autowired
    UtilizadorService utilizadorService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/consumidor")
    public List<UtilizadorDTO> getConsumidores() {

        List<Consumidor> consumidores = utilizadorService.getConsumidores();

        List<UtilizadorDTO> utilizadorDTOS = consumidores
                .stream().map(consumidor -> modelMapper.map(consumidor,UtilizadorDTO.class)).collect(Collectors.toList());

        return utilizadorDTOS;
    }

    @GetMapping("/fornecedor")
    public List<UtilizadorDTO> getFornecedores() {
        List<Fornecedor> fornecedores = utilizadorService.getFornecedores();

        List<UtilizadorDTO> utilizadorDTOS = fornecedores
                .stream().map(fornecedor -> modelMapper.map(fornecedor,UtilizadorDTO.class)).collect(Collectors.toList());

        return utilizadorDTOS;
    }

    @GetMapping("/consumidor/{id}")
    public UtilizadorDTO getConsumidorByID(@PathVariable Integer id) {
        return modelMapper.map(utilizadorService.getConsumidorByID(id),UtilizadorDTO.class);
    }

    @GetMapping("/fornecedor/{id}")
    public UtilizadorDTO getFornecedorByID(@PathVariable Integer id) {
        return modelMapper.map(utilizadorService.getFornecedorByID(id),UtilizadorDTO.class);
    }

    @PostMapping("/consumidor")
    public UtilizadorDTO insertConsumidor(@RequestBody UtilizadorDTO utilizadorDTO) {
        Consumidor consumidor = modelMapper.map(utilizadorDTO, Consumidor.class);
        consumidor = utilizadorService.addConsumidor(consumidor);
        return modelMapper.map(consumidor,UtilizadorDTO.class);
    }

    @PostMapping("/fornecedor")
    public UtilizadorDTO insertFornecedor(@RequestBody UtilizadorDTO utilizadorDTO) {
        Fornecedor fornecedor = modelMapper.map(utilizadorDTO, Fornecedor.class);
        fornecedor = utilizadorService.addFornecedor(fornecedor);
        return modelMapper.map(fornecedor,UtilizadorDTO.class);
    }


    @PutMapping("/consumidor/{id}")
    public UtilizadorDTO updateConsumidor(@PathVariable Integer id,@RequestBody UtilizadorDTO utilizadorDTO){
        Consumidor consumidor = modelMapper.map(utilizadorDTO, Consumidor.class);
        consumidor = utilizadorService.updateConsumidor(id,consumidor);
        return modelMapper.map(consumidor,UtilizadorDTO.class);


    }

    @PutMapping("/fornecedor/{id}")
    public UtilizadorDTO updateFornecedor(@PathVariable Integer id,@RequestBody UtilizadorDTO utilizadorDTO){
        Fornecedor fornecedor = modelMapper.map(utilizadorDTO, Fornecedor.class);
        fornecedor = utilizadorService.updateFornecedor(id,fornecedor);
        return modelMapper.map(fornecedor,UtilizadorDTO.class);


    }


    @DeleteMapping("/consumidor/{id}")
    public void deleteConsumidor(@PathVariable Integer id){
        utilizadorService.deleteConsumidor(id);
    }

    @DeleteMapping("/fornecedor/{id}")
    public void deleteFornecedor(@PathVariable Integer id){
        utilizadorService.deleteFornecedor(id);
    }

    @DeleteMapping("/consumidor")
    public void deleteConsumidorBatch(@RequestBody List<Integer> ids){
        utilizadorService.deleteConsumidorBatch(ids);
    }

    @DeleteMapping("/fornecedor")
    public void deleteFornecedorBatch(@RequestBody List<Integer> ids){
        utilizadorService.deleteFornecedorBatch(ids);
    }


}
