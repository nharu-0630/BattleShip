import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Collections;

class Cell {
    private Integer enemyHp;
    private Integer allyHp;

    private boolean canAttack;

    Cell() {
        enemyHp = -1;
        allyHp = -1;
        canAttack = false;
    }

    public Integer GetEnemyHp() {
        return enemyHp;
    }

    public void SetEnemyHp(int enemyHp) {
        this.enemyHp = enemyHp;
    }

    public Integer GetAllyHp() {
        return allyHp;
    }

    public void SetAllyHp(int allyHp) {
        this.allyHp = allyHp;
    }

    public void SetCanAttak(boolean canAttack) {
        if (canAttack) {
            if (isEmpty()) {
                this.canAttack = true;
            }
        } else {
            this.canAttack = false;
        }
    }

    public boolean GetCanAttack() {
        return canAttack;
    }

    public boolean isAlive() {
        return allyHp > 0;
    }

    public boolean isEmpty() {
        return allyHp == -1 && enemyHp != 0;
    }
}

class Point {
    public Integer x;
    public Integer y;

    Point(int x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public Point Add(Point point) {
        return new Point(this.x + point.x, this.y + point.y);
    }

    @Override
    public String toString() {
        return "(x, y) = " + x + ", " + y;
    }
}

class BoardCells {
    private static final Integer boardSize = 5;
    private static Cell[][] boardCells;
    private static Integer turnCount;

