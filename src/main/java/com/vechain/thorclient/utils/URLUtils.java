package com.vechain.thorclient.utils;

import io.mikael.urlbuilder.UrlBuilder;

import java.io.*;
import java.net.*;
import java.util.HashMap;

public class URLUtils {

	public static String get(String parameters, String mediaType, String encoding, String url) throws IOException {
		String result = null;
		HttpURLConnection uc = null;

		URL u = new URL(url);
		uc = (HttpURLConnection) u.openConnection();
		uc.setDoOutput(true);
		uc.setRequestMethod("GET");
		mediaType = mediaType == null ? "application/json;charset=utf-8" : mediaType;
		uc.addRequestProperty("accept", mediaType);
		if (null != parameters && !parameters.trim().isEmpty()) {
		    OutputStream out = uc.getOutputStream();
		    out.write(parameters.getBytes(encoding));
		    out.flush();
		    out.close();
		}
		for (int i = 0;; i++) {
		    String header = uc.getHeaderField(i);
		    if (header == null)
		        break;
		}
		InputStream buffer = new BufferedInputStream(uc.getInputStream());
		Reader reader = new InputStreamReader(buffer);
		StringBuffer context = new StringBuffer();
		int line;
		while ((line = reader.read()) != -1) {
		    context.append((char) line);
		}
		reader.close();
		buffer.close();
		result = context.toString();

		return result;
	}

	public static String post(String params, String mediaType, String encoding, String url) throws IOException {
		return doSend(params, mediaType, encoding, url, "POST");
	}

	public static String put(String params, String mediaType, String encoding, String url) throws IOException {
		return doSend(params, mediaType, encoding, url, "PUT");
	}

	public static String delete(String mediaType, String encoding, String url) throws IOException {
		String result = null;
		HttpURLConnection uc = null;

		URL u = new URL(url);
		uc = (HttpURLConnection) u.openConnection();
		uc.setRequestMethod("DELETE");
		if (null != mediaType) {
		    uc.addRequestProperty("Content-Type", mediaType);
		}
		uc.addRequestProperty("accept", "*");
		uc.connect();
		uc.disconnect();
		InputStream buffer = new BufferedInputStream(uc.getInputStream());
		Reader reader = new InputStreamReader(buffer);
		StringBuffer context = new StringBuffer();
		int line;
		while ((line = reader.read()) != -1) {
		    context.append((char) line);
		}
		reader.close();
		buffer.close();
		result = context.toString();
		return result;
	}

	private static String doSend(String params, String mediaType, String encoding, String url, String method) throws IOException {
		String result = null;
		HttpURLConnection uc = null;

		URL u = new URL(url);
		uc = (HttpURLConnection) u.openConnection();
		uc.setDoOutput(true);
		uc.setRequestMethod(method);
		if (null != mediaType) {
		    uc.addRequestProperty("Content-Type", mediaType);
		}
		uc.addRequestProperty("accept", "*");
		uc.connect();
		OutputStream out = uc.getOutputStream();
		out.write(params.getBytes(encoding));
		out.flush();

		InputStream buffer = new BufferedInputStream(uc.getInputStream());
		Reader reader = new InputStreamReader(buffer);
		StringBuffer context = new StringBuffer();
		int line;
		while ((line = reader.read()) != -1) {
		    context.append((char) line);
		}
		reader.close();
		buffer.close();
		result = context.toString();

		return result;
	}


	public static boolean download(String uri, String folder, String filename) throws IOException {

	    URL $url = new URL(uri);
	    HttpURLConnection connection = (HttpURLConnection) $url.openConnection();
	    DataInputStream in = new DataInputStream(connection.getInputStream());
	    DataOutputStream out = new DataOutputStream(new FileOutputStream(folder + filename));
	    byte[] buffer = new byte[1024];
	    int line = 0;
	    while ((line = in.read(buffer)) > 0) {
	        out.write(buffer, 0, line);
	    }
	    out.close();
	    in.close();
	    connection.disconnect();
	    return true;
	}




	public static String urlComposite(String urlString, HashMap<String, String> pathMap, HashMap<String, String> queryMap)  {
		String compositeURL = String.valueOf( urlString );
		if(pathMap != null && pathMap.size() > 0){
			for(String key: pathMap.keySet()){
				String pathValue = pathMap.get( key );
				//String encodedPathValue = URLEncoder.encode( pathValue, "utf-8");
				compositeURL = compositeURL.replace( "{" + key + "}", pathValue );
			}
		}
		String queryComposite = "";
		if(queryMap != null && queryMap.size() > 0){
			final int querySize = queryMap.size();
			int index = 0;
			for(String key: queryMap.keySet()){

				String queryValue = queryMap.get( key );
				String queryString = key + "=" + queryValue;
				index ++;
				if(index < querySize){
					queryString += "&";
				}
				queryComposite += queryString;
			}
		}
		if(!StringUtils.isBlank( queryComposite )){
			compositeURL += "?" + queryComposite;
		}
		return UrlBuilder.fromString( compositeURL ).toString();
	}
}
