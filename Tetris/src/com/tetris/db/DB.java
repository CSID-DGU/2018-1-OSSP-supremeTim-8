package com.tetris.db;
import java.sql.*;
public class DB {
	public DB() {
		try {
			//1.get a connection to database
			Connection myCon=DriverManager.getConnection("jdbc:mysql://dbculture.cjot3b1q0op5.ap-northeast-2.rds.amazonaws.com:3306/tetris_db?useSSL=false","dbculture","dbculture");
			//insert
			StringBuilder sb=new StringBuilder();
			String id_="선히";
			int score_=100;
			String sql=sb.append("insert into tetris_db values("+2+",'"+id_+"',"+score_+")").toString();
			//2.create a statemet
			Statement myStmt=myCon.createStatement();
			myStmt.executeUpdate(sql);
			//3.execute sql query
			ResultSet myRe=myStmt.executeQuery("select * from tetris_db");
			//4. process the result set
			while(myRe.next()) {
				System.out.println(myRe.getString("idx")+", "+myRe.getString("name")+", "+myRe.getString("score"));
			}
			System.out.println("success");
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
