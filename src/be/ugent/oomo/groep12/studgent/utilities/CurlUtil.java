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
}
