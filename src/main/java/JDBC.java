import java.sql.*;

public class JDBC {
    /*
type             jdbc:mysql
host/ip          195.35.59.18
port             3306
database_name      u201212290_qaloantec    //database ile alakalı veriler bilgiler


username          u201212290_qaloanuser  // databaseye giriş yapmak için verilen bilgiler
password          HPo?+7r$
     */


    // Ham haldeki bu bilgiler ile JDBC baglantisi kurmak zordur.
    // O yuzden bu datalari analsilir bir URL haline getirmek zorundayiz.


    /*
    URL: jdbc:mysql://195.35.59.18/u201212290_qaloantec
    USERNAME: u201212290_qaloanuser
    PASSWORD: HPo?+7r$
     */
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        // 1. adım : JDBC surucusunu yukle
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 2.adım : Veritabanı baglantısı kurma (connectıon olusturma)
        String URL= "jdbc:mysql://195.35.59.18/u201212290_qaloantec";
        String userName="u201212290_qaloanuser";
        String password="HPo?+7r$";

       Connection connection = DriverManager.getConnection(URL,userName,password);

       //3. adım : SQl Sorguları olusturma (Query Hazırlama)
        String Query= "SELECT * FROM users";

       // 4. adım : SQL sorgularını calıstırma       //sesnsitive insensitive degısıklıklewre duyarlı olup olmaması ile alakalıdır
       Statement statement= connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY); //connctiıon icindekileri yazmasak da calısır
        ResultSet resultSet= statement.executeQuery(Query);

        //5. adım : sonucları isleme
        // Database den donen sonuclar resultset icerisinde
        // Resultset içerisinde "ITEREATOR" ile islem yapabilirim

        resultSet.next();
        System.out.println(resultSet.getString("firstname")); //beklenti : 'Elf'

        resultSet.next();
        System.out.println(resultSet.getString(4)); // beklenti:Elf / tester

        resultSet.next();
        resultSet.next();
        System.out.println(resultSet.getInt(8)); //beklent: Elf / tester / 85462

        resultSet.absolute(15);
        System.out.println(resultSet.getString(6));  //beklentiElf /tester / 85462 /bidasa9700@xcmexico.com

        //NOTTT:İMLEC NEREDEYSE ORDAN SONRASI SAYIYA DAHİL (en sorguyu nerede yaptıysak ıterator orada kalır)
        //satırlar arası geri gitmek istersek PREVİOUS KULLANIRIZ

        resultSet.previous();
        System.out.println(resultSet.getString(2)); //beklentiElf /tester / 85462 /bidasa9700@xcmexico.com / loan

        resultSet.first(); // fırst kullanarak ilk satıra gidebiliriz
        System.out.println(resultSet.getString(3));
        System.out.println(resultSet.getString(4)); // beklenti 'acenk'  not: aynı satırdayken tekrar fırst yazmamıza gferek yok


        // resultSet.beforeFirst(); // sorguyu nerede yaptıysak ıterator orada kalır,
        // bu methot kursörün en basa gelmesini sağlar bunu her işlem sonrası kullanırsak kursor basa gelir
        // resultset.isBeforeFirst();  kursorun 1. satırda olmasını sorgulatır true ve false doner
        // resultSet.afterLast(); // kursoru datanın en sonuna atar,
        // sonradan eklenenleri sondan sorgulayuabilmek için kullanılabilir.
        // resultSet.isAfterLast(); bununla da sorgulamasını yapabiliriz



    }


}
