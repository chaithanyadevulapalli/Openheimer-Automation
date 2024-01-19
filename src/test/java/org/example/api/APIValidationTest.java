package org.example.api;

import org.testng.annotations.Test;
import org.example.dataBase.DatabaseVerification;

public class APIValidationTest {

    @Test
    public void testCreateWorkingClassHero() {
        APITest apiTest = new APITest();
                // Verify in the database
        assert DatabaseVerification.isRecordPresent("natid-13");
    }
}
