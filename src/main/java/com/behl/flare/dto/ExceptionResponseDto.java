package com.behl.flare.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(title = "Error", accessMode = Schema.AccessMode.READ_ONLY)
public class ExceptionResponseDto<T> {

	private String status;
	private T description;

}