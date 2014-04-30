package be.ugent.oomo.groep12.studgent.data;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
		items.put(0, new QuizQuestion(1, 10, "In welk jaar is de boekentoren open gegaan?", false, null, "1942", new GregorianCalendar(2013,1,28,13,24,56), new LatLng(51.044935, 3.725798)));
		ArrayList<String> solutions = new ArrayList<String>();
		solutions.add("Apen");solutions.add("Bananen");solutions.add("Lichten");solutions.add("Schoolboeken");			
		items.put(1, new QuizQuestion(1, 2, "Wat hangt er niet aan het plafond van café de pi-nuts?", false, solutions, "Schoolboeken", new GregorianCalendar(2013,1,28,13,24,56), new LatLng(51.0436016,3.7210573)));
		items.put(2, new QuizQuestion(1, 50, "Dummy opgeloste vraag", true, null, "2", new GregorianCalendar(2013,1,28,13,24,56) , new LatLng(51.03431, 3.701)));
		items.put(3, new QuizQuestion(1, 1, "Dummy vraag mis", false, null, "2", Calendar.getInstance() , new LatLng(51.03431, 3.701)));
	}

	@Override
	public IData getDetails(int id) {
		// TODO Auto-generated method stub
		return null;
	}
}
