package net.money2013.app.datagenandroid.model;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "model")
public class DataModel {

    @Element(name = "version", required = false)
    private Integer mVersion = 1;

    @ElementList(inline = true)
    private List<ObjectModel> mObjectList = null;

    @ElementList(inline = true)
    private List<ViewModel> mViewList = null;

    public Integer getVersion() {
        return mVersion;
    }

    public List<ObjectModel> getObjects() {
        if (mObjectList == null) {
            mObjectList = new ArrayList<>();
        }
        return mObjectList;
    }

    public List<ViewModel> getViewList() {
        if(mViewList == null) {
            mViewList=new ArrayList<>();
        }
        return mViewList;
    }

}
