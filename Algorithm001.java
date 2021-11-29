import java.util.*;

class Algorithm001 extends Interface {
    Algorithm001(boolean alphaSide) {
        super(alphaSide);
        Board.GetCell(0, 0).SetHp(alphaSide, 3);
        Board.GetCell(0, 4).SetHp(alphaSide, 3);
        Board.GetCell(4, 0).SetHp(alphaSide, 3);
        Board.GetCell(4, 4).SetHp(alphaSide, 3);
    }

    public void Think() {
        Board.AttackEnableSearch(alphaSide);
        if (Board.IsLastMove(!alphaSide)) {
            if (alphaSide) {
                Board.ClearValue(alphaSide);
            } else {
                Board.NormalizeValue(alphaSide);
            }
        }
        if (Board.IsLastAttack(!alphaSide)) {
            // 敵に攻撃された
            Board.GetCell(Board.GetLastAttackPoint(!alphaSide)).SetValue(alphaSide, 0);
            for (Point point : Board.PointRound(Board.GetLastAttackPoint(!alphaSide))) {
                Board.GetCell(point).SetValue(alphaSide, Board.GetCell(point).GetValue(alphaSide) + 1);
            }
            switch (Board.GetLastAttackResult(!alphaSide)) {
                case 3:
                case 2:
                    allySumHp--;
                    if (Board.GetLastAttackResult(!alphaSide) == 3) {
                        allyCount--;
                        // 敵に撃沈された
                    } else {
                        // 敵に命中された
                        ArrayList<Point> points = new ArrayList<Point>();
                        for (Point point : Board.PointCross(Board.GetLastAttackPoint(!alphaSide), 2)) {
                            if (Board.IsMoveEnablePoint(alphaSide, Board.GetLastAttackPoint(!alphaSide),
                                    point)) {
                                points.add(point);
                            }
                        }
                        // 移動できる範囲からランダムに移動
                        DoMove(Board.GetLastAttackPoint(!alphaSide), Board.RandomGet(points));
                        return;
                    }
                    break;
                case 1:
                    // 波高しされた

                    break;
            }
        }
        if (Board.IsLastAttack(alphaSide)) {
            // 敵を攻撃した
            switch (Board.GetLastAttackResult(alphaSide)) {
                case 3:
                case 2:
                    enemySumHp--;
                    if (Board.GetLastAttackResult(alphaSide) == 3) {
                        enemyCount--;
                        // 敵を撃沈した
                        Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValue(alphaSide, 0);
                    } else {
                        // 敵を命中した
                        Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValue(alphaSide, 10);
                        if (Board.IsLastMove(!alphaSide)) {
                            // 敵が移動した
                            if (enemyCount == 1) {
                                // 敵が1機のみ
                                Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValue(alphaSide, 0);
                                Board.GetCell(Board.GetLastAttackPoint(alphaSide)
                                        .Plus(Board.GetLastMoveVector(!alphaSide))).SetValue(alphaSide, 10);
                                if (Board.IsAttackEnablePoint(alphaSide, Board.GetLastAttackPoint(alphaSide)
                                        .Plus(Board.GetLastMoveVector(!alphaSide)))) {
                                    // 攻撃可能範囲内なら攻撃する
                                    DoAttack(Board.GetLastAttackPoint(alphaSide)
                                            .Plus(Board.GetLastMoveVector(!alphaSide)));
                                    return;
                                }
                            } else {
                                // 敵が2機以上
                            }
                        } else {
                            // 敵が移動しなかった
                            DoAttack(Board.GetLastAttackPoint(alphaSide), false);
                            return;
                        }
                    }
                    break;
                case 1:
                    // 敵を波高しした
                    Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValue(alphaSide, 0);
                    for (Point point : Board.PointRound(Board.GetLastAttackPoint(alphaSide))) {
                        Board.GetCell(point).SetValue(alphaSide,
                                Board.GetCell(point).GetValue(alphaSide) + 1);
                    }
                    if (Board.IsLastMove(!alphaSide)) {
                        // 敵が移動した

                    } else {
                        // 敵が移動しなかった

                    }
                    break;
                case 0:
                    // 前ターンで敵に命中しなかった
                    Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValue(alphaSide, 0);
                    for (Point point : Board.PointRound(Board.GetLastAttackPoint(alphaSide))) {
                        Board.GetCell(point).SetValue(alphaSide, 0);
                    }
                    break;
            }
        }
        DoAttack(Board.RandomGet(Board.MaxValuePoints(alphaSide, true)), false);
        return;
    }
}
