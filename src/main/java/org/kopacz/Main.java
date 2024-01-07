package org.kopacz;

import java.io.*;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        StringContainer st = new StringContainer("\\d{2}[-]\\d{3}", true);

        st.add("00-000");
        TimeUnit.SECONDS.sleep(1); // dodałem bo inaczej java daje w niektorych te same localDateTime.now()
        // i metoda getDataBetween nawala :(
        st.add("00-001");
        TimeUnit.SECONDS.sleep(1);
        st.add("00-002");
        TimeUnit.SECONDS.sleep(1);
        st.add("00-003");
        TimeUnit.SECONDS.sleep(1);
        st.add("00-004");

        st.show();
        for(int i=0; i<st.size(); i++){
            System.out.println(st.get(i));
        }

        st.remove("00-003");
        st.remove(1);

        for(int i=0; i<st.size(); i++){
            System.out.println(st.get(i));
        }


        System.out.println(st.get("00-002").getAddTime());
//        System.out.println(st.get("00-003").getAddTime());
        System.out.println(st.get("00-004").getAddTime());

        StringContainer dataBetween = st.getDataBetween(null,
                null);
        dataBetween.show();

        st.storeToFile("postalCodes.txt");
        StringContainer fromFile = StringContainer.fromFile("postalCodes.txt");

    }
}