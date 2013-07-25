package iae.home.datagenerator.datamodel;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="object")
public class ObjectModel {
	
	@Attribute(name="class")
	public String msClass;

	@Attribute(name="table")
	public String msTable;
	
	@ElementList(inline=true)
	List<FieldModel> mFields;
}
