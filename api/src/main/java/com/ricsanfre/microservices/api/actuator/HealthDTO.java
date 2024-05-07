package com.ricsanfre.microservices.api.actuator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthDTO {

    private String status;
    private Map<String, Object> details;

}
