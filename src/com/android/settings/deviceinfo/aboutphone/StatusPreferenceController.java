/*
 * Copyright (C) 2020 Wave-OS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.deviceinfo.aboutphone;

import android.content.Context;
import android.os.Build;
import android.os.SystemProperties;
import android.widget.TextView;
import android.text.TextUtils;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import com.android.settings.R;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.widget.LayoutPreference;

public class StatusPreferenceController extends AbstractPreferenceController {

    private static final String KEY_BUILD_STATUS = "rom_build_status";
    private static final String PROP_BUILD_RELEASETYPE = "ro.palyrim.releasetype";
    private static final String PROP_BUILD_MAINTAINER = "ro.palyrim.maintainer";


    public StatusPreferenceController(Context context) {
        super(context);
    }

    private String getbuildStatus() {
	final String buildType = SystemProperties.get(PROP_BUILD_RELEASETYPE,
                this.mContext.getString(R.string.device_info_default));
        final String isOfficial = this.mContext.getString(R.string.build_is_official_title);
	final String isUnofficial = this.mContext.getString(R.string.build_is_unofficial_title);

	if (buildType.equalsIgnoreCase("Official")) {
		return isOfficial;
	} else {
		return isUnofficial;
	}
    }

    private String getMaintainer() {
	final String Maintainer = SystemProperties.get(PROP_BUILD_MAINTAINER,
                this.mContext.getString(R.string.device_info_default));
	final String buildType = SystemProperties.get(PROP_BUILD_RELEASETYPE,
                this.mContext.getString(R.string.device_info_default));
        final String isOffFine = this.mContext.getString(R.string.build_is_official_summary, Maintainer);
	final String isOffMiss = this.mContext.getString(R.string.build_is_official_summary_oopsie);
	final String isCommMiss = this.mContext.getString(R.string.build_is_unofficial_summary_oopsie);
	final String isCommFine = this.mContext.getString(R.string.build_is_unofficial_summary, Maintainer);

	if (buildType.equalsIgnoreCase("Official") && !Maintainer.equalsIgnoreCase("Unknown")) {
		return isOffFine;
	} else if (buildType.equalsIgnoreCase("Official") && Maintainer.equalsIgnoreCase("Unknown")) {
		return isOffMiss;
	} else if (buildType.equalsIgnoreCase("Unofficial") && Maintainer.equalsIgnoreCase("Unknown")) {
		return isCommMiss;
	} else {
		return isCommFine;
	}
    }

    @Override
    public void displayPreference(PreferenceScreen screen) {
        super.displayPreference(screen);
        final Preference buildStatusPref = screen.findPreference(KEY_BUILD_STATUS);
	final String buildStatus = getbuildStatus();
        final String Maintainer = getMaintainer();
	final String isOfficial = SystemProperties.get(PROP_BUILD_RELEASETYPE,
                this.mContext.getString(R.string.device_info_default));
	buildStatusPref.setSummary(Maintainer);
	if (isOfficial.equalsIgnoreCase("Official")) {
		 buildStatusPref.setIcon(R.drawable.verified);
	} else {
		buildStatusPref.setIcon(R.drawable.unverified);
	}
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public String getPreferenceKey() {
        return KEY_BUILD_STATUS;
    }
}
