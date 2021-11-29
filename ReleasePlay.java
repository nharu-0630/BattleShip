public class ReleasePlay {
    public static void main(String args[]) {
        Board.Initialize(true);
        Algorithm001 alphaAlgorithm = new Algorithm001(true);

        System.out.print("f(First) s(Second): ");
        boolean alphaSide = (Board.scanner.nextLine() == "f");

        while (true) {
            if (alphaSide) {
                alphaAlgorithm.Think();
            } else {
                System.out.print("a(Attack), m(Move): ");
                switch (Board.scanner.nextLine()) {
                    case "a":
                        System.out.print("x,y: ");
                        String[] tempArray = Board.scanner.nextLine().split(",");
                        Point point = new Point(Integer.parseInt(tempArray[0]), Integer.parseInt(tempArray[1]));
                        Board.AttackPointForce(alphaSide, point);
                        break;
                    case "m":
                        System.out.print("x,y: ");
                        tempArray = Board.scanner.nextLine().split(",");
                        Point vectorPoint = new Point(Integer.parseInt(tempArray[0]), Integer.parseInt(tempArray[1]));
                        Board.MovePointForce(alphaSide, vectorPoint);
                        break;
                }
            }
            alphaSide = !alphaSide;
        }
    }
}
