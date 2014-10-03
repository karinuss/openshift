/*
 * Simple Post tracker informator-validator 
 * Based on standart UPU S10 http://pls.upu.int/document/2011/an/cep_c_4_gn_ep_4-1/src/d008_ad00_an01_p00_r00.pdf
Country names use ISO 3166-1 alpha-2 codes (see standart)
 
Number:
 2 letter(type) + 8 digits + 1 crc + 2 letter(country)  

 type:
 LA-LZ - not registered, < 2 kg
 RA-RZ - registered, < 2kg 
 CA-CZ - registered, >= 2kg 
 EA-EZ - registered Express(EMS)

Not full UPU compatible
 UA-UZ - Yanwen Express, not tracking outside China.
 WA-WZ - WeDo Express, not tracking outside China.
 YW...CN - Yanwen Express(EMS)

"Первые восемь цифр (12345678) — уникальный номер отправления. 
 Девятая цифра (9) — проверочный код, рассчитываемый по формуле:
  1.  каждая из первых восьми цифр номера умножается соответственно на 8, 6, 4, 2, 3, 5, 9, 7
  2.  полученные значения суммируются
  3.  результат делится на 11, чтобы получить остаток
  4.  остаток вычитается из 11
  5.  полученный результат является проверочным кодом, если он больше или равен 1, но меньше или равен 9
    -    если результат равен 10, то проверочный код равен 0
    -    если результат равен 11, то проверочный код равен 5"
(С) http://ru.wikipedia.org/wiki/Почтовый_идентификатор

 * Tracking sites *
Russian post  http://www.russianpost.ru/tracking20/
China post http://intmail.183.com.cn/icc-itemstatusen.jsp
Singapure post
India post http://www.indiapost.gov.in/tracking.aspx
USA 
UK
YW Express http://www.yw56.com.cn/english/diy.asp
WeDo Express http://www.wedoexpress.com/rest?carrier=wedo
*/
package ru.karinuss.posttrackparser;

import java.util.Locale;
import java.util.Map;
import org.apache.commons.collections4.map.DefaultedMap;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author karina
 */
public class PosttrackParser {

    private static volatile PosttrackParser Instance;
    private static final Map<String, PostCarrier> PostCarrierTypes;
    private static final Map<String, String> PostCountries;
    private static final Map<String, String> PostCarrierWebtrack;
    private static final int TrackLenght = 13, NumberLenght = 9;
    private static final byte[] MULTIPLIERS = { 8, 6, 4, 2, 3, 5, 9, 7 };

    static {
        PostCarrierTypes = new DefaultedMap(new PostCarrier("invalid type", "invalid carrier name"));
        PostCarrierTypes.put("L", new PostCarrier("not registered, < 2 kg"));
        PostCarrierTypes.put("R", new PostCarrier("registered, < 2 kg"));
        PostCarrierTypes.put("C", new PostCarrier("registered parcel, >= 2 kg"));
        PostCarrierTypes.put("E", new PostCarrier("registered Express Mail Service(EMS)", "EMS"));
        PostCarrierTypes.put("U", new PostCarrier("local China Express", "Yanwen Express"));
        PostCarrierTypes.put("Y", new PostCarrier("China Express", "Yanwen Express", "EMS")); //fixme y- > yw
        PostCarrierTypes.put("W", new PostCarrier("local China Express", "WeDo Express"));
        
        PostCarrierWebtrack = new DefaultedMap("");
        PostCarrierWebtrack.put("CN", "http://intmail.183.com.cn/icc-itemstatusen.jsp");
        PostCarrierWebtrack.put("IN", "http://www.indiapost.gov.in/tracking.aspx");
        PostCarrierWebtrack.put("RU", "http://www.russianpost.ru/tracking20/");
        PostCarrierWebtrack.put("WeDo Express", "http://www.wedoexpress.com/rest?carrier=wedo");
        PostCarrierWebtrack.put("Yanwen Express", "http://www.yw56.com.cn/english/diy.asp");
 
        PostCountries = new DefaultedMap("unknown country");
        String[] countries = Locale.getISOCountries();

        for (String c : countries) {
            Locale locale = new Locale("", c);
            PostCountries.put(c, locale.getDisplayCountry(Locale.US));
            // PostCountries.put(c, locale.getDisplayCountry(Locale.US));
        }
    }

    private PosttrackParser() {

    }
    
    public static PosttrackParser getInstance() {
        PosttrackParser instance = Instance;
        
        //    if(instance == null) {
        synchronized (PosttrackParser.class) {
            instance = Instance;
            if (instance == null) {
                instance = Instance = new PosttrackParser();
            }
        }
       //  }

        return instance;
    }

    public PostCarrier parseNumber(String number) throws InvalidTrackNumberException {

        if (number.length() != TrackLenght) {
            throw new InvalidTrackNumberException(number);
        }
        
        String countryCode = number.substring(11);
        PostCarrier carrier = PostCarrierTypes.get(number.substring(0, 1));
        carrier.setCountry(PostCountries.get(number.substring(11)));
        
        if(carrier.getName().compareToIgnoreCase("ordinary post") == 0)
            carrier.setWebtrack(PostCarrierWebtrack.get(countryCode));
        else
            carrier.setWebtrack(PostCarrierWebtrack.get(carrier.getName()));

        if(!validDigits(number)) {
            throw new InvalidTrackNumberException(number);
        }
        
        return carrier;
    }

    public boolean validDigits(String number) {
        
        if (number.length() != TrackLenght) {
            return false;
        }
        
        String sdigits = number.substring(2, 11);
        
        if(!StringUtils.isNumeric(sdigits)) {
            return false;
        }

        byte[] digits = new byte[NumberLenght];  
        
        
        for(int i = 0; i < NumberLenght; i++) {
            digits[i] = Byte.parseByte(String.valueOf(sdigits.charAt(i)));
        }
            
        int sum = 0;
        
        for(int i = 0; i < NumberLenght-1; i++) {
            sum += digits[i]*MULTIPLIERS[i]; 
        }
        
       int valid_code; // = -1; 
       int remainder = 11 - (sum % 11); 
       
       if((remainder >= 1) && (remainder <= 9 )) {
           valid_code = remainder;
       } else if(remainder == 10) {
           valid_code = 0;
       } else if(remainder == 11) {
           valid_code = 5;
       } else {
         //  valid_code = -1;
           return false;
       } 
       
       return (valid_code == digits[NumberLenght-1]);
    }
            
    public static void main(String[] args) {

    }
}
