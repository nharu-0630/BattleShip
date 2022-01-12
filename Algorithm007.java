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
        if (IsEnemyLastAttack()) {
            if (EnemyLastAttackResult().contains(Board.ATTACK_SINK)) {
                allySumHp--;
                allyCount--;
                if (allyCount == 0) {
                    Board.Interrupt();
                }
            }
            if (EnemyLastAttackResult().contains(Board.ATTACK_HIT)) {
                allySumHp--;
            }
        }
        if (IsAllyLastAttack()) {
            if (AllyLastAttackResult().contains(Board.ATTACK_SINK)) {
                enemySumHp--;
                enemyCount--;
                if (enemyCount == 0) {
                    Board.Interrupt();
                }
            }
            if (AllyLastAttackResult().contains(Board.ATTACK_HIT)) {
                enemySumHp--;
            }
        }
        Board.SearchEnableAttackPoints(alphaSide);

        // 敵軍が移動した = 移動先の可能性があるポイントの評価値に1を追加する
        if (IsEnemyLastMove()) {
            switch (EnemyLastMoveVector().x) {
                case 2:
                case 1:
                    for (int x = EnemyLastMoveVector().x; x < Board.BOARD_SIZE; x++) {
                        for (int y = 0; y < Board.BOARD_SIZE; y++) {
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
                    for (int x = 0; x < Board.BOARD_SIZE + EnemyLastMoveVector().x; x++) {
                        for (int y = 0; y < Board.BOARD_SIZE; y++) {
                            int value = Board.GetCell(x, y).GetValue(alphaSide, 0);
                            if (value < 0) {
                                value = 0;
                            }
                            Board.GetCell(x, y).SetValueForce(alphaSide, 0, value + 1);
                        }
                    }
                    break;
            }
            switch (EnemyLastMoveVector().y) {
                case 2:
                case 1:
                    for (int x = 0; x < Board.BOARD_SIZE; x++) {
                        for (int y = EnemyLastMoveVector().y; y < Board.BOARD_SIZE; y++) {
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
                    for (int x = 0; x < Board.BOARD_SIZE; x++) {
                        for (int y = 0; y < Board.BOARD_SIZE + EnemyLastMoveVector().y; y++) {
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
        if (IsAllyLastAttack()) {
            // 敵軍が撃沈した = 命中したポイントの評価値, 逆評価値を-2に固定する
            if (AllyLastAttackResult().contains(Board.ATTACK_SINK)) {
                Board.GetCell(AllyLastAttackPoint()).SetValueForce(alphaSide, 0, -2);
                Board.GetCell(AllyLastAttackPoint()).SetValueForce(alphaSide, 1, -2);
            }
            // 敵軍が命中した = 命中したポイントの評価値を10に設定する
            if (AllyLastAttackResult().contains(Board.ATTACK_HIT)) {
                Board.GetCell(AllyLastAttackPoint()).SetValueForce(alphaSide, 0, 10);
                // 敵軍が移動した = 命中したポイントに移動ベクトルを足したポイントが範囲内ならそのポイントに移動したと判断し、攻撃可能範囲内なら攻撃する (A)
                // 敵軍が移動しなかった = 命中したポイントにもう一度攻撃する
                if (Board.IsLastMove(!alphaSide)) {
                    Point estimatedPoint = AllyLastAttackPoint()
                            .Plus(EnemyLastMoveVector());

                    if (estimatedPoint.IsRange()) {
                        Board.GetCell(AllyLastAttackPoint()).SetValueForce(alphaSide, 0, 0);
                        Board.GetCell(estimatedPoint).SetValueForce(alphaSide, 0, 10);
                        if (Board.IsEnableAttackPoint(alphaSide, estimatedPoint)) {
                            estimatedAttacked = true;
                            estimatedBeforePoint = AllyLastAttackPoint();
                            DoAttack(estimatedPoint);
                            return;
                        }
                    }
                } else {
                    if (Board.IsEnableAttackPoint(alphaSide, AllyLastAttackPoint())) {
                        DoAttack(AllyLastAttackPoint());
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
            if (AllyLastAttackResult().contains(Board.ATTACK_NEAR)) {
                Board.GetCell(AllyLastAttackPoint()).SetValueForce(alphaSide, 0, -1);
                for (Point point : Board.GetRoundPoints(AllyLastAttackPoint())) {
                    Board.GetCell(point).SetValue(alphaSide, 0, Board.GetCell(point).GetValue(alphaSide, 0) + 1);
                }
            }
            // 敵軍が外れした = 攻撃したポイント, 周囲のポイントの評価値を-1に固定する
            if (AllyLastAttackResult().contains(Board.ATTACK_NOHIT)) {
                Board.GetCell(AllyLastAttackPoint()).SetValueForce(alphaSide, 0, -1);
                for (Point point : Board.GetRoundPoints(AllyLastAttackPoint())) {
                    Board.GetCell(point).SetValueForce(alphaSide, 0, -1);
                }
            }
        }

        // 敵軍が攻撃した = 攻撃したポイントの評価値を-1に固定する, 周囲のポイントの評価値に1を追加する, 攻撃したポイントの逆評価値を0に設定する
        if (IsEnemyLastAttack()) {
            Board.GetCell(EnemyLastAttackPoint()).SetValueForce(alphaSide, 0, -1);
            for (Point point : Board.GetRoundPoints(EnemyLastAttackPoint())) {
                Board.GetCell(point).SetValue(alphaSide, 0, Board.GetCell(point).GetValue(alphaSide, 0) + 1);
            }
            Board.GetCell(EnemyLastAttackPoint()).SetValue(alphaSide, 1, 0);
            // 自軍が撃沈した = 命中したポイントの評価値, 逆評価値を-2に固定する
            if (EnemyLastAttackResult().contains(Board.ATTACK_SINK)) {
                Board.GetCell(EnemyLastAttackPoint()).SetValueForce(alphaSide, 0, -2);
                Board.GetCell(EnemyLastAttackPoint()).SetValueForce(alphaSide, 1, -2);
            }
            // 自軍が命中した = 命中したポイントの逆評価値を10に設定する
            if (EnemyLastAttackResult().contains(Board.ATTACK_HIT)) {
                Board.GetCell(EnemyLastAttackPoint()).SetValue(alphaSide, 1, 10);
                ArrayList<Point> points = new ArrayList<Point>();
                for (Point point : Board.GetCrossPoints(EnemyLastAttackPoint(), 2, 2)) {
                    if (Board.IsMoveEnablePoint(alphaSide, EnemyLastAttackPoint(),
                            point)) {
                        points.add(point);
                    }
                }
                if (points.size() != 0) {
                    DoMove(EnemyLastAttackPoint(), Board.GetRandomPoint(
                            new ArrayList<Point>(Board.GetPointValues(alphaSide, points, 1, -1).keySet())));
                    return;
                }
            }
            // 自軍が波高しした = 攻撃したポイントの逆評価値を0に設定する, 周囲のポイントに1を追加する
            if (EnemyLastAttackResult().contains(Board.ATTACK_NEAR)) {
                Board.GetCell(EnemyLastAttackPoint()).SetValue(alphaSide, 1, 0);
                for (Point point : Board.GetRoundPoints(EnemyLastAttackPoint())) {
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
                            vectorPoint = vectorPoint.Divide(2);
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
