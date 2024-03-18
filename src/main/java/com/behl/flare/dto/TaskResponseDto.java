package com.behl.flare.dto;

import java.time.LocalDateTime;

import com.behl.flare.entity.TaskStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
@Schema(title = "TaskResponse", accessMode = Schema.AccessMode.READ_ONLY)
@JsonNaming(value = PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class TaskResponseDto {

	private String id;
	private String title;
	private String description;
	private TaskStatus status;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

}
