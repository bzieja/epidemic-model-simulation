package pl.bzieja.pandemicmodel.model.cell;

import java.awt.*;
import java.util.EnumSet;

public enum Building {

    WORKER      (0, new Color(Integer.parseInt("093ad7", 16))),
    CROWD       (0, new Color(Integer.parseInt("751461", 16))),

    SPAWN       (0, new Color(Integer.parseInt("b5622f", 16))),
    PATH        (0, new Color(Integer.parseInt("ba6937", 16))),
    PARKING     (0, new Color(Integer.parseInt("c0c0c0", 16))),
    WALL        (0, new Color(Integer.parseInt("000000", 16))),
    BUILDING_INTERIOR(0, new Color(Integer.parseInt("bc8f8f", 16))),

    KRAKUS			(20, new Color(Integer.parseInt("80477e", 16))),
    TERRACE_KRAKUS	(20, new Color(Integer.parseInt("80478e", 16))),
    NAWOJKA			(20, new Color(Integer.parseInt("80479e", 16))),
    OBIEZYSWIAT		(20, new Color(Integer.parseInt("2f2943", 16))),
    MENSA			(20, new Color(Integer.parseInt("2f2945", 16))),

    B9      (20, new Color(Integer.parseInt("80471e", 16))),
    B7      (135, new Color(Integer.parseInt("80472e", 16))),
    B5      (218, new Color(Integer.parseInt("80473e", 16))),
    D5      (20, new Color(Integer.parseInt("80474e", 16))),
    D6      (20, new Color(Integer.parseInt("80475e", 16))),
    DS_ALFA (20, new Color(Integer.parseInt("80476e", 16))),
    D1      (20, new Color(Integer.parseInt("804790", 16))),
    U2      (20, new Color(Integer.parseInt("804791", 16))),
    H_A2    (21, new Color(Integer.parseInt("2f2920", 16))),
    H_A1    (20, new Color(Integer.parseInt("2f2921", 16))),
    A0      (66 + 51 + 28 + 66 + 38 + 296 + 266, new Color(Integer.parseInt("2f2922", 16))),
    A2      (88, new Color(Integer.parseInt("2f2923", 16))),
    A1      (20, new Color(Integer.parseInt("2f2924", 16))),
    C1      (75 + 44, new Color(Integer.parseInt("2f2925", 16))),
    C2      (20, new Color(Integer.parseInt("2f2926", 16))),
    C3      (20, new Color(Integer.parseInt("2f2927", 16))),
    U1      (96, new Color(Integer.parseInt("2f2928", 16))),

    C4      (134, new Color(Integer.parseInt("2f2929", 16))),
    C6      (20, new Color(Integer.parseInt("2f2930", 16))),
    C5      (20, new Color(Integer.parseInt("2f2931", 16))),
    A4      (183 + 84, new Color(Integer.parseInt("2f2932", 16))),
    A3      (20, new Color(Integer.parseInt("2f2933", 16))),
    H_B1B2  (20, new Color(Integer.parseInt("2f2934", 16))),
    H_B3B4  (20, new Color(Integer.parseInt("2f2935", 16))),
    B1      (288, new Color(Integer.parseInt("2f2936", 16))),
    P_B1B2  (20, new Color(Integer.parseInt("2f2937", 16))),
    B2      (297, new Color(Integer.parseInt("2f2938", 16))),
    P_B2B3  (20, new Color(Integer.parseInt("2f2939", 16))),
    B3      (20, new Color(Integer.parseInt("2f2940", 16))),
    P_B3B4                  (20, new Color(Integer.parseInt("2f2941", 16))),
    B4                      (20, new Color(Integer.parseInt("2f2942", 16))),
    E_LEARNING_CENTER       (18, new Color(Integer.parseInt("2f2944", 16))),
    D4                      (161, new Color(Integer.parseInt("2f2946", 16))),
    MEDICAL_CENTER          (20, new Color(Integer.parseInt("2f2947", 16))),
    B8                      (230, new Color(Integer.parseInt("2f2948", 16))),
    B6                      (20, new Color(Integer.parseInt("2f2949", 16))),
    H_B6                    (20, new Color(Integer.parseInt("2f2950", 16)));

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
}
