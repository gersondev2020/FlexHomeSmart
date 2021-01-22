package com.gautomation.flexhomesmart.Class;

public class UnidadesAut {
        public String Valor_padrao_unidade(String tag){
        if( (tag.contains("PT") ||  tag.contains("KG")) && !tag.contains("ABER_VAL")){
            return "Kgf/cm²";
        }else if(tag.contains("TT") || tag.contains("TEMPER")){
            return "°C";
        }else if((tag.contains("FT") || tag.contains("VZ") || tag.contains("VAZAO")) && (!tag.contains("CAL") || tag.contains("_CAL"))){
            return "m³/h";
        }else if(tag.contains("CAL") && (tag.contains("VZ") || tag.contains("VAZAO") || tag.contains("FT"))&& !tag.contains("_CAL")){
            return "tn/h";
        }else if(tag.contains("RPM")){
            return "RPM";
        }else if(tag.contains("PH") || tag.contains("NIVEL") || tag.contains("LT") || tag.contains("ABER_VAL")){
            return "%";
        }else if(tag.contains("POT") || tag.contains("EXPO")){
            return "KW";
        }else{
            return "Kgf/cm²";
        }

    }
}
