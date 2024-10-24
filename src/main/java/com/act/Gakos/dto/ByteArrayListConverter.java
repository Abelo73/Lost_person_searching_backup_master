package com.act.Gakos.dto;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Converter
public class ByteArrayListConverter implements AttributeConverter<List<byte[]>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<byte[]> attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error serializing byte array list to JSON", e);
        }
    }

    @Override
    public List<byte[]> convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(dbData, List.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error deserializing JSON to byte array list", e);
        }
    }
}
