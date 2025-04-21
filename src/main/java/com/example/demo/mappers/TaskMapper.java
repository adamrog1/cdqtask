package com.example.demo.mappers;

import com.example.demo.dto.TaskDTO;
import com.example.demo.constants.TaskStatus;
import com.example.demo.models.Task;
import com.example.demo.utils.JsonUtils;

public class TaskMapper {

    public static TaskDTO toDto(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getTaskId() != null ? task.getTaskId() : null);
        dto.setStatus(task.getStatus() != null ? task.getStatus().name() : null);
        dto.setResult(JsonUtils.parseStringToJson(task.getResult()));
        dto.setTaskProgress(task.getProgress().toString() + "%");
        return dto;
    }

    public static Task fromDto(TaskDTO dto) {
        Task task = new Task();
        if (dto.getId() != null) {
            task.setTaskId(dto.getId());
        }
        if (dto.getStatus() != null) {
            task.setStatus(Enum.valueOf(TaskStatus.class, dto.getStatus()));
        }
        task.setResult(JsonUtils.safeStringify(dto.getResult()));
        if (dto.getTaskProgress() != null && dto.getTaskProgress().endsWith("%")) {
            String progressStr = dto.getTaskProgress().substring(0, dto.getTaskProgress().length() - 1);
            task.setProgress(Integer.valueOf(progressStr.trim()));
        }
        return task;
    }
}
