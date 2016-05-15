package edu.uci.ics.cloudberry.noah.adm;

import edu.uci.ics.cloudberry.gnosis.NewYorkGnosis;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MobileSignal {
    public static String TIME = "time";
    public static String DEVICE_ID = "device_id";
    public static String CDMA_DBM = "cdma_dbm";
    public static String CDMA_ASU_LEVEL = "cdma_asu_level";
    public static String CDMA_ECIO = "cdma_ecio";
    public static String EVDO_DBM = "evdo_dbm";
    public static String EVDO_ASU_LEVEL = "evdo_asu_level";
    public static String EVDO_ECIO = "evdo_ecio";
    public static String GSM_DBM = "gsm_dbm";
    public static String GSM_ASU_LEVEL = "gsm_asu_level";
    public static String LTE_DBM = "lte_dbm";
    public static String LTE_ASU_LEVEL = "lte_asu_level";
    public static String LTE_RSRP = "lte_rsrp";
    public static String LTE_RSRQ = "lte_rsrq";
    public static String WCDMA_DBM = "wcdma_dbm";
    public static String WCDMA_ASU_LEVEL = "wcdma_asu_level";
    public static String LOCATION = "location";
    public static String GEO_TAG = "geo_tag";

    public static String COMMA = ",";

    public static Integer TIME_INDEX = 0;
    public static Integer DEVICE_ID_INDEX = 1;
    public static Integer CDMA_DBM_INDEX = 2;
    public static Integer CDMA_ASU_LEVEL_INDEX = 3;
    public static Integer CDMA_ECIO_INDEX = 4;
    public static Integer EVDO_DBM_INDEX = 5;
    public static Integer EVDO_ASU_LEVEL_INDEX = 6;
    public static Integer EVDO_ECIO_INDEX = 7;
    public static Integer GSM_DBM_INDEX = 8;
    public static Integer GSM_ASU_LEVEL_INDEX = 9;
    public static Integer LTE_DBM_INDEX = 10;
    public static Integer LTE_ASU_LEVEL_INDEX = 11;
    public static Integer LTE_RSRP_INDEX = 12;
    public static Integer LTE_RSRQ_INDEX = 13;
    public static Integer WCDMA_DBM_INDEX = 14;
    public static Integer WCDMA_ASU_LEVEL_INDEX = 15;
    public static Integer LNG_INDEX = 16;
    public static Integer LAT_INDEX = 17;

    public static void toADMFile(String srcPath, NewYorkGnosis gnosis) {
        BufferedReader br;
        String line;

        try {
            FileOutputStream fos = new FileOutputStream(new File("signal_sep.adm"));
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            br = new BufferedReader(new FileReader(srcPath));
            br.readLine();      //skip first line
            int count = 0;
            int total = 0;
            while ((line = br.readLine()) != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("{");
                String[] cols = line.split(COMMA);
                DateFormat formatter = new SimpleDateFormat("y-M-d H:m:s");
                Date datetime = formatter.parse(cols[TIME_INDEX]);
                ADM.keyValueToSbWithComma(sb, TIME, ADM.mkDateTimeConstructor(datetime));
                ADM.keyValueToSbWithComma(sb, DEVICE_ID, ADM.mkInt64Constructor(Long.parseLong(cols[DEVICE_ID_INDEX])));
                ADM.keyValueToSbWithComma(sb, CDMA_DBM, ADM.mkFloatConstructor(cols[CDMA_DBM_INDEX]));
                ADM.keyValueToSbWithComma(sb, CDMA_ASU_LEVEL, ADM.mkFloatConstructor(cols[CDMA_ASU_LEVEL_INDEX]));
                ADM.keyValueToSbWithComma(sb, CDMA_ECIO, ADM.mkFloatConstructor(cols[CDMA_ECIO_INDEX]));
                ADM.keyValueToSbWithComma(sb, EVDO_DBM, ADM.mkFloatConstructor(cols[EVDO_DBM_INDEX]));
                ADM.keyValueToSbWithComma(sb, EVDO_ASU_LEVEL, ADM.mkFloatConstructor(cols[EVDO_ASU_LEVEL_INDEX]));
                ADM.keyValueToSbWithComma(sb, EVDO_ECIO, ADM.mkFloatConstructor(cols[EVDO_ECIO_INDEX]));
                ADM.keyValueToSbWithComma(sb, GSM_DBM, ADM.mkFloatConstructor(cols[GSM_DBM_INDEX]));
                ADM.keyValueToSbWithComma(sb, GSM_ASU_LEVEL, ADM.mkFloatConstructor(cols[GSM_ASU_LEVEL_INDEX]));
                String lte_dbm_s = ADM.mkFloatConstructor(cols[LTE_DBM_INDEX]);
                if(!lte_dbm_s.equals("null")) {
                    float lte_dbm = Float.parseFloat(cols[LTE_DBM_INDEX]);
                    if (lte_dbm < -180 || lte_dbm > 180)
                        lte_dbm_s = "null";
                }
                ADM.keyValueToSbWithComma(sb, LTE_DBM, lte_dbm_s);
                ADM.keyValueToSbWithComma(sb, LTE_ASU_LEVEL, ADM.mkFloatConstructor(cols[LTE_ASU_LEVEL_INDEX]));
                ADM.keyValueToSbWithComma(sb, LTE_RSRP, ADM.mkFloatConstructor(cols[LTE_RSRP_INDEX]));
                ADM.keyValueToSbWithComma(sb, LTE_RSRQ, ADM.mkFloatConstructor(cols[LTE_RSRQ_INDEX]));
                ADM.keyValueToSbWithComma(sb, WCDMA_DBM, ADM.mkFloatConstructor(cols[WCDMA_DBM_INDEX]));
                ADM.keyValueToSbWithComma(sb, WCDMA_ASU_LEVEL, ADM.mkFloatConstructor(cols[WCDMA_ASU_LEVEL_INDEX]));
                ADM.keyValueToSbWithComma(sb, LOCATION, ADM.mkPoint(cols[LNG_INDEX], cols[LAT_INDEX]));
                String geoTags = geoTag(cols[LNG_INDEX], cols[LAT_INDEX], gnosis);
                if (!geoTags.equals("null"))
                    count ++;
                total ++;
                ADM.keyValueToSb(sb, GEO_TAG, geoTags);
                sb.append("}\n");
                osw.write(sb.toString());
            }
            System.out.println("!!!!!!" + count);
            System.out.println("!@@@@@" + total);
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


    public static String geoTag(String lng, String lat, NewYorkGnosis gnosis) {
        StringBuilder sb = new StringBuilder();
        scala.Option<NewYorkGnosis.NYGeoTagInfo> info = gnosis.tagPoint(Double.parseDouble(lng), Double.parseDouble(lat));
        if (info.isEmpty()) {
            return "null";
        }
        sb.append(info.get().toString());
        return sb.toString();
    }
}
