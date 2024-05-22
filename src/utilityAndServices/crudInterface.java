package utilityAndServices;
import java.util.*;
import java.sql.*;
public abstract class crudInterface <Type>  { // interfata template care creeaza structura functiilor CRUD pentru mai multe tipuri de obiecte
    public abstract String getObjectType();
    public abstract List<Object> getParametersValues();
    public abstract Type read();
    public void createJDBC(Connection connection) throws SQLException {
        String queryText = "insert into " + getObjectType();
        queryText = queryText + " values(" + "?, ".repeat((getParametersValues()).size() - 1);
        queryText = queryText + "?)";

        List<Object> parametersNames = getParametersValues();
        try(PreparedStatement pstmt = connection.prepareStatement(queryText)) {
            for (int i = 0; i < parametersNames.size(); i++) {
                //System.out.println(parametersNames.get(i));
                pstmt.setObject(i + 1, parametersNames.get(i));
            }
            pstmt.executeUpdate();
        }
        catch (SQLException se) {
            System.err.println(getObjectType() + " creation failed" + se.toString());
        }
    }
}

