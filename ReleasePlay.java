import java.time.*;
import java.time.format.*;
import java.util.*;

public class ReleasePlay {
    public static Scanner scanner = new Scanner(System.in);
    public static final boolean isVisibleLog = true;
    public static final boolean isAttackResultArray = false;
    public static final boolean isEnemySecret = true;

    // アルゴリズム
    public static int alphaAlgorithmNumber = 17;

    public static void main(String args[]) {
        Board.Initialize(isVisibleLog, isAttackResultArray, isEnemySecret);

        AlgorithmSwitcher alphaAlgorithm = new AlgorithmSwitcher(true, isEnemySecret);
        // Board.SetRandom4Points(true, true, true);
        // #region 正方形
        // Board.SetType4Points(true, 0);
        // #endregion
        // #region 大ひし形
        Board.SetType4Points(true, 1);
        // #endregion
        // #region 小ひし形
        // Board.SetType4Points(true, 2);
        // #endregion

        alphaAlgorithm.SetAlgorithm(alphaAlgorithmNumber);
        alphaAlgorithm.SetParameter(null);

        Logger.CreateLogger(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now()) + "-"
                + String.format("%03d", alphaAlgorithmNumber) + "-" + "Release", true);

        System.out.print(ConsoleColors.RED);
        System.out.println("┌" + "─".repeat(9) + "┬" + "─".repeat(28) + "┬" + "─".repeat(35) + "┐");
        System.out.println("│   " + String.format("%03d", alphaAlgorithmNumber)
                + "   │         BattleShip         │   " + Logger.GetFileName() + "   │");
        System.out.println("└" + "─".repeat(9) + "┴" + "─".repeat(28) + "┴" + "─".repeat(35) + "┘");
        System.out.print(ConsoleColors.RESET);

        boolean alphaSide = true;
        switch (ConsoleInput.InputKeysValues(new String[] { "先攻", "後攻" }, new String[] { "f", "s" }, "")) {
            case "f":
                alphaSide = true;
                break;
            case "s":
                alphaSide = false;
                break;
        }

        while (Board.IsContinue(false)) {
            if (alphaSide) {
                alphaAlgorithm.Think();
                if (Board.IsLastAttack(alphaSide)) {
                    Board.AttackResultTransfer(alphaSide,
                            new ArrayList<Integer>(Arrays.asList(ConsoleInput
                                    .InputKeys(new String[] { "ハズレ！", "波高し！", "命中！", "撃沈！" }, "攻撃結果"))));
                }
            } else {
                switch (ConsoleInput.InputKeysValues(new String[] { "攻撃", "移動" }, new String[] { "a", "m" }, "")) {
                    case "a":
                        Board.AttackPointForce(alphaSide, ConsoleInput.InputPoint("攻撃"));
                        break;
                    case "m":
                        Board.MoveVectorForce(alphaSide, ConsoleInput.InputVector("移動"));
                        break;
                }
            }
            alphaSide = !alphaSide;
        }
    }
}
