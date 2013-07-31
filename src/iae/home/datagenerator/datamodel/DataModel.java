package iae.home.datagenerator.datamodel;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="model")
public class DataModel {

	@Element(name="version",required=false)
	private Integer mVersion=1;
	
	@ElementList(inline=true)
	private List<ObjectModel> mObjectList=null;
	
	public Integer getVersion() { 
		return mVersion;
	}
	
	public List<ObjectModel> getObjects() {
		if(mObjectList==null)
			mObjectList=new ArrayList<ObjectModel>();
		
		return mObjectList;
	}
}
