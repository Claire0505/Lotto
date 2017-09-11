package com.admin.claire.lotto.model;

import java.util.Date;
import java.util.Locale;

/**
 * Created by claire on 2017/9/3.
 */

public class Betting {
    private long id;
    private String bettingNum;
    private long dateCreated;


    public Betting(){
        bettingNum = "";
    }

    public Betting(long id, String bettingNum, long dateCreated) {
        this.id = id;
        this.bettingNum = bettingNum;
        this.dateCreated = dateCreated;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBettingNum() {
        return bettingNum;
    }

    public void setBettingNum(String bettingNum) {
        this.bettingNum = bettingNum;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getLocalDateTime() {
        return String.format(Locale.getDefault(), "%tF %<tR", new Date(dateCreated));
    }
}
