package com.fcul.marketplace.service;

import com.fcul.marketplace.dto.relatorio.RelatorioPorDistanciasDTO;
import com.fcul.marketplace.dto.relatorio.RelatorioPorZonasDTO;
import com.fcul.marketplace.model.Categoria;
import com.fcul.marketplace.model.Item;
import com.fcul.marketplace.model.SubEncomenda;
import com.fcul.marketplace.model.Utilizador;
import com.fcul.marketplace.model.utils.Coordinate;
import com.fcul.marketplace.utils.DistanceCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

    @Autowired
    EncomendaService encomendaService;

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

    public RelatorioPorZonasDTO generateRelatorioZonas(String emailFromAuthHeader, String roleFromAuthHeader, List<Integer> categoriasIds, String dataMin, String dataMax) throws ParseException {
        List<Utilizador> utilizadores;
        if (roleFromAuthHeader.equals("ADMIN")) {
            List<SubEncomenda> subEncomendas = encomendaService.getAllSubEncomendas();
            subEncomendas = filtraSubEncomendas(subEncomendas,categoriasIds, dataMin,dataMax);
            utilizadores = subEncomendas.stream().map(SubEncomenda::getFornecedor).collect(Collectors.toList());
        } else if (roleFromAuthHeader.equals("FORNECEDOR")) {
            List<SubEncomenda> subEncomendas = encomendaService.getSubEncomendasByFornecedorEmail(emailFromAuthHeader);
            subEncomendas = filtraSubEncomendas(subEncomendas,categoriasIds, dataMin,dataMax);
            utilizadores = subEncomendas.stream().map(subEncomenda -> subEncomenda.getEncomenda().getConsumidor()).collect(Collectors.toList());
        } else {
            List<SubEncomenda> subEncomendas = encomendaService.getSubEncomendasByConsumidorEmail(emailFromAuthHeader);
            subEncomendas = filtraSubEncomendas(subEncomendas,categoriasIds, dataMin,dataMax);
            utilizadores = subEncomendas.stream().map(SubEncomenda::getFornecedor).collect(Collectors.toList());
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

    private List<SubEncomenda> filtraSubEncomendas(List<SubEncomenda> subEncomendas,List<Integer> categoriasIds, String dataMin, String dataMax) throws ParseException {
        List<SubEncomenda> result = new ArrayList<>();
        for (SubEncomenda subEncomenda: subEncomendas) {
            if(filtraDatas(subEncomenda,dataMin,dataMax) && filtraCategorias(subEncomenda,categoriasIds)){
                result.add(subEncomenda);

            }
        }
        return result;

    }

    private boolean filtraCategorias(SubEncomenda subEncomenda, List<Integer> categoriasIds) {
        if(categoriasIds.size() != 0 ){
            return subEncomenda.getItems().stream().anyMatch(
                    item -> item.getProduto().getSubCategorias().stream().anyMatch(
                            subCategoria -> categoriasIds.contains(subCategoria.getCategoria().getIdCategoria())
                    ));
        }
        return true;




    }

    private boolean filtraDatas(SubEncomenda subEncomenda, String dataMin, String dataMax) throws ParseException {

        Timestamp subEncomendaTimestamp = subEncomenda.getDataEncomenda();

        if (dataMin != null) {
            Timestamp minTimestamp = convertToTimestamp(dataMin, "00:00:00");

            if (subEncomendaTimestamp.before(minTimestamp)) {
                return false;
            }
        }

        if (dataMax != null) {
            Timestamp maxTimestamp = convertToTimestamp(dataMax, "23:59:59");

            if (subEncomendaTimestamp.after(maxTimestamp)) {
                return false;
            }
        }

        return true;


    }

    private static Timestamp convertToTimestamp(String dateString, String timeString) throws ParseException {
        String dateTimeString = dateString + " " + timeString;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parsedDate = format.parse(dateTimeString);
        return new Timestamp(parsedDate.getTime());
    }

    public RelatorioPorDistanciasDTO generateRelatorioDistancias(String emailFromAuthHeader, String roleFromAuthHeader, List<Integer> categoriasIds, String dataMin, String dataMax) throws ParseException {
        List<SubEncomenda> subEncomendas;
        Map<Integer, Integer> distanciasQuantidadeMap = new HashMap<>();
        if (roleFromAuthHeader.equals("ADMIN")) {
            subEncomendas = encomendaService.getAllSubEncomendas();
            subEncomendas = filtraSubEncomendas(subEncomendas,categoriasIds, dataMin,dataMax);
        } else if (roleFromAuthHeader.equals("FORNECEDOR")) {
            subEncomendas = encomendaService.getSubEncomendasByFornecedorEmail(emailFromAuthHeader);
            subEncomendas = filtraSubEncomendas(subEncomendas,categoriasIds, dataMin,dataMax);

        } else {
            subEncomendas = encomendaService.getSubEncomendasByConsumidorEmail(emailFromAuthHeader);
            subEncomendas = filtraSubEncomendas(subEncomendas,categoriasIds, dataMin,dataMax);

        }


        RelatorioPorDistanciasDTO relatorioPorDistanciasDTO = new RelatorioPorDistanciasDTO();
        relatorioPorDistanciasDTO.setGamaDistanciasQuantidadeEncomendasMap(new HashMap<>());


        relatorioPorDistanciasDTO.getGamaDistanciasQuantidadeEncomendasMap().put("<10",0);
        relatorioPorDistanciasDTO.getGamaDistanciasQuantidadeEncomendasMap().put("10-100",0);
        relatorioPorDistanciasDTO.getGamaDistanciasQuantidadeEncomendasMap().put("100-1000",0);
        relatorioPorDistanciasDTO.getGamaDistanciasQuantidadeEncomendasMap().put(">1000",0);

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

}
