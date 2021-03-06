package pl.bzieja.pandemicmodel.core;

/**
 * @author Bartłomiej Zieja
 *
 * TIME_STEP_IN_SIMULATION_WORLD - one cell move in simulation world takes that amount of time in simulation model
 *
 * ONE_ITERATION_TIME_IN_MS - set to:
 * 400 - for slower simulation and for observing simulation - remember to set SHOULD_RENDER_VIEW to 'true'
 * 10 - for faster simulation and simulation without view - remember to set SHOULD_RENDER_VIEW to 'false'
 *
 */
public class AppConfig {
    //////////////////////  MAIN CONFIG    //////////////////////
    public static final int TIME_STEP_IN_SIMULATION_WORLD = 10;
    public static final int ONE_ITERATION_TIME_IN_MS = 700;
    public static final boolean SHOULD_RENDER_VIEW = true;
    public static boolean IS_VACCINATION = false;
    public static double VACCINE_EFFECTIVENESS = 80.0;
    public static final String FILE_WITH_RESULT_NAME = "Results";

    public static final int DAYS_OF_SIMULATION = 32;
    public static final int TIME_OF_THE_DISEASE = 14;
    public static final int TIME_OF_THE_QUARANTINE = 14;
    public static final int TIME_OF_THE_IMMUNITY_AFTER_DISEASE = 99;

    //////////////////////  GROUPS VARIABLES    //////////////////////
    public static final int NUMBER_OF_GROUPS_GOING_TO_WORK = 5;
    public static final int NUMBER_OF_GROUPS_GOING_TO_DINNER = 5;
    public static final int NUMBER_OF_GROUPS_GOING_BACK_HOME = 5;
    public static final int PERCENTAGE_OF_PEOPLE_GOING_TO_THE_DINNER = 20;

    //////////////////////  INFECTION VARIABLES    //////////////////////
    public static final int INFECTION_RADIUS_IN_THE_BUILDING = 2;
    public static final int INFECTION_RADIUS_OUTSIDE = 1;
    public static double PROBABILITY_OF_BEING_INFECTED_BY_ONE_PERSON_IN_THE_BUILDING = 0.05;
    public static double PROBABILITY_OF_BEING_INFECTED_BY_ONE_PERSON_OUTSIDE = 0.01;  //0.1
    public static final int NUMBER_OF_INITIALLY_INFECTED_WORKERS = 10;
    public static final int PROBABILITY_OF_BEING_ASYMPTOMATICALLY_INFECTED = 30;

    //////////////////////  HOUR VARIABLES AND EVENTS   //////////////////////
    public static final String THE_START_OF_THE_DAY = "07:00:00";
    public static final String THE_END_OF_THE_DAY = "18:00:00";

    public static final String START_MORNING_EVENT_HOUR = "07";
    public static final String START_OF_END_DAY_EVENT_HOUR = "16";
    public static final String START_OF_DINNER_EVENT_HOUR = "12";
    public static final String START_OF_END_DINNER_EVENT_HOUR = "14";

    public static void setIsVaccination(boolean isVaccination) {
        IS_VACCINATION = isVaccination;
    }

    public static void setVaccineEffectiveness(double vaccineEffectiveness) {
        VACCINE_EFFECTIVENESS = vaccineEffectiveness;
    }

    public static void setProbabilityOfBeingInfectedByOnePersonInTheBuilding(double probabilityOfBeingInfectedByOnePersonInTheBuilding) {
        PROBABILITY_OF_BEING_INFECTED_BY_ONE_PERSON_IN_THE_BUILDING = probabilityOfBeingInfectedByOnePersonInTheBuilding;
    }

    public static void setProbabilityOfBeingInfectedByOnePersonOutside(double probabilityOfBeingInfectedByOnePersonOutside) {
        PROBABILITY_OF_BEING_INFECTED_BY_ONE_PERSON_OUTSIDE = probabilityOfBeingInfectedByOnePersonOutside;
    }
}
