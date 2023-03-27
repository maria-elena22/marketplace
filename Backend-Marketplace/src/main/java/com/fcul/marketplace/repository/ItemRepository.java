package com.fcul.marketplace.repository;

import com.fcul.marketplace.model.Item;
import com.fcul.marketplace.model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {


}

