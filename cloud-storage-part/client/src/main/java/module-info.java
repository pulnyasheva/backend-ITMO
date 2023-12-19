module com.module.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires io.netty.transport;
    requires io.netty.codec;
    requires io.netty.buffer;
    requires org.common;

    opens com.module.client to javafx.fxml;
    exports com.module.client;
}
