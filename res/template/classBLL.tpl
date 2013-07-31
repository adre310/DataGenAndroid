package iae.home.money2011.v2.bll.objects;

import iae.home.money2011.v2.dal.${name}DAL;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class ${name}BLL implements Parcelable {

public static final String[] ITEM_PROJECTION=new String[] {
"_id"#foreach($field in $fields),"${field.Column}"#end

};

private Integer mId;
public Integer getId(){ return mId;}
public void setId(Integer Id) { mId=Id; } 

#foreach($field in $fields)

private ${field.Type} m${field.Name};
public ${field.Type} get${field.Name}(){ return m${field.Name};}
public void set${field.Name}(${field.Type} ${field.Name}) { m${field.Name}=${field.Name}; } 
#end

public ${name}BLL() {};

public ${name}BLL(Parcel in) {
}

@Override
public int describeContents() { return 0; }

@Override
public void writeToParcel(Parcel out, int flags) {
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


}