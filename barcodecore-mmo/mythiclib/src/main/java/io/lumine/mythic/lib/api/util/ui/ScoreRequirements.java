package io.lumine.mythic.lib.api.util.ui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * A quick number range with special focus on Scoreboards
 *
 * @author Gunging
 */
@SuppressWarnings("unused")
public class ScoreRequirements extends QuickNumberRange {

    /**
     * The name of the objective in question, may
     * not actually exist but this is what the user
     * wrote.
     */
    @NotNull String objectiveName;
    /**
     * @return The name of the objective in question, may
     *         not actually exist but this is what the user
     *         wrote.
     */
    @NotNull public String getObjectiveName() { return objectiveName; }

    /**
     * @return The objective itself, which may not exist if the name
     * is misspelled or whatever.
     */
    @Nullable public Objective getObjective() { return objective; }
    /**
     * The objective itself, which may not exist if the name
     * is misspelled or whatever.
     */
    @Nullable Objective objective;

    /**
     * @return If the objective actually exists.
     */
    public boolean validObjective() { return objective != null; }

    /**
     * A Quick Number Range but with special interest in Scoreboard Objectives.
     *
     * @param min Minimum score allowed
     * @param max Maximum score allowed
     * @param objective Name of the objective.
     */
    public ScoreRequirements(@Nullable Double min, @Nullable Double max, @NotNull String objective) {
        super(min, max);
        objectiveName = objective;
        this.objective = getObjective(objective);
    }

    /**
     * A Quick Number Range but with special interest in Scoreboard Objectives.
     *
     * @param range Range of scores allowed
     * @param objective Name of the objective.
     */
    public ScoreRequirements(@NotNull QuickNumberRange range, @NotNull String objective) {
        super(range.getMinimumInclusive(), range.getMaximumInclusive());
        objectiveName = objective;
        this.objective = getObjective(objective);
    }

    /**
     * A list in the format <code>{[score]=[min]..[max], ...}</code>
     * Not necessarily within {}s, will crop them out if they are there tho.
     *
     * @return An array with all the score requirements. Empty if not in format.
     */
    @NotNull
    public static ArrayList<ScoreRequirements> getFromCompactString(@Nullable String source) {
        // The return
        ArrayList<ScoreRequirements> ret = new ArrayList<>();

        // No null
        if (source == null) { return ret; }

        // Crop {}s
        source = SilentNumbers.unwrapFromCurlyBrackets(source);

        // Split by commas
        String[] splitSR = source.split(",");

        // Foreach
        for (String str : splitSR) {

            // Parse and add
            ScoreRequirements sR = getFromString(str);
            if (sR != null) { ret.add(sR); }
        }

        // Result
        return ret;
    }

    /**
     * In the format <code>[score]=[min]..[max]</code>
     *
     * @return The SINGLE score requirement in there
     */
    @Nullable public static ScoreRequirements getFromString(@Nullable String source) {

        // Bruh
        if (source == null) { return null; }

        // Split equals
        if (source.contains("=")) {

            // Split
            String[] args = source.split("=");

            // Exactly two-yo
            if (args.length == 2) {

                // Parse range lma000
                QuickNumberRange qnr = QuickNumberRange.getFromString(args[1]);

                // Success?
                if (qnr != null) {

                    // JUST GO IN THERE
                    return new ScoreRequirements(qnr, args[0]);
                }
            }
        }

        // Ew format error
        return null;
    }

    @Override
    public String toString() {
        return objectiveName + "=" + super.toString();
    }

    @NotNull
    @Override
    public String toStringColored() {

        // Get Objective Name
        if (validObjective()) {

            return ChatColor.GREEN + objectiveName + " \u00a77: \u00a7e" + super.toStringColored();

        // Objective actually does not exist bruh
        } else {

            return ChatColor.RED + objectiveName + " \u00a77: \u00a7e" + super.toStringColored();
        }
    }

    /**
     * Attempts to get objective. Will be null if not registered.
     *
     * @param name The expected name of the objective.
     */
    @Nullable public static Objective getObjective(@Nullable String name) {
        if (name == null) { return null; }
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager == null) { return null; }
        Scoreboard targetScoreboard = manager.getMainScoreboard();
        return targetScoreboard.getObjective(name);
    }
    public static void setPlayerScore(@NotNull Objective objective, @NotNull Player player, @NotNull PlusMinusPercent score) {

        // Apply
        setEntryScore(objective, player.getName(), score);
    }
    public static void setEntryScore(@NotNull Objective objective, @NotNull String entree, @NotNull PlusMinusPercent score) {

        // Apply
        setEntryScore(objective, entree, SilentNumbers.round(score.apply(getEntryScore(objective, entree) + 0.0D)));
    }
    public static int getPlayerScore(@NotNull Objective objective, @NotNull Player player) {
        return getEntryScore(objective, player.getName());
    }
    public static int getEntryScore(@NotNull Objective objective, @NotNull String entree) {
        Score sc = objective.getScore(entree);
        return sc.getScore();
    }
    public static void setPlayerScore(@NotNull Objective objective, @NotNull Player player, int score) {

        // Set
        setEntryScore(objective, player.getName(), score);
    }
    public static void setEntryScore(@NotNull Objective objective, @NotNull String entry, int score) {

        // Set
        objective.getScore(entry).setScore(score);
    }
    public static void setEntityScore(@NotNull Objective objective, @NotNull Entity player, @NotNull PlusMinusPercent score) {

        // Apply
        setEntityScore(objective, player, SilentNumbers.round(score.apply(getEntityScore(objective, player) + 0.0D)));
    }
    public static int getEntityScore(@NotNull Objective objective, @NotNull Entity player) {
        return objective.getScore(player.getUniqueId().toString()).getScore();
    }
    public static void setEntityScore(@NotNull Objective objective, @NotNull Entity player, int score) {

        // Set
        objective.getScore(player.getUniqueId().toString()).setScore(score);
    }
}
