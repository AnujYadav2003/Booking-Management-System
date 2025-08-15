package com.bookingmanagement.booking_service.client;
import com.bookingmanagement.booking_service.dto.FlightResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlightServiceProxy {

    @Autowired
    private FlightClient flightClient;

    @CircuitBreaker(name = "flight", fallbackMethod = "getFlightFallback")
    @Retry(name = "flight")
    @RateLimiter(name = "flight")
    public FlightResponseDto getFlightById(int id) {
        return flightClient.getFlightById(id);
    }

    public FlightResponseDto getFlightFallback(int id, Throwable t) {
        System.out.println("Fallback: flight-service is down. Reason: " + t.getMessage());

        FlightResponseDto fallback = new FlightResponseDto();
        fallback.setId(-1); // indicates error/fallback
        fallback.setFlightName("Unavailable");
        fallback.setFromStation("N/A");
        fallback.setToStation("N/A");
        fallback.setEconomyCost(0.0);
        fallback.setBusinessCost(0.0);
        fallback.setAvailableSeat(0);
        fallback.setBookedSeat(0);

        return fallback;
    }

    @CircuitBreaker(name = "flight", fallbackMethod = "updateSeatsFallback")
    @Retry(name = "flight")
    @RateLimiter(name = "flight")
    public String updateSeats(int id, int seatsToRelease) {
        return flightClient.updateSeats(id, seatsToRelease);
    }

    public String updateSeatsFallback(int id, int seatsToRelease, Throwable t) {
        return "Failed to update seats. Please try again later.";
    }

    @CircuitBreaker(name = "flight", fallbackMethod = "searchRouteFallback")
    @Retry(name = "flight")
    @RateLimiter(name = "flight")
    public FlightResponseDto searchRoute(int id,String flightName, String fromStation, String toStation) {
        return flightClient.checkFlightAvailability(id, flightName, fromStation, toStation);
    }

    public FlightResponseDto searchRouteFallback(String flightName, String fromStation, String toStation, Throwable t) {
        System.out.println("Fallback: flight-service is down. Reason: " + t.getMessage());

        FlightResponseDto fallback = new FlightResponseDto();
        fallback.setId(-1);
        fallback.setFlightName("Unavailable");
        fallback.setFromStation(fromStation);
        fallback.setToStation(toStation);
        fallback.setEconomyCost(0.0);
        fallback.setBusinessCost(0.0);
        fallback.setAvailableSeat(0);
        fallback.setBookedSeat(0);

        return fallback;
    }

    @CircuitBreaker(name = "flight", fallbackMethod = "releaseSeatsFallback")
    @Retry(name = "flight")
    @RateLimiter(name = "flight")
    public void releaseSeats(int flightId, int seatsToRelease) {
        flightClient.releaseSeats(flightId, seatsToRelease);
    }

    public void releaseSeatsFallback(int flightId, int seatsToRelease, Throwable t) {
        System.out.println("Fallback: Unable to release seats for flight ID " + flightId);
    }
}
