package ru.yandex.practicum.interaction.warehouse;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WarehouseFeignConfig {

    @Bean
    public ErrorDecoder warehouseErrorDecoder() {
        return new WarehouseErrorDecoder();
    }
}
