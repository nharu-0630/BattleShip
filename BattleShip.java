import java.util.*;
import java.io.*;
import org.json.*;

class Cell {
    private int alphaHp;
    private int bravoHp;

    private int alphaValue;
    private int bravoValue;

    private boolean alphaIsAttack;
    private boolean bravoIsAttack;

    Cell() {
        alphaHp = -1;
        bravoHp = -1;
        alphaValue = 0;
        bravoValue = 0;
        alphaIsAttack = false;
        bravoIsAttack = false;
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

    public int GetValue(boolean alphaSide) {
        if (alphaSide) {
            return alphaValue;
        } else {
            return bravoValue;
        }
    }

    public void SetValue(boolean alphaSide, int value) {
        if (alphaSide) {
            alphaValue = value;
        } else {
            bravoValue = value;
        }
    }

    public void SetIsAttak(boolean alphaSide, boolean isAttack) {
        if (isAttack) {
            if (IsEmpty(alphaSide)) {
                if (alphaSide) {
                    alphaIsAttack = true;
                } else {
                    bravoIsAttack = true;
                }
            }
        } else {
            if (alphaSide) {
                alphaIsAttack = false;
            } else {
                bravoIsAttack = false;
            }
        }
    }

    public boolean GetIsAttack(boolean alphaSide) {
        if (alphaSide) {
            return alphaIsAttack;
        } else {
            return bravoIsAttack;
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
    public int x;
    public int y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point Plus(Point point) {
        return new Point(this.x + point.x, this.y + point.y);
    }

    public Point Minus(Point point) {
        return new Point(this.x - point.x, this.y - point.y);
    }

    @Override
    public String toString() {
        return "(" + y + ", " + x + ")";
    }
}

class Board {
    private static final int boardSize = 5;

    private static Cell[][] cells;
    private static int turnCount = 0;

    private static boolean alphaWin = false;
    private static boolean bravoWin = false;

    private static Point lastAlphaAttackPoint = null;
    private static ArrayList<Integer> lastAlphaAttackResult = new ArrayList<Integer>(Arrays.asList(-1));
    private static Point lastAlphaMoveVector = null;

    private static Point lastBravoAttackPoint = null;
    private static ArrayList<Integer> lastBravoAttackResult = new ArrayList<Integer>(Arrays.asList(-1));
    private static Point lastBravoMoveVector = null;

    private static boolean visibleLog = false;

    Board(boolean visibleLog) {
        Initialize(visibleLog);
    }

    public static void Initialize(boolean visibleLog) {
        cells = new Cell[GetBoardSize()][GetBoardSize()];
        for (int x = 0; x < GetBoardSize(); x++) {
            for (int y = 0; y < GetBoardSize(); y++) {
                SetCell(x, y, new Cell());
            }
        }

        turnCount = 0;

        alphaWin = false;
        bravoWin = false;

        lastAlphaAttackPoint = null;
        lastAlphaAttackResult = new ArrayList<Integer>(Arrays.asList(-1));
        lastAlphaMoveVector = null;

        lastBravoAttackPoint = null;
        lastBravoAttackResult = new ArrayList<Integer>(Arrays.asList(-1));
        lastBravoMoveVector = null;

        Board.visibleLog = visibleLog;
    }

    public static void WriteBoardHp(boolean alphaSide) {
        WriteLogSide(alphaSide);
        WriteLogLine("盤面】HP");
        WriteLog("  ");
        for (int i = 0; i < Board.GetBoardSize(); i++) {
            if (i != 0) {
                WriteLog("|");
            }
            WriteLog(Integer.valueOf(i).toString());
        }
        WriteLogLine("");
        for (int y = 0; y < Board.GetBoardSize(); y++) {
            WriteLog(y + "|");
            for (int x = 0; x < Board.GetBoardSize(); x++) {
                if (Board.GetCell(x, y).GetHp(alphaSide) != -1) {
                    WriteLog(Integer.valueOf(Board.GetCell(x, y).GetHp(alphaSide)).toString());
                } else {
                    WriteLog(" ");
                }
                WriteLog(" ");
            }
            WriteLogLine("");
        }
    }

    public static void WriteBoardIsAttack(boolean alphaSide) {
        WriteLogSide(alphaSide);
        SearchAttackPoints(alphaSide);
        WriteLogLine("盤面】攻撃可能範囲");
        WriteLog("  ");
        for (int i = 0; i < Board.GetBoardSize(); i++) {
            if (i != 0) {
                WriteLog("|");
            }
            WriteLog(Integer.valueOf(i).toString());
        }
        WriteLogLine("");
        for (int y = 0; y < Board.GetBoardSize(); y++) {
            WriteLog(Integer.valueOf(y).toString() + "|");
            for (int x = 0; x < Board.GetBoardSize(); x++) {
                if (Board.GetCell(x, y).GetIsAttack(alphaSide)) {
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
        if (visibleLog) {
            if (alphaSide) {
                System.out.print("【α");
            } else {
                System.out.print("【β");
            }
        }
    }

    public static void WriteLogLine(String line) {
        if (visibleLog) {
            System.out.println(line);
        }
    }

    public static void WriteLog(String line) {
        if (visibleLog) {
            System.out.print(line);
        }
    }

    public static boolean IsContinue(boolean interrupt) {
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
            SetTurnCount();
        }
        WriteLogLine("--------------------");
        WriteLogLine("【戦況】 " + Board.GetTurnCount() + "ターン目");
        WriteLogLine("α残機 = " + alphaCount + " (総HP : " + alphaSumHp + ")");
        WriteLogLine("β残機 = " + bravoCount + " (総HP : " + bravoSumHp + ")");
        if (alphaCount == 0) {
            WriteLogLine("αが全滅しました");
            WriteLogLine("βの勝利です");
            alphaWin = false;
            bravoWin = true;
            return false;
        }
        if (bravoCount == 0) {
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
                alphaWin = false;
                bravoWin = false;
            }
            return false;
        }
        return true;
    }

    public static boolean GetAlphaWin() {
        return alphaWin;
    }

    public static boolean GetBravoWin() {
        return bravoWin;
    }

    public static void SetTurnCount() {
        turnCount++;
    }

    public static int GetTurnCount() {
        return turnCount;
    }

    public static int GetBoardSize() {
        return boardSize;
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

    public static void InitializeValues(boolean alphaSide) {
        for (int x = 0; x < Board.GetBoardSize(); x++) {
            for (int y = 0; y < Board.GetBoardSize(); y++) {
                Board.GetCell(x, y).SetValue(alphaSide, 0);
            }
        }
    }

    public static void NormalizeValues(boolean alphaSide) {
        for (int x = 0; x < Board.GetBoardSize(); x++) {
            for (int y = 0; y < Board.GetBoardSize(); y++) {
                int value = 3;
                if (x != 0 && x != Board.GetBoardSize() - 1) {
                    value += 2;
                }
                if (y != 0 && y != Board.GetBoardSize() - 1) {
                    value += 2;
                }
                Board.GetCell(x, y).SetValue(alphaSide, value);
            }
        }
    }

    public static ArrayList<Point> GetShipPoints(boolean alphaSide) {
        ArrayList<Point> points = new ArrayList<Point>();
        for (int x = 0; x < Board.GetBoardSize(); x++) {
            for (int y = 0; y < Board.GetBoardSize(); y++) {
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

    public static ArrayList<Point> GetRandomPoints(int count) {
        ArrayList<Point> points = new ArrayList<Point>();
        Random random = new Random();
        HashMap<Integer, Integer> randomPoints = new HashMap<Integer, Integer>();
        while (randomPoints.size() != count) {
            randomPoints.put(random.nextInt(Board.GetBoardSize()), random.nextInt(Board.GetBoardSize()));
        }
        for (Map.Entry<Integer, Integer> randomPoint : randomPoints.entrySet()) {
            points.add(new Point(randomPoint.getKey(), randomPoint.getValue()));
        }
        return points;
    }

    public static void SetRandom4Points(boolean alphaSide) {
        for (Point point : GetRandomPoints(4)) {
            GetCell(point).SetHp(alphaSide, 3);
        }
    }

    public static ArrayList<Point> GetMaxValuePoints(boolean alphaSide, boolean isAttackEnable) {
        HashMap<Point, Integer> pointsValue = new HashMap<Point, Integer>();
        for (int x = 0; x < Board.GetBoardSize(); x++) {
            for (int y = 0; y < Board.GetBoardSize(); y++) {
                if ((isAttackEnable && Board.IsAttackPoint(alphaSide, new Point(x, y))) || !isAttackEnable) {
                    pointsValue.put(new Point(x, y), Board.GetCell(x, y).GetValue(alphaSide));
                }
            }
        }
        int maxValue = Collections.max(pointsValue.values());
        ArrayList<Point> points = new ArrayList<Point>();
        for (Map.Entry<Point, Integer> pointValue : pointsValue.entrySet()) {
            if (pointValue.getValue() == maxValue) {
                points.add(pointValue.getKey());
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
        if (point.x < Board.GetBoardSize() - 1) {
            points.add(new Point(point.x + 1, point.y));
        }
        if (point.y > 0) {
            points.add(new Point(point.x, point.y - 1));
        }
        if (point.y < Board.GetBoardSize() - 1) {
            points.add(new Point(point.x, point.y + 1));
        }
        if (point.x > 0 && point.y > 0) {
            points.add(new Point(point.x - 1, point.y - 1));
        }
        if (point.x > 0 && point.y < Board.GetBoardSize() - 1) {
            points.add(new Point(point.x - 1, point.y + 1));
        }
        if (point.x < Board.GetBoardSize() - 1 && point.y > 0) {
            points.add(new Point(point.x + 1, point.y - 1));
        }
        if (point.x < Board.GetBoardSize() - 1 && point.y < Board.GetBoardSize() - 1) {
            points.add(new Point(point.x + 1, point.y + 1));
        }
        return points;
    }

    public static ArrayList<Point> GetCrossPoints(Point point, int length) {
        ArrayList<Point> points = new ArrayList<Point>();
        for (int i = 1; i <= length; i++) {
            if (point.x > i - 1) {
                points.add(new Point(point.x - i, point.y));
            }
            if (point.x < Board.GetBoardSize() - i) {
                points.add(new Point(point.x + i, point.y));
            }
            if (point.y > i - 1) {
                points.add(new Point(point.x, point.y - i));
            }
            if (point.y < Board.GetBoardSize() - i) {
                points.add(new Point(point.x, point.y + i));
            }
        }
        return points;
    }

    public static int GetPointDistance(Point aPoint, Point bPoint) {
        return (Math.abs(aPoint.x - bPoint.x) + Math.abs(aPoint.y - bPoint.y));
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

    public static boolean MovePoint(boolean alphaSide, Point oldPoint, Point newPoint) {
        WriteLogSide(alphaSide);
        if (IsMoveEnablePoint(alphaSide, oldPoint, newPoint)) {
            WriteLogLine("移動】 " + oldPoint + " → " + newPoint);
            Board.GetCell(newPoint).SetHp(alphaSide, Board.GetCell(oldPoint).GetHp(alphaSide));
            Board.GetCell(oldPoint).SetHp(alphaSide, -1);
            if (alphaSide) {
                lastAlphaAttackPoint = null;
                lastAlphaAttackResult = new ArrayList<Integer>(Arrays.asList(-1));
                lastAlphaMoveVector = newPoint.Minus(oldPoint);
            } else {
                lastBravoAttackPoint = null;
                lastBravoAttackResult = new ArrayList<Integer>(Arrays.asList(-1));
                lastBravoMoveVector = newPoint.Minus(oldPoint);
            }
            Logger.AddLogger(alphaSide);
            return true;
        } else {
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
        WriteLogLine("移動】 " + vectorPoint);
        if (alphaSide) {
            lastAlphaAttackPoint = null;
            lastAlphaAttackResult = new ArrayList<Integer>(Arrays.asList(-1));
            lastAlphaMoveVector = vectorPoint;
        } else {
            lastBravoAttackPoint = null;
            lastBravoAttackResult = new ArrayList<Integer>(Arrays.asList(-1));
            lastBravoMoveVector = vectorPoint;
        }
        Logger.AddLogger(alphaSide);
    }

    public static void SearchAttackPoints(boolean alphaSide) {
        for (int x = 0; x < Board.GetBoardSize(); x++) {
            for (int y = 0; y < Board.GetBoardSize(); y++) {
                Board.GetCell(x, y).SetIsAttak(alphaSide, false);
            }
        }
        for (Point shipPoint : Board.GetShipPoints(alphaSide)) {
            for (Point point : GetRoundPoints(shipPoint)) {
                Board.GetCell(point).SetIsAttak(alphaSide, true);
            }
        }
        for (Point shipPoint : Board.GetShipPoints(!alphaSide)) {
            if (Board.GetCell(shipPoint).GetHp(!alphaSide) == 0) {
                Board.GetCell(shipPoint).SetIsAttak(alphaSide, false);
            }
        }
    }

    public static ArrayList<Point> GetAttackPoints(boolean alphaSide) {
        ArrayList<Point> points = new ArrayList<Point>();
        for (int x = 0; x < Board.GetBoardSize(); x++) {
            for (int y = 0; y < Board.GetBoardSize(); y++) {
                if (Board.GetCell(x, y).GetIsAttack(alphaSide)) {
                    points.add(new Point(x, y));
                }
            }
        }
        return points;
    }

    public static boolean IsAttackPoint(boolean alphaSide, Point point) {
        SearchAttackPoints(alphaSide);
        return Board.GetCell(point).GetIsAttack(alphaSide);
    }

    public static boolean AttackPoint(boolean alphaSide, Point point, boolean judgeResult) {
        WriteLogSide(alphaSide);
        if (IsAttackPoint(alphaSide, point)) {
            ArrayList<Integer> attackResult = new ArrayList<Integer>();
            WriteLogLine("攻撃】" + point);
            if (judgeResult) {
                if (Board.GetCell(point).IsAlive(!alphaSide)) {
                    Board.GetCell(point).SetHp(!alphaSide, Board.GetCell(point).GetHp(!alphaSide) - 1);
                    // 命中！
                    attackResult = new ArrayList<Integer>(Arrays.asList(2));
                    WriteLogLine("命中！");
                    if (Board.GetCell(point).GetHp(!alphaSide) == 0) {
                        // 撃沈！
                        attackResult = new ArrayList<Integer>(Arrays.asList(3));
                        WriteLogLine("撃沈！");
                    }
                } else {
                    // ハズレ！
                    attackResult = new ArrayList<Integer>(Arrays.asList(0));
                    WriteLogLine("ハズレ！");
                }
                for (Point roundPoint : GetRoundPoints(point)) {
                    if (Board.GetCell(roundPoint).IsAlive(!alphaSide)) {
                        // 波高し！
                        attackResult.add(1);
                        WriteLogLine("波高し！");
                    }
                }
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
            WriteLogLine("攻撃】 拒否されました");
            if (alphaSide) {
                lastAlphaAttackPoint = null;
                lastAlphaAttackResult = new ArrayList<Integer>(Arrays.asList(-1));
                lastAlphaMoveVector = null;
            } else {
                lastBravoAttackPoint = null;
                lastBravoAttackResult = new ArrayList<Integer>(Arrays.asList(-1));
                lastBravoMoveVector = null;
            }
            Logger.AddLogger(alphaSide);
            return false;
        }
    }

    public static void AttackResultTransfer(boolean alphaSide, ArrayList<Integer> attackResult) {
        for (int i = 0; i < attackResult.size(); i++) {
            switch (attackResult.get(i)) {
                case 3:
                    WriteLogLine("撃沈！");
                    Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetHp(!alphaSide, 0);
                    break;
                case 2:
                    WriteLogLine("命中！");
                    break;
                case 1:
                    WriteLogLine("波高し！");
                    break;
                case 0:
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
        WriteLogLine("攻撃】" + point);
        if (Board.GetCell(point).IsAlive(!alphaSide)) {
            Board.GetCell(point).SetHp(!alphaSide, Board.GetCell(point).GetHp(!alphaSide) - 1);
            // 命中！
            attackResult = new ArrayList<Integer>(Arrays.asList(2));
            WriteLogLine("命中！");
            if (Board.GetCell(point).GetHp(!alphaSide) == 0) {
                // 撃沈！
                attackResult = new ArrayList<Integer>(Arrays.asList(3));
                WriteLogLine("撃沈！");
            }
        } else {
            // ハズレ！
            attackResult = new ArrayList<Integer>(Arrays.asList(0));
            WriteLogLine("ハズレ！");
        }
        for (Point roundPoint : GetRoundPoints(point)) {
            if (Board.GetCell(roundPoint).IsAlive(!alphaSide)) {
                // 波高し！
                attackResult.add(1);
                WriteLogLine("波高し！");
            }
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
        Board.WriteLogLine(newPoint.Minus(oldPoint) + " に移動！");
        Board.MovePoint(alphaSide, oldPoint, newPoint);
    }

    public void DoAttack(Point point) {
        Board.WriteLogLine(point + " に魚雷発射！");
        Board.AttackPoint(alphaSide, point, !isEnemySecret);
    }

    public void DoMoveForce(Point vectorPoint) {
        Board.WriteLogLine(vectorPoint + " に移動！");
        Board.MoveVectorForce(alphaSide, vectorPoint);
    }

    public void DoAttackForce(Point point) {
        Board.WriteLogLine(point + " に魚雷発射！");
        Board.AttackPointForce(alphaSide, point);
    }
}

class Logger {
    private static JSONObject jsonObject;

    public static void CreateLogger() {
        jsonObject = new JSONObject();
    }

    public static void AddLogger(boolean alphaSide) {
        JSONObject childJsonObject = new JSONObject();
        childJsonObject.put("alphaSide", alphaSide);
        JSONObject alphaJsonObject = new JSONObject();
        alphaJsonObject.put("hp", GetHpArrayList(true));
        alphaJsonObject.put("value", GetValueArrayList(true));
        alphaJsonObject.put("isAttack", GetIsAttackArrayList(true));
        alphaJsonObject.put("lastAttackPoint", Board.GetLastAttackPoint(true));
        alphaJsonObject.put("lastAttackResult", Board.GetLastAttackResult(true));
        alphaJsonObject.put("lastMoveVector", Board.GetLastMoveVector(true));
        childJsonObject.put("true", alphaJsonObject);
        JSONObject bravoJsonObject = new JSONObject();
        bravoJsonObject.put("hp", GetHpArrayList(false));
        bravoJsonObject.put("value", GetValueArrayList(false));
        bravoJsonObject.put("isAttack", GetIsAttackArrayList(false));
        bravoJsonObject.put("lastAttackPoint", Board.GetLastAttackPoint(false));
        bravoJsonObject.put("lastAttackResult", Board.GetLastAttackResult(false));
        bravoJsonObject.put("lastMoveVector", Board.GetLastMoveVector(false));
        childJsonObject.put("false", bravoJsonObject);
        jsonObject.put(String.valueOf(Board.GetTurnCount()), childJsonObject);
    }

    public static ArrayList<Integer> GetHpArrayList(boolean alphaSide) {
        ArrayList<Integer> hpArrayList = new ArrayList<Integer>();
        for (int x = 0; x < Board.GetBoardSize(); x++) {
            for (int y = 0; y < Board.GetBoardSize(); y++) {
                hpArrayList.add(Board.GetCell(x, y).GetHp(alphaSide));
            }
        }
        return hpArrayList;
    }

    public static ArrayList<Integer> GetValueArrayList(boolean alphaSide) {
        ArrayList<Integer> valueArrayList = new ArrayList<Integer>();
        for (int x = 0; x < Board.GetBoardSize(); x++) {
            for (int y = 0; y < Board.GetBoardSize(); y++) {
                valueArrayList.add(Board.GetCell(x, y).GetValue(alphaSide));
            }
        }
        return valueArrayList;
    }

    public static ArrayList<Boolean> GetIsAttackArrayList(boolean alphaSide) {
        ArrayList<Boolean> isAttackArrayList = new ArrayList<Boolean>();
        for (int x = 0; x < Board.GetBoardSize(); x++) {
            for (int y = 0; y < Board.GetBoardSize(); y++) {
                isAttackArrayList.add(Board.GetCell(x, y).GetIsAttack(alphaSide));
            }
        }
        return isAttackArrayList;
    }

    public static void SaveLogger(String fileName) {
        File file = new File("log");
        if (!file.exists() || !file.isDirectory()) {
            file.mkdir();
        }
        try {
            FileWriter fileWriter = new FileWriter("log/" + fileName);
            fileWriter.write(jsonObject.toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException exception) {
            System.err.println(exception);
        }
    }
}