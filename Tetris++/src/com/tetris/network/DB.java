package com.tetris.network;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class DB {
	public DB(int type,int score,boolean pre) { //창만보여주기 
		boolean me =false;
		String [][] sbr= new String[10][2];
		try {
			int i=0;
			//1.get a connection to database
			Connection myCon=DriverManager.getConnection("jdbc:mysql://dbculture.cjot3b1q0op5.ap-northeast-2.rds.amazonaws.com:3306/tetris_db?useSSL=false","dbculture","dbculture");
			//2.create a statement
			Statement myStmt=myCon.createStatement();
			//3.execute sql query, SORT DESC
			ResultSet myRe=myStmt.executeQuery("select name,score from tetris_db where what="+type+" order by score desc");
			//4.순위에 맞게 name 과 score 저장
			while(myRe.next()) {
				if(i==10)
					break;
				sbr[i][0]=myRe.getString("name");
				sbr[i][1]=myRe.getString("score");
				if(pre && i==0) {
					if(score==Integer.parseInt(sbr[i][1])) me=true;
				}
				i++;
			}
			System.out.println("rank_DB connected success");
			new rank_frame(sbr,i, me);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	public DB(String name, int score, String title) { //저장하기
		int type=0;
		if(title=="젓가락 행진곡") type=1;
		else if(title=="What is Love?") type=2;
	
		try {
			//1.get a connection to database
			Connection myCon=DriverManager.getConnection("jdbc:mysql://dbculture.cjot3b1q0op5.ap-northeast-2.rds.amazonaws.com:3306/tetris_db?useSSL=false","dbculture","dbculture");
			//insert
			StringBuilder sb=new StringBuilder();
			String sql=sb.append("insert into tetris_db values('"+name+"',"+score+","+type+")").toString();
			//2.create a statement
			Statement myStmt=myCon.createStatement();
			myStmt.executeUpdate(sql);
			
			System.out.println("DB connected success");
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		new DB(type,score,true); //직전에 종료한 게임
	}
}
class rank_frame extends JDialog{
	JLabel jlb1=new JLabel("");
	JPanel jlb2=new JPanel();
	JPanel jlb3=new JPanel();

	JPanel jp=new JPanel();
	BorderLayout b=new BorderLayout(20,20);
	public rank_frame(String[][] str,int j,boolean me) {
		this.setLayout(b);
		add(jlb1,BorderLayout.NORTH);
		
		jp.setLayout(new GridLayout(0,3,20,20));
		int i=0;
		jp.add(new JLabel("RANK"));
		jp.add(new JLabel("NAME"));
		jp.add(new JLabel("SCORE"));
		JLabel j1=new JLabel(String.valueOf(i+1));
		j1.setForeground(Color.RED);
		JLabel j2=new JLabel(str[i][0]);
		j2.setForeground(Color.RED);
		JLabel j3=new JLabel(str[i][1]);
		j3.setForeground(Color.RED);
		j1.setHorizontalAlignment(SwingConstants.CENTER);
		j2.setHorizontalAlignment(SwingConstants.CENTER);
		j3.setHorizontalAlignment(SwingConstants.CENTER);
		jp.add(j1);
		jp.add(j2);
		jp.add(j3);
		i++;
		j1 = new JLabel(String.valueOf(i+1));
		j2=new JLabel(str[i][0]);
		j3=new JLabel(str[i][1]);
		j1.setForeground(Color.GREEN);
		j2.setForeground(Color.GREEN);
		j3.setForeground(Color.GREEN);
		j1.setHorizontalAlignment(SwingConstants.CENTER);
		j2.setHorizontalAlignment(SwingConstants.CENTER);
		j3.setHorizontalAlignment(SwingConstants.CENTER);
		jp.add(j1);
		jp.add(j2);
		jp.add(j3);
		i++;
		j1 = new JLabel(String.valueOf(i+1));
		j2=new JLabel(str[i][0]);
		j3=new JLabel(str[i][1]);
		j1.setForeground(Color.BLUE);
		j2.setForeground(Color.BLUE);
		j3.setForeground(Color.BLUE);
		j1.setHorizontalAlignment(SwingConstants.CENTER);
		j2.setHorizontalAlignment(SwingConstants.CENTER);
		j3.setHorizontalAlignment(SwingConstants.CENTER);
		jp.add(j1);
		jp.add(j2);
		jp.add(j3);
		i++;
		for(;i<j;i++) {
			j1 = new JLabel(String.valueOf(i+1));
			j2=new JLabel(str[i][0]);
			j3=new JLabel(str[i][1]);
			j1.setHorizontalAlignment(SwingConstants.CENTER);
			j2.setHorizontalAlignment(SwingConstants.CENTER);
			j3.setHorizontalAlignment(SwingConstants.CENTER);
			jp.add(j1);
			jp.add(j2);
			jp.add(j3);
		}
		add(jp,BorderLayout.CENTER);
		add(jlb2,BorderLayout.WEST);
		add(jlb3,BorderLayout.EAST);
		jlb1.setText("\nTETRIS RANK");
		jlb1.setFont(new Font("굴림",Font.BOLD, 13));
		jlb1.setHorizontalAlignment(SwingConstants.CENTER);
		if(me) { //일등이자신일때 
			JLabel j4=new JLabel("\n축 하 합 니 다! 1 등 입 니 다!");
			j4.setFont(new Font("굴림", Font.BOLD, 11));
			j4.setForeground(Color.RED);
			j4.setHorizontalAlignment(SwingConstants.CENTER);
			add(j4,BorderLayout.SOUTH);
		}
		
		this.pack();
		//화면 중앙에 띄우기
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize(); //스크린전체사이즈
		this.setLocation((size.width-this.getWidth())/2,(size.height-this.getHeight())/2); //스크린 중앙에 위치 
		this.setModal(true);
		this.setVisible(true);
	}
}
