package com.bookingmanagement.flight_service.repository;

import com.bookingmanagement.flight_service.model.FlightModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<FlightModel,Integer> {

    @Query(value = "SELECT * FROM flights WHERE departure_time = :departureTime", nativeQuery = true)
    List<FlightModel> findByDepartureTime(@Param("departureTime") LocalTime departureTime);

    @Query(value = "SELECT * FROM flights WHERE from_station = :fromStation", nativeQuery = true)
    List<FlightModel> findByFromStation(@Param("fromStation") String fromStation);

    @Query(value = "SELECT * FROM flights WHERE flight_no = :flightNo", nativeQuery = true)
    Optional<FlightModel> findByFlightNo(@Param("flightNo") int flightNo);

    @Query(value = "SELECT * FROM flights WHERE id = :id AND flight_name = :flightName AND from_station = :fromStation AND to_station = :toStation", nativeQuery = true)
    Optional<FlightModel> findByIdAndFlightNameAndFromStationAndToStation(
            @Param("id") int id,
            @Param("flightName") String flightName,
            @Param("fromStation") String fromStation,
            @Param("toStation") String toStation
    );

//    List<FlightModel> findByDepartureTime(LocalTime departureTime);
//    List<FlightModel> findByFromStation(String fromStation);
//    Optional<FlightModel> findByFlightNo(int flightNo);
//    Optional<FlightModel> findByIdAndFlightNameAndFromStationAndToStation(
//        int id,
//        String flightName,
//        String fromStation,
//        String toStation
//);
}