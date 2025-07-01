package br.edu.ifrs.poa.pitanga_code.app.config;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.Page;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@JsonComponent
public class PageImplJacksonSerializer extends JsonSerializer<Page<?>> {

    @Override
    public void serialize(Page<?> page, JsonGenerator json, SerializerProvider serializer) throws IOException {
        json.writeStartObject();
        json.writeObjectField("data", page.getContent());
        json.writeNumberField("totalPages", page.getTotalPages());
        json.writeNumberField("totalItems", page.getTotalElements());
        json.writeNumberField("numberOfElements", page.getNumberOfElements());
        json.writeNumberField("currentPage", page.getNumber());
        json.writeEndObject();
    }
}
