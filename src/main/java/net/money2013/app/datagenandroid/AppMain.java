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
            ClassName androidContentProvider=ClassName.get("android.content", "ContentProvider");
            TypeSpec.Builder typeContentProviderBuilder=TypeSpec.classBuilder("MoneyProvider")
                    .addModifiers(Modifier.PUBLIC,Modifier.FINAL)
                    .superclass(androidContentProvider);
            
            for(ObjectModel om : dm.getObjects()) {
                TypeSpec.Builder objectJavaClassBuilder = TypeSpec.interfaceBuilder(om.getName()+"Meta")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
                FieldSpec tableSpec=FieldSpec.builder(String.class, "TABLE", Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                            .initializer("$S", om.getTable())
                            .build();
                objectJavaClassBuilder
                        .addField(tableSpec);
                
                for(FieldModel fm : om.getFields()) {
                    FieldSpec fieldSpec=FieldSpec.builder(String.class, "COL_"+fm.getName().toUpperCase(), Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                            .initializer("$S", fm.getColumn())
                            .build();
                    
                    objectJavaClassBuilder
                            .addField(fieldSpec);
                }

                JavaFile javaFile = JavaFile.builder("net.money2013.app.hb.meta", objectJavaClassBuilder.build())
                                    .build();

                javaFile.writeTo(new File("d:/ttt"));
            }
            
            JavaFile contentProviderJava=JavaFile.builder("net.money2013.app.hb.provider", typeContentProviderBuilder.build())
                                        .build();
            contentProviderJava.writeTo(new File("d:/ttt"));

/*            
MethodSpec main = MethodSpec.methodBuilder("main")
    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
    .returns(void.class)
    .addParameter(String[].class, "args")
    .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
    .build();
*/

        } catch(Exception e) {
            System.err.println("Exception: "+e.getLocalizedMessage());
            e.printStackTrace(System.err);
        }
    }    
}
