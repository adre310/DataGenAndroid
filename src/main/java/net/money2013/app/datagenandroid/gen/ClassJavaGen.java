/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.money2013.app.datagenandroid.gen;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.Modifier;
import net.money2013.app.datagenandroid.GlobalSettings;
import net.money2013.app.datagenandroid.model.DataModel;
import net.money2013.app.datagenandroid.model.FieldModel;
import net.money2013.app.datagenandroid.model.ObjectModel;
import net.money2013.app.datagenandroid.utils.Utils;

/**
 *
 * @author aisaev
 */
public class ClassJavaGen {
    private final DataModel dataModel;
    private static final ClassName GlobalSettingsClass;
    private static final ClassName ContentProviderClass;
    private static final ClassName UriClass;
    
    static {
        GlobalSettingsClass=ClassName.get(GlobalSettings.PACKAGE_NAME, "GlobalSettings");
        ContentProviderClass=ClassName.get("android.content", "ContentProvider");
        UriClass=ClassName.get("android.net","Uri");
    }

    public ClassJavaGen(DataModel dataModel) {
        this.dataModel = dataModel;        
    }
    
    public void gen() throws IOException {
        for(ObjectModel objectModel : dataModel.getObjects()) {
            GenMetaClass(objectModel);
            GenModelClass(objectModel);
        }        
    }
    
    private void GenMetaClass(ObjectModel objectModel) throws IOException {
        TypeSpec.Builder objectJavaClassBuilder = TypeSpec.interfaceBuilder(objectModel.getName()+"Meta")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        
                FieldSpec tableSpec=FieldSpec.builder(String.class, "TABLE", Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                            .initializer("$S", objectModel.getTable())
                            .build();
                
                objectJavaClassBuilder.addField(tableSpec);
                
                FieldSpec uriSpec=FieldSpec.builder(UriClass, "CONTENT_URI", Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                            .initializer("$T.parse($S + $T.CONTENT_AUTHORITY + $S)",UriClass, "content://",GlobalSettingsClass, "/"+objectModel.getName())
                            .build();
                objectJavaClassBuilder.addField(uriSpec);
                
                List<String> fieldList=new ArrayList<>();
                fieldList.add("_id INTEGER PRIMARY KEY AUTOINCREMENT");
                
                for(FieldModel fm : objectModel.getFields()) {
                    fieldList.add(fm.getColumn()+" "+Utils.getColumnTypeByType(fm.getType()));
                    FieldSpec fieldSpec=FieldSpec.builder(String.class, "COL_"+fm.getName().toUpperCase(), Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                            .initializer("$S", fm.getColumn())
                            .build();
                    
                    objectJavaClassBuilder.addField(fieldSpec);                    
                }
                String tableSqlSpecVal="CREATE TABLE "+objectModel.getTable()+"("+String.join(",", fieldList)+")";
                FieldSpec tableSqlSpec=FieldSpec.builder(String.class, "CREATE_TABLE_SQL", Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                            .initializer("$S", tableSqlSpecVal)
                            .build();
                objectJavaClassBuilder.addField(tableSqlSpec);
                
                JavaFile javaFile = JavaFile.builder(GlobalSettings.PACKAGE_NAME + ".meta", objectJavaClassBuilder.build())
                                    .build();

                javaFile.writeTo(new File(GlobalSettings.OUT_DIR));        
    }
    
    private void GenModelClass(ObjectModel objectModel)  throws IOException {
        TypeSpec.Builder objectJavaClassBuilder = TypeSpec.classBuilder(objectModel.getName())
                .addModifiers(Modifier.PUBLIC);

        objectJavaClassBuilder.addField(FieldSpec.builder(Long.class, "mId", Modifier.PUBLIC)
                            .build());

        for(FieldModel fm : objectModel.getFields()) {
            FieldSpec fieldSpec=FieldSpec.builder(Utils.getTypeByName(fm.getType()), "m"+fm.getName(), Modifier.PUBLIC)
                            .build();
                    
            objectJavaClassBuilder.addField(fieldSpec);
        }
        
        JavaFile javaFile = JavaFile.builder(GlobalSettings.PACKAGE_NAME + ".model", objectJavaClassBuilder.build())
                                    .build();

        javaFile.writeTo(new File(GlobalSettings.OUT_DIR));        
    }
}
