/**
 * 
 */
package iae.home.money2011.v2.dal;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.util.Log;

/**
 * @author aisaev
 *
 */
public class MoneyProvider extends ContentProvider {

    private static final UriMatcher mUriMatcher;
    private MoneyDatabase mOpenHelper;

#set($id = 1)
#foreach($object in $model.objects)

private static final int ID_${object.Name.toUpperCase()}=${id}; #set($id = $id+1)

private static final int ID_${object.Name.toUpperCase()}_ITEM=${id}; #set($id = $id+1)

#foreach($view in $object.Views)
private static final int ID_${object.Name.toUpperCase()}_VIEW_${view.Name.toUpperCase()}=${id}; #set($id = $id+1)

#end
#end

#foreach($object in $model.objects)
private static HashMap<String, String> m${object.Name}ProjectionMap;
#foreach($view in $object.Views)
private static HashMap<String, String> m${object.Name}${view.Name}ProjectionMap;
#end
#end

static {
mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
final String authority=BaseEntity.CONTENT_AUTHORITY;

#foreach($object in $model.objects)
mUriMatcher.addURI(authority, "${object.Name}", ID_${object.Name.toUpperCase()});
mUriMatcher.addURI(authority, "${object.Name}/#", ID_${object.Name.toUpperCase()}_ITEM);
#foreach($view in $object.Views)
mUriMatcher.addURI(authority, "${object.Name}/${view.Name}", ID_${object.Name.toUpperCase()}_VIEW_${view.Name.toUpperCase()});
#end
#end

#foreach($object in $model.objects)
m${object.Name}ProjectionMap=new HashMap<String, String>();
m${object.Name}ProjectionMap.put("_id", "_id");
#foreach($field in $object.Fields)
m${object.Name}ProjectionMap.put(${object.Name}DAL.COL_${field.Name.toUpperCase()}, ${object.Name}DAL.COL_${field.Name.toUpperCase()});
#end

#foreach($view in $object.Views)
m${object.Name}${view.Name}ProjectionMap=new HashMap<String, String>();
m${object.Name}${view.Name}ProjectionMap.put("_id", "_id");
#foreach($column in $view.Columns)
m${object.Name}${view.Name}ProjectionMap.put(${object.Name}DAL.View${view.Name}.COL_${column.Name.toUpperCase()}, "${column.Expr} AS "+${object.Name}DAL.View${view.Name}.COL_${column.Name.toUpperCase()});
#end
#end
#end

};


@Override
public String getType(Uri uri) {
	final int match = mUriMatcher.match(uri);
    switch (match) {
#foreach($object in $model.objects)

	case ID_${object.Name.toUpperCase()}_ITEM:
		return ${object.Name}DAL.CONTENT_ITEM_TYPE;
	case ID_${object.Name.toUpperCase()}:
#foreach($view in $object.Views)
	case ID_${object.Name.toUpperCase()}_VIEW_${view.Name.toUpperCase()}:
#end
		return ${object.Name}DAL.CONTENT_TYPE;
#end
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
	}
}

@Override
public Uri insert(Uri uri, ContentValues values) {
	final int match = mUriMatcher.match(uri);
    SQLiteDatabase database = mOpenHelper.getWritableDatabase();
    long rowId=-1;
        
    switch (match) {
#foreach($object in $model.objects)
	case ID_${object.Name.toUpperCase()}:
        rowId=database.insert(${object.Name}DAL.TABLE, null, values);
        break;
#end
     default:
        throw new IllegalArgumentException("Unknown URI " + uri);
	}        

// If the insert succeeded, the row ID exists.
	if (rowId > 0) {
        Uri retUri = uri.buildUpon().appendPath(Long.toString(rowId)).build();
        getContext().getContentResolver().notifyChange(retUri, null);
        return retUri;
    }

	throw new SQLException("Failed to insert row into " + uri);
}

@Override
public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
	final SQLiteDatabase database = mOpenHelper.getReadableDatabase();
    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
	final int match = mUriMatcher.match(uri);
	switch(match) {
#foreach($object in $model.objects)

case ID_${object.Name.toUpperCase()}_ITEM:
        qb.setTables(${object.Name}DAL.TABLE);
        qb.setProjectionMap(m${object.Name}ProjectionMap);
        qb.appendWhere(${object.Name}DAL.COL_DELETED+"=0 AND _id="+uri.getPathSegments().get(1));
        cursor=qb.query(
        			database, 
        			projection, 
        			selection, selectionArgs, 
        			null,
        			null,
        			sortOrder);
        break;
case ID_${object.Name.toUpperCase()}:
        qb.setTables(${object.Name}DAL.TABLE);
        qb.setProjectionMap(m${object.Name}ProjectionMap);
        qb.appendWhere(${object.Name}DAL.COL_DELETED+"=0");
        cursor=qb.query(
        			database, 
        			projection, 
        			selection, selectionArgs, 
        			null,
        			null,
        			sortOrder);
        break;
#foreach($view in $object.Views)
case ID_${object.Name.toUpperCase()}_VIEW_${view.Name.toUpperCase()}:
        qb.setTables(${object.Name}DAL.TABLE);
        qb.setProjectionMap(m${object.Name}${view.Name}ProjectionMap);
        qb.appendWhere(${object.Name}DAL.COL_DELETED+"=0");
        cursor=qb.query(
        			database, 
        			projection, 
        			selection, selectionArgs, 
        			null,
        			null,
        			sortOrder);
        break;
#end
#end
default:
	throw new IllegalArgumentException("Unknown URI " + uri);
}
        
cursor.setNotificationUri(getContext().getContentResolver(), uri);
return cursor;
}

@Override
public int update(Uri uri, ContentValues values, String selection,
		String[] selectionArgs) {
final int match = mUriMatcher.match(uri);
int count=0;
SQLiteDatabase database = mOpenHelper.getWritableDatabase();

switch (match) {
#foreach($object in $model.objects)

case ID_${object.Name.toUpperCase()}_ITEM:
    count=database.update(${object.Name}DAL.TABLE, values, "_id=?", new String[] { uri.getPathSegments().get(1) });
	break;
case ID_${object.Name.toUpperCase()}:
    count=database.update(${object.Name}DAL.TABLE, values, selection, selectionArgs);
	break;
#end
default:
	throw new IllegalArgumentException("Unknown URI " + uri);
}
        
getContext().getContentResolver().notifyChange(uri, null);
return count;
}

}