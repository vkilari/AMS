package com.mobisolutions.ams.http;

/**
 * Created by vkilari on 12/18/17.
 */

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HeaderElement;
import cz.msebera.android.httpclient.ParseException;
import cz.msebera.android.httpclient.message.AbstractHttpMessage;


final public class HttpHelper {

    // TODO may not need to set all headers, most
    // might be implicit.
    public static final String[][] storeHeaders = {
            { "Accept-Language", "en" },
            { "Accept","text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5" },
            { "Connection", "keep-alive" }, { "Proxy-Connection", "keep-alive" } };
    public static final String[][] headersDefault = {
            { "Content-Type", "application/x-www-form-urlencoded; charset=utf-8" }
    };


    public static final String[][] jsonHeaders = {
            { "Content-Type", "application/json" }
    };


    public static final String HDR_USER_AGENT = "User-Agent";
    public static final String HDR_HOST = "Host";
//	private static final String TAG = HttpHelper.class.getSimpleName();


    public static void setHeaders(AbstractHttpMessage request, String headers[][], String[]... header) {
        for (String[] hdr : headers)
            request.setHeader(hdr[0], hdr[1]);
        for (String[] hdr : header)
            request.setHeader(hdr[0], hdr[1]);

        for (String[] headerDefault : headersDefault)
            request.setHeader(headerDefault[0], headerDefault[1]);
    }





    public static Header[] getDefalutHeaders() {
        Header[] headers= new Header[headersDefault.length] ;
        for (int i=0;i<headersDefault.length;i++)
        {
            final String[] stHeaderDefault=headersDefault[i];
            Header header=new Header() {

                @Override
                public String getValue() {
                    // TODO Auto-generated method stub
                    return stHeaderDefault[1];
                }

                @Override
                public String getName() {
                    // TODO Auto-generated method stub
                    return stHeaderDefault[0];
                }

                @Override
                public HeaderElement[] getElements() throws ParseException {
                    // TODO Auto-generated method stub
                    return null;
                }
            };
            headers[i]=header;
        }

        return headers;
    }


    public static String getContentType() {
        // TODO Auto-generated method stub
        return null;
    }

    public static final String[][] imageHeaders = {
            { "Accept", "application/json"},{"Content-type", "multipart/form-data; boundary=" }
    };

    public static Header[] getImagePostHeaders() {
        Header[] headers= new Header[imageHeaders.length] ;
        for (int i=0;i<imageHeaders.length;i++)
        {
            final String[] stHeaderDefault=imageHeaders[i];
            Header header=new Header() {

                @Override
                public String getValue() {
                    // TODO Auto-generated method stub
                    if(stHeaderDefault[1].contains("multipart"))
                    {
                        SimpleMultipartEntity simpleMultipartEntity= new SimpleMultipartEntity();
                        return stHeaderDefault[1]+simpleMultipartEntity.getBoundary();
                    }
                    return stHeaderDefault[1];
                }

                @Override
                public String getName() {
                    // TODO Auto-generated method stub
                    return stHeaderDefault[0];
                }

                @Override
                public HeaderElement[] getElements() throws ParseException {
                    // TODO Auto-generated method stub
                    return null;
                }
            };
            headers[i]=header;
        }

        return headers;
    }


}

