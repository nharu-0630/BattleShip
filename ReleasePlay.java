import java.util.*;

public class ReleasePlay {
    public static Scanner scanner = new Scanner(System.in);

    public static final boolean isVisibleLog = true;
    public static final boolean isAttackResultArray = false;
    public static final boolean isEnemySecret = true;

    public static void main(String args[]) {
        Board.Initialize(isVisibleLog, isAttackResultArray);
        Algorithm001 alphaAlgorithm = new Algorithm001(true, isEnemySecret);

        System.out.print("f(First), s(Second): ");
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
                    while (true) {
                        String tempLine = scanner.nextLine();
                        if (tempLine.chars().allMatch(Character::isDigit)) {
                            int attackResult = Integer.parseInt(tempLine);
                            if (0 <= attackResult && attackResult <= 3) {
                                Board.AttackResultTransfer(alphaSide,
                                        new ArrayList<Integer>(Arrays.asList(attackResult)));
                                break;
                            }
                        }
                    }
                }
            } else {
                System.out.print("a(Attack), m(Move): ");
                switch (scanner.nextLine()) {
                    case "a":
                        System.out.print("x, y: ");
                        String[] tempArray = scanner.nextLine().split(",");
                        if (tempArray.length == 2) {
                            if (tempArray[0].chars().allMatch(Character::isDigit) && tempArray[1].chars()
                                    .allMatch(Character::isDigit)) {
                                int x = Integer.parseInt(tempArray[0]);
                                int y = Integer.parseInt(tempArray[1]);
                                if (0 <= x && x <= 4 && 0 <= y && y <= 4) {
                                    Point point = new Point(x, y);
                                    Board.AttackPointForce(alphaSide, point);
                                    break;
                                }
                            }
                        }
                        break;
                    case "m":
                        System.out.print("x, y: ");
                        tempArray = scanner.nextLine().split(",");
                        if (tempArray.length == 2) {
                            if (tempArray[0].chars().allMatch(Character::isDigit) && tempArray[1].chars()
                                    .allMatch(Character::isDigit)) {
                                int x = Integer.parseInt(tempArray[0]);
                                int y = Integer.parseInt(tempArray[1]);
                                if (0 <= x && x <= 4 && 0 <= y && y <= 4) {
                                    Point vectorPoint = new Point(x, y);
                                    Board.MoveVectorForce(alphaSide, vectorPoint);
                                    break;
                                }
                            }
                        }
                        break;
                }
            }
            alphaSide = !alphaSide;
        }
    }
}
