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
		
		// get cache.
		File dir = App.getContext().getCacheDir();
		String cache_file = resource.replace('/', '-');
		File file = new File(dir, cache_file);
		
		boolean cache_valid = false; // not really required yet.
	
		try {
			Date file_mod_date = new Date(file.lastModified());
			
			Date date_expiry = new Date(System.currentTimeMillis() - R.integer.api_cache_time);
			if (file_mod_date.before(date_expiry) ) {
				cache_valid = false;
			} else {
	        	Log.i("reading file", file.getAbsolutePath());
				// read the cached file
	        	FileInputStream fis = new FileInputStream(file);
				StringBuffer out_buffer = new StringBuffer();

				byte[] buffer = new byte[1024];

				while (fis.read(buffer) != -1) {
					out_buffer.append(new String(buffer));
				}
				fis.close();
				String out = out_buffer.toString();
				cache_valid = true;
				return out;
			}
			
		} catch (FileNotFoundException e) {
			cache_valid = false;
			e.printStackTrace();
		} catch (IOException e) {
			cache_valid = false;
			e.printStackTrace();
		};
		
		// if we get here, the cache wasn't valid anymore or something went seriously wrong reading the cache.
		// fetch content again.
		
		
		try {
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpResponse response = httpclient.execute(new HttpGet(target));
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
		        // file cached
				
		        return out;
		    } else{
		        //Closes the connection.
		        response.getEntity().getContent().close();
		        throw new CurlException(statusLine.getReasonPhrase());
		    }

		} catch (FileNotFoundException e) {
			Log.e("FileNotFound",e.getLocalizedMessage());
			throw new CurlException(e);
		} catch (IOException e) {
			Log.e("IOException",e.getLocalizedMessage());
			throw new CurlException(e);
		} catch (Exception e) {
			Log.e("Exception",e.getLocalizedMessage());
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
