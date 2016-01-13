package net.money2013.app.datagenandroid.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name="join")
public class ViewJoin {

	@Attribute(name="object")
	private String msObject;

	@Attribute(name="alias")
	private String msAlias;

	@Attribute(name="on")
	private String msOn;
	
	public String getObject() { 
		return msObject; 
	}

	public String getAlias() { 
		return msAlias; 
	}

	public String getOn() { 
		return msOn; 
	}

    @Override
    public String toString() {
        return "ViewJoin{" + "msObject=" + msObject + ", msAlias=" + msAlias + ", msOn=" + msOn + '}';
    }
	
        
}
