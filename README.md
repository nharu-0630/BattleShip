![](https://user-images.githubusercontent.com/8305330/147572062-e5d37005-2209-48c7-a17d-5bd4dcacb845.png)

# BattleShip

認知科学2021 潜水艦撃沈ゲーム  
RTX 3080 Ti  
https://xyzyxjp.github.io/BattleShip/

![](https://cdn.discordapp.com/attachments/919744937753460756/927855981432565780/unknown.png)

# Documentation

## ゲームロジック(BattleShip.java)

| boolean | alphaSide |
| ------- | --------- |
| α       | true      |
| β       | false     |

### セル(Cell)

| 変数名           | 変数型                  | デフォルト値 |                    |
| :-----------: | :------------------: | :----: | :----------------: |
| alphaHp       | int                  | -1     | αの戦艦HP(存在しない場合は-1) |
| bravoHp       | int                  | -1     | βの戦艦HP(存在しない場合は-1) |
| alphaValues   | ArrayList\<Integer\> | 0      | αのアルゴリズム用の評価値配列      |
| bravoValues   | ArrayList\<Integer\> | 0      | βのアルゴリズム用の評価値配列      |
| alphaEnableAttack | boolean              | false  | αの攻撃の可否            |
| bravoEnableAttack | boolean              | false  | βの攻撃の可否            |

| メソッド名       | 引数型               | 戻り値型                              |           |                          |
| ----------- | ----------------- | --------------------------------- | --------- | ------------------------ |
| GetHp       | boolean           | int                               | 戦艦HPを取得   |                          |
| SetHp       | boolean, int      |                                   | 戦艦HPを設定   |                          |
| GetValue    | boolean, int      | ArrayList\<Integer\>              | 評価値を取得    | 第2引数`layer`              |
| GetValues   | boolean           | ArrayList\<ArrayList\<Integer\>\> | 評価値リストを取得 |                          |
| SetValue    | boolean, int, int |                                   | 評価値を設定    | 第2引数`layer`, 第3引数`value` |
| AddValue    | boolean, int, int |                                   | 評価値を加算    | 第2引数`layer`, 第3引数`value` |
| SetValueForce    | boolean, int, int |                                   | 評価値を強制設定    | 第2引数`layer`, 第3引数`value` |
| SetEnableAttack | boolean, boolean  |                                   | 攻撃の可否を設定  |                          |
| GetEnableAttack | boolean, boolean  | boolean                           | 攻撃の可否を取得  |                          |
| IsAlive     | boolean           | boolean                           | 戦艦の生死を取得  |                          |
| IsEmpty     | boolean           | boolean                           | 戦艦の有無を取得  |                          |

第1引数には`alphaSide`を割当  

### ポイント(Point)

| 変数名 | 変数型 | デフォルト値 |       |
| --- | --- | ------ | ----- |
| x   | int | 0      | 座標のX値 |
| y   | int | 0      | 座標のY値 |

| メソッド名               | 引数型     | 戻り値型   |             |
| ------------------- | ------- | ------ | ----------- |
| Plus                | Point   | Point  | 引数のポイントを加算  |
| Minus               | Point   | Point  | 引数のポイントを減算  |
| Multiply            | int     | Point  | 引数を乗算       |
| Divide              | int     | Point  | 引数を除算       |
| equals              | boolean | Point  | 引数のポイントと比較  |
| IsRange             | boolean |        | ボード内の座標有無   |
| toString            |         | String | (x, y)      |
| toPointFormatString |         | String | `アルファベット数字` |
| toVectorFormaString |         | String | `方角数字`      |

### ボード(Board)

| int | attackResult |
| ------- | ------------ |
| 撃沈！     | 3            |
| 命中！     | 2            |
| 波高し！    | 1            |
| ハズレ！    | 0            |

| 変数名                   | 変数型                  | デフォルト値 |                         |
| --------------------- | -------------------- | ------ | ----------------------- |
| boardSize             | int                  | 5      | ボードの1辺の長さ               |
| cells                 | Cell[][]             |        | セルの2次元配列                |
| turnCount             | int                  | 0      | ターン数                    |
| alphaWin              | boolean              | false  | αの勝利                    |
| bravoWin              | boolean              | false  | βの勝利                    |
| lastAlphaAttackPoint  | Point                |        | αの前回攻撃ポイント              |
| lastAlphaAttackResult | ArrayList\<Integer\> | -1     | αの前回攻撃結果                |
| lastAlphaMoveVector   | Point                |        | αの前回移動ベクトル              |
| lastBravoAttackPoint  | Point                |        | βの前回攻撃ポイント              |
| lastBravoAttackResult | ArrayList\<Integer\> | -1     | βの前回攻撃結果                |
| lastBravoMoveVector   | Point                |        | βの前回移動ベクトル              |
| isVisibleLog          | boolean              | false  | ログの表示                   |
| isAttackResultArray   | boolean              | false  | 攻撃結果を複数返す場合は`true`      |
| isEnemySecret         | boolean              | false  | 攻撃結果をあとから設定する場合のみ`true` |

| メソッド名                        | 引数型                                    | 戻り値型                      |                         |                                                |
| ---------------------------- | -------------------------------------- | ------------------------- | ----------------------- | ---------------------------------------------- |
| Initialize                   | boolean, boolean                       |                           | 初期化                     | 第2引数は`isVisibleLog`, 第3引数`isAttackResultArray` |
| WriteBoard                 | boolean*                               |                           | 戦況を表示                 |                                                |
| WriteLogSide                 | boolean                                |                           | ログの表示                   |                                                |
| WriteLogLine                 | String                                 |                           | ログの表示                   |                                                |
| WriteLog                     | String                                 |                           | ログの表示                   |                                                |
| IsContinue                   | boolean                                | boolean                   | ターン続行の可否                | 強制終了する場合のみ`true`                               |
| Interrupt                    |                                        |                           | ターンの強制終了                |                                                |
| GetAlphaWin                  |                                        | boolean                   | αの勝利を取得                 |                                                |
| GetBravoWin                  |                                        | boolean                   | βの勝利を取得                 |                                                |
| AdTurnCount                 |                                        |                           | ターン数に1加算                |                                                |
| GetTurnCount*                |                                        | int                       | ターン数を取得                 |                                                |
| GetBoardSize*                |                                        | int                       | ボードの1辺の長さを取得            |                                                |
| GetCell*                     | Point                                  | Cell                      | ポイントのセルを取得              |                                                |
| GetCell*                     | int, int                               | Cell                      | ポイントのセルを取得              |                                                |
| SetCell                      | Point, Cell                            |                           | ポイントにセルを設定              |                                                |
| SetCell                      | int, int, Cell                         |                           | ポイントにセルを設定              |                                                |
| IsLastAttack*                | boolean*                               | boolean                   | 前回移動の有無を取得              |                                                |
| IsLastMove*                  | boolean*                               | boolean                   | 前回攻撃の有無を取得              |                                                |
| GetLastAttackPoint*          | boolean*                               | Point                     | 前回攻撃ポイントを取得             |                                                |
| GetLastAttackResult*         | boolean*                               | ArrayList\<Integer\>      | 前回攻撃結果を取得               |                                                |
| GetLastMoveVector*           | boolean*                               | Point                     | 前回移動ベクトルを取得             |                                                |
| InitializeValues*            | boolean*, int                          |                           | 全ての評価値を0に設定             | 第2引数`layer`                                    |
| NormalizeValues*             | boolean*, int                          |                           | 全ての評価値を周囲4ポイントリストの個数に設定 | 第2引数`layer`                                    |
| GetShipPoints*               | boolean*                               | ArrayList\<Point\>        | 戦艦のポイントリストを取得           |                                                |
| GetRandomPoint*              | Point                                  | ArrayList\<Point\>        | ポイントリストからランダムに取得        |                                                |
| GetRandomPoints*             | int                                    | ArrayList\<Point\>        | ランダムなポイントリストを取得         |                                                |
| SetRandom4Points             | boolean*                               |                           | ランダムな4ポイントに戦艦を配置        |                                                |
| GetMaxValuePoints*           | boolean*, int                          | ArrayList\<Point\>        | 評価値が最大であるポイントリストを取得     | 第2引数`layer`                                    |
| GetMinValuePoints*           | boolean*, int                          | ArrayList\<Point\>        | 評価値が最小であるポイントリストを取得     | 第2引数`layer`                                    |
| GetShortPoints*              | boolean*, Point                        | ArrayList\<Point\>        | ポイントに最も近い戦艦のポイントリストを取得  |                                                |
| GetRoundPoints*              | Point                                  | ArrayList\<Point\>        | ポイントの周囲8ポイントリストを取得      |                                                |
| GetCrossPoints*              | Point, int, int                        | ArrayList\<Point\>        | ポイントから十字方向のポイントリストを取得   | 第1引数`最小距離`, 第2引数`最大距離`                         |
| GetPointDistance*            | Point, Point                           | int                       | ポイント間の距離                | `X軸間の距離` + `Y軸間の距離`                            |
| GetPointValues*              | boolean*, ArrayList\<Point\>, int, int | HashMap\<Point, Integer\> | ポイントリストから評価値リストを取得      | 第3引数`layer`, 第4引数`filter`                      |
| IsMoveEnablePoint*           | boolean*, Point, Point                 | boolean                   | 移動の可否                   | 第2, 3引数`ポイント`                                  |
| IsMoveEnableVector*          | boolean*, Point, Point                 | boolean                   | 移動の可否                   | 第2引数`ポイント`, 第3引数`ベクトル`                         |
| GetFilterMoveEnablePoints*   | boolean*, Point, ArrayList\<Point\>    | ArrayList\<Point\>        | ポイントリストから移動可能なポイントのみ取得  | 第3引数は`ポイント`                                    |
| GetFilterMoveEnableVectors*  | boolean*, Point, ArrayList\<Point\>    | ArrayList\<Point\>        | ポイントリストから移動可能なポイントのみ取得  | 第3引数は`ベクトル`                                    |
| MovePoint                    | boolean*, Point, Point                 | boolean                   | 戦艦の移動                   | 第2, 3引数`ポイント`                                  |
| MoveVector                   | boolean*, Point, Point                 | boolean                   | 戦艦の移動                   | 第2引数`ポイント`, 第3引数`ベクトル`                         |
| MoveVectorForce              | boolean*, Point                        |                           | 戦艦の強制移動                 | 第2引数`ベクトル`                                     |
| SearchEnableAttackPoints*          | boolean*                               |                           | 攻撃の可否を検索                |                                                |
| GetEnableAttackPoints*             | boolean*                               | ArrayList\<Point\>        | 攻撃が可能なポイントリストを取得        |                                                |
| IsEnableAttackPoint*         | boolean*, Point                        | boolean                   | ポイントの攻撃の可否を取得           |                                                |
| GetFilterEnableAttackPoints* | boolean*, ArrayList\<Point\>           | ArrayList\<Point\>        | ポイントリストから攻撃可能なポイントのみ取得  |                                                |
| AttackPoint                  | boolean*, Point, boolean               | boolean                   | ポイントへの攻撃                | 第3引数は攻撃結果をあとから設定する場合のみ`false`                  |
| AttackResultTransfer         | boolean*, ArrayList\<Integer\>         |                           | ポイントへの攻撃結果を設定           | `AttackPoint`の第3引数を`false`にした場合のみ              |
| AttackPointForce             | boolean*, Point                        |                           | ポイントへの強制攻撃              |                                                |

引数型に`*`がついている第1引数は`alphaSide`を割当  
メソッド名に`*`がついているメソッドは`Algorithm`内で使用可能  

### インターフェイス(Interface)

| 変数名           | 変数型     | デフォルト値 |                            |
| ------------- | ------- | ------ | -------------------------- |
| alphaSide     | boolean | false  | `Board`における`alphaSide`     |
| isEnemySecret | boolean | false  | 攻撃結果をあとから設定する場合のみ`true` |
| allyCount     | int     | 4      | 自軍戦艦の数                     |
| allySumHp     | int     | 12     | 自軍戦艦の総HP                   |
| enemyCount    | int     | 4      | 敵軍戦艦の数                     |
| enemySumHp    | int     | 12     | 敵軍戦艦の総HP                   |

| メソッド名         | 引数型          | 戻り値型 |            |                 |
| ------------- | ------------ | ---- | ---------- | --------------- |
| DoMove*       | Point, Point |      | 戦艦の移動      | 第1, 2引数`ポイント` |
| DoMoveForce   | Point        |      | 戦艦の強制移動    | 第1引数`ベクトル`    |
| DoAttack*     | Point        |      | ポイントへの攻撃   |                 |
| DoAttackForce | Point        |      | ポイントへの強制攻撃 |                 |

メソッド名に`*`がついているメソッドのみ`Algorithm`内で一度限り使用可能  

### ロガー(Logger)

| 変数名        | 変数型        | デフォルト値 |     |
| ---------- | ---------- | ------ | --- |
| jsonObject | JSONObject |        |     |

| メソッド名                | 引数型     | 戻り値型                              |             |
| -------------------- | ------- | --------------------------------- | ----------- |
| AddLogger            | boolean | String, boolean                   | 現在の戦況をログに追加 |
| GetHpArrayList       | boolean | ArrayList\<Integer\>              |             |
| GetValuesArrayList   | boolean | ArrayList\<ArrayList\<Integer\>\> |             |
| GetIsAttackArrayList | boolean | ArrayList\<Boolean\>              |             |
| SaveLogger           |         |                                   | ログを保存       |

## AlgorithmXXX.java

### アルゴリズム(Algorithm)の開発

`Algorithm001.java`を参照

```java
import java.util.*;

class AlgorithmXXX extends Interface {
    AlgorithmXXX(boolean alphaSide, boolean isEnemySecret) {
        super(alphaSide, isEnemySecret);
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
        Board.SearchAttackPoints(alphaSide);
        
        /* 
        アルゴリズム
        
        移動する場合
            DoMove(oldPoint, newPoint);
            return;

        攻撃する場合
            DoAttack(point);
            return;

        移動・攻撃したあとにはreturn;を書いてThink()を終了する
        */
        return;
    }
}
```

## DebugPlay.java

2つのアルゴリズムを勝負する  
αとβの初期配置を設定する必要がある  

## ReleasePlay.java

アルゴリズムを実際に使う  
αの初期配置のみ設定する必要がある  

---

[@xyzyxJP](https://twitter.com/xyzyxJP)
