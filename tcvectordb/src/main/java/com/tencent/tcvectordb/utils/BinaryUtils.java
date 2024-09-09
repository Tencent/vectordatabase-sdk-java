package com.tencent.tcvectordb.utils;

import com.tencent.tcvectordb.exception.ParamException;

import java.util.List;

public class BinaryUtils {
    /**
     * Convert the binary array into an uint8 array, grouping every eight bits.
     * @param binaryArray  example: [1,0,1,0,1,0,1,0]
     * @return
     */
    public static List<Integer> binaryToUint8(List<Integer> binaryArray)  {
        if (binaryArray.size() %8 != 0) {
            throw new ParamException("binaryArray size must be multiple of 8");
        }
        List<Integer> uint8Array = new java.util.ArrayList<Integer>();
        for (int i = 0; i < binaryArray.size(); i += 8) {
            int value = 0;
            for (int j = 0; j < 8; j++) {
                value = value << 1;
                if (binaryArray.get(i + j) == 1) {
                    value = value | 1;
                }
            }
            uint8Array.add(value);
        }
        return uint8Array;
    }
}
