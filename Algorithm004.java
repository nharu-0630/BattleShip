import java.util.*;

class Algorithm004 extends Interface {
    Algorithm004(boolean alphaSide, boolean isEnemySecret) {
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

        if (Board.IsLastAttack(!alphaSide)) {
            // 被攻撃
            // 自軍評価
            Board.GetCell(Board.GetLastAttackPoint(!alphaSide)).SetValue(alphaSide, 0, 0);
            for (Point point : Board.GetRoundPoints(Board.GetLastAttackPoint(!alphaSide))) {
                Board.GetCell(point).SetValue(alphaSide, 0, Board.GetCell(point).GetValue(alphaSide, 0) + 1);
            }
            // 敵軍評価
            Board.GetCell(Board.GetLastAttackPoint(!alphaSide)).SetValue(alphaSide, 1, 0);

            if (Board.GetLastAttackResult(!alphaSide).contains(3)) {
                // 被撃沈
                // 自軍評価
                Board.GetCell(Board.GetLastAttackPoint(!alphaSide)).SetValue(alphaSide, 0, -1);
                // 敵軍評価
                Board.GetCell(Board.GetLastAttackPoint(!alphaSide)).SetValue(alphaSide, 1, -1);
            }
            if (Board.GetLastAttackResult(!alphaSide).contains(2)) {
                // 被命中
                // 敵軍評価
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
            if (Board.GetLastAttackResult(!alphaSide).contains(1)) {
                // 被波高し
                // 敵軍評価
                for (Point point : Board.GetRoundPoints(Board.GetLastAttackPoint(!alphaSide))) {
                    Board.GetCell(point).SetValue(alphaSide, 1,
                            Board.GetCell(point).GetValue(alphaSide, 1) + 1);
                }

            }
        }
        if (Board.IsLastAttack(alphaSide)) {
            // 攻撃
            if (Board.GetLastAttackResult(alphaSide).contains(3)) {
                // 撃沈
                Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValue(alphaSide, 0, -1);
                Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValue(alphaSide, 1, -1);
            }
            if (Board.GetLastAttackResult(alphaSide).contains(2)) {
                // 命中
                // 被移動
                Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValue(alphaSide, 0, 10);
                if (Board.IsLastMove(!alphaSide)) {
                    Point estimatedPoint = Board.GetLastAttackPoint(alphaSide)
                            .Plus(Board.GetLastMoveVector(!alphaSide));

                    if (0 <= estimatedPoint.x && estimatedPoint.x <= 4 && 0 <= estimatedPoint.y
                            && estimatedPoint.y <= 4) {
                        Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValue(alphaSide, 0, 0);
                        Board.GetCell(estimatedPoint).SetValue(alphaSide, 0, 10);
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
            if (Board.GetLastAttackResult(alphaSide).contains(1)) {
                // 波高し
                Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValue(alphaSide, 0, 0);
                for (Point point : Board.GetRoundPoints(Board.GetLastAttackPoint(alphaSide))) {
                    Board.GetCell(point).SetValue(alphaSide, 0,
                            Board.GetCell(point).GetValue(alphaSide, 0) + 1);
                }
                // 被移動
                if (Board.IsLastMove(!alphaSide)) {
                } else {
                }
            }
            if (Board.GetLastAttackResult(alphaSide).contains(0)) {
                // 外れ
                Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValue(alphaSide, 0, 0);
                for (Point point : Board.GetRoundPoints(Board.GetLastAttackPoint(alphaSide))) {
                    Board.GetCell(point).SetValue(alphaSide, 0, 0);
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
                    if (Board.GetFilterMoveEnablePoints(alphaSide,
                            preparePoint,
                            Board.GetCrossPoints(preparePoint, 1, 2)).size() != 0) {
                        DoMove(preparePoint, Board.GetRandomPoint(Board.GetFilterMoveEnablePoints(alphaSide,
                                preparePoint,
                                Board.GetCrossPoints(preparePoint, 1, 2))));
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
                        if (!Board.IsMoveEnableVector(alphaSide, movePoint, vectorPoint)) {
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

        DoAttack(Board.GetRandomPoint(Board.GetMaxValuePoints(alphaSide, true, 0)));
        return;

        // Board.WriteDisableTurn();
    }
}
