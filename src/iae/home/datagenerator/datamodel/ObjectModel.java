package iae.home.datagenerator.datamodel;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name="object")
@Namespace(reference="http://money2013.net/datagenerator/2013/07/26")
public class ObjectModel {	
	@Attribute(name="name")
	private String msName;

	@Attribute(name="table")
	private String msTable;

	@Element(name="version",required=false)
	private Integer mVersion=1;
	
	@ElementList(inline=true)
	private List<FieldModel> mFields=null;
	
	public Integer getVersion() { 
		return mVersion;
	}

	public String getName() { 
		return msName; 
	}
	
	public String getTable() { 
		return msTable; 
	}
	
	public List<FieldModel> getFields() {
		if(mFields==null) 
			mFields=new ArrayList<FieldModel>();
		return mFields;
	}
}
