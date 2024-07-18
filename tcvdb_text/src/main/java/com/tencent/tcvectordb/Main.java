package com.tencent.tcvectordb;

import com.tencent.tcvectordb.encoder.SparseVectorBm25Encoder;

public class Main {
    public static void main(String[] args) {
        SparseVectorBm25Encoder encoder = SparseVectorBm25Encoder.getDefaultBm25Encoder();
        System.out.println(encoder.toString());
        System.out.println("Hello world!");
    }
}