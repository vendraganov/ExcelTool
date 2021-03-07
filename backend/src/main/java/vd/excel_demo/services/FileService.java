package vd.excel_demo.services;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vd.excel_demo.models.Student;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class FileService {

    private static final String FAIL_TO_PARSE_EXCEL_FILE = "Fail to parse Excel file: ";
    private static final String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final String SHEET_NAME = "Students";
    private static final String STUDENT_ID = "Student ID";
    private static final String FIRST_NAME = "First Name";
    private static final String LAST_NAME = "Last Name";
    private static final String GENDER = "Gender";
    private static final String DOB = "DOB";

    private static final String[] COLUMNS = {STUDENT_ID, FIRST_NAME, LAST_NAME, GENDER, DOB};

    private final StudentService studentService;

    @Autowired
    public FileService(StudentService studentService) {
        this.studentService = studentService;
    }

    public void uploadFile(MultipartFile file) {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            for (int index = 0; index < workbook.getNumberOfSheets(); index++) {

                Sheet sheet = workbook.getSheetAt(index);

                Iterator<Row> rows = sheet.iterator();

                List<Student> students = new ArrayList<>();

                int rowNumber = 0;
                while (rows.hasNext()) {
                    Row row = rows.next();

                    if (rowNumber == 0) {
                        rowNumber++;
                        continue;
                    }

                    students.add(this.createStudent(row));
                }
                this.studentService.saveStudents(students);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(FAIL_TO_PARSE_EXCEL_FILE + e.getMessage());
        }
    }

    public boolean isExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public Resource downloadFile() throws IOException {

        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet(SHEET_NAME);

            XSSFFont headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLUE.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            this.createHeaderRow(sheet, headerCellStyle);
            this.createRows(sheet);

            workbook.write(byteArrayOutputStream);
            return new ByteArrayResource(byteArrayOutputStream.toByteArray());
        }
    }

    private void createHeaderRow(Sheet sheet, CellStyle headerCellStyle) {
        Row headerRow = sheet.createRow(0);
        for (int col = 0; col < COLUMNS.length; col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(COLUMNS[col]);
            cell.setCellStyle(headerCellStyle);
        }
    }

    private void createRows(Sheet sheet) {
        int rowIndex = 1;
        List<Student> students = this.studentService.getStudents();
        for (Student student : students) {
            Row row = sheet.createRow(rowIndex++);
            for (int col = 0; col < COLUMNS.length; col++) {
              this.createCell(row, student, col, COLUMNS[col]);
            }
        }
    }

    private void createCell(Row row, Student student, int col, String value) {
        switch (value) {
            case STUDENT_ID: {
                row.createCell(col).setCellValue(student.getStudentId());
                break;
            }
            case FIRST_NAME: {
                row.createCell(col).setCellValue(student.getFirstName());
                break;
            }
            case LAST_NAME: {
                row.createCell(col).setCellValue(student.getLastName());
                break;
            }
            case GENDER: {
                row.createCell(col).setCellValue(student.getGender());
                break;
            }
            case DOB: {
                row.createCell(col).setCellValue(student.getDob());
                break;
            }
            default: {
                break;
            }
        }
    }

    private Student createStudent(Row row) {
        Iterator<Cell> cellsInRow = row.iterator();

        Student student = new Student();

        int cellIndex = 0;
        while (cellsInRow.hasNext()) {
            Cell currentCell = cellsInRow.next();
            switch (cellIndex) {
                case 1: {
                    student.setFirstName(currentCell.getStringCellValue());
                    break;
                }
                case 2: {
                    student.setLastName(currentCell.getStringCellValue());
                    break;
                }
                case 3: {
                    student.setGender(currentCell.getStringCellValue());
                    break;
                }
                case 4: {
                    student.setDob(currentCell.getLocalDateTimeCellValue());
                    break;
                }
                default: {
                    break;
                }
            }
            cellIndex++;
        }
        return student;
    }
}
