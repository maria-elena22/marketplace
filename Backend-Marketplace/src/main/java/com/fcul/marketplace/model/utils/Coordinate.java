package com.fcul.marketplace.model.utils;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

@Embeddable
@Data
public class Coordinate {

    @DecimalMin(value = "-90.0", message = "Latitude deve ser entre -90 e 90 graus")
    @DecimalMax(value = "90.0", message = "Latitude deve ser entre -90 e 90 graus")
    @Column(name = "latitude")
    private double latitude;

    @DecimalMin(value = "-180.0", message = "Longitude deve ser entre -180 e 180 graus")
    @DecimalMax(value = "180.0", message = "Longitude deve ser entre -180 e 180 graus")
    @Column(name = "longitude")
    private double longitude;
}