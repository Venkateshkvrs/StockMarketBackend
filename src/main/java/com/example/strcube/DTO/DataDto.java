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
public class DataDto {
    private Integer queryId;
    private String factVariable;
    private String aggregateFunction;
    private List<String> groupByAttributes;
    private Double result;
}
