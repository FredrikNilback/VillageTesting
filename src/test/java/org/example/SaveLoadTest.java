package org.example;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class SaveLoadTest {

    @Mock
    DatabaseConnection databaseConnection;
    VillageInput villageInput;
    Village village;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        village = new Village();
        villageInput = new VillageInput(village, databaseConnection);
    }

    @Test
    public void testSave() {
        when(databaseConnection.GetTownNames()).thenReturn(new ArrayList<>(Arrays.asList("Village1", "Village2")));
        when(databaseConnection.SaveVillage(any(Village.class), eq("TestVillage"))).thenReturn(true);

        ByteArrayInputStream in = new ByteArrayInputStream("TestVillage\ny\n".getBytes());
        villageInput.setScanner(new Scanner(in));
        villageInput.Save();

        verify(databaseConnection).GetTownNames();
        verify(databaseConnection).SaveVillage(any(Village.class), eq("TestVillage"));
    }

    @Test
    public void testSaveFail() {
        // Mocking database behavior
        when(databaseConnection.GetTownNames()).thenReturn(new ArrayList<>(Arrays.asList("Village1", "Village2")));
        when(databaseConnection.SaveVillage(any(Village.class), eq("TestVillage"))).thenReturn(false);

        // Mock user input
        ByteArrayInputStream in = new ByteArrayInputStream("TestVillage\nn\n".getBytes());
        villageInput.setScanner(new Scanner(in));
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        villageInput.Save();

        boolean actualErrorMessage = outContent.toString().contains("Error, something went wrong. Could not save.");
        assertTrue(actualErrorMessage);

        verify(databaseConnection).GetTownNames();
        verify(databaseConnection).SaveVillage(any(Village.class), eq("TestVillage"));
    }

    @Test
    public void testLoad() {
        ArrayList<String> villageNames = new ArrayList<>(Arrays.asList("TestVillage", "AnotherVillage"));
        when(databaseConnection.GetTownNames()).thenReturn(villageNames);
        when(databaseConnection.LoadVillage(eq("TestVillage"))).thenReturn(new Village());

        ByteArrayInputStream in = new ByteArrayInputStream("TestVillage\n".getBytes());
        villageInput.setScanner(new Scanner(in));
        villageInput.Load();

        verify(databaseConnection).GetTownNames();
        verify(databaseConnection).LoadVillage(eq("TestVillage"));
    }

    @Test
    public void testLoadFail() {
        ArrayList<String> villageNames = new ArrayList<>(Arrays.asList("TestVillage", "AnotherVillage"));
        when(databaseConnection.GetTownNames()).thenReturn(villageNames);

        ByteArrayInputStream in = new ByteArrayInputStream("IWantToFailVillage\n".getBytes());
        villageInput.setScanner(new Scanner(in));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        villageInput.Load();

        boolean actual = outContent.toString().contains("That's not one of the choices.");
        assertTrue(actual);
        verify(databaseConnection).GetTownNames();
        verify(databaseConnection, never()).LoadVillage(eq("IWantToFailVillage"));
    }
}