import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileStorageUtil {

    private static final String DIR_STORAGE = "C:/temp";
    private static final String FILE_STORAGE = "storage.txt";


    /**
     * Последовательное сохранение строк(записей) в текстовый файл.
     * @param data
     * @throws IOException
     */
    public static void saveFile(String data) throws IOException {
        File dirStorage = new File(DIR_STORAGE);
        File archiveFile = new File(dirStorage, FILE_STORAGE);
        // Создаем директорию если ее нет
        if (!dirStorage.exists()) {
            dirStorage.mkdir();
        }
        // создаем файл для хранения данных, если его нет
        if (!archiveFile.exists()) {
            archiveFile.createNewFile();
        }

        // создание объекта FileWriter для записи в файл
        try (FileWriter writer = new FileWriter(archiveFile,true)) {
            writer.write(data + System.lineSeparator()); // запись данных в файл
        }
    }

    /**
     * Удаление строки по ее номеру. (нумерация строки -1)
     * @param lineNumber
     */
    public static void deleteLineByNumber(int lineNumber) {
        List<String> lines = new ArrayList<>();
        File dirStorage = new File(DIR_STORAGE);
        File archiveFile = new File(dirStorage, FILE_STORAGE);

        // Вычитываем данные в коллекцию и удаляем не нужные
        try(BufferedReader reader = new BufferedReader(new FileReader(archiveFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line); // добавление строки в список
            }
            if (lineNumber >= 0 && lineNumber < lines.size()) {
                lines.remove(lineNumber); // удаление нужной строки из списка
            } else {
                System.out.println("Строка для удаления не найдена.");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Запишем теперь новые данные
        try (FileWriter writer = new FileWriter(archiveFile, false)) {
            for (String newLine : lines) {
                writer.write(newLine + System.lineSeparator()); // запись строки с символом перевода строки
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Поиск строки по началу записи в тексте. Например: Строка начинающаяся с id:222, в результате выдаст номер строки.
     * Который можно использовать для удаления.
     * @param startName
     * @return
     */
    public static int findLineNumberByStart(String startName) {
        File dirStorage = new File(DIR_STORAGE);
        File archiveFile = new File(dirStorage, FILE_STORAGE);

        int lineNumber = -1; // номер строки, по умолчанию -1 (не найдено)

        try (BufferedReader reader = new BufferedReader(new FileReader(archiveFile))) {
            String line;
            int currentLine = 0;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(startName)) { // проверка на соответствие начального названия
                    lineNumber = currentLine; // запоминаем номер строки
                    break;
                }
                currentLine++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lineNumber;
    }

    /**
     * Метод обновляет определенную строку, меняет данные на новые.
     * Т.е. старые нужно изначально изъять, затем изменить и измененные данные записать.
     * @param lineNumber Номер строки, которую надо изменить
     * @param newData Новые данные строки которые надо изменить.
     */
    public static void updateLineByNumber(int lineNumber, String newData) {
        List<String> lines = new ArrayList<>();
        File dirStorage = new File(DIR_STORAGE);
        File archiveFile = new File(dirStorage, FILE_STORAGE);

        try (BufferedReader reader = new BufferedReader(new FileReader(archiveFile))) {
            String line;
            int currentLine = 0;
            while ((line = reader.readLine()) != null) {
                if (currentLine == lineNumber) { // изменяем данные в нужной строке
                    lines.add(newData); // добавляем новые данные в список
                } else {
                    lines.add(line); // добавляем старые данные в список
                }
                currentLine++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileWriter writer = new FileWriter(archiveFile, false)) {

            for (String newLine : lines) {
                writer.write(newLine + System.lineSeparator()); // записываем измененные данные в файл
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод отдает строку по номеру.
     * @param lineNumber Номер строки
     * @return
     */
    public static String getLineByNumber(int lineNumber) {
        File dirStorage = new File(DIR_STORAGE);
        File archiveFile = new File(dirStorage, FILE_STORAGE);
        String line = null; // строка, по умолчанию null (не найдено)

        try (BufferedReader reader = new BufferedReader(new FileReader(archiveFile))) {
            String currentLine;
            int currentLineNumber = 0;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLineNumber == lineNumber) {
                    line = currentLine; // запоминаем нужную строку
                    break;
                }
                currentLineNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return line;
    }
}
