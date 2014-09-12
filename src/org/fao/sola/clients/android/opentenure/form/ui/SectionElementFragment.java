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
package org.fao.sola.clients.android.opentenure.form.ui;

import org.fao.sola.clients.android.opentenure.ModeDispatcher.Mode;
import org.fao.sola.clients.android.opentenure.R;
import org.fao.sola.clients.android.opentenure.form.FieldTemplate;
import org.fao.sola.clients.android.opentenure.form.SectionElementPayload;
import org.fao.sola.clients.android.opentenure.form.SectionTemplate;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SectionElementFragment extends Fragment {

	private View rootView;
	private SectionElementPayload editedElement;
	private SectionTemplate elementTemplate;

	public SectionElementFragment(SectionElementPayload section, SectionTemplate template, Mode mode){
		this.elementTemplate = template;
		this.editedElement = section;
	}

	public SectionElementPayload getEditedElement() {
		return editedElement;
	}

	public SectionElementFragment(){
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		inflater.inflate(R.menu.field_group, menu);
		
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_field_group, container, false);
		setHasOptionsMenu(true);
		InputMethodManager imm = (InputMethodManager) rootView.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
		
		LinearLayout ll = (LinearLayout) rootView;
		int i = 0;
		for(final FieldTemplate field:elementTemplate.getFields()){
			// Add label
			TextView label = new TextView(getActivity());
			label.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

			label.setText(Html.fromHtml(field.getDisplayName()));
			ll.addView(label);
			// Add input field
			switch(field.getType()){
			case DATE:
				ll.addView(FieldViewFactory.getViewForDateField(getActivity(), field, editedElement.
						getFields().get(i)));
				break;
			case TIME:
				ll.addView(FieldViewFactory.getViewForTimeField(getActivity(), field, editedElement.
						getFields().get(i)));
				break;
			case SNAPSHOT:
			case DOCUMENT:
			case GEOMETRY:
			case TEXT:
				ll.addView(FieldViewFactory.getViewForTextField(getActivity(), field, editedElement.
						getFields().get(i)));
				break;
			case DECIMAL:
			case INTEGER:
				ll.addView(FieldViewFactory.getViewForNumberField(getActivity(), field, editedElement.
						getFields().get(i)));
				break;
			case BOOL:
				ll.addView(FieldViewFactory.getViewForBooleanField(getActivity(), field, editedElement.
						getFields().get(i)));
				break;
			default:
				break;
			}
			i++;
		}
		
		return rootView;
	}
}