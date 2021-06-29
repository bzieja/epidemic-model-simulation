package pl.bzieja.pandemicmodel.model;

/**
 * @author Bartłomiej Zieja
 */
public class AppConfig {
    public static final int TIME_STEP_IN_SIMULATION_CLOCK = 10;
    public static final int ITERATION_TIME = 300;
    public static final int DAYS_OF_SIMULATION = 30;
    public static final int TIME_OF_THE_DISEASE = 14;
    public static final int TIME_OF_THE_QUARANTINE = 14;
    public static final int TIME_OF_THE_IMMUNITY_AFTER_DISEASE = 99;

    public static final int NUMBER_OF_GROUPS_GOING_TO_WORK = 5;
    public static final int NUMBER_OF_GROUPS_GOING_TO_DINNER = 5;
    public static final int NUMBER_OF_GROUPS_GOING_BACK_HOME = 5;
    public static final int PERCENTAGE_OF_PEOPLE_GOING_TO_THE_DINNER = 20;
}
