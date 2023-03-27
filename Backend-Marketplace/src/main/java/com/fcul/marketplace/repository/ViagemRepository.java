package com.fcul.marketplace.repository;

import com.fcul.marketplace.model.Viagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViagemRepository extends JpaRepository<Viagem, Integer> {
    Page<Viagem> findByTransporteIdTransporte(Integer idTransporte, Pageable pageable);

}
