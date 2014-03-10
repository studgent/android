package be.ugent.oomo.groep12.studgent.data;

import java.util.Map;

import be.ugent.oomo.groep12.studgent.common.IData;

public interface IDataSource {
	
	public Map<Integer,? extends IData> getLastItems();
	
	public IData getDetails(int id);

}
