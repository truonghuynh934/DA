package database;

import javafx.collections.FXCollections;
import model.Student;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

public class HandlerExcelFile {
    // index columns in excel file (start 0)
    public static final int COLUMN_INDEX_ID = 0;
    public static final int COLUMN_INDEX_ID_STUDENT = 1;
    public static final int COLUMN_INDEX_FIRST_NAME = 2;
    public static final int COLUMN_INDEX_LAST_NAME = 3;
    public static final int COLUMN_INDEX_SEX = 4;
    public static final int COLUMN_INDEX_DATE_OF_BIRTH = 5;
    public static final int COLUMN_INDEX_HOMETOWN = 6;
    public static final int COLUMN_INDEX_GPA = 7;
    public static final int COLUMN_INDEX_RANK = 8;
    public static final int COLUMN_INDEX_LICENSE_DATE = 9;
    public static final int COLUMN_INDEX_GRADUATION_YEAR = 10;
    public static final int COLUMN_INDEX_REGISTER_NUMBER = 11;
    public static final int COLUMN_INDEX_MAJOR = 12;
    public static final int COLUMN_INDEX_MODE_OF_STUDY = 13;
    public static final int COLUMN_INDEX_TITLE = 14;
    public static final int COLUMN_INDEX_CLASS = 15;
    public static final int COLUMN_INDEX_NOTE = 16;

    public static final int COLUMN_INDEX_REGISTER_NUMBER_EXPORT = 0;
    public static final int COLUMN_INDEX_FIRST_NAME_EXPORT = 1;
    public static final int COLUMN_INDEX_LAST_NAME_EXPORT = 2;
    public static final int COLUMN_INDEX_HOMETOWN_EXPORT = 3;
    public static final int COLUMN_INDEX_CLASS_EXPORT = 4;
    public static final int COLUMN_INDEX_MODE_OF_STUDY_EXPORT = 5;
    public static final int COLUMN_INDEX_DATE_OF_BIRTH_EXPORT = 6;
    public static final int COLUMN_INDEX_ID_CERTIFICATE_EXPORT = 7;
//    public static final int COLUMN_INDEX_LAW_NUMBER_EXPORT = 8;
    public static final int COLUMN_INDEX_NOTE_EXPORT = 8;

    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public int getIndexRowLast(File excelFile) throws Exception {
        int indexRowLast = 0;

        if (excelFile.length() > 0) {
            // Get file
            InputStream inputStream = new FileInputStream(excelFile);
            // Get workbook
            Workbook workbook = getWorkbook(inputStream, excelFile);
            // Get sheet
            Sheet sheet = workbook.getSheetAt(0);
            indexRowLast = sheet.getLastRowNum();

            workbook.close();
            inputStream.close();
        }

        return indexRowLast;
    }

    public int getIndexRowStudentUpdate(String idStudent, File excelFile) throws Exception {
        // Get file
        InputStream inputStream = new FileInputStream(excelFile);
        // Get workbook
        Workbook workbook = getWorkbook(inputStream, excelFile);
        // Get sheet
        Sheet sheet = workbook.getSheetAt(0);

        // Get all rows in a sheet
        Iterator<Row> iteratorRow = sheet.iterator();
        while (iteratorRow.hasNext()) {
            Row nextRow = iteratorRow.next();
            // ignore 1 row first (row header)
            if (nextRow.getRowNum() == 0) continue;
//            System.out.println("- Đang duyệt dòng thứ: " + nextRow.getRowNum());

            // get all cells in a row
            Iterator<Cell> iteratorCell = nextRow.cellIterator();
            while (iteratorCell.hasNext()) {
                Cell nextCell = iteratorCell.next();
                // ignore all cell, except cell IdStudent
                if (nextCell.getColumnIndex() == 1) {
                    System.out.println("ID Student Update: " + (String) getCellValue(nextCell));
                    if (((String) getCellValue(nextCell)).equals(idStudent)) {
                        return nextCell.getRowIndex();
                    }
                } else {
//                    System.out.println("Ô thứ " + nextCell.getColumnIndex() + " trong hàng " + nextCell.getRowIndex());
                }

            }
        }

        workbook.close();
        inputStream.close();
        return -1;
    }

