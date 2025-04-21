package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskDTO {

    private String id;
    private String status;
    private JsonNode result;
    private String taskProgress;
}
