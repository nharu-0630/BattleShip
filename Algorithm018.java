import java.util.*;

class Algorithm018 extends Interface {
    Algorithm018(boolean alphaSide, boolean isEnemySecret) {
        super(alphaSide, isEnemySecret);
        Board.InitializeValues(alphaSide, 1);
    }

    private int estimatedAttackedStatus = 0;
    private Point estimatedBeforePoint = null;
    private boolean prepareTurned = false;
    private Point preparePoint = null;
    private boolean fakeMoveEnable = true;

    private int enemyMoveCount = 0;

    private int enemyFakeMoveCount = 0;
    private int enemyRealMoveCount = 0;
    private int enemyNoMoveCount = 0;

    private int allyAttackType = 0;

    private final int TYPE_SEARCH = 1;
    private final int TYPE_HIT = 2;
    private final int TYPE_FAKEMOVE = 3;
    private final int TYPE_REALMOVE = 4;
    private final int TYPE_NOMOVE = 5;

    private int allyMoveCount = 0;

    private int allyEscapeMoveCount = 0;
    private int allyAttackMoveCount = 0;
    private int allyFreeMoveCount = 0;

    private int allyMoveType = 0;

    private final int TYPE_ESCAPE = 1;
    private final int TYPE_ATTACK = 2;
    private final int TYPE_FREE = 3;

    public void SetParameter(int[] parameters) {

    }

    private void MoveValue(int layer, Point vector) {
        ArrayList<Point> excludePoints = new ArrayList<Point>();
        for (int x = 0; x < Board.BOARD_SIZE; x++) {
            for (int y = 0; y < Board.BOARD_SIZE; y++) {
                Point point = (new Point(x, y)).Plus(vector);
                if (Board.GetCell(x, y).GetValue(alphaSide, layer) == -1
                        || Board.GetCell(x, y).GetValue(alphaSide, layer) == -2) {
                    if (point.IsRange() && Board.GetCell(point).GetValue(alphaSide, layer) == -1) {
                        excludePoints.add(point);
                    }
                }
                if (vector.Distance() == 2) {
                    if ((new Point(x, y).Plus(vector.Divide(2))).IsRange()) {
                        if (Board.GetCell(new Point(x, y).Plus(vector.Divide(2)))
                                .GetValue(alphaSide, layer) == -2) {
                            excludePoints.add(point);
                        }
                    }
                }
            }
        }

        switch (vector.x) {
            case 2:
            case 1:
                for (int x = vector.x; x < Board.BOARD_SIZE; x++) {
                    for (int y = 0; y < Board.BOARD_SIZE; y++) {
                        if (excludePoints.contains(new Point(x, y))) {
                            continue;
                        }
                        int value = CalcMoveValue(layer, new Point(x, y), vector);
                        Board.GetCell(x, y).SetValueForce(alphaSide, layer, value);
                    }
                }
                break;
            case -1:
            case -2:
                for (int x = 0; x < Board.BOARD_SIZE + vector.x; x++) {
                    for (int y = 0; y < Board.BOARD_SIZE; y++) {
                        if (excludePoints.contains(new Point(x, y))) {
                            continue;
                        }
                        int value = CalcMoveValue(layer, new Point(x, y), vector);
                        Board.GetCell(x, y).SetValueForce(alphaSide, layer, value);
                    }
                }
                break;
        }
        switch (vector.y) {
            case 2:
            case 1:
                for (int x = 0; x < Board.BOARD_SIZE; x++) {
                    for (int y = vector.y; y < Board.BOARD_SIZE; y++) {
                        if (excludePoints.contains(new Point(x, y))) {
                            continue;
                        }
                        int value = CalcMoveValue(layer, new Point(x, y), vector);
                        Board.GetCell(x, y).SetValueForce(alphaSide, layer, value);
                    }
                }
                break;
            case -1:
            case -2:
                for (int x = 0; x < Board.BOARD_SIZE; x++) {
                    for (int y = 0; y < Board.BOARD_SIZE + vector.y; y++) {
                        if (excludePoints.contains(new Point(x, y))) {
                            continue;
                        }
                        int value = CalcMoveValue(layer, new Point(x, y), vector);
                        Board.GetCell(x, y).SetValueForce(alphaSide, layer, value);
                    }
                }
                break;
        }
    }

