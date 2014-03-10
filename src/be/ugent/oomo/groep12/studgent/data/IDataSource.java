package be.ugent.oomo.groep12.studgent.data;

import java.util.List;

import be.ugent.oomo.groep12.studgent.common.IData;

public interface IDataSource {
	
	public List<? extends IData> getLastItems();

}
