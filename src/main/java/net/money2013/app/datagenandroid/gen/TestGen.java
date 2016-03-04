/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.money2013.app.datagenandroid.gen;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import javax.lang.model.element.Modifier;
import net.money2013.app.datagenandroid.GlobalSettings;
import net.money2013.app.datagenandroid.model.DataModel;
import net.money2013.app.datagenandroid.model.FieldModel;
import net.money2013.app.datagenandroid.model.ObjectModel;
import net.money2013.app.datagenandroid.model.ViewColumn;
import net.money2013.app.datagenandroid.model.ViewModel;
import net.money2013.app.datagenandroid.utils.Utils;

/**
 *
 * @author aisaev
 */
public class TestGen {
    private final DataModel dataModel;

    public TestGen(DataModel dataModel) {
        this.dataModel = dataModel;
    }

    public void gen() throws IOException {
        for (ObjectModel objectModel : dataModel.getObjects()) {
            GenTestClass(objectModel);
        }
        
        for(ViewModel viewModel : dataModel.getViewList()) {
            GenTestViewClass(viewModel);
        }
    }
    
    private void GenTestClass(ObjectModel objectModel) throws IOException {
        TypeSpec.Builder objectJavaClassBuilder = TypeSpec.classBuilder("Test"+objectModel.getName())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(ClassName.get("org.junit.runner","RunWith"))
                        .addMember("value", "$T.class", ClassName.get("android.support.test.runner","AndroidJUnit4"))
                        .build()
                );

        MethodSpec.Builder testGetterSetter=MethodSpec.methodBuilder("testGetterSetter")
                .addAnnotation(ClassName.get("org.junit","Test"))
                .addModifiers(Modifier.PUBLIC);
        
        
        testGetterSetter.addStatement("$T calendar=$T.getInstance()", ClassName.get(Calendar.class),ClassName.get(Calendar.class));
        testGetterSetter.addStatement("long val$L=$LL", "Id",(long)(Math.random()*10000));
        
        for (FieldModel fm : objectModel.getFields()) {
            String sType=fm.getType();
            if(sType.equals("String")) {
                testGetterSetter.addStatement("$T val$L=$S", ClassName.get(String.class),fm.getName(),UUID.randomUUID().toString());
            } else if(sType.equals("int")) {
                testGetterSetter.addStatement("int val$L=$L", fm.getName(),(int)(Math.random()*100));
            } else if(sType.equals("double")) {
                testGetterSetter.addStatement("double val$L=$L", fm.getName(),(Math.random()*100));
            } else if(sType.equals("Date")) {
                testGetterSetter.addStatement("calendar.set($L,$L,$L,$L,$L,$L)", (int)Math.round(Math.random()*20+100), (int)Math.round(Math.random()*11+1), (int)Math.round(Math.random()*28), (int)Math.round(Math.random()*24), (int)Math.round(Math.random()*60), (int)Math.round(Math.random()*60));
                testGetterSetter.addStatement("$T val$L=calendar.getTime()", ClassName.get(Date.class),fm.getName());
            } else if(sType.equals("boolean")) {                
                testGetterSetter.addStatement("boolean val$L=$L", fm.getName(),(Math.random()>0.5));
            } else if(sType.equals("long")) {
                testGetterSetter.addStatement("long val$L=$LL", fm.getName(),(long)(Math.random()*10000));
            } else {
                testGetterSetter.addStatement("$T val$L=$S", ClassName.get(String.class),fm.getName(),UUID.randomUUID().toString());
            }            
        }
        testGetterSetter.addStatement("$T obj=new $T()", ClassName.get(GlobalSettings.MODEL_PACKAGE_NAME,objectModel.getName()),ClassName.get(GlobalSettings.MODEL_PACKAGE_NAME,objectModel.getName()));
        
        for (FieldModel fm : objectModel.getFields()) {
            testGetterSetter.addStatement("obj.set$L(val$L)", fm.getName(),fm.getName());
        }

        testGetterSetter.addStatement("$T.assertEquals($S,(long)(obj.getId()),valId)",ClassName.get("junit.framework","Assert"),"Id");

        for (FieldModel fm : objectModel.getFields()) {
            String sType=fm.getType();
            if(sType.equals("String")) {
                testGetterSetter.addStatement("$T.assertEquals($S,obj.get$L(),val$L)",ClassName.get("junit.framework","Assert"), fm.getName(),fm.getName(),fm.getName());
            } else if(sType.equals("int")) {
                testGetterSetter.addStatement("$T.assertEquals($S,(int)(obj.get$L()),val$L)",ClassName.get("junit.framework","Assert"), fm.getName(),fm.getName(),fm.getName());
            } else if(sType.equals("double")) {
                testGetterSetter.addStatement("$T.assertEquals($S,(double)(obj.get$L()),val$L)",ClassName.get("junit.framework","Assert"), fm.getName(),fm.getName(),fm.getName());
            } else if(sType.equals("Date")) {
                testGetterSetter.addStatement("$T.assertEquals($S,obj.get$L(),val$L)",ClassName.get("junit.framework","Assert"), fm.getName(),fm.getName(),fm.getName());
            } else if(sType.equals("boolean")) {                
                testGetterSetter.addStatement("$T.assertEquals($S,(boolean)(obj.get$L()),val$L)",ClassName.get("junit.framework","Assert"), fm.getName(),fm.getName(),fm.getName());
            } else if(sType.equals("long")) {
                testGetterSetter.addStatement("$T.assertEquals($S,(long)(obj.get$L()),val$L)",ClassName.get("junit.framework","Assert"), fm.getName(),fm.getName(),fm.getName());
            } else {
                testGetterSetter.addStatement("$T.assertEquals($S,obj.get$L(),val$L)",ClassName.get("junit.framework","Assert"), fm.getName(),fm.getName(),fm.getName());
            }            

        }
        