    /**
     * READ EXCEL
     **/
    public List<Student> readExcel(File excelFile) throws IOException, ParseException {
        List<Student> listStudents = FXCollections.observableArrayList();

        if (excelFile.length() > 0) {
            // Get file
            InputStream inputStream = new FileInputStream(excelFile);
            // Get workbook
            Workbook workbook = getWorkbook(inputStream, excelFile);
            // Get sheet
            Sheet sheet = workbook.getSheetAt(0);

            // Get all rows in a sheet
            Iterator<Row> iteratorRow = sheet.iterator();
            while (iteratorRow.hasNext()) {
                Row nextRow = iteratorRow.next();
                // ignore 1 row first
                if (nextRow.getRowNum() == 0) continue;
//            System.out.println("- Đang duyệt dòng thứ: " + nextRow.getRowNum());

                // get all cells in a row
                Student student = new Student();
                String first_name = "", last_name = "";
                Iterator<Cell> iteratorCell = nextRow.cellIterator();
                while (iteratorCell.hasNext()) {
                    Cell nextCell = iteratorCell.next();
                    // ignore 1 cell first (cell contain STT)
                    if (nextCell.getColumnIndex() == 0) continue;
//                System.out.println("\tĐang đọc ô thứ: " + nextCell.getAddress());
//                System.out.println("\t\tCell type: "+nextCell.getCellType() + ", cell type num: " + nextCell.getCellTypeEnum());

                    Object cellValue = getCellValue(nextCell);
                    // ignore cell null
                    if (cellValue == null) continue;
                    switch (nextCell.getColumnIndex()) {
                        case COLUMN_INDEX_ID:
                            break;
                        case COLUMN_INDEX_ID_STUDENT:
                            student.setIdStudent((String) cellValue);
                            break;
                        case COLUMN_INDEX_FIRST_NAME:
                            first_name = (String) cellValue;
                            student.setName(first_name); // đề phòng case last name null
                            break;
                        case COLUMN_INDEX_LAST_NAME:
                            last_name = (String) cellValue;
                            student.setName(first_name + " " + last_name);
                            break;
                        case COLUMN_INDEX_SEX:
                            student.setSex((String) cellValue);
                            break;
                        case COLUMN_INDEX_DATE_OF_BIRTH:
                            student.setDateOfBirth((String) cellValue);
                            break;
                        case COLUMN_INDEX_HOMETOWN:
                            student.setHometown((String) cellValue);
                            break;
                        case COLUMN_INDEX_GPA:
                            student.setGpa(Float.valueOf(cellValue.toString()));
                            break;
                        case COLUMN_INDEX_RANK:
                            student.setRank((String) cellValue);
                            break;
                        case COLUMN_INDEX_LICENSE_DATE:
                            LocalDate localDate = LocalDate.parse(String.valueOf(cellValue), dateTimeFormatter);
                            student.setLicenseDate(localDate);
                            break;
                        case COLUMN_INDEX_GRADUATION_YEAR:
                            student.setGraduationYear(Float.valueOf(cellValue.toString()).intValue());
                            break;
                        case COLUMN_INDEX_REGISTER_NUMBER:
                            student.setRegisterNumber((String) cellValue);
                            break;
                        case COLUMN_INDEX_MAJOR:
                            student.setMajor((String) cellValue);
                            break;
                        case COLUMN_INDEX_MODE_OF_STUDY:
                            student.setModeOfStudy((String) cellValue);
                            break;
                        case COLUMN_INDEX_TITLE:
                            student.setTitle((String) cellValue);
                            break;
                        case COLUMN_INDEX_CLASS:
                            student.setStrClass((String) cellValue);
                            break;
                        case COLUMN_INDEX_NOTE:
                            student.setNote((String) cellValue);
                            break;
                        default:
                            break;
                    }

                }
//            if (student.getIdStudent() != null) listStudents.add(student);
                if (student.getName() != null) listStudents.add(student); // đề phòng case read from history file
            }

            workbook.close();
            inputStream.close();
        }

        System.out.println("Read done!!!");
        return listStudents;
    }

