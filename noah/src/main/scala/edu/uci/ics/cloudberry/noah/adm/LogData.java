package edu.uci.ics.cloudberry.noah.adm;

import edu.uci.ics.cloudberry.gnosis.NewYorkGnosis;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogData {
    public static String DEVICE_ID = "device_id";
    public static String DEVICE_OS = "device_os";
    public static String COMPANY_ID = "company_id";
    public static String DEVICE_ENTRY_ID = "device_entry_id";
    public static String LOG_TIME = "log_time";
    public static String PACKAGE_NAME = "package_name";
    public static String APP_VERSION = "application_version";
    public static String APP_VERSION_ID = "application_version_id";
    public static String VERSION = "version";
    public static String BATTERY = "battery";
    public static String BACK_BATTERY = "back_battery";
    public static String CPU = "cpu";
    public static String BACK_CPU = "back_cpu";
    public static String MEMORY = "memory";
    public static String DATA_ALL = "data_all";
    public static String BACK_DATA = "back_data";
    public static String DATA_WIFI = "data_wifi";
    public static String DATA_MOBILE = "data_mobile";
    public static String CRASH_COUNT = "crash_count";
    public static String RUN_TIME = "run_time";
    public static String FRONT_RUN_TIME = "front_run_time";
    public static String CODE_SIZE = "code_size";
    public static String DATA_SIZE = "data_size";
    public static String CACHE_SIZE = "cache_size";
    public static String OTHER_SIZE = "other_size";

    public static String COMMA = ",";

    public static Integer DEVICE_ID_INDEX = 0;
    public static Integer DEVICE_OS_INDEX = 1;
    public static Integer COMPANY_ID_INDEX = 2;
    public static Integer DEVICE_ENTRY_ID_INDEX = 3;
    public static Integer LOG_TIME_INDEX = 4;
    public static Integer PACKAGE_NAME_INDEX = 5;
    public static Integer APP_VERSION_INDEX = 6;
    public static Integer APP_VERSION_ID_INDEX = 7;
    public static Integer VERSION_INDEX = 8;
    public static Integer BATTERY_INDEX = 9;
    public static Integer BACK_BATTERY_INDEX = 10;
    public static Integer CPU_INDEX = 11;
    public static Integer BACK_CPU_INDEX = 12;
    public static Integer MEMORY_INDEX = 13;
    public static Integer DATA_ALL_INDEX = 14;
    public static Integer BACK_DATA_INDEX = 15;
    public static Integer DATA_WIFI_INDEX = 16;
    public static Integer DATA_MOBILE_INDEX = 17;
    public static Integer CRASH_COUNT_INDEX = 18;
    public static Integer RUN_TIME_INDEX = 19;
    public static Integer FRONT_RUN_TIME_INDEX = 20;
    public static Integer CODE_SIZE_INDEX = 21;
    public static Integer DATA_SIZE_INDEX = 22;
    public static Integer CACHE_SIZE_INDEX = 23;
    public static Integer OTHER_SIZE_INDEX = 24;

    public static void main(String[] args) {
        BufferedReader br;
        String line;
        String srcPath = "/home/sony/b/hackathon/noah/src/main/resources/log_data_septermber_1st.csv";
        try {
            FileOutputStream fos = new FileOutputStream(new File("log_data.adm"));
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            br = new BufferedReader(new FileReader(srcPath));
            br.readLine();      //skip first line
            while ((line = br.readLine()) != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("{");
                String[] cols = line.split(COMMA);

                ADM.keyValueToSbWithComma(sb,DEVICE_ID, ADM.mkInt64Constructor(Long.parseLong(cols[DEVICE_ID_INDEX])));
                ADM.keyValueToSbWithComma(sb, DEVICE_OS, ADM.mkInt32Constructor(cols[DEVICE_OS_INDEX]));
                ADM.keyValueToSbWithComma(sb, COMPANY_ID, ADM.mkInt32Constructor(cols[COMPANY_ID_INDEX]));
                ADM.keyValueToSbWithComma(sb, DEVICE_ENTRY_ID, ADM.mkInt64Constructor(Long.parseLong(cols[DEVICE_ENTRY_ID_INDEX])));
                DateFormat formatter = new SimpleDateFormat("y-M-d H:m:s");
                Date datetime = formatter.parse(cols[LOG_TIME_INDEX]);
                ADM.keyValueToSbWithComma(sb, LOG_TIME, ADM.mkDateTimeConstructor(datetime));
                ADM.keyValueToSbWithComma(sb, PACKAGE_NAME, ADM.mkQuote(cols[PACKAGE_NAME_INDEX]));
                ADM.keyValueToSbWithComma(sb, APP_VERSION, ADM.mkQuote(cols[APP_VERSION_INDEX]));
                ADM.keyValueToSbWithComma(sb, APP_VERSION_ID, ADM.mkInt64Constructor(Long.parseLong(cols[APP_VERSION_ID_INDEX])));
                ADM.keyValueToSbWithComma(sb, VERSION, ADM.mkQuote(cols[VERSION_INDEX]));
                ADM.keyValueToSbWithComma(sb, BATTERY, ADM.mkFloatConstructor(cols[BATTERY_INDEX]));
                ADM.keyValueToSbWithComma(sb, BACK_BATTERY, ADM.mkFloatConstructor(cols[BACK_BATTERY_INDEX]));
                ADM.keyValueToSbWithComma(sb, CPU, ADM.mkInt32Constructor(cols[CPU_INDEX]));
                ADM.keyValueToSbWithComma(sb, BACK_CPU, ADM.mkInt32Constructor(cols[BACK_CPU_INDEX]));
                ADM.keyValueToSbWithComma(sb, MEMORY, ADM.mkInt64Constructor(Long.parseLong(cols[MEMORY_INDEX])));
                ADM.keyValueToSbWithComma(sb, DATA_ALL, ADM.mkInt64Constructor(Long.parseLong(cols[DATA_ALL_INDEX])));
                ADM.keyValueToSbWithComma(sb, BACK_DATA, ADM.mkInt64Constructor(Long.parseLong(cols[BACK_DATA_INDEX])));
                ADM.keyValueToSbWithComma(sb, DATA_WIFI, ADM.mkFloatConstructor(cols[DATA_WIFI_INDEX]));
                ADM.keyValueToSbWithComma(sb, DATA_MOBILE, ADM.mkFloatConstructor(cols[DATA_MOBILE_INDEX]));
                ADM.keyValueToSbWithComma(sb, CRASH_COUNT, ADM.mkInt32Constructor(cols[CRASH_COUNT_INDEX]));
                ADM.keyValueToSbWithComma(sb, RUN_TIME, ADM.mkInt64Constructor(Long.parseLong(cols[RUN_TIME_INDEX])));
                ADM.keyValueToSbWithComma(sb, FRONT_RUN_TIME, ADM.mkInt64Constructor(Long.parseLong(cols[FRONT_RUN_TIME_INDEX])));
                ADM.keyValueToSbWithComma(sb, CODE_SIZE, ADM.mkFloatConstructor(cols[CODE_SIZE_INDEX]));
                ADM.keyValueToSbWithComma(sb, DATA_SIZE, ADM.mkFloatConstructor(cols[DATA_SIZE_INDEX]));
                ADM.keyValueToSbWithComma(sb, CACHE_SIZE, ADM.mkFloatConstructor(cols[CACHE_SIZE_INDEX]));
                ADM.keyValueToSb(sb, OTHER_SIZE, ADM.mkFloatConstructor(cols[OTHER_SIZE_INDEX]));
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
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}
