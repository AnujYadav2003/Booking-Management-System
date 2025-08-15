package com.bookingmanagement.booking_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FlightResponseDto {
    private int id;
    private String flightName;
    private String fromStation;
    private String toStation;
    private double economyCost;
    private double businessCost;
    private int availableSeat;
    private int bookedSeat;
}