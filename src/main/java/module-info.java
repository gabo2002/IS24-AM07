open module it.polimi.ingsw.am07 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.squareup.moshi;

    requires org.controlsfx.controls;
    requires org.jline;
    requires annotations;
    requires java.rmi;
    requires java.logging;
    requires java.desktop;
    requires moshi.records.reflect;

    exports it.polimi.ingsw.am07;
    exports it.polimi.ingsw.am07.client.gui.viewController;
}