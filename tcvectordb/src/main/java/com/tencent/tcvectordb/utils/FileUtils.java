package com.tencent.tcvectordb.utils;

import com.tencent.tcvectordb.model.param.enums.FileTypeEnum;

import java.io.File;

public class FileUtils {
    public static String getFileType(File file) {
        String fileName = file.getName();
        String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
        return fileType;
    }
}
