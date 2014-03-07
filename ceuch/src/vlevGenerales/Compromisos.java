package vlevGenerales;

import android.os.Parcel;
import android.os.Parcelable;

public class Compromisos implements Parcelable  {
	
	private int _ID;
	private String _title;
	private String _body;
	private String _date;
	
	public Compromisos(){
		super();
	}
	
	public Compromisos(int id, String title, String body, String date){
		super();
		this.set_ID(id);
		this.set_title(title);
		this.set_body(body);
		this.set_date(date);
	}

	public int get_ID() {
		return _ID;
	}

	public void set_ID(int _ID) {
		this._ID = _ID;
	}

	public String get_title() {
		return _title;
	}

	public void set_title(String _title) {
		this._title = _title;
	}

	public String get_body() {
		return _body;
	}

	public void set_body(String _body) {
		this._body = _body;
	}

	public String get_date() {
		return _date;
	}

	public void set_date(String _date) {
		_date = Constants.formateDateFromstring("yyyy-MM-dd", "dd-MM-yyyy", _date);
		this._date = _date;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] {
			String.valueOf(this._ID),
			this._title,
			this._body,
			this._date
		});
	}
	
	public Compromisos(Parcel in)
	{
		String[] data = new String[4];
		in.readStringArray(data);
		
		this.set_ID(Integer.parseInt(data[0]));
		this.set_title(data[1]);
		this.set_body(data[2]);
		this._date = data[3];
	}
	
	//More boilerplate
	//Failure to add this results in the following exception
	//"android.os.BadParcelableException: Parcelable protocol 
	//requires a Parcelable.Creator object called  CREATOR on class"
	 public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
	         public Compromisos createFromParcel(Parcel in) {
	        	 return new Compromisos(in); 
	         }
	         public Compromisos[] newArray(int size) {
	        	 return new Compromisos[size];
	         }
	};
	
	
	
}
