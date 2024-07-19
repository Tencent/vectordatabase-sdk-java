package com.tencent.tcvectordb.hash;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import net.jpountz.xxhash.XXHash32;
import net.jpountz.xxhash.XXHashFactory;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Mm3BaseHash implements BaseHash {
    private static final HashFunction HASHING = Hashing.murmur3_32_fixed();
    @Override
    public Long hash(String text) {
        int hashId =  HASHING.hashString(text, StandardCharsets.UTF_8).asInt();
        return (long)hashId & 4294967295L;
    }
}
