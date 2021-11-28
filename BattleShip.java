import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Collections;

class Cell {
    private int enemyHp;
    private int allyHp;

    private boolean canAttack;

    Cell() {
        enemyHp = -1;
        allyHp = -1;
        canAttack = false;
    }

    public int GetEnemyHp() {
        return enemyHp;
    }

    public void SetEnemyHp(int enemyHp) {
        this.enemyHp = enemyHp;
    }

    public int GetAllyHp() {
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
    public int x;
    public int y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(x, y) = " + x + ", " + y;
    }
}

class BoardCells {
    private Cell[][] boardCells;

    private int turnCount;

    BoardCells(int boardSize) {
        boardCells = new Cell[boardSize][boardSize];
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                SetCell(x, y, new Cell());
            }
        }
        turnCount = 0;
    }

    public void SetTurnCount() {
        turnCount++;
    }

    public int GetTurnCount() {
        return turnCount;
    }

    public Cell GetCell(Point point) {
        return boardCells[point.x][point.y];
    }

    public Cell GetCell(int x, int y) {
        return boardCells[x][y];
    }

    public void SetCell(Point point, Cell cell) {
        boardCells[point.x][point.y] = cell;
    }

    public void SetCell(int x, int y, Cell cell) {
        boardCells[x][y] = cell;
    }
}

class BattleShip {

    final static int boardSize = 5;
    static BoardCells boardCells = new BoardCells(boardSize);

    public static void main(String args[]) {
        // 敵をランダムに4箇所配置
        for (Point point : RandomPoints(4)) {
            boardCells.GetCell(point).SetEnemyHp(3);
        }
        boardCells.GetCell(1, 1).SetAllyHp(3);
        boardCells.GetCell(3, 1).SetAllyHp(3);
        boardCells.GetCell(1, 3).SetAllyHp(3);
        boardCells.GetCell(3, 3).SetAllyHp(3);
        while (IsContinue()) {
            WriteBoardAllyHp();
        }
    }

    // ゲーム続行の可否
    static boolean IsContinue() {
        int allyCount = AllyPoints().size();
        int enemyCount = EnemyPoints().size();
        int allySumHp = 0;
        for (Point point : AllyPoints()) {
            allySumHp += boardCells.GetCell(point).GetAllyHp();
        }
        int enemySumHp = 0;
        for (Point point : EnemyPoints()) {
            enemySumHp += boardCells.GetCell(point).GetEnemyHp();
        }
        boardCells.SetTurnCount();
        System.out.println("【戦況】 " + boardCells.GetTurnCount() + "ターン目");
        System.out.println("味方残機 = " + allyCount + " (総HP : " + allySumHp + ")");
        System.out.println("　敵残機 = " + enemyCount + " (総HP : " + enemySumHp + ")");
        if (allyCount == 0) {
            System.out.println("味方が全滅しました");
            return false;
        }
        if (enemyCount == 0) {
            System.out.println("敵が全滅しました");
            return false;
        }
        return true;
    }

    // 重複を除くランダムなポイントリスト
    static ArrayList<Point> RandomPoints(int count) {
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
    static ArrayList<Point> PointRound(Point point) {
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
    static ArrayList<Point> PointCross(Point point) {
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
    static boolean CanMoveAlly(Point oldPoint, Point newPoint) {
        if (boardCells.GetCell(oldPoint).isAlive() && boardCells.GetCell(newPoint).isEmpty()) {
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
    static int PointDistance(Point aPoint, Point bPoint) {
        return (Math.abs(aPoint.x - bPoint.x) + Math.abs(aPoint.y - bPoint.y));
    }

    // 指定ポイントから指定ポイントへの移動
    static boolean MoveAllyPoint(Point oldPoint, Point newPoint) {
        if (CanMoveAlly(oldPoint, newPoint)) {
            System.out.println("【戦艦移動】 " + oldPoint + " → " + newPoint);
            boardCells.GetCell(newPoint).SetAllyHp(boardCells.GetCell(oldPoint).GetAllyHp());
            boardCells.GetCell(oldPoint).SetAllyHp(-1);
            return true;
        } else {
            System.out.println("【戦艦移動】拒否されました");
            return false;
        }
    }

    // 指定ポイントから指定ベクトル方向への移動
    static boolean MoveAllyVector(Point oldPoint, Point vectorPoint) {
        Point newPoint = new Point(oldPoint.x + vectorPoint.x, oldPoint.y + vectorPoint.y);
        return MoveAllyPoint(oldPoint, newPoint);
    }

    // 指定ポイントへ最も近い味方のポイントリスト
    static ArrayList<Point> ShortPoint(Point point) {
        HashMap<Point, Integer> pointsDistance = new HashMap<Point, Integer>();
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                if (boardCells.GetCell(x, y).isAlive()) {
                    pointsDistance.put(new Point(x, y), PointDistance(new Point(x, y), point));
                }
            }
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

    // 味方のポイントリスト
    static ArrayList<Point> AllyPoints() {
        ArrayList<Point> points = new ArrayList<Point>();
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                if (boardCells.GetCell(x, y).isAlive()) {
                    points.add(new Point(x, y));
                }
            }
        }
        return points;
    }

    // 敵のポイントリスト
    static ArrayList<Point> EnemyPoints() {
        ArrayList<Point> points = new ArrayList<Point>();
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                if (boardCells.GetCell(x, y).GetEnemyHp() > 0) {
                    points.add(new Point(x, y));
                }
            }
        }
        return points;
    }

    // 攻撃可能範囲の検索
    static void CanAttackSearch() {
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                boardCells.GetCell(x, y).SetCanAttak(false);
            }
        }
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                if (boardCells.GetCell(x, y).isAlive()) {
                    for (Point point : PointRound(new Point(x, y))) {
                        boardCells.GetCell(point).SetCanAttak(true);
                    }
                }
            }
        }
    }

    // 指定ポイントへの攻撃可否
    static boolean CanAttackPoint(Point point) {
        CanAttackSearch();
        return boardCells.GetCell(point).GetCanAttack();
    }

    // 指定ポイントへの攻撃
    static boolean AttackPoint(Point point) {
        if (CanAttackPoint(point)) {
            System.out.println("【攻撃処理】" + point);
            if (boardCells.GetCell(point).GetEnemyHp() > 0) {
                boardCells.GetCell(point).SetEnemyHp(boardCells.GetCell(point).GetEnemyHp() - 1);
                // 命中！
                System.out.println("命中！");
                if (boardCells.GetCell(point).GetEnemyHp() == 0) {
                    // 撃沈！
                    System.out.println("撃沈！");
                }
            } else {
                // ハズレ！
                System.out.println("ハズレ！");
            }
            for (Point roundPoint : PointRound(point)) {
                if (boardCells.GetCell(roundPoint).GetEnemyHp() > 0) {
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
    static void WriteBoardAllyHp() {
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
                if (boardCells.GetCell(x, y).GetAllyHp() != -1) {
                    System.out.print(boardCells.GetCell(x, y).GetAllyHp());
                } else {
                    System.out.print(" ");
                }
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    // 盤面に敵HPを表示
    static void WriteBoardEnemyHp() {
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
                if (boardCells.GetCell(x, y).GetEnemyHp() != -1) {
                    System.out.print(boardCells.GetCell(x, y).GetEnemyHp());
                } else {
                    System.out.print(" ");
                }
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    // 盤面に攻撃可能範囲を表示
    static void WriteBoardCanAttack() {
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
                if (boardCells.GetCell(x, y).GetCanAttack()) {
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