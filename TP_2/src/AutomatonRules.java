public class AutomatonRules {
    public enum Rules2D {
        GOL("gol2D", gol),
        GOLV2("gol2Dv2", golvn),
        FILL("fill2D", fill),
        ODD("odd2D", odd2D),
//        EVEN("even2D", even2D),
        ;

        public final String name;
        public final CelularAutomata2D.Rule2D rule;

        Rules2D(String name, CelularAutomata2D.Rule2D rule) {
            this.name = name;
            this.rule = rule;
        }
    }

    public enum Rules3D {
        GOL("gol3D", gol3D),
        DECAY("decay3D", decay3D),
        DECAYV2("decay3Dv2", decay3Dv2),
//        EXPANSION3D("expansion3D", expansion3d),
//        FILL3D("fill3D", fill3D)
        ;

        public final String name;
        public final CelularAutomata3D.Rule3D rule;

        Rules3D(String name, CelularAutomata3D.Rule3D rule) {
            this.name = name;
            this.rule = rule;
        }
    }

    public static final CelularAutomata2D.Rule2D gol = (automata, i, j) -> {
        int sum = automata.sumNeighbors(i, j, 1, true);
        if (automata.getGridCell(i,j)) {
            return sum == 2 || sum == 3;
        } else {
            return sum == 3;
        }
    };

    public static final CelularAutomata2D.Rule2D golvn = (automata, i, j) -> {
        int sum = automata.sumNeighbors(i, j, 1, false);
        if (automata.getGridCell(i,j)) {
            return sum == 2 || sum == 3;
        } else {
            return sum == 3;
        }
    };

    public static final CelularAutomata2D.Rule2D seeds = (automata, i, j) -> automata.sumNeighbors(i, j, 1, true) == 2;

    public static final CelularAutomata2D.Rule2D line = (automata, x, y) -> {
        for(int i = -1; i <= 1 ; i++) {
            if (automata.inBounds(x - 1, y + i) && automata.getGridCell(x - 1, y + i)) {
                return true;
            }
        }
        return false;
    };


    public static final CelularAutomata2D.Rule2D ring = (automata, i, j) ->
            automata.sumNeighbors(i, j, 1, true) == 0 && automata.sumNeighbors(i, j, 2, false) > 0;

    public static final CelularAutomata2D.Rule2D even2D = (automata, i, j) -> {
        int sum = automata.sumNeighbors(i, j, 1, true);
        return sum % 2 == 0 && sum > 0;
    };

    public static final CelularAutomata2D.Rule2D odd2D = (automata, i, j) -> {
        int sum = automata.sumNeighbors(i, j, 1, true);
        return sum % 2 == 1;
    };

    public static final CelularAutomata2D.Rule2D fill = (automata, x, y) -> {
        if (!automata.getGridCell(x, y)) {
            for (int i=-1; i<=1; i++) {
                for (int j=-1; j<=1; j++) {
                    if (i!=0 || j!=0) {
                        for (int k = -1; k <= 1; k++) {
                            for (int l = -1; l <= 1; l++) {
                                if ((k!=0 || l!=0) && i != k && j != l && automata.inBounds(x + i, y + j) && automata.inBounds(x + k, y + l) &&
                                        automata.getGridCell(x + i, y + j) && automata.getGridCell(x + k, y + l)){
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            return false;
        } else return true;
    };

    public static final CelularAutomata2D.Rule2D expansion2D = (automata, i, j) -> automata.getGridCell(i,j) || automata.sumNeighbors(i,j, 1, true) != 0;


    //3D=================================================================================================================================================================

    public static final CelularAutomata3D.Rule3D gol3D = (automata, i, j, k) -> {
        int sum = automata.sumNeighbors(i, j, k, 1, true);
        if (automata.getGridCell(i,j,k)) {
            return sum == 2 || sum == 3;
        } else {
            return sum == 3;
        }
    };

    public static final CelularAutomata3D.Rule3D decay3D = (automata, i, j, k) -> {
        int sum = automata.sumNeighbors(i, j, k, 1, true);
        return sum >= 7 && !automata.getGridCell(i, j, k);
    };

    public static final CelularAutomata3D.Rule3D decay3Dv2 = (automata, i, j, k) -> {
        int sum = automata.sumNeighbors(i, j, k, 1, false);
        return sum >= 2 && !automata.getGridCell(i, j, k);
    };


    public static final CelularAutomata3D.Rule3D ring3D = (automata, i, j, k) ->
            automata.sumNeighbors(i, j, k, 1, true) == 0 && automata.sumNeighbors(i, j, k, 2, true) > 0;

    public static final CelularAutomata3D.Rule3D expansion3d = (automata, i, j, k) -> automata.getGridCell(i,j, k) || automata.sumNeighbors(i,j, k, 1, true) != 0;

    public static final CelularAutomata3D.Rule3D fill3D = (automata, x, y, z) -> {
        if (!automata.getGridCell(x, y, z)) {
            for (int i=-1; i<=1; i++) {
                for (int j=-1; j<=1; j++) {
                    for (int k=-1; k<=1; k++) {
                        if (i!=0 || j!=0 || k!=0) {
                            for (int l = -1; l <= 1; l++) {
                                if (l!=i){
                                    for (int m = -1; m <= 1; m++) {
                                        if (m!=j){
                                            for (int n = -1; n <= 1; n++) {
                                                if (n!=k && (l!=0 || m!=0 || n!=0) && automata.inBounds(x + i, y + j, z+k) && automata.inBounds(x + l, y + m, z + n) &&
                                                    automata.getGridCell(x + i, y + j, z + k) && automata.getGridCell(x+l, y+m, z+n)){
                                                    return true;
                                                }
                                            }
                                         }
                                     }
                                }
                            }
                        }
                    }
                }
            }
            return false;
        } else return true;
    };
}