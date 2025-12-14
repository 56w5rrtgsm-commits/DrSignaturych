import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SignatureDB {

    private static final String URL = "jdbc:sqlite:./signatures.db";

    public SignatureDB() {
        init();
    }

    private void init() {
        try (Connection c = DriverManager.getConnection(URL);
             Statement s = c.createStatement()) {

            s.execute("""
                CREATE TABLE IF NOT EXISTS signatures (
                    name TEXT NOT NULL,
                    hex TEXT NOT NULL,
                    offset INTEGER NOT NULL
                )
            """);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addSignature(String name, String hex, long offset) {
        init();
        try (Connection c = DriverManager.getConnection(URL);
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO signatures (name, hex, offset) VALUES (?,?,?)"
             )) {

            ps.setString(1, name);
            ps.setString(2, hex);
            ps.setLong(3, offset);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Signature> getAll() {
        init();
        List<Signature> list = new ArrayList<>();
        try (Connection c = DriverManager.getConnection(URL);
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT * FROM signatures")) {

            while (rs.next()) {
                // вызов fromHex именно у Signature
                list.add(new Signature(
                        rs.getString("name"),
                        Signature.fromHex(rs.getString("hex")),
                        rs.getLong("offset")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
