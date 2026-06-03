package ru.yandex.practicum.telemetry.analyzer.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;

@Entity
@Table(name = "conditions", schema = "analyzer")
@Setter
@Getter
public class Condition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ConditionTypeAvro type;

    @Enumerated(EnumType.STRING)
    private Operation operation;

    private Integer value;

    @Transient
    public boolean check(int sensorValue) {
        return operation.apply(sensorValue, this.value);
    }

}
