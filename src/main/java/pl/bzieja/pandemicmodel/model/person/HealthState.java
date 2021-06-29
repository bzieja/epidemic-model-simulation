package pl.bzieja.pandemicmodel.model.person;

import pl.bzieja.pandemicmodel.model.AppConfig;

import java.util.EnumSet;

/**
 * @author Bartłomiej Zieja
 */
public enum HealthState {
    HEALTHY(0),
    SYMPTOMATICALLY_ILL(AppConfig.TIME_OF_THE_DISEASE),
    ASYMPTOMATICALLY_ILL(AppConfig.TIME_OF_THE_DISEASE),
    QUARANTINED(AppConfig.TIME_OF_THE_QUARANTINE),
    CONVALESCENT(AppConfig.TIME_OF_THE_IMMUNITY_AFTER_DISEASE);

    private int durationTimeInDays;

    HealthState(int durationTimeInDays) {
        this.durationTimeInDays = durationTimeInDays;
    }

    public static EnumSet<HealthState> workable = EnumSet.of(HEALTHY, ASYMPTOMATICALLY_ILL, CONVALESCENT, SYMPTOMATICALLY_ILL);

}
