package app;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Main {

	public static void main (String[] args) {

		String start = "2022-12-06";
		String end = "2023-12-26";
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		LocalDate endDate = LocalDate.parse(end, dateTimeFormatter);
		LocalDate startDate = LocalDate.parse(start, dateTimeFormatter);
		System.out.println("endDate = " + endDate + "\nstartDate = " + startDate.toString());
	}
}
