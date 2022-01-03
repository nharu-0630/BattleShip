import java.util.*;

class Algorithm011 extends Interface {
    Algorithm011(boolean alphaSide, boolean isEnemySecret) {
        super(alphaSide, isEnemySecret);
        Board.InitializeValues(alphaSide, 1);
    }

    private boolean estimatedAttackedFlag = false;
    private Point estimatedBeforePoint = null;
    private boolean prepareTurned = false;
    private Point preparePoint = null;

    private int enemyMoveCount = 0;

    public void SetParameter(int[] parameters) {

    }

    public void Think() {
        if (Board.IsLastAttack(!alphaSide)) {
            if (Board.GetLastAttackResult(!alphaSide).contains(3)) {
                allySumHp--;
                allyCount--;
                if (allyCount == 0) {
                    Board.Interrupt();
                }
            }
            if (Board.GetLastAttackResult(!alphaSide).contains(2)) {
                allySumHp--;
            }
        }
        if (Board.IsLastAttack(alphaSide)) {
            if (Board.GetLastAttackResult(alphaSide).contains(3)) {
                enemySumHp--;
                enemyCount--;
                if (enemyCount == 0) {
                    Board.Interrupt();
                }
            }
            if (Board.GetLastAttackResult(alphaSide).contains(2)) {
                enemySumHp--;
            }
        }
        Board.SearchEnableAttackPoints(alphaSide);

        // 敵軍が移動した = 移動先の可能性があるポイントの評価値に1を追加する
        if (Board.GetLastMoveVector(!alphaSide) != null) {
            enemyMoveCount++;
            ArrayList<Point> minusPoints = new ArrayList<Point>();
            for (int x = 0; x < Board.GetBoardSize(); x++) {
                for (int y = 0; y < Board.GetBoardSize(); y++) {
                    Point oldPoint = new Point(x, y);
                    Point newPoint = oldPoint.Plus(Board.GetLastMoveVector(!alphaSide));
                    if (Board.GetCell(oldPoint).GetValue(alphaSide, 0) == -1
                            && 0 <= newPoint.x && newPoint.x <= Board.GetBoardSize() - 1
                            && 0 <= newPoint.y && newPoint.y <= Board.GetBoardSize() - 1) {
                        minusPoints.add(newPoint);
                    }
                }
            }
            switch (Board.GetLastMoveVector(!alphaSide).x) {
                case 2:
                case 1:
                    for (int x = Board.GetLastMoveVector(!alphaSide).x; x < Board.GetBoardSize(); x++) {
                        for (int y = 0; y < Board.GetBoardSize(); y++) {
                            int value = Board.GetCell(x, y).GetValue(alphaSide, 0);
                            if (value < 0) {
                                value = 0;
                            }
                            Board.GetCell(x, y).SetValueForce(alphaSide, 0, value + 1);
                        }
                    }
                    break;
                case -1:
                case -2:
                    for (int x = 0; x < Board.GetBoardSize(); x++) {
                        for (int y = 0; y < Board.GetBoardSize() + Board.GetLastMoveVector(!alphaSide).x; y++) {
                            int value = Board.GetCell(x, y).GetValue(alphaSide, 0);
                            if (value < 0) {
                                value = 0;
                            }
                            Board.GetCell(x, y).SetValueForce(alphaSide, 0, value + 1);
                        }
                    }
                    break;
            }
            switch (Board.GetLastMoveVector(!alphaSide).y) {
                case 2:
                case 1:
                    for (int x = 0; x < Board.GetBoardSize(); x++) {
                        for (int y = Board.GetLastMoveVector(!alphaSide).y; y < Board.GetBoardSize(); y++) {
                            int value = Board.GetCell(x, y).GetValue(alphaSide, 0);
                            if (value < 0) {
                                value = 0;
                            }
                            Board.GetCell(x, y).SetValueForce(alphaSide, 0, value + 1);
                        }
                    }
                    break;
                case -1:
                case -2:
                    for (int x = 0; x < Board.GetBoardSize(); x++) {
                        for (int y = 0; y < Board.GetBoardSize() + Board.GetLastMoveVector(!alphaSide).y; y++) {
                            int value = Board.GetCell(x, y).GetValue(alphaSide, 0);
                            if (value < 0) {
                                value = 0;
                            }
                            Board.GetCell(x, y).SetValueForce(alphaSide, 0, value + 1);
                        }
                    }
                    break;
            }
            for (Point point : minusPoints) {
                Board.GetCell(point).SetValueForce(alphaSide, 0, -1);
            }
        }

        // 自軍が攻撃した
        if (Board.IsLastAttack(alphaSide)) {
            // 敵軍が撃沈した = 命中したポイントの評価値, 逆評価値を-2に固定する
            if (Board.GetLastAttackResult(alphaSide).contains(3)) {
                Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValueForce(alphaSide, 0, -2);
                Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValueForce(alphaSide, 1, -2);
            }
            // 敵軍が命中した = 命中したポイントの評価値を20に設定する, 命中したポイントのX軸Y軸対称のポイントの評価値に5を追加する,
            // = 命中したポイントのX軸, Y軸対称のポイントの評価値に3を追加する
            if (Board.GetLastAttackResult(alphaSide).contains(2)) {
                Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValueForce(alphaSide, 0, 20);

                if (enemyMoveCount == 0) {
                    Point xySymmetryPoint = new Point(
                            Math.abs(Board.GetLastAttackPoint(alphaSide).x - (Board.GetBoardSize() - 1)),
                            Math.abs(Board.GetLastAttackPoint(alphaSide).y - (Board.GetBoardSize() - 1)));
                    Board.GetCell(xySymmetryPoint).SetValue(alphaSide, 0,
                            Board.GetCell(xySymmetryPoint).GetValue(alphaSide, 0) + 5);

                    Point xSymmetryPoint = new Point(
                            Math.abs(Board.GetLastAttackPoint(alphaSide).x - (Board.GetBoardSize() - 1)),
                            Board.GetLastAttackPoint(alphaSide).y);
                    Board.GetCell(xSymmetryPoint).SetValue(alphaSide, 0,
                            Board.GetCell(xSymmetryPoint).GetValue(alphaSide, 0) + 3);

                    Point ySymmetryPoint = new Point(
                            Math.abs(Board.GetLastAttackPoint(alphaSide).x - (Board.GetBoardSize() - 1)),
                            Board.GetLastAttackPoint(alphaSide).y);
                    Board.GetCell(ySymmetryPoint).SetValue(alphaSide, 0,
                            Board.GetCell(ySymmetryPoint).GetValue(alphaSide, 0) + 3);
                }

                // 敵軍が移動した = 命中したポイントに移動ベクトルを足したポイントが範囲内ならそのポイントに移動したと判断し、攻撃可能範囲内なら攻撃する (A)
                // 敵軍が移動しなかった = 命中したポイントにもう一度攻撃する
                if (Board.IsLastMove(!alphaSide)) {
                    Point estimatedPoint = Board.GetLastAttackPoint(alphaSide)
                            .Plus(Board.GetLastMoveVector(!alphaSide));

                    if (estimatedPoint.IsRange()) {
                        Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValueForce(alphaSide, 0, 0);
                        Board.GetCell(estimatedPoint).SetValueForce(alphaSide, 0, 20);
                        if (Board.IsEnableAttackPoint(alphaSide, estimatedPoint)) {
                            estimatedAttackedFlag = true;
                            estimatedBeforePoint = Board.GetLastAttackPoint(alphaSide);
                            DoAttack(estimatedPoint);
                            return;
                        }
                    }
                } else {
                    if (Board.IsEnableAttackPoint(alphaSide, Board.GetLastAttackPoint(alphaSide))) {
                        DoAttack(Board.GetLastAttackPoint(alphaSide));
                        return;
                    }
                }
                // 敵軍が命中しなかった = (A) の攻撃結果の場合は移動する前のポイントが攻撃可能範囲内なら攻撃する
            } else {
                if (estimatedAttackedFlag) {
                    estimatedAttackedFlag = false;
                    if (Board.IsEnableAttackPoint(alphaSide, estimatedBeforePoint)) {
                        DoAttack(estimatedBeforePoint);
                    }
                    estimatedBeforePoint = null;
                    return;
                }
            }
            // 敵軍が波高しした = 攻撃したポイントの評価値を-1に固定する, 周囲のポイントの評価値に1を追加する
            if (Board.GetLastAttackResult(alphaSide).contains(1)) {
                Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValueForce(alphaSide, 0, -1);
                for (Point point : Board.GetRoundPoints(Board.GetLastAttackPoint(alphaSide))) {
                    Board.GetCell(point).SetValue(alphaSide, 0, Board.GetCell(point).GetValue(alphaSide, 0) + 1);
                }
            }
            // 敵軍が外れした = 攻撃したポイント, 周囲のポイントの評価値を-1に固定する
            if (Board.GetLastAttackResult(alphaSide).contains(0)) {
                Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValueForce(alphaSide, 0, -1);
                for (Point point : Board.GetRoundPoints(Board.GetLastAttackPoint(alphaSide))) {
                    Board.GetCell(point).SetValueForce(alphaSide, 0, -1);
                }
            }
        }

        // 敵軍が攻撃した = 攻撃したポイントの評価値を-1に固定する, 周囲のポイントの評価値に1を追加する, 攻撃したポイントの逆評価値を0に設定する
        if (Board.IsLastAttack(!alphaSide)) {
            Board.GetCell(Board.GetLastAttackPoint(!alphaSide)).SetValueForce(alphaSide, 0, -1);
            for (Point point : Board.GetRoundPoints(Board.GetLastAttackPoint(!alphaSide))) {
                Board.GetCell(point).SetValue(alphaSide, 0, Board.GetCell(point).GetValue(alphaSide, 0) + 1);
            }
            Board.GetCell(Board.GetLastAttackPoint(!alphaSide)).SetValue(alphaSide, 1, 0);
            // 自軍が撃沈した = 命中したポイントの評価値, 逆評価値を-2に固定する
            if (Board.GetLastAttackResult(!alphaSide).contains(3)) {
                Board.GetCell(Board.GetLastAttackPoint(!alphaSide)).SetValueForce(alphaSide, 0, -2);
                Board.GetCell(Board.GetLastAttackPoint(!alphaSide)).SetValueForce(alphaSide, 1, -2);
            }
            // 自軍が命中した = 命中したポイントの逆評価値を10に設定する
            if (Board.GetLastAttackResult(!alphaSide).contains(2)) {
                Board.GetCell(Board.GetLastAttackPoint(!alphaSide)).SetValue(alphaSide, 1, 10);

                // ArrayList<Point> points = Board.GetFilterMoveEnablePoints(alphaSide,
                // Board.GetLastAttackPoint(!alphaSide),
                // Board.GetCrossPoints(Board.GetLastAttackPoint(!alphaSide), 1, 2));
                // if (Board.GetCell(Board.GetLastAttackPoint(!alphaSide)).GetHp(alphaSide) == 2
                // && points.size() != 0) {
                // DoMove(Board.GetLastAttackPoint(!alphaSide), Board.GetRandomPoint(points));
                // return;
                // }
            }
            // 自軍が波高しした = 攻撃したポイントの逆評価値を0に設定する, 周囲のポイントに1を追加する
            if (Board.GetLastAttackResult(!alphaSide).contains(1)) {
                Board.GetCell(Board.GetLastAttackPoint(!alphaSide)).SetValue(alphaSide, 1, 0);
                for (Point point : Board.GetRoundPoints(Board.GetLastAttackPoint(!alphaSide))) {
                    Board.GetCell(point).SetValue(alphaSide, 1,
                            Board.GetCell(point).GetValue(alphaSide, 1) + 1);
                }
            }
        }

        if (prepareTurned && Board.IsEnableAttackPoint(alphaSide, preparePoint)) {
            prepareTurned = false;
            DoAttack(preparePoint);
            preparePoint = null;
            return;
        }

        ArrayList<Point> maxValuePoints = new ArrayList<Point>(Board.GetPointValues(alphaSide, null, 0, 1).keySet());
        if (Board.GetCell(maxValuePoints.get(0)).GetValue(alphaSide, 0) > 5) {
            for (Point point : maxValuePoints) {
                if (Board.GetCell(point).IsAlive(alphaSide)) {
                    HashMap<Point, Integer> crossPointValues = new HashMap<Point, Integer>();
                    for (Point crossPoint : Board.GetFilterMoveEnablePoints(alphaSide, point,
                            Board.GetCrossPoints(point, 1, 1))) {
                        crossPointValues.put(crossPoint,
                                Board.GetCell(crossPoint).GetValue(alphaSide, 1));
                    }
                    if (crossPointValues.size() != 0) {
                        int value = Collections.max(crossPointValues.values());
                        for (Map.Entry<Point, Integer> crossPointValue : crossPointValues.entrySet()) {
                            if (crossPointValue.getValue() == value) {
                                DoMove(point, crossPointValue.getKey());
                                return;
                            }
                        }
                    }
                } else {
                    for (Point movePoint : Board.GetShortPoints(alphaSide, point)) {
                        Point minusVector = point.Minus(movePoint);
                        Point moveVector = null;
                        if (minusVector.x > 1) {
                            moveVector = new Point(2, 0);
                        } else if (minusVector.x < -1) {
                            moveVector = new Point(-2, 0);
                        } else if (minusVector.y > 1) {
                            moveVector = new Point(0, 2);
                        } else if (minusVector.y < -1) {
                            moveVector = new Point(0, -2);
                        }
                        if (moveVector != null) {
                            if (!Board.IsMoveEnableVector(alphaSide, movePoint, moveVector)
                                    || point.Equal(movePoint.Plus(moveVector))) {
                                moveVector = new Point(moveVector.x / 2, moveVector.y / 2);
                                if (!Board.IsMoveEnableVector(alphaSide, movePoint, moveVector)) {
                                    moveVector = null;
                                }
                            }
                            if (moveVector != null) {
                                DoMove(movePoint, movePoint.Plus(moveVector));
                                return;
                            }
                        }
                    }
                }
            }
        }

        if (Board.GetMaxValuePoints(alphaSide, true, 0).size() != 0) {
            DoAttack(Board.GetRandomPoint(Board.GetMaxValuePoints(alphaSide, true, 0)));
            return;
        } else {
            Board.WriteDisableTurn();
        }
    }
}
