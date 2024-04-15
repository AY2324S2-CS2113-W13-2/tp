//@@author kyhjonathan
package data.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StorageFileExceptionTest {

    @Test
    public void checkStorageTextDateFormat_validStorageTextDateGiven_noExceptionThrown () {
        // Arrange
        String testStorageTextDate = "2024-12-03";

        // Act and Assert
        assertDoesNotThrow(() -> StorageFileException.checkStorageTextDateFormat(testStorageTextDate));
    }

    @Test
    public void checkStorageTextDateFormat_reversedStorageTextDateGiven_exceptionThrown () {
        // Arrange
        String testStorageTextDate = "03-12-2024";

        // Act and Assert
        StorageFileException thrown = assertThrows(StorageFileException.class, () ->
                StorageFileException.checkStorageTextDateFormat(testStorageTextDate));
        assertEquals("Invalid date format in tasks.txt file. " + "Please use the format yyyy-MM-dd for dates.",
                thrown.getMessage());
    }
}
