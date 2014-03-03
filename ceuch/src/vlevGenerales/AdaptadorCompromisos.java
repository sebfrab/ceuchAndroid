package vlevGenerales;

import java.util.ArrayList;

import vlev.ceuch.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AdaptadorCompromisos extends ArrayAdapter<Compromisos> {
	Activity context;
	ViewHolder holder;

	public AdaptadorCompromisos(Activity context, int textViewResourceId, ArrayList<Compromisos> items){
		super(context, textViewResourceId, items);
		this.context = context;
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		
		View item = convertView;
		if(item==null){
			LayoutInflater inflater = context.getLayoutInflater();
			item = inflater.inflate(R.layout.listitem_compromisos, null);
			
			holder = new ViewHolder();
			holder.titulo = (TextView)item.findViewById(R.id.lblTitle);
			holder.cuerpo = (TextView)item.findViewById(R.id.lblBody);
			holder.fecha = (TextView)item.findViewById(R.id.lblFecha);
			
			item.setTag(holder);
			
		}else{
			holder = (ViewHolder)item.getTag();
		}
		Compromisos com = getItem(position);
		
		holder.titulo.setText(com.get_title());
		holder.cuerpo.setText(com.get_body());
		holder.fecha.setText(com.get_date());

		return item;
	}
	
	static class ViewHolder {
		TextView titulo;
		TextView cuerpo;
		TextView fecha;
		}
}
