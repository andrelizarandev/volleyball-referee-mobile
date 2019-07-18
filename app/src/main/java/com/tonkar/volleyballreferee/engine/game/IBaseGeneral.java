package com.tonkar.volleyballreferee.engine.game;

import com.tonkar.volleyballreferee.engine.rules.Rules;
import com.tonkar.volleyballreferee.engine.stored.api.ApiSelectedLeague;

public interface IBaseGeneral {

    GameType getKind();

    String getId();

    String getCreatedBy();

    long getCreatedAt();

    long getUpdatedAt();

    void setUpdatedAt(long updatedAt);

    long getScheduledAt();

    String getRefereedBy();

    void setRefereedBy(String refereedBy);

    String getRefereeName();

    void setRefereeName(String refereeName);

    GameStatus getMatchStatus();

    boolean isMatchCompleted();

    UsageType getUsage();

    void setUsage(UsageType usage);

    Rules getRules();

    ApiSelectedLeague getLeague();

    boolean isIndexed();

    void setIndexed(boolean indexed);
}