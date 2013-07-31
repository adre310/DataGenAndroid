package iae.home.datagenerator.utils;

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
			return "--------------";
	}
}
