public class ReleasePlay {
    public static void main(String args[]) {
        Board.Initialize(true);
        Algorithm001 alphaAlgorithm = new Algorithm001(true);

        System.out.print("f(First) s(Second): ");
        boolean alphaSide = (Board.scanner.nextLine() == "f");

        while (true) {
            Board.IsContinue(false);
            if (alphaSide) {
                alphaAlgorithm.Think();
            } else {
                System.out.print("a(Attack), m(Move): ");
                switch (Board.scanner.nextLine()) {
                    case "a":
                        System.out.print("x,y: ");
                        String[] tempArray = Board.scanner.nextLine().split(",");
                        Point point = new Point(Integer.parseInt(tempArray[0]), Integer.parseInt(tempArray[1]));
                        Board.AttackPoint(alphaSide, point, true);
                        break;
                    case "m":
                        System.out.print("x,y: ");
                        tempArray = Board.scanner.nextLine().split(",");
                        Point oldPoint = new Point(Integer.parseInt(tempArray[0]), Integer.parseInt(tempArray[1]));
                        System.out.print("x,y: ");
                        tempArray = Board.scanner.nextLine().split(",");
                        Point newPoint = new Point(Integer.parseInt(tempArray[0]), Integer.parseInt(tempArray[1]));
                        Board.MovePoint(alphaSide, oldPoint, newPoint, true);
                        break;
                }
            }
            alphaSide = !alphaSide;
        }
    }
}
