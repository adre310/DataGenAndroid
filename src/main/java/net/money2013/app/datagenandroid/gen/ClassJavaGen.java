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
import net.money2013.app.datagenandroid.model.ViewColumn;
import net.money2013.app.datagenandroid.model.ViewModel;
import net.money2013.app.datagenandroid.utils.Utils;

/**
 *
 * @author aisaev
 */
public class ClassJavaGen {

    private final DataModel dataModel;

    public ClassJavaGen(DataModel dataModel) {
        this.dataModel = dataModel;
    }

    public void gen() throws IOException {
        for (ObjectModel objectModel : dataModel.getObjects()) {
            GenMetaClass(objectModel);
            GenModelClass(objectModel);
            GenResolver(objectModel);
        }
        
        for(ViewModel viewModel : dataModel.getViewList()) {
            GenViewMetaClass(viewModel);
            GenViewModelClass(viewModel);
            GenViewGetResolver(viewModel);
        }
    }

    private void GenMetaClass(ObjectModel objectModel) throws IOException {
        TypeSpec.Builder objectJavaClassBuilder = TypeSpec.interfaceBuilder(objectModel.getName() + "Meta")
                .addModifiers(Modifier.PUBLIC);

        FieldSpec tableSpec = FieldSpec.builder(String.class, "TABLE", Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                .initializer("$S", objectModel.getTable())
                .build();

        objectJavaClassBuilder.addField(tableSpec);

        
        FieldSpec uriSpec = FieldSpec.builder(GlobalSettings.URI_CLASS, "CONTENT_URI", Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                .initializer("$T.parse($S + $T.CONTENT_AUTHORITY + $S)", GlobalSettings.URI_CLASS, "content://", GlobalSettings.GLOBAL_SETTINGS_CLASS, "/" + objectModel.getName())
                .build();
        objectJavaClassBuilder.addField(uriSpec);
         
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

        JavaFile javaFile = JavaFile.builder(GlobalSettings.META_PACKAGE_NAME, objectJavaClassBuilder.build())
                .build();

        javaFile.writeTo(new File(GlobalSettings.OUT_DIR));
    }

    private void GenViewMetaClass(ViewModel viewModel) throws IOException {
        TypeSpec.Builder objectJavaClassBuilder = TypeSpec.interfaceBuilder(viewModel.getName() + "Meta")
                .addModifiers(Modifier.PUBLIC);

        FieldSpec uriSpec = FieldSpec.builder(GlobalSettings.URI_CLASS, "CONTENT_URI", Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                .initializer("$T.parse($S + $T.CONTENT_AUTHORITY + $S)", GlobalSettings.URI_CLASS, "content://", GlobalSettings.GLOBAL_SETTINGS_CLASS, "/" + viewModel.getName())
                .build();
        objectJavaClassBuilder.addField(uriSpec);
        
        for (ViewColumn fm : viewModel.getColumns()) {
            FieldSpec fieldSpec = FieldSpec.builder(String.class, "COL_" +((fm.getName().equals("_id"))?"ID":fm.getName().toUpperCase()), Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                    .initializer("$S", fm.getName())
                    .build();

            objectJavaClassBuilder.addField(fieldSpec);
        }

        JavaFile javaFile = JavaFile.builder(GlobalSettings.META_PACKAGE_NAME, objectJavaClassBuilder.build())
                .build();

        javaFile.writeTo(new File(GlobalSettings.OUT_DIR));
    }

    private void GenModelClass(ObjectModel objectModel) throws IOException {
        TypeSpec.Builder objectJavaClassBuilder = TypeSpec.classBuilder(objectModel.getName())
                .addModifiers(Modifier.PUBLIC);

        objectJavaClassBuilder.addField(
                FieldSpec.builder(
                    ParameterizedTypeName.get(GlobalSettings.BEFORE_UPDATE_CLASS, ClassName.get(GlobalSettings.MODEL_PACKAGE_NAME,objectModel.getName())),
                    "onBeforeSave",
                    Modifier.STATIC, Modifier.PUBLIC)
                .initializer("null")
                .build()
        );

        objectJavaClassBuilder.addField(FieldSpec.builder(long.class, "mId", Modifier.PRIVATE)
                .build());

        MethodSpec getFieldId = MethodSpec.methodBuilder("getId")
                .addModifiers(Modifier.PUBLIC)
                .returns(long.class)
                .addStatement("return this.mId")
                .build();
        objectJavaClassBuilder.addMethod(getFieldId);

        MethodSpec setFieldId = MethodSpec.methodBuilder("setId")
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get(GlobalSettings.MODEL_PACKAGE_NAME, objectModel.getName()))
                .addParameter(long.class, "pId")
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
                    .returns(ClassName.get(GlobalSettings.MODEL_PACKAGE_NAME, objectModel.getName()))
                    .addParameter(fieldType, "p" + fm.getName())
                    .addStatement("this.m$L=p$L", fm.getName(), fm.getName())
                    .addStatement("return this")
                    .build();
            objectJavaClassBuilder.addMethod(setField);
        }

        JavaFile javaFile = JavaFile.builder(GlobalSettings.MODEL_PACKAGE_NAME, objectJavaClassBuilder.build())
                .build();

        javaFile.writeTo(new File(GlobalSettings.OUT_DIR));
    }

    private void GenViewModelClass(ViewModel viewModel) throws IOException {
        TypeSpec.Builder objectJavaClassBuilder = TypeSpec.classBuilder(viewModel.getName())
                .addModifiers(Modifier.PUBLIC);

        for (ViewColumn fm : viewModel.getColumns()) {
            Class fieldType = Utils.getTypeByName(fm.getType());
            FieldSpec fieldSpec = FieldSpec.builder(fieldType, "m" + ((fm.getName().equals("_id"))?"Id":fm.getName()), Modifier.PRIVATE)
                    .build();
            objectJavaClassBuilder.addField(fieldSpec);

            MethodSpec getField = MethodSpec.methodBuilder("get" + ((fm.getName().equals("_id"))?"Id":fm.getName()))
                    .addModifiers(Modifier.PUBLIC)
                    .returns(fieldType)
                    .addStatement("return this.m" + ((fm.getName().equals("_id"))?"Id":fm.getName()))
                    .build();
            objectJavaClassBuilder.addMethod(getField);

            MethodSpec setField = MethodSpec.methodBuilder("set" + ((fm.getName().equals("_id"))?"Id":fm.getName()))
                    .addModifiers(Modifier.PUBLIC)
                    .returns(ClassName.get(GlobalSettings.MODEL_PACKAGE_NAME, viewModel.getName()))
                    .addParameter(fieldType, "p" + ((fm.getName().equals("_id"))?"Id":fm.getName()))
                    .addStatement("this.m$L=p$L", ((fm.getName().equals("_id"))?"Id":fm.getName()), ((fm.getName().equals("_id"))?"Id":fm.getName()))
                    .addStatement("return this")
                    .build();
            objectJavaClassBuilder.addMethod(setField);
        }

        JavaFile javaFile = JavaFile.builder(GlobalSettings.MODEL_PACKAGE_NAME, objectJavaClassBuilder.build())
                .build();

        javaFile.writeTo(new File(GlobalSettings.OUT_DIR));
    }
    
    private void GenGetResolver(ObjectModel objectModel) throws IOException {
        ClassName metaClass = ClassName.get(GlobalSettings.META_PACKAGE_NAME, objectModel.getName() + "Meta");
        ClassName modelClass = ClassName.get(GlobalSettings.MODEL_PACKAGE_NAME, objectModel.getName());

        TypeSpec.Builder objectJavaClassBuilder = TypeSpec.classBuilder(objectModel.getName() + "GetResolver")
                .superclass(ParameterizedTypeName.get(GlobalSettings.DEFAULT_GET_RESOLVER_CLASS, modelClass))
                .addModifiers(Modifier.PUBLIC);

        MethodSpec.Builder mapGet = MethodSpec.methodBuilder("mapFromCursor")
                .addModifiers(Modifier.PUBLIC)
                .returns(modelClass)
                .addAnnotation(Override.class)
                .addAnnotation(GlobalSettings.NON_NULL_CLASS)
                .addParameter(ParameterSpec.builder(GlobalSettings.CURSOR_CLASS, "cursor")
                        .addAnnotation(GlobalSettings.NON_NULL_CLASS)
                        .build())
                .addStatement("$T object=new $T()", modelClass, modelClass)
                .addStatement("object.setId($T.CursorToLong(cursor,cursor.getColumnIndex($T.COL_ID)))", GlobalSettings.BLL_CONVERTER_CLASS, metaClass);

        for (FieldModel fieldModel : objectModel.getFields()) {
            mapGet.addStatement("object.set$L($T.CursorTo$L(cursor,cursor.getColumnIndex($T.COL_$L)))", fieldModel.getName(), GlobalSettings.BLL_CONVERTER_CLASS, Utils.getPrefixByName(fieldModel.getType()), metaClass, fieldModel.getName().toUpperCase());
        }
        mapGet
                .addStatement("return object");

        objectJavaClassBuilder.addMethod(mapGet.build());
        JavaFile javaFile = JavaFile.builder(GlobalSettings.RESOLVER_PACKAGE_NAME, objectJavaClassBuilder.build()).build();
        javaFile.writeTo(new File(GlobalSettings.OUT_DIR));
    }

    private void GenViewGetResolver(ViewModel viewModel) throws IOException {
        ClassName metaClass = ClassName.get(GlobalSettings.META_PACKAGE_NAME, viewModel.getName() + "Meta");
        ClassName modelClass = ClassName.get(GlobalSettings.MODEL_PACKAGE_NAME, viewModel.getName());

        TypeSpec.Builder objectJavaClassBuilder = TypeSpec.classBuilder(viewModel.getName() + "GetResolver")
                .superclass(ParameterizedTypeName.get(GlobalSettings.DEFAULT_GET_RESOLVER_CLASS, modelClass))
                .addModifiers(Modifier.PUBLIC);

        MethodSpec.Builder mapGet = MethodSpec.methodBuilder("mapFromCursor")
                .addModifiers(Modifier.PUBLIC)
                .returns(modelClass)
                .addAnnotation(Override.class)
                .addAnnotation(GlobalSettings.NON_NULL_CLASS)
                .addParameter(ParameterSpec.builder(GlobalSettings.CURSOR_CLASS, "cursor")
                        .addAnnotation(GlobalSettings.NON_NULL_CLASS)
                        .build())
                .addStatement("$T object=new $T()", modelClass, modelClass);

        for (ViewColumn fm : viewModel.getColumns()) {
            mapGet.addStatement("object.set$L($T.CursorTo$L(cursor,cursor.getColumnIndex($T.COL_$L)))", ((fm.getName().equals("_id"))?"Id":fm.getName()), GlobalSettings.BLL_CONVERTER_CLASS, Utils.getPrefixByName(fm.getType()), metaClass, ((fm.getName().equals("_id"))?"ID":fm.getName().toUpperCase()));
        }
        mapGet
                .addStatement("return object");

        objectJavaClassBuilder.addMethod(mapGet.build());
        JavaFile javaFile = JavaFile.builder(GlobalSettings.RESOLVER_PACKAGE_NAME, objectJavaClassBuilder.build()).build();
        javaFile.writeTo(new File(GlobalSettings.OUT_DIR));
    }
    
    private void GenPutResolver(ObjectModel objectModel) throws IOException {
        ClassName metaClass = ClassName.get(GlobalSettings.META_PACKAGE_NAME, objectModel.getName() + "Meta");
        ClassName modelClass = ClassName.get(GlobalSettings.MODEL_PACKAGE_NAME, objectModel.getName());

        TypeSpec.Builder objectJavaClassBuilder = TypeSpec.classBuilder(objectModel.getName() + "PutResolver")
                .superclass(ParameterizedTypeName.get(GlobalSettings.DEFAULT_PUT_RESOLVER_CLASS, modelClass))
                .addModifiers(Modifier.PUBLIC);

        MethodSpec.Builder mapToInsertQuery = MethodSpec.methodBuilder("mapToInsertQuery")
                .returns(GlobalSettings.INSERT_QUERY_CLASS)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addAnnotation(GlobalSettings.NON_NULL_CLASS)
                .addParameter(ParameterSpec.builder(modelClass, "object").addAnnotation(GlobalSettings.NON_NULL_CLASS).build())
                .addStatement("return $T.builder().uri($T.CONTENT_URI).build()", GlobalSettings.INSERT_QUERY_CLASS, metaClass);

        objectJavaClassBuilder.addMethod(mapToInsertQuery.build());

        MethodSpec.Builder mapToUpdateQuery = MethodSpec.methodBuilder("mapToUpdateQuery")
                .returns(GlobalSettings.UPDATE_QUERY_CLASS)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addAnnotation(GlobalSettings.NON_NULL_CLASS)
                .addParameter(ParameterSpec.builder(modelClass, "object").addAnnotation(GlobalSettings.NON_NULL_CLASS).build())
                .addStatement("return $T.builder().uri($T.CONTENT_URI).where($S).whereArgs($T.toString(object.getId())).build()", GlobalSettings.UPDATE_QUERY_CLASS, metaClass, "_id = ?", Long.class);

        objectJavaClassBuilder.addMethod(mapToUpdateQuery.build());

        MethodSpec.Builder mapToContentValues = MethodSpec.methodBuilder("mapToContentValues")
                .returns(GlobalSettings.CONTENT_VALUE_CLASS)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addAnnotation(GlobalSettings.NON_NULL_CLASS)
                .addParameter(ParameterSpec.builder(modelClass, "object").addAnnotation(GlobalSettings.NON_NULL_CLASS).build())
                .addStatement("final $T contentValues=new $T()", GlobalSettings.CONTENT_VALUE_CLASS, GlobalSettings.CONTENT_VALUE_CLASS);

        mapToContentValues.beginControlFlow("if($T.onBeforeSave != null)", modelClass);
        mapToContentValues.addStatement("$T.onBeforeSave.BeforeSave(object)", modelClass);
        mapToContentValues.endControlFlow();
        
        for (FieldModel fieldModel : objectModel.getFields()) {
            mapToContentValues
                    .addStatement("contentValues.put($T.COL_$L, $T.ObjectToCV(object.get$L()))", metaClass, fieldModel.getName().toUpperCase(), GlobalSettings.BLL_CONVERTER_CLASS, fieldModel.getName());
        }

        mapToContentValues
                .addStatement("return contentValues");

        objectJavaClassBuilder.addMethod(mapToContentValues.build());

        JavaFile javaFile = JavaFile.builder(GlobalSettings.RESOLVER_PACKAGE_NAME, objectJavaClassBuilder.build()).build();
        javaFile.writeTo(new File(GlobalSettings.OUT_DIR));
    }

    private void GenDeleteResolver(ObjectModel objectModel) throws IOException {
        ClassName metaClass = ClassName.get(GlobalSettings.META_PACKAGE_NAME, objectModel.getName() + "Meta");
        ClassName modelClass = ClassName.get(GlobalSettings.MODEL_PACKAGE_NAME, objectModel.getName());

        TypeSpec.Builder objectJavaClassBuilder = TypeSpec.classBuilder(objectModel.getName() + "DeleteResolver")
                .superclass(ParameterizedTypeName.get(GlobalSettings.DEFAULT_DELETE_RESOLVER_CLASS, modelClass))
                .addModifiers(Modifier.PUBLIC);

        MethodSpec.Builder mapToDeleteQuery = MethodSpec.methodBuilder("mapToDeleteQuery")
                .returns(GlobalSettings.DELETE_QUERY_CLASS)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addAnnotation(GlobalSettings.NON_NULL_CLASS)
                .addParameter(ParameterSpec.builder(modelClass, "object").addAnnotation(GlobalSettings.NON_NULL_CLASS).build())
                .addStatement("return $T.builder().uri($T.CONTENT_URI).where($S).whereArgs($T.toString(object.getId())).build()", GlobalSettings.DELETE_QUERY_CLASS, metaClass, "_id = ?", Long.class);

        objectJavaClassBuilder.addMethod(mapToDeleteQuery.build());

        JavaFile javaFile = JavaFile.builder(GlobalSettings.RESOLVER_PACKAGE_NAME, objectJavaClassBuilder.build()).build();
        javaFile.writeTo(new File(GlobalSettings.OUT_DIR));
    }

    private void GenResolver(ObjectModel objectModel) throws IOException {
        ClassName modelClass = ClassName.get(GlobalSettings.MODEL_PACKAGE_NAME, objectModel.getName());
        ClassName getRslvClass = ClassName.get(GlobalSettings.RESOLVER_PACKAGE_NAME, objectModel.getName() + "GetResolver");
        ClassName putRslvClass = ClassName.get(GlobalSettings.RESOLVER_PACKAGE_NAME, objectModel.getName() + "PutResolver");
        ClassName deleteRslvClass = ClassName.get(GlobalSettings.RESOLVER_PACKAGE_NAME, objectModel.getName() + "DeleteResolver");

        GenGetResolver(objectModel);
        GenPutResolver(objectModel);
        GenDeleteResolver(objectModel);

        TypeSpec.Builder objectJavaClassBuilder = TypeSpec.classBuilder(objectModel.getName() + "Resolver")
                .superclass(ParameterizedTypeName.get(GlobalSettings.CONTENT_RESOLVER_MAPPING_CLASS, modelClass))
                .addModifiers(Modifier.PUBLIC);

        MethodSpec constr = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("super(new $T(),new $T(),new $T())", putRslvClass, getRslvClass, deleteRslvClass)
                .build();
        objectJavaClassBuilder.addMethod(constr);
        JavaFile javaFile = JavaFile.builder(GlobalSettings.RESOLVER_PACKAGE_NAME, objectJavaClassBuilder.build()).build();
        javaFile.writeTo(new File(GlobalSettings.OUT_DIR));
    }
}
