package edu.uci.ics.cloudberry.noah.adm;

import edu.uci.ics.cloudberry.gnosis.NewYorkGnosis;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Devices {
    public static String DEVICE_ID = "device_id";
    public static String COMPANY_ID = "company_id";
    public static String DEVICE_TYPE_ID = "device_type_id";
    public static String DEVICE_TYPE = "device_type";
    public static String DEVICE_OS = "device_os";
    public static String CARRIER_NAME = "carrier_name";
    public static String CARRIER_ID = "carrier_id";
    public static String LANG = "lang";

    public static String COMMA = ",";

    public static Integer DEVICE_ID_INDEX = 0;
    public static Integer COMPANY_ID_INDEX = 4;
    public static Integer DEVICE_TYPE_ID_INDEX = 6;
    public static Integer DEVICE_TYPE_INDEX = 7;
    public static Integer DEVICE_OS_INDEX = 8;
    public static Integer CARRIER_NAME_INDEX = 14;
    public static Integer CARRIER_ID_INDEX = 15;
    public static Integer LANG_INDEX = 24;

    public static void main(String[] args) {
        BufferedReader br;
        String line;
        String srcPath = "/home/sony/b/hackathon/noah/src/main/resources/devices.csv";
        try {
            FileOutputStream fos = new FileOutputStream(new File("devices.adm"));
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            br = new BufferedReader(new FileReader(srcPath));
            //br.readLine();      //skip first line
            int count = 0;
            while ((line = br.readLine()) != null) {
                count ++;
                StringBuilder sb = new StringBuilder();
                sb.append("{");
                String[] cols = line.split(COMMA);
                ADM.keyValueToSbWithComma(sb,DEVICE_ID, ADM.mkInt64Constructor(Long.parseLong(cols[DEVICE_ID_INDEX])));
                ADM.keyValueToSbWithComma(sb, COMPANY_ID, ADM.mkInt32Constructor(cols[COMPANY_ID_INDEX]));
                ADM.keyValueToSbWithComma(sb, DEVICE_TYPE_ID, ADM.mkInt32Constructor(cols[DEVICE_TYPE_ID_INDEX]));
                ADM.keyValueToSbWithComma(sb, DEVICE_TYPE, ADM.mkQuote(cols[DEVICE_TYPE_INDEX]));
                ADM.keyValueToSbWithComma(sb, DEVICE_OS, ADM.mkInt32Constructor(cols[DEVICE_OS_INDEX]));
                ADM.keyValueToSbWithComma(sb, CARRIER_NAME, ADM.mkQuote(cols[CARRIER_NAME_INDEX]));
                ADM.keyValueToSbWithComma(sb, CARRIER_ID, ADM.mkInt32Constructor(cols[CARRIER_ID_INDEX]));
                ADM.keyValueToSb(sb, LANG, ADM.mkQuote(cols[LANG_INDEX]));
                sb.append("}\n");
                osw.write(sb.toString());
            }
            br.close();
            osw.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
