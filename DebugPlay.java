public class DebugPlay {
    public static final Integer maxTurnCount = 60;

    public static void main(String args[]) {
        // CpuVsHuman();
        DeepTry(1000);
    }

    public static void CpuVsHuman() {
        Board.Initialize(true);
        Algorithm001 alphaAlgorithm = new Algorithm001(true);
        AlgorithmHuman bravoAlgorithm = new AlgorithmHuman(false);

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
    }

    public static void DeepTry(Integer maxGameCount) {
        Integer alphaWinCount = 0;
        Integer bravoWinCount = 0;
        for (Integer i = 0; i < maxGameCount; i++) {
            Board.Initialize(false);

            Algorithm001 alphaAlgorithm = new Algorithm001(true);
            Algorithm001 bravoAlgorithm = new Algorithm001(false);

            Board.GetCell(0, 0).SetHp(true, 3);
            Board.GetCell(3, 1).SetHp(true, 3);
            Board.GetCell(1, 3).SetHp(true, 3);
            Board.GetCell(4, 4).SetHp(true, 3);

            /*
             * Board.GetCell(0, 0).SetHp(false, 3);
             * Board.GetCell(0, 4).SetHp(false, 3);
             * Board.GetCell(4, 0).SetHp(false, 3);
             * Board.GetCell(4, 4).SetHp(false, 3);
             */

            for (Point point : Board.RandomPoints(4)) {
                Board.GetCell(point).SetHp(false, 3);
            }

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
            if (Board.GetAlphaWin()) {
                alphaWinCount++;
            } else if (Board.GetBravoWin()) {
                bravoWinCount++;
            }
        }
        System.out.println("α勝利数 = " + alphaWinCount);
        System.out.println("β勝利数 = " + bravoWinCount);
        System.out.println("引き分け数 = " + (maxGameCount - alphaWinCount - bravoWinCount));
    }
}
