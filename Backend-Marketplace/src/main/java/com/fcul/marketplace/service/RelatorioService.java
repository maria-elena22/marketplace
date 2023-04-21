package com.fcul.marketplace.service;

import com.fcul.marketplace.dto.relatorio.RelatorioPorDistanciasDTO;
import com.fcul.marketplace.dto.relatorio.RelatorioPorZonasDTO;
import com.fcul.marketplace.model.SubEncomenda;
import com.fcul.marketplace.model.Utilizador;
import com.fcul.marketplace.model.utils.Coordinate;
import com.fcul.marketplace.utils.DistanceCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

    @Autowired
    EncomendaService encomendaService;

    public RelatorioPorZonasDTO generateRelatorioZonas(String emailFromAuthHeader, String roleFromAuthHeader) {
        List<Utilizador> utilizadores;
        if (roleFromAuthHeader == "ADMIN") {
            List<SubEncomenda> subEncomendas = encomendaService.getAllSubEncomendas();
            utilizadores = subEncomendas.stream().map(subEncomenda -> subEncomenda.getFornecedor()).collect(Collectors.toList());
        } else if (roleFromAuthHeader == "FORNECEDOR") {
            List<SubEncomenda> subEncomendas = encomendaService.getSubEncomendasByFornecedorEmail(emailFromAuthHeader);
            utilizadores = subEncomendas.stream().map(subEncomenda -> subEncomenda.getEncomenda().getConsumidor()).collect(Collectors.toList());
        } else {
            List<SubEncomenda> subEncomendas = encomendaService.getSubEncomendasByConsumidorEmail(emailFromAuthHeader);
            utilizadores = subEncomendas.stream().map(subEncomenda -> subEncomenda.getFornecedor()).collect(Collectors.toList());
        }

        RelatorioPorZonasDTO relatorioPorZonasDTO = new RelatorioPorZonasDTO();
        relatorioPorZonasDTO.setMapEncomendasDistrito(utilizadores.stream()
                .collect(Collectors.groupingBy(Utilizador::getDistrito, Collectors.counting())));
        relatorioPorZonasDTO.setMapEncomendasFreguesias(utilizadores.stream()
                .collect(Collectors.groupingBy(Utilizador::getFreguesia, Collectors.counting())));
        relatorioPorZonasDTO.setMapEncomendasMunicipio(utilizadores.stream()
                .collect(Collectors.groupingBy(Utilizador::getMunicipio, Collectors.counting())));
        relatorioPorZonasDTO.setMapEncomendasContinente(utilizadores.stream()
                .collect(Collectors.groupingBy(utilizador -> utilizador.getContinente().toString(), Collectors.counting())));
        relatorioPorZonasDTO.setMapEncomendasPais(utilizadores.stream()
                .collect(Collectors.groupingBy(utilizador -> utilizador.getPais().name(), Collectors.counting())));
        relatorioPorZonasDTO.setTotalDeEncomendas(utilizadores.size());

        return relatorioPorZonasDTO;
    }

    public RelatorioPorDistanciasDTO generateRelatorioDistancias(String emailFromAuthHeader, String roleFromAuthHeader) {
        List<SubEncomenda> subEncomendas;
        Map<Integer, Integer> distanciasQuantidadeMap = new HashMap<>();
        if (roleFromAuthHeader == "ADMIN") {
            subEncomendas = encomendaService.getAllSubEncomendas();
        } else if (roleFromAuthHeader == "FORNECEDOR") {
            subEncomendas = encomendaService.getSubEncomendasByFornecedorEmail(emailFromAuthHeader);
        } else {
            subEncomendas = encomendaService.getSubEncomendasByConsumidorEmail(emailFromAuthHeader);
        }

        RelatorioPorDistanciasDTO relatorioPorDistanciasDTO = new RelatorioPorDistanciasDTO();

        for (SubEncomenda subEncomenda : subEncomendas) {
            Coordinate coordenadasFornecedor = subEncomenda.getFornecedor().getCoordenadas();
            Coordinate coordenadasConsumidor = subEncomenda.getEncomenda().getConsumidor().getCoordenadas();
            double distance = DistanceCalculator.distance(coordenadasFornecedor.getLatitude(), coordenadasFornecedor.getLongitude(),
                    coordenadasConsumidor.getLatitude(), coordenadasConsumidor.getLongitude());
            String interval = getInterval(distance);

            relatorioPorDistanciasDTO.getGamaDistanciasQuantidadeEncomendasMap().merge(interval, 1, Integer::sum);
        }

        return relatorioPorDistanciasDTO;
    }

    private static String getInterval(double element) {
        if (element < 10) {
            return "<10";
        } else if (element >= 10 && element <= 100) {
            return "10-100";
        } else if (element > 100 && element <= 1000) {
            return "100-1000";
        } else {
            return ">1000";
        }
    }

}
