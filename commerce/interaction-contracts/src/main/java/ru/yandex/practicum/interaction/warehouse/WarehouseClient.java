package ru.yandex.practicum.interaction.warehouse;


import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse", fallback = WarehouseClientFallback.class,
        configuration = WarehouseFeignConfig.class)
public interface WarehouseClient extends WarehouseController {

}
