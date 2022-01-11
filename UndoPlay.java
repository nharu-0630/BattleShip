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

        int allyCount = ConsoleInput.InputInt("AllyCount", 0, 4);
        int allySumHp = ConsoleInput.InputInt("AllySumHp", 0, 12);
        int enemyCount = ConsoleInput.InputInt("EnemyCount", 0, 4);
        int enemySumHp = ConsoleInput.InputInt("EnemySumHp", 0, 12);

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

    public static int[] JSonArrayToIntArray(JSONArray jsonArray) {
        int[] intArray = new int[jsonArray.length()];
        for (int i = 0; i < intArray.length; ++i) {
            intArray[i] = jsonArray.optInt(i);
        }
        return intArray;
    }
}
