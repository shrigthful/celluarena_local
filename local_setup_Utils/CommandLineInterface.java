package com.codingame.gameengine.runner;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.codingame.gameengine.runner.dto.GameResultDto;
import com.google.common.io.Files;

public class CommandLineInterface {

    public static void main(String[] args) {
        try {
            Options options = new Options();

            // Define required options
            options.addOption("h", false, "Print the help")
                    .addOption("p1", true, "Required. Player 1 command line.")
                    .addOption("p2", true, "Required. Player 2 command line.")
                    .addOption("p3", true, "Required. Player 3 command line.")
                    .addOption("s", false, "Server mode")
                    .addOption("l", true, "File output for logs")
                    .addOption("d", true, "Referee seed")
                    .addOption("psy", false, "Format command line output for psyleague instead of brutaltester");

            CommandLine cmd = new DefaultParser().parse(options, args);

            if (cmd.hasOption("h") || !cmd.hasOption("p1") || !cmd.hasOption("p2")) {
                new HelpFormatter().printHelp(
                        "-p1 <player1 command line> -p2 <player2 command line> [-s -l <File output for logs> -d <seed> -psy]",
                        options);
                System.exit(0);
            }

            // Launch Game
            MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();

            // Choose league level
            gameRunner.setLeagueLevel(3);

            if (cmd.hasOption("d")) {
                String[] parse = cmd.getOptionValue("d").split("=", 0);
                Long seed = Long.parseLong(parse[1]);
                gameRunner.setSeed(seed);
            } else {
                gameRunner.setSeed(System.currentTimeMillis());
            }

            GameResultDto result = gameRunner.gameResult;

            int playerCount = 0;

            for (int i = 1; i <= 3; ++i) {
                if (cmd.hasOption("p" + i)) {
                    gameRunner.addAgent(cmd.getOptionValue("p" + i), cmd.getOptionValue("p" + i));
                    playerCount += 1;
                }
            }

            if (cmd.hasOption("s")) {
                gameRunner.start();
            } else {
                Method initialize = GameRunner.class.getDeclaredMethod("initialize", Properties.class);
                initialize.setAccessible(true);
                initialize.invoke(gameRunner, new Properties());

                Method runAgents = GameRunner.class.getDeclaredMethod("runAgents");
                runAgents.setAccessible(true);
                runAgents.invoke(gameRunner);

                if (cmd.hasOption("l")) {
                    Method getJSONResult = GameRunner.class.getDeclaredMethod("getJSONResult");
                    getJSONResult.setAccessible(true);

                    Files.asCharSink(Paths.get(cmd.getOptionValue("l")).toFile(), Charset.defaultCharset())
                            .write((String) getJSONResult.invoke(gameRunner));
                }

                if (cmd.hasOption("psy")) {
                    int score_p1 = result.scores.get(0);
                    int score_p2 = result.scores.get(1);
                    System.out.println(((score_p1 >= score_p2) ? "0" : "1") + " " + ((score_p2 >= score_p1) ? "0" : "1")
                            + " " + ((score_p1 == -1) ? "1" : "0") + " " + ((score_p2 == -1) ? "1" : "0"));
                } else {
                    for (int i = 0; i < playerCount; ++i) {
                        System.out.println(result.scores.get(i));
                    }

                    for (String line : result.uinput) {
                        System.out.println(line);
                    }
                }
            }

            // We have to clean players process properly
            Field getPlayers = GameRunner.class.getDeclaredField("players");
            getPlayers.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<Agent> players = (List<Agent>) getPlayers.get(gameRunner);

            if (players != null) {
                for (Agent player : players) {
                    Field getProcess = CommandLinePlayerAgent.class.getDeclaredField("process");
                    getProcess.setAccessible(true);
                    Process process = (Process) getProcess.get(player);

                    process.destroy();
                }
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

}