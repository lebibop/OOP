package oop.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginController implements Initializable {

    private static final Logger log = LoggerFactory.getLogger("Login logger");
    public Button LoginButton;

    @FXML
    private Label invalid_label;

    @FXML
    private TextField username_box;

    @FXML
    private TextField password_box;

    @FXML
    private void LoginButtonAction(ActionEvent event) throws IOException
    {
        log.debug("Button pressed");

        Parent MainParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/MainItems.fxml")));
        Scene MainScene = new Scene(MainParent);

        Stage MainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        MainStage.setTitle("Hotel");

        if (/*isValidCredentials()*/true)
        {
            log.debug("password verification was successful. switching to a new screen");
            MainStage.setScene(MainScene);
            MainStage.show();
        }
        else
        {
            log.debug("password verification was unsuccessful");
            username_box.clear();
            password_box.clear();
            invalid_label.setText("Sorry, invalid credentials");
        }
    }
    private boolean isValidCredentials()
    {
        boolean LetIn = false;
        Connection Connection = null;
        java.sql.Statement Statement = null;
        try {
            log.debug("password verification");
            Connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/LoginDB", "root", "1234");
            Connection.setAutoCommit(false);
            Statement = Connection.createStatement();

            ResultSet Result = Statement.executeQuery( "SELECT * FROM Users WHERE UserName= " + "'" + username_box.getText() + "'"
                    + " AND Password= " + "'" + password_box.getText() + "'");

            while ( Result.next() ) {
                if (Result.getString("UserName") != null && Result.getString("Password") != null) {
                    LetIn = true;
                }
            }
            Result.close();
            Statement.close();
            Connection.close();
        }
        catch ( Exception e )
        {
            log.warn("Exception " + e);
            System.exit(0);
        }
        return LetIn;

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}