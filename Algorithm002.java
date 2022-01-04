import java.util.*;

class Algorithm002 extends Interface {
    Algorithm002(boolean alphaSide, boolean isEnemySecret) {
        super(alphaSide, isEnemySecret);
    }

    public void SetParameter(int[] parameters) {

    }

    public void Think() {
        if (Board.IsLastAttack(!alphaSide)) {
            if (Board.GetLastAttackResult(!alphaSide).contains(Board.ATTACK_SINK)) {
                allySumHp--;
                allyCount--;
                if (allyCount == 0) {
                    Board.Interrupt();
                }
            }
            if (Board.GetLastAttackResult(!alphaSide).contains(Board.ATTACK_HIT)) {
                allySumHp--;
            }
        }
        if (Board.IsLastAttack(alphaSide)) {
            if (Board.GetLastAttackResult(alphaSide).contains(Board.ATTACK_SINK)) {
                enemySumHp--;
                enemyCount--;
                if (enemyCount == 0) {
                    Board.Interrupt();
                }
            }
            if (Board.GetLastAttackResult(alphaSide).contains(Board.ATTACK_HIT)) {
                enemySumHp--;
            }
        }
        Board.SearchEnableAttackPoints(alphaSide);

        if (Board.IsLastAttack(!alphaSide)) {
            // 敵に攻撃された
            Board.GetCell(Board.GetLastAttackPoint(!alphaSide)).SetValue(alphaSide, 0, 0);
            for (Point point : Board.GetRoundPoints(Board.GetLastAttackPoint(!alphaSide))) {
                Board.GetCell(point).SetValue(alphaSide, 0, Board.GetCell(point).GetValue(alphaSide, 0) + 1);
            }
            if (Board.GetLastAttackResult(!alphaSide).contains(Board.ATTACK_SINK)) {
                // 敵に撃沈された
            }
            if (Board.GetLastAttackResult(!alphaSide).contains(Board.ATTACK_HIT)) {
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
            if (Board.GetLastAttackResult(!alphaSide).contains(Board.ATTACK_NEAR)) {
                // 敵に波高しされた
            }
        }
        if (Board.IsLastAttack(alphaSide)) {
            // 敵を攻撃した
            if (Board.GetLastAttackResult(alphaSide).contains(Board.ATTACK_SINK)) {
                // 敵を撃沈した
                Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValue(alphaSide, 0, -1);
            }
            if (Board.GetLastAttackResult(alphaSide).contains(Board.ATTACK_HIT)) {
                // 敵を命中した
                Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValue(alphaSide, 0, 10);
                if (Board.IsLastMove(!alphaSide)) {
                    // 敵が移動した
                    // if (enemyCount == 1) {
                    // 敵が1機のみ
                    if (Board.GetLastAttackPoint(alphaSide)
                            .Plus(Board.GetLastMoveVector(!alphaSide)).IsRange()) {
                        Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValue(alphaSide, 0, 0);
                        Board.GetCell(Board.GetLastAttackPoint(alphaSide)
                                .Plus(Board.GetLastMoveVector(!alphaSide))).SetValue(alphaSide, 0, 10);
                        if (Board.IsEnableAttackPoint(alphaSide, Board.GetLastAttackPoint(alphaSide)
                                .Plus(Board.GetLastMoveVector(!alphaSide)))) {
                            // 攻撃が可能なら攻撃する
                            DoAttack(Board.GetLastAttackPoint(alphaSide)
                                    .Plus(Board.GetLastMoveVector(!alphaSide)));
                            return;
                        }
                    }
                    // } else {
                    // // 敵が2機以上
                    // }
                } else {
                    // 敵が移動しなかった
                    if (Board.IsEnableAttackPoint(alphaSide, Board.GetLastAttackPoint(alphaSide))) {
                        DoAttack(Board.GetLastAttackPoint(alphaSide));
                        return;
                    }
                }
            }
            if (Board.GetLastAttackResult(alphaSide).contains(Board.ATTACK_NEAR)) {
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
            if (Board.GetLastAttackResult(alphaSide).contains(Board.ATTACK_NOHIT)) {
                Board.GetCell(Board.GetLastAttackPoint(alphaSide)).SetValue(alphaSide, 0, 0);
                for (Point point : Board.GetRoundPoints(Board.GetLastAttackPoint(alphaSide))) {
                    Board.GetCell(point).SetValue(alphaSide, 0, 0);
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
