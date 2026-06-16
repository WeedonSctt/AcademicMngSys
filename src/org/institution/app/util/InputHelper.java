package org.institution.app.util;

import java.util.Scanner;

public class InputHelper {
    public InputHelper() {}

    public int inputInteger(Scanner sc, String placeholder, boolean skippable) {
        while (true) {
            if (skippable) {
                System.out.print("[SKIPPABLE] ");
            }

            System.out.print(placeholder);

            String line = sc.nextLine();

            if (line.isBlank()) {
                if (skippable) {
                    return -1;
                } else {
                    System.out.print("Error: INVALID FORMAT\n");
                }
                continue;
            }

            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.print("Error: INVALID FORMAT\n");
            }
        }
    }

    public double inputDouble(Scanner sc, String placeholder) {
        while (true) {
            System.out.print("[DOUBLE]" + placeholder);

            String line = sc.nextLine();

            if (line.isBlank()) {
                System.out.print("Error: INVALID FORMAT\n");
            }

            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.print("Error: INVALID FORMAT\n");
            }
        }
    }

    public String inputString(Scanner sc, String placeholder, boolean skippable) {
        while (true) {
            if (skippable) {
                System.out.print("[SKIPPABLE] ");
            }
            System.out.print(placeholder);

            String line = sc.nextLine();

            if (line.isBlank()) {
                if (skippable) {
                    return null;
                } else {
                    System.out.print("ERROR: INVALID FORMAT\n");
                }
            } else if (!Character.isLetter(line.charAt(0))) {
                System.out.print("ERROR: MAY BEGIN WITH A LETTER\n");
            } else {
                return line;
            }

        }
    }

    public boolean inputBoolean(Scanner sc, String placeholder) {
        while (true) {
            System.out.print(placeholder);

            String line = sc.nextLine();

            if (line.isBlank()) {
                System.out.print("Error: INVALID FORMAT\n");
                continue;
            }

            if (line.length() > 1) {
                System.out.print("Error: INVALID FORMAT\n");
                continue;
            } else if (!Character.isLetter(line.charAt(0))) {
                System.out.print("Error: INVALID FORMAT\n");
                continue;
            }

            char c = line.charAt(0);

            if (c == 'y' || c == 'Y') {
                return true;
            } else if (c == 'n' || c == 'N') {
                return false;
            } else {
                System.out.print("Invalid Option!\n");
            }
        }
    }    

}
