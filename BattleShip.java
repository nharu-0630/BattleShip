import java.util.*;

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
            return alphaHp == -1;
        } else {
            return bravoHp == -1;
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
        return "(" + x + ", " + y + ")";
    }
}

class Board {
    private static final int boardSize = 5;

    private static Cell[][] cells;
    private static int turnCount;

    private static boolean alphaWin;
    private static boolean bravoWin;

    private static Point lastAlphaAttackPoint;
    private static int lastAlphaAttackResult;
    private static Point lastAlphaMoveVector;

    private static Point lastBravoAttackPoint;
    private static int lastBravoAttackResult;
    private static Point lastBravoMoveVector;

    private static boolean visibleLog;

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
        lastAlphaAttackResult = -1;
        lastAlphaMoveVector = null;

        lastBravoAttackPoint = null;
        lastBravoAttackResult = -1;
        lastBravoMoveVector = null;

        Board.visibleLog = visibleLog;
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

    public static Point GetLastAttackPoint(boolean alphaSide) {
        if (alphaSide) {
            return lastAlphaAttackPoint;
        } else {
            return lastBravoAttackPoint;
        }
    }

    public static int GetLastAttackResult(boolean alphaSide) {
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

    public static void LogSide(boolean alphaSide) {
        if (visibleLog) {
            if (alphaSide) {
                System.out.print("【α");
            } else {
                System.out.print("【β");
            }
        }
    }

    public static void LogLine(String line) {
        if (visibleLog) {
            System.out.println(line);
        }
    }

    public static void Log(String line) {
        if (visibleLog) {
            System.out.print(line);
        }
    }

    public static boolean IsContinue(boolean interrupt) {
        int alphaCount = ShipPoints(true).size();
        int bravoCount = ShipPoints(false).size();
        int alphaSumHp = 0;
        for (Point point : ShipPoints(true)) {
            alphaSumHp += GetCell(point).GetHp(true);
        }
        int bravoSumHp = 0;
        for (Point point : ShipPoints(false)) {
            bravoSumHp += GetCell(point).GetHp(false);
        }
        if (!interrupt) {
            SetTurnCount();
        }
        LogLine("--------------------");
        LogLine("【戦況】 " + Board.GetTurnCount() + "ターン目");
        LogLine("α残機 = " + alphaCount + " (総HP : " + alphaSumHp + ")");
        LogLine("β残機 = " + bravoCount + " (総HP : " + bravoSumHp + ")");
        if (alphaCount == 0) {
            LogLine("αが全滅しました");
            LogLine("βの勝利です");
            alphaWin = false;
            bravoWin = true;
            return false;
        }
        if (bravoCount == 0) {
            LogLine("βが全滅しました");
            LogLine("αの勝利です");
            alphaWin = true;
            bravoWin = false;
            return false;
        }
        if (interrupt) {
            if (alphaSumHp > bravoSumHp) {
                LogLine("αの勝利です");
                alphaWin = true;
                bravoWin = false;
            } else if (bravoSumHp > alphaSumHp) {
                LogLine("βの勝利です");
                alphaWin = false;
                bravoWin = true;
            } else {
                LogLine("引き分けです");
                alphaWin = false;
                bravoWin = false;
            }
            return false;
        }
        return true;
    }

    public static ArrayList<Point> RandomPoints(int count) {
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
        for (Point point : RandomPoints(4)) {
            GetCell(point).SetHp(alphaSide, 3);
        }
    }

    public static ArrayList<Point> MaxValuePoints(boolean alphaSide, boolean isAttackEnable) {
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

    public static ArrayList<Point> PointRound(Point point) {
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

    public static ArrayList<Point> PointCross(Point point, int length) {
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

    public static boolean IsMoveEnablePoint(boolean alphaSide, Point oldPoint, Point newPoint) {
        if (Board.GetCell(oldPoint).IsAlive(alphaSide) && Board.GetCell(newPoint).IsEmpty(alphaSide)) {
            if (PointDistance(oldPoint, newPoint) == 1) {
                return true;
            } else if (Math.abs(oldPoint.x - newPoint.x) == 2 && oldPoint.y == newPoint.y) {
                return true;
            } else if (Math.abs(oldPoint.y - newPoint.y) == 2 && oldPoint.x == newPoint.x) {
                return true;
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

    public static int PointDistance(Point aPoint, Point bPoint) {
        return (Math.abs(aPoint.x - bPoint.x) + Math.abs(aPoint.y - bPoint.y));
    }

    public static boolean MovePoint(boolean alphaSide, Point oldPoint, Point newPoint) {
        LogSide(alphaSide);
        if (IsMoveEnablePoint(alphaSide, oldPoint, newPoint)) {
            LogLine("移動】 " + oldPoint + " → " + newPoint);
            Board.GetCell(newPoint).SetHp(alphaSide, Board.GetCell(oldPoint).GetHp(alphaSide));
            Board.GetCell(oldPoint).SetHp(alphaSide, -1);
            if (alphaSide) {
                lastAlphaAttackPoint = null;
                lastAlphaAttackResult = -1;
                lastAlphaMoveVector = newPoint.Minus(oldPoint);
            } else {
                lastBravoAttackPoint = null;
                lastBravoAttackResult = -1;
                lastBravoMoveVector = newPoint.Minus(oldPoint);
            }
            return true;
        } else {
            LogLine("移動】拒否されました");
            return false;
        }
    }

    public static boolean MoveVector(boolean alphaSide, Point oldPoint, Point vectorPoint) {
        return MovePoint(alphaSide, oldPoint, oldPoint.Plus(vectorPoint));
    }

    public static void MoveVectorForce(boolean alphaSide, Point vectorPoint) {
        LogSide(alphaSide);
        LogLine("移動】 " + vectorPoint);
        if (alphaSide) {
            lastAlphaAttackPoint = null;
            lastAlphaAttackResult = -1;
            lastAlphaMoveVector = vectorPoint;
        } else {
            lastBravoAttackPoint = null;
            lastBravoAttackResult = -1;
            lastBravoMoveVector = vectorPoint;
        }
    }

    public static ArrayList<Point> ShortPoint(boolean alphaSide, Point point) {
        HashMap<Point, Integer> pointsDistance = new HashMap<Point, Integer>();
        for (Point shipPoint : ShipPoints(alphaSide)) {
            pointsDistance.put(shipPoint, PointDistance(shipPoint, point));
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

    public static void ClearValue(boolean alphaSide) {
        for (int x = 0; x < Board.GetBoardSize(); x++) {
            for (int y = 0; y < Board.GetBoardSize(); y++) {
                Board.GetCell(x, y).SetValue(alphaSide, 0);
            }
        }
    }

    public static void NormalizeValue(boolean alphaSide) {
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

    public static ArrayList<Point> ShipPoints(boolean alphaSide) {
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

    public static void AttackPointsSearch(boolean alphaSide) {
        for (int x = 0; x < Board.GetBoardSize(); x++) {
            for (int y = 0; y < Board.GetBoardSize(); y++) {
                Board.GetCell(x, y).SetIsAttak(alphaSide, false);
            }
        }
        for (Point shipPoint : Board.ShipPoints(alphaSide)) {
            for (Point point : PointRound(shipPoint)) {
                Board.GetCell(point).SetIsAttak(alphaSide, true);
            }
        }
        for (Point shipPoint : Board.ShipPoints(!alphaSide)) {
            if (Board.GetCell(shipPoint).GetHp(!alphaSide) == 0) {
                Board.GetCell(shipPoint).SetIsAttak(alphaSide, false);
            }
        }
    }

    public static ArrayList<Point> AttackPoints(boolean alphaSide) {
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
        AttackPointsSearch(alphaSide);
        return Board.GetCell(point).GetIsAttack(alphaSide);
    }

    public static boolean AttackPoint(boolean alphaSide, Point point, boolean judgeResult) {
        LogSide(alphaSide);
        if (IsAttackPoint(alphaSide, point)) {
            int attackResult = -1;
            LogLine("攻撃】" + point);
            if (judgeResult) {
                if (Board.GetCell(point).IsAlive(!alphaSide)) {
                    Board.GetCell(point).SetHp(!alphaSide, Board.GetCell(point).GetHp(!alphaSide) - 1);
                    // 命中！
                    attackResult = 2;
                    LogLine("命中！");
                    if (Board.GetCell(point).GetHp(!alphaSide) == 0) {
                        // 撃沈！
                        attackResult = 3;
                        LogLine("撃沈！");
                    }
                } else {
                    // ハズレ！
                    attackResult = 0;
                    LogLine("ハズレ！");
                }
                for (Point roundPoint : PointRound(point)) {
                    if (Board.GetCell(roundPoint).IsAlive(!alphaSide)) {
                        // 波高し！
                        attackResult = 1;
                        LogLine("波高し！");
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
            return true;
        } else {
            LogLine("攻撃】 拒否されました");
            if (alphaSide) {
                lastAlphaAttackPoint = null;
                lastAlphaAttackResult = -1;
                lastAlphaMoveVector = null;
            } else {
                lastBravoAttackPoint = null;
                lastBravoAttackResult = -1;
                lastBravoMoveVector = null;
            }
            return false;
        }
    }

    public static void AttackResultTransfer(boolean alphaSide, int attackResult) {
        switch (attackResult) {
            case 3:
                LogLine("撃沈！");
                break;
            case 2:
                LogLine("命中！");
                break;
            case 1:
                LogLine("波高し！");
                break;
            case 0:
                LogLine("ハズレ！");
                break;
        }
        if (alphaSide) {
            lastAlphaAttackResult = attackResult;
        } else {
            lastBravoAttackResult = attackResult;
        }
    }

    public static void AttackPointForce(boolean alphaSide, Point point) {
        LogSide(alphaSide);
        int attackResult = -1;
        LogLine("攻撃】" + point);
        if (Board.GetCell(point).IsAlive(!alphaSide)) {
            Board.GetCell(point).SetHp(!alphaSide, Board.GetCell(point).GetHp(!alphaSide) - 1);
            // 命中！
            attackResult = 2;
            LogLine("命中！");
            if (Board.GetCell(point).GetHp(!alphaSide) == 0) {
                // 撃沈！
                attackResult = 3;
                LogLine("撃沈！");
            }
        } else {
            // ハズレ！
            attackResult = 0;
            LogLine("ハズレ！");
        }
        for (Point roundPoint : PointRound(point)) {
            if (Board.GetCell(roundPoint).IsAlive(!alphaSide)) {
                // 波高し！
                attackResult = 1;
                LogLine("波高し！");
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
    }

    public static void WriteBoardHp(boolean alphaSide) {
        LogSide(alphaSide);
        LogLine("盤面】HP");
        Log("  ");
        for (int i = 0; i < Board.GetBoardSize(); i++) {
            if (i != 0) {
                Log("|");
            }
            Log(Integer.valueOf(i).toString());
        }
        LogLine("");
        for (int y = 0; y < Board.GetBoardSize(); y++) {
            Log(y + "|");
            for (int x = 0; x < Board.GetBoardSize(); x++) {
                if (Board.GetCell(x, y).GetHp(alphaSide) != -1) {
                    Log(Integer.valueOf(Board.GetCell(x, y).GetHp(alphaSide)).toString());
                } else {
                    Log(" ");
                }
                Log(" ");
            }
            LogLine("");
        }
    }

    public static void WriteBoardIsAttack(boolean alphaSide) {
        LogSide(alphaSide);
        AttackPointsSearch(alphaSide);
        LogLine("盤面】攻撃可能範囲");
        Log("  ");
        for (int i = 0; i < Board.GetBoardSize(); i++) {
            if (i != 0) {
                Log("|");
            }
            Log(Integer.valueOf(i).toString());
        }
        LogLine("");
        for (int y = 0; y < Board.GetBoardSize(); y++) {
            Log(Integer.valueOf(y).toString() + "|");
            for (int x = 0; x < Board.GetBoardSize(); x++) {
                if (Board.GetCell(x, y).GetIsAttack(alphaSide)) {
                    Log("*");
                } else {
                    Log(" ");
                }
                Log(" ");
            }
            LogLine("");
        }
    }

    public static boolean IsLastMove(boolean alphaSide) {
        if (alphaSide) {
            return (lastAlphaMoveVector != null);
        } else {
            return (lastBravoMoveVector != null);
        }
    }

    public static boolean IsLastAttack(boolean alphaSide) {
        if (alphaSide) {
            return (lastAlphaAttackPoint != null);
        } else {
            return (lastBravoAttackPoint != null);
        }
    }

    public static Point RandomGet(ArrayList<Point> points) {
        Random random = new Random();
        return points.get(random.nextInt(points.size()));
    }
}

class Interface {
    public int allyCount;
    public int allySumHp;
    public int enemyCount;
    public int enemySumHp;

    public final boolean alphaSide;
    public final boolean isEnemySecret;

    Interface(boolean alphaSide, boolean isEnemySecret) {
        this.alphaSide = alphaSide;
        this.isEnemySecret = isEnemySecret;
        allyCount = 4;
        allySumHp = 12;
        enemyCount = 4;
        enemySumHp = 12;
    }

    public void DoMove(Point oldPoint, Point newPoint) {
        Board.LogLine(newPoint.Minus(oldPoint) + " に移動！");
        Board.MovePoint(alphaSide, oldPoint, newPoint);
    }

    public void DoAttack(Point point) {
        Board.LogLine(point + " に魚雷発射！");
        Board.AttackPoint(alphaSide, point, !isEnemySecret);
    }

    public void DoMoveForce(Point vectorPoint) {
        Board.LogLine(vectorPoint + " に移動！");
        Board.MoveVectorForce(alphaSide, vectorPoint);
    }

    public void DoAttackForce(Point point) {
        Board.LogLine(point + " に魚雷発射！");
        Board.AttackPointForce(alphaSide, point);
    }
}
