package com.tencent.tcvectordb.model.param.entity;

import java.util.List;


public class GetImageUrlRes extends BaseRes{
    private List<List<ImageUrlInfo>> images;


    public List<List<ImageUrlInfo>> getImages() {
        return images;
    }

    public void setImages(List<List<ImageUrlInfo>> images) {
        this.images = images;
    }
}
