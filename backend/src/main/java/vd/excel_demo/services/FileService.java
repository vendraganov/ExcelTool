package vd.excel_demo.services;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vd.excel_demo.models.Student;
import vd.excel_demo.utils.Constants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class FileService {

    private static final String SHEET_NAME = "Students";
    private static final String STUDENT_ID = "STUDENT ID";
    private static final String FIRST_NAME = "FIRST NAME";
    private static final String LAST_NAME = "LAST NAME";
    private static final String GENDER = "GENDER";
    private static final String DOB = "DOB";

    private static final String COLUMNS_HEADERS_DO_NOT_MATCH = "Columns headers do not match!";
    private static final String FAIL_TO_PARSE_EXCEL_FILE = "Fail to parse Excel file: ";
    private static final String DATE_PATTERN = "mm/dd/yyyy";

    private final List<String> columnsNames = new ArrayList<>(Arrays.asList(STUDENT_ID, FIRST_NAME, LAST_NAME, GENDER, DOB));

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
                List<String> headers = new ArrayList<>();
                while (rows.hasNext()) {
                    Row row = rows.next();
                    if (rowNumber == 0) {
                        this.validateHeaderNames(row, headers);
                        rowNumber++;
                        continue;
                    }

                    students.add(this.createStudent(row, headers));
                }
                this.studentService.saveStudents(students);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(FAIL_TO_PARSE_EXCEL_FILE + e.getMessage());
        }
    }

    public boolean isExcelFormat(MultipartFile file) {
        return Constants.TYPE.equals(file.getContentType());
    }

    public Resource downloadFile() throws IOException {

        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet(SHEET_NAME);

            XSSFFont headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLACK.getIndex());

            XSSFCreationHelper createHelper = workbook.getCreationHelper();
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            CellStyle dobCellStyle = workbook.createCellStyle();
            dobCellStyle.setDataFormat(createHelper.createDataFormat().getFormat(DATE_PATTERN));

            this.createHeaderRow(sheet, headerCellStyle);
            this.createRows(sheet, dobCellStyle);

            workbook.write(byteArrayOutputStream);
            return new ByteArrayResource(byteArrayOutputStream.toByteArray());
        }
    }

    private void createHeaderRow(Sheet sheet, CellStyle cellStyle) {
        Row headerRow = sheet.createRow(0);
        for (int col = 0; col < columnsNames.size(); col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(columnsNames.get(col));
            cell.setCellStyle(cellStyle);
        }
    }

    private void createRows(Sheet sheet, CellStyle cellStyle) {
        int rowIndex = 1;
        List<Student> students = this.studentService.getStudents();
        for (Student student : students) {
            Row row = sheet.createRow(rowIndex++);
            for (int col = 0; col < columnsNames.size(); col++) {
              this.addData(row, cellStyle, student, col, columnsNames.get(col), false);
            }
        }
    }

    private Student createStudent(Row row, List<String> headers) {
        Student student = new Student();
        for (int col = 0; col < headers.size(); col++) {
            this.addData(row, null, student, col, headers.get(col), true);
        }
        return student;
    }

    private void addData(Row row, CellStyle cellStyle, Student student, int col, String value, boolean upload) {
        switch (value) {
            case STUDENT_ID: {
                row.createCell(col).setCellValue(student.getStudentId());
                break;
            }
            case FIRST_NAME: {
                if (upload) {
                    student.setFirstName(row.getCell(col).getStringCellValue());
                }
                else {
                    row.createCell(col).setCellValue(student.getFirstName());
                }
                break;
            }
            case LAST_NAME: {
                if (upload) {
                    student.setLastName(row.getCell(col).getStringCellValue());
                }
                else {
                    row.createCell(col).setCellValue(student.getLastName());
                }
                break;
            }
            case GENDER: {
                if (upload) {
                    student.setGender(row.getCell(col).getStringCellValue());
                }
                else {
                    row.createCell(col).setCellValue(student.getGender());
                }
                break;
            }
            case DOB: {
                if (upload) {
                    student.setDob(row.getCell(col).getLocalDateTimeCellValue());
                }
                else{
                    Cell cell = row.createCell(col);
                    cell.setCellValue(student.getDob());
                    cell.setCellStyle(cellStyle);
                }
                break;
            }
            default: {
                break;
            }
        }
    }

    private void validateHeaderNames( Row row, List<String> headers) {
        row.iterator().forEachRemaining(cell -> {
            if (columnsNames.stream().anyMatch(col -> col.equalsIgnoreCase(cell.getStringCellValue()))) {
                headers.add(cell.getStringCellValue().toUpperCase());
            }
        });
        if (headers.size() != columnsNames.size() && !headers.containsAll(columnsNames)) {
            throw new IllegalArgumentException(COLUMNS_HEADERS_DO_NOT_MATCH);
        }
    }
}
