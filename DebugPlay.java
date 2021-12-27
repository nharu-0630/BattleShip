import java.time.*;
import java.time.format.*;
import java.util.*;

public class DebugPlay {
    public static Scanner scanner = new Scanner(System.in);

    public static final int maxTurnCount = 60;
    public static final int deepTryCount = 1000;
    // public static final int deepTryCount = 100;
    // public static final boolean isSaveLog = true;
    public static final boolean isSaveLog = false;
    // public static final boolean isVisibleLog = true;
    public static final boolean isVisibleLog = false;
    public static final boolean isAttackResultArray = false;
    public static final boolean isEnemySecret = false;
    // public static final boolean isStepWait = true;
    public static final boolean isStepWait = false;

    public static int alphaAlgorithmNumber = 11;
    public static int bravoAlgorithmNumber = 11;

    public static void main(String args[]) {
        // ParameterDeepTry();
        DeepTry(null);
        // Try(true, null);
        // Try(false, null);
    }

    public static void Try(boolean alphaSide, int[] parameters) {
        Board.Initialize(isVisibleLog, isAttackResultArray, isEnemySecret);

        AlgorithmSwitcher alphaAlgorithm = new AlgorithmSwitcher(true, isEnemySecret);
        switch ((int) (Math.random() * 2)) {
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
            // case 2:
            // Board.GetCell(1, 1).SetHp(true, 3);
            // Board.GetCell(1, 3).SetHp(true, 3);
            // Board.GetCell(3, 1).SetHp(true, 3);
            // Board.GetCell(3, 3).SetHp(true, 3);
            // break;
            // case 3:
            // Board.GetCell(2, 1).SetHp(true, 3);
            // Board.GetCell(2, 3).SetHp(true, 3);
            // Board.GetCell(1, 2).SetHp(true, 3);
            // Board.GetCell(3, 2).SetHp(true, 3);
            // break;
        }

        AlgorithmSwitcher bravoAlgorithm = new AlgorithmSwitcher(false, isEnemySecret);
        switch ((int) (Math.random() * 2)) {
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
            // case 2:
            // Board.GetCell(1, 1).SetHp(false, 3);
            // Board.GetCell(1, 3).SetHp(false, 3);
            // Board.GetCell(3, 1).SetHp(false, 3);
            // Board.GetCell(3, 3).SetHp(false, 3);
            // break;
            // case 3:
            // Board.GetCell(2, 1).SetHp(false, 3);
            // Board.GetCell(2, 3).SetHp(false, 3);
            // Board.GetCell(1, 2).SetHp(false, 3);
            // Board.GetCell(3, 2).SetHp(false, 3);
            // break;
        }

        alphaAlgorithm.SetAlgorithm(alphaAlgorithmNumber);
        bravoAlgorithm.SetAlgorithm(bravoAlgorithmNumber);

        alphaAlgorithm.SetParameter(parameters);
        bravoAlgorithm.SetParameter(null);

        Logger.CreateLogger(
                DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now()) + "-"
                        + String.format("%03d", alphaAlgorithmNumber) + "-"
                        + String.format("%03d", bravoAlgorithmNumber),
                false);

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
            if (isStepWait) {
                scanner.nextLine();
            }
        }

        if (isSaveLog) {
            Logger.SaveLogger();
        }
    }

    public static void ParameterDeepTry() {
        HashMap<int[], Integer> parameterWinCounts = new HashMap<int[], Integer>();
        for (int parameter1 = 0; parameter1 <= 15; parameter1 += 1) {
            parameterWinCounts.put(new int[] { parameter1 },
                    DeepTry(new int[] { parameter1 }));
        }
        java.util.stream.Stream<Map.Entry<int[], Integer>> sortedParameterWinCounts = parameterWinCounts.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue());
        sortedParameterWinCounts
                .forEach(entry -> System.out
                        .println(Arrays.toString(entry.getKey()) + " = " + entry.getValue()));
    }

    public static int DeepTry(int[] parameters) {
        int alphaWinCount = 0;
        int bravoWinCount = 0;
        for (int i = 0; i < deepTryCount; i++) {
            Try(i % 2 == 0, parameters);
            if (Board.GetAlphaWin() && !Board.GetBravoWin()) {
                alphaWinCount++;
            } else if (Board.GetBravoWin() && !Board.GetAlphaWin()) {
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
