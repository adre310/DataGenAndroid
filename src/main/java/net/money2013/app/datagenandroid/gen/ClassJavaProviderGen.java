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
import net.money2013.app.datagenandroid.model.ViewColumn;
import net.money2013.app.datagenandroid.model.ViewJoin;
import net.money2013.app.datagenandroid.model.ViewModel;

/**
 *
 * @author aisaev
 */
public class ClassJavaProviderGen {
    private final DataModel dataModel;

    public ClassJavaProviderGen(DataModel dataModel) {
        this.dataModel = dataModel;
    }

    public void gen() throws IOException {
        TypeSpec.Builder objectJavaClassBuilder = TypeSpec.classBuilder("MoneyProvider")
                .superclass(GlobalSettings.CONTENT_PROVIDER_CLASS)
                .addModifiers(Modifier.PUBLIC);

        objectJavaClassBuilder.addField(
                FieldSpec.builder(GlobalSettings.SQL_HELPER_CLASS, "dbOpenHelper")
                    .addAnnotation(GlobalSettings.INJECT_CLASS)
                    .build());
        objectJavaClassBuilder.addField(
                FieldSpec.builder(ClassName.get(GlobalSettings.PROVIDER_PACKAGE_NAME,"MoneyProvider"), "instance", Modifier.PUBLIC, Modifier.STATIC)
                    .initializer("null")
                    .build());
        
        genGetType(objectJavaClassBuilder);
        genStatic(objectJavaClassBuilder);
        genOnCreate(objectJavaClassBuilder);
        genQuery(objectJavaClassBuilder);
        genInsert(objectJavaClassBuilder);
        genUpdate(objectJavaClassBuilder);
        genDelete(objectJavaClassBuilder);
        
        JavaFile javaFile = JavaFile.builder(GlobalSettings.PROVIDER_PACKAGE_NAME, objectJavaClassBuilder.build())
                .build();
        javaFile.writeTo(new File(GlobalSettings.OUT_DIR));        
    }
    
    private void genStatic(TypeSpec.Builder classBuilder) {
        classBuilder.addField(FieldSpec.builder(GlobalSettings.URI_MATCHER_CLASS, "uriMatcher", Modifier.FINAL, Modifier.PRIVATE, Modifier.STATIC)
                .initializer("new $T($T.NO_MATCH)", GlobalSettings.URI_MATCHER_CLASS, GlobalSettings.URI_MATCHER_CLASS)
                .build());
        int objNumerator=1;
        for(ObjectModel objectModel : dataModel.getObjects()) {
            classBuilder.addField(FieldSpec.builder(int.class, "ID_"+objectModel.getName().toUpperCase(), Modifier.FINAL, Modifier.PRIVATE, Modifier.STATIC)
                    .initializer("$L", Integer.toString(objNumerator++))
                    .build());
        }
        for(ViewModel viewModel : dataModel.getViewList()) {
            classBuilder.addField(FieldSpec.builder(int.class, "ID_"+viewModel.getName().toUpperCase(), Modifier.FINAL, Modifier.PRIVATE, Modifier.STATIC)
                    .initializer("$L", Integer.toString(objNumerator++))
                    .build());            
        }

        for(ViewModel viewModel : dataModel.getViewList()) {
            classBuilder.addField(FieldSpec.builder(ParameterizedTypeName.get(GlobalSettings.HASH_MAP_CLASS, ClassName.get(String.class), ClassName.get(String.class)), viewModel.getName()+"ProjectionMap", Modifier.FINAL, Modifier.PRIVATE, Modifier.STATIC)
                    .initializer("new $T()", ParameterizedTypeName.get(GlobalSettings.HASH_MAP_CLASS, ClassName.get(String.class), ClassName.get(String.class)))
                    .build());
        }
        
        CodeBlock.Builder codeBlock=CodeBlock.builder();
        for(ObjectModel objectModel : dataModel.getObjects()) {
            codeBlock
                .addStatement("uriMatcher.addURI($T.CONTENT_AUTHORITY, $S, $L)", GlobalSettings.GLOBAL_SETTINGS_CLASS,objectModel.getUri(), "ID_"+objectModel.getName().toUpperCase());
        }
        for(ViewModel viewModel : dataModel.getViewList()) {
            codeBlock
                .addStatement("uriMatcher.addURI($T.CONTENT_AUTHORITY, $S, $L)", GlobalSettings.GLOBAL_SETTINGS_CLASS, viewModel.getUri(), "ID_"+viewModel.getName().toUpperCase());
        }
        
        for(ViewModel viewModel : dataModel.getViewList()) {
            String mapName=viewModel.getName()+"ProjectionMap";
            ClassName metaClass = ClassName.get(GlobalSettings.META_PACKAGE_NAME, viewModel.getName() + "Meta");
            for(ViewColumn fm : viewModel.getColumns()) {
                codeBlock.addStatement("$L.put($T.COL_$L, $S + $T.COL_$L)", 
                        mapName, 
                        metaClass, ((fm.getName().equals("_id"))?"ID":fm.getName().toUpperCase()),
                        fm.getExpr()+" AS ",
                        metaClass, ((fm.getName().equals("_id"))?"ID":fm.getName().toUpperCase())
                );
            }            
        }
        /*
        for(ObjectModel objectModel : dataModel.getObjects()) {
            String mapName=objectModel.getName()+"ProjectionMap";
            ClassName metaClass = ClassName.get(GlobalSettings.META_PACKAGE_NAME, objectModel.getName() + "Meta");
            codeBlock.addStatement("$L.put($T.COL_ID, $T.COL_ID)", mapName, metaClass, metaClass);
            for(FieldModel fieldModel : objectModel.getFields()) {
                codeBlock.addStatement("$L.put($T.COL_$L, $T.COL_$L)", mapName, metaClass, fieldModel.getName().toUpperCase(), metaClass, fieldModel.getName().toUpperCase());
            }
         }
        */
        
        classBuilder.addStaticBlock(codeBlock.build());
    }

