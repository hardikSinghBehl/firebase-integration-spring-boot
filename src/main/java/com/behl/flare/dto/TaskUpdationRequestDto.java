package com.behl.flare.dto;

import java.time.LocalDate;

import com.behl.flare.entity.TaskStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(title = "TaskUpdationRequest", accessMode = Schema.AccessMode.WRITE_ONLY)
public class TaskUpdationRequestDto {

	@NotBlank(message = "description must not be empty")
	@Schema(requiredMode = RequiredMode.REQUIRED, description = "description of task", example = "Resolve user authentication issue detailed in Jira#9051")
	private String description;

	@NotNull(message = "Status must not be empty")
	@Schema(requiredMode = RequiredMode.REQUIRED, description = "status of the task", example = "COMPLETED")
	private TaskStatus status;

	@Future(message = "DueDate must be a future date")
	@NotNull(message = "DueDate must not be empty")
	@Schema(requiredMode = RequiredMode.REQUIRED, description = "due-date of the task")
	private LocalDate dueDate;

}
