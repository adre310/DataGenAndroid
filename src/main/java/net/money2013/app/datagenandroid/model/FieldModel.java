package net.money2013.app.datagenandroid.model;

import net.money2013.app.datagenandroid.utils.Utils;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "field")
public class FieldModel {

    @Attribute(name = "name", required = true)
    private String msName;

    @Attribute(name = "column", required = true)
    private String msColumn;

    @Attribute(name = "type", required = true)
    private String msType;

    @Attribute(name = "required", required = false)
    private Boolean msRequired = false;

    public String getName() {
        return msName;
    }

    public String getColumn() {
        return msColumn;
    }

    public String getType() {
        return msType;
    }

    public Boolean getRequired() {
        return msRequired;
    }

}
