package com.behl.flare.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.behl.flare.dto.TaskCreationRequestDto;
import com.behl.flare.dto.TaskResponseDto;
import com.behl.flare.dto.TaskUpdationRequestDto;
import com.behl.flare.entity.Task;
import com.behl.flare.entity.TaskStatus;
import com.behl.flare.exception.InvalidTaskIdException;
import com.behl.flare.exception.TaskOwnershipViolationException;
import com.behl.flare.utility.AuthenticatedUserIdProvider;
import com.behl.flare.utility.DateUtility;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
public class TaskService {

	private final Firestore firestore;
	private final DateUtility dateUtility;
	private final AuthenticatedUserIdProvider authenticatedUserIdProvider;

	/**
	 * Retrieves task details corresponding to provided taskId.
	 * 
	 * @param taskId the ID of the task to retrieve
	 * @return a TaskResponseDto containing details of the retrieved task
	 * @throws IllegalArgumentException if provided argument is {@code null}
	 * @throws InvalidTaskIdException if no task exists corresponding to given taskId
	 * @throws TaskOwnershipViolationException if retrieved task is not created by current authenticated user
	 */
	public TaskResponseDto retrieve(@NonNull final String taskId) {
		final var retrievedDocument = get(taskId);
		final var task = retrievedDocument.toObject(Task.class);
		verifyTaskOwnership(task);
		
		return creatResponse(retrievedDocument, task);
	}
	
	/**
	 * Retrieves all tasks owned by the current authenticated user.
	 * Returns an empty list if no task record exists for the user.
	 *
	 * @return a list of TaskResponseDto representing the user's tasks
	 */
	@SneakyThrows
	public List<TaskResponseDto> retrieve() {
		final var userId = authenticatedUserIdProvider.getUserId();
		return firestore.collection(Task.name()).whereEqualTo("createdBy", userId)
				.get().get().getDocuments()
				.stream()
				.map(document -> {
					final var task = document.toObject(Task.class);
					return creatResponse(document, task);
				}).toList();
	}
	
	/**
	 * Creates a new task record for the current authenticated user
	 * corresponding to the provided creation request. 
	 * 
	 * @param taskCreationRequest containing task details
	 * @throws IllegalArgumentException if provided argument is {@code null}
	 */
	public void create(@NonNull final TaskCreationRequestDto taskCreationRequest) {
		final var task = new Task();
		task.setStatus(TaskStatus.NEW);
		task.setTitle(taskCreationRequest.getTitle());
		task.setDescription(taskCreationRequest.getDescription());
		task.setDueDate(dateUtility.convert(taskCreationRequest.getDueDate()));
		task.setCreatedBy(authenticatedUserIdProvider.getUserId());

		firestore.collection(Task.name()).document().set(task);
	}
	
	/**
	 * Updates an existing task record corresponding to provided taskId with 
	 * given request details.
	 *
	 * @param taskId the ID of the task to update
	 * @param taskUpdationRequest the request containing updated task details
	 * @throws IllegalArgumentException if provided argument is {@code null}
	 * @throws InvalidTaskIdException if no task exists corresponding to given taskId
	 * @throws TaskOwnershipViolationException if retrieved task is not created by current authenticated user
	 */
	public void update(@NonNull final String taskId, @NonNull final TaskUpdationRequestDto taskUpdationRequest) {
		final var retrievedDocument = get(taskId);
		final var task = retrievedDocument.toObject(Task.class);
		verifyTaskOwnership(task);
		
		task.setDescription(taskUpdationRequest.getDescription());
		task.setStatus(taskUpdationRequest.getStatus());
		task.setDueDate(dateUtility.convert(taskUpdationRequest.getDueDate()));
		
		firestore.collection(Task.name()).document(retrievedDocument.getId()).set(task);
	}
	
	/**
	 * Deletes task record corresponding to provided taskId
	 *
	 * @param taskId the ID of the task to delete
	 * @throws IllegalArgumentException if provided argument is {@code null}
	 * @throws InvalidTaskIdException if no task exists corresponding to given taskId
	 * @throws TaskOwnershipViolationException if retrieved task is not created by current authenticated user
	 */
	public void delete(@NonNull final String taskId) {
		final var document = get(taskId);
		final var task = document.toObject(Task.class);
		verifyTaskOwnership(task);
		
		firestore.collection(Task.name()).document(document.getId()).delete();
	}

	/**
	 * Verifies if the given task belongs to the current authenticated user.
	 *
	 * @param task the record to be verified for ownership.
	 * @throws IllegalArgumentException if provided argument is {@code null}
	 * @throws TaskOwnershipViolationException on validation failure
	 */
	private void verifyTaskOwnership(@NonNull final Task task) {
		final var userId = authenticatedUserIdProvider.getUserId();
		final var taskBelongsToUser = task.getCreatedBy().equals(userId);
		if (Boolean.FALSE.equals(taskBelongsToUser)) {
			throw new TaskOwnershipViolationException();
		}
	}
	
	/**
	 * Retrieves a task document from Firestore database corresponding to
	 * its document ID.
	 *
	 * @param taskId the ID of the task document to retrieve
	 * @return the DocumentSnapshot representing the retrieved task document
	 * @throws InvalidTaskIdException if no task exists corresponding to given taskId
	 */
	@SneakyThrows
	private DocumentSnapshot get(@NonNull final String taskId) {
		final var retrievedDocument = firestore.collection(Task.name()).document(taskId).get().get();
		final var documentExists = retrievedDocument.exists();
		if (Boolean.FALSE.equals(documentExists)) {
			throw new InvalidTaskIdException("No task exists in the system with provided-id");
		}
		return retrievedDocument;
	}

	private TaskResponseDto creatResponse(final DocumentSnapshot document, final Task task) {
		return TaskResponseDto.builder()
				.id(document.getId())
				.title(task.getTitle())
				.status(task.getStatus())
				.description(task.getDescription())
				.createdAt(dateUtility.convert(document.getCreateTime()))
				.updatedAt(dateUtility.convert(document.getUpdateTime()))
				.build();
	}

}
