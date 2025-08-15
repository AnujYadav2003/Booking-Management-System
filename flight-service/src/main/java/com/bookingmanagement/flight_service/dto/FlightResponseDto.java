package com.bookingmanagement.flight_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class FlightResponseDto implements Serializable {
    private static final long serialVersionUID = 1L;
    int id;
    private String flightName;
    private int flightNo;
    private int capacity;
    private String fromStation;
    private String toStation;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private int availableSeat;
    private int bookedSeat;
    private double economyCost;
    private double businessCost;
}
