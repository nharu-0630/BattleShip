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
        Board.Initialize(isVisibleLog, isAttackResultArray, isEnemySecret);

        AlgorithmSwitcher alphaAlgorithm = new AlgorithmSwitcher(true, isEnemySecret);
        System.out.print(ConsoleColors.YELLOW);
        System.out.print("Jsonファイル: ");
        String fileName = "log/" + scanner.nextLine();
        JSONObject jsonObject = new JSONObject(Files.readString(Paths.get(fileName)));
        int lastIndex = 1;
        while (jsonObject.has(String.valueOf(lastIndex))) {
            lastIndex++;
        }
        lastIndex--;
        JSONObject lastObject = jsonObject.getJSONObject(String.valueOf(lastIndex)).getJSONObject("true");
        JSONArray lastHpArray = lastObject.getJSONArray("hp");
        for (int i = 0; i < lastHpArray.length(); i++) {
            int x = i % 5;
            int y = i / 5;
            int hp = (Integer) lastHpArray.get(i);
            if (hp != -1) {
                Board.GetCell(x, y).SetHp(true, hp);
            }
        }
        JSONArray lastValuesArray = lastObject.getJSONArray("values");
        for (int i = 0; i < lastValuesArray.length(); i++) {
            int x = i % 5;
            int y = i / 5;
            int[] value = JSonArrayToIntArray(lastValuesArray.getJSONArray(i));
            for (int j = 0; j < value.length; j++) {
                Board.GetCell(x, y).SetValueForce(true, j, value[j]);
            }
        }

        alphaAlgorithm.SetAlgorithm(alphaAlgorithmNumber);
        alphaAlgorithm.SetParameter(null);

        Logger.CreateLogger(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now()) + "-"
                + String.format("%03d", alphaAlgorithmNumber) + "-" + "Release", true);

        System.out.print(ConsoleColors.RED);
        System.out.println("┌" + "─".repeat(9) + "┬" + "─".repeat(28) + "┬" + "─".repeat(40) + "┐");
        System.out.println("│   " + String.format("%03d", alphaAlgorithmNumber)
                + "   │         BattleShip         │   " + Logger.GetFileName() + "   │");
        System.out.println("└" + "─".repeat(9) + "┴" + "─".repeat(28) + "┴" + "─".repeat(40) + "┘");
        System.out.print(ConsoleColors.RESET);

        boolean alphaSide = true;
        String tempLine = "";
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
