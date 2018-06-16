package com.tetris.window;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class SystemMessageArea extends JScrollPane { // 좌측메세지 공간 만들기
	private static final long serialVersionUID = 1L; // 직렬화를 위한 변수 -> 객체입출력에 사용
	private static JTextArea area = new JTextArea(); // text입력공간

	public SystemMessageArea(int x, int y, int width, int height) {
		super(area); // JScrollPane을 만들고 그 객체에 스크롤 기능을 추가해야 하는 텍스트 영역을 전달
		this.setBounds(x, y, width, height); // 위치잡아주기
		this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); // 수직방향 스크롤바 생성
		area.setEditable(false); // 텍스트 필드를 수정할 수 있는지 여부 -> false
		area.setLineWrap(true); // 줄이 너무 길면 자동으로 개행할지 결정 -> true
	}

	public void printMessage(String msg) {
		if (msg != null && !msg.equals("")) {
			area.append("--------------------" + msg + "\n--------------------"); // 영역에 msg 추가
			area.setCaretPosition(area.getText().length()); // msg가 추가되면 그 길이만큼 스크롤하지 않고도 바로 보여줌
		}
	}

	public void clearMessage() { // 필드초기화
		area.setText("");
	}
}
