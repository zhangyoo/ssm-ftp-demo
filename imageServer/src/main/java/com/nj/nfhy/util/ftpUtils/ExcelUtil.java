package com.nj.nfhy.util.ftpUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 导出Excel的工具类，只需给相应参数即可
 * 
 * @author 88386726
 * 
 */
public class ExcelUtil {
	/**
	 * 导出Excel的方法
	 * 
	 * @param request
	 *            Web应用中的request对象
	 * @param list
	 *            数据体集合，集合内放置String数组
	 * @param heads
	 *            数据列的头，使用数组
	 * @param titleName
	 *            要导出的文件名称
	 * @param os
	 *            导出流，Web中使用response得到
	 * @throws IOException
	 *             可能会爆出的异常
	 */
	@SuppressWarnings("deprecation")
	public static void create(HttpServletRequest request, List<?> list, String[] heads, String titleName,
			OutputStream os) throws IOException {
		HSSFWorkbook wb = new HSSFWorkbook();// 建立新HSSFWorkbook对象
		HSSFSheet sheet = wb.createSheet("sheet1");// 建立新的sheet对象
		// ================合并标题列=========================
		Region region = new Region();
		region.setColumnFrom((short) 0);
		region.setColumnTo((short) (heads.length - 1));
		region.setRowFrom((short) 0);
		region.setRowTo((short) 0);
		sheet.addMergedRegion(region);
		// ===============================================
		// 标题列列的字体样式
		HSSFFont titleFont = wb.createFont();
		titleFont.setColor(HSSFFont.COLOR_RED);
		titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		titleFont.setFontHeight((short) 300);

		// 选择列的字体样式
		HSSFFont headerFont = wb.createFont();
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerFont.setColor(HSSFFont.BOLDWEIGHT_NORMAL);
		HSSFPrintSetup printSetup = sheet.getPrintSetup();
		printSetup.setLandscape(true);
		sheet.setFitToPage(true);
		sheet.setHorizontallyCenter(true);
		sheet.createFreezePane(0, 1);
		sheet.setAutobreaks(true);
		sheet.setDefaultColumnWidth((short) 13.5);
		printSetup.setFitHeight((short) 100);
		printSetup.setFitWidth((short) 180);
		// 标题列样式
		HSSFCellStyle titlestyle = wb.createCellStyle();
		titlestyle.setFont(titleFont);
		titlestyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titlestyle.setFillForegroundColor(HSSFColor.WHITE.index);
		titlestyle.setFillPattern(HSSFCellStyle.SQUARES);
		titlestyle.setLeftBorderColor(HSSFColor.BLACK.index);
		titlestyle.setRightBorderColor(HSSFColor.BLACK.index);
		titlestyle.setTopBorderColor(HSSFColor.BLACK.index);
		titlestyle.setBottomBorderColor(HSSFColor.BLACK.index);
		// titlestyle.setFillPattern((short)300);

		titlestyle.setWrapText(true);
		// 选择列样式
		HSSFCellStyle style = wb.createCellStyle();
		style.setFont(headerFont);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setHidden(true);
		// 内容列的样式
		HSSFCellStyle style2 = wb.createCellStyle();
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setDataFormat(wb.createDataFormat().getFormat("0.00"));
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 写入标题
		HSSFRow titleRow = null;

		if (!titleName.equals("")) {
			titleRow = sheet.createRow((short) 0);
			titleRow.setHeightInPoints(30.120f);
			HSSFCell titlecell = titleRow.createCell((short) 0);// 标题
			// titlecell.setEncoding(HSSFCell.ENCODING_UTF_16);//
			titlecell.setCellStyle(titlestyle);
			titlecell.setCellValue(titleName);
		}

		// 创建列名称
		HSSFRow headRow = sheet.createRow((short) 1);//
		headRow.setHeightInPoints(20.120f);

		for (int i = 0; i < heads.length; i++) {
			HSSFCell cell = headRow.createCell((short) i);
			cell.setCellStyle(style);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16);// 设置cell编码解决中文高位字节截断
			cell.setCellValue(heads[i]);
		}
		// 创建内容
		for (int i = 0; i < list.size(); i++) {
			// 建立新行
			HSSFRow row = sheet.createRow((short) i + 2);
			for (int j = 0; j < ((Object[]) list.get(i)).length; j++) {
				// 新建一列
				HSSFCell cell = row.createCell((short) j);
				cell.setCellStyle(style2);
				// cell.setEncoding(HSSFCell.ENCODING_UTF_16);//
				// 设置cell编码解决中文高位字节截断
				if (j == 0) {
					cell.setCellValue((i + 1) + "");
					continue;
				}
				Object t = ((Object[]) list.get(i))[j];
				if (t instanceof BigDecimal) {
					cell.setCellValue(((BigDecimal) ((Object[]) list.get(i))[j]).toString());
					continue;
				}
				if (t instanceof java.sql.Date) {
					cell.setCellValue(((java.sql.Date) ((Object[]) list.get(i))[j]).toString());
					continue;
				}
				if (t instanceof java.util.Date) {
					cell.setCellValue(((java.util.Date) ((Object[]) list.get(i))[j]).toString());
					continue;
				}
				cell.setCellValue((String) ((Object[]) list.get(i))[j]);
			}
		}

		request.setCharacterEncoding("gbk");
		// MessageFormat.format("{0}/{1}.xlsx", request.getRealPath("expExcel"),
		// Long.toString(System.currentTimeMillis()));
		wb.write(os);
		os.flush();
		os.close();
	}

	// excel导出
	@RequestMapping("excelDown")
	@ResponseBody
	public void excelDown(String title, String[] headName, List lists, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// List<?> list = getPersonList();
		// String[] headNames = new String[] { "编号", "名称", "住址", "介绍", "出生日期",
		// "月薪" };
		// String titleName = "测试Excel工具类导出";
		String[] headNames = headName;
		// String titleName = "测试Excel工具类导出";
		String titleName = title;
		OutputStream os = response.getOutputStream();
		response.setHeader("Content-Disposition",
				"attachment; filename=" + new String(titleName.getBytes("GB2312"), "ISO-8859-1"));
		String fileNamexls = "Excel-" + String.valueOf(System.currentTimeMillis()).substring(4, 13) + ".xls";
		String fileNamexlsx = "Excel-" + String.valueOf(System.currentTimeMillis()).substring(4, 13) + ".xlsx";
		String headStr = "attachment; filename=\"" + fileNamexls + "\"";
		response.setContentType("APPLICATION/OCTET-STREAM");
		response.setHeader("Content-Disposition", headStr);
		ExcelUtil.create(request, lists, headNames, titleName, os);
	}

	// 此方法是传入list的格式
	public static List getPersonList() {
		List list = new ArrayList();
		String[] p1 = new String[] { "00001", "科研室主任", "海淀区", "北京大学研究生毕业", "1988-9-6", "4500.99" };
		String[] p2 = new String[] { "00002", "营销部经理", "朝阳区", "北京国际商务学院本科生", "1995-6-8", "6000.25" };
		String[] p3 = new String[] { "00003", "策划部专员", "石景山区", "清华大学应届毕业生", "1889-9-5", "10000" };
		list.add(p1);
		list.add(p2);
		list.add(p3);
		return list;
	}

}