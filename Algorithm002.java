import java.util.*;

class Algorithm002 extends Interface {
    Algorithm002(boolean alphaSide, boolean isEnemySecret) {
        super(alphaSide, isEnemySecret);
    }

    private double attackProbability;

    public void SetParameter(double[] parameters) {
        this.attackProbability = parameters[0];
    }

    public void Think() {
        if (Board.IsLastAttack(!alphaSide)) {
            switch (Board.GetLastAttackResult(!alphaSide)) {
                case 3:
                    allySumHp--;
                    allyCount--;
                    break;
                case 2:
                    allySumHp--;
                    break;
            }
        }
        if (Board.IsLastAttack(alphaSide)) {
            switch (Board.GetLastAttackResult(alphaSide)) {
                case 3:
                    enemySumHp--;
                    enemyCount--;
                    break;
                case 2:
                    enemySumHp--;
                    break;
            }
        }
        Board.SearchAttackPoints(alphaSide);

        if (Board.IsLastAttack(!alphaSide)) {
            // 敵に攻撃された
            Board.GetCell(Board.GetLastAttackPoint(!alphaSide)).SetValue(alphaSide, 0);
            for (Point point : Board.GetRoundPoints(Board.GetLastAttackPoint(!alphaSide))) {
                Board.GetCell(point).SetValue(alphaSide, Board.GetCell(point).GetValue(alphaSide) + 1);
            }
            switch (Board.GetLastAttackResult(!alphaSide)) {
                case 3:
                    // 敵に撃沈された
                    break;
                case 2:
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
                    return;
                case 1:
                    // 波高しされた

                    break;
            }
        }
        if (Board.IsLastAttack(alphaSide)) {
            // 敵を攻撃した
            switch (Board.GetLastAttackResult(alphaSide)) {
                case 3:
                    // 敵を撃沈した
                    Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValue(alphaSide, 0);
                    break;
                case 2:
                    // 敵を命中した
                    Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValue(alphaSide, 10);
                    if (Board.IsLastMove(!alphaSide)) {
                        Random random = new Random();
                        // 敵が移動した
                        if (enemyCount == 1 || random.nextDouble() < attackProbability) {
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
                    break;
                case 1:
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
                    break;
                case 0:
                    // 前ターンで敵に命中しなかった
                    Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValue(alphaSide, 0);
                    for (Point point : Board.GetRoundPoints(Board.GetLastAttackPoint(alphaSide))) {
                        Board.GetCell(point).SetValue(alphaSide, 0);
                    }
                    break;
            }
        }
        Random random = new Random();
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
