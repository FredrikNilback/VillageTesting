package org.example;

import org.example.objects.Worker;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class VillageTest {
    Village village = new Village();
    @BeforeEach
    public void resetVillage() {
        village = new Village();
    }
    @Test
    public void try_starving_villager() {
        village.setFood(0);
        village.addWorker("Starvin' Marvin", "builder");
        for (int i = 0; i < 6; i++) {
            village.day();
        }
        boolean actual = village.isGameOver();
        assertTrue(actual);
    }
    @Test
    public void try_adding_more_workers_than_allowed() {
        village.addWorker("name0", "farmer");
        village.addWorker("name1", "farmer");
        village.addWorker("name2", "farmer");
        village.addWorker("name3", "farmer");
        village.addWorker("name4", "farmer");
        village.addWorker("name5", "farmer");
        village.addWorker("name6_should_not_be_allowed", "farmer");

        int expected = 6;
        int actual = village.getWorkers().size();

        assertEquals(expected, actual);
    }

    @Test
    public void verify_names_of_workers(){
        village.addWorker("name0", "farmer");
        village.addWorker("sameName", "farmer");
        village.addWorker("sameName", "farmer");
        village.addWorker("sameName", "farmer");
        village.addWorker("name1", "farmer");
        village.addWorker("name2", "farmer");
        village.addWorker("sameName", "farmer");

        ArrayList<Worker> workers = village.getWorkers();

        Worker worker0 = workers.get(0);
        Worker worker1 = workers.get(1);
        Worker worker2 = workers.get(2);
        Worker worker3 = workers.get(3);
        Worker worker4 = workers.get(4);
        Worker worker5 = workers.get(5);

        boolean actual = worker1.getName().equals(worker2.getName()) &&
                         worker2.getName().equals(worker3.getName()) &&
                         !(worker3.getName().equals(worker5.getName()));
        assertTrue(actual);
    }

    @Test
    public void try_passing_several_days_check_isGameOver() {
        for (int i = 0; i < 20; ++i) {
            village.day();
        }
        boolean actual = village.isGameOver();
        assertFalse(actual);
    }

    @Test
    public void try_passing_several_days_check_starting_resources_the_same() {
        for (int i = 0; i < 20; ++i) {
            village.day();
        }
        int expected = 10;
        int actual = village.getFood();
        assertEquals(expected, actual);
    }

    @Test
    public void try_passing_several_days_with_worker_should_starve() {
        village.addWorker("Starvin' Marvin", "builder");
        for (int i = 0; i < 20; ++i) {
            village.day();
        }
        boolean actual = village.isGameOver();
        assertTrue(actual);
    }

    @Test
    public void try_passing_several_days_with_worker_should_starve_daysGone_should_be_14() {
        village.addWorker("Starvin' Marvin", "builder");
        for (int i = 0; i < 20; ++i) {
            village.day();
        }

        int expected = 14;
        int actual = village.getDaysGone();

        assertEquals(expected, actual);
    }

    @Test
    public void try_adding_projects_with_INsufficient_resources() {
        village.addProject("House");

        boolean actual = village.getProjects().isEmpty();

        assertTrue(actual);
    }

    @Test
    public void try_adding_projects_with_sufficient_resources() {
        village.addWorker("Fred the Farmer", "farmer");
        village.addWorker("Bob the Builder", "builder");
        village.addWorker("Ludmilla the Lumberjack", "lumberjack");

        for (int i = 1; i <= 20; ++i) {
            village.day();
        }

        village.addProject("House");
        boolean actual = village.getProjects().size() == 1;

        assertTrue(actual);
    }

    @Test
    public void try_building_castle_with_sufficient_resources() {
        village.setFood(500);
        village.setWood(50);
        village.setMetal(50);
        village.addWorker("Bob", "builder");
        village.addProject("Castle");
        for (int i = 0; i < 50; ++i) {
            village.day();
        }

        boolean actual = village.isGameOver();

        assertTrue(actual);
    }

    @Test
    public void build_woodmill_woodPerDay_should_increase_to_2() {
        village.setWood(5);
        village.setMetal(1);
        village.addWorker("Bob", "builder");
        village.addProject("Woodmill");

        for (int i = 0; i < 5; ++i) {
            village.day();
            village.setFood(5);
        }

        boolean actual = village.getWoodPerDay() == 2;
        assertTrue(actual);
    }

    @Test
    public void OverBuilding_Should_see_progress_on_next_build() {
        village.setFood(100);
        village.setWood(100);
        village.setMetal(100);
        for (int i = 0; i < 5; ++i) {
            village.addWorker("Bob", "builder");
        }
        village.addProject("House");
        village.addProject("House");
        village.day();

        int expected = 1;
        int actual = village.getProjects().get(0).getDaysLeft();

        assertEquals(expected, actual);
    }

    @Test
    public void check_produced_resources_no_bonuses() {
        village.setFood(10);
        village.setWood(10);
        village.setMetal(10);

        village.addWorker("Martin", "miner");
        village.addWorker("Fred", "farmer");
        village.addWorker("Ludmilla", "lumberjack");

        village.day();

        int expectedFood = 12;
        int expectedWood = 11;
        int expectedMetal = 11;

        int actualFood = village.getFood();
        int actualWood = village.getWood();
        int actualMetal = village.getMetal();

        assertEquals(expectedFood, actualFood);
        assertEquals(expectedWood, actualWood);
        assertEquals(expectedMetal, actualMetal);
    }

    @Test
    public void make_sure_dead_people_do_not_eat() {
        village.setFood(0);
        village.addWorker("Starvin' Marvin", "builder");
        village.day();
        village.addWorker("Will Survive", "builder");
        for(int i = 0; i < 5; ++i) {
            village.day();
        }
        village.setFood(5000);
        for (int i = 0; i < 10; ++i) {
            village.day();
        }

        int expected = 4990;
        int actual = village.getFood();

        assertEquals(expected, actual);
    }

    @Test
    public void run_the_entire_game_lol() {
        // day 0
        village.addWorker("Farmer", "farmer");
        village.addWorker("Lumberjack", "lumberjack");
        village.addWorker("Lumberjack", "lumberjack");
        village.addWorker("Lumberjack", "lumberjack");
        village.addWorker("Builder", "builder");
        village.addWorker("Builder", "builder");
        village.day();

        // day 1
        village.day();

        // day 2
        village.addProject("House");
        village.day();

        // day 3
        village.day();

        // day 4
        village.addProject("House");
        village.addWorker("Lumberjack", "lumberjack");
        village.addWorker("Builder", "builder");
        village.day();

        // day 5
        village.addProject("House");
        village.addWorker("Farmer", "farmer");
        village.addWorker("Lumberjack", "lumberjack");
        village.day();

        // day 6
        village.addProject("House");
        village.day();

        // day 7
        village.addWorker("Farmer", "farmer");
        village.addWorker("Miner", "miner");
        village.addProject("House");
        village.day();

        // day 8
        village.addWorker("Miner", "miner");
        village.addWorker("Miner", "miner");
        village.addProject("House");
        village.day();

        // day 9
        village.addWorker("Farmer", "farmer");
        village.addWorker("Miner", "miner");
        village.addProject("House");
        village.day();

        // day 10
        village.addWorker("Lumberjack", "lumberjack");
        village.addWorker("Builder", "builder");
        village.addProject("House");
        village.day();

        // day 11
        village.addWorker("Builder", "builder");
        village.addWorker("Farmer", "farmer");
        village.addProject("Woodmill");
        village.day();

        // day 12
        village.addWorker("Lumberjack", "lumberjack");
        village.addWorker("Lumberjack", "lumberjack");
        village.addProject("House");

        village.day();

        // day 13
        village.addWorker("Lumberjack", "lumberjack");
        village.addWorker("Lumberjack", "lumberjack");
        village.addProject("House");
        village.addProject("House");

        village.day();

        // day 14
        village.addWorker("Builder", "builder");
        village.addWorker("Farmer", "farmer");
        village.addProject("House");
        village.addProject("House");
        village.addProject("Quarry");

        village.day();

        // day 15
        village.addWorker("Builder", "builder");
        village.addWorker("Builder", "builder");
        village.addWorker("Builder", "builder");
        village.addWorker("Builder", "builder");
        village.addProject("Farm");
        village.addProject("House");
        village.addProject("Quarry");

        village.day();

        // day 16
        village.addWorker("Builder", "builder");
        village.addWorker("Builder", "builder");
        village.addProject("House");
        village.addProject("House");
        village.addProject("Quarry");

        village.day();

        // day 17
        village.addWorker("Miner", "miner");
        village.addWorker("Miner", "miner");
        village.addProject("House");
        village.addProject("House");
        village.addProject("Quarry");

        village.day();

        // day 18
        village.addWorker("Builder", "builder");
        village.addWorker("Builder", "builder");
        village.addWorker("Builder", "builder");
        village.addWorker("Builder", "builder");
        village.addProject("House");
        village.addProject("House");
        village.addProject("Quarry");

        village.day();

        // day 19
        village.addWorker("Builder", "builder");
        village.addWorker("Builder", "builder");
        village.addWorker("Builder", "builder");
        village.addWorker("Builder", "builder");
        village.addProject("Castle");

        village.day();

        // day 20
        village.addWorker("Builder", "builder");
        village.addWorker("Builder", "builder");
        village.addWorker("Builder", "builder");
        village.addWorker("Builder", "builder");

        village.day();

        // day 21
        village.day();

        //check that the game is over and that the last building built is the castle.
        boolean actual = village.isGameOver() &&
                         village.getBuildings().get(village.getBuildings().size() - 1).getName().equals("Castle");

        assertTrue(actual);

    }




}