    private int CalcMoveValue(int layer, Point point, Point vector) {
        int newValue = Board.GetCell(point).GetValue(alphaSide, layer);
        newValue = newValue < 0 ? 0 : newValue;
        int oldValue = Board.GetCell(point.Minus(vector)).GetValue(alphaSide, layer);
        if (0 <= oldValue && oldValue < 5) {
            newValue += 0;
        } else if (5 <= oldValue && oldValue < 10) {
            newValue += 3;
        } else if (10 <= oldValue && oldValue < 20) {
            newValue += 5;
        } else if (20 <= oldValue) {
            newValue += 10;
        }
        return newValue;
    }

    private void SwipeValue(int layer, Point vector) {
        switch (vector.x) {
            case 2:
            case 1:
                for (int x = Board.BOARD_SIZE - 1; x >= vector.x; x--) {
                    for (int y = 0; y < Board.BOARD_SIZE; y++) {
                        int value = Board.GetCell(x - vector.x, y).GetValue(alphaSide, layer);
                        value = value == -2 ? -1 : value;
                        if (vector.x == 2 && Board.GetCell(x - 1, y).GetValue(alphaSide, layer) == -2) {
                            value = -1;
                        }
                        Board.GetCell(x, y).SetValueForce(alphaSide, layer, value);
                    }
                }
                for (int x = 0; x < vector.x; x++) {
                    for (int y = 0; y < Board.BOARD_SIZE; y++) {
                        Board.GetCell(x, y).SetValueForce(alphaSide, layer, -1);
                    }
                }
                break;
            case -1:
            case -2:
                for (int x = 0; x < Board.BOARD_SIZE + vector.x; x++) {
                    for (int y = 0; y < Board.BOARD_SIZE; y++) {
                        int value = Board.GetCell(x - vector.x, y).GetValue(alphaSide, layer);
                        value = value == -2 ? -1 : value;
                        if (vector.x == -2 && Board.GetCell(x + 1, y).GetValue(alphaSide, layer) == -2) {
                            value = -1;
                        }
                        Board.GetCell(x, y).SetValueForce(alphaSide, layer, value);
                    }
                }
                for (int x = Board.BOARD_SIZE + vector.x; x < Board.BOARD_SIZE; x++) {
                    for (int y = 0; y < Board.BOARD_SIZE; y++) {
                        Board.GetCell(x, y).SetValueForce(alphaSide, layer, -1);
                    }
                }
                break;
        }
        switch (vector.y) {
            case 2:
            case 1:
                for (int x = 0; x < Board.BOARD_SIZE; x++) {
                    for (int y = Board.BOARD_SIZE - 1; y >= vector.y; y--) {
                        int value = Board.GetCell(x, y - vector.y).GetValue(alphaSide, layer);
                        value = value == -2 ? -1 : value;
                        if (vector.y == 2 && Board.GetCell(x, y - 1).GetValue(alphaSide, layer) == -2) {
                            value = -1;
                        }
                        Board.GetCell(x, y).SetValueForce(alphaSide, layer, value);
                    }
                }
                for (int x = 0; x < Board.BOARD_SIZE; x++) {
                    for (int y = 0; y < vector.y; y++) {
                        Board.GetCell(x, y).SetValueForce(alphaSide, layer, -1);
                    }
                }
                break;
            case -1:
            case -2:
                for (int x = 0; x < Board.BOARD_SIZE; x++) {
                    for (int y = 0; y < Board.BOARD_SIZE + vector.y; y++) {
                        int value = Board.GetCell(x, y - vector.y).GetValue(alphaSide, layer);
                        value = value == -2 ? -1 : value;
                        if (vector.y == -2 && Board.GetCell(x, y + 1).GetValue(alphaSide, layer) == -2) {
                            value = -1;
                        }
                        Board.GetCell(x, y).SetValueForce(alphaSide, layer, value);
                    }
                }
                for (int x = 0; x < Board.BOARD_SIZE; x++) {
                    for (int y = Board.BOARD_SIZE + vector.y; y < Board.BOARD_SIZE; y++) {
                        Board.GetCell(x, y).SetValueForce(alphaSide, layer, -1);
                    }
                }
                break;
        }

    }

