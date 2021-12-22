import java.util.*;

class Algorithm002 extends Interface {
    Algorithm002(boolean alphaSide, boolean isEnemySecret) {
        super(alphaSide, isEnemySecret);
    }

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
                for (Point point : Board.GetCrossPoints(Board.GetLastAttackPoint(!alphaSide), 1, 2)) {
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
                    // if (enemyCount == 1) {
                    // 敵が1機のみ
                    Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValue(alphaSide, 0, 0);
                    Board.GetCell(Board.GetLastAttackPoint(alphaSide)
                            .Plus(Board.GetLastMoveVector(!alphaSide))).SetValue(alphaSide, 0, 10);
                    if (Board.IsAttackPoint(alphaSide, Board.GetLastAttackPoint(alphaSide)
                            .Plus(Board.GetLastMoveVector(!alphaSide)))) {
                        // 攻撃が可能なら攻撃する
                        DoAttack(Board.GetLastAttackPoint(alphaSide)
                                .Plus(Board.GetLastMoveVector(!alphaSide)));
                        return;
                    }
                    // } else {
                    // // 敵が2機以上
                    // }
                } else {
                    // 敵が移動しなかった
                    DoAttack(Board.GetLastAttackPoint(alphaSide));
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
        if (Board.GetCell(Board.GetMaxValuePoints(alphaSide, true, 0).get(0)).GetValue(alphaSide, 0) > 10) {
            DoAttack(Board.GetRandomPoint(Board.GetMaxValuePoints(alphaSide, true, 0)));
            return;
        } else {
            Point oldPoint = Board.GetRandomPoint(Board.GetShipPoints(alphaSide));
            ArrayList<Point> points = new ArrayList<Point>();
            for (Point point : Board.GetCrossPoints(oldPoint, 1, 2)) {
                if (Board.IsMoveEnablePoint(alphaSide, oldPoint, point)) {
                    points.add(point);
                }
            }
            DoMove(oldPoint, Board.GetRandomPoint(points));
            return;
        }
    }
}
