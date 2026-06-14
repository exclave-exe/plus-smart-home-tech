package ru.yandex.practicum.shopping.store.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.io.IOException;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonSortCustomizer() {
        return builder -> builder.serializerByType(Sort.class, new JsonSerializer<Sort>() {

            @Override
            public void serialize(

                    Sort value,
                    JsonGenerator gen,
                    SerializerProvider serializers

            ) throws IOException {

                gen.writeStartArray();
                for (Sort.Order order : value) {
                    gen.writeStartObject();
                    gen.writeStringField("property", order.getProperty());
                    gen.writeStringField("direction", order.getDirection().name());
                    gen.writeBooleanField("ignoreCase", order.isIgnoreCase());
                    gen.writeEndObject();
                }
                gen.writeEndArray();
            }
        });
    }
}
