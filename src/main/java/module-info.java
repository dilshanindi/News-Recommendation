module com.example.newsrec {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires java.sql;


    opens com.example.newsrec to javafx.fxml;
    exports com.example.newsrec;
}