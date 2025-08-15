package com.bookingmanagement.flight_service.service;

import com.bookingmanagement.flight_service.dto.FlightRequestDto;
import com.bookingmanagement.flight_service.dto.FlightResponseDto;
import com.bookingmanagement.flight_service.exceptions.FlightNotFoundException;
import com.bookingmanagement.flight_service.model.FlightModel;
import com.bookingmanagement.flight_service.repository.FlightRepository;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalTime;
import java.util.List;
@Service
public class FlightService {

    private static final Logger logger = LoggerFactory.getLogger(FlightService.class);

    @Autowired
    private FlightRepository flightRepository;

    @CachePut(value = "Register Flight")
    public FlightResponseDto registerFlight(FlightRequestDto flightRequestDto) {

        FlightModel flight = FlightModel.builder()
                .flightName(flightRequestDto.getFlightName())
                .flightNo(flightRequestDto.getFlightNo())
                .capacity(flightRequestDto.getCapacity())
                .fromStation(flightRequestDto.getFromStation())
                .toStation(flightRequestDto.getToStation())
                .departureTime(flightRequestDto.getDepartureTime())
                .arrivalTime(flightRequestDto.getArrivalTime())
                .availableSeat(flightRequestDto.getCapacity())
                .bookedSeat(0)
                .businessCost(flightRequestDto.getBusinessCost())
                .economyCost(flightRequestDto.getEconomyCost())
                .build();
               flightRepository.save(flight);
               logger.info("Flight {} is saved", flight.getId());
               return mapToFlightResponse(flight);

    }

    @Cacheable("allFlight")
    public List<FlightResponseDto> getAllFlight() {
        logger.info("Fetching all flights from the database");
        List<FlightModel> allFlights = flightRepository.findAll();
        logger.debug("Number of flights found: {}", allFlights.size());
        return allFlights.stream().map(this::mapToFlightResponse).toList();
    }

    @Cacheable(value = "Get Flight By ID")
    public FlightResponseDto getFlightById(int id) {
        logger.info("Fetching flight with ID: {}", id);
        FlightModel flight = flightRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Flight not found with ID: {}", id);
                    return new FlightNotFoundException("Flight not found with ID " + id);
                });
        return mapToFlightResponse(flight);
    }

    @CachePut(value = "Update Flight")
    public FlightResponseDto updateFlight(int id, FlightRequestDto updateFlightDto) {
        logger.info("Updating flight with ID: {}", id);
        FlightModel flight = flightRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Flight not found for update. ID: {}", id);
                    return new FlightNotFoundException("Flight not found with ID " + id);
                });

        flight.setFlightName(updateFlightDto.getFlightName());
        flight.setFlightNo(updateFlightDto.getFlightNo());
        flight.setCapacity(updateFlightDto.getCapacity());
        flight.setFromStation(updateFlightDto.getFromStation());
        flight.setToStation(updateFlightDto.getToStation());
        flight.setDepartureTime(updateFlightDto.getDepartureTime());
        flight.setArrivalTime(updateFlightDto.getArrivalTime());
        flight.setAvailableSeat(updateFlightDto.getCapacity());
        flight.setBookedSeat(0);
        flight.setEconomyCost(updateFlightDto.getEconomyCost());
        flight.setBusinessCost(updateFlightDto.getBusinessCost());

        flightRepository.save(flight);
        logger.debug("Flight updated successfully: {}", flight);
        return mapToFlightResponse(flight);
    }

    @CacheEvict(value = "Delete Flight")
    public String deleteFlight(int id) {
        logger.warn("Deleting flight with ID: {}", id);
        FlightModel flight = flightRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Flight not found for deletion. ID: {}", id);
                    return new FlightNotFoundException("Flight not found with ID " + id);
                });

        flightRepository.delete(flight);
        logger.info("Flight with ID {} deleted successfully", id);
        return "Flight deleted successfully";
    }

@Cacheable("Get Flight details with Flight Number")
    public FlightResponseDto searchByFlightNo(int flightNo) {
        logger.info("Fetching flight with FlightNo: {}", flightNo);
        FlightModel flight = flightRepository.findByFlightNo(flightNo)
                .orElseThrow(() -> {
                    logger.error("Flight not found with Flight No: {}", flightNo);
                    return new FlightNotFoundException("Flight not found with FlightNo " + flightNo);
                });
        System.out.println(flight.getFlightNo());
        return mapToFlightResponse(flight);
    }
    @Cacheable(value = "Search Flight By Departure Time")
    public List<FlightResponseDto> searchByStartingTime(LocalTime departureTime) {
        logger.info("Searching flights by departure time: {}", departureTime);
        List<FlightModel> flights = flightRepository.findByDepartureTime(departureTime);
        logger.debug("Found {} flights with departure time: {}", flights.size(), departureTime);
        return flights.stream().map(this::mapToFlightResponse).toList();
    }

    @Cacheable(value = "Search Flight By Location")
    public List<FlightResponseDto> searchByLocation(String fromStation) {
        logger.info("Searching flights from station: {}", fromStation);
        List<FlightModel> flights = flightRepository.findByFromStation(fromStation);
        logger.debug("Found {} flights from station: {}", flights.size(), fromStation);
        return flights.stream().map(this::mapToFlightResponse).toList();
    }

    @CachePut(value = "Update seat after booking")
    public String updateSeats(int id, int seatsToRelease) {
        logger.info("Booking seats: {} on flight ID: {}", seatsToRelease, id);

        FlightModel flight = flightRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Flight not found for booking. ID: {}", id);
                    return new RuntimeException("Flight not found with id: " + id);
                });

        if (seatsToRelease <= 0) {
            logger.warn("Attempt to book non-positive seat count: {}", seatsToRelease);
            throw new IllegalArgumentException("Seats to release must be positive");
        }

        logger.debug("Flight before booking update: {}", flight);
        flight.setAvailableSeat(flight.getAvailableSeat() - seatsToRelease);
        flight.setBookedSeat(flight.getBookedSeat() + seatsToRelease);
        flightRepository.save(flight);
        logger.info("Seats booked successfully for flight ID: {}", id);

        return "Updated flight seats successfully for flight ID: " + id;
    }

    @Cacheable(value = "Cancel seat of booking")
    public String cancelSeatUpdate(@PathVariable int id, @RequestParam int seatsToRelease) {
        logger.info("Cancelling {} seats on flight ID: {}", seatsToRelease, id);

        FlightModel flight = flightRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Flight not found for cancellation. ID: {}", id);
                    return new RuntimeException("Flight not found with ID: " + id);
                });

        flight.setAvailableSeat(flight.getAvailableSeat() + seatsToRelease);
        flight.setBookedSeat(flight.getBookedSeat() - seatsToRelease);
        flightRepository.save(flight);
        logger.info("Seats cancelled successfully for flight ID: {}", id);

        return "Flight seats updated successfully.";
    }

    public FlightResponseDto mapToFlightResponse(FlightModel flight) {
        return new FlightResponseDto(flight.getId(), flight.getFlightName(),
                flight.getFlightNo(),flight.getCapacity(),flight.getFromStation(),
                flight.getToStation(),flight.getDepartureTime(),flight.getArrivalTime(),
                flight.getAvailableSeat(),flight.getBookedSeat(),flight.getEconomyCost(),
                flight.getBusinessCost());
    }
}
