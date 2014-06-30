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
package org.fao.sola.clients.android.opentenure.filesystem.json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import android.util.Log;

import org.fao.sola.clients.android.opentenure.filesystem.FileSystemUtilities;
import org.fao.sola.clients.android.opentenure.filesystem.json.model.Claimant;
import org.fao.sola.clients.android.opentenure.filesystem.json.model.Person;
import org.fao.sola.clients.android.opentenure.filesystem.json.model.Share;

import org.fao.sola.clients.android.opentenure.model.Attachment;
import org.fao.sola.clients.android.opentenure.model.Claim;
import org.fao.sola.clients.android.opentenure.model.Owner;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtilities {

	public static boolean createClaimJson(String claimID) {

		Log.d("CreateClaimJson", "Calling data2json");

		String json = data2Json(claimID);
		writeJsonTofile(claimID, json);
		return true;
	}

	private static String data2Json(String claimId) {

		org.fao.sola.clients.android.opentenure.filesystem.json.model.Claim tempClaim = new org.fao.sola.clients.android.opentenure.filesystem.json.model.Claim();

		Claim claim = Claim.getClaim(claimId);

		if (claim != null) {

			TimeZone tz = TimeZone.getTimeZone("UTC");
			SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss'Z'");
			sdf.setTimeZone(tz);

			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			// number of days to add
			String lodgementDate = sdf.format(c.getTime());

			c.add(Calendar.MONTH, 1);
			// number of days to add
			String challengeExpiryDate = sdf.format(c.getTime());

			// tempClaim.setChallengedClaim(null);
			tempClaim.setDescription(claim.getName());
			tempClaim
					.setChallengedClaimId(claim.getChallengedClaim() != null ? claim
							.getChallengedClaim().getClaimId() : null);
			tempClaim.setId(claimId);
			tempClaim.setStatusCode(claim.getStatus());
			tempClaim.setLandUseCode(claim.getLandUse());
			tempClaim.setTypeCode(claim.getType());
			tempClaim.setNr("0001");

			tempClaim.setLodgementDate(lodgementDate);
			tempClaim.setChallengeExpiryDate(challengeExpiryDate);

			Claimant person = new Claimant();

			person.setPhone(claim.getPerson().getContactPhoneNumber());
			person.setBirthDate(sdf.format(claim.getPerson().getDateOfBirth()));
			person.setEmail(claim.getPerson().getEmailAddress());
			person.setName(claim.getPerson().getFirstName());
			person.setId(claim.getPerson().getPersonId());
			person.setLastName(claim.getPerson().getLastName());
			person.setMobilePhone(claim.getPerson().getMobilePhoneNumber());
			// person.setPlaceOfBirth(claim.getPerson().getPlaceOfBirth());
			person.setAddress(claim.getPerson().getPostalAddress());
			person.setGenderCode(claim.getPerson().getGender());
			person.setIdTypeCode(claim.getPerson().getIdType());
			person.setIdNumber(claim.getPerson().getIdNumber());

			tempClaim.setClaimant(person);

			List<org.fao.sola.clients.android.opentenure.filesystem.json.model.Attachment> attachments = new ArrayList<org.fao.sola.clients.android.opentenure.filesystem.json.model.Attachment>();

			for (Iterator iterator = claim.getAttachments().iterator(); iterator
					.hasNext();) {
				Attachment attachment = (Attachment) iterator.next();

				org.fao.sola.clients.android.opentenure.filesystem.json.model.Attachment attach = new org.fao.sola.clients.android.opentenure.filesystem.json.model.Attachment();

				attach.setId(attachment.getAttachmentId());
				attach.setDescription(attachment.getDescription());
				attach.setFileName(attachment.getFileName());
				attach.setFileExtension(attachment.getFileType());
				attach.setTypeCode(attachment.getFileType());
				// attach.setFileType(attachment.getFileType());
				attach.setMd5(attachment.getMD5Sum());
				attach.setSize(attachment.getSize());
				attach.setMimeType(attachment.getMimeType());

				attachments.add(attach);
			}

			List<org.fao.sola.clients.android.opentenure.filesystem.json.model.Share> shares = new ArrayList<org.fao.sola.clients.android.opentenure.filesystem.json.model.Share>();

			List<Owner> owners = Owner.getOwners(claimId);

			for (Iterator iterator = owners.iterator(); iterator.hasNext();) {
				Owner owner = (Owner) iterator.next();

				Share share = new Share();

				Person personJson = new Person();
				org.fao.sola.clients.android.opentenure.model.Person personDB = org.fao.sola.clients.android.opentenure.model.Person
						.getPerson(owner.getPersonId());

				personJson.setAddress(personDB.getPostalAddress());
				personJson.setBirthDate(sdf.format(personDB.getDateOfBirth()));
				personJson.setEmail(personDB.getEmailAddress());
				personJson.setGenderCode(personDB.getGender());
				personJson.setId(owner.getOwnerId());    // This trick is to permit a claimant to be also owner . When the issue will be resolved on SOLA -----> personJson.setId(personDB.getPersonId());
				personJson.setMobilePhone(personDB.getMobilePhoneNumber());
				personJson.setLastName(personDB.getLastName()); 
				personJson.setName(personDB.getFirstName());
				personJson.setPhone(personDB.getContactPhoneNumber());
				
				List<Person> ownersJson = new ArrayList<Person>();
				ownersJson.add(personJson);

				share.setOwners(ownersJson);
				share.setId(""+owner.getId());

				share.setNominator(owner.getShares());
				share.setDenominator(100);

				shares.add(share);

			}

			/*
			 * TEmporary off the additional info on the claim submission
			 */

			// List<AdditionalInfo> xMetadata = new ArrayList<AdditionalInfo>();
			//
			// for (Iterator iterator = claim.getMetadata().iterator();
			// iterator.hasNext();) {
			// Metadata metadataO = (Metadata) iterator.next();
			//
			// AdditionalInfo xm = new AdditionalInfo();
			//
			// xm.setMetadataId(metadataO.getMetadataId());
			// xm.setName(metadataO.getName());
			// xm.setValue(metadataO.getValue());
			//
			// xMetadata.add(xm);
			// }

			tempClaim
					.setGpsGeometry(org.fao.sola.clients.android.opentenure.model.Vertex
							.gpsWKTFromVertices(claim.getVertices()));
			tempClaim
					.setMappedGeometry(org.fao.sola.clients.android.opentenure.model.Vertex
							.mapWKTFromVertices(claim.getVertices()));
			tempClaim.setAttachments(attachments);

			tempClaim.setShares(shares);
			// tempClaim.setAdditionaInfo(xMetadata);

			try {
				Gson gson = new GsonBuilder()
						.setPrettyPrinting()
						.serializeNulls()
						.setFieldNamingPolicy(
								FieldNamingPolicy.UPPER_CAMEL_CASE).create();

				String g = gson.toJson(tempClaim);
				Log.d("CreateClaimJson", g);

				return g;

			} catch (Throwable e) {

				Log.d("CreateClaimJson",
						"An error has occurred" + e.getMessage());
				e.printStackTrace();

				return null;
			}
		} else {

			Log.d("CreateClaimJson", "The claim is null");
			return null;
		}

	}

	private static boolean writeJsonTofile(String claimID, String json) {

		try {

			// convert String into InputStream
			InputStream is = new ByteArrayInputStream(json.getBytes());

			// read it with BufferedReader
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			File jFile = new File(FileSystemUtilities.getClaimFolder(claimID),
					"claim.json");

			if (jFile.exists())
				jFile.delete();

			jFile.createNewFile();

			BufferedWriter writer = null;
			writer = new BufferedWriter(new FileWriter(jFile));

			char[] buffer = new char[1024];
			int x;
			while ((x = (br.read(buffer))) != -1) {

				writer.write(buffer, 0, x);
				writer.flush();

			}

			writer.flush();
			writer.close();
			br.close();

		} catch (Exception e) {
			Log.d("CreateClaimJson", "An error has occurred" + e.getMessage());
		}

		return false;

	}

	/** Transform Calendar to ISO 8601 string. */
	public static String fromCalendar(final Calendar calendar) {
		Date date = calendar.getTime();
		String formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
				.format(date);
		return formatted.substring(0, 22) + ":" + formatted.substring(22);
	}

	/** Get current date and time formatted as ISO 8601 string. */
	public static String now() {
		return fromCalendar(GregorianCalendar.getInstance());
	}

	/** Transform ISO 8601 string to Calendar. */
	public static Calendar toCalendar(final String iso8601string)
			throws ParseException {
		Calendar calendar = GregorianCalendar.getInstance();
		String s = iso8601string.replace("Z", "+00:00");
		try {
			s = s.substring(0, 22) + s.substring(23); // to get rid of the ":"
		} catch (IndexOutOfBoundsException e) {
			throw new ParseException("Invalid length", 0);
		}
		Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(s);
		calendar.setTime(date);
		return calendar;
	}

	public static int remainingDays(java.sql.Date challengeExpiryDate) {
		if (challengeExpiryDate == null)
			return 0;
		return (int) ((challengeExpiryDate.getTime() - new java.util.Date()
				.getTime()) / (1000 * 60 * 60 * 24));
	}

}
