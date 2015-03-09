package com.spun.pickit.database.handling.crud;

abstract class CRUD implements CRUDable{
    private final static String urlPrefix = "http://www.bcdev.me:8080/";

    public abstract String readExtension();
    public abstract String deleteExtension();
    public abstract String updateExtension();
    public abstract String createExtension();

    public final String read(){
        return this.urlPrefix + readExtension();
    }

    public final String delete(){
        return this.urlPrefix + deleteExtension();
    }
    public final String update(){
        return this.urlPrefix + updateExtension();
    }
    public final String create(){
        return this.urlPrefix + createExtension();
    }
}
