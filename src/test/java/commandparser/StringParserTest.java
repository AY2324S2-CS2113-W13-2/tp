package commandparser;

import org.junit.jupiter.api.Test;

import data.exceptions.TaskManagerException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class StringParserTest {

    @Test
    void parseDate_validDateFormatGiven_noExceptionThrown() {
        String validDateString = "15/03/2023";
        LocalDate expectedDate = LocalDate.of(2023, 3, 15);
        assertDoesNotThrow(() -> {
            LocalDate parsedDate = StringParser.parseDate(validDateString);
            assertEquals(expectedDate, parsedDate);
        });
    }

    @Test
    void parseDate_invalidDateFormatGiven_exceptionThrown() {
        String invalidDateString = "30-02-2023";
        assertThrows(TaskManagerException.class, ()
                -> StringParser.parseDate(invalidDateString));
    }

    @Test
    void parseDate_emptyStringGiven_exceptionThrown() {
        String emptyDateString = "";
        assertThrows(TaskManagerException.class, () -> StringParser.parseDate(emptyDateString));
    }

    @Test
    void parseDate_invalidMonthGiven_exceptionThrown() {
        String invalidDateString = "15/13/2023";
        assertThrows(TaskManagerException.class, () -> StringParser.parseDate(invalidDateString));
    }

    @Test
    void parseDate_invalidDayGiven_exceptionThrown() {
        String invalidDateString = "32/12/2023";
        assertThrows(TaskManagerException.class, () -> StringParser.parseDate(invalidDateString));
    }

    @Test
    void parseDate_leapYearGiven_noExceptionThrown() throws TaskManagerException {
        String leapYearDateString = "29/02/2024";
        LocalDate expectedDate = LocalDate.of(2024, 2, 29);
        LocalDate parsedDate = StringParser.parseDate(leapYearDateString);
        assertEquals(expectedDate, parsedDate);
    }

    @Test
    void arseTaskIndex_validIntegerGiven_noExceptionThrown() {
        String validIndexString = "5";
        int expectedIndex = 5;
        assertDoesNotThrow(() -> {
            int parsedIndex = StringParser.parseTaskIndex(validIndexString);
            assertEquals(expectedIndex, parsedIndex);
        });
    }

    @Test
    void parseTaskIndex_invalidIntegerGiven_exceptionThrown() {
        String invalidIndexString = "five";
        assertThrows(TaskManagerException.class, () -> StringParser.parseTaskIndex(invalidIndexString));
    }

    @Test
    void arseTaskDescription_validDescriptionGiven_correctDescriptionReturned() {
        String description = "Valid task description";
        String expectedDescription = "Valid task description";
        assertEquals(expectedDescription, StringParser.parseTaskDescription(description));
    }

    @Test
    void parsePriorityLevel_validHighPriorityGiven_noExceptionThrown() {
        String priority = "H";
        String expectedPriority = "H";
        assertDoesNotThrow(() -> {
            String parsedPriority = StringParser.parsePriorityLevel(priority);
            assertEquals(expectedPriority, parsedPriority);
        });
    }

    @Test
    void parsePriorityLevel_validPriorityGiven_noExceptionThrown() {
        String priority = "m";
        String expectedPriority = "M";
        assertDoesNotThrow(() -> {
            String parsedPriority = StringParser.parsePriorityLevel(priority);
            assertEquals(expectedPriority, parsedPriority);
        });
    }

    @Test
    void parsePriorityLevel_validLowPriorityGiven_noExceptionThrown() {
        String priority = "L";
        String expectedPriority = "L";
        assertDoesNotThrow(() -> {
            String parsedPriority = StringParser.parsePriorityLevel(priority);
            assertEquals(expectedPriority, parsedPriority);
        });
    }

    @Test
    void parsePriorityLevel_invalidPriorityGiven_exceptionThrown() {
        String invalidPriority = "X";
        assertThrows(TaskManagerException.class, () -> StringParser.parsePriorityLevel(invalidPriority));
    }

    @Test
    void validateAddCommand_validFormatGiven_noExceptionThrown() {
        // Ensure the day is within the range of the current week starting on Sunday.
        String[] validParts = {"add", "12", "taskType", "taskDescription"}; // Adjusted to Sunday
        LocalDate startOfWeek = LocalDate.of(2024, 4, 7); // Sunday of the week
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        assertDoesNotThrow(() -> StringParser.validateAddCommand(validParts));
    }

    @Test
    void validateAddCommand_invalidFormatGiven_exceptionThrown() {
        String[] invalidParts = {"add", "15/03/2023", "taskDescription"};
        assertThrows(TaskManagerException.class, () ->
                StringParser.validateAddCommand(invalidParts));
    }

    @Test
    void checkStorageTextDateFormat_validStorageTextDateGiven_noExceptionThrown() {
        String[] validParts = {"update", "15/03/2023", "1", "newDescription"};
        assertDoesNotThrow(() -> StringParser.validateUpdateCommand(validParts));
    }

    @Test
    void validateUpdateCommand_invalidFormatGiven_exceptionThrown() {
        String[] invalidParts = {"update", "15/03/2023", "newDescription"};
        assertThrows(TaskManagerException.class, () ->
                StringParser.validateUpdateCommand(invalidParts));
    }

    @Test
    void validateDeleteCommand_validFormatGiven_noExceptionThrown() {
        String[] validParts = {"delete", "15/03/2023", "1"};
        assertDoesNotThrow(() -> StringParser.validateDeleteCommand(validParts));
    }

    @Test
    void validateDeleteCommand_invalidFormatGiven_exceptionThrown() {
        String[] invalidParts = {"delete", "15/03/2023"};
        assertThrows(TaskManagerException.class, () ->
                StringParser.validateDeleteCommand(invalidParts));
    }

    @Test
    void validateMarkCommand_validFormatGiven_noExceptionThrown() {
        String[] validParts = {"mark", "15/03/2023", "1"};
        assertDoesNotThrow(() -> StringParser.validateMarkCommand(validParts));
    }

    @Test
    void validateMarkCommand_invalidFormatGiven_exceptionThrown() {
        String[] invalidParts = {"mark", "15/03/2023"};
        assertThrows(TaskManagerException.class, () ->
                StringParser.validateMarkCommand(invalidParts));
    }

    @Test
    void validatePriorityCommand_validFormatGiven_noExceptionThrown() {
        String[] validParts = {"priority", "15/03/2023", "1", "H"};
        assertDoesNotThrow(() -> StringParser.validatePriorityCommand(validParts));
    }

    @Test
    void validatePriorityCommand_invalidFormatGiven_exceptionThrown() {
        String[] invalidParts = {"priority", "15/03/2023", "1"};
        assertThrows(TaskManagerException.class, () ->
                StringParser.validatePriorityCommand(invalidParts));
    }
}
