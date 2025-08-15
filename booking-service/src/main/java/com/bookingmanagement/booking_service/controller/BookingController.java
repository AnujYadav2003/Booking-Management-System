package com.bookingmanagement.booking_service.controller;


import com.bookingmanagement.booking_service.dto.BookingRequestDto;
import com.bookingmanagement.booking_service.dto.BookingResponseDto;
import com.bookingmanagement.booking_service.service.BookingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking")

@Tag(name = "Booking Service",description = "Operation related to Booking Service")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService){
        this.bookingService=bookingService;
    }

    @PostMapping()
        public BookingResponseDto booking(@RequestBody BookingRequestDto bookingRequestDto){
        return bookingService.bookFlight(bookingRequestDto);
    }

    @GetMapping()
    public List<BookingResponseDto> getAllBooking()
    {
        return bookingService.getAllBooking();
    }

    @GetMapping("/{id}")
    public BookingResponseDto getBookingById(@PathVariable int id){
        return bookingService.getBookingById(id);
    }

    @PutMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable int id){
        return bookingService.cancelBooking(id);
    }

    @DeleteMapping("/{id}")
   public String deleteBookingById(@PathVariable int id){
        return bookingService.deleteBookingById(id);
    }

}