    private Workbook getWorkbook(InputStream inputStream, File excelFile) throws IOException {
        Workbook workbook = null;
        if (excelFile.getName().endsWith("xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        } else if (excelFile.getName().endsWith("xls")) {
            workbook = new HSSFWorkbook(inputStream);
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }

        return workbook;
    }

    private Object getCellValue(Cell cell) {
        CellType cellType = cell.getCellType();
        Object cellValue = null;
        switch (cellType) {
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case NUMERIC:
                cellValue = cell.getNumericCellValue();
                break;
            case BOOLEAN:
                cellValue = cell.getBooleanCellValue();
                break;
            case FORMULA:
                Workbook workbook = cell.getSheet().getWorkbook();
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                cellValue = evaluator.evaluate(cell).getNumberValue();
                break;
            case _NONE:
            case BLANK:
            case ERROR:
                break;
            default:
                break;
        }
        return cellValue;
    }

    /**
     * WRITE EXCEL
     **/

    public void writeExcel(File excelFile, List<Student> listStudents, int indexRowStart, boolean isNewFile) throws Exception {
        InputStream inputStream = new FileInputStream(excelFile);
        Workbook workbook;

        Sheet sheet;
        if (isNewFile) {
            // Create Workbook
            workbook = getWorkbook(excelFile);
            // Create sheet
            sheet = workbook.createSheet("StudentPrinted"); // Create sheet with sheet name
            // writer header
            writeHeader(sheet, 0);
        } else {
            workbook = getWorkbook(inputStream, excelFile);
            sheet = workbook.getSheetAt(0);
        }

        /** WRITE DATA **/
        for (Student student : listStudents) {
            // Create row
            Row row = sheet.createRow(indexRowStart);
            // Write data on row
            writeRow(student, row, indexRowStart);
            indexRowStart++;
        }
        /****/

        // Auto resize column witdth
        int numberOfColumn = sheet.getRow(0).getPhysicalNumberOfCells();
        for (int columnIndex = 0; columnIndex < numberOfColumn; columnIndex++) {
            sheet.autoSizeColumn(columnIndex);
        }

        // Create file excel
        OutputStream outputStream = new FileOutputStream(excelFile);
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();
        inputStream.close();

        System.out.println("Write Done!!!");
    }

    // Create workbook
    private static Workbook getWorkbook(File excelFile) throws IOException {
        Workbook workbook = null;

        if (excelFile.getName().endsWith("xlsx")) {
            workbook = new XSSFWorkbook();
        } else if (excelFile.getName().endsWith("xls")) {
            workbook = new HSSFWorkbook();
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }

        return workbook;
    }

    // Write header with format
    private static void writeHeader(Sheet sheet, int indexRowHeader) throws Exception {
        /** create CellStyle for header **/
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 18); // font size
        font.setColor(IndexedColors.BLACK.getIndex()); // text color

        // Create CellStyle fad73f
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        /****/

        // Create row
        Row row = sheet.createRow(indexRowHeader);

        // Create cells
        Cell cell = row.createCell(COLUMN_INDEX_ID);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("STT");

        cell = row.createCell(COLUMN_INDEX_ID_STUDENT);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Mã sinh viên");

        cell = row.createCell(COLUMN_INDEX_FIRST_NAME);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Họ");

        cell = row.createCell(COLUMN_INDEX_LAST_NAME);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Tên");

        cell = row.createCell(COLUMN_INDEX_SEX);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Giới tính");

        cell = row.createCell(COLUMN_INDEX_DATE_OF_BIRTH);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Ngày sinh");

        cell = row.createCell(COLUMN_INDEX_HOMETOWN);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Quê quán");

        cell = row.createCell(COLUMN_INDEX_GPA);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("ĐTBC");

        cell = row.createCell(COLUMN_INDEX_RANK);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Xếp hạng");

        cell = row.createCell(COLUMN_INDEX_LICENSE_DATE);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Ngày cấp");

        cell = row.createCell(COLUMN_INDEX_GRADUATION_YEAR);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Năm tốt nghiệp");

        cell = row.createCell(COLUMN_INDEX_REGISTER_NUMBER);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Số vào sổ cấp");

        cell = row.createCell(COLUMN_INDEX_MAJOR);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Ngành đào tạo");

        cell = row.createCell(COLUMN_INDEX_MODE_OF_STUDY);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Hình thức đào tạo");

        cell = row.createCell(COLUMN_INDEX_TITLE);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Danh hiệu");

        cell = row.createCell(COLUMN_INDEX_CLASS);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Khóa");

        cell = row.createCell(COLUMN_INDEX_NOTE);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Ghi chú");
    }

