import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MiseAJourED {
    public static void main(String[] args) {
        String urlSQLite = "jdbc:sqlite:comptoir.db";
        String urlOracle = "jdbc:oracle:thin:@localhost:1521/freepdb1";

        try {
            Connection connectionSQLite = DriverManager.getConnection(urlSQLite);

            String selectSQLiteQuery = "SELECT p.RefProduit, p.NomDuProduit, c.NomDeCategorie, p.Prixunitaire, p.UnitesEnStock, p.UnitesCommandees FROM Produits p JOIN Categories c ON p.CodeCategorie = c.CodeCategorie";
            PreparedStatement selectSQLiteStatement = connectionSQLite.prepareStatement(selectSQLiteQuery);
            ResultSet produitsSQLiteResultSet = selectSQLiteStatement.executeQuery();

            Connection connectionOracle = DriverManager.getConnection(urlOracle, "TP", "TP");

            String insertQuery = "INSERT INTO PRODUIT (REFERENCE, NOM_PRODUIT, NOM_CATEGORIE, PRIXUNITAIRE, UNITESENSTOCK, UNITESCOMANDEES) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStatement = connectionOracle.prepareStatement(insertQuery);

            while (produitsSQLiteResultSet.next()) {
                String reference = produitsSQLiteResultSet.getString("RefProduit");
                String nomDuProduit = produitsSQLiteResultSet.getString("NomDuProduit");
                String nomDeCategorie = produitsSQLiteResultSet.getString("NomDeCategorie");
                double prixUnitaire = produitsSQLiteResultSet.getDouble("Prixunitaire");
                int unitesEnStock = produitsSQLiteResultSet.getInt("UnitesEnStock");
                int unitesCommandees = produitsSQLiteResultSet.getInt("UnitesCommandees");

                String checkQuery = "SELECT * FROM PRODUIT WHERE REFERENCE = ?";
                PreparedStatement checkStatement = connectionOracle.prepareStatement(checkQuery);
                checkStatement.setString(1, reference);
                ResultSet existingProductResultSet = checkStatement.executeQuery();

                if (!existingProductResultSet.next()) {
                    insertStatement.setString(1, reference);
                    insertStatement.setString(2, nomDuProduit);
                    insertStatement.setString(3, nomDeCategorie);
                    insertStatement.setDouble(4, prixUnitaire);
                    insertStatement.setInt(5, unitesEnStock);
                    insertStatement.setInt(6, unitesCommandees);
                    insertStatement.executeUpdate();
                }

                existingProductResultSet.close();
                checkStatement.close();
            }

            produitsSQLiteResultSet.close();
            selectSQLiteStatement.close();
            insertStatement.close();
            connectionSQLite.close();
            connectionOracle.close();

            System.out.println("Mise à jour de la dimension Produit effectuée avec succès.");
        } catch (SQLException e) {
            System.err.println("Une erreur s'est produite : " + e.getMessage());
        }
    }
}
