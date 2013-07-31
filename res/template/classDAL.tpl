public class ${name}DAL {
public static final String TABLE="${table}";

public static final String CONTENT_TYPE ="vnd.android.cursor.dir/vnd.money2013.${table}";
public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.money2013.${table}";

public static final Uri CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY+"/${table}");

#foreach($field in $fields)
public static final String COL_${field.Name}="${field.Column}"; 
#end

public static final String CREATE_TABLE_SQL="CREATE TABLE ${table}(_id INTEGER PRIMARY KEY AUTOINCREMENT#foreach($field in $fields), ${field.Column} ${field.ColumnType}#end)";
}