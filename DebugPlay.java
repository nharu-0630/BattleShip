import java.time.*;
import java.time.format.*;
import java.util.*;

public class DebugPlay {
    public static final int maxTurnCount = 60;
    public static final int deepTryCount = 1000;

    public static void main(String args[]) {
        // CpuVsHuman();
        // DeepTry(new double[] { 1 });
        Try(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now()) + ".json");
    }

    public static void Try(String fileName) {
        Logger.CreateLogger();

        Board.Initialize(false);

        Algorithm002 alphaAlgorithm = new Algorithm002(true, false);
        Algorithm001 bravoAlgorithm = new Algorithm001(false, false);

        Board.GetCell(0, 0).SetHp(true, 3);
        Board.GetCell(3, 1).SetHp(true, 3);
        Board.GetCell(1, 3).SetHp(true, 3);
        Board.GetCell(4, 4).SetHp(true, 3);

        // Board.SetRandom4Points(true);
        Board.SetRandom4Points(false);

        alphaAlgorithm.SetParameter(new double[] { 1 });
        bravoAlgorithm.SetParameter(new double[] { 1 });

        boolean alphaSide = true;
        while (Board.IsContinue(false)) {
            if (alphaSide) {
                alphaAlgorithm.Think();
            } else {
                bravoAlgorithm.Think();
            }
            alphaSide = !alphaSide;
            if (Board.GetTurnCount() >= maxTurnCount) {
                Board.IsContinue(true);
                break;
            }
        }

        Logger.SaveLogger(fileName);
    }

    public static void CpuVsHuman() {
        Logger.CreateLogger();

        Board.Initialize(true);
        Algorithm002 alphaAlgorithm = new Algorithm002(true, false);
        AlgorithmHuman bravoAlgorithm = new AlgorithmHuman(false, false);

        Board.GetCell(0, 0).SetHp(true, 3);
        Board.GetCell(3, 1).SetHp(true, 3);
        Board.GetCell(1, 3).SetHp(true, 3);
        Board.GetCell(4, 4).SetHp(true, 3);

        Board.GetCell(0, 0).SetHp(false, 3);
        Board.GetCell(0, 4).SetHp(false, 3);
        Board.GetCell(4, 0).SetHp(false, 3);
        Board.GetCell(4, 4).SetHp(false, 3);

        Random random = new Random();
        boolean alphaSide = random.nextDouble() <= 0.5;
        while (Board.IsContinue(false)) {
            if (alphaSide) {
                alphaAlgorithm.Think();
            } else {
                bravoAlgorithm.Think();
            }
            alphaSide = !alphaSide;
            if (Board.GetTurnCount() >= maxTurnCount) {
                Board.IsContinue(true);
                break;
            }
        }
    }

    public static void parameterDeepTry() {
        HashMap<Double[], Integer> parameterWinCounts = new HashMap<Double[], Integer>();
        for (double parameter = 0; parameter <= 1; parameter += 0.01) {
            parameterWinCounts.put(new Double[] { parameter }, DeepTry(new double[] { parameter }));
        }
        java.util.stream.Stream<Map.Entry<Double[], Integer>> sortedParameterWinCounts = parameterWinCounts.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue());
        sortedParameterWinCounts
                .forEach(entry -> System.out
                        .println(Arrays.toString(entry.getKey()) + " = " + entry.getValue()));
    }

    public static int DeepTry(double[] parameters) {
        int alphaWinCount = 0;
        int bravoWinCount = 0;
        for (int i = 0; i < deepTryCount; i++) {
            Logger.CreateLogger();

            Board.Initialize(false);

            Algorithm002 alphaAlgorithm = new Algorithm002(true, false);
            Algorithm001 bravoAlgorithm = new Algorithm001(false, false);

            Board.GetCell(0, 0).SetHp(true, 3);
            Board.GetCell(3, 1).SetHp(true, 3);
            Board.GetCell(1, 3).SetHp(true, 3);
            Board.GetCell(4, 4).SetHp(true, 3);

            // Board.SetRandom4Points(true);
            Board.SetRandom4Points(false);

            alphaAlgorithm.SetParameter(parameters);
            bravoAlgorithm.SetParameter(new double[] { 1 });

            boolean alphaSide = (i % 2 == 0);
            while (Board.IsContinue(false)) {
                if (alphaSide) {
                    alphaAlgorithm.Think();
                } else {
                    bravoAlgorithm.Think();
                }
                alphaSide = !alphaSide;
                if (Board.GetTurnCount() >= maxTurnCount) {
                    Board.IsContinue(true);
                    break;
                }
            }
            if (Board.GetAlphaWin()) {
                alphaWinCount++;
            } else if (Board.GetBravoWin()) {
                bravoWinCount++;
            }
        }
        System.out.println(
                "α勝利数 = " + alphaWinCount + " (" + Math.round(alphaWinCount * 100.0 / deepTryCount)
                        + "%)");
        System.out.println(
                "β勝利数 = " + bravoWinCount + " (" + Math.round(bravoWinCount * 100.0 / deepTryCount)
                        + "%)");
        System.out.println("引き分け数 = " + (deepTryCount - alphaWinCount - bravoWinCount) + " ("
                + Math.round((deepTryCount - alphaWinCount - bravoWinCount) * 100.0 / deepTryCount)
                + "%)");
        return alphaWinCount;
    }
}
