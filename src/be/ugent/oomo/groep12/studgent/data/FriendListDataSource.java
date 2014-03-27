package be.ugent.oomo.groep12.studgent.data;

import java.util.HashMap;
import java.util.Map;

import be.ugent.oomo.groep12.studgent.common.Friend;
import be.ugent.oomo.groep12.studgent.common.Gender;
import be.ugent.oomo.groep12.studgent.common.IData;

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
	
	protected void populateList(){
		// populate the list
		items = new HashMap<Integer,Friend>();
		//nog doen
		items.put(1, new Friend(1,"devreese","Floris",Gender.MALE ,"Reninge"));
	}
	
	public Map<Integer, Friend> getLastItems() {
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
