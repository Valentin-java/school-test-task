import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final String INSERT_REQUEST = "INSERT VALUES ";
    private static final String UPDATE_REQUEST = "UPDATE";
    private static final String DELETE_REQUEST = "DELETE";
    private static final String SELECT_REQUEST = "SELECT";

    private static final String ID = "id";
    private static final String LAST_NAME = "lastName";
    private static final String AGE = "age";
    private static final String COST = "cost";
    private static final String ACTIVE = "active";


    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in); // создание объекта Scanner для чтения ввода с консоли

        System.out.print("Введите текст: ");
        String text = scanner.nextLine(); // чтение введенной строки
        scanner.close();


//        FileStorageUtil.deleteLineByNumber(Integer.parseInt(text));
//        FileStorageUtil.saveFile(text);
//        int a = FileStorageUtil.findLineNumberByStart(text);
//        System.out.println("Номер строки: " + a);

        System.out.println("Вы ввели: " + text);
        /**
         * Делаем проверку строки text.
         * Если вводимые команды начинаются верно.
         * Производим необходимые действия.
         */
        if (text.startsWith(INSERT_REQUEST)) {

            // удаляем ключ "INSERT VALUES"
            text = text.replace("INSERT VALUES ", "");

            // Парсим строку
            Map<String, String> parsed = parseInsertString(text);

            // Проверим что id != null, иначе прервем наши действия
            if (!parsed.containsKey(ID) && parsed.get(ID) == null) {
                System.out.println(ID + " " + "Не может быть NULL");
                return;
            }

            // Записываем данные в сторедж.
            // Сначала готовим строку, под какую-нибудь придуманную структуру данных
            StringBuilder sb = new StringBuilder();
            sb.append(ID);
            sb.append(":");
            sb.append(parsed.containsKey(ID) ? parsed.get(ID) : null);
            sb.append(",");
            sb.append(LAST_NAME);
            sb.append(":");
            sb.append(parsed.containsKey(LAST_NAME) ? parsed.get(LAST_NAME) : null);
            sb.append(",");
            sb.append(AGE);
            sb.append(":");
            sb.append(parsed.containsKey(AGE) ? parsed.get(AGE) : null);
            sb.append(",");
            sb.append(COST);
            sb.append(":");
            sb.append(parsed.containsKey(COST) ? parsed.get(COST) : null);
            sb.append(",");
            sb.append(ACTIVE);
            sb.append(":");
            sb.append(parsed.containsKey(ACTIVE) ? parsed.get(ACTIVE) : null);
            sb.append(";");

            // Можно конечно сделать предварительно поиск на уникальность ID
            int checkUniq = FileStorageUtil.findLineNumberByStart(ID + ":" + parsed.get(ID));
            if (checkUniq != -1) {
                System.out.println("Запись с таким id: " + parsed.get(ID) + " уже существует. id должен быть уникальным.");
                return;
            }

            // Сохраняем в нашей БД(в текстовом файле). Пока не делаем проверку на успех записи
            // Но сообщение выведем что бы понимать что мы успешно прошли этот сценарий.
            FileStorageUtil.saveFile(sb.toString());
            System.out.println("Запись внесена успешно.");

            // Нам по заданию нужно вывести весь список включая новую запись
            // На выход из метода: List<Map<String,Object>> в которой содержится row3 (со всеми колонками, в которых есть значение).
            // Пока ничего не будем возвращать! На доработке.
            return;
        }
        if (text.startsWith(UPDATE_REQUEST)) {

            return;
        }
        if (text.startsWith(DELETE_REQUEST)) {

            return;
        }
        if (text.startsWith(SELECT_REQUEST)) {

            return;
        }

        System.out.println("Команда не поддерживается, проверьте корректность ввода. - " + text);





    }

    public static Map<String, String> parseInsertString(String insertString) {
        Map<String, String> params = new HashMap<>();

        String[] parts = insertString.split(","); // разделение строки на части по запятой
        for (String part : parts) {
            String[] keyValue = part.split("="); // разделение каждой части на ключ и значение по знаку равенства
            String key = keyValue[0].trim().replaceAll("[‘’]", ""); // удаляем пробелы и одинарные кавычки в начале и конце ключа
            String value = keyValue[1].trim().replaceAll("[‘’]", ""); // удаляем пробелы и одинарные кавычки в начале и конце значения

            params.put(key, value); // добавляем пару ключ-значение в карту
        }

        return params;
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