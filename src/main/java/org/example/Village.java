package org.example;

import org.example.interfaces.IOccupationAction;
import org.example.objects.Building;
import org.example.objects.PossibleProject;
import org.example.objects.Project;
import org.example.objects.Worker;

import java.util.ArrayList;
import java.util.HashMap;

public class Village {

    private boolean gameOver = false;
    private int food = 0;
    private int wood = 0;
    private int metal = 0;
    private ArrayList<Worker> workers = new ArrayList<>();
    private ArrayList<Building> buildings = new ArrayList<>();
    private ArrayList<Project> projects = new ArrayList<>();
    private final HashMap<String, IOccupationAction> occupationHashMap = new HashMap<>();
    private final HashMap<String, PossibleProject> possibleProjects = new HashMap<>();
    private int metalPerDay = 1;
    private int woodPerDay = 1;
    private int foodPerDay = 5;
    private int maxWorkers = 0;
    private int daysGone = 0;

    public Village(boolean gameOver, int food, int wood, int metal, ArrayList<Worker> workers, ArrayList<Building> buildings, ArrayList<Project> projects, int metalPerDay, int woodPerDay, int foodPerDay, int maxWorkers, int daysGone, int daysUntilStarvation) {
        this();
        this.gameOver = gameOver;
        this.food = food;
        this.wood = wood;
        this.metal = metal;
        this.workers = workers;
        this.buildings = buildings;
        this.projects = projects;
        this.metalPerDay = metalPerDay;
        this.woodPerDay = woodPerDay;
        this.foodPerDay = foodPerDay;
        this.maxWorkers = maxWorkers;
        this.daysGone = daysGone;
        Worker.daysUntilStarvation = daysUntilStarvation;
    }

    public Village() {
        occupationHashMap.put("farmer", name -> addFood(name));
        occupationHashMap.put("lumberjack", name -> addWood(name));
        occupationHashMap.put("miner", name -> addMetal(name));
        occupationHashMap.put("builder", name -> build(name));

        possibleProjects.put("House", new PossibleProject("House", 5,0,3, () -> newHouse()));
        possibleProjects.put("Woodmill", new PossibleProject("Woodmill", 5,1,5, () -> newWoodmill()));
        possibleProjects.put("Quarry", new PossibleProject("Quarry", 3,5,7, () -> newQuarry()));
        possibleProjects.put("Farm", new PossibleProject("Farm", 5,2,5, () -> newFarm()));
        possibleProjects.put("Castle", new PossibleProject("Castle", 50,50,50, () -> newCastle()));

        buildings.add(new Building("House"));
        buildings.add(new Building("House"));
        buildings.add(new Building("House"));
        maxWorkers = 6;
        food = 10;
    }
    public void day() {
        feedWorkers();
        boolean someoneAlive = false;
        for (Worker worker: workers) {
            worker.DoWork();
            if (worker.isAlive()) {
                someoneAlive = true;
            }
        }
        // added if so the daysGone didn't keep increasing when the game was already over.
        if(someoneAlive) {
            daysGone++;
        }
        if (!someoneAlive && workers.size() > 0) {
            System.out.println("Everyone is dead! You lasted " + daysGone + " days!");
            gameOver();
        }
    }

    public void gameOver() {
        gameOver = true;
    }


    public void printInfo() {
        if (workers.size() > 0) {
            System.out.println("You have " + workers.size() + " workers. They are: ");
            for (Worker worker : workers) {
                System.out.println(worker.getName() + ", " + worker.getOccupation() + ".");
                if (worker.isHungry() && worker.getDaysHungry() > 0) {
                    System.out.println(worker.getName() + " has been hungry for " + worker.getDaysHungry() + " days!");
                }
            }
        }
        else {
            System.out.println("You have no workers.");
        }
        System.out.println("Your current buildings are: ");
        for (Building building : buildings) {
            System.out.print(building.getName() + " ");
        }
        System.out.println();
        System.out.println("You can have " + maxWorkers + " workers.");
        System.out.println("Your current projects are: ");
        for (Project project : projects) {
            System.out.print(project.getName() + ", " + project.getDaysLeft() + " points left until completion.");
        }
        System.out.println();
        System.out.println("Current Food:  " + food);
        System.out.println("Current Wood:  " + wood);
        System.out.println("Current Metal: " + metal);
        System.out.println("Generating " + foodPerDay + " food per day per worker.");
        System.out.println("Generating " + woodPerDay + " wood per day per worker.");
        System.out.println("Generating " + metalPerDay + " metal per day per worker.");
    }


