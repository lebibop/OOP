package oop.AppClasses;

import oop.Model.Room;

public class app_room {
    public static void main(String[] args) {
        Room st = new Room();

        st.setId_room(1);
        st.setNumber(134);
        st.setStatus("Free");
        st.setCapacity(3);
        st.setPrice(3500);

        System.out.println(st.toString());
    }
}
