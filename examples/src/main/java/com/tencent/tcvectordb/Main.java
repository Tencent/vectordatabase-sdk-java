package com.tencent.tcvectordb;

import com.tencent.tcvectordb.utils.JsonUtils;
import src.main.java.com.tencent.tcvectordb.VectorDBExampleWithAI_doc;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
//        System.out.println("----------------------------------------------------------");
//        System.out.println("------------------------- example ------------------------");
//        System.out.println("----------------------------------------------------------");
//        com.tencent.tcvectordb.VectorDBExample.example();
//        System.out.println("----------------------------------------------------------");
//        System.out.println("----------------- example with embedding -----------------");
//        System.out.println("----------------------------------------------------------");
//        com.tencent.tcvectordb.VectorDBExampleWithEmbedding.example();
        System.out.println("----------------------------------------------------------");
        System.out.println("----------------- example with ai doc -----------------");
        System.out.println("----------------------------------------------------------");
        VectorDBExampleWithAI_doc.example();
    }
}
