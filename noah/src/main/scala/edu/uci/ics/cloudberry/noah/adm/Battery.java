package edu.uci.ics.cloudberry.noah.adm;

import edu.uci.ics.cloudberry.gnosis.NewYorkGnosis;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Battery {
    public static String TIME = "time";
    public static String DEVICE_ID = "device_id";
    public static String CHARGE_RATE = "charge_rate";
    public static String DRAIN_RATE = "drain_rate";
    public static String DELTA_TIME = "delta_time";
    public static String IS_BATTERY_DRAINING = "is_battery_draining";
    public static String BATTERY_PERCENTAGE = "battery_percentage";
    public static String BATTERY_TEMPERATURE = "battery_temperature";
    public static String BATTERY_VOLTAGE = "battery_voltage";

    public static String COMMA = ",";

    public static Integer TIME_INDEX = 0;
    public static Integer DEVICE_ID_INDEX = 1;
    public static Integer CHARGE_RATE_INDEX = 2;
    public static Integer DRAIN_RATE_INDEX = 3;
    public static Integer DELTA_TIME_INDEX = 4;
    public static Integer IS_BATTERY_DRAINING_INDEX = 5;
    public static Integer BATTERY_PERCENTAGE_INDEX = 6;
    public static Integer BATTERY_TEMPERATURE_INDEX = 7;
    public static Integer BATTERY_VOLTAGE_INDEX = 8;


    public static void main(String[] args) {
        BufferedReader br;
        String line;
        String srcPath = "/home/sony/b/hackathon/noah/src/main/resources/device_battery_stats_november_1st.csv";
        try {
            FileOutputStream fos = new FileOutputStream(new File("battery.adm"));
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            br = new BufferedReader(new FileReader(srcPath));
            br.readLine();      //skip first line
            while ((line = br.readLine()) != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("{");
                String[] cols = line.split(COMMA);

                DateFormat formatter = new SimpleDateFormat("y-M-d H:m:s");
                Date datetime = formatter.parse(cols[TIME_INDEX]);
                ADM.keyValueToSbWithComma(sb, TIME, ADM.mkDateTimeConstructor(datetime));
                ADM.keyValueToSbWithComma(sb, DEVICE_ID, ADM.mkInt64Constructor(Long.parseLong(cols[DEVICE_ID_INDEX])));
                ADM.keyValueToSbWithComma(sb, CHARGE_RATE, ADM.mkFloatConstructor(cols[CHARGE_RATE_INDEX]));
                ADM.keyValueToSbWithComma(sb, DRAIN_RATE, ADM.mkFloatConstructor(cols[DRAIN_RATE_INDEX]));
                ADM.keyValueToSbWithComma(sb, DELTA_TIME, ADM.mkFloatConstructor(cols[DELTA_TIME_INDEX]));
                ADM.keyValueToSbWithComma(sb, IS_BATTERY_DRAINING, ADM.mkInt32Constructor(cols[IS_BATTERY_DRAINING_INDEX]));
                ADM.keyValueToSbWithComma(sb, BATTERY_PERCENTAGE, ADM.mkFloatConstructor(cols[BATTERY_PERCENTAGE_INDEX]));
                ADM.keyValueToSbWithComma(sb, BATTERY_TEMPERATURE, ADM.mkFloatConstructor(cols[BATTERY_TEMPERATURE_INDEX]));
                ADM.keyValueToSb(sb, BATTERY_VOLTAGE, ADM.mkFloatConstructor(cols[BATTERY_VOLTAGE_INDEX]));
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
