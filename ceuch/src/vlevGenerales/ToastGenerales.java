package vlevGenerales;

import android.content.Context;
import android.widget.Toast;

public  class ToastGenerales {
	
	
	public static void mensaje(Context context, String mensaje, int duracion){
		
		if(duracion==0){
			Toast toast = Toast.makeText(context, mensaje, Toast.LENGTH_SHORT);
			toast.show();
		}else{
			Toast toast = Toast.makeText(context, mensaje, Toast.LENGTH_LONG);
			toast.show();
		}
	}
}
