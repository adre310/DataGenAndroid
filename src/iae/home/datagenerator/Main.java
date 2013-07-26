/**
 * 
 */
package iae.home.datagenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.RuntimeSingleton;
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
		File result = new File("d:\\temp\\example.xml");
		
		try {
			RuntimeSingleton.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, "d:\\temp");
			Velocity.init();
			VelocityContext mVelContext=new VelocityContext();
			ObjectModel om=serializer.read(ObjectModel.class, result);
			mVelContext.put("fields", om.mFields);
			
			Template mTpl=null;
			mTpl=Velocity.getTemplate("class.vm");
			
            BufferedWriter writer = writer = new BufferedWriter(
                    new OutputStreamWriter(System.out));
			
            if ( mTpl != null)
            	mTpl.merge(mVelContext, writer);

            /*
             *  flush and cleanup
             */

            writer.flush();
            writer.close();
            
			System.out.println(om.msClass);
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
	}

}
