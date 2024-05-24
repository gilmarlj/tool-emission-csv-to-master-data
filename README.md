# Tool Emission CSV to Master Data

## Prerequisites

- Java Development Kit (JDK) 17

## How to Use

1. **Get the JAR File:**
   Download `application-tool-emission-csv-to-master-data-1.0-SNAPSHOT.jar`.

2. **CSV Format:**
   Ensure your CSV file is in the correct format. Use `test.csv` in the `resources` directory as an example:

3. **RSA Encryption:**
- **Public Key:** Must be provided manually.
- **Private Key:** Can be provided manually or retrieved from the Key Vault.

4. **Azure Key Vault Configuration:**
- **URL:** Specify the vault URL.
- **Secret Name:** Provide the secret name.
- Use browser credentials to authenticate. Ensure the user has appropriate roles to access the key.

5. **Execute the Tool:**
```sh
java -jar application-tool-emission-csv-to-master-data-1.0-SNAPSHOT.jar
```

5. **Output**
  - The application will generate SQL scripts in the log. A log file will also be created.
