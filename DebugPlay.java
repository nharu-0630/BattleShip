import java.time.*;
import java.time.format.*;
import java.util.*;

public class DebugPlay {
    public static final int maxTurnCount = 60;
    public static final int deepTryCount = 1000;
    // public static final int deepTryCount = 100;
    public static final boolean isVisibleLog = false;
    public static final boolean isAttackResultArray = false;
    public static final boolean isEnemySecret = false;

    public static void main(String args[]) {
        DeepTry(null);
        // Try(true, null);
        // Try(false, null);
    }

    public static void Try(boolean alphaSide, double[] parameters) {
        Board.Initialize(isVisibleLog, isAttackResultArray, isEnemySecret);

        Algorithm004 alphaAlgorithm = new Algorithm004(true, isEnemySecret);
        switch ((int) (Math.random() * 3)) {
            case 0:
                Board.GetCell(0, 0).SetHp(true, 3);
                Board.GetCell(3, 1).SetHp(true, 3);
                Board.GetCell(1, 3).SetHp(true, 3);
                Board.GetCell(4, 4).SetHp(true, 3);
                break;
            case 1:
                Board.GetCell(4, 0).SetHp(true, 3);
                Board.GetCell(1, 1).SetHp(true, 3);
                Board.GetCell(3, 3).SetHp(true, 3);
                Board.GetCell(0, 4).SetHp(true, 3);
                break;
            case 2:
                Board.GetCell(1, 1).SetHp(true, 3);
                Board.GetCell(1, 3).SetHp(true, 3);
                Board.GetCell(3, 1).SetHp(true, 3);
                Board.GetCell(3, 3).SetHp(true, 3);
                break;
            case 3:
                Board.GetCell(2, 1).SetHp(true, 3);
                Board.GetCell(2, 3).SetHp(true, 3);
                Board.GetCell(1, 2).SetHp(true, 3);
                Board.GetCell(3, 2).SetHp(true, 3);
                break;
            case 4:
                Board.SetRandom4Points(true);
                break;
        }

        Algorithm005 bravoAlgorithm = new Algorithm005(false, isEnemySecret);
        switch ((int) (Math.random() * 3)) {
            case 0:
                Board.GetCell(0, 0).SetHp(false, 3);
                Board.GetCell(3, 1).SetHp(false, 3);
                Board.GetCell(1, 3).SetHp(false, 3);
                Board.GetCell(4, 4).SetHp(false, 3);
                break;
            case 1:
                Board.GetCell(4, 0).SetHp(false, 3);
                Board.GetCell(1, 1).SetHp(false, 3);
                Board.GetCell(3, 3).SetHp(false, 3);
                Board.GetCell(0, 4).SetHp(false, 3);
                break;
            case 2:
                Board.GetCell(1, 1).SetHp(false, 3);
                Board.GetCell(1, 3).SetHp(false, 3);
                Board.GetCell(3, 1).SetHp(false, 3);
                Board.GetCell(3, 3).SetHp(false, 3);
                break;
            case 3:
                Board.GetCell(2, 1).SetHp(false, 3);
                Board.GetCell(2, 3).SetHp(false, 3);
                Board.GetCell(1, 2).SetHp(false, 3);
                Board.GetCell(3, 2).SetHp(false, 3);
                break;
            case 4:
                Board.SetRandom4Points(false);
                break;
        }

        alphaAlgorithm.SetParameter(parameters);
        bravoAlgorithm.SetParameter(new double[] { 1 });

        Logger.CreateLogger(
                DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now()) + "-" + alphaAlgorithm
                        .getClass().getCanonicalName() + "-" + bravoAlgorithm.getClass().getCanonicalName());

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

        Logger.SaveLogger();
    }

    public static void ParameterDeepTry() {
        HashMap<Double[], Integer> parameterWinCounts = new HashMap<Double[], Integer>();
        for (double parameter = 0; parameter <= 1; parameter += 0.05) {
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
            Try(i % 2 == 0, parameters);
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
