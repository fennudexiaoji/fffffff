package com.app.base.bean;

import com.common.lib.retrofit.model.BaseResponse;

/**
 * Created by 7du-28 on 2018/8/8.
 */

public class FileBean{

    /**
     * source_name : 1533714092626.png
     * file_name : 20180808170455_432.png
     * path : uploadfile/multipart/20180808170455_432.png
     */

    private String source_name;
    private String file_name;
    private String path;

    public String getSource_name() {
        return source_name;
    }

    public void setSource_name(String source_name) {
        this.source_name = source_name;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
