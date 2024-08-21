public class AutomatonRules {
    public static final CelularAutomata2D.Rule2D GOL = (automata, i, j) -> {
        int sum = automata.sumNeighbors(i, j, 1, true);
        if (automata.getGridCell(i,j)) {
            return sum == 2 || sum == 3;
        } else {
            return sum == 3;
        }
    };

    public static final CelularAutomata2D.Rule2D seeds = (automata, i, j) -> automata.sumNeighbors(i, j, 1, true) == 2;

    public static final CelularAutomata3D.Rule3D basic3D = (automata, i, j, k) -> {
        int sum = automata.sumNeighbors(i, j, k, 1, true);
        if (automata.getGridCell(i,j, k)) {
            return sum >= 3 && sum <= 7;
        } else {
            return sum >= 4 && sum <= 9;
        }
    };

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

    public static final CelularAutomata3D.Rule3D ring3D = (automata, i, j, k) ->
            automata.sumNeighbors(i, j, k, 1, true) == 0 && automata.sumNeighbors(i, j, k, 2, true) > 0;


    public static final CelularAutomata2D.Rule2D even2D = (automata, i, j) -> {
        int sum = automata.sumNeighbors(i, j, 1, true);
        return sum % 2 == 0 && sum > 0;
    };

    public static final CelularAutomata2D.Rule2D odd2D = (automata, i, j) -> {
        int sum = automata.sumNeighbors(i, j, 1, true);
        return sum % 2 == 1;
    };

}
