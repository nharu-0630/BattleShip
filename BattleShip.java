import java.util.*;
import java.io.*;
import org.json.*;

// class PointList<T> extends ArrayList<T> {

//     public T GetRandom() {
//         return super.get((int) (Math.random() * super.size()));
//     }
// }

class Cell {
    // 戦艦HP
    private int alphaHp;
    private int bravoHp;
    // 評価値配列
    private ArrayList<Integer> alphaValues;
    private ArrayList<Integer> bravoValues;
    // 攻撃可否
    private boolean alphaEnableAttack;
    private boolean bravoEnableAttack;

    Cell() {
        alphaHp = -1;
        bravoHp = -1;
        alphaValues = new ArrayList<Integer>(Arrays.asList(0));
        bravoValues = new ArrayList<Integer>(Arrays.asList(0));
        alphaEnableAttack = false;
        bravoEnableAttack = false;
    }

    /**
     * 戦艦HPを取得
     * 
     * @param alphaSide
     * @return
     */
    public int GetHp(boolean alphaSide) {
        if (alphaSide) {
            return alphaHp;
        } else {
            return bravoHp;
        }
    }

    /**
     * 戦艦HPを設定
     * 
     * @param alphaSide
     * @param hp
     */
    public void SetHp(boolean alphaSide, int hp) {
        if (alphaSide) {
            alphaHp = hp;
        } else {
            bravoHp = hp;
        }
    }

    /**
     * 評価値を取得
     * 
     * @param alphaSide
     * @param layer
     * @return
     */
    public int GetValue(boolean alphaSide, int layer) {
        if (alphaSide) {
            if (alphaValues.size() > layer) {
                return alphaValues.get(layer);
            }
        } else {
            if (bravoValues.size() > layer) {
                return bravoValues.get(layer);
            }
        }
        return -1;
    }

    /**
     * 評価値配列を取得
     * 
     * @param alphaSide
     * @return
     */
    public ArrayList<Integer> GetValues(boolean alphaSide) {
        if (alphaSide) {
            return alphaValues;
        } else {
            return bravoValues;
        }
    }

    /**
     * 評価値を設定
     * 
     * @param alphaSide
     * @param layer
     * @param value
     */
    public void SetValue(boolean alphaSide, int layer, int value) {
        if (alphaSide) {
            while (layer >= alphaValues.size()) {
                alphaValues.add(0);
            }
            if (alphaValues.get(layer) >= 0) {
                alphaValues.set(layer, value);
            }
        } else {
            while (layer >= bravoValues.size()) {
                bravoValues.add(0);
            }
            if (bravoValues.get(layer) >= 0) {
                bravoValues.set(layer, value);
            }
        }
    }

    /**
     * 評価値を加算
     * 
     * @param alphaSide
     * @param layer
     * @param value
     */
    public void AddValue(boolean alphaSide, int layer, int value) {
        if (value == 0) {
            return;
        }
        value = GetValue(alphaSide, layer) + value;
        value = value == -1 ? 0 : value;
        if (alphaSide) {
            while (layer >= alphaValues.size()) {
                alphaValues.add(0);
            }
            if (alphaValues.get(layer) >= 0) {
                alphaValues.set(layer, value);
            }
        } else {
            while (layer >= bravoValues.size()) {
                bravoValues.add(0);
            }
            if (bravoValues.get(layer) >= 0) {
                value = bravoValues.set(layer, value);
            }
        }
    }

    /**
     * 評価値を強制設定
     * 
     * @param alphaSide
     * @param layer
     * @param value
     */
    public void SetValueForce(boolean alphaSide, int layer, int value) {
        if (alphaSide) {
            while (layer >= alphaValues.size()) {
                alphaValues.add(0);
            }
            if (alphaValues.get(layer) >= -1) {
                alphaValues.set(layer, value);
            }
        } else {
            while (layer >= bravoValues.size()) {
                bravoValues.add(0);
            }
            if (bravoValues.get(layer) >= -1) {
                bravoValues.set(layer, value);
            }
        }
    }

    /**
     * 攻撃可否を設定
     * 
     * @param alphaSide
     * @param enableAttack
     */
    public void SetEnableAttack(boolean alphaSide, boolean enableAttack) {
        if (enableAttack) {
            if (alphaSide) {
                if (alphaHp == -1) {
                    alphaEnableAttack = true;
                }
            } else {
                if (bravoHp == -1) {
                    bravoEnableAttack = true;
                }
            }
        } else {
            if (alphaSide) {
                alphaEnableAttack = false;
            } else {
                bravoEnableAttack = false;
            }
        }
    }

    /**
     * 攻撃可否を取得
     * 
     * @param alphaSide
     * @return
     */
    public boolean GetEnableAttack(boolean alphaSide) {
        if (alphaSide) {
            return alphaEnableAttack;
        } else {
            return bravoEnableAttack;
        }
    }

    /**
     * 戦艦生死を取得
     * 
     * @param alphaSide
     * @return
     */
    public boolean IsAlive(boolean alphaSide) {
        if (alphaSide) {
            return alphaHp > 0;
        } else {
            return bravoHp > 0;
        }
    }

    /**
     * 戦艦有無を取得
     * 
     * @param alphaSide
     * @return
     */
    public boolean IsEmpty(boolean alphaSide) {
        if (alphaSide) {
            return alphaHp == -1 && bravoHp != 0;
        } else {
            return bravoHp == -1 && alphaHp != 0;
        }
    }
}

class Point {

    public boolean empty = true;
    // 座標X, Y値
    public int x;
    public int y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
        empty = false;
    }

    Point(String string) {
        if (string.length() != 2) {
            return;
        }
        if (!Character.isDigit(string.charAt(1))) {
            return;
        }
        int number = Integer.valueOf(string.substring(1, 2));
        switch (string.substring(0, 1)) {
            case "A":
                x = number - 1;
                y = 0;
                empty = false;
                break;
            case "B":
                x = number - 1;
                y = 1;
                empty = false;
                break;
            case "C":
                x = number - 1;
                y = 2;
                empty = false;
                break;
            case "D":
                x = number - 1;
                y = 3;
                empty = false;
                break;
            case "E":
                x = number - 1;
                y = 4;
                empty = false;
                break;
            case "東":
                x = number;
                y = 0;
                empty = false;
                break;
            case "西":
                x = number * -1;
                y = 0;
                empty = false;
                break;
            case "南":
                x = 0;
                y = number;
                empty = false;
                break;
            case "北":
                x = 0;
                y = number * -1;
                empty = false;
                break;
        }
    }

    /**
     * ポイントの加算を取得
     * 
     * @param point
     * @return
     */
    public Point Plus(Point point) {
        return new Point(this.x + point.x, this.y + point.y);
    }

    /**
     * ポイントの減算を取得
     * 
     * @param point
     * @return
     */
    public Point Minus(Point point) {
        return new Point(this.x - point.x, this.y - point.y);
    }

    /**
     * 数値の乗算を取得
     * 
     * @param number
     * @return
     */
    public Point Multiply(int number) {
        return new Point(x * number, y * number);
    }

    /**
     * 数値の除算を取得
     * 
     * @param number
     * @return
     */
    public Point Divide(int number) {
        return new Point(x / number, y / number);
    }

    /**
     * 距離を取得
     */
    public int Distance() {
        return Math.abs(x) + Math.abs(y);
    }

    /**
     * ポイントの一致を取得
     */
    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Point) {
            Point point = (Point) object;
            return (x == point.x && y == point.y);
        } else {
            return false;
        }
    }

    /**
     * ボード内の座標有無を取得
     * 
     * @return
     */
    public boolean IsRange() {
        return (0 <= x && x < Board.BOARD_SIZE && 0 <= y && y < Board.BOARD_SIZE);
    }

    /**
     * (x, y)を取得
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    /**
     * アルファベット数字を取得
     * 
     * @return
     */
    public String toPointFormatString() {
        String[] yStrings = new String[] { "A", "B", "C", "D", "E" };
        if (0 <= y && y <= 4) {
            return yStrings[y] + (x + 1);
        }
        return toString();
    }

    /**
     * 方角数字を取得
     * 
     * @return
     */
    public String toVectorFormaString() {
        if (y == 0) {
            if (x > 0) {
                return "東" + x;
            }
            if (x < 0) {
                return "西" + Math.abs(x);
            }
        }
        if (x == 0) {
            if (y > 0) {
                return "南" + y;
            }
            if (y < 0) {
                return "北" + Math.abs(y);
            }
        }
        return toString();
    }
}

