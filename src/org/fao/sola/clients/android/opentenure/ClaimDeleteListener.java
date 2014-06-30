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

package org.fao.sola.clients.android.opentenure;

import java.util.Iterator;
import java.util.List;

import org.fao.sola.clients.android.opentenure.filesystem.FileSystemUtilities;
import org.fao.sola.clients.android.opentenure.model.Adjacency;
import org.fao.sola.clients.android.opentenure.model.Attachment;
import org.fao.sola.clients.android.opentenure.model.Claim;
import org.fao.sola.clients.android.opentenure.model.ClaimStatus;
import org.fao.sola.clients.android.opentenure.model.Owner;
import org.fao.sola.clients.android.opentenure.model.Vertex;
import org.fao.sola.clients.android.opentenure.network.WithdrawClaim;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ClaimDeleteListener implements OnClickListener {

	String claimId;
	ViewHolder vh;

	public ClaimDeleteListener(String claimId, ViewHolder vh) {

		this.claimId = claimId;
		this.vh = vh;
	}

	@Override
	public void onClick(final View v) {
		// TODO Auto-generated method stub

		final Claim claim = Claim.getClaim(claimId);

		if (claim.getStatus().equals(ClaimStatus._UNMODERATED)
				|| claim.getStatus().equals(ClaimStatus._CHALLENGED)) {
			if (!OpenTenureApplication.isLoggedin()) {

				Toast toast = Toast.makeText(v.getContext(),
						R.string.message_login_before, Toast.LENGTH_SHORT);
				toast.show();
				return;

			} else {

				if (claimId != null) {

					// Here the withdrawn case
					// custom dialog
					final Dialog dialog = new Dialog(v.getContext());
					dialog.setContentView(R.layout.custom_remove_claim);
					dialog.setTitle(R.string.new_file);

					// Confirm Dialog
					TextView message = (TextView) dialog
							.findViewById(R.id.remove_quest);
					message.setTextSize(30);

					// Confirm Button

					final Button confirmButton = (Button) dialog
							.findViewById(R.id.ClaimWithdrawnConfirm);
					confirmButton.setText(R.string.confirm);

					confirmButton
							.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub

									// Here the community server call

									WithdrawClaim wdc = new WithdrawClaim();
									wdc.execute(claimId);

									dialog.dismiss();

								}
							});

					// Delete locally Button

					final Button deleteLocally = (Button) dialog
							.findViewById(R.id.ClaimDeleteLocally);
					deleteLocally.setText(R.string.delete_locally);

					deleteLocally
							.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub

									List<Owner> list = Owner.getOwners(claimId);
									for (Iterator iterator = list.iterator(); iterator
											.hasNext();) {
										Owner owner = (Owner) iterator.next();
										owner.delete();
									}

									List<Vertex> vertexList = claim
											.getVertices();
									for (Iterator iterator = vertexList
											.iterator(); iterator.hasNext();) {
										Vertex vertex = (Vertex) iterator
												.next();
										vertex.delete();
									}

									List<Attachment> attachments = claim
											.getAttachments();

									for (Iterator iterator = attachments
											.iterator(); iterator.hasNext();) {
										Attachment attachment = (Attachment) iterator
												.next();

										attachment.delete();

									}

									List<Adjacency> adjacencies = Adjacency
											.getAdjacencies(claimId);
									for (Iterator iterator = adjacencies
											.iterator(); iterator.hasNext();) {
										Adjacency adjacency = (Adjacency) iterator
												.next();

										adjacency.delete();

									}

									claim.delete();

									FileSystemUtilities.deleteClaim(claimId);

									OpenTenureApplication
											.getLocalClaimsFragment().refresh();

									dialog.dismiss();

								}
							});

					// Cancel Button

					final Button cancelButton = (Button) dialog
							.findViewById(R.id.ClaimWithdrawnDelete);
					cancelButton.setText(R.string.cancel);

					cancelButton.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							dialog.dismiss();

						}
					});

					dialog.show();
				} else {
					Toast toast = Toast.makeText(v.getContext(),
							R.string.message_save_claim_before_submit,
							Toast.LENGTH_SHORT);
					toast.show();
				}

			}

		} else {

			AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());

			dialog.setTitle(R.string.action_remove_claim);
			dialog.setMessage(R.string.message_remove_claim);

			dialog.setPositiveButton(R.string.confirm,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

							List<Owner> list = Owner.getOwners(claimId);
							for (Iterator iterator = list.iterator(); iterator
									.hasNext();) {
								Owner owner = (Owner) iterator.next();
								owner.delete();
							}

							List<Vertex> vertexList = claim.getVertices();
							for (Iterator iterator = vertexList.iterator(); iterator
									.hasNext();) {
								Vertex vertex = (Vertex) iterator.next();
								vertex.delete();
							}

							List<Attachment> attachments = claim
									.getAttachments();

							for (Iterator iterator = attachments.iterator(); iterator
									.hasNext();) {
								Attachment attachment = (Attachment) iterator
										.next();

								attachment.delete();

							}

							List<Adjacency> adjacencies = Adjacency
									.getAdjacencies(claimId);
							for (Iterator iterator = adjacencies.iterator(); iterator
									.hasNext();) {
								Adjacency adjacency = (Adjacency) iterator
										.next();

								adjacency.delete();

							}

							claim.delete();

							FileSystemUtilities.deleteClaim(claimId);

							OpenTenureApplication.getLocalClaimsFragment()
									.refresh();

						}
					});

			dialog.setNegativeButton(R.string.cancel,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					});

			dialog.show();

		}

	}

}