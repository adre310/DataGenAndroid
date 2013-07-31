package iae.home.datagenerator.datamodel;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="view")
public class ViewModel {
	@Attribute(name="name")
	private String msName;

	@ElementList(inline=true)
	private List<ViewColumn> mColumns=null;
	
	public String getName() { 
		return msName; 
	}

	public List<ViewColumn> getColumns() {
		if(mColumns==null) 
			mColumns=new ArrayList<ViewColumn>();
		return mColumns;
	}
	
	@Override
	public String toString() {
		return msName;
	}
	
}
