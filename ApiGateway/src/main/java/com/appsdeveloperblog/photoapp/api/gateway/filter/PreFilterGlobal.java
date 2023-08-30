package com.appsdeveloperblog.photoapp.api.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Set;


@Component
public class PreFilterGlobal implements GlobalFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        logger.info("My first Pre-filter is executed....");
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().toString();
        logger.info("Request Path: {}", path);
        HttpHeaders headers = request.getHeaders();
        Set<String> headerNames = headers.keySet();

        headerNames.forEach(headerName -> {
            String headerValue = headers.getFirst(headerName);
            logger.info("Header Name: {}, Header Value: {}", headerName, headerValue);
        });

        return chain.filter(exchange);
    }
}
