package com.behl.flare.dto;

import java.time.LocalDateTime;

import com.behl.flare.entity.TaskStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
@Schema(title = "TaskResponse", accessMode = Schema.AccessMode.READ_ONLY)
public class TaskResponseDto {

	private String id;
	private String title;
	private String description;
	private TaskStatus status;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

}
