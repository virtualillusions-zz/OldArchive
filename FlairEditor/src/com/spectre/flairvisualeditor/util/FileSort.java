package com.spectre.flairvisualeditor.util;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Elmar Baumann
 */
public final class FileSort {

    public static void sortFilesNamesAscendingIgnoreCase(List<? extends File> files) {
        Collections.sort(files, SortComparator);
    }
    public static Comparator<File> SortComparator = new Comparator<File>() {

        @Override
        public int compare(File file1, File file2) {
            boolean isDir1 = file1.isDirectory();
            boolean isDir2 = file2.isDirectory();

            if (isDir1 && !isDir2) {
                return -1;
            } else if (!isDir1 && isDir2) {
                return 1;
            } else {

                String name1 = file1.getName();
                String name2 = file2.getName();

                return name1.compareToIgnoreCase(name2);
            }
        }
    };

    private FileSort() {
    }
}
