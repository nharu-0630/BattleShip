![](https://user-images.githubusercontent.com/8305330/147572062-e5d37005-2209-48c7-a17d-5bd4dcacb845.png)

# BattleShip

認知科学2021 潜水艦撃沈ゲーム  
RTX 3080 Ti  
https://xyzyxjp.github.io/BattleShip/

![](https://cdn.discordapp.com/attachments/919744937753460756/930838044922224720/unknown.png)

# Documentation

## ゲームロジック(BattleShip.java)

### セル(Cell)

Javadoc形式に移行

### ポイント(Point)

Javadoc形式に移行

### ボード(Board)

Javadoc形式に移行

### インターフェイス(Interface)

Javadoc形式に移行

### ロガー(Logger)

Javadoc形式に移行

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
            if (Board.GetLastAttackResult(!alphaSide).contains(Board.RESULT_SINK)) {
                allySumHp--;
                allyCount--;
                if (allyCount == 0) {
                    Board.Interrupt();
                }
            }
            if (Board.GetLastAttackResult(!alphaSide).contains(Board.RESULT_HIT)) {
                allySumHp--;
            }
        }
        if (Board.IsLastAttack(alphaSide)) {
            if (Board.GetLastAttackResult(alphaSide).contains(Board.RESULT_SINK)) {
                enemySumHp--;
                enemyCount--;
                if (enemyCount == 0) {
                    Board.Interrupt();
                }
            }
            if (Board.GetLastAttackResult(alphaSide).contains(Board.RESULT_HIT)) {
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

デバッグ用

## ReleasePlay.java

リリース用

## UndoPlay.java

復元用

---

[@xyzyxJP](https://twitter.com/xyzyxJP)
