package net.money2013.app.datagenandroid.model;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "view")
public class ViewModel {

    @Attribute(name = "name")
    private String msName;

    @Attribute(name = "table", required = true)
    private String msTable;

    @Attribute(name = "groupBy", required = false)
    private String msGroup = null;

    @Attribute(name="uri", required = true)
    private String msUri;
        
    @ElementList(inline = true)
    private List<ViewColumn> mColumns = null;

    @ElementList(inline = true, required = false)
    private List<ViewJoin> mJoins = null;

    public String getName() {
        return msName;
    }

    public String getTable() {
        return msTable;
    }
    
    public String getGroupBy() {
        return msGroup;
    }

    public boolean getIsGroupByExists() {
        return msGroup != null;
    }

    public List<ViewColumn> getColumns() {
        if (mColumns == null) {
            mColumns = new ArrayList<ViewColumn>();
        }
        return mColumns;
    }

    public List<ViewJoin> getJoins() {
        if (mJoins == null) {
            mJoins = new ArrayList<ViewJoin>();
        }
        return mJoins;
    }

    public String getUri() {
        return msUri;
    }

    public void setUri(String msUri) {
        this.msUri = msUri;
    }
   
}
