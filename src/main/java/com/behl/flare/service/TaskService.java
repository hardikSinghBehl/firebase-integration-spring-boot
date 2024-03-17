package com.behl.flare.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.behl.flare.dto.TaskCreationRequestDto;
import com.behl.flare.dto.TaskResponseDto;
import com.behl.flare.dto.TaskUpdationRequestDto;
import com.behl.flare.entity.Task;
import com.behl.flare.entity.TaskStatus;
import com.behl.flare.exception.InvalidTaskIdException;
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

	public TaskResponseDto retrieve(@NonNull final String taskId) {
		final var retrievedDocument = get(taskId);
		final var task = retrievedDocument.toObject(Task.class);
		return creatResponse(retrievedDocument, task);
	}
	
	@SneakyThrows
	public List<TaskResponseDto> retrieve() {
		return firestore.collection(Task.name()).get().get().getDocuments().stream().map(document -> {
			final var task = document.toObject(Task.class);
			return creatResponse(document, task);
		}).toList();
	}
	
	public void create(@NonNull final TaskCreationRequestDto taskCreationRequest) {
		final var task = new Task();
		task.setStatus(TaskStatus.NEW);
		task.setTitle(taskCreationRequest.getTitle());
		task.setDescription(taskCreationRequest.getDescription());
		task.setDueDate(dateUtility.convert(taskCreationRequest.getDueDate()));

		save(task);
	}
	
	public void update(@NonNull final String taskId, @NonNull final TaskUpdationRequestDto taskUpdationRequest) {
		final var retrievedDocument = get(taskId);
		final var task = retrievedDocument.toObject(Task.class);
		task.setDescription(taskUpdationRequest.getDescription());
		task.setStatus(taskUpdationRequest.getStatus());
		task.setDueDate(dateUtility.convert(taskUpdationRequest.getDueDate()));
		
		save(task);
	}
	
	public void delete(@NonNull final String taskId) {
		final var document = get(taskId);
		firestore.collection(Task.name()).document(document.getId()).delete();
	}
	
	private void save(@NonNull final Task task) {
		firestore.collection(Task.name()).document().set(task);
	}
	
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