class Board {
    // ボードの長さ
    public static final int BOARD_SIZE = 5;

    // 攻撃結果
    // なし
    public static final int ATTACK_NULL = -1;
    // ハズレ！
    public static final int RESULT_NOHIT = 0;
    // 波高し！
    public static final int RESULT_NEAR = 1;
    // 命中！
    public static final int RESULT_HIT = 2;
    // 撃沈！
    public static final int RESULT_SINK = 3;

    // セル二次元配列
    private static Cell[][] cells;
    // ターン数
    private static int turnCount = 0;
    // 勝利フラッグ
    private static boolean alphaWin = false;
    private static boolean bravoWin = false;

    // 前ターンの攻撃ポイント
    private static Point lastAlphaAttackPoint = null;
    private static Point lastBravoAttackPoint = null;
    // 前ターンの攻撃結果
    private static ArrayList<Integer> lastAlphaAttackResult = new ArrayList<Integer>(Arrays.asList(ATTACK_NULL));
    private static ArrayList<Integer> lastBravoAttackResult = new ArrayList<Integer>(Arrays.asList(ATTACK_NULL));
    // 前ターンの移動ベクトル
    private static Point lastAlphaMoveVector = null;
    private static Point lastBravoMoveVector = null;

    // ログの表示可否
    private static boolean isVisibleLog = false;
    // 攻撃結果の複数可否
    private static boolean isAttackResultArray = false;
    // 攻撃結果の上書き可否
    private static boolean isEnemySecret = false;

    Board(boolean isVisibleLog, boolean isAttackResultArray, boolean isEnemySecret) {
        Initialize(isVisibleLog, isAttackResultArray, isEnemySecret);
    }

