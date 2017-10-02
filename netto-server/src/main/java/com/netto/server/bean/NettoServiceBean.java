package com.netto.server.bean;

import org.springframework.beans.factory.FactoryBean;

public class NettoServiceBean implements FactoryBean<Object> {
    private ServiceBean serviceBean;

    private Object refBean;

    public NettoServiceBean(ServiceBean serviceBean, Object refBean) {
        this.serviceBean = serviceBean;
        this.refBean = refBean;
    }

    public ServiceBean getServiceBean() {
        return serviceBean;
    }

    @Override
    public Object getObject() {
        return this.refBean;
    }

    @Override
    public Class<?> getObjectType() {
        return this.refBean.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
