package com.bookingmanagement.booking_service.repository;

import com.bookingmanagement.booking_service.model.BookingModel;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookingRepository extends JpaRepository<BookingModel, Integer> {

}
