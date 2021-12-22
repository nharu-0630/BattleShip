import java.time.*;
import java.time.format.*;
import java.util.*;

public class ReleasePlay {
    public static Scanner scanner = new Scanner(System.in);

    public static final boolean isVisibleLog = true;
    public static final boolean isAttackResultArray = false;
    public static final boolean isEnemySecret = true;

    public static void main(String args[]) {
        Board.Initialize(isVisibleLog, isAttackResultArray, isEnemySecret);

        Algorithm005 alphaAlgorithm = new Algorithm005(true, isEnemySecret);
        alphaAlgorithm.SetParameter(null);
        switch ((int) (Math.random() * 4)) {
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
        }

        boolean alphaSide = true;
        String tempLine = "";
        CONFIRM: while (true) {
            System.out.print("先攻(f), 後攻(s): ");
            tempLine = scanner.nextLine();
            if (tempLine.equals("f") || tempLine.equals("s")) {
                switch (tempLine) {
                    case "f":
                        alphaSide = true;
                        break;
                    case "s":
                        alphaSide = false;
                        break;
                }
                break CONFIRM;
            }
            System.out.println("入力が正しくありません");
        }

        Logger.CreateLogger(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now()) + "-"
                + alphaAlgorithm.getClass().getCanonicalName() + "-" + "AlgorithmHuman" + ".json", true);

        while (Board.IsContinue(false)) {
            if (alphaSide) {
                alphaAlgorithm.Think();
                if (Board.IsLastAttack(alphaSide)) {
                    CONFIRM: while (true) {
                        System.out.print("ハズレ！(0), 波高し！(1), 命中！(2), 撃沈！(3): ");
                        tempLine = scanner.nextLine();
                        if (tempLine.chars().allMatch(Character::isDigit)) {
                            int attackResult = Integer.parseInt(tempLine);
                            if (0 <= attackResult && attackResult <= 3) {
                                System.out.println("攻撃結果 = " + attackResult);
                                System.out.print("確定(y), 取消(n): ");
                                if (scanner.nextLine().equals("y")) {
                                    Board.AttackResultTransfer(alphaSide,
                                            new ArrayList<Integer>(Arrays.asList(attackResult)));
                                    break CONFIRM;
                                }
                            }
                        }
                        System.out.println("入力が正しくありません");
                    }
                }
            } else {
                CONFIRM: while (true) {
                    System.out.print("攻撃(a), 移動(m): ");
                    switch (scanner.nextLine()) {
                        case "a":
                            System.out.print("x, y: ");
                            Point point = new Point(scanner.nextLine());
                            if (!point.empty && 0 <= point.x && point.x <= 4 && 0 <= point.y && point.y <= 4) {
                                System.out.println("攻撃 = " + point);
                                System.out.print("確定(y), 取消(n): ");
                                if (scanner.nextLine().equals("y")) {
                                    Board.AttackPointForce(alphaSide, point);
                                    break CONFIRM;
                                } else {
                                    System.out.println("取消されました");
                                    continue CONFIRM;
                                }
                            }
                            break;
                        case "m":
                            System.out.print("x, y: ");
                            point = new Point(scanner.nextLine());
                            if (!point.empty
                                    && (point.x == 0 || point.y == 0) && (Math.abs(point.x + point.y) == 1 || Math.abs(
                                            point.x + point.y) == 2)) {
                                System.out.println("移動 = " + point);
                                System.out.print("確定(y), 取消(n): ");
                                if (scanner.nextLine().equals("y")) {
                                    Board.MoveVectorForce(alphaSide, point);
                                    break CONFIRM;
                                } else {
                                    System.out.println("取消されました");
                                    continue CONFIRM;
                                }
                            }
                            break;
                    }
                    System.out.println("入力が正しくありません");
                }
            }
            alphaSide = !alphaSide;
        }
    }
}
