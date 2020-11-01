// Source: source: https://www.aliexpresscode.com/2020/04/javafx-scene-builder-load-and-adding.html

package sample;
import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author amir
 */
public class mySQLConnect extends Dictionary {

    /*//Connection conn = null;
    public static Connection ConnectDb() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/btl", "root", "YES");
            //JOptionPane.showMessageDialog(null, "Connection Established");
            return conn;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return null;
        }

    }

    public Vector<Word> insertFromFile() throws FileNotFoundException {

        Connection conn = ConnectDb();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM tbl_edict");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                words.add(new Word(rs.getString("word"), rs.getString("detail")));
            }
        } catch (Exception e) {

        }

        return words;
    }*/

    //Connection conn = null;
    public static Connection ConnectDb(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/test12345","root","");
            //JOptionPane.showMessageDialog(null, "Connection Established");
            return conn;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return null;
        }

    }

   /* public static ObservableList<Word> getDataWord(){
        Connection conn = ConnectDb();
        ObservableList<Word> list = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM `tbl_edict`");
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                //  Edit();
                int value1 = Integer.parseInt(rs.getString("idx"));
                String value2 = rs.getString("word");
                String value3 = rs.getString("detail");
                value3 =value3.substring(14,value3.length());
                value3 =value3.substring(0,value3.length()-20);
                String[] arrOfStr = value3.split("<br />",6);
                String res = "";
                for (String a : arrOfStr) res = res +"\n"+ a;
                value3 = res;
                value3 = value3.substring(3, value3.length());
                // list.add(new Word(Integer.parseInt(rs.getString("idx")), rs.getString("word"), rs.getString("detail")));
                list.add(new Word(value1,value2,value3));
            }
        } catch (Exception e) {
        }
        return list;
    }*/


    public void insertFromMySQL(Dictionary Dict) {
        Connection conn = ConnectDb();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM `tbl_edict`");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String value = rs.getString("detail");
                value = value.substring(16, value.length());
                value = value.substring(0, value.length() - 20);
                String[] words1 = value.split("<br />");
                value = "";
                for (String word : words1) {
                    value = value + '\n' + word;

                }

                Word temp = new Word(rs.getString("word"), value);
                //Dict.getDictionary(temp);
            }
        } catch (Exception e) {
        }

    }

    public  mySQLConnect() {

    }

}
