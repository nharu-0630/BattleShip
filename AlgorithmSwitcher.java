class AlgorithmSwitcher extends Interface {
    AlgorithmSwitcher(boolean alphaSide, boolean isEnemySecret) {
        super(alphaSide, isEnemySecret);
    }

    private int algorithmNumber = 1;
    private Algorithm001 algorithm001;
    private Algorithm002 algorithm002;
    private Algorithm003 algorithm003;
    private Algorithm004 algorithm004;
    private Algorithm005 algorithm005;
    private Algorithm006 algorithm006;
    private Algorithm007 algorithm007;
    private Algorithm008 algorithm008;
    private Algorithm009 algorithm009;
    private Algorithm010 algorithm010;
    private Algorithm011 algorithm011;
    private Algorithm012 algorithm012;
    private Algorithm013 algorithm013;
    private Algorithm014 algorithm014;
    private Algorithm015 algorithm015;
    private Algorithm016 algorithm016;

    public void SetAlgorithm(int algorithmNumber) {
        this.algorithmNumber = algorithmNumber;
    }

    public void SetParameter(int[] parameters) {
        switch (algorithmNumber) {
            case 1:
                algorithm001 = new Algorithm001(alphaSide, isEnemySecret);
                algorithm001.SetParameter(parameters);
                break;
            case 2:
                algorithm002 = new Algorithm002(alphaSide, isEnemySecret);
                algorithm002.SetParameter(parameters);
                break;
            case 3:
                algorithm003 = new Algorithm003(alphaSide, isEnemySecret);
                algorithm003.SetParameter(parameters);
                break;
            case 4:
                algorithm004 = new Algorithm004(alphaSide, isEnemySecret);
                algorithm004.SetParameter(parameters);
                break;
            case 5:
                algorithm005 = new Algorithm005(alphaSide, isEnemySecret);
                algorithm005.SetParameter(parameters);
                break;
            case 6:
                algorithm006 = new Algorithm006(alphaSide, isEnemySecret);
                algorithm006.SetParameter(parameters);
                break;
            case 7:
                algorithm007 = new Algorithm007(alphaSide, isEnemySecret);
                algorithm007.SetParameter(parameters);
                break;
            case 8:
                algorithm008 = new Algorithm008(alphaSide, isEnemySecret);
                algorithm008.SetParameter(parameters);
                break;
            case 9:
                algorithm009 = new Algorithm009(alphaSide, isEnemySecret);
                algorithm009.SetParameter(parameters);
                break;
            case 10:
                algorithm010 = new Algorithm010(alphaSide, isEnemySecret);
                algorithm010.SetParameter(parameters);
                break;
            case 11:
                algorithm011 = new Algorithm011(alphaSide, isEnemySecret);
                algorithm011.SetParameter(parameters);
                break;
            case 12:
                algorithm012 = new Algorithm012(alphaSide, isEnemySecret);
                algorithm012.SetParameter(parameters);
                break;
            case 13:
                algorithm013 = new Algorithm013(alphaSide, isEnemySecret);
                algorithm013.SetParameter(parameters);
                break;
            case 14:
                algorithm014 = new Algorithm014(alphaSide, isEnemySecret);
                algorithm014.SetParameter(parameters);
                break;
            case 15:
                algorithm015 = new Algorithm015(alphaSide, isEnemySecret);
                algorithm015.SetParameter(parameters);
                break;
            case 16:
                algorithm016 = new Algorithm016(alphaSide, isEnemySecret);
                algorithm016.SetParameter(parameters);
                break;
        }
    }

    public void Think() {
        switch (algorithmNumber) {
            case 1:
                algorithm001.Think();
                break;
            case 2:
                algorithm002.Think();
                break;
            case 3:
                algorithm003.Think();
                break;
            case 4:
                algorithm004.Think();
                break;
            case 5:
                algorithm005.Think();
                break;
            case 6:
                algorithm006.Think();
                break;
            case 7:
                algorithm007.Think();
                break;
            case 8:
                algorithm008.Think();
                break;
            case 9:
                algorithm009.Think();
                break;
            case 10:
                algorithm010.Think();
                break;
            case 11:
                algorithm011.Think();
                break;
            case 12:
                algorithm012.Think();
                break;
            case 13:
                algorithm013.Think();
                break;
            case 14:
                algorithm014.Think();
                break;
            case 15:
                algorithm015.Think();
                break;
            case 16:
                algorithm016.Think();
                break;
        }
    }
}