        objectJavaClassBuilder.addMethod(testGetterSetter.build());
        
        JavaFile javaFile = JavaFile.builder(GlobalSettings.MODEL_PACKAGE_NAME, objectJavaClassBuilder.build())
                .build();

        javaFile.writeTo(new File(GlobalSettings.TEST_OUT_DIR));        
    }

    private void GenTestViewClass(ViewModel viewModel) throws IOException {
        TypeSpec.Builder objectJavaClassBuilder = TypeSpec.classBuilder("Test"+viewModel.getName())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(ClassName.get("org.junit.runner","RunWith"))
                        .addMember("value", "$T.class", ClassName.get("android.support.test.runner","AndroidJUnit4"))
                        .build()
                );

        MethodSpec.Builder testGetterSetter=MethodSpec.methodBuilder("testGetterSetter")
                .addAnnotation(ClassName.get("org.junit","Test"))
                .addModifiers(Modifier.PUBLIC);
        
        
        testGetterSetter.addStatement("$T calendar=$T.getInstance()", ClassName.get(Calendar.class),ClassName.get(Calendar.class));
        
        for (ViewColumn fm : viewModel.getColumns()) {
            String sType=fm.getType();
            String sName=(fm.getName().equals("_id"))?"Id":fm.getName();
            if(sType.equals("String")) {
                testGetterSetter.addStatement("$T val$L=$S", ClassName.get(String.class),sName,UUID.randomUUID().toString());
            } else if(sType.equals("int")) {
                testGetterSetter.addStatement("int val$L=$L", sName,(int)(Math.random()*100));
            } else if(sType.equals("double")) {
                testGetterSetter.addStatement("double val$L=$L", sName,(Math.random()*100));
            } else if(sType.equals("Date")) {
                testGetterSetter.addStatement("calendar.set($L,$L,$L,$L,$L,$L)", (int)Math.round(Math.random()*20+100), (int)Math.round(Math.random()*11+1), (int)Math.round(Math.random()*28), (int)Math.round(Math.random()*24), (int)Math.round(Math.random()*60), (int)Math.round(Math.random()*60));
                testGetterSetter.addStatement("$T val$L=calendar.getTime()", ClassName.get(Date.class),sName);
            } else if(sType.equals("boolean")) {
                testGetterSetter.addStatement("boolean val$L=$L", sName,(Math.random()>0.5));
            } else if(sType.equals("long")) {
                testGetterSetter.addStatement("long val$L=$LL", sName,(long)(Math.random()*10000));
            } else {
                testGetterSetter.addStatement("$T val$L=$S", ClassName.get(String.class),sName,UUID.randomUUID().toString());
            }            
        }
        testGetterSetter.addStatement("$T obj=new $T()", ClassName.get(GlobalSettings.MODEL_PACKAGE_NAME,viewModel.getName()),ClassName.get(GlobalSettings.MODEL_PACKAGE_NAME,viewModel.getName()));
        
        for (ViewColumn fm : viewModel.getColumns()) {
            String sName=(fm.getName().equals("_id"))?"Id":fm.getName();
            testGetterSetter.addStatement("obj.set$L(val$L)", sName,sName);
        }

        for (ViewColumn fm : viewModel.getColumns()) {
            String sName=(fm.getName().equals("_id"))?"Id":fm.getName();
            String sType=fm.getType();
            if(sType.equals("String")) {
                testGetterSetter.addStatement("$T.assertEquals($S,obj.get$L(),val$L)",ClassName.get("junit.framework","Assert"), sName,sName,sName);
            } else if(sType.equals("int")) {
                testGetterSetter.addStatement("$T.assertEquals($S,(int)(obj.get$L()),val$L)",ClassName.get("junit.framework","Assert"), sName,sName,sName);
            } else if(sType.equals("double")) {
                testGetterSetter.addStatement("$T.assertEquals($S,(double)(obj.get$L()),val$L)",ClassName.get("junit.framework","Assert"), sName,sName,sName);
            } else if(sType.equals("Date")) {
                testGetterSetter.addStatement("$T.assertEquals($S,obj.get$L(),val$L)",ClassName.get("junit.framework","Assert"), sName,sName,sName);
            } else if(sType.equals("boolean")) {                
                testGetterSetter.addStatement("$T.assertEquals($S,(boolean)(obj.get$L()),val$L)",ClassName.get("junit.framework","Assert"), sName,sName,sName);
            } else if(sType.equals("long")) {
                testGetterSetter.addStatement("$T.assertEquals($S,(long)(obj.get$L()),val$L)",ClassName.get("junit.framework","Assert"), sName,sName,sName);
            } else {
                testGetterSetter.addStatement("$T.assertEquals($S,obj.get$L(),val$L)",ClassName.get("junit.framework","Assert"), sName,sName,sName);
            }            
        }
        
        objectJavaClassBuilder.addMethod(testGetterSetter.build());
        
        JavaFile javaFile = JavaFile.builder(GlobalSettings.MODEL_PACKAGE_NAME, objectJavaClassBuilder.build())
                .build();

        javaFile.writeTo(new File(GlobalSettings.TEST_OUT_DIR));        
    }
    
}
