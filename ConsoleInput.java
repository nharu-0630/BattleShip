import java.util.Arrays;
import java.util.Scanner;

public class ConsoleInput {
    public static Scanner scanner = new Scanner(System.in);

    public static String InputKeysValues(String[] inputKeys, String[] inputValues, String confirmText) {
        String tempLine = "";
        int index;
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
        int index;
        CONFIRM: while (true) {
            System.out.print(ConsoleColors.YELLOW);
            for (int i = 0; i < inputKeys.length; i++) {
                System.out.print(inputKeys[i]);
                System.out.print("(" + (i) + ")");
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
        return index;
    }

    public static Point InputPoint(String confirmText) {
        Point point;
        CONFIRM: while (true) {
            System.out.print(ConsoleColors.YELLOW);
            System.out.print("ポイント: ");
            System.out.print(ConsoleColors.RESET);
            point = new Point(scanner.nextLine());
            if (!point.empty && point.IsRange()) {
                System.out.println(confirmText + " = " + point.toPointFormatString());
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
        return point;
    }

    public static Point InputVector(String confirmText) {
        Point point;
        CONFIRM: while (true) {
            System.out.print(ConsoleColors.YELLOW);
            System.out.print("ベクトル: ");
            System.out.print(ConsoleColors.RESET);
            point = new Point(scanner.nextLine());
            if (!point.empty
                    && (point.x == 0 || point.y == 0) && ((Math.abs(point.x) + Math.abs(point.y)) == 1
                            || (Math.abs(point.x) + Math.abs(point.y)) == 2)) {
                System.out.println(confirmText + " = " + point.toVectorFormaString());
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
        return point;
    }

    public static int InputInt(String confirmText, int min, int max) {
        String tempLine = "";
        int value;
        CONFIRM: while (true) {
            System.out.print(ConsoleColors.YELLOW);
            System.out.print(confirmText + ": ");
            System.out.print(ConsoleColors.RESET);
            tempLine = scanner.nextLine();
            if (tempLine.chars().allMatch(Character::isDigit)) {
                value = Integer.parseInt(tempLine);
                if (min <= value && value <= max) {
                    System.out.println(confirmText + " = " + value);
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
        return value;
    }
}
