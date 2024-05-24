package com.inditex.icdmsuscon.gui;

public class HelpContent {

    public static String getHelpMessage() {
        return "How to use the Emission CSV to SQL Tool:\n" +
                "\n" +
                "1. Enter your public and private keys in the respective fields.\n" +
                "2. Click on 'Open File' to select a CSV file containing emission data.\n" +
                "3. The CSV file should follow this format:\n" +
                "\n" +
                "   marketId,factor,unitId\n" +
                "   34349e01-badc-4820-8e35-880c72597257,0.8898,b5ddb418-e1e0-46e7-babd-49c230b959dc\n" +
                "\n" +
                "4. Click on 'Generate' to generate SQL insert statements from the CSV data.\n" +
                "5. The generated SQL statements will be logged in the log area below.\n" +
                "\n" +
                "Ensure your CSV file is properly formatted with the correct headers and data types.";
    }
}