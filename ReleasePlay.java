import java.time.*;
import java.time.format.*;
import java.util.*;

public class ReleasePlay {
    public static Scanner scanner = new Scanner(System.in);

    public static final boolean isVisibleLog = true;
    public static final boolean isAttackResultArray = false;
    public static final boolean isEnemySecret = true;

    public static void main(String args[]) {
        Logger.CreateLogger();

        Board.Initialize(isVisibleLog, isAttackResultArray, isEnemySecret);

        Algorithm004 alphaAlgorithm = new Algorithm004(true, isEnemySecret);
        alphaAlgorithm.SetParameter(null);
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
                            String[] tempArray = scanner.nextLine().replaceAll(" ", "").split(",");
                            if (tempArray.length == 2) {
                                if (tempArray[0].chars().allMatch(Character::isDigit) && tempArray[1].chars()
                                        .allMatch(Character::isDigit)) {
                                    int x = Integer.parseInt(tempArray[0]);
                                    int y = Integer.parseInt(tempArray[1]);
                                    if (0 <= x && x <= 4 && 0 <= y && y <= 4) {
                                        Point point = new Point(x, y);
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
                                }
                            }
                            break;
                        case "m":
                            System.out.print("x, y: ");
                            tempArray = scanner.nextLine().replaceAll(" ", "").split(",");
                            if (tempArray.length == 2) {
                                if (tempArray[0].matches("[+-]?\\d*(\\.\\d+)?") && tempArray[1]
                                        .matches("[+-]?\\d*(\\.\\d+)?")) {
                                    int x = Integer.parseInt(tempArray[0]);
                                    int y = Integer.parseInt(tempArray[1]);
                                    if ((x == 0 || y == 0) && (Math.abs(x + y) == 1 || Math.abs(x + y) == 2)) {
                                        Point vectorPoint = new Point(x, y);
                                        System.out.println("移動 = " + vectorPoint);
                                        System.out.print("確定(y), 取消(n): ");
                                        if (scanner.nextLine().equals("y")) {
                                            Board.MoveVectorForce(alphaSide, vectorPoint);
                                            break CONFIRM;
                                        } else {
                                            System.out.println("取消されました");
                                            continue CONFIRM;
                                        }
                                    }
                                }
                            }
                            break;
                    }
                    System.out.println("入力が正しくありません");
                }
            }
            alphaSide = !alphaSide;
        }

        Logger.SaveLogger(
                DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now()) + "-" + alphaAlgorithm
                        .getClass().getCanonicalName() + "-" + "AlgorithmHuman" + ".json");
    }
}
