import java.util.*;
import java.io.*;
import org.json.*;

class Cell {
    private int alphaHp;
    private int bravoHp;

    private ArrayList<Integer> alphaValues;
    private ArrayList<Integer> bravoValues;

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

    public int GetHp(boolean alphaSide) {
        if (alphaSide) {
            return alphaHp;
        } else {
            return bravoHp;
        }
    }

    public void SetHp(boolean alphaSide, int hp) {
        if (alphaSide) {
            alphaHp = hp;
        } else {
            bravoHp = hp;
        }
    }

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

    public ArrayList<Integer> GetValues(boolean alphaSide) {
        if (alphaSide) {
            return alphaValues;
        } else {
            return bravoValues;
        }
    }

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

    public void AddValue(boolean alphaSide, int layer, int value) {
        if (alphaSide) {
            while (layer >= alphaValues.size()) {
                alphaValues.add(0);
            }
            if (alphaValues.get(layer) >= 0) {
                alphaValues.set(layer, GetValue(alphaSide, layer) + value);
            }
        } else {
            while (layer >= bravoValues.size()) {
                bravoValues.add(0);
            }
            if (bravoValues.get(layer) >= 0) {
                bravoValues.set(layer, GetValue(alphaSide, layer) + value);
            }
        }
    }

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

    public boolean GetEnableAttack(boolean alphaSide) {
        if (alphaSide) {
            return alphaEnableAttack;
        } else {
            return bravoEnableAttack;
        }
    }

    public boolean IsAlive(boolean alphaSide) {
        if (alphaSide) {
            return alphaHp > 0;
        } else {
            return bravoHp > 0;
        }
    }

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

    public Point Plus(Point point) {
        return new Point(this.x + point.x, this.y + point.y);
    }

    public Point Minus(Point point) {
        return new Point(this.x - point.x, this.y - point.y);
    }

    public Point Multiply(int number) {
        return new Point(x * number, y * number);
    }

    public Point Divide(int number) {
        return new Point(x / number, y / number);
    }

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

    public boolean IsRange() {
        return (0 <= x && x < Board.BOARD_SIZE && 0 <= y && y < Board.BOARD_SIZE);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public String toPointFormatString() {
        String[] yStrings = new String[] { "A", "B", "C", "D", "E" };
        return yStrings[y] + (x + 1);
    }

    public String toVectorFormaString() {
        if (x > 0) {
            return "東" + x;
        }
        if (x < 0) {
            return "西" + Math.abs(x);
        }
        if (y > 0) {
            return "南" + y;
        }
        if (y < 0) {
            return "北" + Math.abs(y);
        }
        return toPointFormatString();
    }
}

class Board {
    public static final int BOARD_SIZE = 5;

    public static final int ATTACK_NULL = -1;
    public static final int ATTACK_NOHIT = 0;
    public static final int ATTACK_NEAR = 1;
    public static final int ATTACK_HIT = 2;
    public static final int ATTACK_SINK = 3;

    private static Cell[][] cells;
    private static int turnCount = 0;

    private static boolean alphaWin = false;
    private static boolean bravoWin = false;

    private static Point lastAlphaAttackPoint = null;
    private static ArrayList<Integer> lastAlphaAttackResult = new ArrayList<Integer>(Arrays.asList(ATTACK_NULL));
    private static Point lastAlphaMoveVector = null;

    private static Point lastBravoAttackPoint = null;
    private static ArrayList<Integer> lastBravoAttackResult = new ArrayList<Integer>(Arrays.asList(ATTACK_NULL));
    private static Point lastBravoMoveVector = null;

    private static boolean isVisibleLog = false;
    private static boolean isAttackResultArray = false;
    private static boolean isEnemySecret = false;

    Board(boolean isVisibleLog, boolean isAttackResultArray, boolean isEnemySecret) {
        Initialize(isVisibleLog, isAttackResultArray, isEnemySecret);
    }

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

    public static void WriteLogSide(boolean alphaSide) {
        if (isVisibleLog) {
            if (alphaSide) {
                System.out.print("【α");
            } else {
                System.out.print("【β");
            }
        }
    }

    public static void WriteLogLine(String line) {
        if (isVisibleLog) {
            System.out.println(line);
        }
    }

    public static void WriteLog(String line) {
        if (isVisibleLog) {
            System.out.print(line);
        }
    }

