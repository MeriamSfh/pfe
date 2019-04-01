package com.exalead.customcode.search;

public class Rot13Utils {
    public static char rot13(char i) {
        if (i >= 'a' && i <= 'm' || i >= 'A' && i <= 'M')
            return (char)(i+13);
        if (i >= 'n' && i <= 'z' || i >= 'N' && i <='Z')
            return (char)(i-13);
        else
            return i;
    }
    public static String rot13(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i=0; i < s.length(); i++){
            sb.append(rot13(s.charAt(i)));
        }
        return sb.toString();
    }
}
