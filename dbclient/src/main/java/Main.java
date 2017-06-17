
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Main {
	
	public static final String username = "SA";
    public static final String password = "";
    public static final String urlConnection = "jdbc:hsqldb:hsql://127.0.0.1:9024/test-db";
    private static Connection connection;
    private static ResultSet resultset;
	public static void main(String[] args) throws InterruptedException, SQLException{		

		connection = DriverManager.getConnection(urlConnection,username,password);
		
		//createTableStudent();
	//	createTableEnrollment();
	//	createTableFaculty();
	//	createTableClass()
		
	//	addStudents();
	//	addFaculty();
	//	addClass();
	//	addEnrollment();
		
		 firstQuery();
		 secondQuery();
		 thirdQuery();
		 fourthQuery();
		 fifthQuery();
		 sixthQuery();
		 seventhQuery();
		 
	
}
	public static void createTableStudent() throws SQLException, ClassNotFoundException{
		connection.prepareStatement("CREATE TABLE Student ("
			+ " id int,"
			+ " name varchar(50),"
			+ " sex varchar(50),"
			+ " age int, level int,"
			+ " PRIMARY KEY (id))")
				.executeUpdate();
	}
	
	public static void createTableEnrollment() throws SQLException, ClassNotFoundException{
		connection.prepareStatement("CREATE TABLE Enrollment ("
			+ " studentId int,"
			+ " classId int,"
			+ " FOREIGN KEY (studentId) REFERENCES Student(id),"
			+ " FOREIGN KEY (classId) REFERENCES Class(id))")
				.executeUpdate();
	}
	
	public static void createTableFaculty() throws SQLException, ClassNotFoundException{
		connection.prepareStatement("CREATE TABLE Faculty ("
			+ " id int,"
			+ " name varchar(50),"
			+ " PRIMARY KEY (id))")
				.executeUpdate();
	}
	
	public static void createTableClass() throws SQLException, ClassNotFoundException{
		connection.prepareStatement("CREATE TABLE Class ("
			+ " id int,"
			+ " name varchar(50),"
			+ " facultyId int, PRIMARY KEY(id),"
			+ " FOREIGN KEY (facultyId) REFERENCES Faculty(id))")
				.executeUpdate();
	}
	public static void addStudents() throws SQLException{
		connection.prepareStatement("INSERT INTO Student(id,name,sex,age,level)"
			+ "VALUES"
			+ "(1,'John Smith','male',23,2),"
			+ "(2,'Rebecca Milson','female',27,3),"
			+ "(3,'George Heartbraker','male',19,1),"
			+ "(4,'Deepika Chopra','female',25,3)")
				.executeUpdate();
	}
	
	public static void addFaculty() throws SQLException{
		connection.prepareStatement("INSERT INTO Faculty(id,name)"
			+ "VALUES(100,'Engineering'),"
			+ "(101,'Philosophy'),"
			+ "(102,'Law and administration'),"
			+ "(103,'Languages');")
				.executeUpdate();
	}
	
	public static void addClass() throws SQLException{
		connection.prepareStatement("INSERT INTO Class(id,name,facultyId) "
			+ "VALUES(1000,'Introduction to labour law',102),"
			+ "(1001,'Graph algoritms',100),"
			+ "(1002,'Existentialism in 20th century',101),"
			+ "(1003,'English grammar',103),"
			+ "(1004,'From Plato to Kant',101)")
				.executeUpdate();
	}
	
	public static void addEnrollment() throws SQLException{
		connection.prepareStatement("INSERT INTO Enrollment(studentId, classId) "
			+ "VALUES(1,1000),"
			+ "(1,1002),"
			+ "(1,1003),"
			+ "(1,1004),"
			+ "(2,1002),"
			+ "(2,1003),"
			+ "(4,1000),"
			+ "(4,1002),"
			+ "(4,1003)")
				.executeUpdate();
	}
	public static void firstQuery() throws SQLException{
		System.out.println("Numery i nazwiska wszystkich studentów");
		resultset = connection.prepareStatement("SELECT id, name FROM Student").executeQuery();
		while(resultset.next()){			
			System.out.println("Numer = " + resultset.getString("id") +" Imiê i nazwisko = " + resultset.getString("name"));
		}
	}
	
	public static void secondQuery() throws SQLException{
		System.out.println("Numery i nazwiska wszystkich osób, które nie s¹ zapisane na ¿aden przedmiot");
		resultset = connection.prepareStatement("SELECT s.id, s.name FROM Student s WHERE s.id NOT IN (SELECT studentId FROM Enrollment)").executeQuery();
		while(resultset.next()){			
			System.out.println("Numer = " + resultset.getString("id") +" Imiê i nazwisko = " + resultset.getString("name"));
		}
	}
	
	public static void thirdQuery() throws SQLException{
		System.out.println("Numery i nazwiska osób p³ci ¿eñskiej ucz¹cych siê o egzystencjaliŸmie w 20 wieku");
		resultset = connection.prepareStatement("SELECT s.id,s.name FROM Student s INNER JOIN Enrollment e ON s.id = e.studentId WHERE s.sex='female' AND e.classId = 1002").executeQuery();
		while(resultset.next()){			
			System.out.println("Numer = " + resultset.getString("id") +" Imiê i nazwisko = " + resultset.getString("name"));
		}
	}
	
	public static void fourthQuery() throws SQLException{
		System.out.println(" Nazwy wszystkich wydziałów, na których przedmioty nikt się nie zapisał ");
		resultset = connection.prepareStatement("SELECT c.name  FROM Class c WHERE NOT EXISTS(SELECT e.classId FROM Enrollment e WHERE e.classId = c.id)").executeQuery();
		while(resultset.next()){			
			System.out.println("name = "+resultset.getString("name"));
		}
	}
	
	public static void fifthQuery() throws SQLException{
		System.out.println("Wiek najstarszej osoby ucz¹cej siê o prawie pracy");
		resultset = connection.prepareStatement("SELECT MAX(s.age) as tmpAge FROM Student s INNER JOIN Enrollment e ON s.id = e.studentId WHERE e.classId = 1000").executeQuery();
		while(resultset.next()){			
			System.out.println("Wiek = " + resultset.getString("tmpAge"));
		}
	}
	
	public static void sixthQuery() throws SQLException{
		System.out.println("Nazwy przedmiotów, na które zapisa³y siê przynajmniej dwie osoby");
		resultset = connection.prepareStatement("SELECT c.name, Count(studentId) as tmpStudent From Enrollment e INNER JOIN Class c ON c.id = e.classId GROUP BY c.name HAVING Count(e.studentId) >=2").executeQuery();
		while(resultset.next()){			
			System.out.println("Nazwa = " + resultset.getString("name"));
		}
	}
	public static void seventhQuery() throws SQLException{
		System.out.println(" Poziomy osób studiujących i średni wiek osób na każdym poziomie ");
		resultset = connection.prepareStatement("SELECT AVG(s.age) as sage ,s.level as slevel FROM Student s WHERE s.id IN(SELECT id FROM Student) GROUP BY s.level ").executeQuery();
		while(resultset.next()){			
			System.out.println("level = "+resultset.getString("slevel")+" avg age="+resultset.getString("sage"));
		}
	}
}