    public static void WriteDisableTurn() {
        System.out.print(ConsoleColors.RED_BOLD);
        System.out.println("【警告】無効ターン");
        System.out.println(Logger.GetFileName() + " : " + Board.GetTurnCount());
        System.out.print(ConsoleColors.RESET);
    }

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

    public static boolean GetAlphaWin() {
        return alphaWin;
    }

    public static boolean GetBravoWin() {
        return bravoWin;
    }

    public static void AddTurnCount() {
        turnCount++;
    }

    public static int GetTurnCount() {
        return turnCount;
    }

    public static Cell GetCell(Point point) {
        return cells[point.x][point.y];
    }

    public static Cell GetCell(int x, int y) {
        return cells[x][y];
    }

    public static void SetCell(Point point, Cell cell) {
        cells[point.x][point.y] = cell;
    }

    public static void SetCell(int x, int y, Cell cell) {
        cells[x][y] = cell;
    }

    public static boolean IsLastAttack(boolean alphaSide) {
        if (alphaSide) {
            return (lastAlphaAttackPoint != null);
        } else {
            return (lastBravoAttackPoint != null);
        }
    }

    public static boolean IsLastMove(boolean alphaSide) {
        if (alphaSide) {
            return (lastAlphaMoveVector != null);
        } else {
            return (lastBravoMoveVector != null);
        }
    }

    public static Point GetLastAttackPoint(boolean alphaSide) {
        if (alphaSide) {
            return lastAlphaAttackPoint;
        } else {
            return lastBravoAttackPoint;
        }
    }

    public static ArrayList<Integer> GetLastAttackResult(boolean alphaSide) {
        if (alphaSide) {
            return lastAlphaAttackResult;
        } else {
            return lastBravoAttackResult;
        }
    }

    public static Point GetLastMoveVector(boolean alphaSide) {
        if (alphaSide) {
            return lastAlphaMoveVector;
        } else {
            return lastBravoMoveVector;
        }
    }

    public static void InitializeValues(boolean alphaSide, int layer) {
        for (int x = 0; x < Board.BOARD_SIZE; x++) {
            for (int y = 0; y < Board.BOARD_SIZE; y++) {
                Board.GetCell(x, y).SetValue(alphaSide, layer, 0);
            }
        }
    }

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

    public static Point GetRandomPoint(ArrayList<Point> points) {
        Random random = new Random();
        return points.get(random.nextInt(points.size()));
    }

    // public static ArrayList<Point> GetRandomPoints(int count) {
    // ArrayList<Point> points = new ArrayList<Point>();
    // Random random = new Random();
    // HashMap<Integer, Integer> randomPoints = new HashMap<Integer, Integer>();
    // while (randomPoints.size() != count) {
    // randomPoints.put(random.nextInt(Board.BOARD_SIZE),
    // random.nextInt(Board.BOARD_SIZE));
    // }
    // for (Map.Entry<Integer, Integer> randomPoint : randomPoints.entrySet()) {
    // points.add(new Point(randomPoint.getKey(), randomPoint.getValue()));
    // }
    // return points;
    // }

    // public static void SetRandom4Points(boolean alphaSide) {
    // for (Point point : GetRandomPoints(4)) {
    // GetCell(point).SetHp(alphaSide, 3);
    // }
    // }

    public static void SetRandom4Points(boolean alphaSide, boolean roundTrim, boolean cornerTrim) {
        for (Point point : GetRandomPoints(4, roundTrim, cornerTrim)) {
            GetCell(point).SetHp(alphaSide, 3);
        }
    }

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

    public static int GetPointDistance(Point aPoint, Point bPoint) {
        return (Math.abs(aPoint.x - bPoint.x) + Math.abs(aPoint.y - bPoint.y));
    }

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

