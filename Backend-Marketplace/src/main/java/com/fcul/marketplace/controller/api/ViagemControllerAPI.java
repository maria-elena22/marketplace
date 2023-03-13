package com.fcul.marketplace.controller.api;

import com.fcul.marketplace.model.Viagem;
import com.fcul.marketplace.service.ViagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/viagem")
public class ViagemControllerAPI {

    @Autowired
    ViagemService viagemService;

    @GetMapping()
    public List<Viagem> getViagens() {
        return viagemService.getViagens();
    }

    @PostMapping()
    public Viagem insertViagem(@RequestBody Viagem viagem) {
        return viagemService.addViagem(viagem);
    }

    @DeleteMapping("/{id}")
    public void deleteViagem(@PathVariable Integer id){
        viagemService.deleteViagem(id);
    }

    @DeleteMapping()
    public void deleteViagem(@RequestBody List<Integer> ids){
        viagemService.deleteViagemBatch(ids);
    }

}
