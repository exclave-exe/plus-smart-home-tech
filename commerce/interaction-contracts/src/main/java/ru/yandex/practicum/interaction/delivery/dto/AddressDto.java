package ru.yandex.practicum.interaction.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class AddressDto {

    private String country;
    private String city;
    private String street;
    private String house;
    private String flat;

}