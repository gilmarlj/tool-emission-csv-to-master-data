package com.inditex.icdmsuscon.az;

import com.azure.identity.InteractiveBrowserCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;
import com.inditex.icdmsuscon.emissions.security.SecurityProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;
import java.util.List;

@Slf4j
public class AzureGetKeysService {

    private final SecretClient secretClient;

    public AzureGetKeysService(String keyVaultUrl) {
        this.secretClient = new SecretClientBuilder()
                .vaultUrl(keyVaultUrl)
                .credential(new InteractiveBrowserCredentialBuilder().build())
                .buildClient();
    }

    public SecurityProperties fetchKey(String secretName, String publicKey) throws Exception {
        if (secretClient.getVaultUrl().isEmpty() || secretName.isEmpty()) {
            throw new IllegalArgumentException("Key Vault URL and Secret Name must be provided");
        }

        KeyVaultSecret privateKeySecret = secretClient.getSecret(secretName);

        if (privateKeySecret != null) {
            String privateKey = privateKeySecret.getValue();
            // Here we assume the public key is stored separately or can be constructed/derived as needed
            // For simplicity, we'll use a placeholder
            List<String> privateKeys = List.of(privateKey);

            return new SecurityProperties(privateKeys, publicKey);
        } else {
            throw new Exception("Failed to fetch secret from Azure Key Vault");
        }
    }
}
