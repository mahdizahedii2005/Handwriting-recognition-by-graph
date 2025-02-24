package Listener;

import java.util.Scanner;

public class CmdListener implements Listenable {
    private final Scanner sc;

    public CmdListener() {
        sc = new Scanner(System.in);
    }

    @Override
    public String listen() {
        return sc.nextLine();
    }

    @Override
    public boolean hasAnyCommand() {
        return sc.hasNextLine();
    }
}
