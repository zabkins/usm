package pl.zarczynski.usm.common;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateHelper {

	public static final String entityDateFormat = "dd/MM/yyyy HH:mm:ss z";
	public static final String dtoDateFormat = "dd/MM/yyyy HH:mm:ss";

	private static final SimpleDateFormat sdf = new SimpleDateFormat(entityDateFormat);
	private static final DateTimeFormatter formatterToString = DateTimeFormatter.ofPattern(entityDateFormat).withZone(ZoneId.systemDefault());
	private static final DateTimeFormatter formatterFromString = DateTimeFormatter.ofPattern(dtoDateFormat).withZone(ZoneId.systemDefault());

	public static String parseDate(Date date) {
		return sdf.format(date);
	}

	public static String parseDate(ZonedDateTime zonedDateTime) {
		return formatterToString.format(zonedDateTime);
	}

	public static ZonedDateTime parseStringToZonedDateTime (String date) {
		return ZonedDateTime.parse(date, formatterFromString);
	}
}
