package ru.yandex.practicum.shopping.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableFeignClients(basePackages = "ru.yandex.practicum.interaction")
@ComponentScan(basePackages = {"ru.yandex.practicum.shopping.cart", "ru.yandex.practicum.interaction"})
public class ShoppingCartApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingCartApplication.class, args);
    }

}