    public void writeRow(Student student, Row row, int id) {
        String[] name = student.getName().split(" ");
        String firstName = "", lastName = name[name.length - 1];
        for (int i = 0; i < name.length - 1; i++) {
            firstName = firstName + name[i];
            if (i < (name.length - 2)) {
                firstName = firstName + " ";
            }
        }

        Cell cell = row.createCell(COLUMN_INDEX_ID);
        cell.setCellValue(id);

        cell = row.createCell(COLUMN_INDEX_ID_STUDENT);
        cell.setCellValue(student.getIdStudent());

        cell = row.createCell(COLUMN_INDEX_FIRST_NAME);
        cell.setCellValue(firstName);

        cell = row.createCell(COLUMN_INDEX_LAST_NAME);
        cell.setCellValue(lastName);

        cell = row.createCell(COLUMN_INDEX_SEX);
        cell.setCellValue(student.getSex());

        cell = row.createCell(COLUMN_INDEX_DATE_OF_BIRTH);
        cell.setCellValue(student.getDateOfBirth());

        cell = row.createCell(COLUMN_INDEX_HOMETOWN);
        cell.setCellValue(student.getHometown());

        cell = row.createCell(COLUMN_INDEX_GPA);
        cell.setCellValue(student.getGpa());

        cell = row.createCell(COLUMN_INDEX_RANK);
        cell.setCellValue(student.getRank());

        cell = row.createCell(COLUMN_INDEX_LICENSE_DATE);
        cell.setCellValue(dateTimeFormatter.format(student.getLicenseDate()));

        cell = row.createCell(COLUMN_INDEX_GRADUATION_YEAR);
        cell.setCellValue(student.getGraduationYear());

        cell = row.createCell(COLUMN_INDEX_REGISTER_NUMBER);
        cell.setCellValue(student.getRegisterNumber());

        cell = row.createCell(COLUMN_INDEX_MAJOR);
        cell.setCellValue(student.getMajor());

        cell = row.createCell(COLUMN_INDEX_MODE_OF_STUDY);
        cell.setCellValue(student.getModeOfStudy());

        cell = row.createCell(COLUMN_INDEX_TITLE);
        cell.setCellValue(student.getTitle());

        cell = row.createCell(COLUMN_INDEX_CLASS);
        cell.setCellValue(student.getStrClass());

        cell = row.createCell(COLUMN_INDEX_NOTE);
        cell.setCellValue(student.getNote());
    }

