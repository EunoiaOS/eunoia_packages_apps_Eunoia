/*
 * Copyright (C) 2023 The AtigaOS Project
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

package com.eunoiaos.settings.eunoia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemProperties;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.drawer.Tile;
import com.android.settingslib.search.SearchIndexable;
import com.android.settingslib.widget.LayoutPreference;

import com.eunoiaos.settings.version.BuildNumberController;

import com.google.android.material.appbar.AppBarLayout;

@SearchIndexable
public class EunoiaVersion extends DashboardFragment {
    private static String KEY_VERSION_HEADER = "eunoia_version_info";
    private static final String KEY_EUNOIA_DEVICE_PROP = "ro.eunoia.device";
    private static final String KEY_EUNOIA_MAINTAINER_PROP = "ro.eunoia.maintainer";
    private static final String KEY_EUNOIA_RELEASE_PROP = "ro.eunoia.releasetype";

    private ImageView mAvatar;
    private ImageView mBadge;
    private BuildNumberController mBuildNumberController;
    private Context mContext;
    private TextView mDevice;
    private TextView mMaintainer;
    private Boolean isOfficial;

    @Override
    protected boolean displayTile(Tile tile) {
        return false;
    }

    @Override
    protected int getPreferenceScreenResId() {
        return R.xml.top_level_eunoia_version;
    }

    @Override
    protected String getLogTag() {
        return "NextVersionSettings";
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.EUNOIA_SETTINGS;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mBuildNumberController.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mBuildNumberController = use(BuildNumberController.class);
        mBuildNumberController.setHost(this /* parent */);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View appBarContainer = getActivity().findViewById(R.id.app_bar_container);
        if (appBarContainer != null) {
            appBarContainer.setVisibility(View.GONE);
        }

        mContext = view.getContext();
        LayoutPreference headerPreference = findPreference(KEY_VERSION_HEADER);
        if (headerPreference != null) {
            mAvatar = headerPreference.findViewById(R.id.version_header_icon);
            mAvatar.setImageResource(R.drawable.maintainer_avatar);

            String device = SystemProperties.get(KEY_EUNOIA_DEVICE_PROP, mContext.getString(R.string.unknown));
            String maintainer = SystemProperties.get(KEY_EUNOIA_MAINTAINER_PROP, mContext.getString(R.string.unknown));
            String release = SystemProperties.get(KEY_EUNOIA_RELEASE_PROP, mContext.getString(R.string.unknown));
            mDevice = headerPreference.findViewById(R.id.version_header_title);
            mDevice.setText("EunoiaOS | " + device);
            mMaintainer = headerPreference.findViewById(R.id.version_header_summary);
            mMaintainer.setText(maintainer);

            isOfficial = "VERIFIED".equals(release);
            mBadge = headerPreference.findViewById(R.id.version_header_badge);
            if (isOfficial) {
                mBadge.setVisibility(View.VISIBLE);
            } else {
                mBadge.setVisibility(View.GONE);
            }
        }
    }

    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider(R.xml.top_level_eunoia_version);

    private static String capitalize(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() +
           text.substring(1).toLowerCase();
    }
}
