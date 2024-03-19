package com.behl.flare.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.behl.flare.dto.ExceptionResponseDto;
import com.behl.flare.dto.TaskCreationRequestDto;
import com.behl.flare.dto.TaskResponseDto;
import com.behl.flare.dto.TaskUpdationRequestDto;
import com.behl.flare.service.TaskService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
@Tag(name = "Task Management", description = "Endpoints for managing tasks.")
public class TaskController {

	private final TaskService taskService;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Creates a new task record", description = "Creates a new task with provided details")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Task created successfully",
					content = @Content(schema = @Schema(implementation = Void.class))),
			@ApiResponse(responseCode = "401", description = "Authentication failure: Invalid access token",
					content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))),
			@ApiResponse(responseCode = "400", description = "Invalid request body",
					content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))) })
	public ResponseEntity<HttpStatus> create(@Valid @RequestBody TaskCreationRequestDto taskCreationRequest) {
		taskService.create(taskCreationRequest);
		return ResponseEntity.ok().build();
	}

	@GetMapping(value = "/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Retrieves task details", description = "Retrieve details of a specific task by its ID")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Task details retrieved successfully"),
			@ApiResponse(responseCode = "404", description = "No task exists in the system with provided-id",
					content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))),
			@ApiResponse(responseCode = "401", description = "Authentication failure: Invalid access token",
					content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))),
			@ApiResponse(responseCode = "403", description = "Access denied: Insufficient permissions",
					content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))) })
	public ResponseEntity<TaskResponseDto> retrieve(
			@PathVariable(required = true, name = "taskId") final String taskId) {
		final var response = taskService.retrieve(taskId);
		return ResponseEntity.ok(response);
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Retrieves all tasks of authenticated user", description = "Retrieve details of all tasks corresponding to authenticated user")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Task details retrieved successfully"),
			@ApiResponse(responseCode = "401", description = "Authentication failure: Invalid access token",
					content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))) })
	public ResponseEntity<List<TaskResponseDto>> retrieve() {
		final var response = taskService.retrieve();
		return ResponseEntity.ok(response);
	}

	@PutMapping(value = "/{taskId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Updates task details", description = "Update details of a specified task by its ID")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Task details updated successfully",
					content = @Content(schema = @Schema(implementation = Void.class))),
			@ApiResponse(responseCode = "404", description = "No task exists in the system with provided-id",
					content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))),
			@ApiResponse(responseCode = "401", description = "Authentication failure: Invalid access token",
					content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))),
			@ApiResponse(responseCode = "403", description = "Access denied: Insufficient permissions",
					content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))) })
	public ResponseEntity<HttpStatus> update(@PathVariable(required = true, name = "taskId") final String taskId,
			@Valid @RequestBody TaskUpdationRequestDto taskUpdationRequest) {
		taskService.update(taskId, taskUpdationRequest);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping(value = "/{taskId}")
	@Operation(summary = "Deletes a task record", description = "Delete a specific task by its ID")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Task deleted successfully",
					content = @Content(schema = @Schema(implementation = Void.class))),
			@ApiResponse(responseCode = "404", description = "No task exists in the system with provided-id",
					content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))),
			@ApiResponse(responseCode = "401", description = "Authentication failure: Invalid access token",
					content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))),
			@ApiResponse(responseCode = "403", description = "Access denied: Insufficient permissions",
					content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))) })
	public ResponseEntity<HttpStatus> delete(@PathVariable(required = true, name = "taskId") final String taskId) {
		taskService.delete(taskId);
		return ResponseEntity.ok().build();
	}

}