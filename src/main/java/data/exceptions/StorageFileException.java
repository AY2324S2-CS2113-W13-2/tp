package data.exceptions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class StorageFileException extends Exception{

    public static final String INVALID_STORAGE_TEXT_DATE_FORMAT_MESSAGE =
            "Invalid date format in tasks.txt file. Please use the format yyyy-MM-dd for dates.";

    public StorageFileException(String errorMessage) {
        super(errorMessage);
    }

    public static void checkStorageTextDateFormat(String date) throws StorageFileException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new StorageFileException(INVALID_STORAGE_TEXT_DATE_FORMAT_MESSAGE);
        }
    }
}
