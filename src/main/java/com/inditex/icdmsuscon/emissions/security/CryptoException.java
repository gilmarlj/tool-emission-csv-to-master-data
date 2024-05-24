package com.inditex.icdmsuscon.emissions.security;

public class CryptoException extends Exception {

  public CryptoException(String message) {
    super(message);
  }

  public CryptoException(Exception ex) {
    super(ex);
  }

  public CryptoException(String message, Exception ex) {
    super(message, ex);
  }
}
