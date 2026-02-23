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
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settingslib.drawer.Tile;
import com.android.settingslib.search.SearchIndexable;
import com.android.settingslib.widget.LayoutPreference;

@SearchIndexable
public class EunoiaTeam extends DashboardFragment {
    @Override
    protected boolean displayTile(Tile tile) {
        return false;
    }

    @Override
    protected int getPreferenceScreenResId() {
        return R.xml.top_level_eunoia_team;
    }

    @Override
    protected String getLogTag() {
        return "EunoiaTeamSettings";
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.EUNOIA_SETTINGS;
    }

    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider(R.xml.top_level_eunoia_team);
}