    private void Estimate() {
        if (IsEnemyLastAttack()) {
            if (EnemyLastAttackResult().contains(Board.RESULT_SINK)) {
                allySumHp--;
                allyCount--;
                if (allyCount == 0) {
                    Board.Interrupt();
                }
            }
            if (EnemyLastAttackResult().contains(Board.RESULT_HIT)) {
                allySumHp--;
            }
        }
        if (IsAllyLastAttack()) {
            if (AllyLastAttackResult().contains(Board.RESULT_SINK)) {
                enemySumHp--;
                enemyCount--;
                if (enemyCount == 0) {
                    Board.Interrupt();
                }
            }
            if (AllyLastAttackResult().contains(Board.RESULT_HIT)) {
                enemySumHp--;
            }
        }
        Board.SearchEnableAttackPoints(alphaSide);
    }

    public void Think() {
        Estimate();

        // 自軍が移動した = 移動先の可能性があるポイントの逆評価値に1を追加する
        if (IsAllyLastMove()) {
            // MoveValue(1, AllyLastMoveVector());
            if (allyCount > 1) {
                MoveValue(1, AllyLastMoveVector());
            } else {
                SwipeValue(1, AllyLastMoveVector());
            }
            allyMoveCount++;
            switch (allyMoveType) {
                case TYPE_ESCAPE:
                    allyEscapeMoveCount++;
                    break;
                case TYPE_ATTACK:
                    allyAttackMoveCount++;
                    break;
                case TYPE_FREE:
                    allyFreeMoveCount++;
                    break;
            }
        }

        // 自軍が攻撃した = 攻撃したポイントの逆評価値を-1に固定する, 周囲のポイントの逆評価値に1を追加する
        if (IsAllyLastAttack()) {
            Board.GetCell(AllyLastAttackPoint()).SetValueForce(alphaSide, 1, -1);
            for (Point point : Board.GetRoundPoints(AllyLastAttackPoint())) {
                Board.GetCell(point).AddValue(alphaSide, 1, 1);
            }
            // 敵軍が撃沈した = 命中したポイントの評価値, 逆評価値を-2に固定する
            if (AllyLastAttackResult().contains(Board.RESULT_SINK)) {
                Board.GetCell(AllyLastAttackPoint()).SetValueForce(alphaSide, 0, -2);
                Board.GetCell(AllyLastAttackPoint()).SetValueForce(alphaSide, 1, -2);

                for (Point point : Board.GetRoundPoints(AllyLastAttackPoint())) {
                    if (Board.GetCell(point).GetValue(alphaSide, 0) > 0) {
                        Board.GetCell(point).AddValue(alphaSide, 0, -1);
                    }
                }
            }
            // 敵軍が命中した = 命中したポイントの評価値を20に設定する, 命中したポイントのX軸Y軸対称のポイントの評価値に5を追加する,
            // = 命中したポイントのX軸, Y軸対称のポイントの評価値に3を追加する
            if (AllyLastAttackResult().contains(Board.RESULT_HIT)) {
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
            if (AllyLastAttackResult().contains(Board.RESULT_NEAR)) {
                Board.GetCell(AllyLastAttackPoint()).SetValueForce(alphaSide, 0, -1);
                for (Point point : Board.GetRoundPoints(AllyLastAttackPoint())) {
                    Board.GetCell(point).AddValue(alphaSide, 0, 1);
                }
            }
            // 敵軍が外れした = 攻撃したポイント, 周囲のポイントの評価値を-1に固定する
            if (AllyLastAttackResult().contains(Board.RESULT_NOHIT)) {
                Board.GetCell(AllyLastAttackPoint()).SetValueForce(alphaSide, 0, -1);
                for (Point point : Board.GetRoundPoints(AllyLastAttackPoint())) {
                    Board.GetCell(point).SetValueForce(alphaSide, 0, -1);
                }
            }
            if (AllyLastAttackResult().contains(Board.RESULT_SINK)
                    || AllyLastAttackResult().contains(Board.RESULT_HIT)) {
                switch (allyAttackType) {
                    case TYPE_FAKEMOVE:
                        enemyFakeMoveCount++;
                        break;
                    case TYPE_REALMOVE:
                        enemyRealMoveCount++;
                        break;
                    case TYPE_NOMOVE:
                        enemyNoMoveCount++;
                        break;
                }
            } else {
                switch (allyAttackType) {
                    case TYPE_FAKEMOVE:
                        enemyRealMoveCount++;
                        break;
                    case TYPE_REALMOVE:
                        enemyFakeMoveCount++;
                        break;
                }
            }
        }

        // 敵軍が移動した = 移動先の可能性があるポイントの評価値に1を追加する
        if (IsEnemyLastMove()) {
            enemyMoveCount++;
            // MoveValue(0, EnemyLastMoveVector());
            if (enemyCount > 1) {
                MoveValue(0, EnemyLastMoveVector());
            } else {
                SwipeValue(0, EnemyLastMoveVector());
            }
        }

        // 敵軍が攻撃した = 攻撃したポイントの評価値を-1に固定する, 周囲のポイントの評価値に1を追加する
        if (IsEnemyLastAttack()) {
            Board.GetCell(EnemyLastAttackPoint()).SetValueForce(alphaSide, 0, -1);
            for (Point point : Board.GetRoundPoints(EnemyLastAttackPoint())) {
                Board.GetCell(point).AddValue(alphaSide, 0, 1);
            }
            // 自軍が撃沈した = 命中したポイントの評価値, 逆評価値を-2に固定する
            if (EnemyLastAttackResult().contains(Board.RESULT_SINK)) {
                Board.GetCell(EnemyLastAttackPoint()).SetValueForce(alphaSide, 0, -2);
                Board.GetCell(EnemyLastAttackPoint()).SetValueForce(alphaSide, 1, -2);
            }
            // 自軍が命中した = 命中したポイントの逆評価値を10に設定する
            if (EnemyLastAttackResult().contains(Board.RESULT_HIT)) {
                if (allyMoveType == TYPE_ESCAPE) {
                    fakeMoveEnable = false;
                }
                Board.GetCell(EnemyLastAttackPoint()).SetValue(alphaSide, 1, 20);
            }
            // 自軍が波高しした = 攻撃したポイントの逆評価値を0に設定する, 周囲のポイントに1を追加する
            if (EnemyLastAttackResult().contains(Board.RESULT_NEAR)) {
                Board.GetCell(EnemyLastAttackPoint()).SetValue(alphaSide, 1, 0);
                for (Point point : Board.GetRoundPoints(EnemyLastAttackPoint())) {
                    Board.GetCell(point).AddValue(alphaSide, 1, 1);
                }
            }
            // 自軍が外れした = 攻撃したポイント, 周囲のポイントの逆評価値を-1に固定する
            if (EnemyLastAttackResult().contains(Board.RESULT_NOHIT)) {
                Board.GetCell(EnemyLastAttackPoint()).SetValueForce(alphaSide, 1, -1);
                for (Point point : Board.GetRoundPoints(EnemyLastAttackPoint())) {
                    Board.GetCell(point).SetValueForce(alphaSide, 1, -1);
                }
            }
        }

        if (estimatedAttackedStatus == 2) {
            estimatedAttackedStatus = 0;
        }
        if (estimatedAttackedStatus == 1) {
            estimatedAttackedStatus++;
        }

        allyAttackType = 0;
        allyMoveType = 0;

        Board.WriteLogLine("AllyMoveCount = " + allyMoveCount);
        Board.WriteLogLine("TYPE_ESCAPE = " + allyEscapeMoveCount + ", TYPE_ATTACK = " + allyAttackMoveCount
                + ", TYPE_FREE = " + allyFreeMoveCount);
        Board.WriteLogLine("EnemyMoveCount = " + enemyMoveCount);
        Board.WriteLogLine(
                "TYPE_FAKEMOVE = " + enemyFakeMoveCount + ", TYPE_REALMOVE = " + enemyRealMoveCount
                        + ", TYPE_NOMOVE = " + enemyNoMoveCount);

        // 自軍が攻撃した
        if (IsAllyLastAttack()) {
            // 敵軍が命中した
            // 敵軍が命中しなかった = (A) の攻撃結果の場合は移動する前のポイントが攻撃可能範囲内なら攻撃する
            if (AllyLastAttackResult().contains(Board.RESULT_HIT)) {
                // 敵軍が移動した = 命中したポイントに移動ベクトルを足したポイントが範囲内ならそのポイントに移動したと判断し、攻撃可能範囲内なら攻撃する (A)
                // 敵軍が移動しなかった = 命中したポイントにもう一度攻撃する
                if (IsEnemyLastMove()) {
                    Point estimatedPoint = AllyLastAttackPoint()
                            .Plus(EnemyLastMoveVector());
                    if ((enemyFakeMoveCount >= 0 && enemyRealMoveCount == 0) || !estimatedPoint.IsRange()
                            || (enemyFakeMoveCount > enemyRealMoveCount)) {
                        allyAttackType = TYPE_FAKEMOVE;
                        Board.WriteLogLine("TYPE_FAKEMOVE");
                        DoAttack(AllyLastAttackPoint());
                        return;
                    } else if ((enemyRealMoveCount >= 1 && enemyFakeMoveCount == 0)
                            || (enemyRealMoveCount > enemyFakeMoveCount)) {
                        Board.GetCell(AllyLastAttackPoint()).SetValueForce(alphaSide, 0, 0);
                        Board.GetCell(estimatedPoint).SetValueForce(alphaSide, 0, 20);
                        if (Board.IsEnableAttackPoint(alphaSide, estimatedPoint)) {
                            estimatedAttackedStatus = 1;
                            estimatedBeforePoint = AllyLastAttackPoint();
                            allyAttackType = TYPE_REALMOVE;
                            Board.WriteLogLine("TYPE_REALMOVE");
                            DoAttack(estimatedPoint);
                            return;
                        }
                    }
                } else {
                    if (Board.IsEnableAttackPoint(alphaSide, AllyLastAttackPoint())) {
                        allyAttackType = TYPE_NOMOVE;
                        Board.WriteLogLine("TYPE_NOMOVE");
                        DoAttack(AllyLastAttackPoint());
                        return;
                    }
                }
            } else {
                if (estimatedAttackedStatus == 1) {
                    estimatedAttackedStatus = 0;
                    if (Board.IsEnableAttackPoint(alphaSide, estimatedBeforePoint)) {
                        Board.GetCell(estimatedBeforePoint).SetValueForce(alphaSide, 0, 20);
                        allyAttackType = TYPE_FAKEMOVE;
                        Board.WriteLogLine("TYPE_FAKEMOVE");
                        DoAttack(estimatedBeforePoint);
                        estimatedBeforePoint = null;
                        return;
                    }
                }
                if (AllyLastAttackResult().contains(Board.RESULT_NEAR)) {

                }
            }
        }

        // 敵軍が攻撃した
        if (IsEnemyLastAttack()) {
            // 自軍が命中した
            if (EnemyLastAttackResult().contains(Board.RESULT_HIT)) {
                if (fakeMoveEnable) {
                    if (Board.GetCell(EnemyLastAttackPoint()).GetHp(alphaSide) == 2 || true) {
                        HashMap<Point, Integer> pointsValue = new HashMap<Point, Integer>();
                        for (Point point : Board.GetCrossPoints(EnemyLastAttackPoint(), 1, 1)) {
                            if (Board.IsMoveEnablePoint(alphaSide, EnemyLastAttackPoint(), point)) {
                                pointsValue.put(point, Board.GetPointDistance(point, new Point(2, 2)));
                            }
                        }
                        if (pointsValue.size() != 0) {
                            int maxValue = Collections.max(pointsValue.values());
                            for (Map.Entry<Point, Integer> pointValue : pointsValue.entrySet()) {
                                if (pointValue.getValue() == maxValue) {
                                    for (Point movePoint : Board.GetShipPoints(alphaSide)) {
                                        Point moveVector = pointValue.getKey()
                                                .Minus(EnemyLastAttackPoint());
                                        if (movePoint == EnemyLastAttackPoint()) {
                                            continue;
                                        }
                                        if (!movePoint.Plus(moveVector).IsRange()) {
                                            continue;
                                        }
                                        if (Board.IsMoveEnableVector(alphaSide, movePoint, moveVector)) {
                                            allyMoveType = TYPE_ESCAPE;
                                            Board.WriteLogLine("TYPE_ESCAPE");
                                            DoMove(movePoint, movePoint.Plus(moveVector));
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (prepareTurned && Board.IsEnableAttackPoint(alphaSide, preparePoint)) {
            prepareTurned = false;
            allyAttackType = TYPE_HIT;
            Board.WriteLogLine("TYPE_HIT");
            DoAttack(preparePoint);
            preparePoint = null;
            return;
        }

        ArrayList<Point> maxValuePoints = new ArrayList<Point>(
                Board.GetPointValues(alphaSide, null, 0, 1).keySet());
        if (Board.GetCell(maxValuePoints.get(0)).GetValue(alphaSide, 0) >= 5) {
            for (Point point : maxValuePoints) {
                if (Board.IsEnableAttackPoint(alphaSide, point)) {
                    // if (IsAllyLastAttack() && AllyLastAttackResult().contains(Board.RESULT_HIT))
                    // {
                    // if (IsEnemyLastMove()) {
                    // if (point.equals(AllyLastAttackPoint())) {
                    // allyAttackType = TYPE_FAKEMOVE;
                    // Board.WriteLogLine("TYPE_FAKEMOVE");
                    // } else if (point.Plus(EnemyLastMoveVector()).equals(AllyLastAttackPoint())) {
                    // allyAttackType = TYPE_REALMOVE;
                    // Board.WriteLogLine("TYPE_REALMOVE");
                    // } else {
                    // allyAttackType = TYPE_SEARCH;
                    // Board.WriteLogLine("TYPE_SEARCH");
                    // }
                    // } else {
                    // allyAttackType = TYPE_NOMOVE;
                    // Board.WriteLogLine("TYPE_NOMOVE");
                    // }
                    // } else {
                    // allyAttackType = TYPE_SEARCH;
                    // Board.WriteLogLine("TYPE_SEARCH");
                    // }
                    allyAttackType = TYPE_SEARCH;
                    Board.WriteLogLine("TYPE_SEARCH");
                    DoAttack(point);
                    return;
                }
            }
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
                                allyMoveType = TYPE_ATTACK;
                                Board.WriteLogLine("TYPE_ATTACK");
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
                                allyMoveType = TYPE_ATTACK;
                                Board.WriteLogLine("TYPE_ATTACK");
                                DoMove(movePoint, movePoint.Plus(moveVector));
                                return;
                            }
                        }
                    }
                }
            }
        }

        if (Board.GetMaxValuePoints(alphaSide, true, 0).size() != 0) {
            allyAttackType = TYPE_SEARCH;
            Board.WriteLogLine("TYPE_SEARCH");
            DoAttack(Board.GetRandomPoint(Board.GetMaxValuePoints(alphaSide, true, 0)));
            return;
        } else {
            for (Point movePoint : Board.GetShipPoints(alphaSide)) {
                for (Point moveVector : Board.GetCrossPoints(movePoint, 1, 2)) {
                    if (Board.IsMoveEnableVector(alphaSide, movePoint, moveVector)) {
                        allyMoveType = TYPE_FREE;
                        Board.WriteLogLine("TYPE_FREE");
                        DoMove(movePoint, movePoint.Plus(moveVector));
                        return;
                    }
                }
            }
            // Board.WriteDisableTurn();
        }
    }
}