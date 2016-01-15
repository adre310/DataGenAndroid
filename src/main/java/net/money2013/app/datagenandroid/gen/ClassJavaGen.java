/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.money2013.app.datagenandroid.gen;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
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
    private static final ClassName globalSettingsClass = ClassName.get(GlobalSettings.PACKAGE_NAME, "GlobalSettings");
    private static final ClassName contentProviderClass = ClassName.get("android.content", "ContentProvider");
    private static final ClassName contentValueClass = ClassName.get("android.content","ContentValues");
    private static final ClassName uriClass = ClassName.get("android.net", "Uri");
    private static final ClassName defaultGetResolverClass = ClassName.get("com.pushtorefresh.storio.contentresolver.operations.get", "DefaultGetResolver");
    private static final ClassName defaultPutResolverClass = ClassName.get("com.pushtorefresh.storio.contentresolver.operations.put", "DefaultPutResolver");
    private static final ClassName defaultDeleteResolverClass = ClassName.get("com.pushtorefresh.storio.contentresolver.operations.delete", "DefaultDeleteResolver");
    private static final ClassName ResolverClass = ClassName.get("com.pushtorefresh.storio.contentresolver","ContentResolverTypeMapping");
    private static final ClassName insertQueryClass = ClassName.get("com.pushtorefresh.storio.contentresolver.queries","InsertQuery");
    private static final ClassName updateQueryClass = ClassName.get("com.pushtorefresh.storio.contentresolver.queries","UpdateQuery");
    private static final ClassName deleteQueryClass = ClassName.get("com.pushtorefresh.storio.contentresolver.queries","DeleteQuery");
    private static final ClassName cursorClass = ClassName.get("android.database", "Cursor");
    private static final ClassName nonNullClass = ClassName.get("android.support.annotation", "NonNull");
    private static final ClassName bllConverterClass = ClassName.get(GlobalSettings.PACKAGE_NAME+".util", "BLLConverter");
    

    public ClassJavaGen(DataModel dataModel) {
        this.dataModel = dataModel;
    }

    public void gen() throws IOException {
        for (ObjectModel objectModel : dataModel.getObjects()) {
            GenMetaClass(objectModel);
            GenModelClass(objectModel);
            GenResolver(objectModel);
        }
    }

    private void GenMetaClass(ObjectModel objectModel) throws IOException {
        TypeSpec.Builder objectJavaClassBuilder = TypeSpec.interfaceBuilder(objectModel.getName() + "Meta")
                .addModifiers(Modifier.PUBLIC);

        FieldSpec tableSpec = FieldSpec.builder(String.class, "TABLE", Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                .initializer("$S", objectModel.getTable())
                .build();

        objectJavaClassBuilder.addField(tableSpec);

        /*
        FieldSpec uriSpec = FieldSpec.builder(uriClass, "CONTENT_URI", Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                .initializer("$T.parse($S + $T.CONTENT_AUTHORITY + $S)", uriClass, "content://", globalSettingsClass, "/" + objectModel.getName())
                .build();
        objectJavaClassBuilder.addField(uriSpec);
        */
        
        List<String> fieldList = new ArrayList<>();
        fieldList.add("_id INTEGER PRIMARY KEY AUTOINCREMENT");
        FieldSpec fieldSpecId = FieldSpec.builder(String.class, "COL_ID", Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                .initializer("$S", "_id")
                .build();

        objectJavaClassBuilder.addField(fieldSpecId);

        for (FieldModel fm : objectModel.getFields()) {
            fieldList.add(fm.getColumn() + " " + Utils.getColumnTypeByType(fm.getType()));
            FieldSpec fieldSpec = FieldSpec.builder(String.class, "COL_" + fm.getName().toUpperCase(), Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                    .initializer("$S", fm.getColumn())
                    .build();

            objectJavaClassBuilder.addField(fieldSpec);
        }
        String tableSqlSpecVal = "CREATE TABLE " + objectModel.getTable() + "(" + String.join(",", fieldList) + ")";
        FieldSpec tableSqlSpec = FieldSpec.builder(String.class, "CREATE_TABLE_SQL", Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                .initializer("$S", tableSqlSpecVal)
                .build();
        objectJavaClassBuilder.addField(tableSqlSpec);

        JavaFile javaFile = JavaFile.builder(GlobalSettings.PACKAGE_NAME + ".meta", objectJavaClassBuilder.build())
                .build();

        javaFile.writeTo(new File(GlobalSettings.OUT_DIR));
    }

    private void GenModelClass(ObjectModel objectModel) throws IOException {
        TypeSpec.Builder objectJavaClassBuilder = TypeSpec.classBuilder(objectModel.getName())
                .addModifiers(Modifier.PUBLIC);

        objectJavaClassBuilder.addField(FieldSpec.builder(Long.class, "mId", Modifier.PRIVATE)
                .build());

        MethodSpec getFieldId = MethodSpec.methodBuilder("getId")
                .addModifiers(Modifier.PUBLIC)
                .returns(Long.class)
                .addStatement("return this.mId")
                .build();
        objectJavaClassBuilder.addMethod(getFieldId);

        MethodSpec setFieldId = MethodSpec.methodBuilder("setId")
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get(GlobalSettings.PACKAGE_NAME + ".model", objectModel.getName()))
                .addParameter(Long.class, "pId")
                .addStatement("this.mId=pId")
                .addStatement("return this")
                .build();
        objectJavaClassBuilder.addMethod(setFieldId);

        for (FieldModel fm : objectModel.getFields()) {
            Class fieldType = Utils.getTypeByName(fm.getType());
            FieldSpec fieldSpec = FieldSpec.builder(fieldType, "m" + fm.getName(), Modifier.PRIVATE)
                    .build();
            objectJavaClassBuilder.addField(fieldSpec);

            MethodSpec getField = MethodSpec.methodBuilder("get" + fm.getName())
                    .addModifiers(Modifier.PUBLIC)
                    .returns(fieldType)
                    .addStatement("return this.m" + fm.getName())
                    .build();
            objectJavaClassBuilder.addMethod(getField);

            MethodSpec setField = MethodSpec.methodBuilder("set" + fm.getName())
                    .addModifiers(Modifier.PUBLIC)
                    .returns(ClassName.get(GlobalSettings.PACKAGE_NAME + ".model", objectModel.getName()))
                    .addParameter(fieldType, "p" + fm.getName())
                    .addStatement("this.m$L=p$L", fm.getName(), fm.getName())
                    .addStatement("return this")
                    .build();
            objectJavaClassBuilder.addMethod(setField);
        }

        JavaFile javaFile = JavaFile.builder(GlobalSettings.PACKAGE_NAME + ".model", objectJavaClassBuilder.build())
                .build();

        javaFile.writeTo(new File(GlobalSettings.OUT_DIR));
    }

    private void GenGetResolver(ObjectModel objectModel) throws IOException {
        ClassName metaClass = ClassName.get(GlobalSettings.PACKAGE_NAME + ".meta", objectModel.getName() + "Meta");
        ClassName modelClass = ClassName.get(GlobalSettings.PACKAGE_NAME + ".model", objectModel.getName());

        TypeSpec.Builder objectJavaClassBuilder = TypeSpec.classBuilder(objectModel.getName() + "GetResolver")
                .superclass(ParameterizedTypeName.get(defaultGetResolverClass, modelClass))
                .addModifiers(Modifier.PUBLIC);

        MethodSpec.Builder mapGet = MethodSpec.methodBuilder("mapFromCursor")
                .addModifiers(Modifier.PUBLIC)
                .returns(modelClass)
                .addAnnotation(Override.class)
                .addAnnotation(nonNullClass)
                .addParameter(ParameterSpec.builder(cursorClass, "cursor")
                        .addAnnotation(nonNullClass)
                        .build())
                .addStatement("$T object=new $T()", modelClass, modelClass)
                .addStatement("object.setId($T.CursorToLong(cursor,cursor.getColumnIndex($T.COL_ID)))", bllConverterClass, metaClass);

        for (FieldModel fieldModel : objectModel.getFields()) {
            mapGet.addStatement("object.set$L($T.CursorTo$L(cursor,cursor.getColumnIndex($T.COL_$L)))", fieldModel.getName(), bllConverterClass, Utils.getTypeByName(fieldModel.getType()).getSimpleName(), metaClass, fieldModel.getName().toUpperCase());
        }
        mapGet
                .addStatement("return object");

        objectJavaClassBuilder.addMethod(mapGet.build());
        JavaFile javaFile = JavaFile.builder(GlobalSettings.PACKAGE_NAME + ".resolver", objectJavaClassBuilder.build()).build();
        javaFile.writeTo(new File(GlobalSettings.OUT_DIR));
    }
    
    private void GenPutResolver(ObjectModel objectModel) throws IOException {
        ClassName metaClass = ClassName.get(GlobalSettings.PACKAGE_NAME + ".meta", objectModel.getName() + "Meta");
        ClassName modelClass = ClassName.get(GlobalSettings.PACKAGE_NAME + ".model", objectModel.getName());

        TypeSpec.Builder objectJavaClassBuilder = TypeSpec.classBuilder(objectModel.getName() + "PutResolver")
                .superclass(ParameterizedTypeName.get(defaultPutResolverClass, modelClass))
                .addModifiers(Modifier.PUBLIC);

        MethodSpec.Builder mapToInsertQuery=MethodSpec.methodBuilder("mapToInsertQuery")
                .returns(insertQueryClass)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addAnnotation(nonNullClass)
                .addParameter(ParameterSpec.builder(modelClass, "object").addAnnotation(nonNullClass).build())
                .addStatement("return $T.builder().uri($S + $T.CONTENT_AUTHORITY + $S).build()", insertQueryClass, "content://", globalSettingsClass, "/" + objectModel.getName());
                
        objectJavaClassBuilder.addMethod(mapToInsertQuery.build());

        MethodSpec.Builder mapToUpdateQuery=MethodSpec.methodBuilder("mapToUpdateQuery")
                .returns(updateQueryClass)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addAnnotation(nonNullClass)
                .addParameter(ParameterSpec.builder(modelClass, "object").addAnnotation(nonNullClass).build())
                .addStatement("return $T.builder().uri($S + $T.CONTENT_AUTHORITY + $S).where($S).whereArgs($T.toString(object.getId())).build()", updateQueryClass, "content://", globalSettingsClass, "/" + objectModel.getName(),"_id = ?",Long.class);
                
        objectJavaClassBuilder.addMethod(mapToUpdateQuery.build());

        MethodSpec.Builder mapToContentValues=MethodSpec.methodBuilder("mapToContentValues")
                .returns(contentValueClass)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addAnnotation(nonNullClass)
                .addParameter(ParameterSpec.builder(modelClass, "object").addAnnotation(nonNullClass).build())
                .addStatement("final $T contentValues=new $T()", contentValueClass, contentValueClass);
        
        for(FieldModel fieldModel : objectModel.getFields()) {
            mapToContentValues
                .addStatement("contentValues.put($T.COL_$L, $T.ObjectToCV(object.get$L()))", metaClass, fieldModel.getName().toUpperCase(), bllConverterClass, fieldModel.getName());
        }
        
        mapToContentValues
                .addStatement("return contentValues");
        
        objectJavaClassBuilder.addMethod(mapToContentValues.build());

        JavaFile javaFile = JavaFile.builder(GlobalSettings.PACKAGE_NAME + ".resolver", objectJavaClassBuilder.build()).build();
        javaFile.writeTo(new File(GlobalSettings.OUT_DIR));
    }
    
    private void GenDeleteResolver(ObjectModel objectModel) throws IOException {
        ClassName metaClass = ClassName.get(GlobalSettings.PACKAGE_NAME + ".meta", objectModel.getName() + "Meta");
        ClassName modelClass = ClassName.get(GlobalSettings.PACKAGE_NAME + ".model", objectModel.getName());

        TypeSpec.Builder objectJavaClassBuilder = TypeSpec.classBuilder(objectModel.getName() + "DeleteResolver")
                .superclass(ParameterizedTypeName.get(defaultDeleteResolverClass, modelClass))
                .addModifiers(Modifier.PUBLIC);

        MethodSpec.Builder mapToDeleteQuery=MethodSpec.methodBuilder("mapToDeleteQuery")
                .returns(deleteQueryClass)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addAnnotation(nonNullClass)
                .addParameter(ParameterSpec.builder(modelClass, "object").addAnnotation(nonNullClass).build())
                .addStatement("return $T.builder().uri($S + $T.CONTENT_AUTHORITY + $S).where($S).whereArgs($T.toString(object.getId())).build()", deleteQueryClass, "content://", globalSettingsClass, "/" + objectModel.getName(),"_id = ?",Long.class);
                
        objectJavaClassBuilder.addMethod(mapToDeleteQuery.build());
        
        JavaFile javaFile = JavaFile.builder(GlobalSettings.PACKAGE_NAME + ".resolver", objectJavaClassBuilder.build()).build();
        javaFile.writeTo(new File(GlobalSettings.OUT_DIR));
    }

    private void GenResolver(ObjectModel objectModel) throws IOException {
        ClassName modelClass = ClassName.get(GlobalSettings.PACKAGE_NAME + ".model", objectModel.getName());
        ClassName getRslvClass=ClassName.get(GlobalSettings.PACKAGE_NAME + ".resolver",objectModel.getName() + "GetResolver"); 
        ClassName putRslvClass=ClassName.get(GlobalSettings.PACKAGE_NAME + ".resolver",objectModel.getName() + "PutResolver"); 
        ClassName deleteRslvClass=ClassName.get(GlobalSettings.PACKAGE_NAME + ".resolver",objectModel.getName() + "DeleteResolver"); 
        
        GenGetResolver(objectModel);
        GenPutResolver(objectModel);
        GenDeleteResolver(objectModel);
        
        TypeSpec.Builder objectJavaClassBuilder = TypeSpec.classBuilder(objectModel.getName() + "Resolver")
                .superclass(ParameterizedTypeName.get(ResolverClass, modelClass))
                .addModifiers(Modifier.PUBLIC);
        
        MethodSpec constr=MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("super(new $T(),new $T(),new $T())", putRslvClass, getRslvClass, deleteRslvClass)
                .build();
        objectJavaClassBuilder.addMethod(constr);
        JavaFile javaFile = JavaFile.builder(GlobalSettings.PACKAGE_NAME + ".resolver", objectJavaClassBuilder.build()).build();
        javaFile.writeTo(new File(GlobalSettings.OUT_DIR));        
    }    
}
