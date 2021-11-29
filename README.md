
# BattleShip

認知科学2021 潜水艦撃沈ゲーム
RTX 3080 Ti

## Documentation

### ゲームロジック(BattleShip.java)

#### セル(Cell)

| 変数名           | 変数型     | デフォルト値 |                    |
| :-----------: | :-----: | :----: | :----------------: |
| alphaHp       | Integer | -1     | αの戦艦HP(存在しない場合は-1) |
| bravoHp       | Integer | -1     | βの戦艦HP(存在しない場合は-1) |
| alphaValue    | Integer | 0      | αのアルゴリズム用の評価値      |
| bravoValue    | Integer | 0      | βのアルゴリズム用の評価値      |
| alphaIsAttack | boolean | false  | αが攻撃可能可否         |
| bravoIsAttack | boolean | false  | βが攻撃可能可否         |

| メソッド名       | 引数型              | 戻り値型    |           |                            |     |
| ----------- | ---------------- | ------- | --------- | -------------------------- | --- |
| GetHp       | boolean          | Integer | 戦艦HPを取得   |                            |     |
| SetHp       | boolean, Integer | なし      | 戦艦HPを設定   |                            |     |
| GetValue    | boolean          | Integer | 評価値を取得    |                            |     |
| SetValue    | boolean, Integer | なし      | 評価値を設定          |                            |     |
| SetIsAttack | boolean, boolean | なし      | 攻撃可能可否を設定 | セルに味方戦艦がいない場合のみ、trueを設定できる |     |
| GetIsAttack | boolean, boolean | boolean | 攻撃可能可否を取得 |                            |     |
| GetIsAlive  | boolean          | boolean | 戦艦の生死を取得  |                            |     |
| GetIsEmpty  | boolean          | boolean | 戦艦の有無を取得  |                            |     |

#### ポイント(Point)

| 変数名 | 変数型     | デフォルト値 |       |
| --- | ------- | ------ | ----- |
| x   | Integer | なし     | 座標のX値 |
| y   | Intger  | なし     | 座標のY値 |




### DebugPlay.java

2つのアルゴリズムを勝負する
αとβの初期配置を設定する必要がある

### ReleasePlay.java

アルゴリズムを実際に使う
αの初期配置のみ設定する必要がある

---

[@xyzyxJP](https://twitter.com/xyzyxJP)
