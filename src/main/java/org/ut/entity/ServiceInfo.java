package org.ut.entity;

public class ServiceInfo {

    public static final String M_GET = "get";
    public static final String M_POST = "post";
    public static final String M_PUT = "put";
    public static final String M_DELETE = "delete";

    private String name;
    private String url;
    private String method;

    public ServiceInfo(String name, String url, String method) {
        this.name = name;
        this.url = url;
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
