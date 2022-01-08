package com.lulo.bank.transaction.authorizer.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateUtils {
	
	private DateUtils() {}
	
	/**
	 * Convert a text string to LocalDateTime.
	 * @param textDate text date.
	 * @return LocalDateTime response.
	 */
	public static LocalDateTime getLocalDateTime(String textDate) 
	{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		return LocalDateTime.parse(textDate, formatter);
	}

	/**
	 * Get the date with minutes subtracted.
	 * @param date Transaction date time.
	 * @param minutes Minutes to subtract.
	 * @return LocalDateTime.
	 */
	public static LocalDateTime subtractMinutes(LocalDateTime date, int minutes) 
	{
		return date.minusMinutes(minutes);
	}
	
	/**
	 * Get the date with minutes added.
	 * @param date Transaction date time.
	 * @param minutes Minutes to add.
	 * @return LocalDateTime.
	 */
	public static LocalDateTime addMinutes(LocalDateTime date, int minutes) 
	{
		return date.plusMinutes(minutes);
	}
}
