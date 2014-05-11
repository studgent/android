package be.ugent.oomo.groep12.studgent.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

import android.annotation.SuppressLint;
import android.app.DownloadManager.Query;
import android.util.Log;
import be.ugent.oomo.groep12.studgent.activity.LoginActivity;
import be.ugent.oomo.groep12.studgent.common.CalendarEvent;
import be.ugent.oomo.groep12.studgent.common.ICalendarEvent;
import be.ugent.oomo.groep12.studgent.common.IData;
import be.ugent.oomo.groep12.studgent.common.IPointOfInterest;
import be.ugent.oomo.groep12.studgent.common.IQuizQuestion;
import be.ugent.oomo.groep12.studgent.common.PointOfInterest;
import be.ugent.oomo.groep12.studgent.common.QuizQuestion;
import be.ugent.oomo.groep12.studgent.exception.CurlException;
import be.ugent.oomo.groep12.studgent.exception.DataSourceException;
import be.ugent.oomo.groep12.studgent.utilities.CurlUtil;
import be.ugent.oomo.groep12.studgent.utilities.JSONUtil;
import be.ugent.oomo.groep12.studgent.utilities.LoginUtility;

@SuppressLint("UseSparseArrays")
public class QuizQuestionsDataSource implements IDataSource {

	private static QuizQuestionsDataSource instance = null;
	protected static Map<Integer, QuizQuestion> items;
	protected int userID;
	
	protected QuizQuestionsDataSource() {
      // Exists only to defeat instantiation.
	}
	
	public static QuizQuestionsDataSource getInstance() {
		if (instance == null) {
			instance = new QuizQuestionsDataSource();
		}
		return instance;
	}
	
	public boolean checkAnswer(QuizQuestion question, String givenanswer ) throws DataSourceException {
		
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("token", LoginUtility.getInstance().getToken() );
		postData.put("answer", givenanswer );
		
		
		try {
			String apidata =  CurlUtil.post("user/" + userID + "/questions/" + question.getId(), postData);
				
			return true;
		
		} catch (CurlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
		
		
		/*question.setLastTry(Calendar.getInstance());
		givenanswer = givenanswer.toLowerCase().trim().replace(" ","");
		if (question.getAnswer().equalsIgnoreCase(givenanswer)){
			question.setSolved(true);
			return true;
		}else{
			return false;
		}*/
	}

	
	@Override
	public Map<Integer, QuizQuestion> getLastItems() throws DataSourceException {
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

	

	private void populateList(){
		items = new HashMap<Integer,QuizQuestion>();
		/*items.put(0, new QuizQuestion(1, 10, "Wanneer is de bouw van de boekentoren gestart?", false, null, "1942", new GregorianCalendar(2013,1,28,13,24,56), new LatLng(51.044935, 3.725798)));
		
		ArrayList<String> solutions = new ArrayList<String>();
		solutions.add("Apen");solutions.add("Bananen");solutions.add("Lichten");solutions.add("Schoolboeken");
		
		items.put(1, new QuizQuestion(1, 2, "Wat vind je niet terug aan het plafond van café de pi-nuts? ", false, solutions, "Schoolboeken", new GregorianCalendar(2013,1,28,13,24,56), new LatLng(51.0436016,3.7210573)));
		items.put(2, new QuizQuestion(1, 50, "Dummy opgeloste vraag.", true, null, "2", new GregorianCalendar(2013,1,28,13,24,56) , new LatLng(51.03431, 3.701)));
		items.put(3, new QuizQuestion(1, 1, "Dummy vraag mis?", false, null, "2", Calendar.getInstance() , new LatLng(51.03431, 3.701)));
		*/
		
		try {
			Log.i("retrieving resource", "user/" + userID + "/questions/all");
			String apidata =  CurlUtil.get("user/" + userID + "/questions/all");
			JSONArray question_items = new JSONArray(apidata);
			for (int i = 0; i < question_items.length(); i++) {
				JSONObject item = question_items.getJSONObject(i);
				QuizQuestion question = parseQuestion(item);
				items.put(question.getId(), question);
			}
			Log.i("items", ""+items.size());
		} catch (CurlException e) {
			Log.e("error retrieving QuizQuestions", e.getLocalizedMessage());
		} catch (JSONException e){
			Log.e("error parsing Quiz json", e.getLocalizedMessage());
		} catch (ParseException e){
			Log.e("error parsing date", e.getLocalizedMessage());
		}
	}
	

	private QuizQuestion parseQuestion(JSONObject item) throws JSONException, ParseException {
		QuizQuestion quizquestion;
		int id = item.optInt("id",0);
		int points = item.optInt("points");
		String type = JSONUtil.optString(item, "type"),
			   question = JSONUtil.optString(item, "question"),
			   answer = JSONUtil.optString(item, "answer");
		boolean answered = item.optBoolean("answered");
		
		Double latitude = item.optDouble("latitude", 0.0),
			   longitude = item.optDouble("longitude", 0.0);
		LatLng location = new LatLng(latitude,longitude);
		Date date_answered = null;
		GregorianCalendar date = null;
		boolean correct = false;
		if ( answered ) {
			JSONObject last =  item.getJSONObject("last_answer");
			date_answered = item.optString("date_from").equals("null") ? 
								null :
								new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(last.optString("date"));
			date = new GregorianCalendar();
			date.setTime(date_answered);
			correct = item.optBoolean("correct");
		}
		
		 
		JSONArray choices_json = item.getJSONArray("choices");
		ArrayList<String> choices = null;
		if ( choices_json.length() > 0 ) {
			choices = new ArrayList<String>();
			for (int i = 0; i < choices_json.length(); i++) {
				choices.add(choices_json.getJSONObject(i).getString("choice"));
			} 
		}

		quizquestion = new QuizQuestion(id, 
										points, 
										question, 
										correct, 
										choices, 
										answer, 
										date , 
										location);
		return quizquestion;
	}

	@Override
	public IData getDetails(int id) {
		// TODO Auto-generated method stub
		return null;
	}
}
