package com.bookingmanagement.booking_service.service;

import com.bookingmanagement.booking_service.client.FlightClient;
import com.bookingmanagement.booking_service.dto.BookingRequestDto;
import com.bookingmanagement.booking_service.dto.BookingResponseDto;
import com.bookingmanagement.booking_service.dto.FlightResponseDto;
import com.bookingmanagement.booking_service.model.BookingModel;
import com.bookingmanagement.booking_service.repository.BookingRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    private final BookingRepository bookingRepository;
    private final FlightClient flightClient;

    @Autowired
    public BookingService(
            BookingRepository bookingRepository,
            FlightClient flightClient) {
        this.bookingRepository = bookingRepository;
        this.flightClient = flightClient;
    }

//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;


    @CachePut(value = "Booking Flight Ticket")
    public BookingResponseDto bookFlight(BookingRequestDto dto) {
        log.info("Attempting to book flight: {}, for passenger: {}", dto.getFlightName(), dto.getPassengerName());

        FlightResponseDto flight = flightClient.checkFlightAvailability(
                dto.getFlightId(), dto.getFlightName(), dto.getFromStation(), dto.getToStation()
        );

        if (flight == null) {
            log.error("No flight found matching details: ID={}, Name={}, From={}, To={}",
                    dto.getFlightId(), dto.getFlightName(), dto.getFromStation(), dto.getToStation());
            throw new RuntimeException("No flight available");
        }

        if (flight.getAvailableSeat() < dto.getSeatCount()) {
            log.warn("Insufficient seats: Requested={}, Available={}",
                    dto.getSeatCount(), flight.getAvailableSeat());
            throw new RuntimeException("Not enough seats available");
        }

        double pricePerSeat = switch (dto.getClassType()) {
            case ECONOMY -> flight.getEconomyCost();
            case BUSINESS -> flight.getBusinessCost();
        };

        double totalPrice = pricePerSeat * dto.getSeatCount();

        flightClient.updateSeats(flight.getId(), dto.getSeatCount());
        log.info("Updated flight seat count for flight ID: {}", flight.getId());

        BookingModel booking = BookingModel.builder()
                .flightId(dto.getFlightId())
                .passengerName(dto.getPassengerName())
                .email(dto.getEmail())
                .seatCount(dto.getSeatCount())
                .classType(dto.getClassType())
                .flightName(dto.getFlightName())
                .totalCost(totalPrice)
                .fromStation(dto.getFromStation())
                .toStation(dto.getToStation())
                .bookingTime(LocalDateTime.now())

                .build();

        bookingRepository.save(booking);
        log.info("Booking successful. Booking ID: {}, Passenger: {}", booking.getId(), dto.getPassengerName());
//        String message = "Ticket booked for " + dto.getPassengerName() +
//                " on flight " + dto.getFlightName();
//        kafkaTemplate.send("booking-notification-topic", message);
        return mapToBookingResponse(booking);
    }

       @Cacheable(value = "Getting details of all bookings")
    public List<BookingResponseDto> getAllBooking() {
        log.info("Fetching all bookings");
        List<BookingModel> allBookings = bookingRepository.findAll();
        log.debug("Found {} bookings", allBookings.size());
        return allBookings.stream().map(this::mapToBookingResponse).toList();
    }

    public BookingResponseDto getBookingById(int id) {
        log.info("Fetching booking by ID: {}", id);
        BookingModel booking = bookingRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Booking not found with ID: {}", id);
                    return new RuntimeException("Booking not available with id: " + id);
                });
        return mapToBookingResponse(booking);
    }

    @CacheEvict("Cancelling the booking")
    @Transactional
    public String cancelBooking(int bookingId) {
        log.info("Cancelling booking with ID: {}", bookingId);

        BookingModel booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    log.error("Booking not found for cancellation. ID: {}", bookingId);
                    return new RuntimeException("Booking not found with ID: " + bookingId);
                });

        int seatsToRelease = booking.getSeatCount();
        int flightId = booking.getFlightId();

        flightClient.releaseSeats(flightId, seatsToRelease);
        bookingRepository.deleteById(bookingId);

        log.info("Booking cancelled successfully. Released {} seats on flight ID: {}", seatsToRelease, flightId);
        return "Booking cancelled and seats released for flight ID: " + flightId;
    }

    @CacheEvict("Deleting the Booking Data")
    public String deleteBookingById(int id) {
        log.info("Deleting booking with ID: {}", id);
        BookingModel booking = bookingRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Booking not found for deletion. ID: {}", id);
                    return new RuntimeException("Booking not available with id: " + id);
                });

        bookingRepository.deleteById(id);
        log.info("Booking with ID {} deleted successfully", id);
        return "Booking deleted successfully";
    }

    public BookingResponseDto mapToBookingResponse(BookingModel bookingModel) {
        return new BookingResponseDto(bookingModel.getId(),bookingModel.getFlightId(),
                bookingModel.getPassengerName(), bookingModel.getEmail(),bookingModel.getSeatCount(),
                bookingModel.getTotalCost(),bookingModel.getFlightName(),
                bookingModel.getClassType(),bookingModel.getFromStation(),
                bookingModel.getToStation(),bookingModel.getBookingTime());
    }
}
