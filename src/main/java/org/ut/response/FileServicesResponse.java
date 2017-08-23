package org.ut.response;

import org.ut.entity.ServiceInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FileServicesResponse implements Serializable {

    private List<ServiceInfo> services;

    public FileServicesResponse() {
        this.services = new ArrayList<>();
    }

    public List<ServiceInfo> getServiceInfos() {
        return services;
    }

    public void setServiceInfos(List<ServiceInfo> versions) {
        this.services = versions;
    }

    public void addService(String name, String url, String method) {
        this.services.add(new ServiceInfo(name, url, method));
    }

}
