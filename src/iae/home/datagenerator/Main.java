/**
 * 
 */
package iae.home.datagenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import iae.home.datagenerator.datamodel.AppSettings;
import iae.home.datagenerator.datamodel.FieldModel;
import iae.home.datagenerator.datamodel.ObjectModel;

/**
 * @author aisaev
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
/*		
		ObjectModel om=new ObjectModel();
		om.msClass="person";
		om.msTable="person_table";
		om.mFields=new ArrayList<FieldModel>();
		
		for(int i=0;i<4;i++){
			FieldModel fm=new FieldModel();
			fm.msName="field_"+Integer.toString(i);
			fm.msColumn="col_"+Integer.toString(i);
			om.mFields.add(fm);
		}

		AppSettings ap=new AppSettings();
		ap.mProperties=new HashMap<String, String>();
		ap.mProperties.put("1", "val 1");
		ap.mProperties.put("2", "val 2");
		ap.mProperties.put("3", "val 3");
		
		Serializer serializer = new Persister();
		File result = new File("d:/temp/example.xml");
		File settings=new File("d:/temp/settings.xml");
		
		try {
		serializer.write(om, result);
		serializer.write(ap, settings);
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
*/

		Serializer serializer = new Persister();
		File result = new File("d:/temp/example.xml");
		
		try {
			ObjectModel om=serializer.read(ObjectModel.class, result);
			System.out.println(om.msClass);
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
	}

}
