package com.bookingmanagement.booking_service.client;

import com.bookingmanagement.booking_service.dto.FlightResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Service
//@FeignClient(name = "flight-service", url = "http://localhost:8081")
@FeignClient(name = "flight-service")
public interface FlightClient {

    @GetMapping("/api/flight/{id}")
    FlightResponseDto getFlightById(@PathVariable int  id);


    @PutMapping("/api/flight/update-seats/{id}")
    String updateSeats(@PathVariable("id") int id, @RequestParam("seatsToRelease") int seatsToRelease);

@GetMapping("/api/flight/check-availability")
    FlightResponseDto checkFlightAvailability(
            @RequestParam("id") int id,
            @RequestParam("flightName") String flightName,
            @RequestParam("fromStation") String fromStation,
            @RequestParam("toStation") String toStation
    );

    @PutMapping("/api/flight/releaseSeat/{id}")
    void releaseSeats(@PathVariable("id") int flightId,
                      @RequestParam("seatsToRelease") int seatsToRelease);

}
