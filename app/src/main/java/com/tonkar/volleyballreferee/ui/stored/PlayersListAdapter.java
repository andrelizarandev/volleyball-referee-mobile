package com.tonkar.volleyballreferee.ui.stored;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.tonkar.volleyballreferee.R;
import com.tonkar.volleyballreferee.engine.stored.api.ApiPlayer;
import com.tonkar.volleyballreferee.engine.team.IBaseTeam;
import com.tonkar.volleyballreferee.engine.team.TeamType;
import com.tonkar.volleyballreferee.ui.util.UiUtils;

import java.util.ArrayList;
import java.util.List;

public class PlayersListAdapter extends BaseAdapter {

    private final LayoutInflater  mLayoutInflater;
    private final Context         mContext;
    private final IBaseTeam       mTeamService;
    private final TeamType        mTeamType;
    private final List<ApiPlayer> mPlayers;

    PlayersListAdapter(LayoutInflater layoutInflater, Context context, IBaseTeam teamService, TeamType teamType) {
        mLayoutInflater = layoutInflater;
        mContext = context;
        mTeamService = teamService;
        mTeamType = teamType;
        mPlayers = new ArrayList<>(mTeamService.getPlayers(mTeamType));
    }

    @Override
    public int getCount() {
        return mPlayers.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int index, View view, ViewGroup parent) {
        TextView playerText;

        if (view == null) {
            playerText = (TextView) mLayoutInflater.inflate(R.layout.ladder_item, null);
        } else {
            playerText = (TextView) view;
        }

        int number = mPlayers.get(index).getNum();
        playerText.setText(UiUtils.formatNumberFromLocale(number));

        if (mTeamService.isLibero(mTeamType, number)) {
            UiUtils.colorTeamText(mContext, mTeamService.getLiberoColor(mTeamType), playerText);
        } else {
            UiUtils.colorTeamText(mContext, mTeamService.getTeamColor(mTeamType), playerText);
        }

        if (mTeamService.isCaptain(mTeamType, number)) {
            playerText.setPaintFlags(playerText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        } else {
            playerText.setPaintFlags(playerText.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
        }

        return playerText;
    }
}