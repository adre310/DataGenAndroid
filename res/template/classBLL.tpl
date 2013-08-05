package iae.home.money2011.v2.bll.objects;

import iae.home.money2011.v2.dal.${name}DAL;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;
import java.util.Date;

import org.json.JSONObject;

public class ${name}BLL implements Parcelable {

public static final String[] ITEM_PROJECTION=new String[] {
"_id"
#foreach($field in $fields)
,${name}DAL.COL_${field.Name.toUpperCase()}
#end
};

#set($id=0)
public static final int ID_COL=${id}; #set($id=$id+1)

#foreach($field in $fields)
public static final int ID_COL_${field.Name.toUpperCase()}=${id}; #set($id=$id+1)

#end

private long mId;
public long getId(){ return mId;}
public void setId(long Id) { mId=Id; } 
#foreach($field in $fields)

private ${field.Type} m${field.Name};
public ${field.Type} get${field.Name}(){ return m${field.Name};}
public void set${field.Name}(${field.Type} ${field.Name}) { m${field.Name}=${field.Name}; } 
#end

/*
public void setNew() {
    this.mId=-1L;
    this.mGuid=UUID.randomUUID().toString();
}
*/

public void setData(Cursor cursor) {
	mId=BLLConverter.CursorToObject(cursor,ID_COL,mId);
#foreach($field in $fields)
	m${field.Name}=BLLConverter.CursorToObject(cursor,ID_COL_${field.Name.toUpperCase()},m${field.Name});
#end
}

public void save(ContentValues cv) {
#foreach($field in $fields)
	cv.put(${name}DAL.COL_${field.Name.toUpperCase()}, BLLConverter.ObjectToCV(m${field.Name}));
#end
}

public ${name}BLL() {
};

public ${name}BLL(Parcel in) {
	mId=BLLConverter.ParcelToObject(in,mId);
#foreach($field in $fields)
	m${field.Name}=BLLConverter.ParcelToObject(in,m${field.Name});
#end
}

@Override
public int describeContents() { return 0; }

@Override
public void writeToParcel(Parcel out, int flags) {
	BLLConverter.ObjectToParcel(out,mId);
#foreach($field in $fields)
	BLLConverter.ObjectToParcel(out,m${field.Name});
#end
}

public static final Parcelable.Creator<${name}BLL> CREATOR=
	new Parcelable.Creator<${name}BLL>() {

	@Override
	public ${name}BLL createFromParcel(Parcel in) {
		return new ${name}BLL(in);
	}

	@Override
	public ${name}BLL[] newArray(int size) {
		return new ${name}BLL[size];
	}
		
};

// Views
#foreach($view in $views)
public static class View${view.Name} {

public static final String[] ITEM_PROJECTION=new String[] {
"_id"
#foreach($column in $view.Columns)
,${name}DAL.View${view.Name}.COL_${column.Name.toUpperCase()} 
#end
};

#set($id=0)
public static final int ID_COL=${id}; #set($id=$id+1)

#foreach($column in $view.Columns)
public static final int ID_COL_${column.Name.toUpperCase()}=${id}; #set($id=$id+1)

#end
}
#end

}