    /**
     * ボードの初期化
     * 
     * @param isVisibleLog
     * @param isAttackResultArray
     * @param isEnemySecret
     */
    public static void Initialize(boolean isVisibleLog, boolean isAttackResultArray, boolean isEnemySecret) {
        cells = new Cell[BOARD_SIZE][BOARD_SIZE];
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                SetCell(x, y, new Cell());
            }
        }

        turnCount = 0;

        alphaWin = false;
        bravoWin = false;

        lastAlphaAttackPoint = null;
        lastAlphaAttackResult = new ArrayList<Integer>(Arrays.asList(ATTACK_NULL));
        lastAlphaMoveVector = null;

        lastBravoAttackPoint = null;
        lastBravoAttackResult = new ArrayList<Integer>(Arrays.asList(ATTACK_NULL));
        lastBravoMoveVector = null;

        Board.isVisibleLog = isVisibleLog;
        Board.isAttackResultArray = isAttackResultArray;
        Board.isEnemySecret = isEnemySecret;
    }

    /**
     * 戦況のログ出力
     * 
     * @param alphaSide
     */
    public static void WriteBoard(boolean alphaSide) {
        WriteLogSide(alphaSide);
        WriteLogLine("盤面】");
        WriteLog("  ");
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            if (i != 0) {
                WriteLog("│");
            }
            WriteLog(String.valueOf(i + 1));
        }
        WriteLog("     ");
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            if (i != 0) {
                WriteLog("│");
            }
            WriteLog(String.valueOf(i + 1));
        }
        WriteLog("     ");
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            if (i != 0) {
                WriteLog("│");
            }
            WriteLog(String.valueOf(i + 1));
        }
        WriteLogLine("");
        String[] yStrings = new String[] { "A", "B", "C", "D", "E" };
        for (int y = 0; y < Board.BOARD_SIZE; y++) {
            WriteLog(yStrings[y] + "│");
            for (int x = 0; x < Board.BOARD_SIZE; x++) {
                if (Board.GetCell(x, y).GetHp(alphaSide) != -1) {
                    WriteLog(String.valueOf(Board.GetCell(x, y).GetHp(alphaSide)));
                } else {
                    WriteLog(" ");
                }
                WriteLog(" ");
            }
            WriteLog("  " + yStrings[y] + "│");
            for (int x = 0; x < Board.BOARD_SIZE; x++) {
                switch (Board.GetCell(x, y).GetValue(alphaSide, 0)) {
                    case -1:
                        WriteLog("- ");
                        break;
                    case -2:
                        WriteLog("X ");
                        break;
                    default:
                        WriteLog(String.valueOf(Board.GetCell(x, y).GetValue(alphaSide, 0)));
                        if (String.valueOf(Board.GetCell(x, y).GetValue(alphaSide, 0)).length() == 1) {
                            WriteLog(" ");
                        }
                        break;
                }
            }
            WriteLog("  " + yStrings[y] + "│");
            for (int x = 0; x < Board.BOARD_SIZE; x++) {
                if (Board.GetCell(x, y).GetEnableAttack(alphaSide)) {
                    WriteLog("*");
                } else {
                    WriteLog(" ");
                }
                WriteLog(" ");
            }
            WriteLogLine("");
        }
    }

    /**
     * ログ出力 (Side)
     * 
     * @param alphaSide
     */
    public static void WriteLogSide(boolean alphaSide) {
        if (isVisibleLog) {
            if (alphaSide) {
                System.out.print("【α");
            } else {
                System.out.print("【β");
            }
        }
    }

    /**
     * ログ出力 (WriteLine)
     * 
     * @param line
     */
    public static void WriteLogLine(String line) {
        if (isVisibleLog) {
            System.out.println(line);
        }
    }

    /**
     * ログ出力 (Write)
     * 
     * @param line
     */
    public static void WriteLog(String line) {
        if (isVisibleLog) {
            System.out.print(line);
        }
    }

    /**
     * コンソール出力 (無効ターン)
     */
    public static void WriteDisableTurn() {
        System.out.print(ConsoleColors.RED_BOLD);
        System.out.println("【警告】無効ターン");
        System.out.println(Logger.GetFileName() + " : " + Board.GetTurnCount());
        System.out.print(ConsoleColors.RESET);
    }

    /**
     * ゲーム継続の可否
     * 
     * @param interrupt
     * @return
     */
    public static boolean IsContinue(boolean interrupt) {
        if (!(!alphaWin && !bravoWin)) {
            return false;
        }
        int alphaCount = GetShipPoints(true).size();
        int bravoCount = GetShipPoints(false).size();
        int alphaSumHp = 0;
        for (Point point : GetShipPoints(true)) {
            alphaSumHp += GetCell(point).GetHp(true);
        }
        int bravoSumHp = 0;
        for (Point point : GetShipPoints(false)) {
            bravoSumHp += GetCell(point).GetHp(false);
        }
        if (!interrupt) {
            AddTurnCount();
        }

        WriteLog(ConsoleColors.RED);
        WriteLogLine("┌" + "─".repeat(2 + String.valueOf(Board.GetTurnCount()).length()) + "┬"
                + "─".repeat(5 + (String.valueOf(alphaCount) + String.valueOf(alphaSumHp)).length()) + "┬"
                + "─".repeat(5 + (String.valueOf(bravoCount) + String.valueOf(bravoSumHp)).length()) + "┐");
        WriteLogLine("│ " + Board.GetTurnCount() + " │ " + alphaCount + " (" + alphaSumHp + ") │ " + bravoCount
                + " (" + bravoSumHp + ") │");
        WriteLogLine("└" + "─".repeat(2 + String.valueOf(Board.GetTurnCount()).length()) + "┴"
                + "─".repeat(5 + (String.valueOf(alphaCount) + String.valueOf(alphaSumHp)).length()) + "┴"
                + "─".repeat(5 + (String.valueOf(bravoCount) + String.valueOf(bravoSumHp)).length()) + "┘");
        System.out.print(ConsoleColors.RESET);
        // if (!isEnemySecret) {
        // WriteLogLine("α残機 = " + alphaCount + " (総HP : " + alphaSumHp + ")");
        // WriteLogLine("β残機 = " + bravoCount + " (総HP : " + bravoSumHp + ")");
        // }
        if (alphaCount == 0 && !isEnemySecret) {
            WriteLogLine("αが全滅しました");
            WriteLogLine("βの勝利です");
            alphaWin = false;
            bravoWin = true;
            return false;
        }
        if (bravoCount == 0 && !isEnemySecret) {
            WriteLogLine("βが全滅しました");
            WriteLogLine("αの勝利です");
            alphaWin = true;
            bravoWin = false;
            return false;
        }
        if (interrupt) {
            if (alphaSumHp > bravoSumHp) {
                WriteLogLine("αの勝利です");
                alphaWin = true;
                bravoWin = false;
            } else if (bravoSumHp > alphaSumHp) {
                WriteLogLine("βの勝利です");
                alphaWin = false;
                bravoWin = true;
            } else {
                WriteLogLine("引き分けです");
                alphaWin = true;
                bravoWin = true;
            }
            return false;
        }
        return true;
    }

    /**
     * ゲームの中断
     */
    public static void Interrupt() {
        int alphaCount = GetShipPoints(true).size();
        int bravoCount = GetShipPoints(false).size();
        int alphaSumHp = 0;
        for (Point point : GetShipPoints(true)) {
            alphaSumHp += GetCell(point).GetHp(true);
        }
        int bravoSumHp = 0;
        for (Point point : GetShipPoints(false)) {
            bravoSumHp += GetCell(point).GetHp(false);
        }
        if (alphaCount == 0 && !isEnemySecret) {
            WriteLogLine("αが全滅しました");
            WriteLogLine("βの勝利です");
            alphaWin = false;
            bravoWin = true;
        }
        if (bravoCount == 0 && !isEnemySecret) {
            WriteLogLine("βが全滅しました");
            WriteLogLine("αの勝利です");
            alphaWin = true;
            bravoWin = false;
        }
        if (alphaWin == false && bravoWin == false) {
            if (alphaSumHp > bravoSumHp) {
                WriteLogLine("αの勝利です");
                alphaWin = true;
                bravoWin = false;
            } else if (bravoSumHp > alphaSumHp) {
                WriteLogLine("βの勝利です");
                alphaWin = false;
                bravoWin = true;
            } else {
                WriteLogLine("引き分けです");
                alphaWin = true;
                bravoWin = true;
            }
        }
    }

    /**
     * α勝利フラッグを取得
     * 
     * @return
     */
    public static boolean GetAlphaWin() {
        return alphaWin;
    }

    /**
     * β勝利フラッグを取得
     * 
     * @return
     */
    public static boolean GetBravoWin() {
        return bravoWin;
    }

    /**
     * ターン数の加算
     */
    public static void AddTurnCount() {
        turnCount++;
    }

    /**
     * ターン数を取得
     * 
     * @return
     */
    public static int GetTurnCount() {
        return turnCount;
    }

    /**
     * セルを取得
     * 
     * @param point
     * @return
     */
    public static Cell GetCell(Point point) {
        return cells[point.x][point.y];
    }

    /**
     * セルを取得
     * 
     * @param x
     * @param y
     * @return
     */
    public static Cell GetCell(int x, int y) {
        return cells[x][y];
    }

    /**
     * セルを設定
     * 
     * @param point
     * @param cell
     */
    public static void SetCell(Point point, Cell cell) {
        cells[point.x][point.y] = cell;
    }

    /**
     * セルを設定
     * 
     * @param x
     * @param y
     * @param cell
     */
    public static void SetCell(int x, int y, Cell cell) {
        cells[x][y] = cell;
    }

    /**
     * 前ターンの攻撃有無を取得
     * 
     * @param alphaSide
     * @return
     */
    public static boolean IsLastAttack(boolean alphaSide) {
        if (alphaSide) {
            return (lastAlphaAttackPoint != null);
        } else {
            return (lastBravoAttackPoint != null);
        }
    }

    /**
     * 前ターンの移動有無を取得
     * 
     * @param alphaSide
     * @return
     */
    public static boolean IsLastMove(boolean alphaSide) {
        if (alphaSide) {
            return (lastAlphaMoveVector != null);
        } else {
            return (lastBravoMoveVector != null);
        }
    }

    /**
     * 前ターンの攻撃ポイントを取得
     * 
     * @param alphaSide
     * @return
     */
    public static Point GetLastAttackPoint(boolean alphaSide) {
        if (alphaSide) {
            return lastAlphaAttackPoint;
        } else {
            return lastBravoAttackPoint;
        }
    }

    /**
     * 前ターンの攻撃結果を取得
     * 
     * @param alphaSide
     * @return
     */
    public static ArrayList<Integer> GetLastAttackResult(boolean alphaSide) {
        if (alphaSide) {
            return lastAlphaAttackResult;
        } else {
            return lastBravoAttackResult;
        }
    }

    /**
     * 前ターンの移動ベクトルを取得
     * 
     * @param alphaSide
     * @return
     */
    public static Point GetLastMoveVector(boolean alphaSide) {
        if (alphaSide) {
            return lastAlphaMoveVector;
        } else {
            return lastBravoMoveVector;
        }
    }

    /**
     * 評価値の初期化
     * 
     * @param alphaSide
     * @param layer
     */
    public static void InitializeValues(boolean alphaSide, int layer) {
        for (int x = 0; x < Board.BOARD_SIZE; x++) {
            for (int y = 0; y < Board.BOARD_SIZE; y++) {
                Board.GetCell(x, y).SetValue(alphaSide, layer, 0);
            }
        }
    }

    /**
     * 評価値の正規化
     * 
     * @param alphaSide
     * @param layer
     */
    public static void NormalizeValues(boolean alphaSide, int layer) {
        for (int x = 0; x < Board.BOARD_SIZE; x++) {
            for (int y = 0; y < Board.BOARD_SIZE; y++) {
                int value = 3;
                if (x != 0 && x != Board.BOARD_SIZE - 1) {
                    value += 2;
                }
                if (y != 0 && y != Board.BOARD_SIZE - 1) {
                    value += 2;
                }
                Board.GetCell(x, y).SetValue(alphaSide, layer, value);
            }
        }
    }

    /**
     * 戦艦リストを取得
     * 
     * @param alphaSide
     * @return
     */
    public static ArrayList<Point> GetShipPoints(boolean alphaSide) {
        ArrayList<Point> points = new ArrayList<Point>();
        for (int x = 0; x < Board.BOARD_SIZE; x++) {
            for (int y = 0; y < Board.BOARD_SIZE; y++) {
                if (Board.GetCell(x, y).IsAlive(alphaSide)) {
                    points.add(new Point(x, y));
                }
            }
        }
        return points;
    }

    /**
     * ランダムにポイントを取得
     * 
     * @param points
     * @return
     */
    public static Point GetRandomPoint(ArrayList<Point> points) {
        Random random = new Random();
        return points.get(random.nextInt(points.size()));
    }

    /**
     * ランダムな4箇所に戦艦を設定
     * 
     * @param alphaSide
     * @param roundTrim
     * @param cornerTrim
     */
    public static void SetRandom4Points(boolean alphaSide, boolean roundTrim, boolean cornerTrim) {
        for (Point point : GetRandomPoints(4, roundTrim, cornerTrim)) {
            GetCell(point).SetHp(alphaSide, 3);
        }
    }

    /**
     * 指定した4箇所に戦艦を設定
     * 
     * @param alphaSide
     * @param type
     */
    public static void SetType4Points(boolean alphaSide, int type) {
        switch (type) {
            case 0:
                Board.GetCell(new Point("B2")).SetHp(alphaSide, 3);
                Board.GetCell(new Point("B4")).SetHp(alphaSide, 3);
                Board.GetCell(new Point("D2")).SetHp(alphaSide, 3);
                Board.GetCell(new Point("D4")).SetHp(alphaSide, 3);
                break;
            case 1:
                Board.GetCell(new Point("A3")).SetHp(alphaSide, 3);
                Board.GetCell(new Point("C1")).SetHp(alphaSide, 3);
                Board.GetCell(new Point("C5")).SetHp(alphaSide, 3);
                Board.GetCell(new Point("E3")).SetHp(alphaSide, 3);
                break;
            case 2:
                Board.GetCell(new Point("B3")).SetHp(alphaSide, 3);
                Board.GetCell(new Point("C2")).SetHp(alphaSide, 3);
                Board.GetCell(new Point("C4")).SetHp(alphaSide, 3);
                Board.GetCell(new Point("D3")).SetHp(alphaSide, 3);
                break;
            case 3:
                Board.GetCell(new Point("A2")).SetHp(alphaSide, 3);
                Board.GetCell(new Point("D1")).SetHp(alphaSide, 3);
                Board.GetCell(new Point("E4")).SetHp(alphaSide, 3);
                Board.GetCell(new Point("B5")).SetHp(alphaSide, 3);
        }
    }

    /**
     * ランダムなポイントリストを取得
     * 
     * @param count
     * @param roundTrim
     * @param cornerTrim
     * @return
     */
    public static ArrayList<Point> GetRandomPoints(int count, boolean roundTrim,
            boolean cornerTrim) {
        ArrayList<Point> points = new ArrayList<Point>();
        for (int i = 0; i < count; i++) {
            LOOP: while (true) {
                int x = (int) (Math.random() * Board.BOARD_SIZE);
                int y = (int) (Math.random() * Board.BOARD_SIZE);
                for (Point point : points) {
                    if (point.equals(new Point(x, y))) {
                        continue LOOP;
                    }
                }
                if (roundTrim) {
                    for (Point roundPoint : Board.GetRoundPoints(new Point(x, y))) {
                        for (Point point : points) {
                            if (point.equals(roundPoint)) {
                                continue LOOP;
                            }
                        }
                    }
                }
                if (cornerTrim) {
                    if ((x == 0 || x == Board.BOARD_SIZE - 1) && (y == 0 || y == Board.BOARD_SIZE - 1)) {
                        continue LOOP;
                    }
                }
                points.add(new Point(x, y));
                break;
            }
        }
        return points;
    }

    /**
     * 評価値が最大であるポイントリストを取得
     * 
     * @param alphaSide
     * @param isEnableAttack
     * @param layer
     * @return
     */
    public static ArrayList<Point> GetMaxValuePoints(boolean alphaSide, boolean isEnableAttack, int layer) {
        HashMap<Point, Integer> pointsValue = new HashMap<Point, Integer>();
        for (int x = 0; x < Board.BOARD_SIZE; x++) {
            for (int y = 0; y < Board.BOARD_SIZE; y++) {
                if ((isEnableAttack && Board.IsEnableAttackPoint(alphaSide, new Point(x, y))) || !isEnableAttack) {
                    pointsValue.put(new Point(x, y), Board.GetCell(x, y).GetValue(alphaSide, layer));
                }
            }
        }
        ArrayList<Point> points = new ArrayList<Point>();
        if (pointsValue.keySet().size() != 0) {
            int maxValue = Collections.max(pointsValue.values());
            for (Map.Entry<Point, Integer> pointValue : pointsValue.entrySet()) {
                if (pointValue.getValue() == maxValue) {
                    points.add(pointValue.getKey());
                }
            }
        }
        return points;
    }

    /**
     * 評価値が最小であるポイントリストを取得
     * 
     * @param alphaSide
     * @param isEnableAttack
     * @param layer
     * @return
     */
    public static ArrayList<Point> GetMinValuePoints(boolean alphaSide, boolean isEnableAttack, int layer) {
        HashMap<Point, Integer> pointsValue = new HashMap<Point, Integer>();
        for (int x = 0; x < Board.BOARD_SIZE; x++) {
            for (int y = 0; y < Board.BOARD_SIZE; y++) {
                if ((isEnableAttack && Board.IsEnableAttackPoint(alphaSide, new Point(x, y))) || !isEnableAttack) {
                    pointsValue.put(new Point(x, y), Board.GetCell(x, y).GetValue(alphaSide, layer));
                }
            }
        }
        ArrayList<Point> points = new ArrayList<Point>();
        if (pointsValue.keySet().size() != 0) {
            int minValue = Collections.min(pointsValue.values());
            if (minValue < -1) {
                minValue = 0;
            }
            for (Map.Entry<Point, Integer> pointValue : pointsValue.entrySet()) {
                if (pointValue.getValue() == minValue) {
                    points.add(pointValue.getKey());
                }
            }
        }
        return points;
    }

    /**
     * 最も近い戦艦リストを取得
     * 
     * @param alphaSide
     * @param point
     * @return
     */
    public static ArrayList<Point> GetShortPoints(boolean alphaSide, Point point) {
        HashMap<Point, Integer> pointsDistance = new HashMap<Point, Integer>();
        for (Point shipPoint : GetShipPoints(alphaSide)) {
            pointsDistance.put(shipPoint, GetPointDistance(shipPoint, point));
        }
        int shortDistance = Collections.min(pointsDistance.values());
        ArrayList<Point> points = new ArrayList<Point>();
        for (Map.Entry<Point, Integer> pointDistance : pointsDistance.entrySet()) {
            if (pointDistance.getValue() == shortDistance) {
                points.add(pointDistance.getKey());
            }
        }
        return points;
    }

    /**
     * 周囲のポイントリストを取得
     * 
     * @param point
     * @return
     */
    public static ArrayList<Point> GetRoundPoints(Point point) {
        ArrayList<Point> points = new ArrayList<Point>();
        if (point.x > 0) {
            points.add(new Point(point.x - 1, point.y));
        }
        if (point.x < Board.BOARD_SIZE - 1) {
            points.add(new Point(point.x + 1, point.y));
        }
        if (point.y > 0) {
            points.add(new Point(point.x, point.y - 1));
        }
        if (point.y < Board.BOARD_SIZE - 1) {
            points.add(new Point(point.x, point.y + 1));
        }
        if (point.x > 0 && point.y > 0) {
            points.add(new Point(point.x - 1, point.y - 1));
        }
        if (point.x > 0 && point.y < Board.BOARD_SIZE - 1) {
            points.add(new Point(point.x - 1, point.y + 1));
        }
        if (point.x < Board.BOARD_SIZE - 1 && point.y > 0) {
            points.add(new Point(point.x + 1, point.y - 1));
        }
        if (point.x < Board.BOARD_SIZE - 1 && point.y < Board.BOARD_SIZE - 1) {
            points.add(new Point(point.x + 1, point.y + 1));
        }
        return points;
    }

    /**
     * 十字方向のポイントリストを取得
     * 
     * @param point
     * @param minLength
     * @param maxLength
     * @return
     */
    public static ArrayList<Point> GetCrossPoints(Point point, int minLength, int maxLength) {
        ArrayList<Point> points = new ArrayList<Point>();
        for (int i = minLength; i <= maxLength; i++) {
            if (point.x > i - 1) {
                points.add(new Point(point.x - i, point.y));
            }
            if (point.x < Board.BOARD_SIZE - i) {
                points.add(new Point(point.x + i, point.y));
            }
            if (point.y > i - 1) {
                points.add(new Point(point.x, point.y - i));
            }
            if (point.y < Board.BOARD_SIZE - i) {
                points.add(new Point(point.x, point.y + i));
            }
        }
        return points;
    }

    /**
     * ポイント間の距離を取得
     * 
     * @param aPoint
     * @param bPoint
     * @return
     */
    public static int GetPointDistance(Point aPoint, Point bPoint) {
        return (Math.abs(aPoint.x - bPoint.x) + Math.abs(aPoint.y - bPoint.y));
    }

    /**
     * 評価値リストを取得
     * 
     * @param alphaSide
     * @param points
     * @param layer
     * @param filter
     * @return
     */
    public static HashMap<Point, Integer> GetPointValues(boolean alphaSide, ArrayList<Point> points, int layer,
            int filter) {
        if (points == null) {
            points = new ArrayList<Point>();
            for (int x = 0; x < Board.BOARD_SIZE; x++) {
                for (int y = 0; y < Board.BOARD_SIZE; y++) {
                    points.add(new Point(x, y));
                }
            }
        }
        HashMap<Point, Integer> tempPointsValue = new HashMap<Point, Integer>();
        for (Point point : points) {
            tempPointsValue.put(point, Board.GetCell(point).GetValue(alphaSide, layer));
        }
        if (filter == 0) {
            return tempPointsValue;
        } else {
            int value = 0;
            if (filter == 1) {
                value = Collections.max(tempPointsValue.values());
            } else if (filter == -1) {
                value = Collections.min(tempPointsValue.values());
                if (value < -1) {
                    value = 0;
                }
            }
            HashMap<Point, Integer> pointsValue = new HashMap<Point, Integer>();
            for (Map.Entry<Point, Integer> pointValue : tempPointsValue.entrySet()) {
                if (pointValue.getValue() == value) {
                    pointsValue.put(pointValue.getKey(), pointValue.getValue());
                }
            }
            return pointsValue;
        }
    }

    /**
     * 移動可否を取得 (ポイント)
     * 
     * @param alphaSide
     * @param oldPoint
     * @param newPoint
     * @return
     */
    public static boolean IsMoveEnablePoint(boolean alphaSide, Point oldPoint, Point newPoint) {
        if (!oldPoint.IsRange() || !newPoint.IsRange()) {
            return false;
        }
        if (Board.GetCell(oldPoint).IsAlive(alphaSide) && Board.GetCell(newPoint).IsEmpty(alphaSide)) {
            if (GetPointDistance(oldPoint, newPoint) == 1) {
                return true;
            } else if (Math.abs(oldPoint.x - newPoint.x) == 2 && oldPoint.y == newPoint.y) {
                return (Board.GetCell(new Point((oldPoint.x + newPoint.x) / 2, oldPoint.y)).IsEmpty(alphaSide));
            } else if (Math.abs(oldPoint.y - newPoint.y) == 2 && oldPoint.x == newPoint.x) {
                return (Board.GetCell(new Point(oldPoint.x, (oldPoint.y + newPoint.y) / 2)).IsEmpty(alphaSide));
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 移動可否を取得 (ベクトル)
     * 
     * @param alphaSide
     * @param oldPoint
     * @param vectorPoint
     * @return
     */
    public static boolean IsMoveEnableVector(boolean alphaSide, Point oldPoint, Point vectorPoint) {
        return IsMoveEnablePoint(alphaSide, oldPoint, oldPoint.Plus(vectorPoint));
    }

    /**
     * 移動可能ポイントリストを取得 (ポイント)
     * 
     * @param alphaSide
     * @param oldPoint
     * @param newPoints
     * @return
     */
    public static ArrayList<Point> GetFilterMoveEnablePoints(boolean alphaSide, Point oldPoint,
            ArrayList<Point> newPoints) {
        ArrayList<Point> tempPoints = new ArrayList<Point>();
        for (Point point : newPoints) {
            if (Board.IsMoveEnablePoint(alphaSide, oldPoint, point)) {
                tempPoints.add(point);
            }
        }
        return tempPoints;
    }

    /**
     * 移動可否ポイントリストを取得 (ベクトル)
     * 
     * @param alphaSide
     * @param oldPoint
     * @param newVectors
     * @return
     */
    public static ArrayList<Point> GetFilterMoveEnableVectors(boolean alphaSide, Point oldPoint,
            ArrayList<Point> newVectors) {
        ArrayList<Point> tempVectors = new ArrayList<Point>();
        for (Point vector : newVectors) {
            if (Board.IsMoveEnableVector(alphaSide, oldPoint, vector)) {
                tempVectors.add(vector);
            }
        }
        return tempVectors;
    }

    /**
     * 移動 (ポイント)
     * 
     * @param alphaSide
     * @param oldPoint
     * @param newPoint
     * @return
     */
    public static boolean MovePoint(boolean alphaSide, Point oldPoint, Point newPoint) {
        WriteLogSide(alphaSide);
        if (IsMoveEnablePoint(alphaSide, oldPoint, newPoint)) {
            WriteLogLine("移動】 " + oldPoint.toPointFormatString() + " → " + newPoint.toPointFormatString());
            Board.GetCell(newPoint).SetHp(alphaSide, Board.GetCell(oldPoint).GetHp(alphaSide));
            Board.GetCell(oldPoint).SetHp(alphaSide, -1);
            if (alphaSide) {
                lastAlphaAttackPoint = null;
                lastAlphaAttackResult = new ArrayList<Integer>(Arrays.asList(ATTACK_NULL));
                lastAlphaMoveVector = newPoint.Minus(oldPoint);
            } else {
                lastBravoAttackPoint = null;
                lastBravoAttackResult = new ArrayList<Integer>(Arrays.asList(ATTACK_NULL));
                lastBravoMoveVector = newPoint.Minus(oldPoint);
            }
            Logger.AddLogger(alphaSide);
            return true;
        } else {
            System.out.print(ConsoleColors.RED_BOLD);
            System.out.println("【警告】移動拒否");
            System.out.println(Logger.GetFileName() + " : " + Board.GetTurnCount());
            System.out.print(ConsoleColors.RESET);
            WriteLogLine("移動】拒否されました");
            Logger.AddLogger(alphaSide);
            return false;
        }
    }

    /**
     * 移動 (ベクトル)
     * 
     * @param alphaSide
     * @param oldPoint
     * @param vectorPoint
     * @return
     */
    public static boolean MoveVector(boolean alphaSide, Point oldPoint, Point vectorPoint) {
        return MovePoint(alphaSide, oldPoint, oldPoint.Plus(vectorPoint));
    }

    /**
     * 強制移動 (ベクトル)
     * 
     * @param alphaSide
     * @param vectorPoint
     */
    public static void MoveVectorForce(boolean alphaSide, Point vectorPoint) {
        WriteLogSide(alphaSide);
        WriteLogLine("移動】 " + vectorPoint.toVectorFormaString());
        if (alphaSide) {
            lastAlphaAttackPoint = null;
            lastAlphaAttackResult = new ArrayList<Integer>(Arrays.asList(ATTACK_NULL));
            lastAlphaMoveVector = vectorPoint;
        } else {
            lastBravoAttackPoint = null;
            lastBravoAttackResult = new ArrayList<Integer>(Arrays.asList(ATTACK_NULL));
            lastBravoMoveVector = vectorPoint;
        }
        Logger.AddLogger(alphaSide);
    }

    /**
     * 攻撃可否ポイントを検索
     * 
     * @param alphaSide
     */
    public static void SearchEnableAttackPoints(boolean alphaSide) {
        for (int x = 0; x < Board.BOARD_SIZE; x++) {
            for (int y = 0; y < Board.BOARD_SIZE; y++) {
                Board.GetCell(x, y).SetEnableAttack(alphaSide, false);
            }
        }
        for (Point shipPoint : Board.GetShipPoints(alphaSide)) {
            for (Point point : GetRoundPoints(shipPoint)) {
                Board.GetCell(point).SetEnableAttack(alphaSide, true);
            }
        }
        for (Point shipPoint : Board.GetShipPoints(!alphaSide)) {
            if (Board.GetCell(shipPoint).GetHp(!alphaSide) == 0) {
                Board.GetCell(shipPoint).SetEnableAttack(alphaSide, false);
            }
        }
    }

    /**
     * 攻撃可能ポイントリストを取得
     * 
     * @param alphaSide
     * @return
     */
    public static ArrayList<Point> GetEnableAttackPoints(boolean alphaSide) {
        ArrayList<Point> points = new ArrayList<Point>();
        for (int x = 0; x < Board.BOARD_SIZE; x++) {
            for (int y = 0; y < Board.BOARD_SIZE; y++) {
                if (Board.GetCell(x, y).GetEnableAttack(alphaSide)) {
                    points.add(new Point(x, y));
                }
            }
        }
        return points;
    }

    /**
     * 攻撃可否を取得
     * 
     * @param alphaSide
     * @param point
     * @return
     */
    public static boolean IsEnableAttackPoint(boolean alphaSide, Point point) {
        if (!point.IsRange()) {
            return false;
        }
        SearchEnableAttackPoints(alphaSide);
        return Board.GetCell(point).GetEnableAttack(alphaSide);
    }

    /**
     * 攻撃可能ポイントリストを取得
     * 
     * @param alphaSide
     * @param points
     * @return
     */
    public static ArrayList<Point> GetFilterEnableAttackPoints(boolean alphaSide, ArrayList<Point> points) {
        ArrayList<Point> tempPoints = new ArrayList<Point>();
        for (Point point : points) {
            if (Board.IsEnableAttackPoint(alphaSide, point)) {
                tempPoints.add(point);
            }
        }
        return tempPoints;
    }

    /**
     * 攻撃
     * 
     * @param alphaSide
     * @param point
     * @param judgeResult
     * @return
     */
    public static boolean AttackPoint(boolean alphaSide, Point point, boolean judgeResult) {
        WriteLogSide(alphaSide);
        if (IsEnableAttackPoint(alphaSide, point)) {
            ArrayList<Integer> attackResult = new ArrayList<Integer>();
            WriteLogLine("攻撃】" + point.toPointFormatString());
            if (judgeResult) {
                if (Board.GetCell(point).IsAlive(!alphaSide)) {
                    Board.GetCell(point).SetHp(!alphaSide, Board.GetCell(point).GetHp(!alphaSide) - 1);
                    // 命中！
                    attackResult = new ArrayList<Integer>(Arrays.asList(RESULT_HIT));
                    if (Board.GetCell(point).GetHp(!alphaSide) == 0) {
                        // 撃沈！
                        attackResult = new ArrayList<Integer>(Arrays.asList(RESULT_SINK));
                    }
                } else {
                    // ハズレ！
                    attackResult = new ArrayList<Integer>(Arrays.asList(RESULT_NOHIT));
                }
                for (Point roundPoint : GetRoundPoints(point)) {
                    if (Board.GetCell(roundPoint).IsAlive(!alphaSide)) {
                        // 波高し！
                        if (isAttackResultArray) {
                            attackResult.add(RESULT_NEAR);
                        } else {
                            if (attackResult.contains(RESULT_NOHIT)) {
                                attackResult = new ArrayList<Integer>(Arrays.asList(RESULT_NEAR));
                            }
                        }
                    }
                }
            }
            Board.WriteLog(ConsoleColors.BLUE_BOLD);
            if (attackResult.contains(RESULT_SINK)) {
                WriteLogLine("<撃沈！>");
            }
            if (attackResult.contains(RESULT_HIT)) {
                WriteLogLine("<命中！>");
            }
            if (attackResult.contains(RESULT_NEAR)) {
                WriteLogLine("<波高し！>");
            }
            if (attackResult.contains(RESULT_NOHIT)) {
                WriteLogLine("<ハズレ！>");
            }
            Board.WriteLog(ConsoleColors.RESET);
            if (alphaSide) {
                lastAlphaAttackPoint = point;
                lastAlphaAttackResult = attackResult;
                lastAlphaMoveVector = null;
            } else {
                lastBravoAttackPoint = point;
                lastBravoAttackResult = attackResult;
                lastBravoMoveVector = null;
            }
            Logger.AddLogger(alphaSide);
            return true;
        } else {
            System.out.print(ConsoleColors.RED_BOLD);
            System.out.println("【警告】攻撃拒否");
            System.out.println(Logger.GetFileName() + " : " + Board.GetTurnCount());
            System.out.print(ConsoleColors.RESET);
            WriteLogLine("攻撃】 拒否されました");
            if (alphaSide) {
                lastAlphaAttackPoint = null;
                lastAlphaAttackResult = new ArrayList<Integer>(Arrays.asList(ATTACK_NULL));
                lastAlphaMoveVector = null;
            } else {
                lastBravoAttackPoint = null;
                lastBravoAttackResult = new ArrayList<Integer>(Arrays.asList(ATTACK_NULL));
                lastBravoMoveVector = null;
            }
            Logger.AddLogger(alphaSide);
            return false;
        }
    }

    /**
     * 攻撃結果を上書き
     * 
     * @param alphaSide
     * @param attackResult
     */
    public static void AttackResultTransfer(boolean alphaSide, ArrayList<Integer> attackResult) {
        Board.WriteLog(ConsoleColors.BLUE_BOLD);
        for (int i = 0; i < attackResult.size(); i++) {
            switch (attackResult.get(i)) {
                case RESULT_SINK:
                    WriteLogLine("<撃沈！>");
                    Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetHp(!alphaSide, 0);
                    break;
                case RESULT_HIT:
                    WriteLogLine("<命中！>");
                    break;
                case RESULT_NEAR:
                    WriteLogLine("<波高し！>");
                    break;
                case RESULT_NOHIT:
                    WriteLogLine("<ハズレ！>");
                    break;
            }
        }
        Board.WriteLog(ConsoleColors.RESET);
        if (alphaSide) {
            lastAlphaAttackResult = attackResult;
        } else {
            lastBravoAttackResult = attackResult;
        }
    }

    /**
     * 強制攻撃
     * 
     * @param alphaSide
     * @param point
     */
    public static void AttackPointForce(boolean alphaSide, Point point) {
        WriteLogSide(alphaSide);
        ArrayList<Integer> attackResult = new ArrayList<Integer>();
        WriteLogLine("攻撃】" + point.toPointFormatString());
        if (Board.GetCell(point).IsAlive(!alphaSide)) {
            Board.GetCell(point).SetHp(!alphaSide, Board.GetCell(point).GetHp(!alphaSide) - 1);
            // 命中！
            attackResult = new ArrayList<Integer>(Arrays.asList(RESULT_HIT));
            if (Board.GetCell(point).GetHp(!alphaSide) == 0) {
                // 撃沈！
                attackResult = new ArrayList<Integer>(Arrays.asList(RESULT_SINK));
            }
        } else {
            // ハズレ！
            attackResult = new ArrayList<Integer>(Arrays.asList(RESULT_NOHIT));
        }
        for (Point roundPoint : GetRoundPoints(point)) {
            if (Board.GetCell(roundPoint).IsAlive(!alphaSide)) {
                // 波高し！
                if (isAttackResultArray) {
                    attackResult.add(RESULT_NEAR);
                } else {
                    if (attackResult.contains(RESULT_NOHIT)) {
                        attackResult = new ArrayList<Integer>(Arrays.asList(RESULT_NEAR));
                    }
                }
            }
        }
        Board.WriteLog(ConsoleColors.BLUE_BOLD);
        if (attackResult.contains(RESULT_SINK)) {
            WriteLogLine("<撃沈！>");
        }
        if (attackResult.contains(RESULT_HIT)) {
            WriteLogLine("<命中！>");
        }
        if (attackResult.contains(RESULT_NEAR)) {
            WriteLogLine("<波高し！>");
        }
        if (attackResult.contains(RESULT_NOHIT)) {
            WriteLogLine("<ハズレ！>");
        }
        Board.WriteLog(ConsoleColors.RESET);
        if (alphaSide) {
            lastAlphaAttackPoint = point;
            lastAlphaAttackResult = attackResult;
            lastAlphaMoveVector = null;
        } else {
            lastBravoAttackPoint = point;
            lastBravoAttackResult = attackResult;
            lastBravoMoveVector = null;
        }
        Logger.AddLogger(alphaSide);
    }
}

class Interface {
    // BoardにおけるalphaSide
    public final boolean alphaSide;
    // 攻撃結果の上書き可否
    public final boolean isEnemySecret;

    // 戦艦数
    public int allyCount;
    public int enemyCount;
    // 合計HP
    public int allySumHp;
    public int enemySumHp;

    /**
     * 前ターンの敵攻撃有無
     * 
     * @return
     */
    public boolean IsEnemyLastAttack() {
        return Board.IsLastAttack(!alphaSide);
    }

    /**
     * 前ターンの味方攻撃有無
     * 
     * @return
     */
    public boolean IsAllyLastAttack() {
        return Board.IsLastAttack(alphaSide);
    }

    /**
     * 前ターンの敵移動有無
     * 
     * @return
     */
    public boolean IsEnemyLastMove() {
        return Board.IsLastMove(!alphaSide);
    }

    /**
     * 前ターンの味方移動有無
     * 
     * @return
     */
    public boolean IsAllyLastMove() {
        return Board.IsLastMove(alphaSide);
    }

    /**
     * 前ターンの敵攻撃結果
     * 
     * @return
     */
    public ArrayList<Integer> EnemyLastAttackResult() {
        return Board.GetLastAttackResult(!alphaSide);
    }

    /**
     * 前ターンの味方攻撃結果
     * 
     * @return
     */
    public ArrayList<Integer> AllyLastAttackResult() {
        return Board.GetLastAttackResult(alphaSide);
    }

    /**
     * 前ターンの敵攻撃ポイント
     * 
     * @return
     */
    public Point EnemyLastAttackPoint() {
        return Board.GetLastAttackPoint(!alphaSide);
    }

    /**
     * 前ターンの味方攻撃ポイント
     * 
     * @return
     */
    public Point AllyLastAttackPoint() {
        return Board.GetLastAttackPoint(alphaSide);
    }

    /**
     * 前ターンの敵移動ベクトル
     * 
     * @return
     */
    public Point EnemyLastMoveVector() {
        return Board.GetLastMoveVector(!alphaSide);
    }

    /**
     * 前ターンの味方移動ベクトル
     * 
     * @return
     */
    public Point AllyLastMoveVector() {
        return Board.GetLastMoveVector(alphaSide);
    }

    Interface(boolean alphaSide, boolean isEnemySecret) {
        this.alphaSide = alphaSide;
        this.isEnemySecret = isEnemySecret;
        allyCount = 4;
        allySumHp = 12;
        enemyCount = 4;
        enemySumHp = 12;
    }

    /**
     * パラメータを設定 (復元用)
     * 
     * @param parameters
     */
    public void SetCountSumHp(int[] parameters) {
        allyCount = parameters[0];
        allySumHp = parameters[1];
        enemyCount = parameters[2];
        enemySumHp = parameters[3];
    }

    /**
     * パラメータを取得
     * 
     * @return
     */
    public int[] GetCountSumHp() {
        return new int[] { allyCount, allySumHp, enemyCount, enemySumHp };
    }

    /**
     * 移動
     * 
     * @param oldPoint
     * @param newPoint
     */
    public void DoMove(Point oldPoint, Point newPoint) {
        Board.WriteBoard(alphaSide);
        Board.WriteLogLine("allyCount = " + allyCount + ", allySumHp = " + allySumHp + ", enemyCount = " + enemyCount
                + ", enemySumHp = " + enemySumHp);
        Board.WriteLog(ConsoleColors.BLUE_BOLD);
        Board.WriteLogLine("<" + newPoint.Minus(oldPoint).toVectorFormaString() + " に移動！>");
        Board.WriteLog(ConsoleColors.RESET);
        Board.MovePoint(alphaSide, oldPoint, newPoint);
    }

    /**
     * 攻撃
     * 
     * @param point
     */
    public void DoAttack(Point point) {
        Board.WriteBoard(alphaSide);
        Board.WriteLogLine("allyCount = " + allyCount + ", allySumHp = " + allySumHp + ", enemyCount = " + enemyCount
                + ", enemySumHp = " + enemySumHp);
        Board.WriteLog(ConsoleColors.BLUE_BOLD);
        Board.WriteLogLine("<" + point.toPointFormatString() + " に魚雷発射！>");
        Board.WriteLog(ConsoleColors.RESET);
        Board.AttackPoint(alphaSide, point, !isEnemySecret);
    }

    // /**
    // * 強制移動
    // *
    // * @param vectorPoint
    // */
    // public void DoMoveForce(Point vectorPoint) {
    // Board.WriteBoard(alphaSide);
    // Board.WriteLogLine("allyCount = " + allyCount + ", allySumHp = " + allySumHp
    // + ", enemyCount = " + enemyCount
    // + ", enemySumHp = " + enemySumHp);
    // Board.WriteLog(ConsoleColors.BLUE_BOLD);
    // Board.WriteLogLine("<" + vectorPoint.toVectorFormaString() + " に移動！>");
    // Board.WriteLog(ConsoleColors.RESET);
    // Board.MoveVectorForce(alphaSide, vectorPoint);
    // }

    // /**
    // * 強制攻撃
    // *
    // * @param point
    // */
    // public void DoAttackForce(Point point) {
    // Board.WriteBoard(alphaSide);
    // Board.WriteLogLine("allyCount = " + allyCount + ", allySumHp = " + allySumHp
    // + ", enemyCount = " + enemyCount
    // + ", enemySumHp = " + enemySumHp);
    // Board.WriteLog(ConsoleColors.BLUE_BOLD);
    // Board.WriteLogLine("<" + point.toPointFormatString() + " に魚雷発射！>");
    // Board.WriteLog(ConsoleColors.RESET);
    // Board.AttackPointForce(alphaSide, point);
    // }
}

class Logger {
    // JSONObject
    private static JSONObject jsonObject;
    // ファイル名 (拡張子なし)
    private static String fileName;
    // 自動保存の有無
    private static boolean autoSave;

    /**
     * ロガーを作成
     * 
     * @param fileName
     * @param autoSave
     */
    public static void CreateLogger(String fileName, boolean autoSave) {
        jsonObject = new JSONObject();
        Logger.fileName = fileName;
        Logger.autoSave = autoSave;
    }

    /**
     * ファイル名を取得
     * 
     * @return
     */
    public static String GetFileName() {
        return fileName;
    }

    /**
     * ロガーに戦況を追加
     * 
     * @param alphaSide
     */
    public static void AddLogger(boolean alphaSide) {
        JSONObject childJsonObject = new JSONObject();
        childJsonObject.put("alphaSide", alphaSide);
        JSONObject alphaJsonObject = new JSONObject();
        alphaJsonObject.put("hp", GetHps(true));
        alphaJsonObject.put("values", GetValues(true));
        alphaJsonObject.put("enableAttack", GetEnableAttacks(true));
        if (Board.GetLastAttackPoint(true) != null) {
            alphaJsonObject.put("lastAttackPoint", Board.GetLastAttackPoint(true).toPointFormatString());
            alphaJsonObject.put("lastAttackResult", Board.GetLastAttackResult(true));
        }
        if (Board.GetLastMoveVector(true) != null) {
            alphaJsonObject.put("lastMoveVector", Board.GetLastMoveVector(true).toVectorFormaString());
        }
        childJsonObject.put("true", alphaJsonObject);
        JSONObject bravoJsonObject = new JSONObject();
        bravoJsonObject.put("hp", GetHps(false));
        bravoJsonObject.put("values", GetValues(false));
        bravoJsonObject.put("enableAttack", GetEnableAttacks(false));
        if (Board.GetLastAttackPoint(false) != null) {
            bravoJsonObject.put("lastAttackPoint", Board.GetLastAttackPoint(false).toPointFormatString());
            bravoJsonObject.put("lastAttackResult", Board.GetLastAttackResult(false));
        }
        if (Board.GetLastMoveVector(false) != null) {
            bravoJsonObject.put("lastMoveVector", Board.GetLastMoveVector(false).toVectorFormaString());
        }
        childJsonObject.put("false", bravoJsonObject);
        jsonObject.put(String.valueOf(Board.GetTurnCount()), childJsonObject);
        if (autoSave) {
            SaveLogger();
        }
    }

    /**
     * 一次元配列の戦艦HPを取得
     * 
     * @param alphaSide
     * @return
     */
    public static ArrayList<Integer> GetHps(boolean alphaSide) {
        ArrayList<Integer> hps = new ArrayList<Integer>();
        for (int x = 0; x < Board.BOARD_SIZE; x++) {
            for (int y = 0; y < Board.BOARD_SIZE; y++) {
                hps.add(Board.GetCell(y, x).GetHp(alphaSide));
            }
        }
        return hps;
    }

    /**
     * 一次元配列の評価値配列を取得
     * 
     * @param alphaSide
     * @return
     */
    public static ArrayList<ArrayList<Integer>> GetValues(boolean alphaSide) {
        ArrayList<ArrayList<Integer>> values = new ArrayList<ArrayList<Integer>>();
        for (int x = 0; x < Board.BOARD_SIZE; x++) {
            for (int y = 0; y < Board.BOARD_SIZE; y++) {
                values.add(Board.GetCell(y, x).GetValues(alphaSide));
            }
        }
        return values;
    }

    /**
     * 一次元配列の攻撃可否を取得
     * 
     * @param alphaSide
     * @return
     */
    public static ArrayList<Boolean> GetEnableAttacks(boolean alphaSide) {
        ArrayList<Boolean> enableAttacks = new ArrayList<Boolean>();
        for (int x = 0; x < Board.BOARD_SIZE; x++) {
            for (int y = 0; y < Board.BOARD_SIZE; y++) {
                enableAttacks.add(Board.GetCell(y, x).GetEnableAttack(alphaSide));
            }
        }
        return enableAttacks;
    }

    /**
     * ロガーを保存
     */
    public static void SaveLogger() {
        File folder = new File("log");
        if (!folder.exists() || !folder.isDirectory()) {
            folder.mkdir();
        }
        try {
            File file = new File("log/" + fileName + ".json");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(jsonObject.toString());
            bufferedWriter.close();
            outputStreamWriter.close();
        } catch (IOException exception) {
            System.err.println(exception);
        }
    }
}