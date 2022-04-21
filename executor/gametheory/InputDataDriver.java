package gametheory;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputDataDriver {
	String path;
	FileInputStream file;
	XSSFWorkbook workbook;
	XSSFSheet sheet;
	
	public InputDataDriver(String path) throws IOException {
		this.path = path;
		file = new FileInputStream(path);
	    workbook = new XSSFWorkbook(file);
		// get the first sheet
		sheet = workbook.getSheetAt(0);
	}
    /**
     *
     * @param normalPlayerList
     * @param row
     * @return List<Conflict>
     * @throws IOException 
     */
    public List<Conflict> getConflictSet(int row) throws IOException{
        List<Conflict> conflictSet = new ArrayList<>();
        
        while(isRowEmpty(row)){
                int col = 0;
                while(true){
                    if(getStringOfCoordinate(row, col) != null) {
                        String s = getStringOfCoordinate(row, col);
                        if(!s.equals("")) {
                            Conflict conflict = new Conflict(s);
                            conflictSet.add(conflict);
                        }
                        col++;
                    }
                    if(getStringOfCoordinate(row, col) == null) break;
                }
            row++;
        }
        return conflictSet;
    }

    public NormalPlayer setNormalPlayer(int row, int numberOfProperties) throws IOException{
        int strategiesOfPlayer = getValueOfCoordinate(row, 0);
        List<Strategy> strategies = new ArrayList<>(strategiesOfPlayer);
        int column = 1;
        for (int i = 0; i < strategiesOfPlayer; i++) {
            Strategy strategy = new Strategy();
            for (int j = 0; j < numberOfProperties; j++) {
                strategy.addProperty(getValueOfCoordinate(row, column));
                column++;
            }
            strategies.add(strategy);
        }
        return new NormalPlayer(strategies);
    }

    public Integer getValueOfCoordinate(int rowIndex, int columnIndex) throws IOException{
		Row row = sheet.getRow(rowIndex);
		Cell cell = row.getCell(columnIndex);
		if(cell == null){
        	System.err.println("There is an empty cell");
			return null;
		}
		file.close();
		return (int) cell.getNumericCellValue();			
    }

    public String getStringOfCoordinate(int rowIndex, int columnIndex) throws IOException {
        Row row = sheet.getRow(rowIndex);
        Cell cell = row.getCell(columnIndex);
		if(cell == null){
        	System.err.println("There is an empty cell");
			return null;
		}
		file.close();
        return cell.getStringCellValue();
    }
    
    public int getColumnNumberofRow(int rowIndex) throws IOException {
    	return sheet.getRow(0).getPhysicalNumberOfCells();
    }
    
    public boolean isRowEmpty(int rowIndex) throws IOException {
		Row row = sheet.getRow(rowIndex);
		
        if (row == null) {
            return true;
        }
        if (row.getLastCellNum() <= 0) {
            return true;
        }
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellType() != CellType.BLANK && StringUtils.isNotBlank(cell.toString())) {
                return false;
            }
        }
		file.close();
        return true;
    }
    
    public static void displayNormalPlayerList(List<NormalPlayer> normalPlayerList){
        for(NormalPlayer normalPlayer : normalPlayerList){
            System.out.println("Normal player : " + (normalPlayerList.indexOf(normalPlayer)+1));
            normalPlayer.display();
            System.out.println("---------------");
        }
    }
}