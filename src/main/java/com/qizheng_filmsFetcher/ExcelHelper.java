package com.qizheng_filmsFetcher;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelHelper {

    // Method to write movie data to an Excel file
    public static void writeMoviesToExcel(List<Movie> movies, String fileName) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Movies");

        int rowNum = 0;
        // Creating header row
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("Title");
        headerRow.createCell(1).setCellValue("Genre");
        headerRow.createCell(2).setCellValue("Release Date");
        headerRow.createCell(3).setCellValue("Release Year");
        headerRow.createCell(4).setCellValue("Duration");
        headerRow.createCell(5).setCellValue("Language");
        headerRow.createCell(6).setCellValue("Country");
        headerRow.createCell(7).setCellValue("Director");
        headerRow.createCell(8).setCellValue("Rating");
        headerRow.createCell(9).setCellValue("Description");

        // Adding movie data to the sheet
        for (Movie movie : movies) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(movie.getTitle());
            row.createCell(1).setCellValue(movie.getGenre());
            row.createCell(2).setCellValue(movie.getReleaseDate());
            row.createCell(3).setCellValue(movie.getReleaseYear());
            row.createCell(4).setCellValue(movie.getDuration());
            row.createCell(5).setCellValue(movie.getLanguage());
            row.createCell(6).setCellValue(movie.getCountry());
            row.createCell(7).setCellValue(movie.getDirector());
            row.createCell(8).setCellValue(movie.getRating());
            row.createCell(9).setCellValue(movie.getDescription());
        }

        // Writing the workbook to a file
        try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
            workbook.write(fileOut);
        }

        // Closing the workbook
        workbook.close();
    }
}
