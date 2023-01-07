package app.model;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateAdapter extends XmlAdapter<String, LocalDate> {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public String marshal(LocalDate dateTime) {
        return dateTime.format(dateTimeFormatter);
    }

    @Override
    public LocalDate unmarshal(String dateTime) {
        return LocalDate.parse(dateTime, dateTimeFormatter);
    }
}
