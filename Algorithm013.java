import java.util.*;

class Algorithm013 extends Interface {
    Algorithm013(boolean alphaSide, boolean isEnemySecret) {
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
            enemyMoveCount++;
            switch (EnemyLastMoveVector().x) {
                case 2:
                case 1:
                    for (int x = EnemyLastMoveVector().x; x < Board.BOARD_SIZE; x++) {
                        for (int y = 0; y < Board.BOARD_SIZE; y++) {
                            int value = Board.GetCell(x, y).GetValue(alphaSide, 0);
                            value = value < 0 ? 0 : value;
                            Board.GetCell(x, y).SetValueForce(alphaSide, 0, value + 1);
                        }
                    }
                    break;
                case -1:
                case -2:
                    for (int x = 0; x < Board.BOARD_SIZE + EnemyLastMoveVector().x; x++) {
                        for (int y = 0; y < Board.BOARD_SIZE; y++) {
                            int value = Board.GetCell(x, y).GetValue(alphaSide, 0);
                            value = value < 0 ? 0 : value;
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
                            value = value < 0 ? 0 : value;
                            Board.GetCell(x, y).SetValueForce(alphaSide, 0, value + 1);
                        }
                    }
                    break;
                case -1:
                case -2:
                    for (int x = 0; x < Board.BOARD_SIZE; x++) {
                        for (int y = 0; y < Board.BOARD_SIZE + EnemyLastMoveVector().y; y++) {
                            int value = Board.GetCell(x, y).GetValue(alphaSide, 0);
                            value = value < 0 ? 0 : value;
                            Board.GetCell(x, y).SetValueForce(alphaSide, 0, value + 1);
                        }
                    }
                    break;
            }
        }

        // 敵軍が攻撃した = 攻撃したポイントの評価値を-1に固定する, 周囲のポイントの評価値に1を追加する, 攻撃したポイントの逆評価値を0に設定する
        if (IsEnemyLastAttack()) {
            Board.GetCell(EnemyLastAttackPoint()).SetValueForce(alphaSide, 0, -1);
            for (Point point : Board.GetRoundPoints(EnemyLastAttackPoint())) {
                Board.GetCell(point).AddValue(alphaSide, 0, 1);
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
            }
            // 自軍が波高しした = 攻撃したポイントの逆評価値を0に設定する, 周囲のポイントに1を追加する
            if (EnemyLastAttackResult().contains(Board.ATTACK_NEAR)) {
                Board.GetCell(EnemyLastAttackPoint()).SetValue(alphaSide, 1, 0);
                for (Point point : Board.GetRoundPoints(EnemyLastAttackPoint())) {
                    Board.GetCell(point).AddValue(alphaSide, 1, 1);
                }
            }
        }

        // 自軍が攻撃した
        if (IsAllyLastAttack()) {
            // 敵軍が撃沈した = 命中したポイントの評価値, 逆評価値を-2に固定する
            if (AllyLastAttackResult().contains(Board.ATTACK_SINK)) {
                Board.GetCell(AllyLastAttackPoint()).SetValueForce(alphaSide, 0, -2);
                Board.GetCell(AllyLastAttackPoint()).SetValueForce(alphaSide, 1, -2);
                for (Point point : Board.GetRoundPoints(AllyLastAttackPoint())) {
                    Board.GetCell(point).AddValue(alphaSide, 0, -1);
                }
            }
            // 敵軍が命中した = 命中したポイントの評価値を20に設定する, 命中したポイントのX軸Y軸対称のポイントの評価値に5を追加する,
            // = 命中したポイントのX軸, Y軸対称のポイントの評価値に3を追加する
            if (AllyLastAttackResult().contains(Board.ATTACK_HIT)) {
                Board.GetCell(AllyLastAttackPoint()).SetValueForce(alphaSide, 0, 20);
                for (Point point : Board.GetRoundPoints(AllyLastAttackPoint())) {
                    Board.GetCell(point).AddValue(alphaSide, 0, -1);
                }

                if (enemyMoveCount == 0) {
                    Point xySymmetryPoint = new Point(
                            Math.abs(AllyLastAttackPoint().x - (Board.BOARD_SIZE - 1)),
                            Math.abs(AllyLastAttackPoint().y - (Board.BOARD_SIZE - 1)));
                    Board.GetCell(xySymmetryPoint).AddValue(alphaSide, 0, 5);

                    Point xSymmetryPoint = new Point(
                            Math.abs(AllyLastAttackPoint().x - (Board.BOARD_SIZE - 1)),
                            AllyLastAttackPoint().y);
                    Board.GetCell(xSymmetryPoint).AddValue(alphaSide, 0, 3);

                    Point ySymmetryPoint = new Point(
                            Math.abs(AllyLastAttackPoint().x - (Board.BOARD_SIZE - 1)),
                            AllyLastAttackPoint().y);
                    Board.GetCell(ySymmetryPoint).AddValue(alphaSide, 0, 3);
                }

            }
            // 敵軍が波高しした = 攻撃したポイントの評価値を-1に固定する, 周囲のポイントの評価値に1を追加する
            if (AllyLastAttackResult().contains(Board.ATTACK_NEAR)) {
                Board.GetCell(AllyLastAttackPoint()).SetValueForce(alphaSide, 0, -1);
                for (Point point : Board.GetRoundPoints(AllyLastAttackPoint())) {
                    Board.GetCell(point).AddValue(alphaSide, 0, 1);
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

        // 自軍が攻撃した
        if (IsAllyLastAttack()) {
            // 敵軍が命中した
            if (AllyLastAttackResult().contains(Board.ATTACK_HIT)) {
                // 敵軍が移動した = 命中したポイントに移動ベクトルを足したポイントが範囲内ならそのポイントに移動したと判断し、攻撃可能範囲内なら攻撃する (A)
                // 敵軍が移動しなかった = 命中したポイントにもう一度攻撃する
                if (IsEnemyLastMove()) {
                    Point estimatedPoint = AllyLastAttackPoint()
                            .Plus(EnemyLastMoveVector());

                    if (estimatedPoint.IsRange()) {
                        Board.GetCell(AllyLastAttackPoint()).SetValueForce(alphaSide, 0, 0);
                        Board.GetCell(estimatedPoint).SetValueForce(alphaSide, 0, 20);
                        if (Board.IsEnableAttackPoint(alphaSide, estimatedPoint)) {
                            estimatedAttackedFlag = true;
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
                if (estimatedAttackedFlag) {
                    estimatedAttackedFlag = false;
                    if (Board.IsEnableAttackPoint(alphaSide, estimatedBeforePoint)) {
                        DoAttack(estimatedBeforePoint);
                        estimatedBeforePoint = null;
                        return;
                    }
                }
                if (AllyLastAttackResult().contains(Board.ATTACK_NEAR)) {

                }
            }
        }

        if (prepareTurned && Board.IsEnableAttackPoint(alphaSide, preparePoint)) {
            prepareTurned = false;
            DoAttack(preparePoint);
            preparePoint = null;
            return;
        }

        ArrayList<Point> maxValuePoints = new ArrayList<Point>(
                Board.GetPointValues(alphaSide, null, 0, 1).keySet());
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
                                    || point.equals(movePoint.Plus(moveVector))) {
                                moveVector = moveVector.Divide(2);
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