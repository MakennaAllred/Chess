package dataAccess;

import model.GameData;

import java.util.Collection;

public record ListGamesRes(Collection<GameData> games) {
}
