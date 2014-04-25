package be.ugent.oomo.groep12.studgent.data;

import java.util.HashMap;
import java.util.Map;

import be.ugent.oomo.groep12.studgent.common.Friend;
import be.ugent.oomo.groep12.studgent.common.Gender;
import be.ugent.oomo.groep12.studgent.common.IData;
import be.ugent.oomo.groep12.studgent.common.Trophie;

public class TrophieListDataSource implements IDataSource {
	
	private static TrophieListDataSource instance = null;
	protected static Map<Integer, Trophie> items;
	
	protected TrophieListDataSource() {
      // Exists only to defeat instantiation.
	}
	
	public static TrophieListDataSource getInstance() {
		if (instance == null) {
			instance = new TrophieListDataSource();
		}
		return instance;
	}
	
	protected void populateList(){
		// populate the list
		items = new HashMap<Integer,Trophie>();
		//nog doen
		items.put(1, new Trophie(1,"10x checkin",100));
		items.put(1, new Trophie(2,"nuchter",10));
		items.put(1, new Trophie(3,"quizzz master",100));
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

}