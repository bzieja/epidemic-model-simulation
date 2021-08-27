package pl.bzieja.pandemicmodel.model.cell;

import java.awt.*;
import java.util.List;
import java.util.EnumSet;

public enum Building {

    WORKER      (0, new Color(Integer.parseInt("093ad7", 16))),
    CROWD       (0, new Color(Integer.parseInt("751461", 16))),

    SPAWN       (0, new Color(Integer.parseInt("b5622f", 16))),
    PATH        (0, new Color(Integer.parseInt("ba6937", 16))),
    PARKING     (0, new Color(Integer.parseInt("c0c0c0", 16))),
    WALL        (0, new Color(Integer.parseInt("000000", 16))),
    BUILDING_INTERIOR(0, new Color(Integer.parseInt("bc8f8f", 16))),

    KRAKUS			(7, new Color(Integer.parseInt("80477e", 16))),
    TERRACE_KRAKUS	(7, new Color(Integer.parseInt("80478e", 16))),
    NAWOJKA			(7, new Color(Integer.parseInt("80479e", 16))),
    OBIEZYSWIAT		(7, new Color(Integer.parseInt("2f2943", 16))),
    MENSA			(7, new Color(Integer.parseInt("2f2945", 16))),

    B9      (47, new Color(Integer.parseInt("80471e", 16))),
    B7      (55, new Color(Integer.parseInt("80472e", 16))),
    B5      (110, new Color(Integer.parseInt("80473e", 16))),
    D5      (17, new Color(Integer.parseInt("80474e", 16))),
    D6      (7, new Color(Integer.parseInt("80475e", 16))),
    DS_ALFA (6, new Color(Integer.parseInt("80476e", 16))),
    D1      (107, new Color(Integer.parseInt("804790", 16))),
    U2      (28, new Color(Integer.parseInt("804791", 16))),
    H_A2    (21, new Color(Integer.parseInt("2f2920", 16))),
    H_A1    (8, new Color(Integer.parseInt("2f2921", 16))),
    A0      (380, new Color(Integer.parseInt("2f2922", 16))),
    A2      (167, new Color(Integer.parseInt("2f2923", 16))),
    A1      (165, new Color(Integer.parseInt("2f2924", 16))),
    C1      (258, new Color(Integer.parseInt("2f2925", 16))),
    C2      (225, new Color(Integer.parseInt("2f2926", 16))),
    C3      (88, new Color(Integer.parseInt("2f2927", 16))),
    U1      (104, new Color(Integer.parseInt("2f2928", 16))),

    C4      (174, new Color(Integer.parseInt("2f2929", 16))),
    C6      (12, new Color(Integer.parseInt("2f2930", 16))),
    C5      (23, new Color(Integer.parseInt("2f2931", 16))),
    A4      (218, new Color(Integer.parseInt("2f2932", 16))),
    A3      (162, new Color(Integer.parseInt("2f2933", 16))),
    H_B1B2  (1, new Color(Integer.parseInt("2f2934", 16))),
    H_B3B4  (24, new Color(Integer.parseInt("2f2935", 16))),
    B1      (211, new Color(Integer.parseInt("2f2936", 16))),
    P_B1B2  (1, new Color(Integer.parseInt("2f2937", 16))),
    B2      (169, new Color(Integer.parseInt("2f2938", 16))),
    P_B2B3  (2, new Color(Integer.parseInt("2f2939", 16))),
    B3      (119, new Color(Integer.parseInt("2f2940", 16))),
    P_B3B4                  (1, new Color(Integer.parseInt("2f2941", 16))),
    B4                      (97, new Color(Integer.parseInt("2f2942", 16))),
    E_LEARNING_CENTER       (7, new Color(Integer.parseInt("2f2944", 16))),
    D4                      (50, new Color(Integer.parseInt("2f2946", 16))),
    MEDICAL_CENTER          (7, new Color(Integer.parseInt("2f2947", 16))),
    B8                      (60, new Color(Integer.parseInt("2f2948", 16))),
    B6                      (83, new Color(Integer.parseInt("2f2949", 16))),
    H_B6                    (7, new Color(Integer.parseInt("2f2950", 16)));

    public static EnumSet<Building> walkable = EnumSet.of(SPAWN, PATH, PARKING, BUILDING_INTERIOR, B9, B7, B5, D5, D6, DS_ALFA, D1, U2, H_A2, H_A1, A0, A2,
            A1, C1, C2, C3, U1, C4, C6, C5, A4, A3, H_B1B2, H_B3B4, B1, P_B1B2, B2, P_B2B3, B3, P_B3B4, B4, E_LEARNING_CENTER, D4, MEDICAL_CENTER, B8, B6, H_B6,
            KRAKUS, TERRACE_KRAKUS, NAWOJKA, OBIEZYSWIAT, MENSA);
    public static EnumSet<Building> gastronomy = EnumSet.of(KRAKUS, TERRACE_KRAKUS, NAWOJKA, OBIEZYSWIAT, MENSA);
    public static EnumSet<Building> buildings = EnumSet.of(B9, B7, B5, D5, D6, DS_ALFA, D1, U2, H_A2, H_A1, A0, A2,
            A1, C1, C2, C3, U1, C4, C6, C5, A4, A3, H_B1B2, H_B3B4, B1, P_B1B2, B2, P_B2B3, B3, P_B3B4, B4, E_LEARNING_CENTER, D4, MEDICAL_CENTER, B8, B6, H_B6,
            KRAKUS, TERRACE_KRAKUS, NAWOJKA, OBIEZYSWIAT, MENSA, SPAWN, BUILDING_INTERIOR); //SPAWN AND BUILDING INTERIOR NEEDED FOR PERSONS ALGORITHMS

    private final int numberOfWorkers;
    private final Color color;
    private int[][] routeMap;
    private List<Cell> cellsWhichBelongsToGivenBuilding;

    Building(int numberOfWorkers, Color color) {
        this.numberOfWorkers = numberOfWorkers;
        this.color = color;
    }

    public int getNumberOfWorkers() {
        return numberOfWorkers;
    }

    public Color getColor() {
        return color;
    }

    public void setRouteMap(int[][] routeMap) {
        this.routeMap = routeMap;
    }

    public int[][] getRouteMap() {
        return routeMap;
    }

    public void setCellsWhichBelongsToGivenBuilding(List<Cell> cellsWhichBelongsToGivenBuilding) {
        this.cellsWhichBelongsToGivenBuilding = cellsWhichBelongsToGivenBuilding;
    }

    public List<Cell> getCellsWhichBelongsToGivenBuilding() {
        return cellsWhichBelongsToGivenBuilding;
    }
}
