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

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settingslib.drawer.Tile;

@SearchIndexable
public class EunoiaTeam extends DashboardFragment {
    protected final String TAG = "EunoiaTeamSettings";

    public EunoiaTeam() {
        // Required empty public constructor
    }

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
        return TAG;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.EUNOIA_SETTINGS;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState) {
        return inflater.inflate(R.layout.team_container, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addInformation(view);
    }

    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider(R.xml.top_level_eunoia_team);

    private void addInformation(View parent) {
        // Official Website
        View mWebsite = parent.findViewById(R.id.eunoia_website);
        setupInformation(mWebsite, R.drawable.ic_eunoia_website, R.string.eunoia_website_title, R.string.eunoia_website_url);
        mWebsite.setBackground(createBackground(R.color.eunoia_card, R.color.eunoia_divider, 24f, 6f));
        setMargin(mWebsite, 16, 6, 16, 0);

        // Telegram
        View mTelegram = parent.findViewById(R.id.eunoia_telegram);
        setupInformation(mTelegram, R.drawable.ic_eunoia_telegram, R.string.eunoia_telegram_title, R.string.eunoia_telegram_url);
        mTelegram.setBackground(createBackground(R.color.eunoia_card, R.color.eunoia_divider, 6f, 6f));
        setMargin(mTelegram, 16, 3, 16, 3);

        // Support
        View mSupport = parent.findViewById(R.id.eunoia_support);
        setupInformation(mSupport, R.drawable.ic_eunoia_support, R.string.eunoia_support_title, R.string.eunoia_support_url);
        mSupport.setBackground(createBackground(R.color.eunoia_card, R.color.eunoia_divider, 6f, 24f));
        setMargin(mSupport, 16, 0, 16, 6);
    }

    private GradientDrawable createBackground(int colorBg, int colorFg, float topRadius, float bottomRadius) {
        GradientDrawable drawable = new GradientDrawable();
        int color = getResources().getColor(colorBg, null);
        int border = getResources().getColor(colorFg, null);

        float scale = getResources().getDisplayMetrics().density;
        float top = topRadius * scale;
        float bottom = bottomRadius * scale;

        int strokeWidth = (int) (1 * scale);
        drawable.setColor(color);
        drawable.setStroke(strokeWidth, border);
        drawable.setCornerRadii(new float[] {
                top, top,               // Top left
                top, top,               // Top right
                bottom, bottom,         // Bottom right
                bottom, bottom          // Bottom left
        });

        return drawable;
    }

    private void setMargin(View parent, int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)  parent.getLayoutParams();
        float scale = getResources().getDisplayMetrics().density;

        int left = (int) (leftMargin * scale);
        int top = (int) (topMargin * scale);
        int right = (int) (rightMargin * scale);
        int bottom = (int) (bottomMargin * scale);

        params.setMargins(left, top, right, bottom);
        parent.setLayoutParams(params);
    }

    private void setupInformation(View information, int iconRes, int title, int summary) {
        ImageView mIcon = information.findViewById(R.id.information_icon);
        TextView mTitle = information.findViewById(R.id.information_title);
        TextView mSummary = information.findViewById(R.id.information_summary);

        mIcon.setImageResource(iconRes);
        mTitle.setText(getString(title));
        mSummary.setText(getString(summary));
    }
}
