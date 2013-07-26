package iae.home.datagenerator.datamodel;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name="object")
@Namespace(reference="http://money2013.net/datagenerator/2013/07/26")
public class ObjectModel {
	
	@Attribute(name="name")
	public String msClass;

	@Attribute(name="table")
	public String msTable;
	
	@ElementList(inline=true)
	public List<FieldModel> mFields;
}
