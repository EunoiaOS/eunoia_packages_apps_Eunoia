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

package com.eunoiaos.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.search.SearchIndexable;
import com.android.settingslib.widget.LayoutPreference;

import com.eunoiaos.settings.version.BuildNumberController;

@SearchIndexable
public class EunoiaVersionFragment extends DashboardFragment {
    private static String KEY_VERSION_HEADER = "eunoia_version_info";

    private BuildNumberController mBuildNumberController;
    private ImageView mMaintainer;

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
    public int getTitle() {
        return 0;
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

        LayoutPreference headerPreference = findPreference(KEY_VERSION_HEADER);
        if (headerPreference != null) {
            mMaintainer = headerPreference.findViewById(R.id.version_header_icon);
            mMaintainer.setImageResource(R.drawable.maintainer_avatar);
        }
    }

    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider(R.xml.top_level_eunoia_version);
}
