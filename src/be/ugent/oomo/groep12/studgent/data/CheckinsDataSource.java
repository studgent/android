package be.ugent.oomo.groep12.studgent.data;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;
import be.ugent.oomo.groep12.studgent.common.Checkin;
import be.ugent.oomo.groep12.studgent.common.IData;
import be.ugent.oomo.groep12.studgent.exception.CurlException;
import be.ugent.oomo.groep12.studgent.exception.DataSourceException;
import be.ugent.oomo.groep12.studgent.utilities.CurlUtil;

public class CheckinsDataSource  implements IDataSource {
	
	protected static Map<Integer, Checkin> items;
	private boolean checkinsOfUser;
	private int sourceID;
	
	public CheckinsDataSource(boolean checkinOfUser, int sourceID) {
      // Exists only to defeat instantiation.
		this.checkinsOfUser = checkinOfUser;
		this.sourceID = sourceID;
		if(checkinsOfUser)
			populateListByUserID();
		else
			populateListByPOIID();
	}
	
	
	
	public void setCheckinsOfUser( boolean checkinsOfUser){
		this.checkinsOfUser=checkinsOfUser;
	}
	
	@SuppressLint("UseSparseArrays")
	protected void populateListByUserID(){
		// populate the list
		items = new HashMap<Integer,Checkin>();
		
		String user_resource = "user/" + sourceID;
		try {
			//all checkin trophies
			String apidata =  CurlUtil.get(user_resource);
			JSONObject  response = new JSONObject(apidata);
			JSONArray checkins = new JSONArray(response.getString("checkins"));
			String user_name = response.getString("first_name")+" "+response.getString("last_name");
			for (int i = 0; i < checkins.length(); i++) {
				JSONObject checkin = checkins.getJSONObject(i);
				int id = checkin.getInt("id");
				int poi_id = checkin.getInt("poi_id");
				//get the name of the poi
				String poi_name = new JSONObject(CurlUtil.get("poi/id/" + poi_id)).getString("name");
				String message = checkin.getString("message");
				items.put(id,new Checkin(id,user_name,poi_name,message));
			}
			Log.i("items", ""+items.size());
		} catch (CurlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@SuppressLint("UseSparseArrays")
	protected void populateListByPOIID(){
		// populate the list
				items = new HashMap<Integer,Checkin>();
				
				String poi_resource = "poi/id/" + sourceID;
				try {
					//all checkin trophies
					String apidata =  CurlUtil.get(poi_resource);
					JSONObject  response = new JSONObject(apidata);
					JSONArray checkins = new JSONArray(response.getString("checkins"));
					String poi_name = response.getString("name");
					for (int i = 0; i < checkins.length(); i++) {
						JSONObject checkin = checkins.getJSONObject(i);
						int id = checkin.getInt("id");
						int user_id = checkin.getInt("user_id");
						String message = checkin.getString("message");
						JSONObject user_resource = new JSONObject(CurlUtil.get("user/" + user_id));
						String user_name = user_resource.getString("first_name")+" "+user_resource.getString("last_name");
						items.put(id,new Checkin(id,user_name,poi_name,message));
					}
					Log.i("items", ""+items.size());
				} catch (CurlException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}

	@Override
	public IData getDetails(int id) {
		return items.get(id);
	}

	
	public void delete(){
	}
	
	public static int getCheckinNumber(int userID){
		int checkinnumber = 0;
		try{
			String user_resource = "user/" + userID;
			String apidata =  CurlUtil.get(user_resource);
			JSONObject  response = new JSONObject(apidata);
			JSONArray checkins = new JSONArray(response.getString("checkins"));
			checkinnumber = checkins.length();
		}catch (CurlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return checkinnumber;
	}



	@Override
	public Map<Integer, Checkin> getLastItems()
			throws DataSourceException {
		// TODO Auto-generated method stub
		
		//hierin moet er gezorgt worden dat je checkinsOfUser en sourceID gezet wordt
		return items;
	}
	
}