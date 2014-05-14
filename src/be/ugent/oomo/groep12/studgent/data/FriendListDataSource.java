package be.ugent.oomo.groep12.studgent.data;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.util.Log;
import be.ugent.oomo.groep12.studgent.common.Friend;
import be.ugent.oomo.groep12.studgent.common.IData;
import be.ugent.oomo.groep12.studgent.exception.CurlException;
import be.ugent.oomo.groep12.studgent.exception.DataSourceException;
import be.ugent.oomo.groep12.studgent.utilities.CurlUtil;
import be.ugent.oomo.groep12.studgent.utilities.JSONUtil;
import be.ugent.oomo.groep12.studgent.utilities.LoginUtility;

@SuppressLint("UseSparseArrays")
public class FriendListDataSource implements IDataSource {
	
	private static FriendListDataSource instance = null;
	protected static Map<Integer, Friend> items;
	
	protected FriendListDataSource() {
      // Exists only to defeat instantiation.
	}
	
	public static FriendListDataSource getInstance() {
		if (instance == null) {
			instance = new FriendListDataSource();
		}
		return instance;
	}
	
	
	public boolean follow(int friendID, boolean follow) throws DataSourceException {

		int userID = LoginUtility.getId();
		
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("token", LoginUtility.getToken() );
		boolean added = false;
		//update online	
		String followunfollow = follow ? "/follow/" : "/unfollow/";
		String resource = "user/" + 
						  userID + 
						  followunfollow + 
						  friendID;
		try {
			String apidata =  CurlUtil.post(resource, postData);
			JSONObject items = new JSONObject(apidata);
			String message = items.getString("message");
			if ( message.equals("added to following list") ){
				added = true;
			} else { // somethings wrong or user removed from list
				added = false;
			}
		} catch (CurlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return added;
	}
	
	protected void populateList(){
		int userID = LoginUtility.getId();
		// populate the list
		items = new HashMap<Integer,Friend>();

		try {
			Log.i("retrieving resource", "user/" + userID + "/following");
			String apidata =  CurlUtil.get("user/" + userID + "/following", true);
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
		return friend;
	}
	public Map<Integer, Friend> getLastItems() throws DataSourceException {
		int userID = LoginUtility.getId();
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

	
	public void delete(){
		items = null;
	}
}
