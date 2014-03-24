package be.ugent.oomo.groep12.studgent.utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.exception.CurlException;
import android.util.Log;

/**
 * Utility class to fetch content from url
 * Provides basic caching method for speed improvements.
 * Has option to ignore the cache to force a refresh
 * 
 */
public class CurlUtil {
	
	
	/**
	 * Retrieves content  from cache and appends it to the outputparameter out_buffer
	 * @param out_buffer outputparameter of type StringBuffer for the content
	 * @param file cache file
	 * @return boolean false if something went wrong, true if cache loaded in out_buffer
	 */
	protected static boolean get_cache(StringBuffer out_buffer, File file) {
		try {
			// read the cached file
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			while (fis.read(buffer) != -1) {
				out_buffer.append(new String(buffer));
			}
			fis.close();
			return true; //  file content loaded in stringbuffer 
		} catch (FileNotFoundException e) {
			return false; // no cache file found, return false
		} catch (IOException e) {
			e.printStackTrace();
			return false; // problem reading cache file, return false
		}
	}

	/**
	 * Executes a GET request to the resource specified in the parameter.
	 * @param resource from the backend api
	 * @return String with list of json items
	 * @throws CurlException
	 */
	public static String get(String resource) throws CurlException {
		return get(resource,false);
	}
	
	/**
	 * Executes a GET request to the resource specified in the parameter.
	 * @param resource from the backend api
	 * @return String with list of json items
	 * @param ignore_cache true to force refresh, false to use cache if possible
	 * @throws CurlException
	 */
	public static String get(String resource, boolean ignore_cache) throws CurlException{
		String api = App.getContext().getResources().getString(R.string.studgent_api);
		String target = api + '/' + resource;
		return getRaw(target, ignore_cache);
	}
	
	
	public static String getRaw(String resource, boolean ignore_cache) throws CurlException {
		Log.i("retrieving", resource);
		// get cache.
		File dir = App.getContext().getCacheDir();
		String cache_file = resource.replace('/', '-')
									.replace(':', '-')
									.replace('?', '-')
									.replace('&', '-')
									.replace('=', '-')
									.replace('+', '-');
		File file = new File(dir, cache_file);

		Date file_mod_date = new Date(file.lastModified());
		
		Date date_expiry = new Date(System.currentTimeMillis() - R.integer.api_cache_time);
		StringBuffer cache_buffer = new StringBuffer();
		
		// check for cache first
		if (!ignore_cache && file_mod_date.after(date_expiry) && get_cache(cache_buffer, file) ) {
			Log.i("retrieved from cache file", file.getAbsolutePath());
			return cache_buffer.toString();
		} else {
			Log.i("no cache available", file.getAbsolutePath());
			try {
			    HttpClient httpclient = new DefaultHttpClient();
			    HttpResponse response = httpclient.execute(new HttpGet(resource));
			    StatusLine statusLine = response.getStatusLine();
			    
			    if(statusLine.getStatusCode() == HttpStatus.SC_OK){
			    	// get content
			        ByteArrayOutputStream out_buffer = new ByteArrayOutputStream();
			        response.getEntity().writeTo(out_buffer);
			        out_buffer.close();
			        String out = out_buffer.toString();
			        
			        // cache the file
			        FileOutputStream output = new FileOutputStream(file, false); //overwrite
			        output.write(out.getBytes());
			        output.close();
					
			        return out;
			    } else{
			        //Closes the connection.
			        response.getEntity().getContent().close();
			        throw new CurlException(statusLine.getReasonPhrase());
			    }
			} catch (FileNotFoundException e) {
				Log.e("FileNotFound",e.getMessage());
				throw new CurlException(e);
			} catch (IOException e) {
				Log.e("IOException",e.getMessage());
				throw new CurlException(e);
			} catch (Exception e) {
				Log.e("Exception",""+e.getMessage());
				throw new CurlException(e);
			}
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
		return get(resource + "/id/" + id, false);
	}
	
	/**
	 * GET request to the backend api to retrieve one object, specified by id.
	 * @param resource of the backend api
	 * @param id of the requested object
	 * @param ignore_cache true to force refresh, false to use cache if possible
	 * @return String with one json item
	 * @throws CurlException
	 */
	public static String get_by_id(String resource, int id, boolean ignore_cache) throws CurlException{
		return get(resource + "/id/" + id, ignore_cache);
	}

	/**
	 * GET request to the backend api to retrieve objects, filtered by name specified in the parameter
	 * @param resource of the backend api
	 * @param name to filter items on
	 * @return String with filtered list with json items
	 * @throws CurlException
	 */
	public static String get_by_name(String resource, String name) throws CurlException{
		return get(resource + "/name/" + name, false);
	}

	/**
	 * GET request to the backend api to retrieve objects, filtered by name specified in the parameter
	 * @param resource of the backend api
	 * @param name to filter items on
	 * @param ignore_cache true to force refresh, false to use cache if possible
	 * @return String with filtered list with json items
	 * @throws CurlException
	 */
	public static String get_by_name(String resource, String name, boolean ignore_cache) throws CurlException{
		return get(resource + "/name/" + name, ignore_cache);
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
		return get(resource + "/page/" + perpage + '/' + page, false);
	}
	
	/**	
	 * GET request to the backend api to retrieve objects per page
	 * @param resource of the backend api
	 * @param perpage amount of objects per page
	 * @param page offset, pagenumber
	 * @param ignore_cache true to force refresh, false to use cache if possible
	 * @return String with page from list with json items
	 * @throws CurlException
	 */
	public static String get_paged(String resource, int perpage, int page, boolean ignore_cache) throws CurlException{
		return get(resource + "/page/" + perpage + '/' + page, ignore_cache);
	}
}
