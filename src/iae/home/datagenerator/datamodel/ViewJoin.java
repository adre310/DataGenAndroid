package iae.home.datagenerator.datamodel;

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
	
}
