package vlev.ceuch;

import java.util.ArrayList;
import vlevGenerales.AdaptadorCompromisos;
import vlevGenerales.ToastGenerales;
import vlevGenerales.Compromisos;
import vlevGenerales.Constants;
import com.loopj.android.http.*;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CompromisosActivity extends Activity {
	ListView ListViewCompromiso;
	private AdaptadorCompromisos _m_adapter; 
	ArrayList<Compromisos> listaCompromisos;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compromisos);
		
		ListViewCompromiso = (ListView)findViewById(R.id.listViewCompromisos);
		registerForContextMenu(ListViewCompromiso);
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    
	    actualizar();
	}
	
	//Guardar los datos cuando se gire la pantalla
	@Override
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		outState.putParcelableArrayList("listCompromisos",listaCompromisos);
	}
	
	//Cargar datos al girar pantalla
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState){
		super.onRestoreInstanceState(savedInstanceState);
		if(savedInstanceState!=null){
			listaCompromisos =  savedInstanceState.getParcelableArrayList("listCompromisos");
			_m_adapter = new AdaptadorCompromisos(CompromisosActivity.this, R.layout.listitem_compromisos, listaCompromisos);
			ListViewCompromiso.setAdapter(_m_adapter);
	    }
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mantenedores, menu);
		return true;
	}
	
	//	Agregar xml para menu contextual
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.opciones_listas, menu);
	}
	
	//	Opciones del menu principal
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case android.R.id.home:
				Intent intent = new Intent(this, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
	            return true;
			case R.id.action_logout_mantenedores:
				logout();
				return true;
			case R.id.action_nuevo:
				Intent intentNuevo = new Intent(this, CompromisosNuevo.class);
				startActivity(intentNuevo);
				return true;
			case R.id.action_refrescar:
				actualizar();
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}
	
	//	Opciones del menu contextual
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_eliminar:
				eliminar(item);
				return true;
			default:
			return super.onContextItemSelected(item);
		}
	}
	
	//	Actualizar lista de Compromisos
	public void actualizar(){
		AsyncHttpClient client = new AsyncHttpClient();
		PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
		client.setCookieStore(myCookieStore);
		RequestParams rp = new RequestParams();
		
		client.post(Constants.URL_SERVER+"Compromisos/ListAndroid",rp, new JsonHttpResponseHandler(){
			 @Override
			 public void onSuccess(JSONObject jObject){    
				 try {
					listaCompromisos = new ArrayList<Compromisos>();
					
					JSONArray jsonArray = jObject.getJSONArray("compromisos");
					
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						Compromisos com = new Compromisos(jsonObject.getInt("idcompromisos"), jsonObject.getString("titulo"), jsonObject.getString("cuerpo"), jsonObject.getString("fecha"));
						listaCompromisos.add(com);
					}
					
					_m_adapter = new AdaptadorCompromisos(CompromisosActivity.this, R.layout.listitem_compromisos, listaCompromisos);
					ListViewCompromiso.setAdapter(_m_adapter);
					
				} catch (JSONException ex) {
					ToastGenerales.mensaje(getApplicationContext(), "problemas en la conexion", 1);
				}
			     
			 }   
			 @Override
			 public void onFailure(Throwable arg0){
				 ToastGenerales.mensaje(getApplicationContext(), "problemas en la conexion", 1);
			 }
		}); 
		
	}

	//	Eliminar Compromiso
	public void eliminar(MenuItem item){
		try{

			final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
			final AdaptadorCompromisos comAdap = (AdaptadorCompromisos) ListViewCompromiso.getAdapter();
			
			final Compromisos com = comAdap.getItem(info.position);	
			
			AsyncHttpClient client = new AsyncHttpClient();
			PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
			client.setCookieStore(myCookieStore);
			
			client.post(Constants.URL_SERVER+"compromisos/deleteAndroid/"+Integer.toString(com.get_ID()),null, new JsonHttpResponseHandler(){
				 @Override
				 public void onSuccess(JSONObject jObject){    
					 try {
						boolean result = jObject.getBoolean("response");
						if (result){
							listaCompromisos.remove(info.position);
							//comAdap.remove(com);
							comAdap.notifyDataSetChanged();
							ToastGenerales.mensaje(getApplicationContext(), "compromiso eliminado", 1);
				    	}else{
				    		ToastGenerales.mensaje(getApplicationContext(), "problemas en la conexion", 1);
				    	}
						
					} catch (JSONException ex) {
						ToastGenerales.mensaje(getApplicationContext(), "problemas en la conexion", 1);
					}
				     
				 }   
				 @Override
				 public void onFailure(Throwable arg0){
					 ToastGenerales.mensaje(getApplicationContext(), "problemas en la conexion", 1);
				 }
				 
				 @Override
		            public void onFinish() {
					 
		            }
			}); 
			
		}catch(Exception ex){
			Log.e("ERROR-RESCATE", ex.toString());
		}
	}
	
	//	LOGOUT
	public void logout(){
		PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
		myCookieStore.clear();
		finish();
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}
	
}
