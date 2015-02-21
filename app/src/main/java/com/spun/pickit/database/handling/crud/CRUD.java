package com.spun.pickit.database.handling.crud;

public abstract class CRUD implements CRUDable{
    protected static final String URL = "www.bcdev.me:8080/";

//for easy pickup
//protected String readExtension(){
//    String extension;
//    return extension;
//}
//    protected String deleteExtension(){
//        String extension;
//        return extension;
//    }
//    protected String updateExtension(){
//        String extension;
//        return extension;
//    }
//    protected String createExtension(){
//        String extension;
//        return extension;
//    }
//
//    protected void readPrimitive(){}
//    protected void deletePrimitive(){}
//    protected void updatePrimitive(){}
//    protected void createPrimitive(){}

    protected abstract String readExtension();
    protected abstract String deleteExtension();
    protected abstract String updateExtension();
    protected abstract String createExtension();

    protected abstract void readPrimitive();
    protected abstract void deletePrimitive();
    protected abstract void updatePrimitive();
    protected abstract void createPrimitive();

    public final void read(){
        String extension = this.URL + readExtension();
        readPrimitive();
    }

    public final void delete(){
        String extension = this.URL + deleteExtension();
        deletePrimitive();
    }
    public final void update(){
        String extension = this.URL + updateExtension();
        updatePrimitive();
    }
    public final void create(){
        String extension = this.URL + createExtension();
        createPrimitive();
    }
}
