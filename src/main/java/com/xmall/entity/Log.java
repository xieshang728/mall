package com.xmall.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Log {
    private Integer id;
    private String name;
    private Integer type;
    private String url;
    private String requestType;
    private String requestParam;
    private String user;
    private String ip;
    private String ipInfo;
    private Integer time;
    private Date createDate;

}
