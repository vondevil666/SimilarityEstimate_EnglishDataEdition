import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by dongz on 2017/7/25.
 */
public class Main {
    private String standardExcelPath;
    private String targetExcelPath;
    private int targetEnglishVolumeNumber;
    private String targetFileName;
    private static UI ui;

    public static void main(String[] args) {
//        Main main=new Main();
//        main.setStandardExcelPath("StandardPatternEnglish-200.xlsx");
//        main.setTargetExcelPath("Alak(The2001)-663.xlsx","B");
//        main.startMainStream();
        try{
            ui=new UI();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void printOut(String s) {
        UI.UIPrintOut(ui,s);
    }

    public void startMainStream(){
        ArrayList<HashSet<String>> standardList = ExcelReader.readExcel(standardExcelPath);
        ArrayList<String> targetList = ExcelReader.readExcel(targetExcelPath, targetEnglishVolumeNumber);
        ArrayList<ArrayList<String>> standardDataWeNeedList=ExcelReader.getStandardDataWeNeedList();
        ArrayList<String> targetDataWeNeedList=ExcelReader.getTargetDataWeNeedList();

        for (int i=0;i<standardList.size();i++) {
            outer:for(int j=0;j<targetList.size();j++) {
                String[] currentSplitedArray=targetList.get(j).split(",|，");    //目标表中未拆分的英语词数组
                for(String singleSplitedString:currentSplitedArray){
                    if(singleSplitedString.length()==0)continue;
                    if(standardList.get(i).contains(singleSplitedString.trim())){
                        if(currentSplitedArray.length==1){
                            standardDataWeNeedList.set(i,newArray(standardDataWeNeedList,i));
                            standardDataWeNeedList.get(i).add(targetDataWeNeedList.get(j));
                            break outer;
                        }
                        standardDataWeNeedList.get(i).add(targetDataWeNeedList.get(j));
                        break;
                    }
                }
            }
        }
        ArrayList finalList=makeFinalList(standardDataWeNeedList);
        CreateFile.createFile(finalList, targetExcelPath);
        System.out.println(targetFileName);
        printOut(targetFileName+"处理完成.");
    }

    //当发现完全匹配的目标词条时，将standardDataWeNeedList中已添加的目标词汇（非完全匹配的词条）清空
    //但是需要保留standard'Data'W'e'Need'List前4列内容（序号，标准词等）
    private ArrayList<String> newArray(ArrayList<ArrayList<String>> standardDataWeNeedList,int i){
        ArrayList<String> lvList = new ArrayList<String>();
        lvList.add(standardDataWeNeedList.get(i).get(0));
        lvList.add(standardDataWeNeedList.get(i).get(1));
        lvList.add(standardDataWeNeedList.get(i).get(2));
        lvList.add(standardDataWeNeedList.get(i).get(3));
        return lvList;
    }

    private ArrayList<String[]> makeFinalList(ArrayList<ArrayList<String>> preFinalList) {
        ArrayList<String[]> finalList=new ArrayList<String[]>();
        StringBuilder sb;
        for (ArrayList<String> rowList:preFinalList) {
            String[] array=new String[5];
            sb=new StringBuilder();
            for(int i=0;i<rowList.size();i++) {
                if(i<4) array[i] = rowList.get(i);
                else if(i<7) sb.append(rowList.get(i)).append(',');
            }
            array[4]=sb.toString().endsWith(",")?sb.substring(0,sb.length()-1):sb.substring(0,sb.length());
            finalList.add(array);
        }
        return finalList;
    }

    public void setStandardExcelPath(String standardExcelPath){
        printOut("加载标准表...");
        this.standardExcelPath=standardExcelPath;
    }

    public void setTargetExcelPath(String targetExcelPath,String volumeName){
        printOut("加载目标表...");
        targetFileName = targetExcelPath;
        targetEnglishVolumeNumber = checkEnglishVolumeNameValidationReturnInteger(volumeName);
        this.targetExcelPath=targetExcelPath;
    }

    private String getFileName(String filePath) {
        return filePath.substring(filePath.lastIndexOf('\\')+1,filePath.length());
    }

    //需要用户指定目标表中英文在哪一列，该函数检查用户输入是否有效，只接受字符串A-Z
    private int checkEnglishVolumeNameValidationReturnInteger(String volumeName) {
        char c=0;
        try{
            if(volumeName.length()>1)throw new Exception("Invalid volume name.");
            else c = volumeName.charAt(0);
            if(c>90 || c<65) throw new Exception("Invalid volume name.");
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        return c-65;
    }
}