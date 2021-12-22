import java.util.*;

class Algorithm004 extends Interface {
    Algorithm004(boolean alphaSide, boolean isEnemySecret) {
        super(alphaSide, isEnemySecret);
    }

    private boolean estimatedAttacked = false;
    private Point estimatedBeforePoint = null;
    private boolean prepareTurned = false;
    private Point preparePoint = null;

    public void SetParameter(double[] parameters) {

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
        Board.SearchAttackPoints(alphaSide);

        if (Board.IsLastAttack(!alphaSide)) {
            // 敵に攻撃された
            Board.GetCell(Board.GetLastAttackPoint(!alphaSide)).SetValue(alphaSide, 0, 0);
            for (Point point : Board.GetRoundPoints(Board.GetLastAttackPoint(!alphaSide))) {
                Board.GetCell(point).SetValue(alphaSide, 0, Board.GetCell(point).GetValue(alphaSide, 0) + 1);
            }
            if (Board.GetLastAttackResult(!alphaSide).contains(3)) {
                // 敵に撃沈された
            }
            if (Board.GetLastAttackResult(!alphaSide).contains(2)) {
                // 敵に命中された
                ArrayList<Point> points = new ArrayList<Point>();
                for (Point point : Board.GetCrossPoints(Board.GetLastAttackPoint(!alphaSide), 2, 2)) {
                    if (Board.IsMoveEnablePoint(alphaSide, Board.GetLastAttackPoint(!alphaSide),
                            point)) {
                        points.add(point);
                    }
                }
                // 移動できる範囲からランダムに移動
                if (points.size() != 0) {
                    DoMove(Board.GetLastAttackPoint(!alphaSide), Board.GetRandomPoint(points));
                    return;
                }
            }
            if (Board.GetLastAttackResult(!alphaSide).contains(1)) {
                // 敵に波高しされた
            }
        }
        if (Board.IsLastAttack(alphaSide)) {
            // 敵を攻撃した
            if (Board.GetLastAttackResult(alphaSide).contains(3)) {
                // 敵を撃沈した
                Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValue(alphaSide, 0, 0);
            }
            if (Board.GetLastAttackResult(alphaSide).contains(2)) {
                // 敵を命中した
                Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValue(alphaSide, 0, 10);
                if (Board.IsLastMove(!alphaSide)) {
                    // 敵が移動した
                    Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValue(alphaSide, 0, 0);
                    Board.GetCell(Board.GetLastAttackPoint(alphaSide)
                            .Plus(Board.GetLastMoveVector(!alphaSide))).SetValue(alphaSide, 0, 10);
                    if (Board.IsAttackPoint(alphaSide, Board.GetLastAttackPoint(alphaSide)
                            .Plus(Board.GetLastMoveVector(!alphaSide)))) {
                        // 攻撃が可能なら攻撃する
                        estimatedAttacked = true;
                        estimatedBeforePoint = Board.GetLastAttackPoint(alphaSide);
                        DoAttack(Board.GetLastAttackPoint(alphaSide)
                                .Plus(Board.GetLastMoveVector(!alphaSide)));
                        return;
                    }
                } else {
                    // 敵が移動しなかった
                    DoAttack(Board.GetLastAttackPoint(alphaSide));
                    return;
                }
            } else {
                if (estimatedAttacked) {
                    estimatedAttacked = false;
                    DoAttack(estimatedBeforePoint);
                    estimatedBeforePoint = null;
                    return;
                }
            }
            if (Board.GetLastAttackResult(alphaSide).contains(1)) {
                // 敵を波高しした
                Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValue(alphaSide, 0, 0);
                for (Point point : Board.GetRoundPoints(Board.GetLastAttackPoint(alphaSide))) {
                    Board.GetCell(point).SetValue(alphaSide, 0,
                            Board.GetCell(point).GetValue(alphaSide, 0) + 1);
                }
                if (Board.IsLastMove(!alphaSide)) {
                    // 敵が移動した
                } else {
                    // 敵が移動しなかった
                }
            }
            if (Board.GetLastAttackResult(alphaSide).contains(0)) {
                Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValue(alphaSide, 0, 0);
                for (Point point : Board.GetRoundPoints(Board.GetLastAttackPoint(alphaSide))) {
                    Board.GetCell(point).SetValue(alphaSide, 0, 0);
                }
            }
        }
        if (prepareTurned && Board.IsAttackPoint(alphaSide, preparePoint)) {
            prepareTurned = false;
            DoAttack(preparePoint);
            preparePoint = null;
            return;
        }
        if (Board.GetCell(Board.GetMaxValuePoints(alphaSide, false, 0).get(0)).GetValue(alphaSide, 0) > 10) {
            if (Board.GetCell(Board.GetMaxValuePoints(alphaSide, false, 0).get(0)).GetValue(alphaSide, 0) != Board
                    .GetCell(Board.GetMaxValuePoints(alphaSide, true, 0).get(0)).GetValue(alphaSide, 0)) {
                preparePoint = Board.GetRandomPoint(Board.GetMaxValuePoints(alphaSide, false, 0));
                if (Board.GetCell(preparePoint).IsAlive(alphaSide)) {
                    DoMove(preparePoint, Board.GetRandomPoint(Board.GetCrossPoints(preparePoint, 1, 1)));
                    return;
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
    }
}
