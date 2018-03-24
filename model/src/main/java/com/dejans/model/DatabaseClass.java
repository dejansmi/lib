package com.dejans.model;

public interface DatabaseClass {

    public String typeClass ();
    public String nameClass ();
    public String getStatus();
    public Object get(String itemName);
    public Object getValueForPrimaryKey(String itemName);
    public boolean isChanged(String itemName);
    public void set(String itemName, Object value);
    public void setStatus(String status);


}