/*
 * Copyright (C) 2023-2025 The EunoiaOS Project
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

package com.eunoiaos.settings.version;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.VisibleForTesting;
import androidx.preference.Preference;

import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.core.BasePreferenceController;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;

public class VersionDetailController extends BasePreferenceController {
    private static final String TAG = "eunoiaVersionCtrl";
    private static final String KEY_EUNOIA_VERSION = "eunoia_version";
    private static final int DELAY_TIMER_MILLIS = 500;
    private static final int ACTIVITY_TRIGGER_COUNT = 3;

    private static final String KEY_EUNOIA_VERSION_PROP = "ro.eunoia.version";
    private static final String KEY_EUNOIA_CODENAME_PROP = "ro.eunoia.codename";
    private static final String KEY_EUNOIA_DEVICE_PROP = "ro.eunoia.device";
    private static final String KEY_EUNOIA_RELEASE_PROP = "ro.eunoia.releasetype";

    private final UserManager mUserManager;
    private final long[] mHits = new long[ACTIVITY_TRIGGER_COUNT];

    private RestrictedLockUtils.EnforcedAdmin mFunDisallowedAdmin;
    private boolean mFunDisallowedBySystem;

    private boolean showCodename = false;

    public VersionDetailController(Context context, String key) {
        super(context, key);
        mUserManager = (UserManager) mContext.getSystemService(Context.USER_SERVICE);
        initializeAdminPermissions();
    }

    @Override
    public int getAvailabilityStatus() {
        return AVAILABLE;
    }

    @Override
    public boolean useDynamicSliceSummary() {
        return true;
    }

    @Override
    public boolean isSliceable() {
        return true;
    }

    @Override
    public String getPreferenceKey() {
        return KEY_EUNOIA_VERSION;
    }

    @Override
    public CharSequence getSummary() {
        String version = SystemProperties.get(KEY_EUNOIA_VERSION_PROP,
                mContext.getString(R.string.unknown));
        String release = SystemProperties.get(KEY_EUNOIA_RELEASE_PROP,
                mContext.getString(R.string.unknown));

        return version + " | " + capitalize(release);
    }

    @Override
    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!TextUtils.equals(preference.getKey(), KEY_EUNOIA_VERSION)) {
            return false;
        }
        String version = SystemProperties.get(KEY_EUNOIA_VERSION_PROP,
                mContext.getString(R.string.unknown));
        String codename = SystemProperties.get(KEY_EUNOIA_CODENAME_PROP,
                mContext.getString(R.string.unknown));
        String device = SystemProperties.get(KEY_EUNOIA_DEVICE_PROP,
                mContext.getString(R.string.unknown));
        String release = SystemProperties.get(KEY_EUNOIA_RELEASE_PROP,
                mContext.getString(R.string.unknown));

        if (showCodename) {
            preference.setSummary(codename + " | " + device + " | " + capitalize(release));
            showCodename = false;
        } else {
            preference.setSummary(version + " | " + capitalize(release));
            showCodename = true;
        }
        return false;
    }

    /**
     * Copies the array onto itself to remove the oldest hit.
     */
    @VisibleForTesting
    void arrayCopy() {
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
    }

    @VisibleForTesting
    void initializeAdminPermissions() {
        mFunDisallowedAdmin = RestrictedLockUtilsInternal.checkIfRestrictionEnforced(
                mContext, UserManager.DISALLOW_FUN, UserHandle.myUserId());
        mFunDisallowedBySystem = RestrictedLockUtilsInternal.hasBaseUserRestriction(
                mContext, UserManager.DISALLOW_FUN, UserHandle.myUserId());
    }

    private static String capitalize(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() +
           text.substring(1).toLowerCase();
    }
}
