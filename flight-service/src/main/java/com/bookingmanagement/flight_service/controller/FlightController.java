package com.bookingmanagement.flight_service.controller;

import com.bookingmanagement.flight_service.dto.FlightRequestDto;
import com.bookingmanagement.flight_service.dto.FlightResponseDto;
import com.bookingmanagement.flight_service.model.FlightModel;
import com.bookingmanagement.flight_service.repository.FlightRepository;
import com.bookingmanagement.flight_service.service.FlightService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/flight")
@Tag(name = "Flight Service",description = "Operation related to Flight Service")
public class FlightController {

    private final FlightService flightService;
    private final FlightRepository flightRepository;
    @Autowired
    public FlightController(FlightService flightService,FlightRepository flightRepository){
        this.flightService=flightService;
        this.flightRepository=flightRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<FlightResponseDto> registerFlight(@RequestBody FlightRequestDto flightRequestDto) {
        FlightResponseDto res= flightService.registerFlight(flightRequestDto);
        log.info("Flight registered successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping
    public ResponseEntity<List<FlightResponseDto>> getAllFlights() {
        List<FlightResponseDto>allFlights= flightService.getAllFlight();
        log.info("All flight details");
        return ResponseEntity.status(HttpStatus.OK).body(allFlights);
    }
    @GetMapping("/{id}")
    public ResponseEntity<FlightResponseDto> getFlightById(@PathVariable int id) {
        FlightResponseDto flight= flightService.getFlightById(id);
        log.info("Flight details whose ID is : "+id);
        return ResponseEntity.status(HttpStatus.OK).body(flight);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<FlightResponseDto> updateFlight(@PathVariable int id, @RequestBody FlightRequestDto flightRequestDto) {
        FlightResponseDto updatedFlight= flightService.updateFlight(id, flightRequestDto);
        log.info("Flight details whose ID is :"+ id+" is updated");
        return ResponseEntity.status(HttpStatus.OK).body(updatedFlight);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFlight(@PathVariable int id) {
         flightService.deleteFlight(id);
        log.info("Flight details whose id is : "+ id+" is deleted successfully");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Flight details is deleted");
    }

    @GetMapping("/search/time/departureTime/{departureTime}")
    public List<FlightResponseDto> searchByStartingTime(@PathVariable LocalTime departureTime)
    {
        return flightService.searchByStartingTime(departureTime);
    }

    @GetMapping("/search/byLocation/{fromStation}")
    public List<FlightResponseDto> searchByLocation(@PathVariable String fromStation){
        return flightService.searchByLocation(fromStation);
    }

    @GetMapping("/search/flightNo/{flightNo}")
    public ResponseEntity<FlightResponseDto> searchByFlightNo(@PathVariable int flightNo){
        return ResponseEntity.ok(flightService.searchByFlightNo(flightNo));
    }

    @GetMapping("/check-availability")
    public ResponseEntity<FlightResponseDto> checkFlightAvailability(
            @RequestParam int id,
            @RequestParam String flightName,
            @RequestParam String fromStation,
            @RequestParam String toStation) {

        Optional<FlightModel> flight = flightRepository
                .findByIdAndFlightNameAndFromStationAndToStation(id, flightName, fromStation, toStation);
        return flight.map(it -> ResponseEntity.ok(flightService.mapToFlightResponse(it)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }



    @PutMapping("/update-seats/{id}")
   public String updateSeats(@PathVariable int id, @RequestParam int seatsToRelease){
        return flightService.updateSeats(id,seatsToRelease);
    }

    @PutMapping("/releaseSeat/{id}")
    public String updateSeatsFromCancellation(@PathVariable int id, @RequestParam int seatsToRelease){
        return flightService.cancelSeatUpdate(id,seatsToRelease);
    }
}