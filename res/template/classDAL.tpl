package iae.home.money2011.v2.dal;

import android.content.ContentValues;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;

public final class ${name}DAL {
public static final String TABLE="${table}";

public static final String CONTENT_TYPE ="vnd.android.cursor.dir/vnd.money2013.${name}";
public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.money2013.${name}";

public static final Uri CONTENT_URI = Uri.parse("content://" + MoneyProvider.CONTENT_AUTHORITY+"/${name}");

// Columns
#foreach($field in $fields)
public static final String COL_${field.Name.toUpperCase()}="${field.Column}"; 
#end

public static final String CREATE_TABLE_SQL="CREATE TABLE ${table}(_id INTEGER PRIMARY KEY AUTOINCREMENT#foreach($field in $fields), ${field.Column} ${field.ColumnType}#end)";

// Views
#foreach($view in $views)
public static class View${view.Name} {

public static final Uri CONTENT_URI = Uri.parse("content://" + MoneyProvider.CONTENT_AUTHORITY+"/${name}/${view.Name}");

// Columns
#foreach($column in $view.Columns)
public static final String COL_${column.Name.toUpperCase()}="${column.Name}"; 
#end
}
#end
}