package com.tencent.tcvectordb.utils;

import com.tencent.tcvectordb.model.param.enums.FileTypeEnum;

import java.io.File;

public class FileUtils {
    public static FileTypeEnum getFileType(File file) {
        String fileName = file.getName();
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (fileType.equals("") || fileType.equals("md") || fileType.equals("markdown")){
            return FileTypeEnum.MARKDOWN;
        }
        return FileTypeEnum.UNSUPPORT;
    }
}
