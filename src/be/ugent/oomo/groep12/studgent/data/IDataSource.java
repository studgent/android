package be.ugent.oomo.groep12.studgent.data;

import java.util.Map;

import be.ugent.oomo.groep12.studgent.common.IData;
import be.ugent.oomo.groep12.studgent.exception.DataSourceException;

public interface IDataSource {
	
	public Map<Integer,? extends IData> getLastItems() throws DataSourceException;
	
	public IData getDetails(int id);

}
