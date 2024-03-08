package org.example.objects;

import org.example.interfaces.IOccupationAction;

public class Worker {
    public static int daysUntilStarvation = 5;
    private String name;
    private String occupation;
    private IOccupationAction occupationAction;
    private boolean hungry;
    private boolean alive;
    private int daysHungry;

    public Worker(String name, String occupation, IOccupationAction occupationAction, boolean hungry, boolean alive, int daysHungry) {
        this.name = name;
        this.occupation = occupation;
        this.occupationAction = occupationAction;
        this.alive = alive;
        this.hungry = hungry;
        this.daysHungry = daysHungry;
    }

    public Worker(String name, String occupation, IOccupationAction occupationAction) {
        this.name = name;
        this.occupation = occupation;
        this.occupationAction = occupationAction;
        hungry = false;
        alive = true;
        // Changed daysHungry to 1 because it makes more sense to have a worker be hungry for 1 day
        // when starved for 1 day
        daysHungry = 1;
    }

    public void DoWork() {
        if (!alive) {
            System.out.println(name + " is not alive and cannot work...");
            return;
        }
        if (!hungry) {
            occupationAction.Work(name);
            hungry = true;
        }
        else {
            daysHungry++;
            // changed >= to > to keep mechanic consistent after changing base value of daysHungry to 1.
            if (daysHungry > daysUntilStarvation) {
                alive = false;
                System.out.println(getName() + " has died of hunger!");
            }
        }
    }

    public void Feed() {
        if (alive) {
            daysHungry = 1;
            hungry = false;
        }
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
    public IOccupationAction getOccupationAction() {
        return occupationAction;
    }
    public void setOccupationAction(IOccupationAction occupationAction) {
        this.occupationAction = occupationAction;
    }
    public boolean isHungry() {
        return hungry;
    }
    public void setHungry(boolean hungry) {
        this.hungry = hungry;
    }
    public int getDaysHungry() {
        return daysHungry;
    }
    public void setDaysHungry(int daysHungry) {
        this.daysHungry = daysHungry;
    }
    public boolean isAlive() {
        return alive;
    }
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
