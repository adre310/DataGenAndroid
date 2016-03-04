/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.money2013.app.datagenandroid;

import com.squareup.javapoet.ClassName;

/**
 *
 * @author aisaev
 */
public interface GlobalSettings {
    public static final String PACKAGE_NAME="net.money2013.app.hb";
    public static final String PERSIST_PACKAGE_NAME=PACKAGE_NAME+".persist";
    public static final String META_PACKAGE_NAME=PACKAGE_NAME+".persist.meta";
    public static final String MODEL_PACKAGE_NAME=PACKAGE_NAME+".persist.model";
    public static final String RESOLVER_PACKAGE_NAME=PACKAGE_NAME+".persist.resolver";
    public static final String PROVIDER_PACKAGE_NAME=PACKAGE_NAME+".persist";
    public static final String OUT_DIR="c:/tmp";
    public static final String MAIN_OUT_DIR="c:/tmp/main";
    public static final String TEST_OUT_DIR="c:/tmp/androidTest";
 
    public static final ClassName APP_MAIN_CLASS = ClassName.get(GlobalSettings.PACKAGE_NAME, "AppMain");
    public static final ClassName GLOBAL_SETTINGS_CLASS = ClassName.get(GlobalSettings.PACKAGE_NAME, "GlobalSettings");
    public static final ClassName CONTENT_PROVIDER_CLASS = ClassName.get("android.content", "ContentProvider");
    public static final ClassName CONTENT_VALUE_CLASS = ClassName.get("android.content","ContentValues");
    public static final ClassName URI_CLASS = ClassName.get("android.net", "Uri");
    public static final ClassName DEFAULT_GET_RESOLVER_CLASS = ClassName.get("com.pushtorefresh.storio.contentresolver.operations.get", "DefaultGetResolver");
    public static final ClassName DEFAULT_PUT_RESOLVER_CLASS = ClassName.get("com.pushtorefresh.storio.contentresolver.operations.put", "DefaultPutResolver");
    public static final ClassName DEFAULT_DELETE_RESOLVER_CLASS = ClassName.get("com.pushtorefresh.storio.contentresolver.operations.delete", "DefaultDeleteResolver");
    public static final ClassName CONTENT_RESOLVER_MAPPING_CLASS = ClassName.get("com.pushtorefresh.storio.contentresolver","ContentResolverTypeMapping");
    public static final ClassName INSERT_QUERY_CLASS = ClassName.get("com.pushtorefresh.storio.contentresolver.queries","InsertQuery");
    public static final ClassName UPDATE_QUERY_CLASS = ClassName.get("com.pushtorefresh.storio.contentresolver.queries","UpdateQuery");
    public static final ClassName DELETE_QUERY_CLASS = ClassName.get("com.pushtorefresh.storio.contentresolver.queries","DeleteQuery");
    public static final ClassName CURSOR_CLASS = ClassName.get("android.database", "Cursor");
    public static final ClassName NON_NULL_CLASS = ClassName.get("android.support.annotation", "NonNull");
    public static final ClassName URI_MATCHER_CLASS=ClassName.get("android.content","UriMatcher");
    public static final ClassName HASH_MAP_CLASS=ClassName.get("java.util","HashMap");
    //public static final ClassName MONEY_DATABASE_CLASS=ClassName.get(PROVIDER_PACKAGE_NAME,"MoneyDatabase");
    public static final ClassName SQL_DATABASE_CLASS=ClassName.get("android.database.sqlite","SQLiteDatabase");
    public static final ClassName SQL_EXCEPTION_CLASS=ClassName.get("android.database","SQLException");
    public static final ClassName SQL_BUILDER_CLASS=ClassName.get("android.database.sqlite","SQLiteQueryBuilder");
    public static final ClassName SQL_HELPER_CLASS=ClassName.get("android.database.sqlite","SQLiteOpenHelper");
    public static final ClassName BLL_CONVERTER_CLASS = ClassName.get(PERSIST_PACKAGE_NAME+".util", "BLLConverter");    
    public static final ClassName BEFORE_UPDATE_CLASS = ClassName.get(PERSIST_PACKAGE_NAME+".util", "OnBeforeUpdate");
    public static final ClassName INJECT_CLASS=ClassName.get("javax.inject","Inject");
    public static final ClassName PARCELABLE_CLASS=ClassName.get("android.os","Parcelable");
    public static final ClassName PARCELABLE_CREATOR_CLASS=ClassName.get("android.os.Parcelable","Creator");
    public static final ClassName PARCEL_CLASS=ClassName.get("android.os","Parcel");
}
