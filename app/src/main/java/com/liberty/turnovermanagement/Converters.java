package com.liberty.turnovermanagement;

import androidx.room.TypeConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Converters {
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @TypeConverter
    public LocalDateTime fromTimestamp(String value) {
        return value == null ? null : LocalDateTime.parse(value, formatter);
    }

    @TypeConverter
    public String dateToTimestamp(LocalDateTime date) {
        return date == null ? null : date.format(formatter);
    }
}
