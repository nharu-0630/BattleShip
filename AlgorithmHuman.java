class AlgorithmHuman extends Interface {
    AlgorithmHuman(boolean alphaSide) {
        super(alphaSide);
        Board.GetCell(0, 0).SetHp(alphaSide, 3);
        Board.GetCell(0, 4).SetHp(alphaSide, 3);
        Board.GetCell(4, 0).SetHp(alphaSide, 3);
        Board.GetCell(4, 4).SetHp(alphaSide, 3);
    }

    public void Think() {
        Board.AttackEnableSearch(alphaSide);
        Board.WriteBoardHp(alphaSide);
        Board.WriteBoardIsAttack(alphaSide);
        System.out.print("a(Attack), m(Move): ");
        switch (Board.scanner.nextLine()) {
            case "a":
                System.out.print("x,y: ");
                String[] tempArray = Board.scanner.nextLine().split(",");
                Point point = new Point(Integer.parseInt(tempArray[0]), Integer.parseInt(tempArray[1]));
                DoAttack(point);
                break;
            case "m":
                System.out.print("x,y: ");
                tempArray = Board.scanner.nextLine().split(",");
                Point oldPoint = new Point(Integer.parseInt(tempArray[0]), Integer.parseInt(tempArray[1]));
                System.out.print("x,y: ");
                tempArray = Board.scanner.nextLine().split(",");
                Point newPoint = new Point(Integer.parseInt(tempArray[0]), Integer.parseInt(tempArray[1]));
                DoMove(oldPoint, newPoint);
                break;
        }
    }
}
