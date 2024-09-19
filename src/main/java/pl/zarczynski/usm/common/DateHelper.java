package pl.zarczynski.usm.common;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateHelper {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss z");
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss z").withZone(ZoneId.systemDefault());

	public static String parseDate(Date date) {
		return sdf.format(date);
	}

	public static String parseDate(ZonedDateTime zonedDateTime) {
		return formatter.format(zonedDateTime);
	}
}
