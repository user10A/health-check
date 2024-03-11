package healthcheck.excel;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

import java.io.IOException;

public interface ExcelExportUtils {
    void createCell(Row row, int columnCount, Object value, CellStyle style);
    void writeCustomerData();
    void createHeaderRow();
    void exportDataToExcel(HttpServletResponse response) throws IOException;
}