    /****************************************************************************************************************/
    public void writeExcelExport(File excelFile, List<Student> listStudents, int indexRowStart, boolean isNewFile) throws Exception {
        InputStream inputStream = new FileInputStream(excelFile);
        Workbook workbook;

        Sheet sheet;
        if (isNewFile) {
            // Create Workbook
            workbook = getWorkbook(excelFile);
            // Create sheet
            sheet = workbook.createSheet("StudentExported"); // Create sheet with sheet name
            // writer header
            writeHeaderExcelExport(sheet, 0);
        } else {
            workbook = getWorkbook(inputStream, excelFile);
            sheet = workbook.getSheetAt(0);
        }

        /** WRITE DATA **/
        for (Student student : listStudents) {
            // Create row
            Row row = sheet.createRow(indexRowStart);
            // Write data on row
            writeRowExcelExport(student, row, indexRowStart);
            indexRowStart++;
        }
        /****/

        // Auto resize column witdth
        int numberOfColumn = sheet.getRow(0).getPhysicalNumberOfCells();
        for (int columnIndex = 0; columnIndex < numberOfColumn; columnIndex++) {
            sheet.autoSizeColumn(columnIndex);
        }

        // Create file excel
        OutputStream outputStream = new FileOutputStream(excelFile);
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();
        inputStream.close();

        System.out.println("Write Excel Export Done!!!");
    }

    // Write header with format
    private static void writeHeaderExcelExport(Sheet sheet, int indexRowHeader) throws Exception {
        /** create CellStyle for header **/
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 18); // font size
        font.setColor(IndexedColors.BLACK.getIndex()); // text color

        // Create CellStyle fad73f
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        /****/

        // Create row
        Row row = sheet.createRow(indexRowHeader);

        // Create cells
        Cell cell = row.createCell(COLUMN_INDEX_REGISTER_NUMBER_EXPORT);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Số vào sổ");

        cell = row.createCell(COLUMN_INDEX_FIRST_NAME_EXPORT);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Họ");

        cell = row.createCell(COLUMN_INDEX_LAST_NAME_EXPORT);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Tên");

        cell = row.createCell(COLUMN_INDEX_HOMETOWN_EXPORT);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Nơi sinh");

        cell = row.createCell(COLUMN_INDEX_CLASS_EXPORT);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Lớp");

        cell = row.createCell(COLUMN_INDEX_MODE_OF_STUDY_EXPORT);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Hệ");

        cell = row.createCell(COLUMN_INDEX_DATE_OF_BIRTH_EXPORT);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("NTNS");

        cell = row.createCell(COLUMN_INDEX_ID_CERTIFICATE_EXPORT);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Số hiệu bằng");

//        cell = row.createCell(COLUMN_INDEX_LAW_NUMBER_EXPORT);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("QĐTN SỐ");

        cell = row.createCell(COLUMN_INDEX_NOTE_EXPORT);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Ghi chú");
    }

    public void writeRowExcelExport(Student student, Row row, int id) {
        String[] name = student.getName().split(" ");
        String firstName = "", lastName = name[name.length - 1];
        for (int i = 0; i < name.length - 1; i++) {
            firstName = firstName + name[i];
            if (i < (name.length - 2)) {
                firstName = firstName + " ";
            }
        }

        Cell cell = row.createCell(COLUMN_INDEX_REGISTER_NUMBER_EXPORT);
        cell.setCellValue(student.getRegisterNumber());

        cell = row.createCell(COLUMN_INDEX_FIRST_NAME_EXPORT);
        cell.setCellValue(firstName);

        cell = row.createCell(COLUMN_INDEX_LAST_NAME_EXPORT);
        cell.setCellValue(lastName);

        cell = row.createCell(COLUMN_INDEX_HOMETOWN_EXPORT);
        cell.setCellValue(student.getHometown());

        cell = row.createCell(COLUMN_INDEX_CLASS_EXPORT);
        cell.setCellValue(student.getMajor() + " " + student.getStrClass());

        cell = row.createCell(COLUMN_INDEX_MODE_OF_STUDY_EXPORT);
        cell.setCellValue(student.getModeOfStudy());

        cell = row.createCell(COLUMN_INDEX_DATE_OF_BIRTH_EXPORT);
        cell.setCellValue(student.getDateOfBirth());

        cell = row.createCell(COLUMN_INDEX_ID_CERTIFICATE_EXPORT);
        cell.setCellValue("");

//        cell = row.createCell(COLUMN_INDEX_LAW_NUMBER_EXPORT);
//        cell.setCellValue("");

        cell = row.createCell(COLUMN_INDEX_NOTE_EXPORT);
        cell.setCellValue(student.getNote());
    }
}
