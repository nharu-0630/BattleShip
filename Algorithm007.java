import java.util.*;

class Algorithm007 extends Interface {
    Algorithm007(boolean alphaSide, boolean isEnemySecret) {
        super(alphaSide, isEnemySecret);
        Board.InitializeValues(alphaSide, 1);
    }

    private boolean estimatedAttacked = false;
    private Point estimatedBeforePoint = null;
    private boolean prepareTurned = false;
    private Point preparePoint = null;

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
        }

        // 自軍が攻撃した
        if (Board.IsLastAttack(alphaSide)) {
            // 敵軍が撃沈した = 命中したポイントの評価値, 逆評価値を-2に固定する
            if (Board.GetLastAttackResult(alphaSide).contains(3)) {
                Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValueForce(alphaSide, 0, -2);
                Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValueForce(alphaSide, 1, -2);
            }
            // 敵軍が命中した = 命中したポイントの評価値を10に設定する
            if (Board.GetLastAttackResult(alphaSide).contains(2)) {
                Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValueForce(alphaSide, 0, 10);
                // 敵軍が移動した = 命中したポイントに移動ベクトルを足したポイントが範囲内ならそのポイントに移動したと判断し、攻撃可能範囲内なら攻撃する (A)
                // 敵軍が移動しなかった = 命中したポイントにもう一度攻撃する
                if (Board.IsLastMove(!alphaSide)) {
                    Point estimatedPoint = Board.GetLastAttackPoint(alphaSide)
                            .Plus(Board.GetLastMoveVector(!alphaSide));

                    if (estimatedPoint.IsRange()) {
                        Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValueForce(alphaSide, 0, 0);
                        Board.GetCell(estimatedPoint).SetValueForce(alphaSide, 0, 10);
                        if (Board.IsEnableAttackPoint(alphaSide, estimatedPoint)) {
                            estimatedAttacked = true;
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
                if (estimatedAttacked) {
                    estimatedAttacked = false;
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
                ArrayList<Point> points = new ArrayList<Point>();
                for (Point point : Board.GetCrossPoints(Board.GetLastAttackPoint(!alphaSide), 2, 2)) {
                    if (Board.IsMoveEnablePoint(alphaSide, Board.GetLastAttackPoint(!alphaSide),
                            point)) {
                        points.add(point);
                    }
                }
                if (points.size() != 0) {
                    DoMove(Board.GetLastAttackPoint(!alphaSide), Board.GetRandomPoint(
                            new ArrayList<Point>(Board.GetPointValues(alphaSide, points, 1, -1).keySet())));
                    return;
                }
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
        if (Board.GetCell(Board.GetMaxValuePoints(alphaSide, false, 0).get(0)).GetValue(alphaSide, 0) > 5) {
            if (Board.GetCell(Board.GetMaxValuePoints(alphaSide, false, 0).get(0)).GetValue(alphaSide, 0) != Board
                    .GetCell(Board.GetMaxValuePoints(alphaSide, true, 0).get(0)).GetValue(alphaSide, 0)) {
                preparePoint = Board.GetRandomPoint(Board.GetMaxValuePoints(alphaSide, false, 0));
                if (Board.GetCell(preparePoint).IsAlive(alphaSide)) {
                    if (Board.GetFilterMoveEnablePoints(alphaSide, preparePoint,
                            Board.GetCrossPoints(preparePoint, 1, 1)).size() != 0) {
                        DoMove(preparePoint, Board.GetRandomPoint(Board.GetFilterMoveEnablePoints(alphaSide,
                                preparePoint,
                                Board.GetCrossPoints(preparePoint, 1, 1))));
                        return;
                    }
                } else {
                    Point movePoint = Board.GetRandomPoint(Board.GetShortPoints(alphaSide, preparePoint));
                    Point minusPoint = preparePoint.Minus(movePoint);
                    Point vectorPoint = null;
                    if (minusPoint.x > 1) {
                        vectorPoint = new Point(2, 0);
                    }
                    if (minusPoint.x < -1) {
                        vectorPoint = new Point(-2, 0);
                    }
                    if (minusPoint.y > 1) {
                        vectorPoint = new Point(0, 2);
                    }
                    if (minusPoint.y < -1) {
                        vectorPoint = new Point(0, -2);
                    }
                    if (vectorPoint != null) {
                        if (!Board.IsMoveEnableVector(alphaSide, movePoint, vectorPoint)
                                || minusPoint.x + minusPoint.y < 2) {
                            vectorPoint = new Point(vectorPoint.x / 2, vectorPoint.y / 2);
                            if (!Board.IsMoveEnableVector(alphaSide, movePoint, vectorPoint)) {
                                vectorPoint = null;
                            }
                        }
                    }
                    if (vectorPoint != null) {
                        DoMove(movePoint, movePoint.Plus(vectorPoint));
                        return;
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
