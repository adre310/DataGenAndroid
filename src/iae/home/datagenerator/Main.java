/**
 * 
 */
package iae.home.datagenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.OutputStreamWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import iae.home.datagenerator.datamodel.DataModel;
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
		File result = new File("./res/model/datamodel.xml");
		
		try {
			DataModel om=serializer.read(DataModel.class, result);
			
			
			//RuntimeSingleton.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, "d:\\temp");
			Velocity.init();
			Template tplClassBLL=null;
			Template tplClassDAL=null;
			Template tplProvider=null; 
			tplClassBLL=Velocity.getTemplate("/res/template/classBll.tpl");
			tplClassDAL=Velocity.getTemplate("/res/template/classDAL.tpl");
			tplProvider=Velocity.getTemplate("/res/template/provider.tpl");
			
			for (ObjectModel obj : om.getObjects()) {
				VelocityContext context=new VelocityContext();
				context.put("name", obj.getName());
				context.put("table", obj.getTable());
				context.put("version", obj.getVersion());
				context.put("fields",obj.getFields());
				context.put("views",obj.getViews());

				FileWriter writer=new FileWriter(new File("D:/temp/out/bll/objects/"+obj.getName()+"BLL.java"));
				
	            if ( tplClassBLL != null)
	            	tplClassBLL.merge(context, writer);

	            /*
	             *  flush and cleanup
	             */

	            writer.flush();
	            writer.close();
			}
			
			for (ObjectModel obj : om.getObjects()) {
				VelocityContext context=new VelocityContext();
				context.put("name", obj.getName());
				context.put("table", obj.getTable());
				context.put("version", obj.getVersion());
				context.put("fields",obj.getFields());
				context.put("views",obj.getViews());

				FileWriter writer=new FileWriter(new File("D:/temp/out/dal/"+obj.getName()+"DAL.java"));
				
	            if ( tplClassDAL != null)
	            	tplClassDAL.merge(context, writer);

	            /*
	             *  flush and cleanup
	             */

	            writer.flush();
	            writer.close();
			}

			VelocityContext context=new VelocityContext();
			context.put("model", om);

			FileWriter writer=new FileWriter(new File("D:/temp/out/dal/MoneyProvider.java"));
			
            if ( tplProvider != null)
            	tplProvider.merge(context, writer);

            /*
             *  flush and cleanup
             */

            writer.flush();
            writer.close();
			
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
	}

}
