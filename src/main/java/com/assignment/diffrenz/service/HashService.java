package com.assignment.diffrenz.service;

import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Service;
import org.bouncycastle.jcajce.provider.digest.SHA256;
@Service
public class HashService {

    public String hashString(String input) {
        SHA256.Digest sha256Digest = new SHA256.Digest();
        byte[] hash = sha256Digest.digest(input.getBytes());

        return new String(Hex.encode(hash));
    }
}
