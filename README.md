
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
| alphaIsAttack | boolean | false  | αが攻撃可能であるか         |
| bravoIsAttack | boolean | false  | βが攻撃可能であるか         |

| メソッド名 | 引数型 | 戻り値型 |     |
| ----- | --- | ---- | --- |
|       |     |      |     |

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
