package org.example.utils;

import org.apache.poi.hssf.record.PageBreakRecord;
import org.testng.Assert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileVerification {

    public static boolean dataCheck(String filepath) {
        boolean check = false;
        //String filePath = "path/to/downloaded/taxrelief.txt"; // Update with the actual path

        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            int recordCount = 0;
           // Assert.assertTrue(fileExists(filepath),"File does not exist at path");
            if (!fileExists(filepath)) {
                System.out.println("File does not exist at path: " + filepath);
                return false;

            }

            while ((line = reader.readLine()) != null) {
                // Assuming each line is in the format: <natid>, <tax relief amount>
                String[] parts = line.split(",");

                // Assuming the last part is the tax relief amount
                String taxReliefAmount = parts[parts.length - 1].trim();

                // Verify if it's a numeric value (you can add more validations based on your data)
                if (isNumeric(taxReliefAmount)) {
                    System.out.println("Valid record: " + line);
                    recordCount++;
                } else {
                    System.out.println("Invalid record: " + line);
                }

                lines.add(line);
            }

            // Read the footer (last line) to get the total number of records
            String footer = lines.get(lines.size() - 1);
            int totalRecords = Integer.parseInt(footer.trim());
            System.out.println("Total number of records: " + totalRecords);

            // Verify if the total number of records matches the actual record count
            if (totalRecords == recordCount-1) {
                System.out.println("Verification passed: Total records match the count.");
                check =  true;
            } else {
                System.out.println("Verification failed: Total records do not match the count.");
                check =  false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return check;
        }
        return check;
    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isFile();
    }
}
