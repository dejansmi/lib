package com.dejans.model;

import java.util.List;

public interface DatabaseClassList<T> {

    public String typeClass ();
    public String nameClass();
    public String nameClassBase();
    public String[] getxItems();
    public String[] getxType();
    public String[] getxPrimaryKey ();
    public String[] getxPrimaryKeyType ();
    public List<T> getList();
    public DatabaseClass newObject();
    public void add(Object obj);

}