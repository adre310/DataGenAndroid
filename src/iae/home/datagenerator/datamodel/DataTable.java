package iae.home.datagenerator.datamodel;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root
public class DataTable {
	
	@Attribute(name="class")
	public String ClassName;

}
