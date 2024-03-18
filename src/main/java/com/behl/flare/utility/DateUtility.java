package com.behl.flare.utility;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.google.cloud.Timestamp;

import lombok.NonNull;

@Component
public class DateUtility {

	/**
	 * Converts the given {@link LocalDate} to a {@link Date} object. 
	 * The resulting Date will represent the start of the day at UTC timezone.
	 * 
	 * @param localDate to convert
	 * @return the converted Date
	 * @throws IllegalArgumentException if provided argument is {@code null}
	 */
	public Date convert(@NonNull final LocalDate localDate) {
		return Date.from(localDate.atStartOfDay(ZoneOffset.UTC).toInstant());
	}

	/**
	 * Converts the given {@link Timestamp} to a {@link LocalDateTime object}. The
	 * result will be represented in UTC timezone.
	 * 
	 * @param timestamp to convert
	 * @return the converted LocalDateTime
	 * @throws IllegalArgumentException if provided argument is {@code null}
	 */
	public LocalDateTime convert(@NonNull final Timestamp timestamp) {
		return timestamp.toDate().toInstant().atZone(ZoneOffset.UTC).toLocalDateTime();
	}

}
