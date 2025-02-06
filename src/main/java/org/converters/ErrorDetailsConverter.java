package org.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.models.ErrorDetails;

public class ErrorDetailsConverter {

    public ErrorDetails convert(String payload) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(payload, ErrorDetails.class);
    }
}