    private void genGetType(TypeSpec.Builder classBuilder) {
        MethodSpec.Builder getTypeMethod=MethodSpec.methodBuilder("getType")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(String.class)
                    .addParameter(GlobalSettings.URI_CLASS, "uri")
                ;

        getTypeMethod.addStatement("final int match = uriMatcher.match(uri)");
        getTypeMethod.beginControlFlow("switch (match)");
        for(ObjectModel objectModel : dataModel.getObjects()) {
            getTypeMethod.addStatement("case $L: return $S", "ID_"+objectModel.getName().toUpperCase(),"vnd.android.cursor.dir/vnd.money2013."+objectModel.getName());
        }
        for(ViewModel viewModel : dataModel.getViewList()) {
            getTypeMethod.addStatement("case $L: return $S", "ID_"+viewModel.getName().toUpperCase(),"vnd.android.cursor.dir/vnd.money2013."+viewModel.getName());
        }
        getTypeMethod.addStatement("default: throw new $T($S + uri)", IllegalArgumentException.class, "Unknown URI ");
        getTypeMethod.endControlFlow();
        classBuilder.addMethod(getTypeMethod.build());
    }
        
    private void genOnCreate(TypeSpec.Builder classBuilder) {
        classBuilder.addMethod(
                MethodSpec.methodBuilder("onCreate")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(boolean.class)
                    .addStatement("instance=this")
                    .addStatement("(($T)(getContext().getApplicationContext())).appComponent().inject(this)", GlobalSettings.APP_MAIN_CLASS)
                    .addStatement("return true")
                    .build()
        );
    }

    private void genInsert(TypeSpec.Builder classBuilder) {
        MethodSpec.Builder insertMethod=MethodSpec.methodBuilder("insert")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(GlobalSettings.URI_CLASS)
                    .addParameter(GlobalSettings.URI_CLASS, "uri")
                    .addParameter(GlobalSettings.CONTENT_VALUE_CLASS,"values")
                ;
        insertMethod.addStatement("final int match = uriMatcher.match(uri)");
        insertMethod.addStatement("long rowId = -1");
        insertMethod.addStatement("$T database = dbOpenHelper.getWritableDatabase()",GlobalSettings.SQL_DATABASE_CLASS);
        insertMethod.beginControlFlow("switch (match)");
        for(ObjectModel objectModel : dataModel.getObjects()) {
            ClassName metaClass = ClassName.get(GlobalSettings.META_PACKAGE_NAME, objectModel.getName() + "Meta");
            insertMethod.addStatement("case ID_$L: rowId = database.insertOrThrow($T.TABLE, null, values); break", objectModel.getName().toUpperCase(),metaClass);
        }
        insertMethod.addStatement("default: throw new $T($S + uri)", IllegalArgumentException.class, "Unknown URI ");
        insertMethod.endControlFlow();
        
        insertMethod.beginControlFlow("if(rowId > 0)");
        insertMethod.addStatement("$T retUri = uri.buildUpon().appendPath(Long.toString(rowId)).build()", GlobalSettings.URI_CLASS);
        insertMethod.addStatement("return retUri");
        insertMethod.endControlFlow();
        insertMethod.addStatement("throw new $T($S + uri)",GlobalSettings.SQL_EXCEPTION_CLASS, "Failed to insert row into ");
        
        classBuilder.addMethod(insertMethod.build());
    }
    
