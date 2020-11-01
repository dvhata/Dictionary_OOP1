package sample;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import java.util.Vector;

public class DictionaryManagement extends Dictionary {

    public Vector<Word> insertFromDatabase() throws FileNotFoundException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            String dbURL = "jdbc:mysql://localhost/btl";
            String username = "root";
            String password = "";
            connection = DriverManager.getConnection(dbURL, username, password);
            if (connection != null) {
                System.out.println("Connect successfully");
            }

            // Cau lenh xem du lieu
            String sql = "SELECT * FROM tbl_edict";

            // Tao doi tuong thuc thi cau lenh
            assert connection != null;
            statement = connection.createStatement();

            // Thuc thi
            resultSet = statement.executeQuery(sql);

            // Neu danh sach hong ton tai
            if (!resultSet.isBeforeFirst()) {
                JOptionPane.showMessageDialog(null, "No data");
                return null;
            }
            // Trong khi chưa hết dữ liệu
            try {
                while (resultSet.next()) {
                    StringBuilder meaning = new StringBuilder(resultSet.getString("detail"));
                    meaning = new StringBuilder(meaning.substring(16, meaning.length()));
                    meaning = new StringBuilder(meaning.substring(0, meaning.length() - 20));
                    String[] words1 = meaning.toString().split("<br />");
                    meaning = new StringBuilder();
                    for (String word : words1) {
                        meaning.append('\n').append(word);
                    }
                    for (int i = 2; i < words1.length; ++i) {
                        meaning.append('\n').append(words1[i]);
                    }
                    Word temp = new Word(resultSet.getString("word"), meaning.toString());
                    words.add(temp);
                }
            } catch (Exception e) {
                System.out.println("FAIL RESULT SET");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("FAIL INSERT FROM DATABASE");
        }

        return words;
    }

    /**
     * public Vector<Word> insertFromFile() throws IOException {
     * File myFile = new File("D:\\Code\\OOP\\Dictionary\\src\\sample\\dictionaries1.txt");
     * Scanner sc = new Scanner(myFile);
     * while (sc.hasNextLine()) {
     * String target;
     * String explain;
     * Word temp;
     * target = sc.next();
     * explain = sc.nextLine();
     * <p>
     * temp = new Word(target, explain);
     * words.add(temp);
     * }
     * sc.close();
     * return words;
     * }
     * <p>
     * /**
     * Tra cuu tu dien.
     */
    public void DictionaryLookup(Dictionary x) {
        try {
            System.out.println("Nhap tu ban muon tra: ");
            String lookUpWord;
            Scanner sc = new Scanner(System.in);
            lookUpWord = sc.next();
            for (Word word : words) {
                if (lookUpWord.equals(word.getWord_target())) {
                    System.out.println(word.getWord_explain());
                }
            }
        } catch (NullPointerException e) {
            System.out.println("ERROR Dictionary Lookup");
        }
    }

    public void deleteWords(String s) {
        try {
            for (int i = 0; i < words.size(); i++) {
                if (s.equals(words.get(i).getWord_target())) {
                    words.remove(i);
                    break;
                }
            }
        } catch (NullPointerException e) {
            System.out.println("ERROR Dictionary Lookup");
        }
    }

    public void addWords() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Them tu: ");
        System.out.println("Tu tieng anh: ");
        String s1 = sc.next();
        System.out.println("Nghia: ");
        String s2 = sc.next();
        Word temp = new Word(s1, s2);
        words.add(temp);
    }

    public void editWords() {
        System.out.println("Ban muon thay doi: ");
        System.out.println("1. Tu vung");
        System.out.println("2. Nghia");
        Scanner sc = new Scanner(System.in);
        int select = sc.nextInt();
        if (select == 1) {
            System.out.println("Nhap tu muon sua: ");
            String s1 = sc.next();
            for (Word word : words) {
                if (word.getWord_target().equals(s1)) {
                    word.setWord_target(s1);
                }
            }
        }
        if (select == 2) {
            System.out.println("Nhap nghia muon sua: ");
            String s2 = sc.next();
            for (Word word : words) {
                if (word.getWord_explain().equals(s2)) {
                    word.setWord_explain(s2);
                }
            }
        }
    }

    /**
     public void exportToFile() throws IOException {
     File myFile = new File("D:\\Code\\OOP\\Dictionary\\DictionaryListWords.txt");
     OutputStream os = new FileOutputStream(myFile);
     OutputStreamWriter osw = new OutputStreamWriter(os);
     for (Word word : words) {
     osw.write(word.getWord_target() + "      "
     + word.getWord_explain());
     }
     osw.flush();
     }
     */
}
