package com.inditex.icdmsuscon.emissions.security;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class CryptoConverter {

  private final SecurityProperties securityProperties;
  

  protected String decrypt(final String encryptedContent) throws CryptoException {
    if (encryptedContent == null) {
      return null;
    }

    final List<PrivateKey> privateKeys = this.getPrivateKeys();

    for (final PrivateKey privateKey : privateKeys) {
      try {
        final byte[] data = Base64.getDecoder().decode(encryptedContent);
        final byte[] decryptedBytes = this.cipherIt(privateKey, Cipher.DECRYPT_MODE, data);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
      } catch (final Exception ex) {
        log.error("Error decrypting data", ex);
      }
    }

    throw new CryptoException("None of the private keys could decrypt the data");
  }

  private byte[] cipherIt(final Key key, final int mode, final byte[] data)
      throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
    cipher.init(mode, key);
    return cipher.doFinal(data);
  }

  public String encrypt(final String data) throws CryptoException {
    String result = null;
    if (data != null) {
      final PublicKey publicKey = this.getPublicKey();
      try {
        final byte[] encryptedContent = this.cipherIt(publicKey, Cipher.ENCRYPT_MODE, data.getBytes());
        result = Base64.getEncoder().encodeToString(encryptedContent);
      } catch (final Exception ex) {
        throw new CryptoException(ex);
      }
    }
    return result;
  }

  private List<PrivateKey> getPrivateKeys() throws CryptoException {

    if (this.securityProperties == null
        || this.securityProperties.privateKeys() == null || this.securityProperties.privateKeys().isEmpty()) {
      throw new CryptoException("No private keys found");
    }

    final List<PrivateKey> privateKeys = new ArrayList<>();
    try {
      for (final String key : this.securityProperties.privateKeys()) {
        final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(key.getBytes()));
        final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        privateKeys.add(keyFactory.generatePrivate(keySpec));
      }
    } catch (final Exception ex) {
      throw new CryptoException(ex);
    }
    return privateKeys;
  }

  private PublicKey getPublicKey() throws CryptoException {
    if (this.securityProperties == null || this.securityProperties.publicKey() == null) {
      throw new CryptoException("No public key found");
    }
    try {
      final X509EncodedKeySpec keySpec =
          new X509EncodedKeySpec(Base64.getDecoder().decode(this.securityProperties.publicKey().getBytes()));
      final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      return keyFactory.generatePublic(keySpec);
    } catch (final Exception ex) {
      throw new CryptoException(ex);
    }
  }
}
