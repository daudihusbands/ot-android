/**
 * ******************************************************************************************
 * Copyright (C) 2014 - Food and Agriculture Organization of the United Nations (FAO).
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,281
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
package org.fao.sola.clients.android.opentenure.model;

import java.io.StringReader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.fao.sola.clients.android.opentenure.OpenTenureApplication;
import org.fao.sola.clients.android.opentenure.R;
import org.fao.sola.clients.android.opentenure.filesystem.json.JsonUtilities;
import org.fao.sola.clients.android.opentenure.form.FormPayload;

import android.content.Context;

public class Claim {
	
	
	public enum Status {
		unmoderated, moderated, challenged, created, uploading, updating, upload_incomplete, update_incomplete, upload_error, update_error, withdrawn, reviewed
	};

	public static final int MAX_SHARES_PER_CLAIM = 100;

	public FormPayload getSurveyForm() {
		return surveyForm;
	}

	public void setSurveyForm(FormPayload surveyForm) {
		this.surveyForm = surveyForm;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<AdditionalInfo> getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(List<AdditionalInfo> additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	Database db = OpenTenureApplication.getInstance().getDatabase();

	public Claim() {
		this.claimId = UUID.randomUUID().toString();
		this.status = ClaimStatus._CREATED;
		this.availableShares = MAX_SHARES_PER_CLAIM;
	}

	public int getAvailableShares() {
		return availableShares;
	}

	public void setAvailableShares(int availableShares) {
		this.availableShares = availableShares;
	}

	@Override
	public String toString() {
		return "Claim [" + "claimId=" + claimId + ", status=" + status
				+ ", claimNumber=" + claimNumber + ", type=" + type + ", name="
				+ name + ", person=" + person + ", propertyLocations="
				+ Arrays.toString(propertyLocations.toArray()) + ", vertices="
				+ Arrays.toString(vertices.toArray()) + ", additionalInfo="
				+ Arrays.toString(additionalInfo.toArray())
				+ ", challengedClaim=" + challengedClaim + ", notes=" + notes
				+ ", challengeExpiryDate=" + challengeExpiryDate
				+ ", dateOfStart=" + dateOfStart
				+ ", version=" + version
				+ ", challengingClaims="
				// + Arrays.toString(challengingClaims.toArray())
				+ ", attachments=" + Arrays.toString(attachments.toArray())
				+ ", shares=" + Arrays.toString(shares.toArray())
				+ ", availableShares=" + availableShares + "]";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getClaimId() {
		return claimId;
	}

	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public List<Vertex> getVertices() {
		return vertices;
	}

	public void setVertices(List<Vertex> vertices) {
		this.vertices = vertices;
	}

	public List<PropertyLocation> getPropertyLocations() {
		return propertyLocations;
	}

	public void setPropertyLocations(List<PropertyLocation> propertyLocations) {
		this.propertyLocations = propertyLocations;
	}

	public Claim getChallengedClaim() {
		return challengedClaim;
	}

	public void setChallengedClaim(Claim challengedClaim) {
		this.challengedClaim = challengedClaim;
	}

	public List<Claim> getChallengingClaims() {
		return challengingClaims;
	}

	public void setChallengingClaims(List<Claim> challengingClaims) {
		this.challengingClaims = challengingClaims;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public List<ShareProperty> getShares() {
		return shares;
	}

	public void setShares(List<ShareProperty> shares) {
		availableShares = MAX_SHARES_PER_CLAIM;
		for (ShareProperty share : shares) {
			availableShares -= share.getShares();
		}
		this.shares = shares;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getChallengeExpiryDate() {
		return challengeExpiryDate;
	}

	public void setChallengeExpiryDate(Date challengeExpiryDate) {
		this.challengeExpiryDate = challengeExpiryDate;
	}

	public String getLandUse() {
		return landUse;
	}

	public void setLandUse(String landUse) {
		this.landUse = landUse;
	}

	public java.sql.Date getDateOfStart() {
		return dateOfStart;
	}

	public void setDateOfStart(java.sql.Date dateOfStart) {
		this.dateOfStart = dateOfStart;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getClaimNumber() {
		return claimNumber;
	}

	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}

	public AdjacenciesNotes getAdjacenciesNotes() {
		return adjacenciesNotes;
	}

	public void setAdjacenciesNotes(AdjacenciesNotes adjacenciesNotes) {
		this.adjacenciesNotes = adjacenciesNotes;
	}

	public String getRecorderName() {
		return recorderName;
	}

	public void setRecorderName(String recorderName) {
		this.recorderName = recorderName;
	}

	public static int createClaim(Claim claim) {
		int result = 0;
		Connection localConnection = null;
		PreparedStatement statement = null;

		try {

			localConnection = OpenTenureApplication.getInstance().getDatabase()
					.getConnection();
			statement = localConnection
					.prepareStatement("INSERT INTO CLAIM(CLAIM_ID, STATUS, CLAIM_NUMBER, NAME, TYPE, PERSON_ID, CHALLENGED_CLAIM_ID, CHALLANGE_EXPIRY_DATE,DATE_OF_START, LAND_USE, NOTES, RECORDER_NAME, VERSION, SURVEY_FORM) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			statement.setString(1, claim.getClaimId());
			statement.setString(2, claim.getStatus());
			statement.setString(3, claim.getClaimNumber());
			statement.setString(4, claim.getName());
			statement.setString(5, claim.getType());
			statement.setString(6, claim.getPerson().getPersonId());
			if (claim.getChallengedClaim() != null) {
				statement.setString(7, claim.getChallengedClaim().getClaimId());

			} else {
				statement.setString(7, null);
			}
			statement.setDate(8, claim.getChallengeExpiryDate());
			statement.setDate(9, claim.getDateOfStart());
			statement.setString(10, claim.getLandUse());
			statement.setString(11, claim.getNotes());
			statement.setString(12, claim.getRecorderName());
			statement.setString(13, claim.getVersion());
			if (claim.getSurveyForm() != null) {
				statement.setCharacterStream(14, new StringReader(claim.getSurveyForm().toJson()));

			} else {
				statement.setCharacterStream(14, null);
			}

			result = statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
				}
			}
			if (localConnection != null) {
				try {
					localConnection.close();
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}

	public int create() {
		int result = 0;
		Connection localConnection = null;
		PreparedStatement statement = null;

		try {

			localConnection = db.getConnection();
			statement = localConnection
					.prepareStatement("INSERT INTO CLAIM(CLAIM_ID, STATUS, CLAIM_NUMBER, NAME, TYPE, PERSON_ID, CHALLENGED_CLAIM_ID, CHALLANGE_EXPIRY_DATE,DATE_OF_START, LAND_USE, NOTES,RECORDER_NAME,VERSION, SURVEY_FORM) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			statement.setString(1, getClaimId());
			statement.setString(2, getStatus());
			statement.setString(3, getClaimNumber());
			statement.setString(4, getName());
			statement.setString(5, getType());
			statement.setString(6, getPerson().getPersonId());
			if (getChallengedClaim() != null) {
				statement.setString(7, getChallengedClaim().getClaimId());
			} else {
				statement.setString(7, null);
			}
			statement.setDate(8, getChallengeExpiryDate());
			statement.setDate(9, getDateOfStart());
			statement.setString(10, getLandUse());
			statement.setString(11, getNotes());
			statement.setString(12, getRecorderName());
			statement.setString(13, getVersion());
			if (getSurveyForm() != null) {
				statement.setCharacterStream(14, new StringReader(getSurveyForm().toJson()));

			} else {
				statement.setCharacterStream(14, null);
			}
			result = statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
				}
			}
			if (localConnection != null) {
				try {
					localConnection.close();
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}

	public static int updateClaim(Claim claim) {
		int result = 0;
		Connection localConnection = null;
		PreparedStatement statement = null;

		try {

			localConnection = OpenTenureApplication.getInstance().getDatabase()
					.getConnection();
			statement = localConnection
					.prepareStatement("UPDATE CLAIM SET STATUS=?, CLAIM_NUMBER=?, NAME=?, PERSON_ID=?, TYPE=?,CHALLENGED_CLAIM_ID=?, CHALLANGE_EXPIRY_DATE=?, DATE_OF_START=?, LAND_USE=?, NOTES=?, RECORDER_NAME=?, VERSION=? , SURVEY_FORM=? WHERE CLAIM_ID=?");
			statement.setString(1, claim.getStatus());
			statement.setString(2, claim.getClaimNumber());
			statement.setString(3, claim.getName());
			statement.setString(4, claim.getPerson().getPersonId());
			statement.setString(5, claim.getType());
			if (claim.getChallengedClaim() != null) {
				statement.setString(6, claim.getChallengedClaim().getClaimId());
			} else {
				statement.setString(6, null);
			}
			statement.setDate(7, claim.getChallengeExpiryDate());
			statement.setDate(8, claim.getDateOfStart());
			statement.setString(9, claim.getLandUse());
			statement.setString(10, claim.getNotes());
			statement.setString(11, claim.getRecorderName());
			statement.setString(12, claim.getVersion());
			if (claim.getSurveyForm() != null) {
				statement.setCharacterStream(13, new StringReader(claim.getSurveyForm().toJson()));

			} else {
				statement.setCharacterStream(13, null);
			}
			statement.setString(14, claim.getClaimId());
			result = statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
				}
			}
			if (localConnection != null) {
				try {
					localConnection.close();
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}

	public int update() {
		int result = 0;
		Connection localConnection = null;
		PreparedStatement statement = null;

		try {

			localConnection = db.getConnection();
			statement = localConnection
					.prepareStatement("UPDATE CLAIM SET STATUS=?, CLAIM_NUMBER=?,NAME=?, PERSON_ID=?, TYPE=?, CHALLENGED_CLAIM_ID=?, CHALLANGE_EXPIRY_DATE=?, DATE_OF_START=?, LAND_USE=?, NOTES=?, RECORDER_NAME=?, VERSION=?, SURVEY_FORM=?  WHERE CLAIM_ID=?");
			statement.setString(1, getStatus());
			statement.setString(2, getClaimNumber());
			statement.setString(3, getName());
			statement.setString(4, getPerson().getPersonId());
			statement.setString(5, getType());
			if (getChallengedClaim() != null) {
				statement.setString(6, getChallengedClaim().getClaimId());

			} else {
				statement.setString(6, null);
			}
			statement.setDate(7, getChallengeExpiryDate());
			statement.setDate(8, getDateOfStart());
			statement.setString(9, getLandUse());
			statement.setString(10, getNotes());
			statement.setString(11, getRecorderName());
			statement.setString(12, getVersion());
			if (getSurveyForm() != null) {
				statement.setCharacterStream(13, new StringReader(getSurveyForm().toJson()));

			} else {
				statement.setCharacterStream(13, null);
			}
			statement.setString(14, getClaimId());
			result = statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
				}
			}
			if (localConnection != null) {
				try {
					localConnection.close();
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}

	public static Claim getClaim(String claimId) {
		Claim claim = null;
		Connection localConnection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {

			localConnection = OpenTenureApplication.getInstance().getDatabase()
					.getConnection();
			statement = localConnection
					.prepareStatement("SELECT STATUS, CLAIM_NUMBER, NAME, PERSON_ID, TYPE, CHALLENGED_CLAIM_ID, CHALLANGE_EXPIRY_DATE, DATE_OF_START, LAND_USE, NOTES, RECORDER_NAME, VERSION, SURVEY_FORM FROM CLAIM WHERE CLAIM_ID=?");
			statement.setString(1, claimId);
			rs = statement.executeQuery();
			while (rs.next()) {
				claim = new Claim();
				claim.setClaimId(claimId);
				claim.setStatus(rs.getString(1));
				claim.setClaimNumber(rs.getString(2));
				claim.setName(rs.getString(3));
				claim.setPerson(Person.getPerson(rs.getString(4)));
				claim.setType((rs.getString(5)));
				claim.setChallengedClaim(Claim.getClaim(rs.getString(6)));
				// claim.setChallengingClaims(getChallengingClaims(claimId));
				claim.setChallengeExpiryDate(rs.getDate(7));
				claim.setDateOfStart(rs.getDate(8));
				claim.setLandUse(rs.getString(9));
				claim.setNotes(rs.getString(10));
				claim.setRecorderName(rs.getString(11));
				claim.setVersion(rs.getString(12));
				Clob clob = rs.getClob(13);
				if(clob != null){
					claim.setSurveyForm(FormPayload.fromJson(clob.getSubString(1L, (int)clob.length())));
				}else{
					claim.setSurveyForm(new FormPayload());
				}
				claim.setVertices(Vertex.getVertices(claimId));
				claim.setPropertyLocations(PropertyLocation
						.getPropertyLocations(claimId));
				claim.setAttachments(Attachment.getAttachments(claimId));
				claim.setShares(ShareProperty.getShares(claimId));
				claim.setAdditionalInfo(AdditionalInfo
						.getClaimAdditionalInfo(claimId));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
				}
			}
			if (localConnection != null) {
				try {
					localConnection.close();
				} catch (SQLException e) {
				}
			}
		}
		return claim;
	}

	public static List<Claim> getChallengingClaims(String claimId) {
		List<Claim> challengingClaims = new ArrayList<Claim>();
		Connection localConnection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			localConnection = OpenTenureApplication.getInstance().getDatabase()
					.getConnection();
			statement = localConnection
					.prepareStatement("SELECT CLAIM_ID FROM CLAIM WHERE CHALLENGED_CLAIM_ID=?");
			statement.setString(1, claimId);
			rs = statement.executeQuery();
			while (rs.next()) {

				Claim challengingClaim = Claim.getClaim(rs.getString(1));
				challengingClaims.add(challengingClaim);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
				}
			}
			if (localConnection != null) {
				try {
					localConnection.close();
				} catch (SQLException e) {
				}
			}
		}
		return challengingClaims;
	}

	public static int addOwner(String claimId, String personId, int shares) {
		return Claim.getClaim(claimId).addOwner(personId, shares);
	}


	public int addOwner(String personId, int shares) {

		ShareProperty share = new ShareProperty();
		share.setClaimId(claimId);
		share.setShares(shares);

		int result = share.create();

		if (result == 1) {
			availableShares -= shares;
		}
		return result;
	}

	public int removeShare(String shareId) {

		ShareProperty share = ShareProperty.getShare(shareId);
		int shares = share.getShares();

		int result = share.deleteShare();

		if (result == 1) {
			availableShares += shares;
		}
		return result;
	}

	public static List<Claim> getAllClaims() {
		List<Claim> claims = new ArrayList<Claim>();
		Connection localConnection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {

			localConnection = OpenTenureApplication.getInstance().getDatabase()
					.getConnection();
			statement = localConnection
					.prepareStatement("SELECT CLAIM_ID FROM CLAIM");
			rs = statement.executeQuery();
			while (rs.next()) {
				Claim claim = Claim.getClaim(rs.getString(1));
				claims.add(claim);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
				}
			}
			if (localConnection != null) {
				try {
					localConnection.close();
				} catch (SQLException e) {
				}
			}
		}
		return claims;
	}

	public static int getNumberOfClaims() {
		Connection localConnection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		int result = 0;

		try {

			localConnection = OpenTenureApplication.getInstance().getDatabase()
					.getConnection();
			statement = localConnection
					.prepareStatement("SELECT COUNT (*) FROM CLAIM");
			rs = statement.executeQuery();
			while (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
				}
			}
			if (localConnection != null) {
				try {
					localConnection.close();
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}

	public int delete() {
		int result = 0;
		Connection localConnection = null;
		PreparedStatement statement = null;

		try {

			localConnection = OpenTenureApplication.getInstance().getDatabase()
					.getConnection();
			statement = localConnection
					.prepareStatement("DELETE CLAIM WHERE CLAIM_ID=?");
			statement.setString(1, getClaimId());

			result = statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();

		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
				}
			}
			if (localConnection != null) {
				try {
					localConnection.close();
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}

	public String getSlogan(Context context) {
		String claimName = getName().equalsIgnoreCase("") ? context
				.getString(R.string.default_claim_name) : getName();
		return claimName + ", " + context.getString(R.string.by) + ": "
				+ getPerson().getFirstName() + " " + getPerson().getLastName();
	}

	public boolean isModifiable() {
		
		if(getChallengeExpiryDate() == null)
			return true;

		if ((getStatus().equals(ClaimStatus._MODERATED) || getStatus().equals(
				ClaimStatus._WITHDRAWN))
				|| (JsonUtilities.remainingDays(getChallengeExpiryDate()) < 1))
			return false;
		else return true;
	}

	private String claimId;
	private String name;
	private String type;
	private String status;
	private Person person;
	private Date dateOfStart;
	private Claim challengedClaim;
	private AdjacenciesNotes adjacenciesNotes;
	private List<Vertex> vertices;
	private List<PropertyLocation> propertyLocations;
	private List<AdditionalInfo> additionalInfo;
	private List<Claim> challengingClaims;
	private List<Attachment> attachments;
	private List<ShareProperty> shares;
	private Date challengeExpiryDate;
	private int availableShares;
	private String landUse;
	private String notes;
	private String claimNumber;
	private String recorderName;
	private String version;
	private FormPayload surveyForm;

}
