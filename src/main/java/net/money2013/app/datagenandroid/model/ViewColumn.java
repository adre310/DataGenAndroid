package net.money2013.app.datagenandroid.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name="column")
public class ViewColumn {
	@Attribute(name="name")
	private String msName;

	public String getName() { 
		return msName; 
	}

	@Attribute(name="expr")
	private String msExpr;

	public String getExpr() { 
		return msExpr; 
	}
	
	@Override
	public String toString() {
		return msName;
	}

}
