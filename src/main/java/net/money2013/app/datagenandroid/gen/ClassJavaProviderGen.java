/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.money2013.app.datagenandroid.gen;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import java.io.File;
import java.io.IOException;
import javax.lang.model.element.Modifier;
import net.money2013.app.datagenandroid.GlobalSettings;
import net.money2013.app.datagenandroid.model.DataModel;
import net.money2013.app.datagenandroid.model.FieldModel;
import net.money2013.app.datagenandroid.model.ObjectModel;

/**
 *
 * @author aisaev
 */
public class ClassJavaProviderGen {
    private final DataModel dataModel;
    private static final ClassName globalSettingsClass = ClassName.get(GlobalSettings.PACKAGE_NAME, "GlobalSettings");
    private static final ClassName contentProviderClass = ClassName.get("android.content", "ContentProvider");
    private static final ClassName contentValueClass = ClassName.get("android.content","ContentValues");
    private static final ClassName uriClass = ClassName.get("android.net", "Uri");
    private static final ClassName uriMatcherClass=ClassName.get("android.content","UriMatcher");
    private static final ClassName cursorClass = ClassName.get("android.database", "Cursor");
    private static final ClassName nonNullClass = ClassName.get("android.support.annotation", "NonNull");
    private static final ClassName hashMapClass=ClassName.get("java.util","HashMap");
    private static final ClassName moneyDatabase=ClassName.get(GlobalSettings.PACKAGE_NAME+ ".provider","MoneyDatabase");

    public ClassJavaProviderGen(DataModel dataModel) {
        this.dataModel = dataModel;
    }

    public void gen() throws IOException {
        TypeSpec.Builder objectJavaClassBuilder = TypeSpec.classBuilder("MoneyProvider")
                .superclass(contentProviderClass)
                .addModifiers(Modifier.PUBLIC);
        
        genStatic(objectJavaClassBuilder);
        genOnCreate(objectJavaClassBuilder);
        genInsert(objectJavaClassBuilder);
        genUpdate(objectJavaClassBuilder);
        genQuery(objectJavaClassBuilder);
        
        JavaFile javaFile = JavaFile.builder(GlobalSettings.PACKAGE_NAME + ".provider", objectJavaClassBuilder.build())
                .build();
        javaFile.writeTo(new File(GlobalSettings.OUT_DIR));        
    }
    
    private void genStatic(TypeSpec.Builder classBuilder) {
        classBuilder.addField(FieldSpec.builder(uriMatcherClass, "uriMatcher", Modifier.FINAL, Modifier.PRIVATE, Modifier.STATIC)
                .initializer("new $T($T.NO_MATCH)", uriMatcherClass, uriMatcherClass)
                .build());
        int objNumerator=1;
        for(ObjectModel objectModel : dataModel.getObjects()) {
            classBuilder.addField(FieldSpec.builder(int.class, "ID_"+objectModel.getName().toUpperCase(), Modifier.FINAL, Modifier.PRIVATE, Modifier.STATIC)
                    .initializer("$L", Integer.toString(objNumerator++))
                    .build());
        }
        
        for(ObjectModel objectModel : dataModel.getObjects()) {
            classBuilder.addField(FieldSpec.builder(ParameterizedTypeName.get(hashMapClass, ClassName.get(String.class), ClassName.get(String.class)), objectModel.getName()+"ProjectionMap", Modifier.FINAL, Modifier.PRIVATE, Modifier.STATIC)
                    .initializer("new $T()", ParameterizedTypeName.get(hashMapClass, ClassName.get(String.class), ClassName.get(String.class)))
                    .build());
        }
        CodeBlock.Builder codeBlock=CodeBlock.builder();
        for(ObjectModel objectModel : dataModel.getObjects()) {
            codeBlock
                .addStatement("uriMatcher.addURI($T.CONTENT_AUTHORITY, $S, $L)", globalSettingsClass, objectModel.getName(), "ID_"+objectModel.getName().toUpperCase());
        }
        
        
        for(ObjectModel objectModel : dataModel.getObjects()) {
            String mapName=objectModel.getName()+"ProjectionMap";
            ClassName metaClass = ClassName.get(GlobalSettings.PACKAGE_NAME + ".meta", objectModel.getName() + "Meta");
            codeBlock.addStatement("$L.put($T.COL_ID, $T.COL_ID)", mapName, metaClass, metaClass);
            for(FieldModel fieldModel : objectModel.getFields()) {
                codeBlock.addStatement("$L.put($T.COL_$L, $T.COL_$L)", mapName, metaClass, fieldModel.getName().toUpperCase(), metaClass, fieldModel.getName().toUpperCase());
            }
         }
        
        classBuilder.addStaticBlock(codeBlock.build());
    }
    
    private void genOnCreate(TypeSpec.Builder classBuilder) {
        classBuilder.addField(
                FieldSpec.builder(moneyDatabase, "dbOpenHelper", Modifier.PRIVATE)
                    .addAnnotation(nonNullClass)
                    .build());
        classBuilder.addMethod(
                MethodSpec.methodBuilder("onCreate")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(boolean.class)
                    .addStatement("dbOpenHelper=new $T(getContext())", moneyDatabase)
                    .addStatement("return true")
                    .build()
        );
    }

    private void genInsert(TypeSpec.Builder classBuilder) {
        MethodSpec.Builder insertMethod=MethodSpec.methodBuilder("insert")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(uriClass)
                    .addParameter(uriClass, "uri")
                    .addParameter(contentValueClass,"values")
                ;
        
        classBuilder.addMethod(insertMethod.build());
    }
    
    private void genQuery(TypeSpec.Builder classBuilder) {
        MethodSpec.Builder queryMethod=MethodSpec.methodBuilder("query")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(cursorClass)
                    .addParameter(uriClass, "uri")
                    .addParameter(String[].class,"projection")
                    .addParameter(String.class,"selection")
                    .addParameter(String[].class,"selectionArgs")
                    .addParameter(String.class,"sortOrder")
                ;
        
        classBuilder.addMethod(queryMethod.build());
    }    

    private void genUpdate(TypeSpec.Builder classBuilder) {
        MethodSpec.Builder updateMethod=MethodSpec.methodBuilder("update")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(int.class)
                    .addParameter(uriClass, "uri")
                    .addParameter(contentValueClass,"values")
                    .addParameter(String.class,"selection")
                    .addParameter(String[].class,"selectionArgs")
                ;
        
        classBuilder.addMethod(updateMethod.build());
    }    
    
}
