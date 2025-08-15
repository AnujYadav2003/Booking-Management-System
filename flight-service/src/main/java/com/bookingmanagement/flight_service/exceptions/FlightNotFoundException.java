package com.bookingmanagement.flight_service.exceptions;

public class FlightNotFoundException extends RuntimeException{
   public FlightNotFoundException(String message) {
       super(message);
   }
}
