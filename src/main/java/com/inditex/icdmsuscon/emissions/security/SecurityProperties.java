package com.inditex.icdmsuscon.emissions.security;

import java.util.List;

public record SecurityProperties(List<String> privateKeys, String publicKey) { }
