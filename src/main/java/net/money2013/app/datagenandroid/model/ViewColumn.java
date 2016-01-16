package net.money2013.app.datagenandroid.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "column")
public class ViewColumn {

    @Attribute(name = "name", required = true)
    private String msName;

    public String getName() {
        return msName;
    }

    @Attribute(name = "expr", required = true)
    private String msExpr;

    public String getExpr() {
        return msExpr;
    }

    @Attribute(name="type",required = true)
    private String msType;

    public String getType() {
        return msType;
    }
        
}
