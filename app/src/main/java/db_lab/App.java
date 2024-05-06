package db_lab;

import db_lab.data.DAOException;
import db_lab.data.DAOUtils;
import db_lab.model.Model;
import java.sql.SQLException;

public final class App {

    public static void main(String[] args) throws SQLException {
        // If you want to get a feel of the application before having implemented
        // all methods, you can pass the controller a mocked model instead:
        //
        // var model = Model.mock();
        var connection = DAOUtils.localMySQLConnection("tessiland", "root", "");
        var model = Model.fromConnection(connection);
        var view = new View(() -> {
            // We want to make sure we close the connection when we're done
            // with our application.
            try {
                connection.close();
            } catch (Exception e) {}
        });
        var controller = new Controller(model, view);
        view.setController(controller);
        controller.userRequestedInitialPage();
    }
}
