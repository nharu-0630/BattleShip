import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import org.json.*;

public class UndoPlay {
    public static Scanner scanner = new Scanner(System.in);
    public static final boolean isVisibleLog = true;
    public static final boolean isAttackResultArray = false;
    public static final boolean isEnemySecret = true;

    // アルゴリズム
    public static int alphaAlgorithmNumber = 17;

    public static void main(String args[]) throws JSONException, IOException {
        System.out.println(ConsoleInput.InputKeys(new String[] { "ハズレ！", "波高し！", "命中！", "撃沈！" }, "攻撃結果"));

        Board.Initialize(isVisibleLog, isAttackResultArray, isEnemySecret);

        AlgorithmSwitcher alphaAlgorithm = new AlgorithmSwitcher(true, isEnemySecret);
        JSONObject jsonObject;
        while (true) {
            System.out.print(ConsoleColors.YELLOW);
            System.out.print("Jsonファイル: ");
            System.out.print(ConsoleColors.RESET);
            String fileName = "log/" + scanner.nextLine();
            if ((new File(fileName)).exists()) {
                jsonObject = new JSONObject(Files.readString(Paths.get(fileName)));
                break;
            } else {
                System.out.print(ConsoleColors.RED);
                System.out.println("ファイルが存在しません");
                System.out.print(ConsoleColors.RESET);
            }
        }
        int lastIndex = 1;
        while (jsonObject.has(String.valueOf(lastIndex))) {
            lastIndex++;
        }
        lastIndex--;
        System.out.println("LastIndex = " + lastIndex);
        JSONObject lastAlphaObject = jsonObject.getJSONObject(String.valueOf(lastIndex)).getJSONObject("true");
        JSONArray lastAlphaHpArray = lastAlphaObject.getJSONArray("hp");
        for (int i = 0; i < lastAlphaHpArray.length(); i++) {
            int x = i % 5;
            int y = i / 5;
            int hp = (Integer) lastAlphaHpArray.get(i);
            if (hp != -1) {
                Board.GetCell(x, y).SetHp(true, hp);
                System.out.println((new Point(x, y)).toPointFormatString() + " AlphaHp = " + hp);
            }
        }
        JSONArray lastAlphaValuesArray = lastAlphaObject.getJSONArray("values");
        for (int i = 0; i < lastAlphaValuesArray.length(); i++) {
            int x = i % 5;
            int y = i / 5;
            int[] value = JSonArrayToIntArray(lastAlphaValuesArray.getJSONArray(i));
            for (int j = 0; j < value.length; j++) {
                Board.GetCell(x, y).SetValueForce(true, j, value[j]);
                System.out.println((new Point(x, y)).toPointFormatString() + " Layer = " + j + ", Value = " + value[j]);
            }
        }
        JSONObject lastBravoObject = jsonObject.getJSONObject(String.valueOf(lastIndex)).getJSONObject("false");
        JSONArray lastBravoHpArray = lastBravoObject.getJSONArray("hp");
        for (int i = 0; i < lastBravoHpArray.length(); i++) {
            int x = i % 5;
            int y = i / 5;
            int hp = (Integer) lastBravoHpArray.get(i);
            if (hp == 0) {
                Board.GetCell(x, y).SetHp(false, hp);
                System.out.println((new Point(x, y)).toPointFormatString() + " BravoHp = " + hp);
            }
        }

        String tempLine = "";

        int allyCount = 0;
        CONFIRM: while (true) {
            System.out.print(ConsoleColors.YELLOW);
            System.out.print("AllyCount: ");
            System.out.print(ConsoleColors.RESET);
            tempLine = scanner.nextLine();
            if (tempLine.chars().allMatch(Character::isDigit)) {
                allyCount = Integer.parseInt(tempLine);
                if (0 <= allyCount && allyCount <= 4) {
                    System.out.println("AllyCount = " + allyCount);
                    System.out.print(ConsoleColors.YELLOW);
                    System.out.print("確定(y), 取消(n): ");
                    System.out.print(ConsoleColors.RESET);
                    if (scanner.nextLine().equals("y")) {
                        break CONFIRM;
                    } else {
                        System.out.print(ConsoleColors.RED);
                        System.out.println("取消されました");
                        System.out.print(ConsoleColors.RESET);
                        continue CONFIRM;
                    }
                }
            }
            System.out.print(ConsoleColors.RED);
            System.out.println("入力が正しくありません");
            System.out.print(ConsoleColors.RESET);
        }

        int allySumHp = 0;
        CONFIRM: while (true) {
            System.out.print(ConsoleColors.YELLOW);
            System.out.print("AllySumHp: ");
            System.out.print(ConsoleColors.RESET);
            tempLine = scanner.nextLine();
            if (tempLine.chars().allMatch(Character::isDigit)) {
                allySumHp = Integer.parseInt(tempLine);
                if (0 <= allySumHp && allySumHp <= 12) {
                    System.out.println("AllySumHp = " + allySumHp);
                    System.out.print(ConsoleColors.YELLOW);
                    System.out.print("確定(y), 取消(n): ");
                    System.out.print(ConsoleColors.RESET);
                    if (scanner.nextLine().equals("y")) {
                        break CONFIRM;
                    } else {
                        System.out.print(ConsoleColors.RED);
                        System.out.println("取消されました");
                        System.out.print(ConsoleColors.RESET);
                        continue CONFIRM;
                    }
                }
            }
            System.out.print(ConsoleColors.RED);
            System.out.println("入力が正しくありません");
            System.out.print(ConsoleColors.RESET);
        }

        int enemyCount = 0;
        CONFIRM: while (true) {
            System.out.print(ConsoleColors.YELLOW);
            System.out.print("EnemyCount: ");
            System.out.print(ConsoleColors.RESET);
            tempLine = scanner.nextLine();
            if (tempLine.chars().allMatch(Character::isDigit)) {
                enemyCount = Integer.parseInt(tempLine);
                if (0 <= enemyCount && enemyCount <= 4) {
                    System.out.println("EnemyCount = " + enemyCount);
                    System.out.print(ConsoleColors.YELLOW);
                    System.out.print("確定(y), 取消(n): ");
                    System.out.print(ConsoleColors.RESET);
                    if (scanner.nextLine().equals("y")) {
                        break CONFIRM;
                    } else {
                        System.out.print(ConsoleColors.RED);
                        System.out.println("取消されました");
                        System.out.print(ConsoleColors.RESET);
                        continue CONFIRM;
                    }
                }
            }
            System.out.print(ConsoleColors.RED);
            System.out.println("入力が正しくありません");
            System.out.print(ConsoleColors.RESET);
        }

        int enemySumHp = 0;
        CONFIRM: while (true) {
            System.out.print(ConsoleColors.YELLOW);
            System.out.print("EnemySumHp: ");
            System.out.print(ConsoleColors.RESET);
            tempLine = scanner.nextLine();
            if (tempLine.chars().allMatch(Character::isDigit)) {
                enemySumHp = Integer.parseInt(tempLine);
                if (0 <= enemySumHp && enemySumHp <= 12) {
                    System.out.println("EnemySumHp = " + enemySumHp);
                    System.out.print(ConsoleColors.YELLOW);
                    System.out.print("確定(y), 取消(n): ");
                    System.out.print(ConsoleColors.RESET);
                    if (scanner.nextLine().equals("y")) {
                        break CONFIRM;
                    } else {
                        System.out.print(ConsoleColors.RED);
                        System.out.println("取消されました");
                        System.out.print(ConsoleColors.RESET);
                        continue CONFIRM;
                    }
                }
            }
            System.out.print(ConsoleColors.RED);
            System.out.println("入力が正しくありません");
            System.out.print(ConsoleColors.RESET);
        }

        alphaAlgorithm.SetAlgorithm(alphaAlgorithmNumber);
        alphaAlgorithm.SetParameter(null);
        alphaAlgorithm.SetCountSumHp(new int[] { allyCount, allySumHp, enemyCount, enemySumHp });

        Logger.CreateLogger(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now()) + "-"
                + String.format("%03d", alphaAlgorithmNumber) + "-" + "Release", true);

