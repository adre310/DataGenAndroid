package iae.home.datagenerator.datamodel;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name="field")
public class FieldModel {

	@Attribute(name="name")
	public String msName;

	@Attribute(name="column")
	public String msColumn;
	
}
