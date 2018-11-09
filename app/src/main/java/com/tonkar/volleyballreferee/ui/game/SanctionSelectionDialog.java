package com.tonkar.volleyballreferee.ui.game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.tonkar.volleyballreferee.R;
import com.tonkar.volleyballreferee.interfaces.team.BaseTeamService;
import com.tonkar.volleyballreferee.interfaces.GameService;
import com.tonkar.volleyballreferee.interfaces.sanction.SanctionType;
import com.tonkar.volleyballreferee.interfaces.team.TeamType;
import com.tonkar.volleyballreferee.ui.util.UiUtils;
import com.tonkar.volleyballreferee.ui.team.PlayerToggleButton;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;

public abstract class SanctionSelectionDialog {

    private AlertDialog   mAlertDialog;
    private GridView      mSanctionGrid;
    private PlayerAdapter mPlayerAdapter;
    private SanctionType  mSelectedSanctionType;

    SanctionSelectionDialog(LayoutInflater layoutInflater, final Context context, String title, final GameService gameService, final TeamType teamType) {
        View sanctionsView = layoutInflater.inflate(R.layout.sanction_selection_dialog, null);

        final Spinner sanctionSpinner = sanctionsView.findViewById(R.id.sanction_spinner);
        final SanctionTypeAdapter sanctionTypeAdapter = new SanctionTypeAdapter(layoutInflater, context);
        sanctionSpinner.setAdapter(sanctionTypeAdapter);

        sanctionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                mSelectedSanctionType = sanctionTypeAdapter.getItem(index);
                computePlayersVisibility();
                computeOkAvailability();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        mSanctionGrid = sanctionsView.findViewById(R.id.sanction_grid);

        mPlayerAdapter = new PlayerAdapter(layoutInflater, context, gameService, teamType);
        mSanctionGrid.setAdapter(mPlayerAdapter);

        mSelectedSanctionType = sanctionTypeAdapter.getItem(sanctionSpinner.getSelectedItemPosition());
        computePlayersVisibility();

        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppTheme_Dialog);
        builder.setTitle(title).setView(sanctionsView);
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            if (SanctionType.DELAY_WARNING.equals(mSelectedSanctionType) || SanctionType.DELAY_PENALTY.equals(mSelectedSanctionType)) {
                onSanction(teamType, sanctionTypeAdapter.getItem(sanctionSpinner.getSelectedItemPosition()), -1);
            } else {
                onSanction(teamType, sanctionTypeAdapter.getItem(sanctionSpinner.getSelectedItemPosition()), mPlayerAdapter.getSelectedPlayer());
            }
        });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {});

        mAlertDialog = builder.create();
    }

    private void computePlayersVisibility() {
        if (SanctionType.DELAY_WARNING.equals(mSelectedSanctionType) || SanctionType.DELAY_PENALTY.equals(mSelectedSanctionType)) {
            mSanctionGrid.setVisibility(View.GONE);
        } else {
            mSanctionGrid.setVisibility(View.VISIBLE);
        }
    }

    private void computeOkAvailability() {
        if (SanctionType.DELAY_WARNING.equals(mSelectedSanctionType) || SanctionType.DELAY_PENALTY.equals(mSelectedSanctionType)) {
            mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
        } else if (mPlayerAdapter.getSelectedPlayer() >= 0) {
            mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
        } else {
            mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        }
    }

    public void show() {
        if (mAlertDialog != null) {
            mAlertDialog.show();
            computeOkAvailability();
        }
    }

    public abstract void onSanction(TeamType teamType, SanctionType sanctionType, int player);

    private class SanctionTypeAdapter extends BaseAdapter {

        private final LayoutInflater     mLayoutInflater;
        private final Context            mContext;
        private final List<SanctionType> mSanctionTypes;

        SanctionTypeAdapter(LayoutInflater layoutInflater, Context context) {
            mLayoutInflater = layoutInflater;
            mContext = context;
            mSanctionTypes = new ArrayList<>();
            mSanctionTypes.add(SanctionType.DELAY_WARNING);
            mSanctionTypes.add(SanctionType.DELAY_PENALTY);
            mSanctionTypes.add(SanctionType.YELLOW);
            mSanctionTypes.add(SanctionType.RED);
            mSanctionTypes.add(SanctionType.RED_EXPULSION);
            mSanctionTypes.add(SanctionType.RED_DISQUALIFICATION);
        }

        @Override
        public int getCount() {
            return mSanctionTypes.size();
        }

        @Override
        public SanctionType getItem(int i) {
            return mSanctionTypes.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int index, View view, ViewGroup viewGroup) {
            View itemView;

            if (view == null) {
                itemView = mLayoutInflater.inflate(R.layout.sanction_selection_item, null);
            } else {
                itemView = view;
            }

            ImageView sanctionImage = itemView.findViewById(R.id.sanction_type_image);
            TextView sanctionText = itemView.findViewById(R.id.sanction_text);

            switch (mSanctionTypes.get(index)) {
                case YELLOW:
                    sanctionText.setText(mContext.getResources().getString(R.string.yellow_card));
                    sanctionImage.setImageResource(R.drawable.yellow_card);
                    break;
                case RED:
                    sanctionText.setText(mContext.getResources().getString(R.string.red_card));
                    sanctionImage.setImageResource(R.drawable.red_card);
                    break;
                case RED_EXPULSION:
                    sanctionText.setText(mContext.getResources().getString(R.string.red_card_expulsion));
                    sanctionImage.setImageResource(R.drawable.expulsion_card);
                    break;
                case RED_DISQUALIFICATION:
                    sanctionText.setText(mContext.getResources().getString(R.string.red_card_disqualification));
                    sanctionImage.setImageResource(R.drawable.disqualification_card);
                    break;
                case DELAY_WARNING:
                    sanctionText.setText(mContext.getResources().getString(R.string.yellow_card));
                    sanctionImage.setImageResource(R.drawable.delay_warning);
                    break;
                case DELAY_PENALTY:
                    sanctionText.setText(mContext.getResources().getString(R.string.red_card));
                    sanctionImage.setImageResource(R.drawable.delay_penalty);
                    break;
            }

            return itemView;
        }
    }

    private class PlayerAdapter extends BaseAdapter {

        private final LayoutInflater  mLayoutInflater;
        private final Context         mContext;
        private final BaseTeamService mTeamService;
        private final TeamType        mTeamType;
        private final List<Integer>   mPlayers;
        private       int             mSelectedPlayer;

        private PlayerAdapter(LayoutInflater layoutInflater, Context context, BaseTeamService teamService, TeamType teamType) {
            mLayoutInflater = layoutInflater;
            mContext = context;
            mTeamService = teamService;
            mTeamType = teamType;
            mPlayers = new ArrayList<>(mTeamService.getPlayers(mTeamType));
            mSelectedPlayer = -1;
        }

        @Override
        public int getCount() {
            // Coach + players
            return 1 + mPlayers.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, final ViewGroup parent) {
            PlayerToggleButton button;

            if (view == null) {
                button = (PlayerToggleButton) mLayoutInflater.inflate(R.layout.player_toggle_item, null);
            } else {
                button = (PlayerToggleButton) view;
            }

            final int player;

            if (mPlayers.size() > position) {
                player = mPlayers.get(position);
                button.setText(UiUtils.formatNumberFromLocale(player));
                if (mTeamService.isLibero(mTeamType, player)) {
                    button.setColor(mContext, mTeamService.getLiberoColor(mTeamType));
                } else {
                    button.setColor(mContext, mTeamService.getTeamColor(mTeamType));
                }
            } else {
                player = 0;
                button.setText(mContext.getResources().getString(R.string.coach_abbreviation));
                button.setColor(mContext, mTeamService.getTeamColor(mTeamType));
            }


            button.setOnCheckedChangeListener((cButton, isChecked) -> {
                UiUtils.animate(mContext, cButton);
                if (isChecked) {
                    for (int index = 0; index < parent.getChildCount(); index++) {
                        PlayerToggleButton child = (PlayerToggleButton) parent.getChildAt(index);
                        if (child != cButton && child.isChecked()) {
                            child.setChecked(false);
                            mSelectedPlayer = -1;
                        }
                    }
                    mSelectedPlayer = player;
                } else {
                    mSelectedPlayer = -1;
                }
                computeOkAvailability();
            });

            return button;
        }

        public int getSelectedPlayer() {
            return mSelectedPlayer;
        }
    }

}
