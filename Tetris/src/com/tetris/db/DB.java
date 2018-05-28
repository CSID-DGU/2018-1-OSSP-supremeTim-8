package com.tetris.db;
import java.sql.*;
public class DB {
	//public static void main(String[] args) {
		// TODO Auto-generated method stub
	public DB() {
		try {
			//1.get a connection to database
			Connection myCon=DriverManager.getConnection("jdbc:mysql://mytestdb.c0l7ddni7jh4.ap-northeast-2.rds.amazonaws.com:3306/mydb?useSSL=false&serverTimezone=UTC","test_db","testdatabase");
			//insert
			StringBuilder sb=new StringBuilder();
			String id_="선히";
			int score_=100;
			String sql=sb.append("insert into user values('"+id_+"',"+score_+");").toString();
			//2.create a statemet
			Statement myStmt=myCon.createStatement();
			myStmt.executeUpdate(sql);
			//3.execute sql query
			ResultSet myRe=myStmt.executeQuery("select * from user");
			//4. process the result set
			while(myRe.next()) {
				System.out.println(myRe.getString("id")+", "+myRe.getString("score"));
			}
			System.out.println("success");
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	//}
	}
}
