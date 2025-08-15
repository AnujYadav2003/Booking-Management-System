package com.apiGateway.Gateway.authfilter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final WebClient webClient = WebClient.create();

    private static final List<String> openEndpoints = List.of(
            "/api/auth/login",
            "/api/auth/register"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // Allow open endpoints
        if (openEndpoints.stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }

        // Get token
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Validate token with auth-service
        return webClient.get()
                .uri("http://localhost:8084/api/auth/validate")
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .retrieve()
                .onStatus(HttpStatus.UNAUTHORIZED::equals,
                        response -> Mono.error(new RuntimeException("Invalid token")))
                .bodyToMono(String.class)
                .then(chain.filter(exchange))
                .onErrorResume(e -> {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                });
    }

    @Override
    public int getOrder() {
        return -1; // high priority
    }
}








//public class Routes {
//
//    private final AuthClient authClient;
//
//    public Routes(AuthClient authClient) {
//        this.authClient = authClient;
//    }
//
//    @PostMapping("/login")
//    public String login(@RequestBody AuthRequest request) {
//        try {
//            String jwtToken = authClient.createAuthenticationToken(request);
//            return jwtToken;
//        } catch (Exception e) {
//            return "Invalid Credentials";
/// /            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
//        }
//    }
//
//    @PostMapping()
//    public ResponseEntity<String> registerUser(@RequestBody User user){
//        return authClient.registerUser(user);
//    }
//
//@Autowired
//private AuthClient authClient;
//
//    @PostMapping()
//    public ResponseEntity<String> registerViaGateway(@RequestBody User user) {
//        return authClient.registerUser(user);
//    }
//
//    @PostMapping()
//    public ResponseEntity<String> authenticateViaGateway(@RequestBody AuthRequest authRequest) {
//        String token = authClient.createAuthenticationToken(authRequest);
//        return ResponseEntity.ok(token);
//    }
//}
//    @Autowired
//    private AuthClient authClient;
//
//    @PostMapping("/register")
//    public ResponseEntity<String> registerUser(@RequestBody User user) {
//        return authClient.registerUser(user);
//    }
//
//    @PostMapping("/authenticate")
//    public ResponseEntity<String> authenticateViaGateway(@RequestBody AuthRequest authRequest) {
//        String token = authClient.createAuthenticationToken(authRequest);
//        return ResponseEntity.ok(token);
//    }
//}

//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.client.WebClient;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.core.io.buffer.DataBuffer;
//import reactor.core.publisher.Mono;
//
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//
//@Component
//public class AuthenticationFilter implements GlobalFilter, Ordered {
//
//    private final WebClient webClient = WebClient.create();
//
//    private static final List<String> openEndpoints = List.of(
//            "/api/auth/login",
//            "/api/auth/register"
//    );
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        String path = exchange.getRequest().getURI().getPath();
//
//        // Allow public endpoints
//        if (openEndpoints.stream().anyMatch(path::startsWith)) {
//            return chain.filter(exchange);
//        }
//
//        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return this.buildErrorResponse(exchange, "Missing or invalid Authorization header");
//        }
//
//        // Validate token
//        return webClient.get()
//                .uri("http://localhost:8084/api/auth/validate")
//                .header(HttpHeaders.AUTHORIZATION, authHeader)
//                .retrieve()
//                .bodyToMono(String.class)
//                .then(chain.filter(exchange))
//                .onErrorResume(e -> this.buildErrorResponse(exchange, "JWT token is invalid or expired"));
//    }
//
//    private Mono<Void> buildErrorResponse(ServerWebExchange exchange, String message) {
//        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
//
//        String body = String.format("{\"error\": \"%s\"}", message);
//        DataBuffer buffer = exchange.getResponse()
//                .bufferFactory()
//                .wrap(body.getBytes(StandardCharsets.UTF_8));
//
//        return exchange.getResponse().writeWith(Mono.just(buffer));
//    }
//
//    @Override
//    public int getOrder() {
//        return -1;
//    }
//}
//