        System.out.print(ConsoleColors.RED);
        System.out.println("┌" + "─".repeat(9) + "┬" + "─".repeat(28) + "┬" + "─".repeat(35) + "┐");
        System.out.println("│   " + String.format("%03d", alphaAlgorithmNumber)
                + "   │         BattleShip         │   " + Logger.GetFileName() + "   │");
        System.out.println("└" + "─".repeat(9) + "┴" + "─".repeat(28) + "┴" + "─".repeat(35) + "┘");
        System.out.print(ConsoleColors.RESET);

        boolean alphaSide = true;
        tempLine = "";
        CONFIRM: while (true) {
            System.out.print(ConsoleColors.YELLOW);
            System.out.print("先攻(f), 後攻(s): ");
            System.out.print(ConsoleColors.RESET);
            tempLine = scanner.nextLine();
            if (tempLine.equals("f") || tempLine.equals("s")) {
                switch (tempLine) {
                    case "f":
                        alphaSide = true;
                        System.out.println("先攻");
                        break;
                    case "s":
                        alphaSide = false;
                        System.out.println("後攻");
                        break;
                }
                break CONFIRM;
            }
            System.out.print(ConsoleColors.RED);
            System.out.println("入力が正しくありません");
            System.out.print(ConsoleColors.RESET);
        }