    private void genQuery(TypeSpec.Builder classBuilder) {
        MethodSpec.Builder queryMethod=MethodSpec.methodBuilder("query")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(GlobalSettings.CURSOR_CLASS)
                    .addParameter(GlobalSettings.URI_CLASS, "uri")
                    .addParameter(String[].class,"projection")
                    .addParameter(String.class,"selection")
                    .addParameter(String[].class,"selectionArgs")
                    .addParameter(String.class,"sortOrder")
                ;
        queryMethod.addStatement("$T database = dbOpenHelper.getReadableDatabase()",GlobalSettings.SQL_DATABASE_CLASS);
        queryMethod.addStatement("$T qb = new $T()", GlobalSettings.SQL_BUILDER_CLASS, GlobalSettings.SQL_BUILDER_CLASS);
        queryMethod.addStatement("$T cursor = null", GlobalSettings.CURSOR_CLASS);
        queryMethod.addStatement("final int match = uriMatcher.match(uri)");
        queryMethod.beginControlFlow("switch (match)");
        for(ObjectModel objectModel : dataModel.getObjects()) {
            ClassName metaClass = ClassName.get(GlobalSettings.META_PACKAGE_NAME, objectModel.getName() + "Meta");
            queryMethod.addStatement("case ID_$L: qb.setTables($T.TABLE)", objectModel.getName().toUpperCase(),metaClass);
            queryMethod.addStatement("cursor = qb.query(database, projection,selection, selectionArgs, null, null, sortOrder)");
            queryMethod.addStatement("break");
        }
        for(ViewModel viewModel : dataModel.getViewList()) {
            String table=viewModel.getTable();
            for(ViewJoin join : viewModel.getJoins()) {
                table+=" "+join.getJoin();
            }
            queryMethod.addStatement("case ID_$L: qb.setTables($S)", viewModel.getName().toUpperCase(),table);
            queryMethod.addStatement("qb.setProjectionMap($LProjectionMap)",viewModel.getName());
            if(viewModel.getGroupBy()==null)
                queryMethod.addStatement("cursor = qb.query(database, projection,selection, selectionArgs, null, null, sortOrder)");
            else
                queryMethod.addStatement("cursor = qb.query(database, projection,selection, selectionArgs, $S, null, sortOrder)",viewModel.getGroupBy());
                
            queryMethod.addStatement("break");
        }

        queryMethod.addStatement("default: throw new $T($S + uri)", IllegalArgumentException.class, "Unknown URI ");
        queryMethod.endControlFlow();
        
        queryMethod.addStatement("cursor.setNotificationUri(getContext().getContentResolver(), uri)");
        queryMethod.addStatement("return cursor");

        classBuilder.addMethod(queryMethod.build());
    }    

    private void genUpdate(TypeSpec.Builder classBuilder) {
        MethodSpec.Builder updateMethod=MethodSpec.methodBuilder("update")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(int.class)
                    .addParameter(GlobalSettings.URI_CLASS, "uri")
                    .addParameter(GlobalSettings.CONTENT_VALUE_CLASS,"values")
                    .addParameter(String.class,"selection")
                    .addParameter(String[].class,"selectionArgs")
                ;

        updateMethod.addStatement("final int match = uriMatcher.match(uri)");
        updateMethod.addStatement("int count = 0");
        updateMethod.addStatement("$T database = dbOpenHelper.getWritableDatabase()",GlobalSettings.SQL_DATABASE_CLASS);
        updateMethod.beginControlFlow("switch (match)");
        for(ObjectModel objectModel : dataModel.getObjects()) {
            ClassName metaClass = ClassName.get(GlobalSettings.META_PACKAGE_NAME, objectModel.getName() + "Meta");
            updateMethod.addStatement("case ID_$L: count = database.update($T.TABLE, values, selection, selectionArgs); break", objectModel.getName().toUpperCase(),metaClass);
        }
        updateMethod.addStatement("default: throw new $T($S + uri)", IllegalArgumentException.class, "Unknown URI ");
        updateMethod.endControlFlow();
        
        updateMethod.addStatement("return count");
        classBuilder.addMethod(updateMethod.build());
    }    

    private void genDelete(TypeSpec.Builder classBuilder) {
        MethodSpec.Builder updateMethod=MethodSpec.methodBuilder("delete")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(int.class)
                    .addParameter(GlobalSettings.URI_CLASS, "uri")
                    .addParameter(String.class,"selection")
                    .addParameter(String[].class,"selectionArgs")
                ;

        updateMethod.addStatement("final int match = uriMatcher.match(uri)");
        updateMethod.addStatement("int count = 0");
        updateMethod.addStatement("$T database = dbOpenHelper.getWritableDatabase()",GlobalSettings.SQL_DATABASE_CLASS);
        updateMethod.beginControlFlow("switch (match)");
        for(ObjectModel objectModel : dataModel.getObjects()) {
            ClassName metaClass = ClassName.get(GlobalSettings.META_PACKAGE_NAME, objectModel.getName() + "Meta");
            updateMethod.addStatement("case ID_$L: count = database.delete($T.TABLE, selection, selectionArgs); break", objectModel.getName().toUpperCase(),metaClass);
        }
        updateMethod.addStatement("default: throw new $T($S + uri)", IllegalArgumentException.class, "Unknown URI ");
        updateMethod.endControlFlow();
        
        updateMethod.addStatement("return count");
        classBuilder.addMethod(updateMethod.build());
    }    
    
}
