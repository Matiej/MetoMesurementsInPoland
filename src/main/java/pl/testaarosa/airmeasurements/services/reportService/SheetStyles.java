package pl.testaarosa.airmeasurements.services.reportService;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)

public class SheetStyles {

    public HSSFCellStyle cellStyle(HSSFWorkbook workbook, String styleName) {
        //air, /ordinary/ syn/ stat
        HSSFFont font = workbook.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL);
        if (styleName.equalsIgnoreCase("ordinary")) {
            font.setFontHeightInPoints((short) 8);
        } else if(styleName.equalsIgnoreCase("false")){
            font.setColor(HSSFColor.RED.index);
        } else {
            font.setFontHeightInPoints((short) 9);
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            switch (styleName.toLowerCase()) {
                case "air":
                    font.setColor(HSSFColor.BLUE.index);
                    break;
                case "synoptic":
                    font.setColor(HSSFColor.ORANGE.index);
                    break;
                case "station":
                    font.setColor(HSSFColor.DARK_YELLOW.index);
                    break;
                default:
                    font.setColor(HSSFColor.BLACK.index);
                    break;
            }
        }
        HSSFCellStyle style = workbook.createCellStyle();
        if (styleName.equalsIgnoreCase("ordinary") || styleName.equalsIgnoreCase("false")) {
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        } else {
            style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
            style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
            style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
            style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
            style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
        }
        style.setVerticalAlignment(CellStyle.ALIGN_CENTER);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFont(font);
        return style;
    }
}
