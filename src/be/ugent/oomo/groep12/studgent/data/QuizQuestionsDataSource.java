package be.ugent.oomo.groep12.studgent.data;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

import android.util.Log;
import be.ugent.oomo.groep12.studgent.common.IData;
import be.ugent.oomo.groep12.studgent.common.IPointOfInterest;
import be.ugent.oomo.groep12.studgent.common.PointOfInterest;
import be.ugent.oomo.groep12.studgent.common.QuizQuestion;
import be.ugent.oomo.groep12.studgent.exception.CurlException;
import be.ugent.oomo.groep12.studgent.utilities.CurlUtil;
import be.ugent.oomo.groep12.studgent.utilities.JSONUtil;

public class QuizQuestionsDataSource implements IDataSource {

	private static QuizQuestionsDataSource instance = null;
	protected static Map<Integer, QuizQuestion> items;
	
	protected QuizQuestionsDataSource() {
      // Exists only to defeat instantiation.
	}
	
	public static QuizQuestionsDataSource getInstance() {
		if (instance == null) {
			instance = new QuizQuestionsDataSource();
		}
		return instance;
	}

	
	
	@Override
	public Map<Integer, QuizQuestion> getLastItems() {
		if (items == null ) {
			populateList();
		}
		return items;
	}
	
	private void populateList(){
		items = new HashMap<Integer,QuizQuestion>();
		items.put(0, new QuizQuestion(1, 10, "hoeveel is 10x10", false, null, "100", null, null));
		ArrayList<String> solutions = new ArrayList<String>();
		solutions.add("1");solutions.add("2");solutions.add("3");		
		items.put(0, new QuizQuestion(1, 10, "hoeveel is 1+1", false, solutions, "2", null, new LatLng(51.03431, 3.701)));
		items.put(0, new QuizQuestion(1, 10, "Dummy opgeloste vraag", true, null, "2", null, new LatLng(51.03431, 3.701)));
		items.put(0, new QuizQuestion(1, 10, "Dummy vraag mis", false, null, "2", Calendar.getInstance() , new LatLng(51.03431, 3.701)));
	}

	@Override
	public IData getDetails(int id) {
		// TODO Auto-generated method stub
		return null;
	}
}
