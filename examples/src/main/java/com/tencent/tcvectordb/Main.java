package com.tencent.tcvectordb;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("----------------------------------------------------------");
        System.out.println("------------------------- example ------------------------");
        System.out.println("----------------------------------------------------------");
        VectorDBExample.example();
        System.out.println("----------------------------------------------------------");
        System.out.println("----------------- example with embedding -----------------");
        System.out.println("----------------------------------------------------------");
        VectorDBExampleWithEmbedding.example();
    }
}
