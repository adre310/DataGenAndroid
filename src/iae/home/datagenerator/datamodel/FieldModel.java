package iae.home.datagenerator.datamodel;

import iae.home.datagenerator.utils.Utils;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name="field")
public class FieldModel {

	@Attribute(name="name")
	private String msName;

	@Attribute(name="column")
	private String msColumn;

	@Attribute(name="type")
	private String msType;

	@Attribute(name="required",required=false)
	private Boolean msRequired=false;
	
	public String getName() { 
		return msName; 
	}

	public String getColumn() { 
		return msColumn; 
	}

	public String getType() { 
		return msType; 
	}

	public String getColumnType() { 
		return Utils.getColumnTypeByType(msType); 
	}
	
	public Boolean getRequired() { 
		return msRequired; 
	}
	
	@Override
	public String toString() {
		return msName;
	}

}
