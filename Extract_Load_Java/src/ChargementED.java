import java.io.File;
import java.io.IOException;
import java.sql.*;


import jxl.*;
import jxl.read.biff.BiffException;


public class ChargementED {

    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur : Pilote JDBC SQLite introuvable.");
            return;
        }


        String urlSQLite = "jdbc:sqlite:comptoir.db";
        String urlOracle = "jdbc:oracle:thin:@localhost:1521/freepdb1";

        try (
                Connection connectionSQLite = DriverManager.getConnection(urlSQLite, "", "");
                Connection connectionOracle = DriverManager.getConnection(urlOracle, "TP", "TP");

                Statement statementSQLite = connectionSQLite.createStatement();
                ResultSet resultSetSQLite = statementSQLite.executeQuery("SELECT p.RefProduit, p.NomDuProduit, c.NomDeCategorie, p.Prixunitaire, p.UnitesEnStock, p.UnitesCommandees FROM Produits p JOIN Categories c ON p.CodeCategorie = c.CodeCategorie");
                PreparedStatement preparedStatement = connectionOracle.prepareStatement(
                        "INSERT INTO PRODUIT (REFERENCE, NOM_PRODUIT, NOM_CATEGORIE, PRIXUNITAIRE, UNITESENSTOCK, UNITESCOMANDEES) VALUES (?,?,?,?,?,?)")
        ) {
            while (resultSetSQLite.next()) {
                // Extraction des données de la table Produits
                String reference = resultSetSQLite.getString("RefProduit");
                String nomDuProduit = resultSetSQLite.getString("NomDuProduit");
                String nomDeCategorie = resultSetSQLite.getString("NomDeCategorie");
                double prixUnitaire = resultSetSQLite.getDouble("Prixunitaire");
                int unitesEnStock = resultSetSQLite.getInt("UnitesEnStock");
                int unitesCommandees = resultSetSQLite.getInt("UnitesCommandees");


                // Insertion des données dans la table PRODUIT de l'entrepôt
                preparedStatement.setString(1, reference);
                preparedStatement.setString(2, nomDuProduit);
                preparedStatement.setString(3, nomDeCategorie);
                preparedStatement.setDouble(4, prixUnitaire);
                preparedStatement.setInt(5, unitesEnStock);
                preparedStatement.setInt(6, unitesCommandees);

                // Exécution de l'instruction d'insertion
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            // Gestion de l'erreur
            System.err.println("Une erreur s'est produite lors du chargement de la base SQLite: " + e.getMessage());
        }


        /**
         * CHARGER LES DONNEES DE LA TABLE CLIENT
         */
        try {
            Workbook fichier = Workbook.getWorkbook(new File("C:\\M1_S2\\IDBD\\TP1\\TP_DATA\\IDBD\\Clients.xls"));
            Sheet feuille = fichier.getSheet(0);

            String insertClientSQL = "INSERT INTO CLIENT (CODE_CLIENT, CONTACT, FONCTION) VALUES (?, ?, ?)";
            Connection connectionOracle = DriverManager.getConnection(urlOracle, "TP", "TP");
            PreparedStatement preparedStatementOracle = connectionOracle.prepareStatement(insertClientSQL);

            for(int ligne=1; ligne < feuille.getRows(); ligne++){
                String codeClient = feuille.getCell(0, ligne).getContents();
                String contact = feuille.getCell(1, ligne).getContents();
                String fonction = feuille.getCell(2,ligne).getContents();


                // Insertion des données dans la table CLIENT de l'entrepôt Oracle
                preparedStatementOracle.setString(1, codeClient);
                preparedStatementOracle.setString(2, contact);
                preparedStatementOracle.setString(3, fonction);


                // Exécution de l'instruction d'instruction
                preparedStatementOracle.executeUpdate();
            }
            fichier.close();
        }
        catch (SQLException | IOException | BiffException e){
            System.err.println("Erreur s'est produite lors de la manipulation d'Oracle");
        }
    }
}
