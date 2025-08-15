//package com.bookingmanagement.flight_service.Mapper;
//
//import com.bookingmanagement.flight_service.dto.FlightResponseDto;
//import com.bookingmanagement.flight_service.model.FlightModel;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class FlightMapper {
//
//    public static FlightResponseDto toDto(FlightModel model) {
//        if (model == null) {
//            throw new IllegalArgumentException("FlightModel cannot be null in FlightMapper.toDto()");
//        }
//
//        return new FlightResponseDto(
//                model.getId(),
//                model.getFlightName(),
//                model.getFlightNo(),
//                model.getCapacity(),
//                model.getFromStation(),
//                model.getToStation(),
//                model.getDepartureTime(),
//                model.getArrivalTime(),
//                model.getAvailableSeat(),
//                model.getBookedSeat(),
//                model.getEconomyCost(),
//                model.getBusinessCost()
//        );
//    }
//
//    // Converts a list of FlightModel to a list of FlightResponseDto
//    public static List<FlightResponseDto> toDtoList(List<FlightModel> models) {
//        return models.stream()
//                .map(FlightMapper::toDto)
//                .collect(Collectors.toList());
//    }
//
//    private FlightMapper() {
//    }
//}
