package pl.bzieja.pandemicmodel.model.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.bzieja.pandemicmodel.model.AppConfig;
import pl.bzieja.pandemicmodel.model.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bartłomiej Zieja
 */
@Component
public class EventContainer {

    Model model;
    private Map<String, Event> hoursToEventsMap = new HashMap<>();
    private List<String> listOfHoursWhenMorningEventOccurs = new ArrayList<>();
    private List<String> listOfHoursWhenDinnerStartEventOccurs = new ArrayList<>();
    private List<String> listOfHoursWhenDinnerEndsEventOccurs = new ArrayList<>();
    private List<String> listOfHoursWhenTheEndOfTheDayEventOccurs = new ArrayList<>();

    @Autowired
    public EventContainer(Model model) {
        this.model = model;
        createHoursForMorningWorkEvent();
        createHoursForDinnerStartEvent();
        createHoursForDinnerEndEvent();
        createHoursForEndOfTheDayEvent();
        listOfHoursWhenMorningEventOccurs.forEach(h -> hoursToEventsMap.put(h, new MorningWorkEvent(model)));
        listOfHoursWhenDinnerStartEventOccurs.forEach(h -> hoursToEventsMap.put(h, new DinnerStartEvent(model)));
        listOfHoursWhenDinnerEndsEventOccurs.forEach(h -> hoursToEventsMap.put(h, new DinnerEndsEvent(model)));
        listOfHoursWhenTheEndOfTheDayEventOccurs.forEach(h -> hoursToEventsMap.put(h, new TheEndOfTheDayEvent(model)));
    }

    private void createHoursForMorningWorkEvent() {
        int minuteOfEvent = 50 / AppConfig.NUMBER_OF_GROUPS_GOING_TO_WORK;

        for (int i = 1; i <= AppConfig.NUMBER_OF_GROUPS_GOING_TO_WORK; i++) {
            String minuteOfEventAsString = (i * minuteOfEvent) < 10 ? "0" + (i * minuteOfEvent) : String.valueOf((i * minuteOfEvent));
            String eventTime = "07:" + minuteOfEventAsString + ":00";
            listOfHoursWhenMorningEventOccurs.add(eventTime);
        }
    }

    private void createHoursForEndOfTheDayEvent() {
        int minuteOfEvent = 50 / AppConfig.NUMBER_OF_GROUPS_GOING_TO_WORK;

        for (int i = 1; i <= AppConfig.NUMBER_OF_GROUPS_GOING_TO_WORK; i++) {
            String minuteOfEventAsString = (i * minuteOfEvent) < 10 ? "0" + (i * minuteOfEvent) : String.valueOf((i * minuteOfEvent));
            String eventTime = "16:" + minuteOfEventAsString + ":00";
            listOfHoursWhenTheEndOfTheDayEventOccurs.add(eventTime);
        }
    }

    private void createHoursForDinnerStartEvent() {
        int minuteOfEvent = 50 / AppConfig.NUMBER_OF_GROUPS_GOING_TO_DINNER;

        for (int i = 1; i <= AppConfig.NUMBER_OF_GROUPS_GOING_TO_DINNER; i++) {
            String minuteOfEventAsString = (i * minuteOfEvent) < 10 ? "0" + (i * minuteOfEvent) : String.valueOf((i * minuteOfEvent));
            String eventTime = "12:" + minuteOfEventAsString + ":00";
            listOfHoursWhenDinnerStartEventOccurs.add(eventTime);
        }
    }

    private void createHoursForDinnerEndEvent() {
        int minuteOfEvent = 50 / AppConfig.NUMBER_OF_GROUPS_GOING_TO_DINNER;

        for (int i = 1; i <= AppConfig.NUMBER_OF_GROUPS_GOING_TO_DINNER; i++) {
            String minuteOfEventAsString = (i * minuteOfEvent) < 10 ? "0" + (i * minuteOfEvent) : String.valueOf((i * minuteOfEvent));
            String eventTime = "14:" + minuteOfEventAsString + ":00";
            listOfHoursWhenDinnerEndsEventOccurs.add(eventTime);
        }
    }

    public Map<String, Event> getHoursToEventsMap() {
        return hoursToEventsMap;
    }
}
