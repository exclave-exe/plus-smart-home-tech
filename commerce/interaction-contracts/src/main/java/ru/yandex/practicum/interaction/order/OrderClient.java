package ru.yandex.practicum.interaction.order;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "order", path = "/api/v1/order")
public interface OrderClient extends OrderController {

}