    public void addWorker(String name, String occupation) {
        // added the && so that adding more workers than there is room no longer is possible.
        if(occupationHashMap.containsKey(occupation) && this.maxWorkers > this.workers.size()) {
            IOccupationAction jobInterface = occupationHashMap.get(occupation);
            Worker worker = new Worker(name, occupation, jobInterface);
            workers.add(worker);
            System.out.println(name + " was successfully added.");
            return;
        }
        if(!(this.maxWorkers > this.workers.size())) {
            System.out.println("Not enough room for a new worker.");
        }
        else {
            System.out.println("There is no such job.");
        }
    }

    public void addProject(String name) {
        if (possibleProjects.containsKey(name)) {
            PossibleProject possibleProject = possibleProjects.get(name);
            // getWood() and getMetal() used to be > instead of >=
            // this caused issues when trying to build when you had exactly the right amount of resources.
            if (getWood() >= possibleProject.getWoodCost() &&
                    getMetal() >= possibleProject.getMetalCost()) {
                wood -= possibleProject.getWoodCost();
                metal -= possibleProject.getMetalCost();

                Project newProject = possibleProject.GetProject();
                projects.add(newProject);
                System.out.println(newProject.getName() + " added to the project queue!");
                return;
            }
            System.out.println("Not enough material!");
            return;
        }
        System.out.println("That was not one of the options.");
    }

    public void addFood(String name) {
        food += foodPerDay;
        System.out.println(name + " gathers " + foodPerDay + " food!");
    }
    public void addMetal(String name) {
        metal += metalPerDay;
        System.out.println(name + " gathers " + metalPerDay + " metal!");
    }
    public void addWood(String name) {
        wood += woodPerDay;
        System.out.println(name + " gathers " + woodPerDay + " wood!");
    }
    public void build(String name) {
        if (projects.size() > 0) {
            Project currentProject = projects.get(0);
            System.out.println(name + " builds on " + currentProject.getName() + "!");
            boolean complete = currentProject.BuildOn();
            if (complete) {
                projects.remove(currentProject);
                buildings.add(new Building(currentProject.getName()));
                System.out.println(currentProject.getName() + " was completed!");
                currentProject.Complete();
            }
        }
        else {
            System.out.println("No buildings for " + name + " to work on!");
        }
    }

    private void feedWorkers() {
        for (Worker worker: workers) {
            if (food > 0 && worker.isAlive()) {
                worker.Feed();
                System.out.print(worker.getName() + " eats. ");
                food--;
            }
            else {
                if (worker.isAlive()) {
                    System.out.println("No food left for " + worker.getName() + "! " + worker.getDaysHungry() + " days without food! ");
                }
                else {
                    System.out.println(worker.getName() + " is dead...");
                }
            }
        }
        System.out.println();
    }

    private void newHouse() {
        maxWorkers += 2;
    }
    private void newFarm() {
        foodPerDay += 5;
    }
    private void newQuarry() {
        metalPerDay++;
    }
    private void newWoodmill() {
        woodPerDay++;
    }
    private void newCastle() {
        System.out.println("Castle complete! It took " + daysGone + " days!");
        gameOver();
    }

    public int getFood() {
        return food;
    }
    public void setFood(int food) {
        this.food = food;
    }
    public int getWood() {
        return wood;
    }
    public void setWood(int wood) {
        this.wood = wood;
    }
    public int getMetal() {
        return metal;
    }
    public void setMetal(int metal) {
        this.metal = metal;
    }
    public ArrayList<Worker> getWorkers() {
        return workers;
    }
    public void setWorkers(ArrayList<Worker> workers) {
        this.workers = workers;
    }
    public ArrayList<Building> getBuildings() {
        return buildings;
    }
    public void setBuildings(ArrayList<Building> buildings) {
        this.buildings = buildings;
    }
    public ArrayList<Project> getProjects() {
        return projects;
    }
    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
    }
    public int getMetalPerDay() {
        return metalPerDay;
    }
    public void setMetalPerDay(int metalPerDay) {
        this.metalPerDay = metalPerDay;
    }
    public int getWoodPerDay() {
        return woodPerDay;
    }
    public void setWoodPerDay(int woodPerDay) {
        this.woodPerDay = woodPerDay;
    }
    public int getFoodPerDay() {
        return foodPerDay;
    }
    public void setFoodPerDay(int foodPerDay) {
        this.foodPerDay = foodPerDay;
    }
    public int getDaysGone() {
        return daysGone;
    }
    public void setDaysGone(int daysGone) {
        this.daysGone = daysGone;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public HashMap<String, IOccupationAction> getOccupationHashMap() {
        return occupationHashMap;
    }

    public HashMap<String, PossibleProject> getPossibleProjects() {
        return possibleProjects;
    }

    public int getMaxWorkers() {
        return maxWorkers;
    }

    public void setMaxWorkers(int maxWorkers) {
        this.maxWorkers = maxWorkers;
    }

    public boolean isFull() {
        return maxWorkers <= workers.size();
    }
}
