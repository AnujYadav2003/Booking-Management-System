package com.bookingmanagement.booking_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name="bookings")
public class BookingModel {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int flightId;
    private String passengerName;
    private String email;
    private int seatCount;
    private double totalCost;
    @Enumerated(EnumType.STRING)
    private ClassType classType;

    private String flightName;
    private String fromStation;
    private String toStation;
    private LocalDateTime bookingTime;
}