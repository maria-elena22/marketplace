package com.fcul.marketplace.model.utils;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Data
public class Coordinate {

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

}