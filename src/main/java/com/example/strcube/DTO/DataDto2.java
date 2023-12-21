package com.example.strcube.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataDto2 {
    private String factVariable;
    private List<String> dimensions;
    private String aggregateFunction;
}
