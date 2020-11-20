package com.example.gonggong;
import android.provider.BaseColumns;
public class DataBase  {

    public static final class CreateDB implements BaseColumns{
        public static final String FACILITYNAME="facname";
        public static final String FACILITYADDRESS="facaddr";
        public static final String CODE="code";
        public static String latitude="latitude";
        public static String longitude="longitude";
        public static final String _TABLENAME="favoritetable";
        public static final String _CREATE="create table if not exists "+_TABLENAME+
                "("+_ID+" integer primary key autoincrement, "
                +FACILITYNAME+" text not null, "
                +FACILITYADDRESS+" text not null ,"
                +CODE+" integer not null , "
                +latitude+" double not null , "
                +longitude+" double not null );";
    }
}
