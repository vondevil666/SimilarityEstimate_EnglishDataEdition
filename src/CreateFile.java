import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class CreateFile {
	List<String[]> finalList;

	public static void createFile(List<String[]> finalList,String filePath){
//		this.finalList=finalList;
		Workbook wb=new XSSFWorkbook();
		Sheet sheet=wb.createSheet("Sheet");
		Row row;
		FileOutputStream fileOut;

		for(int i=0;i<=finalList.size();i++){
			row=sheet.createRow(i);
			if(i==0){
				row.createCell(0).setCellValue("正确100词序");
				row.createCell(1).setCellValue("order207");
				row.createCell(2).setCellValue("chinese");
				row.createCell(3).setCellValue("Standardwords");
				row.createCell(4).setCellValue(filePath.substring(filePath.lastIndexOf('\\')+1,filePath.lastIndexOf('.')));
			}
			else{
				row.createCell(0).setCellValue(finalList.get(i-1)[0]);
				row.createCell(1).setCellValue(finalList.get(i-1)[1]);
				row.createCell(2).setCellValue(finalList.get(i-1)[2]);
				row.createCell(3).setCellValue(finalList.get(i-1)[3]);
				row.createCell(4).setCellValue(finalList.get(i-1)[4]);
			}
		}
		try{
		fileOut=new FileOutputStream(filePath.substring(0,filePath.lastIndexOf('.'))+"_NEW.xlsx");
		wb.write(fileOut);
		fileOut.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
