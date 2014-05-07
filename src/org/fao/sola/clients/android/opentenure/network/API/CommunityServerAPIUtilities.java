/**
 * ******************************************************************************************
 * Copyright (C) 2014 - Food and Agriculture Organization of the United Nations (FAO).
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice,this list
 *       of conditions and the following disclaimer.
 *    2. Redistributions in binary form must reproduce the above copyright notice,this list
 *       of conditions and the following disclaimer in the documentation and/or other
 *       materials provided with the distribution.
 *    3. Neither the name of FAO nor the names of its contributors may be used to endorse or
 *       promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,STRICT LIABILITY,OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * *********************************************************************************************
 */
package org.fao.sola.clients.android.opentenure.network.API;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

public class CommunityServerAPIUtilities {
	
	public static final String HTTP_LOGIN= "https://ot.flossola.org/ws/en-us/auth/login?username=%s&password=%s";	
	public static final String HTTPS_LOGIN= "http://ot.flossola.org/ws/en-us/auth/login?username=%s&password=%s";
	
	public static final String HTTPS_LOGOUT= "https://ot.flossola.org/ws/en-us/auth/logout";
	public static final String HTTP_LOGOUT= "http://ot.flossola.org/ws/en-us/auth/logout";
	
	public static final String HTTPS_GETCLAIM = "https://ot.flossola.org/ws/claim/en-us/getclaim/%s";
	public static final String HTTP_GETCLAIM = "http://ot.flossola.org/ws/claim/en-us/getclaim/%s";
	
	public static final String HTTPS_SAVECLAIM = "https://ot.flossola.org/ws/en-us/claim/saveClaim";
	public static final String HTTP_SAVECLAIM = "http://ot.flossola.org/ws/en-us/claim/saveClaim";
	
	public static final String HTTPS_SAVEATTACHMENT = "https://ot.flossola.org/ws/en-us/claim/saveAttachment";
	public static final String HTTP_SAVEATTACHMENT = "http://ot.flossola.org/ws/en-us/claim/saveAttachment";
	
	public static final String HTTPS_UPLOADCHUNK = "https://ot.flossola.org/ws/en-us/claim/uploadChunk";
	public static final String HTTP_UPLOADCHUNK = "http://ot.flossola.org/ws/en-us/claim/uploadChunk";
	
	
	
	
public static String Slurp(final InputStream is, final int bufferSize)
	{
	  final char[] buffer = new char[bufferSize];
	  final StringBuilder out = new StringBuilder();
	  try {
	    final Reader in = new InputStreamReader(is, "UTF-8");
	    try {
	      for (;;) {
	        int rsz = in.read(buffer, 0, buffer.length);
	        if (rsz < 0)
	          break;
	        out.append(buffer, 0, rsz);
	      }
	    }
	    finally {
	      in.close();
	    }
	  }
	  catch (UnsupportedEncodingException ex) {
	    /* ... */
	  }
	  catch (IOException ex) {
	      /* ... */
	  }
	  return out.toString();
}

	
public class Login{
	private int status;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}


	
}

}




