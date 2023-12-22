package org.kopacz;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {

        StringContainer st = new StringContainer("\\d{2}[-]\\d{3}", true);

        st.add("00-000");
        st.add("00-001");
        st.add("00-002");
        st.add("00-003");
        st.add("00-004");

//        for(int i=0; i<st.size(); i++){
//            System.out.println(st.get(i)); //powinno wypisac dodane kody pocztowe
//        }

        StringContainer dataBetween = st.getDataBetween(LocalDateTime.of(2023,12,21,11,11,11),
                null);
        dataBetween.show();
//        st.show();

//        String string = st.get(1);
//        String string2 = st.get("tymek3");
//        System.out.println(string);
//        System.out.println(string2);
//        st.remove(2);
//        st.remove("tymek0");
//        st.show();

    }
}