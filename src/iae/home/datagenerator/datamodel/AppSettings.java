package iae.home.datagenerator.datamodel;

import java.util.Map;

import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name="config")
@Namespace(reference="http://money2013.net/datagenerator/2013/07/26")
public class AppSettings {
	
	@ElementMap(entry="property", key="name", inline=true,attribute=true)
	public Map<String, String> mProperties;
}
