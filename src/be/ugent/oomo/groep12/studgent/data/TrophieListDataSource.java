package be.ugent.oomo.groep12.studgent.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;
import be.ugent.oomo.groep12.studgent.common.Friend;
import be.ugent.oomo.groep12.studgent.common.Gender;
import be.ugent.oomo.groep12.studgent.common.IData;
import be.ugent.oomo.groep12.studgent.common.Trophie;
import be.ugent.oomo.groep12.studgent.exception.CurlException;
import be.ugent.oomo.groep12.studgent.utilities.CurlUtil;
import be.ugent.oomo.groep12.studgent.utilities.LoginUtility;

public class TrophieListDataSource implements IDataSource {
	
	private static TrophieListDataSource instance = null;
	protected static Map<Integer, Trophie> items;
	private int userID;
	
	private TrophieListDataSource() {
      // Exists only to defeat instantiation.
	}
	
	public static TrophieListDataSource getInstance() {
		if (instance == null) {
			instance = new TrophieListDataSource();
		}
		return instance;
	}
	
	public void setUserID(int userID){
		if(this.userID!=userID){
			this.userID = userID;
			this.delete();
		}
	}
	
	@SuppressLint("UseSparseArrays")
	protected void populateList(){
		// populate the list
		items = new HashMap<Integer,Trophie>();
		//nog doen
		
		int teller = 0;
		String user_resource = "user/" + userID;
		Integer[] pointsValues= {5,20,30};
		String[] checkinTrofieNames= {"1e Minister","vice-President", "President"};
		String[] quizTrofieNames= {"quizz Bachelor","quizz Master", "quizz King"};
		try {
			//all checkin trophies
			String apidata =  CurlUtil.get(user_resource);
			JSONObject  response = new JSONObject(apidata);
			JSONArray checkins = new JSONArray(response.getString("checkins"));
			Map<Integer,Integer> frequentyTable  = new HashMap<Integer,Integer>();
			for (int i = 0; i < checkins.length(); i++) {
				JSONObject checkin = checkins.getJSONObject(i);
				int poi_id = checkin.getInt("poi_id");
				Integer freq = frequentyTable.get(poi_id);
				frequentyTable.put(poi_id, (freq == null) ? 1 : freq + 1);
			}
			System.out.println("frequentietable: ----------\n"+frequentyTable.toString());
			Iterator it = frequentyTable.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pairs = (Map.Entry)it.next();
		        int poi_id = Integer.parseInt(pairs.getKey().toString());
		        int freq = Integer.parseInt(pairs.getValue().toString());
		        int rank = 2;
		        while(rank>0 && freq<pointsValues[rank]){
		        	rank--;
		        }
		        if( rank!=-1){
		    		String poi_name_resource = "poi/id/" + poi_id;
		        	String poi_data =  CurlUtil.get(poi_name_resource);
		        	JSONObject poiResponse = new JSONObject(poi_data);
		        	String POIName = poiResponse.getString("name");
		        	items.put(teller, new Trophie(poi_id, checkinTrofieNames[rank]+" of the "+POIName+"\n"+pointsValues[rank]+"X ingechecked" , pointsValues[rank]));
		        	teller++;
		        }
		        it.remove(); // avoids a ConcurrentModificationException
		    }
		    
		    //all quiz trofies
			int quizCount = getQuizCount(this.userID);
			int rank = 2;
	        while(rank>0 && quizCount<pointsValues[rank]){
	        	rank--;
	        }
	        if( rank!=-1){
	        	items.put(teller, new Trophie(0, ""+quizTrofieNames[rank]+"\n"+pointsValues[rank]+" vragen goed beantwoord", pointsValues[rank]));
	        	//hierboven is er een error mogelijk met de trofie ID!!!!!!!!!!!!!!!!!
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
	
	public Map<Integer, Trophie> getLastItems() {
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
	
	public static int getQuizCount(int userID){
		int quiznumber = 0;
		try{
			String user_resource = "user/" + userID;
			String apidata =  CurlUtil.get(user_resource);
			JSONObject  response = new JSONObject(apidata);
			JSONArray quizQuestions = new JSONArray(response.getString("answers"));
			quiznumber = quizQuestions.length();
		}catch (CurlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return quiznumber;
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
	
}