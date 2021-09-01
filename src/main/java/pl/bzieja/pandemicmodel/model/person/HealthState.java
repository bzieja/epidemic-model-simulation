package pl.bzieja.pandemicmodel.model.person;

import pl.bzieja.pandemicmodel.model.AppConfig;

import java.awt.*;
import java.util.EnumSet;

/**
 * @author Bartłomiej Zieja
 */
public enum HealthState {
    HEALTHY(0, new Color(Integer.parseInt("093ad7", 16))),
    SYMPTOMATICALLY_ILL(AppConfig.TIME_OF_THE_DISEASE, new Color(Integer.parseInt("19b338", 16))),
    ASYMPTOMATICALLY_ILL(AppConfig.TIME_OF_THE_DISEASE, new Color(Integer.parseInt("a0c217", 16))),
    QUARANTINED(AppConfig.TIME_OF_THE_QUARANTINE, new Color(Integer.parseInt("b5622f", 16))),
    CONVALESCENT(AppConfig.TIME_OF_THE_IMMUNITY_AFTER_DISEASE,new Color(Integer.parseInt("07ebe7", 16)));

    private int durationTimeOfSpecialStateInDays;
    private final Color color;

    HealthState(int durationTimeOfSpecialStateInDays, Color color) {
        this.durationTimeOfSpecialStateInDays = durationTimeOfSpecialStateInDays;
        this.color = color;
    }

    public static EnumSet<HealthState> workable = EnumSet.of(HEALTHY, ASYMPTOMATICALLY_ILL, CONVALESCENT, SYMPTOMATICALLY_ILL);
    public static EnumSet<HealthState> stayAHome = EnumSet.of(QUARANTINED);
    public static EnumSet<HealthState> canInfect = EnumSet.of(SYMPTOMATICALLY_ILL, ASYMPTOMATICALLY_ILL);
    public static EnumSet<HealthState> canBeInfected = EnumSet.of(HEALTHY);

    public Color getColor() {
        return color;
    }

//
//    public void decreaseIllDayByOne(){
//        this.durationTimeOfSpecialStateInDays--;
//    }
//
//    public int getDurationTimeOfSpecialStateInDays() {
//        return durationTimeOfSpecialStateInDays;
//    }
}
