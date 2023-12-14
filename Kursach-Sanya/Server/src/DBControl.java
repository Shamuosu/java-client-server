import java.sql.*;
import java.util.logging.Level;
public class DBControl {

    public DBControl() {
    }
    public static String fetchStorage() {
        String response = "";
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/bulk",
                    "root", "qwerty");

            Statement st;
            st = con.createStatement();

            ResultSet rs;
            rs = st.executeQuery("select * from storage");

            while (rs.next()) {
                int id = rs.getInt("id");
                String item = rs.getString("item").trim();
                int price = rs.getInt("price");
                response += id + ";" + item + ";" + price + ";";
                System.out.println(response);
            }
            rs.close();
            st.close();
            con.close();

        } catch (SQLException e) {
            Server.LOG.log(Level.WARNING, "DATABASE RETRIEVAL ERROR: " + e);
        }
        Server.LOG.log(Level.INFO, "PRICE RETRIEVED");
        return response;
    }

    public static String fetchPrice() {
        String response = "";
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/bulk",
                    "root", "qwerty");

            Statement st;
            st = con.createStatement();

            ResultSet rs;
            rs = st.executeQuery("select * from orders");

            while (rs.next()) {
                int id = rs.getInt("id");
                String item = rs.getString("item").trim();
                int quantity = rs.getInt("quantity");
                int total = rs.getInt("totalprice");
                String status = rs.getString("status").trim();

                response += id + ";" + item + ";" + quantity + ";" + total + ";" + status + ";";
                System.out.println(response);
            }
            rs.close();
            st.close();
            con.close();

        } catch (SQLException e) {
            Server.LOG.log(Level.WARNING, "DATABASE RETRIEVAL ERROR: " + e);
        }
        Server.LOG.log(Level.INFO, "ORDERS RETRIEVED");
        return response;
    }

    public static String addUser(String userName, String password, String status) {
        String response = "";
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/bulk",
                    "root", "qwerty");

            Statement st;
            st = con.createStatement();
            ResultSet rs;
            rs = st.executeQuery("select * from users");
            while (rs.next()) {
                String dbName = rs.getString("name").trim();
                String dbPassword = rs.getString("pass").trim();
                String dbStatus = rs.getString("status").trim();
                if ((dbName.equals(userName)) && (dbPassword.equals(password)) && (dbStatus.equals(status))) {
                    response = status;
                }
            }
            if (!response.isEmpty()) {
                response = "UsrExst;" + response + ";";
            } else {
                String sql = "insert into users (name, pass, status) values (?, ?, ?)";
                PreparedStatement pst;
                pst = con.prepareStatement(sql);

                pst.setString(1, userName);
                pst.setString(2, password);
                pst.setString(3, status);
                pst.executeUpdate();
                response = "UsrAdd;" + status + ";";
                pst.close();
            }
            con.close();
        }catch (SQLException e){
            Server.LOG.log(Level.WARNING, "DATABASE ADDITION ERROR: " + e);
        }
        return response;
    }

    public static String checkUser(String login, String password, String mode) {
        String response = "";
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/bulk",
                    "root", "qwerty");


            Statement st;
            st = con.createStatement();
            ResultSet rs;
            rs = st.executeQuery("select * from users");

            while (rs.next()) {
                String dbName = rs.getString("name").trim();
                String dbPassword = rs.getString("pass").trim();
                if ((dbName.equals(login)) && (dbPassword.equals(password))) {
                    response = "ChkSuc;" + mode + ";";
                }
            }
            rs.close();
            st.close();
            con.close();

        } catch (SQLException e) {
            Server.LOG.log(Level.WARNING, "DATABASE SEARCH ERROR: " + e);
        }
        if (response.isEmpty()){
            response = "NoUsr;" + mode + ";";
        }
        return response;
    }

    public static String addStorage(String[] clientData){
        String response = null;
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/bulk",
                    "root", "qwerty");

            Statement st;
            st = con.createStatement();

            ResultSet rs;
            rs = st.executeQuery("select * from storage");

            String sqlAdd = "";
            int controlVal = 0;

            while (rs.next()) {
                String dbID = rs.getString("id").trim();
                if ((dbID.equals(clientData[0]))) {
                    sqlAdd = "update storage set item = ?, price = ? where id = ?";
                    controlVal = 1;
                }
            }

            if (sqlAdd.isEmpty()) {
                sqlAdd = "insert into storage (id, item, price) values (?, ?, ?)";
                controlVal = 2;
            }

            PreparedStatement pst;
            pst = con.prepareStatement(sqlAdd);

            if (controlVal == 1) {
                pst.setString(1, clientData[1]);
                pst.setInt(2, Integer.parseInt(clientData[2]));
                pst.setInt(3, Integer.parseInt(clientData[0]));
            } else if (controlVal == 2) {
                pst.setInt(1, Integer.parseInt(clientData[0]));
                pst.setString(2, clientData[1]);
                pst.setInt(3, Integer.parseInt(clientData[2]));
            }
            response = "ADDED";
            pst.executeUpdate();
            pst.close();
            rs.close();
            st.close();
            con.close();
        } catch (SQLException e) {
            Server.LOG.log(Level.WARNING, "ADDITION ERROR: " + e);
        }
        return response;
    }

    public static String updatePrice(String[] clientData){
        String response = null;
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/bulk",
                    "root", "qwerty");

            Statement st;
            st = con.createStatement();

            ResultSet rs;
            rs = st.executeQuery("select * from orders");

            String sqlAdd = "";
            int controlVal = 0;

            while (rs.next()) {
                String dbID = rs.getString("id").trim();
                if ((dbID.equals(clientData[0]))) {
                    sqlAdd = "update orders set item = ?, quantity = ?, totalprice = ?, status = ? where id = ?";
                    controlVal = 1;
                }
            }

            if (sqlAdd.isEmpty()) {
                sqlAdd = "insert into orders (id, item, quantity, totalprice, status) values (?, ?, ?, ?, ?)";
                controlVal = 2;
            }

            PreparedStatement pst;

            System.out.println("controlVal: " + controlVal);
            if (controlVal == 1){
                pst = con.prepareStatement(sqlAdd);
                pst.setString(1, clientData[1]);
                pst.setInt(2, Integer.parseInt(clientData[2]));
                pst.setInt(3, Integer.parseInt(clientData[3]));
                pst.setString(4, clientData[4]);
                pst.setInt(5, Integer.parseInt(clientData[0]));
                pst.executeUpdate();
                pst.close();
                response = "DATA " + clientData[0] + "STATUS UPDATED: " + clientData[1];
            }else if (controlVal == 2){
                pst = con.prepareStatement(sqlAdd);
                pst.setInt(1, Integer.parseInt(clientData[0]));
                pst.setString(2, clientData[1]);
                pst.setInt(3, Integer.parseInt(clientData[2]));
                pst.setInt(4, Integer.parseInt(clientData[3]));
                pst.setString(5, clientData[4]);

                pst.executeUpdate();
                pst.close();
                response = "ADDED: " + clientData[1];
            }

            rs.close();
            st.close();
            con.close();
            Server.LOG.log(Level.INFO, response);

        } catch (SQLException e) {
            Server.LOG.log(Level.WARNING, "UPDATE ERROR: " + e);
        }
        return response;
    }
}
