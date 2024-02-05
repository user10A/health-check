package healthcheck.excel;
import healthcheck.entities.Schedule;
import healthcheck.entities.TimeSheet;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.IOException;
import java.util.List;

public class ExcelExportUtils {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Schedule> scheduleList;

    public ExcelExportUtils(List<Schedule> customerList) {
        this.scheduleList = customerList;
        workbook = new XSSFWorkbook();
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);

        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);

        } else {
            cell.setCellValue(value.toString());
        }

        cell.setCellStyle(style);
    }

    private void createHeaderRow() {
        sheet = workbook.createSheet("Doctor schedule");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(20);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        createCell(row, 0, " Doctor schedule", style);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
        font.setFontHeightInPoints((short) 10);

        row = sheet.createRow(1);
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "ID", style);
        createCell(row, 1, "First Name", style);
        createCell(row, 2, "Last Name", style);
        createCell(row, 3, "image", style);
        createCell(row, 4, "position", style);
        createCell(row, 5, "day of week", style);
        createCell(row, 6, "date of consultation", style);
        createCell(row, 7, "start time of consultation", style);
        createCell(row, 8, "end time of consultation", style);
    }

    private void writeCustomerData() {
        int rowCount = 2;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);


        for (Schedule schedule : scheduleList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, schedule.getId(), style);
            createCell(row, columnCount++, schedule.getDoctor().getFirstName(), style);
            createCell(row, columnCount++, schedule.getDoctor().getLastName(), style);
            createCell(row, columnCount++, schedule.getDoctor().getImage(), style);
            createCell(row, columnCount++, schedule.getDoctor().getPosition(), style);
            createCell(row, columnCount++, schedule.getDayOfWeek(), style);
            for (TimeSheet timeSheet : schedule.getTimeSheets()) {
                createCell(row, columnCount++, timeSheet.getDateOfConsultation(), style);
                createCell(row, columnCount++, timeSheet.getStartTimeOfConsultation(), style);
                createCell(row, columnCount++, timeSheet.getEndTimeOfConsultation(), style);

            }
        }
    }

    public void exportDataToExcel(HttpServletResponse response) throws IOException {
        createHeaderRow();
        writeCustomerData();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

}
