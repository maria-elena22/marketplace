package com.fcul.marketplace.repository.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableUtils {

    public static Integer DEFAULT_PAGE_SIZE = 10;

    public static Integer DEFAULT_PAGE_NUMBER = 0;


    public static Pageable getDefaultPageable(Integer page, Integer size, Sort.Direction sortDir, String sortKey) {

        int pageSize = (size == null) ? DEFAULT_PAGE_SIZE : size;
        int pageNumber = (page == null) ? DEFAULT_PAGE_NUMBER : page;
        Sort.Direction sortDirection = (sortDir == null) ? Sort.Direction.ASC : sortDir;
        String sortingKey = (sortKey == null) ? "id" : sortKey;

        return PageRequest.of(pageNumber, pageSize, sortDirection, sortingKey);
    }
}
