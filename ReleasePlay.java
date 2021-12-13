import java.util.*;

public class ReleasePlay {
    public static Scanner scanner = new Scanner(System.in);

    public static final boolean isVisibleLog = true;
    public static final boolean isAttackResultArray = false;
    public static final boolean isEnemySecret = true;

    public static void main(String args[]) {
        Board.Initialize(isVisibleLog, isAttackResultArray);
        Algorithm001 alphaAlgorithm = new Algorithm001(true, isEnemySecret);

        System.out.print("f(First) s(Second): ");
        boolean alphaSide = true;
        switch (scanner.nextLine()) {
            case "f":
                alphaSide = true;
                break;
            case "s":
                alphaSide = false;
                break;
        }
        while (true) {
            if (alphaSide) {
                Board.WriteBoardHp(alphaSide);
                Board.WriteBoardIsAttack(alphaSide);
                alphaAlgorithm.Think();
                if (Board.IsLastAttack(alphaSide)) {
                    System.out.print("0(No Hit), 1(Near), 2(Hit), 3(Sink): ");
                    int attackResult = Integer.parseInt(scanner.nextLine());
                    Board.AttackResultTransfer(alphaSide, new ArrayList<Integer>(Arrays.asList(attackResult)));
                }
            } else {
                System.out.print("a(Attack), m(Move): ");
                switch (scanner.nextLine()) {
                    case "a":
                        System.out.print("x,y: ");
                        String[] tempArray = scanner.nextLine().split(",");
                        Point point = new Point(Integer.parseInt(tempArray[0]), Integer.parseInt(tempArray[1]));
                        Board.AttackPointForce(alphaSide, point);
                        break;
                    case "m":
                        System.out.print("x,y: ");
                        tempArray = scanner.nextLine().split(",");
                        Point vectorPoint = new Point(Integer.parseInt(tempArray[0]), Integer.parseInt(tempArray[1]));
                        Board.MoveVectorForce(alphaSide, vectorPoint);
                        break;
                }
            }
            alphaSide = !alphaSide;
        }
    }
}
