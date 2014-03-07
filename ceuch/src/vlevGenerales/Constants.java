package vlevGenerales;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

final public class Constants
{
    public static final String URL_SERVER = "http://10.0.2.2/ceuch/admin/";
    //public static final String URL_SERVER = "http://cruzadaeucaristica.com/admin/";

    private Constants()
    {
    }
    
    
    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate){

	    Date parsed = null;
	    String outputDate = "";

	    SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
	    SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

	    try {
	        parsed = df_input.parse(inputDate);
	        outputDate = df_output.format(parsed);
	    } catch (java.text.ParseException e) { 
	    	Log.e("FECHA", e.toString());
	    }

	    return outputDate;
	}
	
}
