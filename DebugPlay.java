import java.util.Random;

public class DebugPlay {
    public static final Integer maxTurnCount = 60;

    public static void main(String args[]) {
        // CpuVsHuman();
        DeepTry(1000);
    }

    public static void CpuVsHuman() {
        Board.Initialize(true);
        Algorithm001 alphaAlgorithm = new Algorithm001(true, false);
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

    public static void DeepTry(Integer maxGameCount) {
        Integer alphaWinCount = 0;
        Integer bravoWinCount = 0;
        for (Integer i = 0; i < maxGameCount; i++) {
            Board.Initialize(false);

            Algorithm001 alphaAlgorithm = new Algorithm001(true, false);
            Algorithm001 bravoAlgorithm = new Algorithm001(false, false);

            Board.GetCell(0, 0).SetHp(true, 3);
            Board.GetCell(3, 1).SetHp(true, 3);
            Board.GetCell(1, 3).SetHp(true, 3);
            Board.GetCell(4, 4).SetHp(true, 3);

            Board.SetRandom4Points(false);

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
                "α勝利数 = " + alphaWinCount + " (" + Math.round(alphaWinCount * 100.0 / maxGameCount)
                        + "%)");
        System.out.println(
                "β勝利数 = " + bravoWinCount + " (" + Math.round(bravoWinCount * 100.0 / maxGameCount)
                        + "%)");
        System.out.println("引き分け数 = " + (maxGameCount - alphaWinCount - bravoWinCount) + " ("
                + Math.round((maxGameCount - alphaWinCount - bravoWinCount) * 100.0 / maxGameCount)
                + "%)");
    }
}