    public static boolean IsMoveEnablePoint(boolean alphaSide, Point oldPoint, Point newPoint) {
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

    public static boolean IsMoveEnableVector(boolean alphaSide, Point oldPoint, Point vectorPoint) {
        return IsMoveEnablePoint(alphaSide, oldPoint, oldPoint.Plus(vectorPoint));
    }

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

    public static boolean MoveVector(boolean alphaSide, Point oldPoint, Point vectorPoint) {
        return MovePoint(alphaSide, oldPoint, oldPoint.Plus(vectorPoint));
    }

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

    public static boolean IsEnableAttackPoint(boolean alphaSide, Point point) {
        SearchEnableAttackPoints(alphaSide);
        return Board.GetCell(point).GetEnableAttack(alphaSide);
    }

    public static ArrayList<Point> GetFilterEnableAttackPoints(boolean alphaSide, ArrayList<Point> points) {
        ArrayList<Point> tempPoints = new ArrayList<Point>();
        for (Point point : points) {
            if (Board.IsEnableAttackPoint(alphaSide, point)) {
                tempPoints.add(point);
            }
        }
        return tempPoints;
    }

    public static boolean AttackPoint(boolean alphaSide, Point point, boolean judgeResult) {
        WriteLogSide(alphaSide);
        if (IsEnableAttackPoint(alphaSide, point)) {
            ArrayList<Integer> attackResult = new ArrayList<Integer>();
            WriteLogLine("攻撃】" + point.toPointFormatString());
            if (judgeResult) {
                if (Board.GetCell(point).IsAlive(!alphaSide)) {
                    Board.GetCell(point).SetHp(!alphaSide, Board.GetCell(point).GetHp(!alphaSide) - 1);
                    // 命中！
                    attackResult = new ArrayList<Integer>(Arrays.asList(ATTACK_HIT));
                    if (Board.GetCell(point).GetHp(!alphaSide) == 0) {
                        // 撃沈！
                        attackResult = new ArrayList<Integer>(Arrays.asList(ATTACK_SINK));
                    }
                } else {
                    // ハズレ！
                    attackResult = new ArrayList<Integer>(Arrays.asList(ATTACK_NOHIT));
                }
                for (Point roundPoint : GetRoundPoints(point)) {
                    if (Board.GetCell(roundPoint).IsAlive(!alphaSide)) {
                        // 波高し！
                        if (isAttackResultArray) {
                            attackResult.add(ATTACK_NEAR);
                        } else {
                            if (attackResult.contains(ATTACK_NOHIT)) {
                                attackResult = new ArrayList<Integer>(Arrays.asList(ATTACK_NEAR));
                            }
                        }
                    }
                }
            }
            if (attackResult.contains(ATTACK_SINK)) {
                WriteLogLine("撃沈！");
            }
            if (attackResult.contains(ATTACK_HIT)) {
                WriteLogLine("命中！");
            }
            if (attackResult.contains(ATTACK_NEAR)) {
                WriteLogLine("波高し！");
            }
            if (attackResult.contains(ATTACK_NOHIT)) {
                WriteLogLine("ハズレ！");
            }
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

    public static void AttackResultTransfer(boolean alphaSide, ArrayList<Integer> attackResult) {
        for (int i = 0; i < attackResult.size(); i++) {
            switch (attackResult.get(i)) {
                case ATTACK_SINK:
                    WriteLogLine("撃沈！");
                    Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetHp(!alphaSide, 0);
                    break;
                case ATTACK_HIT:
                    WriteLogLine("命中！");
                    break;
                case ATTACK_NEAR:
                    WriteLogLine("波高し！");
                    break;
                case ATTACK_NOHIT:
                    WriteLogLine("ハズレ！");
                    break;
            }
        }
        if (alphaSide) {
            lastAlphaAttackResult = attackResult;
        } else {
            lastBravoAttackResult = attackResult;
        }
    }

    public static void AttackPointForce(boolean alphaSide, Point point) {
        WriteLogSide(alphaSide);
        ArrayList<Integer> attackResult = new ArrayList<Integer>();
        WriteLogLine("攻撃】" + point.toPointFormatString());
        if (Board.GetCell(point).IsAlive(!alphaSide)) {
            Board.GetCell(point).SetHp(!alphaSide, Board.GetCell(point).GetHp(!alphaSide) - 1);
            // 命中！
            attackResult = new ArrayList<Integer>(Arrays.asList(ATTACK_HIT));
            if (Board.GetCell(point).GetHp(!alphaSide) == 0) {
                // 撃沈！
                attackResult = new ArrayList<Integer>(Arrays.asList(ATTACK_SINK));
            }
        } else {
            // ハズレ！
            attackResult = new ArrayList<Integer>(Arrays.asList(ATTACK_NOHIT));
        }
        for (Point roundPoint : GetRoundPoints(point)) {
            if (Board.GetCell(roundPoint).IsAlive(!alphaSide)) {
                // 波高し！
                if (isAttackResultArray) {
                    attackResult.add(ATTACK_NEAR);
                } else {
                    if (attackResult.contains(ATTACK_NOHIT)) {
                        attackResult = new ArrayList<Integer>(Arrays.asList(ATTACK_NEAR));
                    }
                }
            }
        }
        if (attackResult.contains(ATTACK_SINK)) {
            WriteLogLine("撃沈！");
        }
        if (attackResult.contains(ATTACK_HIT)) {
            WriteLogLine("命中！");
        }
        if (attackResult.contains(ATTACK_NEAR)) {
            WriteLogLine("波高し！");
        }
        if (attackResult.contains(ATTACK_NOHIT)) {
            WriteLogLine("ハズレ！");
        }
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
    public final boolean alphaSide;
    public final boolean isEnemySecret;

    public int allyCount;
    public int allySumHp;
    public int enemyCount;
    public int enemySumHp;

    Interface(boolean alphaSide, boolean isEnemySecret) {
        this.alphaSide = alphaSide;
        this.isEnemySecret = isEnemySecret;
        allyCount = 4;
        allySumHp = 12;
        enemyCount = 4;
        enemySumHp = 12;
    }

    public void DoMove(Point oldPoint, Point newPoint) {
        Board.WriteBoard(alphaSide);
        Board.WriteLog(ConsoleColors.BLUE_BOLD);
        Board.WriteLogLine("<" + newPoint.Minus(oldPoint).toVectorFormaString() + " に移動！>");
        Board.WriteLog(ConsoleColors.RESET);
        Board.MovePoint(alphaSide, oldPoint, newPoint);
    }

    public void DoAttack(Point point) {
        Board.WriteBoard(alphaSide);
        Board.WriteLog(ConsoleColors.BLUE_BOLD);
        Board.WriteLogLine("<" + point.toPointFormatString() + " に魚雷発射！>");
        Board.WriteLog(ConsoleColors.RESET);
        Board.AttackPoint(alphaSide, point, !isEnemySecret);
    }

    public void DoMoveForce(Point vectorPoint) {
        Board.WriteBoard(alphaSide);
        Board.WriteLog(ConsoleColors.BLUE_BOLD);
        Board.WriteLogLine("<" + vectorPoint.toVectorFormaString() + " に移動！>");
        Board.WriteLog(ConsoleColors.RESET);
        Board.MoveVectorForce(alphaSide, vectorPoint);
    }

    public void DoAttackForce(Point point) {
        Board.WriteBoard(alphaSide);
        Board.WriteLog(ConsoleColors.BLUE_BOLD);
        Board.WriteLogLine("<" + point.toPointFormatString() + " に魚雷発射！>");
        Board.WriteLog(ConsoleColors.RESET);
        Board.AttackPointForce(alphaSide, point);
    }
}

class Logger {
    private static JSONObject jsonObject;
    private static String fileName;
    private static boolean autoSave;

    public static void CreateLogger(String fileName, boolean autoSave) {
        jsonObject = new JSONObject();
        Logger.fileName = fileName;
        Logger.autoSave = autoSave;
    }

    public static String GetFileName() {
        return fileName;
    }

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

    public static ArrayList<Integer> GetHps(boolean alphaSide) {
        ArrayList<Integer> hps = new ArrayList<Integer>();
        for (int x = 0; x < Board.BOARD_SIZE; x++) {
            for (int y = 0; y < Board.BOARD_SIZE; y++) {
                hps.add(Board.GetCell(y, x).GetHp(alphaSide));
            }
        }
        return hps;
    }

    public static ArrayList<ArrayList<Integer>> GetValues(boolean alphaSide) {
        ArrayList<ArrayList<Integer>> values = new ArrayList<ArrayList<Integer>>();
        for (int x = 0; x < Board.BOARD_SIZE; x++) {
            for (int y = 0; y < Board.BOARD_SIZE; y++) {
                values.add(Board.GetCell(y, x).GetValues(alphaSide));
            }
        }
        return values;
    }

    public static ArrayList<Boolean> GetEnableAttacks(boolean alphaSide) {
        ArrayList<Boolean> enableAttacks = new ArrayList<Boolean>();
        for (int x = 0; x < Board.BOARD_SIZE; x++) {
            for (int y = 0; y < Board.BOARD_SIZE; y++) {
                enableAttacks.add(Board.GetCell(y, x).GetEnableAttack(alphaSide));
            }
        }
        return enableAttacks;
    }

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