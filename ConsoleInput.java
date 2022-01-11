import java.util.Arrays;
import java.util.Scanner;

public class ConsoleInput {
    public static Scanner scanner = new Scanner(System.in);

    public static String InputKeysValues(String[] inputKeys, String[] inputValues, String confirmText) {
        String tempLine = "";
        int index = -1;
        CONFIRM: while (true) {
            System.out.print(ConsoleColors.YELLOW);
            for (int i = 0; i < inputKeys.length; i++) {
                System.out.print(inputKeys[i]);
                System.out.print("(" + inputValues[i] + ")");
                if (i < inputKeys.length - 1) {
                    System.out.print(", ");
                } else {
                    System.out.print(": ");
                }
            }
            System.out.print(ConsoleColors.RESET);
            tempLine = scanner.nextLine();
            index = Arrays.asList(inputValues).indexOf(tempLine);
            if (index != -1) {
                System.out.println(confirmText + " = " + inputKeys[index]);
                System.out.print(ConsoleColors.YELLOW);
                System.out.print("確定(y), 取消(n): ");
                System.out.print(ConsoleColors.RESET);
                if (scanner.nextLine().equals("y")) {
                    break CONFIRM;
                } else {
                    System.out.print(ConsoleColors.RED);
                    System.out.println("取消されました");
                    System.out.print(ConsoleColors.RESET);
                    continue CONFIRM;
                }
            }
            System.out.print(ConsoleColors.RED);
            System.out.println("入力が正しくありません");
            System.out.print(ConsoleColors.RESET);
        }
        return inputValues[index];
    }

    public static int InputKeys(String[] inputKeys, String confirmText) {
        String tempLine = "";
        int index = -1;
        CONFIRM: while (true) {
            System.out.print(ConsoleColors.YELLOW);
            for (int i = 0; i < inputKeys.length; i++) {
                System.out.print(inputKeys[i]);
                System.out.print("(" + (i + 1) + ")");
                if (i < inputKeys.length - 1) {
                    System.out.print(", ");
                } else {
                    System.out.print(": ");
                }
            }
            System.out.print(ConsoleColors.RESET);
            tempLine = scanner.nextLine();
            if (tempLine.chars().allMatch(Character::isDigit)) {
                index = Integer.parseInt(tempLine);
                index--;
                if (0 <= index && index < inputKeys.length) {
                    System.out.println(confirmText + " = " + inputKeys[index]);
                    System.out.print(ConsoleColors.YELLOW);
                    System.out.print("確定(y), 取消(n): ");
                    System.out.print(ConsoleColors.RESET);
                    if (scanner.nextLine().equals("y")) {
                        break CONFIRM;
                    } else {
                        System.out.print(ConsoleColors.RED);
                        System.out.println("取消されました");
                        System.out.print(ConsoleColors.RESET);
                        continue CONFIRM;
                    }
                }
            }
            System.out.print(ConsoleColors.RED);
            System.out.println("入力が正しくありません");
            System.out.print(ConsoleColors.RESET);
        }
        return index++;
    }
}
