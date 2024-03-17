package com.behl.flare.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
@Schema(title = "TokenSuccessResponse", accessMode = Schema.AccessMode.READ_ONLY)
public class TokenSuccessResponseDto {

	private String accessToken;

}