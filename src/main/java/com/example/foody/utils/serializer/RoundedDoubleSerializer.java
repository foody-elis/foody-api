package com.example.foody.utils.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Custom serializer for rounding double values to one decimal place.
 */
public class RoundedDoubleSerializer extends JsonSerializer<Double> {

    /**
     * Serializes a Double value by rounding it to one decimal place.
     *
     * @param value the Double value to serialize
     * @param gen the JsonGenerator used to write the JSON output
     * @param serializers the SerializerProvider that can be used to get serializers for serializing Objects value contains
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            gen.writeNumber(Math.round(value * 10.0) / 10.0);
        } else {
            gen.writeNull();
        }
    }
}