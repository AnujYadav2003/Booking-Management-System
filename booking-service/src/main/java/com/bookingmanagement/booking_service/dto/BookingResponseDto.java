package com.bookingmanagement.booking_service.dto;

import com.bookingmanagement.booking_service.model.ClassType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookingResponseDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private int flightId;
    private String passengerName;
    private String email;
    private int seatCount;
    private double totalCost;
    private String flightName;
    private ClassType classType;
    private String fromStation;
    private String toStation;

    private LocalDateTime bookingTime;
}
