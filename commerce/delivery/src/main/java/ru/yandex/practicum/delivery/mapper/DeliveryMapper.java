package ru.yandex.practicum.delivery.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.delivery.model.Delivery;
import ru.yandex.practicum.interaction.delivery.dto.DeliveryDto;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {

    DeliveryDto toDto(Delivery delivery);

    Delivery toEntity(DeliveryDto dto);

}
