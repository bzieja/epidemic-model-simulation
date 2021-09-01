package pl.bzieja.pandemicmodel.model.person;

/**
 * @author Bartłomiej Zieja
 */
public class InteractionState {
    private int infectedPersonsInRange = 0;
    private double probabilityOfBeingInfected = 0.0;
    private int daysInSpecialState = 0;

    public void setInfectedPersonsInRange(int infectedPersonsInRange) {
        this.infectedPersonsInRange = infectedPersonsInRange;
    }

    public void setProbabilityOfBeingInfected(double probabilityOfBeingInfected) {
        this.probabilityOfBeingInfected = probabilityOfBeingInfected;
    }

    public int getInfectedPersonsInRange() {
        return infectedPersonsInRange;
    }

    public double getProbabilityOfBeingInfected() {
        return probabilityOfBeingInfected;
    }

    public int getDaysInSpecialState() {
        return daysInSpecialState;
    }

    public void increaseNumberOfDaysInSpecialState() {
        daysInSpecialState++;
    }

    public void resetNumberOfDaysInSpecialState(){
        daysInSpecialState = 0;
    }
}
