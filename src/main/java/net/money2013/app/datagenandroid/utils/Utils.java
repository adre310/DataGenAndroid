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
        
        public static Class getTypeByName(String sType) throws Exception {
            String lowerType=sType.toLowerCase();
            if(lowerType.equals("string"))
                return String.class;
            if(lowerType.equals("int"))
		return int.class;
            if(lowerType.equals("double"))
		return double.class;
            if(lowerType.equals("date"))
		return Date.class;
            if(lowerType.equals("boolean"))
		return boolean.class;
            if(lowerType.equals("long"))
		return long.class;
            else
		throw new Exception("Inalid sType "+sType);            
        }

        public static String getPrefixByName(String sType) {
            if(sType.equals("String"))
                return "String";
            if(sType.equals("int"))
		return "Integer";
            if(sType.equals("double"))
		return "Double";
            if(sType.equals("Date"))
		return "Date";
            if(sType.equals("boolean"))
		return "Boolean";
            if(sType.equals("long"))
		return "Long";
            else
		return "String";            
        }
        
}