    BoardCells() {
        boardCells = new Cell[boardSize][boardSize];
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                SetCell(x, y, new Cell());
            }
        }
        turnCount = 0;
    }

    public static void SetTurnCount() {
        turnCount++;
    }

    public static Integer GetTurnCount() {
        return turnCount;
    }

    public static Cell GetCell(Point point) {
        return boardCells[point.x][point.y];
    }

    public static Cell GetCell(int x, Integer y) {
        return boardCells[x][y];
    }

    public static void SetCell(Point point, Cell cell) {
        boardCells[point.x][point.y] = cell;
    }

    public static void SetCell(int x, Integer y, Cell cell) {
        boardCells[x][y] = cell;
    }

    // ゲーム続行の可否
    public static boolean IsContinue() {
        Integer allyCount = AllyPoints().size();
        Integer enemyCount = EnemyPoints().size();
        Integer allySumHp = 0;
        for (Point point : AllyPoints()) {
            allySumHp += GetCell(point).GetAllyHp();
        }
        Integer enemySumHp = 0;
        for (Point point : EnemyPoints()) {
            enemySumHp += GetCell(point).GetEnemyHp();
        }
        SetTurnCount();
        System.out.println("【戦況】 " + GetTurnCount() + "ターン目");
        System.out.println("味方残機 = " + allyCount + " (総HP : " + allySumHp + ")");
        System.out.println("　敵残機 = " + enemyCount + " (総HP : " + enemySumHp + ")");
        if (allyCount == 0) {
            System.out.println("味方が全滅しました");
            System.out.println("あなたの負けです");
            return false;
        }
        if (enemyCount == 0) {
            System.out.println("敵が全滅しました");
            System.out.println("あなたの勝ちです");
            return false;
        }
        return true;
    }

    // 重複を除くランダムなポイントリスト
    public static ArrayList<Point> RandomPoints(int count) {
        ArrayList<Point> points = new ArrayList<Point>();
        Random random = new Random();
        HashMap<Integer, Integer> randomPoints = new HashMap<Integer, Integer>();
        while (randomPoints.size() != count) {
            randomPoints.put(random.nextInt(boardSize), random.nextInt(boardSize));
        }
        for (Map.Entry<Integer, Integer> randomPoint : randomPoints.entrySet()) {
            points.add(new Point(randomPoint.getKey(), randomPoint.getValue()));
        }
        return points;

    }

    // 指定ポイントから8方向のポイントリスト
    public static ArrayList<Point> PointRound(Point point) {
        ArrayList<Point> points = new ArrayList<Point>();
        if (point.x > 0) {
            points.add(new Point(point.x - 1, point.y));
        }
        if (point.x < boardSize - 1) {
            points.add(new Point(point.x + 1, point.y));
        }
        if (point.y > 0) {
            points.add(new Point(point.x, point.y - 1));
        }
        if (point.y < boardSize - 1) {
            points.add(new Point(point.x, point.y + 1));
        }
        if (point.x > 0 && point.y > 0) {
            points.add(new Point(point.x - 1, point.y - 1));
        }
        if (point.x > 0 && point.y < boardSize - 1) {
            points.add(new Point(point.x - 1, point.y + 1));
        }
        if (point.x < boardSize - 1 && point.y > 0) {
            points.add(new Point(point.x + 1, point.y - 1));
        }
        if (point.x < boardSize - 1 && point.y < boardSize - 1) {
            points.add(new Point(point.x + 1, point.y + 1));
        }
        return points;
    }

    // 指定ポイントから4方向のポイントリスト
    public static ArrayList<Point> PointCross(Point point) {
        ArrayList<Point> points = new ArrayList<Point>();
        if (point.x > 0) {
            points.add(new Point(point.x - 1, point.y));
        }
        if (point.x < boardSize - 1) {
            points.add(new Point(point.x + 1, point.y));
        }
        if (point.y > 0) {
            points.add(new Point(point.x, point.y - 1));
        }
        if (point.y < boardSize - 1) {
            points.add(new Point(point.x, point.y + 1));
        }
        return points;
    }

    // 指定ポイントから指定ポイントへの移動可否
    public static boolean CanMoveAlly(Point oldPoint, Point newPoint) {
        if (GetCell(oldPoint).isAlive() && GetCell(newPoint).isEmpty()) {
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

    // 指定ポイントから指定ポイントへの距離（X距離 + Y距離）
    public static Integer PointDistance(Point aPoint, Point bPoint) {
        return (Math.abs(aPoint.x - bPoint.x) + Math.abs(aPoint.y - bPoint.y));
    }

    // 指定ポイントから指定ポイントへの移動
    public static boolean MoveAllyPoint(Point oldPoint, Point newPoint) {
        if (CanMoveAlly(oldPoint, newPoint)) {
            System.out.println("【戦艦移動】 " + oldPoint + " → " + newPoint);
            GetCell(newPoint).SetAllyHp(GetCell(oldPoint).GetAllyHp());
            GetCell(oldPoint).SetAllyHp(-1);
            return true;
        } else {
            System.out.println("【戦艦移動】拒否されました");
            return false;
        }
    }

    // 指定ポイントから指定ベクトル方向への移動
    public static boolean MoveAllyVector(Point oldPoint, Point vectorPoint) {
        Point newPoint = new Point(oldPoint.x + vectorPoint.x, oldPoint.y + vectorPoint.y);
        return MoveAllyPoint(oldPoint, newPoint);
    }

    // 指定ポイントへ最も近い味方のポイントリスト
    public static ArrayList<Point> ShortPoint(Point point) {
        HashMap<Point, Integer> pointsDistance = new HashMap<Point, Integer>();
        for (Point allyPoint : AllyPoints()) {
            pointsDistance.put(allyPoint, PointDistance(allyPoint, point));
        }
        Integer shortDistance = Collections.min(pointsDistance.values());
        ArrayList<Point> points = new ArrayList<Point>();
        for (Map.Entry<Point, Integer> pointDistance : pointsDistance.entrySet()) {
            if (pointDistance.getValue() == shortDistance) {
                points.add(pointDistance.getKey());
            }
        }
        return points;
    }

    // 味方のポイントリスト
    public static ArrayList<Point> AllyPoints() {
        ArrayList<Point> points = new ArrayList<Point>();
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                if (GetCell(x, y).isAlive()) {
                    points.add(new Point(x, y));
                }
            }
        }
        return points;
    }

    // 敵のポイントリスト
    public static ArrayList<Point> EnemyPoints() {
        ArrayList<Point> points = new ArrayList<Point>();
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                if (GetCell(x, y).GetEnemyHp() > 0) {
                    points.add(new Point(x, y));
                }
            }
        }
        return points;
    }

    // 攻撃可能範囲の検索
    public static void CanAttackSearch() {
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                GetCell(x, y).SetCanAttak(false);
            }
        }
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                if (GetCell(x, y).isAlive()) {
                    for (Point point : PointRound(new Point(x, y))) {
                        GetCell(point).SetCanAttak(true);
                    }
                }
            }
        }
    }

    // 指定ポイントへの攻撃可否
    public static boolean CanAttackPoint(Point point) {
        CanAttackSearch();
        return GetCell(point).GetCanAttack();
    }

    // 指定ポイントへの攻撃
    public static boolean AttackPoint(Point point) {
        if (CanAttackPoint(point)) {
            System.out.println("【攻撃処理】" + point);
            if (GetCell(point).GetEnemyHp() > 0) {
                GetCell(point).SetEnemyHp(GetCell(point).GetEnemyHp() - 1);
                // 命中！
                System.out.println("命中！");
                if (GetCell(point).GetEnemyHp() == 0) {
                    // 撃沈！
                    System.out.println("撃沈！");
                }
            } else {
                // ハズレ！
                System.out.println("ハズレ！");
            }
            for (Point roundPoint : PointRound(point)) {
                if (GetCell(roundPoint).GetEnemyHp() > 0) {
                    // 波高し！
                    System.out.println("波高し！");
                }
            }
            return true;
        } else {
            System.out.println("【攻撃処理】 拒否されました");
            return false;
        }
    }

    // 盤面に味方HPを表示
    public static void WriteBoardAllyHp() {
        System.out.println("【盤面表示】味方HP");
        System.out.print("  ");
        for (int i = 0; i < boardSize; i++) {
            if (i != 0) {
                System.out.print("|");
            }
            System.out.print((i));
        }
        System.out.println();
        for (int y = 0; y < boardSize; y++) {
            System.out.print(y + "|");
            for (int x = 0; x < boardSize; x++) {
                if (GetCell(x, y).GetAllyHp() != -1) {
                    System.out.print(GetCell(x, y).GetAllyHp());
                } else {
                    System.out.print(" ");
                }
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    // 盤面に敵HPを表示
    public static void WriteBoardEnemyHp() {
        System.out.println("【盤面表示】敵HP");
        System.out.print("  ");
        for (int i = 0; i < boardSize; i++) {
            if (i != 0) {
                System.out.print("|");
            }
            System.out.print((i));
        }
        System.out.println();
        for (int y = 0; y < boardSize; y++) {
            System.out.print(y + "|");
            for (int x = 0; x < boardSize; x++) {
                if (GetCell(x, y).GetEnemyHp() != -1) {
                    System.out.print(GetCell(x, y).GetEnemyHp());
                } else {
                    System.out.print(" ");
                }
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    // 盤面に攻撃可能範囲を表示
    public static void WriteBoardCanAttack() {
        CanAttackSearch();
        System.out.println("【盤面表示】攻撃可能範囲");
        System.out.print("  ");
        for (int i = 0; i < boardSize; i++) {
            if (i != 0) {
                System.out.print("|");
            }
            System.out.print((i));
        }
        System.out.println();
        for (int y = 0; y < boardSize; y++) {
            System.out.print(y + "|");
            for (int x = 0; x < boardSize; x++) {
                if (GetCell(x, y).GetCanAttack()) {
                    System.out.print("*");
                } else {
                    System.out.print(" ");
                }
                System.out.print(" ");
            }
            System.out.println();
        }
    }

}

class Algorithm extends BoardCells {
    private static Point lastEnemyAttackPoint;
    private static Integer lastEnemyAttackResult;
    private static Point lastEnemyMoveVector;

    private static Point lastAllyAttackPoint;
    private static Integer lastAllyAttackResult;
    private static Point lastAllyMovePoint;

    private static Integer allyCount;
    private static Integer allySumHp;
    private static Integer enemyCount;
    private static Integer enemySumHp;

    Algorithm() {
        super();
        allyCount = 4;
        allySumHp = 12;
        enemyCount = 4;
        enemySumHp = 12;
    }

    public static void SetLastEnemyAttackPoint(Point point, Integer result) {
        lastEnemyAttackPoint = point;
        lastEnemyAttackResult = result;
    }

    public static void SetLastEnemyMoveVector(Point point) {
        lastEnemyMoveVector = point;
    }

    public static void SetLastAllyAttackPoint(Point point, Integer result) {
        lastAllyAttackPoint = point;
        lastAllyAttackResult = result;
    }

    public static void SetLastAllyMovePoint(Point point) {
        lastAllyMovePoint = point;
    }

    public static void Think() {
        if (lastAllyAttackResult != null) {
            // 前ターンで敵に攻撃した
            switch (lastAllyAttackResult) {
                case 2:
                    // 前ターンで敵に命中した
                    if (lastEnemyMoveVector == null) {
                        // 敵が移動しなかった
                        DoAttack(lastAllyAttackPoint);
                        return;
                    } else {
                        // 敵が移動した
                        if (enemyCount == 1) {
                            // 敵が1機のみ
                            BoardCells.AttackPoint(lastAllyAttackPoint);
                            DoAttack(lastAllyAttackPoint.Add(lastEnemyMoveVector));
                            return;
                        } else {
                            // 敵が2機以上
                        }
                    }
                    break;
                case 1:
                    // 前ターンで敵の8方向に命中した
                    ArrayList<Point> possibilityPoints = BoardCells.PointRound(lastAllyAttackPoint);
                    if (lastEnemyMoveVector == null) {
                        // 敵が移動しなかった
                        if (possibilityPoints.size() <= 3) {
                            // 敵がいる可能性があるポイントが3以下なら最も探索できるポイントに攻撃する
                            HashMap<Point, Integer> pointsRound = new HashMap<Point, Integer>();
                            for (Point point : possibilityPoints) {
                                pointsRound.put(point, PointRound(point).size());
                            }
                            Integer maxRound = Collections.max(pointsRound.values());
                            for (Map.Entry<Point, Integer> pointRound : pointsRound.entrySet()) {
                                if (pointRound.getValue() == maxRound) {
                                    DoAttack(pointRound.getKey());
                                    return;
                                }
                            }
                        }
                    } else {
                        // 敵が移動した
                    }
                    break;
                case 0:
                    // 前ターンで敵に命中しなかった

                    break;
            }

        }
    }

    public static void DoAttack(Point point) {
        System.out.println(point + " に魚雷発射！");
        BoardCells.AttackPoint(point);
        return;
    }
}

class BattleShip {

    // public static BoardCells boardCells = new BoardCells(5);
    public static Algorithm boardCells = new Algorithm();

    public static void main(String args[]) {
        // 敵をランダムに4箇所配置
        for (Point point : BoardCells.RandomPoints(4)) {
            BoardCells.GetCell(point).SetEnemyHp(3);
        }
        BoardCells.GetCell(1, 1).SetAllyHp(3);
        BoardCells.GetCell(3, 1).SetAllyHp(3);
        BoardCells.GetCell(1, 3).SetAllyHp(3);
        BoardCells.GetCell(3, 3).SetAllyHp(3);
        Scanner scanner = new Scanner(System.in);
        while (BoardCells.IsContinue()) {
            BoardCells.WriteBoardAllyHp();
            scanner.nextLine();
            Algorithm.Think();
        }
        System.out.println("ゲームが終了しました");
        scanner.nextLine();
        scanner.close();
    }

}