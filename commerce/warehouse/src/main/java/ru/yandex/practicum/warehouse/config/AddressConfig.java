package ru.yandex.practicum.warehouse.config;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.interaction.delivery.dto.AddressDto;

import java.security.SecureRandom;

@Component
public class AddressConfig {

    private static final String[] ADDRESSES = new String[]{"ADDRESS_1", "ADDRESS_2"};
    private static final String CURRENT_ADDRESS = ADDRESSES[new SecureRandom().nextInt(ADDRESSES.length)];

    public AddressDto getWarehouseAddress() {
        return new AddressDto(CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS);
    }

}