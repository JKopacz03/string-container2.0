package org.kopacz;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        StringContainer st = new StringContainer("\\d{2}[-]\\d{3}", true);

        st.add("00-000");
        TimeUnit.SECONDS.sleep(1);
        st.add("00-001");
        TimeUnit.SECONDS.sleep(1);
        st.add("00-002");
        TimeUnit.SECONDS.sleep(1);
        st.add("00-003");
        TimeUnit.SECONDS.sleep(1);
        st.add("00-004");



//        st.storeToFile("postalcodes.txt");
//
//        StringContainer fromFile = st.fromFile("postalcodes.txt");

//        System.out.println(fromFile);

//        for(int i=0; i<st.size(); i++){
//            System.out.println(st.get(i)); //powinno wypisac dodane kody pocztowe
//        }

        LocalDateTime zero = st.get("00-000").getAddTime();
        LocalDateTime one = st.get("00-001").getAddTime();
        LocalDateTime two = st.get("00-002").getAddTime();
        LocalDateTime three = st.get("00-003").getAddTime();
        System.out.println(zero);
        System.out.println(one);
        System.out.println(two);
        System.out.println(three);

        StringContainer dataBetween = st.getDataBetween(st.get("00-001").getAddTime(),
                st.get("00-002").getAddTime());
        dataBetween.show();


    }
}