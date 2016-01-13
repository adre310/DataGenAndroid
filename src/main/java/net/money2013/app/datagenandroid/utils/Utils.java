package net.money2013.app.datagenandroid.utils;

import java.util.Date;

public class Utils {

	public static String getColumnTypeByType(String sType) {
            if(sType.equals("String"))
		return "TEXT";
            if(sType.equals("int"))
		return "INTEGER";
            if(sType.equals("double"))
		return "REAL";
            if(sType.equals("Date"))
		return "INTEGER";
            if(sType.equals("boolean"))
		return "INTEGER";
            if(sType.equals("long"))
		return "INTEGER";
            else
		return "TEXT";
	}
        
        public static Class getTypeByName(String sType) {
            if(sType.equals("String"))
                return String.class;
            if(sType.equals("int"))
		return Integer.class;
            if(sType.equals("double"))
		return Double.class;
            if(sType.equals("Date"))
		return Date.class;
            if(sType.equals("boolean"))
		return Boolean.class;
            if(sType.equals("long"))
		return Long.class;
            else
		return String.class;            
        }
        
}
