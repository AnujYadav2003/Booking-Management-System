package com.bookingmanagement.flight_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FlightRequestDto {

    private String flightName;
    private int flightNo;
    private int capacity;
    private String fromStation;
    private String toStation;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private double economyCost;
    private double businessCost;
}
