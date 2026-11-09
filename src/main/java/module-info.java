module org.uabc2109545.juegofarkle_p6 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.UABC.farkle to javafx.fxml;
    exports org.UABC.farkle;
}