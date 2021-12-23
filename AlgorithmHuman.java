import java.util.*;

class AlgorithmHuman extends Interface {
    public static Scanner scanner = new Scanner(System.in);

    AlgorithmHuman(boolean alphaSide, boolean isEnemySecret) {
        super(alphaSide, isEnemySecret);
    }

    public void Think() {
        Board.SearchEnableAttackPoints(alphaSide);
        Board.WriteBoard(alphaSide);
        System.out.print("a(Attack), m(Move): ");
        switch (scanner.nextLine()) {
            case "a":
                System.out.print("x,y: ");
                String[] tempArray = scanner.nextLine().split(",");
                Point point = new Point(Integer.parseInt(tempArray[0]), Integer.parseInt(tempArray[1]));
                DoAttack(point);
                break;
            case "m":
                System.out.print("x,y: ");
                tempArray = scanner.nextLine().split(",");
                Point oldPoint = new Point(Integer.parseInt(tempArray[0]), Integer.parseInt(tempArray[1]));
                System.out.print("x,y: ");
                tempArray = scanner.nextLine().split(",");
                Point newPoint = new Point(Integer.parseInt(tempArray[0]), Integer.parseInt(tempArray[1]));
                DoMove(oldPoint, newPoint);
                break;
        }
    }
}
