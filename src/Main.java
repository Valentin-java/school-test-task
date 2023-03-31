import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final String INSERT_REQUEST = "INSERT VALUES ";
    private static final String UPDATE_REQUEST = "UPDATE VALUES ";
    private static final String DELETE_REQUEST = "DELETE WHERE ";
    private static final String SELECT_REQUEST = "SELECT WHERE ";

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

        System.out.println("Вы ввели: " + text);

        /**
         * Делаем проверку строки text.
         * Если вводимые команды начинаются верно.
         * Производим необходимые действия.
         */

        if (text.startsWith(INSERT_REQUEST)) {
            staticinsertDataTable(text);
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

    /**
     * Выведет список всех записей.
     */
    private static void showLists() {
        List<Map<String, String>> data = FileStorageUtil.getList() ;
        for (Map<String, String> row : data) {
            for (Map.Entry<String, String> entry : row.entrySet()) {
                System.out.print(entry.getKey() + ": " + entry.getValue() + ", ");
            }
            System.out.println();
        }
    }

    /**
     * Создаст строку по условному стандарту записи в наш локальный файл(БД)
     * @param parsed
     * @return
     */
    private static String getLogString(Map<String, String> parsed) {
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

        return sb.toString();
    }

    /**
     * Парсим строку в мапу, что бы потом легче было разбирать по ключам и создавать запись перед сохранением в БД.
     * @param insertString
     * @return
     */
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

    /**
     * Сценарий внесения записи в БД
     * @param request
     * @return
     */
    private static void staticinsertDataTable(String request) throws IOException {
// удаляем ключ "INSERT VALUES"
        request = request.replace("INSERT VALUES ", "");

        // Парсим строку
        Map<String, String> parsed = parseInsertString(request);

        // Проверим что id != null, иначе прервем наши действия
        if (!parsed.containsKey(ID) && parsed.get(ID) == null) {
            System.out.println(ID + " " + "Не может быть NULL");
            return;
        }

        // Записываем данные в сторедж.
        // Сначала готовим строку, под какую-нибудь придуманную структуру данных
        String logString = getLogString(parsed);

        // Можно конечно сделать предварительно поиск на уникальность ID
        int checkUniq = FileStorageUtil.findLineNumberByStart(ID + ":" + parsed.get(ID));
        if (checkUniq != -1) {
            System.out.println("Запись с таким id: " + parsed.get(ID) + " уже существует. id должен быть уникальным.");
            return;
        }

        // Сохраняем в нашей БД(в текстовом файле). Пока не делаем проверку на успех записи
        // Но сообщение выведем что бы понимать что мы успешно прошли этот сценарий.
        FileStorageUtil.saveFile(logString);

        // Нам по заданию нужно вывести весь список включая новую запись
        // На выход из метода: List<Map<String,Object>>
        // в которой содержится row3 (со всеми колонками, в которых есть значение).
        showLists();
    }

    private static void updateDataTable(String request) {

    }

    private static void deleteDataTable(String request) {

    }

    private static void selectDataTable(String request) {

    }
}