package com.johndoeo.bean;

import java.io.Serializable;
import java.util.List;

public class City implements Serializable {
    private String code;
    private String firstChar;
    private String name;
    private String pinyin;
    private String rank;
    private List<String> subLevelModelList;
    private static final long serialVersionUID = 1L;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public List<String> getSubLevelModelList() {
        return subLevelModelList;
    }

    public void setSubLevelModelList(List<String> subLevelModelList) {
        this.subLevelModelList = subLevelModelList;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
