package vlev.ceuch;

import org.json.JSONException;
import org.json.JSONObject;

import vlevGenerales.Constants;
import vlevGenerales.ToastGenerales;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class CompromisosNuevo extends Activity {

	private EditText txtTitulo;
	private EditText txtFecha;
	private EditText txtCuerpo;
	private ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compromisos_nuevo);
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    
	    txtTitulo = (EditText) findViewById(R.id.txt_titulo_compromiso);
	    txtFecha  = (EditText) findViewById(R.id.txt_fecha_compromiso);
	    txtCuerpo = (EditText) findViewById(R.id.txt_cuerpo_compromiso);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nuevo, menu);
		return true;
	}

//	Opciones del menu principal
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case android.R.id.home:
				Intent intent = new Intent(this, CompromisosActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
	            return true;
			case R.id.action_logout_nuevo:
				logout();
				return true;
			case R.id.action_ingresar:
				ingresar();
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}
	
	public void ingresar(){
		
		pd = new ProgressDialog(CompromisosNuevo.this);
		pd.setTitle("Procesando...");
		pd.setMessage("Por favor espere.");
		pd.setCancelable(false);
		pd.setIndeterminate(true);
		pd.show();
		
		txtTitulo.setEnabled(false);
		txtCuerpo.setEnabled(false);
		txtFecha.setEnabled(false);
		
		AsyncHttpClient client = new AsyncHttpClient();
		PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
		client.setCookieStore(myCookieStore);
		
		RequestParams rp = new RequestParams();
		rp.put("Compromiso_titulo", txtTitulo.getText().toString());
		rp.put("Compromiso_cuerpo", txtCuerpo.getText().toString());
		rp.put("Comrpomiso_fecha", txtFecha.getText().toString());
		
		client.post(Constants.URL_SERVER+"compromisos/createAndroid",rp, new JsonHttpResponseHandler(){
						
			@Override
			 public void onSuccess(JSONObject jObject){    
				 try {
					boolean result = jObject.getBoolean("response");
					if (result){
						Intent intent = new Intent(CompromisosNuevo.this, CompromisosActivity.class);
						finish();
						startActivity(intent);
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
				 if (pd!=null)
						pd.dismiss();

		     }
		}); 
		
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
