//package com.bookingmanagement.booking_service.mapper;
//
//import com.bookingmanagement.booking_service.dto.BookingResponseDto;
//import com.bookingmanagement.booking_service.model.BookingModel;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class BookingMapper {
//
//    public static BookingResponseDto toDto(BookingModel model) {
//        return new BookingResponseDto(
//                model.getId(),
//                model.getFlightId(),
//                model.getPassengerName(),
//                model.getEmail(),
//                model.getSeatCount(),
//                model.getTotalCost(),
//                model.getFlightName(),
//                model.getClassType(),
//                model.getFromStation(),
//                model.getToStation(),
//                model.getBookingTime()
//        );
//    }
//
//
//
//    // Converts a list of FlightModel to a list of FlightResponseDto
//    public static List<BookingResponseDto> toDtoList(List<BookingModel> models) {
//        return models.stream()
//                .map(BookingMapper::toDto)
//                .collect(Collectors.toList());
//    }
//
//    private BookingMapper() {
//    }
//}
