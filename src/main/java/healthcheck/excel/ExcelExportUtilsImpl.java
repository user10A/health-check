package healthcheck.excel;

import healthcheck.dto.Schedule.ResponseToGetSchedules;
import healthcheck.dto.Schedule.ScheduleDate;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class ExcelExportUtilsImpl implements ExcelExportUtils{

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<ResponseToGetSchedules> schedules;

    public ExcelExportUtilsImpl(List<ResponseToGetSchedules> customerList) {
        this.schedules = customerList;
        workbook = new XSSFWorkbook();
    }
    @Override
    public void createCell(Row row, int columnCount, Object value, CellStyle style) {
        try {
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
        } catch (Exception e) {
            log.info("1");
            e.printStackTrace();
            // throw new RuntimeException("Error occurred while creating cell", e);
        }
    }

    @Override
    public void createHeaderRow() {
        try {
            sheet = workbook.createSheet("Doctor schedule");
            Row row = sheet.createRow(0);
            int rowIndex = 2; // Start row index

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
            createCell(row, 1, "Full Name", style);
            createCell(row, 2, "position", style);
            createCell(row, 3, "date ", style);
            createCell(row, 4, "time sheet ", style);
            createCell(row, 5, "day of week", style);

        } catch (Exception e) {
            log.info("2");

            // Handle the exception, log it or re-throw it if necessary
            e.printStackTrace();
            // throw new RuntimeException("Error occurred while creating header row", e);
        }
    }

    @Override
    public void writeCustomerData() {
        try {
            int rowCount = 2;
            CellStyle style = workbook.createCellStyle();
            XSSFFont font = workbook.createFont();
            font.setFontHeight(14);
            style.setFont(font);

            for (ResponseToGetSchedules schedule : schedules) {
                Row row = sheet.createRow(rowCount++);
                int columnCount = 0;
                createCell(row, columnCount++, schedule.getId(), style);
                createCell(row, columnCount++, schedule.getSurname(), style);
                createCell(row, columnCount++, schedule.getPosition(), style);
                for (ScheduleDate timeSheet : schedule.getDates()) {
                    createCell(row, columnCount++, timeSheet.getDateOfConsultation(), style);
                    createCell(row, columnCount++, timeSheet.getStartTimeOfConsultation(), style);
                    createCell(row, columnCount++, timeSheet.getDayOfWeek(), style);
                }
            }
        } catch (Exception e) {
            log.info("3");
            // Handle the exception, log it or re-throw it if necessary
            e.printStackTrace();
            // throw new RuntimeException("Error occurred while writing customer data", e);
        }
    }

    @Override
    public void exportDataToExcel(HttpServletResponse response) throws IOException {
        try {
            createHeaderRow();
            writeCustomerData();
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        } catch (IOException e) {
            log.info("4");

            // Handle the exception, log it or re-throw it if necessary
            e.printStackTrace();
            throw e;
        }
    }


}