package com.fcul.marketplace.controller.api;

import com.fcul.marketplace.model.Transporte;
import com.fcul.marketplace.service.ProdutoService;
import com.fcul.marketplace.service.TransporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transporte")
public class TransporteControllerAPI {

    @Autowired
    TransporteService transporteService;

    @GetMapping()
    public List<Transporte> getTransportes() {
        return transporteService.getTransportes();
    }

    @PostMapping()
    public Transporte insertTransporte(@RequestBody Transporte transporte) {
        return transporteService.addTransporte(transporte);
    }

    @DeleteMapping("/{id}")
    public void deleteTransporte(@PathVariable Integer id){
        transporteService.deleteTransporte(id);
    }

    @DeleteMapping()
    public void deleteTransportes(@RequestBody List<Integer> ids){
        transporteService.deleteTransporteBatch(ids);
    }
}
