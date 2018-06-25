package com.vechain.thorclient.utils;

import com.vechain.thorclient.core.model.blockchain.NodeProvider;
import com.vechain.thorclient.core.model.exception.ClientIOException;
import io.mikael.urlbuilder.UrlBuilder;

import java.io.*;
import java.net.*;
import java.util.HashMap;

public class URLUtils {

	public static String get(String parameters, String mediaType, String encoding, String url) throws ClientIOException {
		String result = null;
		HttpURLConnection uc = null;
        OutputStream out = null;
        Reader reader = null;
        BufferedInputStream buffer = null;
        try {
            URL u = new URL( url );
            uc = (HttpURLConnection) u.openConnection();
            uc.setDoOutput( true );
            uc.setConnectTimeout( NodeProvider.getNodeProvider().getTimeout() );
            uc.setRequestMethod( "GET" );
            mediaType = mediaType == null ? "application/json;charset=utf-8" : mediaType;
            uc.addRequestProperty( "accept", mediaType );
            if (null != parameters && !parameters.trim().isEmpty()) {
                out = uc.getOutputStream();
                out.write( parameters.getBytes( encoding ) );
                out.flush();
                out.close();
                out = null;
            }

            buffer = new BufferedInputStream( uc.getInputStream() );
            reader = new InputStreamReader( buffer );
            StringBuilder context = new StringBuilder();
            int line;
            while ((line = reader.read()) != -1) {
                context.append( (char) line );
            }

            result = context.toString();
        }catch (IOException e){
            throw ClientIOException.create( e );
        }finally {
            if(uc != null){
                uc.disconnect();
            }

            if(out != null){
                try{
                    out.close();
                }catch(IOException e){
                    throw ClientIOException.create( e );
                }
            }
            if(buffer != null){
                try {
                    buffer.close();
                } catch (IOException e) {
                    throw ClientIOException.create( e );
                }
            }
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw ClientIOException.create( e );
                }
            }


        }
		return result;
	}

	public static String post(String params, String mediaType, String encoding, String url) throws ClientIOException {
		return doSend(params, mediaType, encoding, url, "POST");
	}

	private static String doSend(String params, String mediaType, String encoding, String url, String method) throws ClientIOException {
		String result = null;
		HttpURLConnection uc = null;
        OutputStream out = null;
        Reader reader = null;
        BufferedInputStream buffer = null;
        try {
            URL u = new URL( url );
            uc = (HttpURLConnection) u.openConnection();
            uc.setDoOutput( true );
            uc.setConnectTimeout( NodeProvider.getNodeProvider().getTimeout() );
            uc.setRequestMethod( method );
            if (null != mediaType) {
                uc.addRequestProperty( "Content-Type", mediaType );
            }
            uc.addRequestProperty( "accept", "*" );
            uc.connect();
            out = uc.getOutputStream();
            out.write( params.getBytes( encoding ) );
            out.flush();
            out.close();
            out = null;
            buffer = new BufferedInputStream( uc.getInputStream() );
            reader = new InputStreamReader( buffer );
            StringBuffer context = new StringBuffer();
            int line;
            while ((line = reader.read()) != -1) {
                context.append( (char) line );
            }
            reader.close();
            buffer.close();
        }catch(IOException e){
            throw ClientIOException.create( e );
        }finally {
            if(uc != null){
                uc.disconnect();
            }
            if(out != null){
                try{
                    out.close();
                }catch(IOException e){
                    throw ClientIOException.create( e );
                }
            }
            if(buffer != null){
                try {
                    buffer.close();
                } catch (IOException e) {
                    throw ClientIOException.create( e );
                }
            }
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw ClientIOException.create( e );
                }
            }
        }


		return result;
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
