package com.modac.server.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modac.server.domain.Multipart;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;

@Converter
public class MultipartAttributeConverter implements AttributeConverter<Multipart, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    public String convertToDatabaseColumn(Multipart multipart) {

        try {
            return objectMapper.writeValueAsString(multipart);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error occurred during convert to JSON: " + e);
        }
    }

    @Override
    public Multipart convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, Multipart.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error occurred during convert to Object: " + e);
        }
    }
}
