/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.money2013.app.datagenandroid;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.io.File;
import javax.lang.model.element.Modifier;
import net.money2013.app.datagenandroid.gen.ClassJavaGen;
import net.money2013.app.datagenandroid.gen.ClassJavaProviderGen;
import net.money2013.app.datagenandroid.model.DataModel;
import net.money2013.app.datagenandroid.model.FieldModel;
import net.money2013.app.datagenandroid.model.ObjectModel;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 *
 * @author aisaev
 */
public class AppMain {
    
    public static void main(String[] args) {
        Serializer serializer = new Persister();
        try {
            DataModel dm=serializer.read(DataModel.class, new File(AppMain.class.getClassLoader().getResource("model/datamodel.xml").getFile()));
            ClassJavaGen gen=new ClassJavaGen(dm);
            gen.gen();
            ClassJavaProviderGen genProvider=new ClassJavaProviderGen(dm);
            genProvider.gen();
        } catch(Exception e) {
            System.err.println("Exception: "+e.getLocalizedMessage());
            e.printStackTrace(System.err);
        }
    }    
}