        while (Board.IsContinue(false)) {
            if (alphaSide) {
                alphaAlgorithm.Think();
                if (Board.IsLastAttack(alphaSide)) {
                    CONFIRM: while (true) {
                        System.out.print(ConsoleColors.YELLOW);
                        System.out.print("ハズレ！(0), 波高し！(1), 命中！(2), 撃沈！(3): ");
                        System.out.print(ConsoleColors.RESET);
                        tempLine = scanner.nextLine();
                        if (tempLine.chars().allMatch(Character::isDigit)) {
                            int attackResult = Integer.parseInt(tempLine);
                            if (0 <= attackResult && attackResult <= 3) {
                                String[] tempStrings = new String[] { "ハズレ！", "波高し！", "命中！", "撃沈！" };
                                System.out.println("攻撃結果 = " + tempStrings[attackResult]);
                                System.out.print(ConsoleColors.YELLOW);
                                System.out.print("確定(y), 取消(n): ");
                                System.out.print(ConsoleColors.RESET);
                                if (scanner.nextLine().equals("y")) {
                                    Board.AttackResultTransfer(alphaSide,
                                            new ArrayList<Integer>(Arrays.asList(attackResult)));
                                    break CONFIRM;
                                } else {
                                    System.out.print(ConsoleColors.RED);
                                    System.out.println("取消されました");
                                    System.out.print(ConsoleColors.RESET);
                                    continue CONFIRM;
                                }
                            }
                        }
                        System.out.print(ConsoleColors.RED);
                        System.out.println("入力が正しくありません");
                        System.out.print(ConsoleColors.RESET);
                    }
                }
            } else {
                CONFIRM: while (true) {
                    System.out.print(ConsoleColors.YELLOW);
                    System.out.print("攻撃(a), 移動(m): ");
                    System.out.print(ConsoleColors.RESET);
                    switch (scanner.nextLine()) {
                        case "a":
                            System.out.print(ConsoleColors.YELLOW);
                            System.out.print("ポイント: ");
                            System.out.print(ConsoleColors.RESET);
                            Point point = new Point(scanner.nextLine());
                            if (!point.empty && 0 <= point.x && point.x <= 4 && 0 <= point.y && point.y <= 4) {
                                System.out.println("攻撃 = " + point.toPointFormatString());
                                System.out.print(ConsoleColors.YELLOW);
                                System.out.print("確定(y), 取消(n): ");
                                System.out.print(ConsoleColors.RESET);
                                if (scanner.nextLine().equals("y")) {
                                    Board.AttackPointForce(alphaSide, point);
                                    break CONFIRM;
                                } else {
                                    System.out.print(ConsoleColors.RED);
                                    System.out.println("取消されました");
                                    System.out.print(ConsoleColors.RESET);
                                    continue CONFIRM;
                                }
                            }
                            break;
                        case "m":
                            System.out.print(ConsoleColors.YELLOW);
                            System.out.print("ベクトル: ");
                            System.out.print(ConsoleColors.RESET);
                            point = new Point(scanner.nextLine());
                            if (!point.empty
                                    && (point.x == 0 || point.y == 0) && ((Math.abs(point.x) + Math.abs(point.y)) == 1
                                            || (Math.abs(point.x) + Math.abs(point.y)) == 2)) {
                                System.out.println("移動 = " + point.toVectorFormaString());
                                System.out.print(ConsoleColors.YELLOW);
                                System.out.print("確定(y), 取消(n): ");
                                System.out.print(ConsoleColors.RESET);
                                if (scanner.nextLine().equals("y")) {
                                    Board.MoveVectorForce(alphaSide, point);
                                    break CONFIRM;
                                } else {
                                    System.out.print(ConsoleColors.RED);
                                    System.out.println("取消されました");
                                    System.out.print(ConsoleColors.RESET);
                                    continue CONFIRM;
                                }
                            }
                            break;
                    }
                    System.out.print(ConsoleColors.RED);
                    System.out.println("入力が正しくありません");
                    System.out.print(ConsoleColors.RESET);
                }
            }
            alphaSide = !alphaSide;
        }
    }

    public static int[] JSonArrayToIntArray(JSONArray jsonArray) {
        int[] intArray = new int[jsonArray.length()];
        for (int i = 0; i < intArray.length; ++i) {
            intArray[i] = jsonArray.optInt(i);
        }
        return intArray;
    }
}
