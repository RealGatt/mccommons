package de.exceptionflug.mccommons.scoreboards;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Scoreboards {

	private static final Map<AbstractBoardHolder, Scoreboard> BOARD_HOLDER_MAP = new ConcurrentHashMap<>();
	private static final Map<UUID, AbstractBoardHolder> BOARD_HOLDERS = new ConcurrentHashMap<>();

	public static Set<AbstractBoardHolder> getBoardHolders(final Scoreboard scoreboard) {
		final Set<AbstractBoardHolder> out = new HashSet<>();
		for (final AbstractBoardHolder uuid : BOARD_HOLDER_MAP.keySet()) {
			if (BOARD_HOLDER_MAP.get(uuid).equals(scoreboard))
				out.add(uuid);
		}
		return out;
	}

	public static AbstractBoardHolder getBoardHolder(Player player) {
		return BOARD_HOLDERS.computeIfAbsent(player.getUniqueId(), uuid -> new PlayerBoardHolder(player.getUniqueId()));
	}

	public static Scoreboard getCurrentBoard(final Player p) {
		return BOARD_HOLDER_MAP.get(BOARD_HOLDERS.computeIfAbsent(p.getUniqueId(), uuid -> new PlayerBoardHolder(p.getUniqueId())));
	}

	public static Scoreboard getCurrentBoard(final AbstractBoardHolder boardHolder) {
		return BOARD_HOLDER_MAP.get(boardHolder);
	}

	public static void show(final Player player, final Scoreboard scoreboard) {
		show(new PlayerBoardHolder(player.getUniqueId()), scoreboard);
	}

	public static void show(final AbstractBoardHolder boardHolder, final Scoreboard scoreboard) {
		final Scoreboard oldBoard = getCurrentBoard(boardHolder);
		if (oldBoard != null) {
			for (final Team team : oldBoard.getTeams()) {
				boardHolder.accept(team.createDestroyPacket());
			}
			for (final Objective objective : oldBoard.getObjectives()) {
				boardHolder.accept(objective.createDestroyPacket());
			}
		}
		if (scoreboard != null) {
			BOARD_HOLDER_MAP.put(boardHolder, scoreboard);
			for (final Objective objective : scoreboard.getObjectives()) {
				boardHolder.accept(scoreboard.createObjectiveCreatePacket(objective));
				for (final Score score : objective.getScores()) {
					if (score.isScoreSet()) {
						boardHolder.accept(score.createSetScorePacket());
					}
				}
			}
			for (final DisplaySlot slot : scoreboard.displaySlotObjectiveMap.keySet()) {
				if (slot != null) {
					final Objective objective = scoreboard.getObjectiveOfDisplaySlot(slot);
					boardHolder.accept(objective.createPositionPacket());
				}
			}
			for (final Team team : scoreboard.getTeams()) {
				boardHolder.accept(scoreboard.createTeamCreatePacket(team));
			}
		}
	}

}
