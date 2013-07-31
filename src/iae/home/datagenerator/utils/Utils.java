package iae.home.datagenerator.utils;

public class Utils {

	public static String getColumnTypeByType(String sType) {
		if(sType.equals("String"))
			return "TEXT";
		if(sType.equals("Integer"))
			return "INTEGER";
		if(sType.equals("Double"))
			return "REAL";
		if(sType.equals("Date"))
			return "INTEGER";
		if(sType.equals("Boolean"))
			return "INTEGER";
		else
			return "--------------";
	}
}
