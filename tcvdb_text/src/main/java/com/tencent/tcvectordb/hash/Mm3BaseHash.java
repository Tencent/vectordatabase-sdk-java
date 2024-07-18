package com.tencent.tcvectordb.hash;

import net.jpountz.xxhash.XXHash32;
import net.jpountz.xxhash.XXHashFactory;

import java.nio.ByteBuffer;

public class Mm3BaseHash implements BaseHash {
    @Override
    public int hash(String text) {
        XXHashFactory factory = XXHashFactory.fastestInstance();
        XXHash32 hash32 = factory.hash32();
        int seed = 0; // You can choose any seed value
        return hash32.hash(ByteBuffer.wrap(text.getBytes()), seed);
    }
}
