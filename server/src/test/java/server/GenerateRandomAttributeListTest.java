package server;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import shared.*;

public class GenerateRandomAttributeListTest {
    @Test
    public void test5() {
        Gameserver server = new Gameserver();
        for (int j = 0; j < 5; j++) {
            ArrayList<Integer> list = server.generateRandomAttriGroup(5);
            for (int i : list) {
                System.out.println(i);
                assert (i >= 1);
            }
            System.out.println("");
        }

    }

    @Test
    public void test10() {
        Gameserver server = new Gameserver();
        for (int j = 0; j < 5; j++) {
            ArrayList<Integer> list = server.generateRandomAttriGroup(10);
            for (int i : list) {
                System.out.println(i);
                assert (i >= 1);
            }
            System.out.println("");
        }
    }

}
