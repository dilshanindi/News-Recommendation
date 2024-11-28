module com.example.newsrec {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;



    opens com.example.newsrec to javafx.fxml;
    exports com.example.newsrec;
}