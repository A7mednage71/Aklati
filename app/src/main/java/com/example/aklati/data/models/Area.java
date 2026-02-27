package com.example.aklati.data.models;

import com.google.gson.annotations.SerializedName;

public class Area {
    @SerializedName("strArea")
    private String area;

    public String getStrArea() {
        return area;
    }

    // Returns the country flag URL from FlagCDN based on the area name
    // FlagCDN format: https://flagcdn.com/w320/{country-code}.png
    
    public String getCountryFlag() {
        if (area == null) return "";

        String countryCode = getCountryCode(area);
        if (countryCode.isEmpty()) {
            // Return a default flag or empty if country not found
            return "";
        }
        return "https://flagcdn.com/w320/" + countryCode + ".png";
    }

    private String getCountryCode(String areaName) {
        if (areaName == null) return "";

        switch (areaName.toLowerCase()) {
            case "algerian":
                return "dz";
            case "american":
                return "us";
            case "argentinian":
                return "ar";
            case "australian":
                return "au";
            case "british":
                return "gb";
            case "canadian":
                return "ca";
            case "chinese":
                return "cn";
            case "croatian":
                return "hr";
            case "dutch":
                return "nl";
            case "egyptian":
                return "eg";
            case "filipino":
                return "ph";
            case "french":
                return "fr";
            case "greek":
                return "gr";
            case "indian":
                return "in";
            case "irish":
                return "ie";
            case "italian":
                return "it";
            case "jamaican":
                return "jm";
            case "japanese":
                return "jp";
            case "kenyan":
                return "ke";
            case "malaysian":
                return "my";
            case "mexican":
                return "mx";
            case "moroccan":
                return "ma";
            case "norwegian":
                return "no";
            case "polish":
                return "pl";
            case "portuguese":
                return "pt";
            case "russian":
                return "ru";
            case "saudi arabian":
                return "sa";
            case "slovakian":
                return "sk";
            case "spanish":
                return "es";
            case "syrian":
                return "sy";
            case "thai":
                return "th";
            case "tunisian":
                return "tn";
            case "turkish":
                return "tr";
            case "ukrainian":
                return "ua";
            case "uruguayan":
                return "uy";
            case "venezulan":
            case "venezuelan":
                return "ve";
            case "vietnamese":
                return "vn";
            default:
                return "";
        }
    }
}


