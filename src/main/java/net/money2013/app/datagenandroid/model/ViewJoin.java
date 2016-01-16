package net.money2013.app.datagenandroid.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name="join")
public class ViewJoin {
    @Attribute(name="join", required = true)
    private String join;

    public String getJoin() {
        return join;
    }
        
}
