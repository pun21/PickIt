package com.spun.pickit.database.handling.crud;

abstract class CRUD implements CRUDable{
    private final static String urlPrefix = "http://www.bcdev.me:8080/";

    protected abstract String readExtension();
    protected abstract String deleteExtension();
    protected abstract String updateExtension();
    protected abstract String createExtension();

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
