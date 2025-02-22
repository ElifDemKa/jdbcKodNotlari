package utilities;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCReusableMethods {

    public static Connection connection;
    public static Statement statement;
    public static ResultSet resultSet;


    public static void createConnection(){

        String URL = ConfigReader.getProperty("URL");
        String USERNAME = ConfigReader.getProperty("USERNAME");
        String PASSWORD = ConfigReader.getProperty("PASSWORD");

        try {
            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // bu method bir connection create eder. configuration propertiese eklemiş olduğumuz datalara göre
    // bize bir connection create eder ve yukarıdaki connection içerisine kaydeder
    // sadece olusturur ve public static olduğu için bütün methodlar bunu görür herhangi bir return u yok

//------------------------------------------------------------------------------------------------------------------------


    public static Connection getConnection(){

        String URL = ConfigReader.getProperty("URL");
        String USERNAME = ConfigReader.getProperty("USERNAME");
        String PASSWORD = ConfigReader.getProperty("PASSWORD");

        try {
            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return connection;
    }
    // eger test yaparken bir yerde connection görmek ve getirmek istiyorsak bu methodu kullanabiliriz
    // bu da yine aynı yerden verileri ceker ve class seviyesindeki connectiona kaydeder ve
    // aynı zamanda bunu bize döndürür
    // NOT: EGER SADECE GÖRMEK İSTEMİYORSAN  SADECE CALISSIN İSTİYORSAN CREATE CONNECTİON
    // AMA CALISTIĞIN YERDE GÖRMEK İSTİYORSAN GETCONNETİONU KULLANABİLİRSİN

    //------------------------------------------------------------------------------------------------------------------------


    public static Statement getStatement(){
        createConnection();
        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return statement;
    }
    // Yine sadece statement almak istiyorsan
    // yukarıda olusturulan connectionu alıyorsun o connectıonun uzerine bir statemen create edip
    // o statementı buraya atıyoruz ve o statmenti bize döndürür
    //connection yoksa statement alamayız ve connectionun içi bos olmamalı hata alırız

//-----------------------------------------------------------------------------------------------------------------

    public static ResultSet executeQuery(String Query){

        createConnection();
        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            resultSet = statement.executeQuery(Query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resultSet;
    }
    //queryi çalıstırmak istiyorum select sorgusu calıstırmak istiyorum
    //select sorgusunda  queryi parametre olarak gönderdiğim zaman bı methods calısır
    //Önce connection create edecek yukarıda , sonra bir satatement olusturucak connection üzerine
    //sonra o connectionu bir resulktsete atacak ve resultseti de bana getirecek
    //yani aslında bütün select sorgularını bu methodla cozebiliriz
    //result seti elimize alır istedğimiz gibi oynarız.


//--------------------------------------------------------------------------------------------------------------

    public static int updateQuery(String query) {
        getStatement();
        int affectedRows;

        try {
            affectedRows = statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Etkilenen satir sayisi: "+affectedRows);
        return affectedRows;
    }
    //bir update query calsıtıracaksak bişr get statementi cagırırız
    //sonra o statemen uzerine execute update calsıtırırz ve
    // parametre olarak gelen queriy bunu  iççine koyarız
    // bu bize etkilenen satır sayısını int olarak döner
    //butun update querylerini bu methodla cözebilirz

//----------------------------------------------------------------------------------------------------------



    public static void closeConnection(){

        if(resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (statement != null){
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


    }
    //Close connection ile önce resultset, sonra statement sonra da connectionu kapatırtız
    // null değilse kapatırız


//----------------------------------------------------------------------------------------------------------


    //getrowcount yani benim resultset içinde kac satırım var
    // bunu öğrenmek için bu methodu çalıstırabiliriz

    public static int getRowCount() throws Exception {
        resultSet.last();
        int rowCount = resultSet.getRow();
        return rowCount;
    }


//------------------------------------------------------------------------------------------------------

    //getrowmap satırları map a atarsın , bir string ve objecten olusan map e döküyorsun

    public static Map<String, Object> getRowMap(String query) {
        return getQueryResultMap(query).get(0);
    }


//-----------------------------------------------------------------------------------------------------

    //getrowlist bir listin içerisine ve listin icerisindeki satırlara gidiyorsun
    public static List<Object> getRowList(String query, int row) {

        return getQueryResultList(query).get(row);
    }
//---------------------------------------------------------------------------------------------
    //getcellvalue örneğın 3. stırın 2.columndaki data
    // burda da int row int columg girersin ordaki datayı ceker sana getirir

    public static Object getCellValue(String query, int row, int column) {

        return getQueryResultList(query).get(row).get(column);
    }
//-----------------------------------------------------------------------------------

    //getquerylist resultsetin tamamını bir liste atmak istiyorsan
    //bu methodu kullanabilirsin

    public static List<List<Object>> getQueryResultList(String query) {
        executeQuery(query);
        List<List<Object>> rowList = new ArrayList<>();
        ResultSetMetaData rsmd;
        try {
            rsmd = resultSet.getMetaData();
            while (resultSet.next()) {
                List<Object> row = new ArrayList<>();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    row.add(resultSet.getObject(i));
                }
                rowList.add(row);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rowList;
    }
//-------------------------------------------------------------------------------------
    //getcolumndata yani kac tane column var
    //reult setin içerisindeki column isimlerini ogreniriz
    // ve ardından asagıdaki methodla map e dokeriz

    public static List<Object> getColumnData(String query, String column) {
        executeQuery(query);
        List<Object> rowList = new ArrayList<>();
        ResultSetMetaData rsmd;
        try {
            rsmd = resultSet.getMetaData();
            while (resultSet.next()) {
                rowList.add(resultSet.getObject(column));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rowList;
    }

    public static List<Map<String, Object>> getQueryResultMap(String query) {
        executeQuery(query);
        List<Map<String, Object>> rowList = new ArrayList<>();
        ResultSetMetaData rsmd;
        try {
            rsmd = resultSet.getMetaData();
            while (resultSet.next()) {
                Map<String, Object> colNameValueMap = new HashMap<>();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    colNameValueMap.put(rsmd.getColumnName(i), resultSet.getObject(i));
                }
                rowList.add(colNameValueMap);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rowList;
    }

//-------------------------------------------------------------------------------------------------------------
    //getcolumnNmes columların ısımlerini ogrenmek için

    public static List<String> getColumnNames(String query) {
        executeQuery(query);
        List<String> columns = new ArrayList<>();
        ResultSetMetaData rsmd;
        try {
            rsmd = resultSet.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                columns.add(rsmd.getColumnName(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columns;
    }
//-----------------------------------------------------------------------------------------
    //result setin tamamını yazdırmak için bu methodu kullanırız

    public static void printResultSet(ResultSet resultSet) {
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(metaData.getColumnName(i) + ": " + resultSet.getString(i) + " ");
                }
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println("ResultSet yazdırılırken bir hata oluştu: " + e.getMessage());
        }
    }
}
