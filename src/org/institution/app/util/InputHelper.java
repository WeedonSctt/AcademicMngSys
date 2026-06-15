package org.institution.app.util;

import java.util.Scanner;

public class InputHelper {
    public InputHelper() {}

    public int inputInteger(Scanner sc, String placeholder) {
        while (true) {
            System.out.print(placeholder);

            String line = sc.nextLine();

            if (line.isBlank()) {
                System.out.println("Error: INVALID FORMAT");
                continue;
            }

            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Error: INVALID FORMAT");
            }
        }
    }

    public double inputDouble(Scanner sc, String placeholder) {
        while (true) {
            System.out.print("[DOUBLE]" + placeholder);

            String line = sc.nextLine();

            if (line.isBlank()) {
                System.out.println("Error: INVALID FORMAT");
            }

            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("Error: INVALID FORMAT");
            }
        }
    }

    public String inputString(Scanner sc, String placeholder) {
        while (true) {
            System.out.print(placeholder);

            String line = sc.nextLine();

            if (line.isBlank()) {
                System.out.println("ERROR: INVALID FORMAT");
                continue;
            } else if (!Character.isLetter(line.charAt(0))) {
                System.out.println("ERROR: MAY BEGIN WITH A LETTER");
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
                System.out.println("Error: INVALID FORMAT");
                continue;
            }

            if (line.length() > 1) {
                System.out.print("Error: INVALID FORMAT");
                continue;
            } else if (!Character.isLetter(line.charAt(0))) {
                System.out.print("Error: INVALID FORMAT");
                continue;
            }

            char c = line.charAt(0);

            if (c == 'y' || c == 'Y') {
                return true;
            } else if (c == 'n' || c == 'N') {
                return false;
            } else {
                System.out.println("Invalid Option!");
            }
        }
    }    

}
