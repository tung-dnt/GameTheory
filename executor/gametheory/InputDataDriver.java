package gametheory;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputDataDriver {
    /**
     *
     * @param normalPlayerList
     * @param row
     * @return List<Conflict>
     * @throws IOException 
     */
    public static List<Conflict> getConflictSet(List<NormalPlayer> normalPlayerList, int row, String path) throws IOException{
        List<Conflict> conflictSet = new ArrayList<>();
        
        for(NormalPlayer normalPlayer : normalPlayerList){
            if(getStringOfCoordinate(row, 0, path).equals("x") == false){
                int col = 0;
                while(true){
                    if(getStringOfCoordinate(row, col, path) != null) {
                        String s = getStringOfCoordinate(row, col, path);
                        if(!s.equals("")) {
                            Conflict conflict = new Conflict(s);
                            conflictSet.add(conflict);
                        }
                        col++;
                    }
                    if(getStringOfCoordinate(row, col, path) == null) break;
                }
            }
            row+=1;
        }
        return conflictSet;
    }

    public static NormalPlayer setNormalPlayer(int row, int numberOfProperties, String path) throws IOException{
        int strategiesOfPlayer = getValueOfCoordinate(row, 0, path);
        List<Strategy> strategies = new ArrayList<>(strategiesOfPlayer);
        int column = 1;
        for (int i = 0; i < strategiesOfPlayer; i++) {
            Strategy strategy = new Strategy();
            for (int j = 0; j < numberOfProperties; j++) {
                strategy.addProperty(getValueOfCoordinate(row, column, path));
                column++;
            }
            strategies.add(strategy);
        }
        return new NormalPlayer(strategies);
    }

    public static Integer getValueOfCoordinate(int rowIndex, int columnIndex, String path) throws IOException   {
        FileInputStream file = new FileInputStream(path);
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		// get the first sheet
		XSSFSheet sheet = workbook.getSheetAt(0);
		Row row = sheet.getRow(rowIndex);
		Cell cell = row.getCell(columnIndex);
		if(cell == null){
        	System.err.println("There is an empty cell");
			return null;
		}
		file.close();
		return (int) cell.getNumericCellValue();			
    }

    public static String getStringOfCoordinate(int rowIndex, int columnIndex, String path) throws IOException {
        FileInputStream file = new FileInputStream(path);
        // create workbook for file
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        // get the first sheet
        XSSFSheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(rowIndex);
        Cell cell = row.getCell(columnIndex);
		if(cell == null){
        	System.err.println("There is an empty cell");
			return null;
		}
		file.close();
        return cell.getStringCellValue();
    }
    
    public static void displayNormalPlayerList(List<NormalPlayer> normalPlayerList){
        for(NormalPlayer normalPlayer : normalPlayerList){
            System.out.println("Normal player : " + (normalPlayerList.indexOf(normalPlayer)+1));
            normalPlayer.display();
            System.out.println("---------------");
        }
    }
}