package be.ugent.oomo.groep12.studgent.utilities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.exception.CurlException;
import android.content.res.Resources;

public class CurlUtil {
	
	/**
	 * Executes a GET request to the resource specified in the parameter.
	 * @param resource from the backend api
	 * @return String with list of json items
	 * @throws CurlException
	 */
	public static String get(String resource) throws CurlException {
		String api = App.getContext().getResources().getString(R.string.studgent_api);
		String target = api + '/' + resource;
		try {
			
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpResponse response = httpclient.execute(new HttpGet(target));
		    StatusLine statusLine = response.getStatusLine();
		    if(statusLine.getStatusCode() == HttpStatus.SC_OK){
		        ByteArrayOutputStream out = new ByteArrayOutputStream();
		        response.getEntity().writeTo(out);
		        out.close();
		        return out.toString();
		    } else{
		        //Closes the connection.
		        response.getEntity().getContent().close();
		        throw new CurlException(statusLine.getReasonPhrase());
		    }

		} catch (Exception e) {
			throw new CurlException(e);
		}
	}

	
	/**
	 * GET request to the backend api to retrieve one object, specified by id.
	 * @param resource of the backend api
	 * @param id of the requested object
	 * @return String with one json item
	 * @throws CurlException
	 */
	public static String get_by_id(String resource, int id) throws CurlException{
		return get(resource + "/id/" + id);
	}

	/**
	 * GET request to the backend api to retrieve objects, filtered by name specified in the parameter
	 * @param resource of the backend api
	 * @param name to filter items on
	 * @return String with filtered list with json items
	 * @throws CurlException
	 */
	public static String get_by_name(String resource, String name) throws CurlException{
		return get(resource + "/name/" + name);
	}
	
	/**	
	 * GET request to the backend api to retrieve objects per page
	 * @param resource of the backend api
	 * @param perpage amount of objects per page
	 * @param page offset, pagenumber
	 * @return String with page from list with json items
	 * @throws CurlException
	 */
	public static String get_paged(String resource, int perpage, int page) throws CurlException{
		return get(resource + "/page/" + perpage + '/' + page);
	}
}
