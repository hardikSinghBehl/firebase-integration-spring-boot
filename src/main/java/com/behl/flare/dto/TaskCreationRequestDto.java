package com.behl.flare.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Schema(title = "TaskCreationRequest", accessMode = Schema.AccessMode.WRITE_ONLY)
public class TaskCreationRequestDto {

	@NotBlank(message = "Title must not be empty")
	@Schema(requiredMode = RequiredMode.REQUIRED, description = "title of the task", example = "Fix Bug: User Authentication")
	private String title;

	@NotBlank(message = "Description must not be empty")
	@Schema(requiredMode = RequiredMode.REQUIRED, description = "description of the task", example = "Resolve user authentication issue detailed in Jira#9051")
	private String description;

	@FutureOrPresent(message = "DueDate must be a future date")
	@NotNull(message = "DueDate must not be empty")
	@Schema(requiredMode = RequiredMode.REQUIRED, description = "due-date of the task")
	private LocalDate dueDate;

}
