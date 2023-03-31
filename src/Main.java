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
            insertDataTable(text);
            return;

        }
        if (text.startsWith(UPDATE_REQUEST)) {
            updateDataTable(text);
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
     * Сценарий внесения записи в БД
     *
     * @param request
     * @return
     */
    private static void insertDataTable(String request) throws IOException {
        // удаляем команду "INSERT VALUES", оно нам больше не нужно
        request = request.replace(INSERT_REQUEST, "");

        // Парсим строку
        Map<String, String> parsed = parseInsertString(request);

        // Проверим что id != null, иначе прервем наши действия
        if (!parsed.containsKey(ID) && parsed.get(ID) == null) {
            System.out.println(ID + " " + "Не может быть NULL");
            return;
        }

        // Сначала готовим строку, под какую-нибудь придуманную структуру данных
        String logString = getLogString(parsed);

        // Предварительно проверим на уникальность ID. Что запись с таким ID не существует.
        int checkUniq = FileStorageUtil.findLineNumberByStart(ID + ":" + parsed.get(ID));
        if (checkUniq != -1) {
            System.out.println("Запись с таким id: " + parsed.get(ID) + " уже существует. id должен быть уникальным.");
            return;
        }

        // Сохраняем в нашей БД(в текстовом файле)
        FileStorageUtil.saveFile(logString);

        // Нам по заданию нужно вывести весь список включая новую запись
        showLists();
    }

    /**
     * Сценарий обновления записи в БД
     * Первый сценарий обновление по id
     * UPDATE VALUES ‘active’=false, ‘cost’=10.1 where ‘id’=3
     *
     * Второй сценарий обновление списка записей
     * UPDATE VALUES ‘active’=true  where ‘active’=false
     *
     * @param request
     */
    private static void updateDataTable(String request) {
        // удаляем из команды "UPDATE VALUES ", оно нам больше не нужно
        request = request.replace(UPDATE_REQUEST, "");

        if (!request.contains("where")) {
            System.out.println("Не верный формат запроса. Не найден фильтр - where");
            return;
        }

        // достаем фильтр where - если его нет прерываем работу метода, так как нет фильтра
        String filter = request.split("where")[1].trim();
        if (filter.isEmpty()) {
            System.out.println("Не верный формат запроса. Не найден оператор фильтра - where");
            return;
        }

        // Разделим элемент фильтра на ключ и значение, теперь мы знаем по какому ключу и что менять
        var filterArr = filter.replaceAll("[‘’]", "").split("=");

        // Первый сценарий изменения записи по id.
        // Если ключ - первый элемент filterArr == id
        if (filterArr[0].equals(ID)) {
            // То ищем определенную запись в БД по id.
            int row = FileStorageUtil.findLineNumberByStart(ID + ":" + filterArr[1]);

            // если не нашли, завершаем работу
            if (row == -1) {
                System.out.println("Запись с таким id:" + filterArr[1] + " не найдена в БД");
                return;
            }
            // Если нашли, делаем изменения

            // Достаем наши параметры, которые надо менять
            String requestParams = request.split("where")[0].trim();
            Map<String, String> params = parseInsertString(requestParams);

            // Достаем строку, затем ее конвертируем в мапу
            String oldRow = FileStorageUtil.getLineByNumber(row);
            var targetMap = FileStorageUtil.createMapFromString(oldRow);

            // Делаем изменения
            targetMap.putAll(params);

            // Записываем в БД

            // Сначала готовим строку, под какую-нибудь придуманную структуру данных
            String logString = getLogString(targetMap);

            // Обновляем старую строку на новую
            FileStorageUtil.updateLineByNumber(row, logString);

            // Завершаем работу
            System.out.println("Запись с id: " + filterArr[1] + " успешно обновлена.");
            return;

        }

        // Второй сценарий изменения записи - придется доставать все записи, делать изменения и заново записывать

        // Достаем список записей

        // Производим там необходимые изменения

        // Обратно записываем этот список в нашу БД

    }


    /**
     * Сценарий удаления записи в БД
     *
     * @param request
     */
    private static void deleteDataTable(String request) {

    }

    /**
     * Сценарий выборки определенной записи в БД
     *
     * @param request
     */
    private static void selectDataTable(String request) {

    }

    /**
     * Выведет список всех записей.
     */
    private static void showLists() {
        List<Map<String, String>> data = FileStorageUtil.getList();
        for (Map<String, String> row : data) {
            for (Map.Entry<String, String> entry : row.entrySet()) {
                System.out.print(entry.getKey() + ": " + entry.getValue() + ", ");
            }
            System.out.println();
        }
    }

    /**
     * Создаст строку по условному стандарту записи в наш локальный файл(БД)
     * Например запись строк будет выглядеть вот так: id:3,lastName:Федоров,age:40,cost:null,active:true;
     *
     * @param parsed мапа с данными
     * @return Готовая для записи в нашу БД строка
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
     *
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

}