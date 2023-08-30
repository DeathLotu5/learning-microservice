package com.appsdeveloperblog.photoapp.api.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Mono;

/**
 * Post filter combination Pre filter
 */

@Configuration
public class GlobalFiltersConfiguration {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean
    @Order(1)
    public GlobalFilter secondPreFilter() {
        return ((exchange, chain) -> {
            logger.info("Second Pre-filter executed...");

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                logger.info("Second Post-filters executed....");
            }));
        });
    }

    @Bean
    @Order(2)
    public GlobalFilter thirdPreFilter() {
        return ((exchange, chain) -> {
            logger.info("Third Pre-filter executed...");

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                logger.info("Third Post-filters executed....");
            }));
        });
    }

    @Bean
    @Order(3)
    public GlobalFilter forthPreFilter() {
        return ((exchange, chain) -> {
            logger.info("Forth Pre-filter executed...");

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                logger.info("Forth Post-filters executed....");
            }));
        });
    }

}
