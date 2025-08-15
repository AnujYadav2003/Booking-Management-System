package com.bookingmanagement.booking_service.dto;

import com.bookingmanagement.booking_service.model.ClassType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookingRequestDto {

    private int flightId;
    private String passengerName;
    private String email;
    private int seatCount;

    private ClassType classType;
    private String flightName;

    private String fromStation;
    private String toStation;
}
