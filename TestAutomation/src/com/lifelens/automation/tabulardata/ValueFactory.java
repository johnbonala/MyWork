package com.lifelens.automation.tabulardata;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;

public class ValueFactory {

	private static Logger logger = Logger.getLogger(ValueFactory.class.getName());
	private static final String DATETIME = "{dateTime}";
	private static final String ANY = "{any}";

	private static DateFormat sdf = SimpleDateFormat
			.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT);

	public static Value valueFor(String str) {
		logger.debug("Start of ValueFactory: valueFor with string parameter");
		if (DATETIME.equals(str)) {
			str = sdf.format(new Date());
		} else if (ANY.equalsIgnoreCase(str)) {
			return Value.acceptAny();
		}
		logger.debug("End of ValueFactory: valueFor with string parameter");
		return new Value(str.trim());
	}

	public static Value valueFor(Cell cell) {
		logger.debug("Start of ValueFactory: valueFor with cell parameter");
		String value = null;

		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			value = cell.getStringCellValue();
			break;

		case Cell.CELL_TYPE_NUMERIC:
			// to handle date type cell values
			if (DateUtil.isCellDateFormatted(cell)) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				value = sdf.format(cell.getDateCellValue());
			} else {
				// to handle double cell values
				cell.setCellType(Cell.CELL_TYPE_STRING);
				value = cell.getStringCellValue();
			}
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			boolean booleanValue = (boolean) cell.getBooleanCellValue();
			value = String.valueOf(booleanValue);
			break;
		case Cell.CELL_TYPE_BLANK:
			value = "";

		}
		logger.debug("End of ValueFactory: valueFor with cell parameter");
		if (value != null) {
			return new Value(value.trim());
		} else {
			return new Value("");
		}
	}
}
