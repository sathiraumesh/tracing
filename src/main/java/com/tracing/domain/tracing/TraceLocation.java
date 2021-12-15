package com.tracing.domain.tracing;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class TraceLocation {

    @NotBlank(message = "longitude is mandatory")
    private float lon;

    @NotBlank(message = "latitude is mandatory")
    private float lat;
}
