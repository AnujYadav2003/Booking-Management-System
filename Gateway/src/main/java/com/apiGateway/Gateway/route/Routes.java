package com.apiGateway.Gateway.route;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Routes {

    @Bean
    public RouteLocator RouteHandler(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r
                        .path("/api/auth/**")
                        .uri("http://localhost:8084"))
                .route("flight-service", r -> r
                        .path("/api/flight/**")
                        .uri("http://localhost:8081"))
                .route("booking-service", r -> r
                        .path("/api/booking/**")
                        .uri("http://localhost:8080"))
                .route("service-registry",r->r
                        .path(("/**"))
                        .uri(("http://localhost:8761")))

                .build();
    }
}

////    @Bean
////    public RouteLocator RouteHandler(RouteLocatorBuilder builder) {
////        return builder.routes()
////                .route("auth-service", r -> r
////                        .path("/api/auth/**")
////                        .uri("lb://auth-service"))
////                .route("flight-service", r -> r
////                        .path("/api/flight/**")
////                        .uri("lb://flight-service"))
////                .route("booking-service", r -> r
////                        .path("/api/booking/**")
////                        .uri("lb://booking-service"))
////                .build();
////    }
//
//}

// api gateway
// ressiliance4j
// open feign
// redis
// service registry
// // testing
// //kafka

// spring boot
// mysql
//jwt


