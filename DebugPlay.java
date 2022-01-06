import java.time.*;
import java.time.format.*;
import java.util.*;

public class DebugPlay {
    public static Scanner scanner = new Scanner(System.in);
    public static final boolean isAttackResultArray = false;
    public static final boolean isEnemySecret = false;

    // ターン上限数
    public static final int maxTurnCount = 60;

    // ゲーム試合数
    public static final int deepTryCount = 1000;
    // public static final int deepTryCount = 100;

    // ログ保存
    // public static final boolean isSaveLog = true;
    public static final boolean isSaveLog = false;

    // ログ表示
    // public static final boolean isVisibleLog = true;
    public static final boolean isVisibleLog = false;

    // ステップ待機
    // public static final boolean isStepWait = true;
    public static final boolean isStepWait = false;

    // アルゴリズム
    public static int alphaAlgorithmNumber = 14;
    public static int bravoAlgorithmNumber = 15;

    public static void main(String args[]) {
        // Try(true, null);

        DeepTry(null);

        // ParameterDeepTry();

        // for (int i = 1; i <= 14; i++) {
        // for (int j = i; j <= 14; j++) {
        // alphaAlgorithmNumber = i;
        // bravoAlgorithmNumber = j;
        // if (i != 3 && j != 3) {
        // DeepTry(null);
        // }
        // }
        // }
    }

    public static void Try(boolean alphaSide, int[] parameters) {
        Board.Initialize(isVisibleLog, isAttackResultArray, isEnemySecret);

        AlgorithmSwitcher alphaAlgorithm = new AlgorithmSwitcher(true, isEnemySecret);
        Board.SetRandom4Points(true, true, true);

        AlgorithmSwitcher bravoAlgorithm = new AlgorithmSwitcher(false, isEnemySecret);
        Board.SetRandom4Points(false, true, true);

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
        HashMap<int[], Double> parameterWinCounts = new HashMap<int[], Double>();
        for (int parameter1 = 0; parameter1 <= 20; parameter1 += 1) {
            for (int parameter2 = 0; parameter2 <= 20; parameter2 += 1) {
                for (int parameter3 = 0; parameter3 <= 20; parameter3 += 1) {
                    int[] parameters = new int[] { parameter1, parameter2, parameter3 };
                    parameterWinCounts.put(
                            parameters,
                            DeepTry(parameters)[0]);
                }
            }
        }

        java.util.stream.Stream<Map.Entry<int[], Double>> sortedParameterWinCounts = parameterWinCounts.entrySet()
                .stream()
                .sorted(Map.Entry
                        .comparingByValue());
        sortedParameterWinCounts
                .forEach(entry -> System.out.println(Arrays.toString(entry.getKey()) + " = " + entry.getValue()));
    }

    public static double[] DeepTry(int[] parameters) {
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
        double[] result = new double[] { (alphaWinCount * 100.0 / deepTryCount),
                (bravoWinCount * 100.0 / deepTryCount),
                ((deepTryCount - alphaWinCount - bravoWinCount) * 100.0 / deepTryCount) };
        System.out.println(String.format("%03d", alphaAlgorithmNumber) + "-"
                + String.format("%03d", bravoAlgorithmNumber) + " = " + Arrays.toString(result));
        return result;
    }
}
