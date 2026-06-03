package ru.yandex.practicum.telemetry.analyzer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@Table(name = "sensors",  schema = "analyzer")
@Setter
@Getter
public class Sensor {

    @Id
    private String id;

    private String hubId;

}