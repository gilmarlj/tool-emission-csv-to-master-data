# Tool Emission CSV to Master Data

## Prerequisites

- Java Development Kit (JDK) 17

## How to Use

1. **Get the JAR File:**
   Download `application-tool-emission-csv-to-master-data-1.0-SNAPSHOT.jar`.

2. **CSV Format:**
   Ensure your CSV file is in the correct format. Use `test.csv` in the `resources` directory as an example:
```csv
marketId,factor,unitId
18e16f50-639d-45a0-8f63-674ace882d4e,0.1234,f1b1b2ee-15c7-4d64-8643-01534b7633a5
```
3. **RSA Encryption:**
- **Public Key:** Must be provided manually. the public key is used to encrypt the data.

5. **Execute the Tool:**
```sh
java -jar application-tool-emission-csv-to-master-data-1.0-SNAPSHOT.jar
```

5. **Output**
  - The application will generate SQL scripts in the log. A log file will also be created.
