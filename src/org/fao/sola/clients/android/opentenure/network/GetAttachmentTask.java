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
package org.fao.sola.clients.android.opentenure.network;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.fao.sola.clients.android.opentenure.filesystem.FileSystemUtilities;
import org.fao.sola.clients.android.opentenure.filesystem.json.JsonUtilities;
import org.fao.sola.clients.android.opentenure.model.Attachment;
import org.fao.sola.clients.android.opentenure.model.AttachmentStatus;
import org.fao.sola.clients.android.opentenure.model.MD5;
import org.fao.sola.clients.android.opentenure.network.API.CommunityServerAPI;
import org.fao.sola.clients.android.opentenure.network.response.GetAttachmentResponse;

import android.os.AsyncTask;
import android.util.Log;

public class GetAttachmentTask extends AsyncTask<String, Void, Boolean> {

	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub

		/*
		 * Check if the file already exists
		 */
		GetAttachmentResponse res = null;
		int lenght = 100000; /* Should be setted by property */
		long offSet = 0;

		Attachment att = Attachment.getAttachment(params[1]);

		File file = new File(
				FileSystemUtilities.getAttachmentFolder(params[0]),
				att.getFileName());

		try {

			if (file.exists()) {
				offSet = file.length();
			} else
				file.createNewFile();

			/* Here I need a cycle */
			boolean cycle = true;

			while (file.length() < att.getSize()) {

				if ((att.getSize() - file.length()) < lenght)
					lenght = (int) (att.getSize() - file.length());

				res = CommunityServerAPI.getAttachment(att.getAttachmentId(),
						offSet, lenght + offSet - 1);

				if (res.getHttpStatusCode() == HttpStatus.SC_OK) {

					FileOutputStream fos = new FileOutputStream(file);
					fos.write(res.getArray());
					fos.close();

					cycle = false;

				} else if (res.getHttpStatusCode() == HttpStatus.SC_PARTIAL_CONTENT) {

					Log.d("CommunityServerAPI",
							"ATTACHMENT RETRIEVED PARTIALLY : "
									+ res.getMessage());

					FileOutputStream fos = new FileOutputStream(file, true);
					fos.write(res.getArray());
					fos.close();

					Log.d("CommunityServerAPI",
							"AL momento la lunghezza del file e' "
									+ file.length());
					offSet = offSet + lenght;

				} else if (res.getHttpStatusCode() == HttpStatus.SC_NOT_FOUND) {

					Log.d("CommunityServerAPI", "ATTACHMENT NOT RETRIEVED : "
							+ res.getMessage());

					att.setStatus(AttachmentStatus._DOWNLOAD_FAILED);
					Attachment.updateAttachment(att);

					break;

				} else {

					Log.d("CommunityServerAPI", "ATTACHMENT NOT RETRIEVED : "
							+ res.getMessage());

					if (file.length() >= lenght) {

						att.setStatus(AttachmentStatus._DOWNLOAD_INCOMPLETE);
						Attachment.updateAttachment(att);
					} else {
						att.setStatus(AttachmentStatus._DOWNLOAD_FAILED);
						Attachment.updateAttachment(att);
						file.delete();
					}
					break;
				}

			}

			if (att.getSize() == file.length()
					&& MD5.checkMD5(res.getMd5(), file)) {

				att.setPath(file.getAbsolutePath());
				att.setStatus(AttachmentStatus._UPLOADED);
				Attachment.updateAttachment(att);

			} else if(! att.getStatus().equals(AttachmentStatus._DOWNLOAD_INCOMPLETE)) {

				Log.d("CommunityServerAPI",
						"ATTACHMENT DOES NOT MATCH SIZE OR MD5");
				att.setStatus(AttachmentStatus._DOWNLOAD_FAILED);

				Attachment.updateAttachment(att);
				file.delete();

			}

		} catch (IOException e) {

			Log.d("CommunityServerAPI",
					"ATTACHMENT DO NOT RETRIEVED - ERROR OCCURRED : "
							+ e.getMessage());

			e.printStackTrace();

			if (file.length() >= lenght) {

				att.setStatus(AttachmentStatus._DOWNLOAD_FAILED);
				Attachment.updateAttachment(att);

				file.delete();

			}

		}

		return null;
	}

}
