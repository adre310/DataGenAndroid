package iae.home.datagenerator.datamodel;

import java.util.HashMap;
import java.util.Map;

import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

@Root(name = "resources")
public class ResStrings {

    @ElementMap(entry = "string", key = "name", attribute = true, inline = true)
    private Map<String, String> mStrings;

    public Map<String, String> getStrings() {
        return mStrings;
    }

    public ResStrings() {
        mStrings = new HashMap<>();
    }

}
