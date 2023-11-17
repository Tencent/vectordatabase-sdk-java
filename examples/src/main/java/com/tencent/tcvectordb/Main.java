package com.tencent.tcvectordb;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("----------------------------------------------------------");
        System.out.println("------------------------- example ------------------------");
        System.out.println("----------------------------------------------------------");
        com.tencent.tcvectordb.VectorDBExample.example();
        System.out.println("----------------------------------------------------------");
        System.out.println("----------------- example with embedding -----------------");
        System.out.println("----------------------------------------------------------");
        com.tencent.tcvectordb.VectorDBExampleWithEmbedding.example();
    }
}
