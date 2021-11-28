package de.darthweiter.banplugin.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class FileIO {

    private static final String WORKING_DIR = System.getProperty("user.dir");

    private FileIO() {
    }

    /**
     * Creates the File and Directory
     *
     * @param directory the Directory to create
     * @param fileName  the File to create
     * @param data      the Data of the File.
     * @return true if file was created, otherwise false.
     */
    public static boolean createFile(File directory, String fileName, Map<String, String> data) {

        String filePath;

        if (!directory.exists()) {
            if (!directory.mkdir()) {
                return false;
            } else {
                filePath = directory.getAbsolutePath() + File.separator + fileName;
            }
        } else {
            filePath = directory.getAbsolutePath() + File.separator + fileName;
        }

        File file = new File(filePath);
        return fileWrite(file, data);
    }

    /**
     * Write Data to a File
     *
     * @param file        the File to write Data to.
     * @param mapWithData The Map with Data.
     * @return true if Data was written to the file, otherwise false.
     */
    private static boolean fileWrite(File file, Map<String, String> mapWithData) {
        try (FileWriter fw = new FileWriter(file.getAbsoluteFile());
             BufferedWriter bw = new BufferedWriter(fw)) {
            StringBuilder output = new StringBuilder();
            for (Map.Entry<String, String> entrySet : mapWithData.entrySet()) {
                output.append(entrySet.getKey()).append("=").append(entrySet.getValue());
                bw.write(output.toString());
                bw.newLine();
                output = new StringBuilder();
            }
            return true;
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return false;
        }
    }

    /**
     * Load the Directory with the given name.
     *
     * @param directoryName - The Name of the Directory.
     * @return the Directory or null if not found.
     */
    public static File loadDirectory(String directoryName) {
        String directoryPath = WORKING_DIR + File.separator + directoryName;
        if (createDirectory(directoryPath)) {
            return new File(directoryPath);
        } else {
            return null;
        }
    }

    /**
     * Creat the Directory with the directory Path
     *
     * @param directoryPath the Path of the directory
     * @return true if directory exist otherwise false.
     */
    private static boolean createDirectory(String directoryPath) {
        File directory = new File(directoryPath);

        if (!directory.exists()) {
            return directory.mkdir();
        } else {
            return true;
        }
    }
}
