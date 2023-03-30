import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in); // создание объекта Scanner для чтения ввода с консоли

        System.out.print("Введите текст: ");
        String text = scanner.nextLine(); // чтение введенной строки


//        FileStorageUtil.deleteLineByNumber(Integer.parseInt(text));
//        FileStorageUtil.saveFile(text);
        int a = FileStorageUtil.findLineNumberByStart(text);
        System.out.println("Номер строки: " + a);

        System.out.println("Вы ввели: " + text);

        scanner.close();

    }

    private static List<Map<String, Object>> insertDataTable(String comand) {

        return null;
    }

    private static List<Map<String, Object>> updateDataTable(String comand) {

        return null;
    }

    private static List<Map<String, Object>> deleteDataTable(String comand) {

        return null;
    }

    private static List<Map<String, Object>> selectDataTable(String comand) {

        return null;
    }
}