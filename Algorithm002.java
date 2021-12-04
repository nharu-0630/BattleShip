import java.util.*;

class Algorithm002 extends Interface {
    Algorithm002(boolean alphaSide, boolean isEnemySecret) {
        super(alphaSide, isEnemySecret);
    }

    private Random random = new Random();
    private double attackProbability;

    public void SetParameter(double[] parameters) {
        attackProbability = parameters[0];
    }

    public void Think() {
        if (Board.IsLastAttack(!alphaSide)) {
            if (Board.GetLastAttackResult(!alphaSide).contains(3)) {
                allySumHp--;
                allyCount--;
            }
            if (Board.GetLastAttackResult(!alphaSide).contains(2)) {
                allySumHp--;
            }
        }
        if (Board.IsLastAttack(alphaSide)) {
            if (Board.GetLastAttackResult(alphaSide).contains(3)) {
                enemySumHp--;
                enemyCount--;
            }
            if (Board.GetLastAttackResult(alphaSide).contains(2)) {
                enemySumHp--;
            }
        }
        Board.SearchAttackPoints(alphaSide);

        if (Board.IsLastAttack(!alphaSide)) {
            // 敵に攻撃された
            Board.GetCell(Board.GetLastAttackPoint(!alphaSide)).SetValue(alphaSide, 0);
            for (Point point : Board.GetRoundPoints(Board.GetLastAttackPoint(!alphaSide))) {
                Board.GetCell(point).SetValue(alphaSide, Board.GetCell(point).GetValue(alphaSide) + 1);
            }
            if (Board.GetLastAttackResult(!alphaSide).contains(3)) {
                // 敵に撃沈された
            }
            if (Board.GetLastAttackResult(!alphaSide).contains(2)) {
                // 敵に命中された
                ArrayList<Point> points = new ArrayList<Point>();
                for (Point point : Board.GetCrossPoints(Board.GetLastAttackPoint(!alphaSide), 2)) {
                    if (Board.IsMoveEnablePoint(alphaSide, Board.GetLastAttackPoint(!alphaSide),
                            point)) {
                        points.add(point);
                    }
                }
                // 移動できる範囲からランダムに移動
                DoMove(Board.GetLastAttackPoint(!alphaSide), Board.GetRandomPoint(points));
            }
            if (Board.GetLastAttackResult(!alphaSide).contains(1)) {
                // 敵に波高しされた
            }
        }
        if (Board.IsLastAttack(alphaSide)) {
            // 敵を攻撃した
            if (Board.GetLastAttackResult(alphaSide).contains(3)) {
                // 敵を撃沈した
                Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValue(alphaSide, 0);
            }
            if (Board.GetLastAttackResult(alphaSide).contains(2)) {
                // 敵を命中した
                Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValue(alphaSide, 10);
                if (Board.IsLastMove(!alphaSide)) {
                    // 敵が移動した
                    if (enemyCount == 1) {
                        // 敵が1機のみ
                        Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValue(alphaSide, 0);
                        Board.GetCell(Board.GetLastAttackPoint(alphaSide)
                                .Plus(Board.GetLastMoveVector(!alphaSide))).SetValue(alphaSide, 10);
                        if (Board.IsAttackPoint(alphaSide, Board.GetLastAttackPoint(alphaSide)
                                .Plus(Board.GetLastMoveVector(!alphaSide)))) {
                            // 攻撃が可能なら攻撃する
                            DoAttack(Board.GetLastAttackPoint(alphaSide)
                                    .Plus(Board.GetLastMoveVector(!alphaSide)));
                            return;
                        }
                    } else {
                        // 敵が2機以上
                    }
                } else {
                    // 敵が移動しなかった
                    DoAttack(Board.GetLastAttackPoint(alphaSide));
                    return;
                }
            }
            if (Board.GetLastAttackResult(alphaSide).contains(1)) {
                // 敵を波高しした
                Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValue(alphaSide, 0);
                for (Point point : Board.GetRoundPoints(Board.GetLastAttackPoint(alphaSide))) {
                    Board.GetCell(point).SetValue(alphaSide,
                            Board.GetCell(point).GetValue(alphaSide) + 1);
                }
                if (Board.IsLastMove(!alphaSide)) {
                    // 敵が移動した
                } else {
                    // 敵が移動しなかった
                }
            }
            if (Board.GetLastAttackResult(alphaSide).contains(0)) {
                Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValue(alphaSide, 0);
                for (Point point : Board.GetRoundPoints(Board.GetLastAttackPoint(alphaSide))) {
                    Board.GetCell(point).SetValue(alphaSide, 0);
                }
            }
        }
        if (random.nextDouble() <= attackProbability) {
            DoAttack(Board.GetRandomPoint(Board.GetMaxValuePoints(alphaSide, true)));
        } else {
            Point oldPoint = Board.GetRandomPoint(Board.GetShipPoints(alphaSide));
            ArrayList<Point> points = new ArrayList<Point>();
            for (Point point : Board.GetCrossPoints(oldPoint, 2)) {
                if (Board.IsMoveEnablePoint(alphaSide, oldPoint, point)) {
                    points.add(point);
                }
            }
            DoMove(oldPoint, Board.GetRandomPoint(points));
        }
        return;
    }
}