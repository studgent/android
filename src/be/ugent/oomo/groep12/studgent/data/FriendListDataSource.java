package be.ugent.oomo.groep12.studgent.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

import android.util.Log;
import be.ugent.oomo.groep12.studgent.common.CalendarEvent;
import be.ugent.oomo.groep12.studgent.common.Friend;
import be.ugent.oomo.groep12.studgent.common.Gender;
import be.ugent.oomo.groep12.studgent.common.ICalendarEvent;
import be.ugent.oomo.groep12.studgent.common.IData;
import be.ugent.oomo.groep12.studgent.exception.CurlException;
import be.ugent.oomo.groep12.studgent.exception.DataSourceException;
import be.ugent.oomo.groep12.studgent.utilities.CurlUtil;
import be.ugent.oomo.groep12.studgent.utilities.JSONUtil;
import be.ugent.oomo.groep12.studgent.utilities.LoginUtility;

public class FriendListDataSource implements IDataSource {
	
	private static FriendListDataSource instance = null;
	protected static Map<Integer, Friend> items;
	protected int userID;
	
	protected FriendListDataSource() {
      // Exists only to defeat instantiation.
	}
	
	public static FriendListDataSource getInstance() {
		if (instance == null) {
			instance = new FriendListDataSource();
		}
		return instance;
	}
	
	protected void populateList(){
		// populate the list
		items = new HashMap<Integer,Friend>();

		try {
			Log.i("retrieving resource", "user/" + userID + "/following");
			String apidata =  CurlUtil.get("user/" + userID + "/following");
			JSONArray user_items = new JSONArray(apidata);
			for (int i = 0; i < user_items.length(); i++) {
				JSONObject item = user_items.getJSONObject(i);
				boolean following = item.getBoolean("following");
				Friend friend = parseFriend(item.getJSONObject("user"), following);
				items.put(friend.getId(), friend);
			}
			Log.i("items", ""+items.size());
		} catch (CurlException e) {
			Log.e("error retrieving calendar", e.getLocalizedMessage());
		} catch (JSONException e){
			Log.e("error parsing json", e.getLocalizedMessage());
		} catch (ParseException e){
			Log.e("error parsing date", e.getLocalizedMessage());
		}

	}
	
	protected Friend parseFriend(JSONObject item, boolean following) throws JSONException, ParseException{
		Friend friend;
		int id = item.optInt("id",0);
		int score = item.optInt("score",0);
		String email = JSONUtil.optString(item, "email"),
			   firstname = JSONUtil.optString(item, "first_name"),
			   lastname = JSONUtil.optString(item, "last_name"),
			   description = JSONUtil.optString(item, "details"),
			   phone = JSONUtil.optString(item, "phone");
		
		friend = new Friend(id, following, firstname, lastname, email, description, phone, score);
		Log.i("Creating user", friend.toString());
		return friend;
	}
	public Map<Integer, Friend> getLastItems() throws DataSourceException {
		userID = LoginUtility.getInstance().getId();
		if (userID == 0)
		{
			throw new DataSourceException("USER ID NOT SET");
		}
		if (items == null ) {
			populateList();
		}
		return items;
	}

	@Override
	public IData getDetails(int id) {
		return items.get(id);
	}

}
