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
import iae.home.datagenerator.datamodel.ResStrings;

/**
 * @author aisaev
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Serializer serializer = new Persister();
		File result = new File("./res/model/datamodel.xml");
		File resFile= new File("./res/strings.xml");
		
		try {
			DataModel om=serializer.read(DataModel.class, result);

			ResStrings resStrings=serializer.read(ResStrings.class, resFile);
		
			ExecStrings(resStrings, om);
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

	private static final String[] sExcludeFields=new String[] {
		"Deleted",
		"Modified",
		"Guid"
	};
	
	private static void ExecStrings(ResStrings resStrings,DataModel om) throws Exception {
		Serializer serializer = new Persister();
		File file=new File("D:/temp/out/strings.xml");
		
		for(ObjectModel obj : om.getObjects()) {
			String keyObj=obj.getName().toLowerCase();
			if(!resStrings.getStrings().containsKey(keyObj)) {
				resStrings.getStrings().put(keyObj, obj.getName()+":");
			}
			
			for(FieldModel fm : obj.getFields()) {
				Boolean bFind=true;
				
				for(String s : sExcludeFields) {
					if(s.equals(fm.getName()))
							bFind=false;
				}
				
				if(bFind) {
					String key=(obj.getName()+"_"+fm.getName()).toLowerCase();
					if(!resStrings.getStrings().containsKey(key)) {
						resStrings.getStrings().put(key, fm.getName()+":");
					}
				}
			}
		}
		
		serializer.write(resStrings, file);
	}
}
