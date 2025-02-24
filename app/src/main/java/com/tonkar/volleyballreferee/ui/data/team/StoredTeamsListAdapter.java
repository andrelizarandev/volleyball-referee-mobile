package com.tonkar.volleyballreferee.ui.data.team;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.material.card.MaterialCardView;
import com.tonkar.volleyballreferee.R;
import com.tonkar.volleyballreferee.engine.api.model.ApiTeamSummary;
import com.tonkar.volleyballreferee.ui.data.SelectableArrayAdapter;
import com.tonkar.volleyballreferee.ui.util.UiUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class StoredTeamsListAdapter extends SelectableArrayAdapter<ApiTeamSummary> {

    static class ViewHolder {
        MaterialCardView listItemCard;
        TextView         nameText;
        ImageView        kindItem;
        ImageView        genderItem;
    }

    private final LayoutInflater       mLayoutInflater;
    private final List<ApiTeamSummary> mStoredTeamsList;
    private final List<ApiTeamSummary> mFilteredStoredTeamsList;
    private final NameFilter           mNameFilter;

    StoredTeamsListAdapter(Context context, LayoutInflater layoutInflater, List<ApiTeamSummary> storedTeamsList) {
        super(context, R.layout.stored_teams_list_item, storedTeamsList);
        mLayoutInflater = layoutInflater;
        mStoredTeamsList = storedTeamsList;
        mFilteredStoredTeamsList = new ArrayList<>();
        mFilteredStoredTeamsList.addAll(mStoredTeamsList);
        mNameFilter = new NameFilter();
    }

    void updateStoredTeamsList(List<ApiTeamSummary> storedTeamsList) {
        mStoredTeamsList.clear();
        mFilteredStoredTeamsList.clear();
        mStoredTeamsList.addAll(storedTeamsList);
        mFilteredStoredTeamsList.addAll(storedTeamsList);
        cleanSelectedItems();
    }

    @Override
    public int getCount() {
        return mFilteredStoredTeamsList.size();
    }

    @Override
    public ApiTeamSummary getItem(int index) {
        return mFilteredStoredTeamsList.get(index);
    }

    @Override
    public long getItemId(int index) {
        return 0;
    }

    @Override
    public @NonNull View getView(int index, View view, @NonNull ViewGroup parent) {
        View teamView = view;
        ViewHolder viewHolder;

        if (teamView == null) {
            teamView = mLayoutInflater.inflate(R.layout.stored_teams_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.listItemCard = teamView.findViewById(R.id.list_item_card);
            viewHolder.nameText = teamView.findViewById(R.id.stored_team_name);
            viewHolder.kindItem = teamView.findViewById(R.id.team_kind_item);
            viewHolder.genderItem = teamView.findViewById(R.id.team_gender_item);
            teamView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) teamView.getTag();
        }

        ApiTeamSummary team = mFilteredStoredTeamsList.get(index);
        viewHolder.nameText.setText(team.getName());

        switch (team.getGender()) {
            case MIXED:
                UiUtils.colorChipIcon(getContext(), R.color.colorMixedLight, R.drawable.ic_mixed, viewHolder.genderItem);
                break;
            case LADIES:
                UiUtils.colorChipIcon(getContext(), R.color.colorLadiesLight, R.drawable.ic_ladies, viewHolder.genderItem);
                break;
            case GENTS:
                UiUtils.colorChipIcon(getContext(), R.color.colorGentsLight, R.drawable.ic_gents, viewHolder.genderItem);
                break;
        }

        switch (team.getKind()) {
            case INDOOR_4X4:
                UiUtils.colorChipIcon(getContext(), R.color.colorIndoor4x4Light, R.drawable.ic_4x4_small, viewHolder.kindItem);
                break;
            case BEACH:
                UiUtils.colorChipIcon(getContext(), R.color.colorBeachLight, R.drawable.ic_beach, viewHolder.kindItem);
                break;
            case SNOW:
                UiUtils.colorChipIcon(getContext(), R.color.colorSnowLight, R.drawable.ic_snow, viewHolder.kindItem);
                break;
            case INDOOR:
            default:
                UiUtils.colorChipIcon(getContext(), R.color.colorIndoorLight, R.drawable.ic_6x6_small, viewHolder.kindItem);
                break;
        }

        viewHolder.listItemCard.setCardBackgroundColor(ContextCompat.getColor(getContext(), isSelectedItem(team.getId()) ? R.color.colorSelectedItem : R.color.colorSurface));

        return teamView;
    }

    @Override
    public @NonNull Filter getFilter() {
        return mNameFilter;
    }

    private class NameFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (prefix == null || prefix.length() == 0) {
                results.values = mStoredTeamsList;
                results.count = mStoredTeamsList.size();
            } else {
                String lowerCaseText = prefix.toString().toLowerCase(Locale.getDefault());

                List<ApiTeamSummary> matchValues = new ArrayList<>();

                for (ApiTeamSummary team : mStoredTeamsList) {
                    if (lowerCaseText.isEmpty() || team.getName().toLowerCase(Locale.getDefault()).contains(lowerCaseText)) {
                        matchValues.add(team);
                    }
                }

                results.values = matchValues;
                results.count = matchValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mFilteredStoredTeamsList.clear();

            if (results.values != null) {
                mFilteredStoredTeamsList.addAll((Collection<? extends ApiTeamSummary>) results.values);
            }

            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}
