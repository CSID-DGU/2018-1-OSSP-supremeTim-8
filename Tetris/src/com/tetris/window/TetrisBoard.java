package com.tetris.window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
<<<<<<< HEAD
import java.util.Random; // ÏïÑÏù¥ÌÖúÏùÑ ÎûúÎç§ÏúºÎ°ú Ï†ïÌïòÍ∏∞ ÏúÑÌï¥ import
=======
import java.util.Random; // æ∆¿Ã≈€¿ª ∑£¥˝¿∏∑Œ ¡§«œ±‚ ¿ß«ÿ import
>>>>>>> create_LEVEL

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.tetris.classes.Block;
import com.tetris.classes.TetrisBlock;
import com.tetris.controller.TetrisController;
import com.tetris.network.GameClient;
import com.tetris.shape.CenterUp;
import com.tetris.shape.LeftTwoUp;
import com.tetris.shape.LeftUp;
import com.tetris.shape.Line;
import com.tetris.shape.Nemo;
import com.tetris.shape.RightTwoUp;
import com.tetris.shape.RightUp;

public class TetrisBoard extends JPanel implements Runnable, KeyListener, MouseListener, ActionListener {
	private static final long serialVersionUID = 1L;

	private Tetris tetris;
	private GameClient client;

	public static final int BLOCK_SIZE = 20;
	public static final int BOARD_X = 120;
	public static final int BOARD_Y = 50;
	private int minX = 1, minY = 0, maxX = 10, maxY = 21, down = 50, up = 0;
	private final int MESSAGE_X = 2;
	private final int MESSAGE_WIDTH = BLOCK_SIZE * (7 + minX);
	private final int MESSAGE_HEIGHT = BLOCK_SIZE * (6 + minY);
	private final int PANEL_WIDTH = maxX * BLOCK_SIZE + MESSAGE_WIDTH + BOARD_X;
	private final int PANEL_HEIGHT = maxY * BLOCK_SIZE + MESSAGE_HEIGHT + BOARD_Y;

	private final int MAX_ITEM_NUM = 4;
	private final int MIN_ITEM_NUM = 1;

	private SystemMessageArea systemMsg = new SystemMessageArea(BLOCK_SIZE * 1, BOARD_Y + BLOCK_SIZE + BLOCK_SIZE * 7,
<<<<<<< HEAD
			BLOCK_SIZE * 5, BLOCK_SIZE * 12); // ÏôºÏ™Ω Î©îÏÑ∏ÏßÄ Î∞ïÏä§
	private MessageArea messageArea = new MessageArea(this, 2, PANEL_HEIGHT - (MESSAGE_HEIGHT - MESSAGE_X),
			PANEL_WIDTH - BLOCK_SIZE * 7 - 2, MESSAGE_HEIGHT - 2); // ÏïÑÎûòÏ™Ω Î©îÏÑ∏ÏßÄ Î∞ïÏä§
	private JButton btnStart = new JButton("ÏãúÏûëÌïòÍ∏∞");
	private JButton btnExit = new JButton("ÎÇòÍ∞ÄÍ∏∞");
	private JCheckBox checkGhost = new JCheckBox("Í≥†Ïä§Ìä∏Î™®Îìú", true);
	private JCheckBox checkGrid = new JCheckBox("Í≤©Ïûê ÌëúÏãú", true);
=======
			BLOCK_SIZE * 5, BLOCK_SIZE * 12); // øﬁ¬  ∏ﬁºº¡ˆ π⁄Ω∫
	private MessageArea messageArea = new MessageArea(this, 2, PANEL_HEIGHT - (MESSAGE_HEIGHT - MESSAGE_X),
			PANEL_WIDTH - BLOCK_SIZE * 7 - 2, MESSAGE_HEIGHT - 2); // æ∆∑°¬  ∏ﬁºº¡ˆ π⁄Ω∫
	private JButton btnStart = new JButton("Ω√¿€«œ±‚");
	private JButton btnExit = new JButton("≥™∞°±‚");
	private JCheckBox checkGhost = new JCheckBox("∞ÌΩ∫∆Æ∏µÂ", true);
	private JCheckBox checkGrid = new JCheckBox("∞›¿⁄ «•Ω√", true);
>>>>>>> create_LEVEL
	private Integer[] lv = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };
	private JComboBox<Integer> comboSpeed = new JComboBox<Integer>(lv);

	private String ip;
	private int port;
	private String nickName;
	private Thread th;
	private ArrayList<Block> blockList;
	private ArrayList<TetrisBlock> nextBlocks;
	private TetrisBlock shap;
	private TetrisBlock ghost;
	private TetrisBlock hold;
	private Block[][] map;
	private TetrisController controller;
	private TetrisController controllerGhost;

	private boolean isPlay = false;
	private boolean isHold = false;
	private boolean usingGhost = true;
	private boolean usingGrid = true;
	private int removeLineCount = 0; // ¡ˆøÓ ¡Ÿ¿« ∞≥ºˆ
	private int removeLineSum = 0; // √— ¡°ºˆ
	private int removeLineTemp = 0; // ∑π∫ß¿ª ø√∑¡¡÷±‚ ¿ß«— ¡°ºˆ¿« «’ (∑π∫ß¿Ã ø√∂Û∞°∏È √ ±‚»≠)
	private int removeLineCombo = 0;
	private int level = 1;

	public TetrisBoard(Tetris tetris, GameClient client) {
		this.tetris = tetris;
		this.client = client;
		this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.setLayout(null);
		this.setFocusable(true);

		btnStart.setBounds(PANEL_WIDTH - BLOCK_SIZE * 7, PANEL_HEIGHT - messageArea.getHeight(), BLOCK_SIZE * 7,
				messageArea.getHeight() / 2);
		btnStart.setFocusable(false);
		btnStart.setEnabled(false);
<<<<<<< HEAD
		btnStart.addActionListener(this); // ÏãúÏûë Î≤ÑÌäº ÎßåÎì§Í≥† ÏûÖÎ†• Í∏∞Îã§Î¶º
		btnExit.setBounds(PANEL_WIDTH - BLOCK_SIZE * 7, PANEL_HEIGHT - messageArea.getHeight() / 2, BLOCK_SIZE * 7,
				messageArea.getHeight() / 2);
		btnExit.setFocusable(false);
		btnExit.addActionListener(this); // ÎÅùÎÇ¥Í∏∞ Î≤ÑÌäº ÎßåÎì§Í≥† ÏûÖÎ†• Í∏∞Îã§Î¶º
		checkGhost.setBounds(PANEL_WIDTH - BLOCK_SIZE * 7 + 35, 5, 95, 20);
		checkGhost.setBackground(new Color(0, 87, 102));
		checkGhost.setForeground(Color.WHITE);
		checkGhost.setFont(new Font("Íµ¥Î¶º", Font.BOLD, 13));
		checkGhost.addChangeListener(new ChangeListener() { // Í≥†Ïä§Ìä∏ Ï≤¥ÌÅ¨ Î∞ïÏä§ ÎßåÎì§Í≥†
			@Override
			public void stateChanged(ChangeEvent arg0) { // ÏÇ¨Ïö©Ìï† Í±¥ÏßÄ state Î≥ÄÍ≤Ω ÌôïÏù∏
=======
		btnStart.addActionListener(this); // Ω√¿€ πˆ∆∞ ∏∏µÈ∞Ì ¿‘∑¬ ±‚¥Ÿ∏≤
		btnExit.setBounds(PANEL_WIDTH - BLOCK_SIZE * 7, PANEL_HEIGHT - messageArea.getHeight() / 2, BLOCK_SIZE * 7,
				messageArea.getHeight() / 2);
		btnExit.setFocusable(false);
		btnExit.addActionListener(this); // ≥°≥ª±‚ πˆ∆∞ ∏∏µÈ∞Ì ¿‘∑¬ ±‚¥Ÿ∏≤
		checkGhost.setBounds(PANEL_WIDTH - BLOCK_SIZE * 7 + 35, 5, 95, 20);
		checkGhost.setBackground(new Color(0, 87, 102));
		checkGhost.setForeground(Color.WHITE);
		checkGhost.setFont(new Font("±º∏≤", Font.BOLD, 13));
		checkGhost.addChangeListener(new ChangeListener() { // ∞ÌΩ∫∆Æ √º≈© π⁄Ω∫ ∏∏µÈ∞Ì
			@Override
			public void stateChanged(ChangeEvent arg0) { // ªÁøÎ«“ ∞«¡ˆ state ∫Ø∞Ê »Æ¿Œ
>>>>>>> create_LEVEL
				usingGhost = checkGhost.isSelected();
				TetrisBoard.this.setRequestFocusEnabled(true);
				TetrisBoard.this.repaint();
			}
		});
		checkGrid.setBounds(PANEL_WIDTH - BLOCK_SIZE * 7 + 35, 25, 95, 20);
		checkGrid.setBackground(new Color(0, 87, 102));
		checkGrid.setForeground(Color.WHITE);
<<<<<<< HEAD
		checkGrid.setFont(new Font("Íµ¥Î¶º", Font.BOLD, 13));
		checkGrid.addChangeListener(new ChangeListener() { // Í∑∏Î¶¨Îìú Ï≤¥ÌÅ¨ Î∞ïÏä§ ÎßåÎì§Í≥†
			@Override
			public void stateChanged(ChangeEvent arg0) { // ÏÇ¨Ïö©Ìï† Í±¥ÏßÄ state Î≥ÄÍ≤Ω ÌôïÏù∏
=======
		checkGrid.setFont(new Font("±º∏≤", Font.BOLD, 13));
		checkGrid.addChangeListener(new ChangeListener() { // ±◊∏ÆµÂ √º≈© π⁄Ω∫ ∏∏µÈ∞Ì
			@Override
			public void stateChanged(ChangeEvent arg0) { // ªÁøÎ«“ ∞«¡ˆ state ∫Ø∞Ê »Æ¿Œ
>>>>>>> create_LEVEL
				usingGrid = checkGrid.isSelected();
				TetrisBoard.this.setRequestFocusEnabled(true);
				TetrisBoard.this.repaint();
			}
		});
		comboSpeed.setBounds(PANEL_WIDTH - BLOCK_SIZE * 8, 5, 45, 20);
<<<<<<< HEAD
		// this.add(comboSpeed); // Î≤ÑÌäº ÎπÑÌôúÏÑ±Ìôî
=======
		// this.add(comboSpeed); // πˆ∆∞ ∫Ò»∞º∫»≠
>>>>>>> create_LEVEL

		this.add(systemMsg);
		this.add(messageArea);
		this.add(btnStart);
		this.add(btnExit);
		this.add(checkGhost);
<<<<<<< HEAD
		this.add(checkGrid); // Íµ¨ÌòÑÌïú UI Î™®Îëê Ï∂îÍ∞Ä
=======
		this.add(checkGrid); // ±∏«ˆ«— UI ∏µŒ √ﬂ∞°
>>>>>>> create_LEVEL
	}

	public void startNetworking(String ip, int port, String nickName) {
		this.ip = ip;
		this.port = port;
		this.nickName = nickName;
		this.repaint();
	}

<<<<<<< HEAD
	public void gameStart(int speed) { // Í≤åÏûÑÏãúÏûë Î©îÏÜåÎìú
		comboSpeed.setSelectedItem(new Integer(speed));

		if (th != null) { // Ï≤òÏùå Ïù¥ Î©îÏÜåÎìúÎ•º Ïã§ÌñâÌïú Í≤ΩÏö∞ th=null
=======
	public void gameStart(int speed) { // ∞‘¿”Ω√¿€ ∏ﬁº“µÂ
		comboSpeed.setSelectedItem(new Integer(speed));

		if (th != null) { // √≥¿Ω ¿Ã ∏ﬁº“µÂ∏¶ Ω««‡«— ∞ÊøÏ th=null
>>>>>>> create_LEVEL
			try {
				isPlay = false;
				th.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

<<<<<<< HEAD
		map = new Block[maxY][maxX]; // Block 2Ï∞®Ïõê Î∞∞Ïó¥ ÏÉùÏÑ±
		blockList = new ArrayList<Block>();
		nextBlocks = new ArrayList<TetrisBlock>(); // Îã§ÏùåÏóê ÎÇòÏò¨ ÌÖåÌä∏Î¶¨Ïä§ Î∏îÎü≠Îì§ÏùÑ Îã¥ÏïÑÎÜìÏùÑ Î≥ÄÏàò

		shap = getRandomTetrisBlock(); // ÎûúÎç§ÏúºÎ°ú Î∏îÎü≠ ÏÉùÏÑ±
		ghost = getBlockClone(shap, true); // Í≥†Ïä§Ìä∏ Î∏îÎü≠ ÏÉùÏÑ±
		hold = null;
		isHold = false;
		controller = new TetrisController(shap, maxX - 1, maxY - 1, map);
		controllerGhost = new TetrisController(ghost, maxX - 1, maxY - 1, map); // Ïù¥Îèô Í¥ÄÎ†® Ï†ïÏùò
		this.showGhost(); // Í≥†Ïä§Ìä∏ Î≥¥Ïó¨Ï£ºÍ∏∞
		for (int i = 0; i < 5; i++) {
			nextBlocks.add(getRandomTetrisBlock()); // Î∏îÎü≠ 5Í∞ú Ï∂îÍ∞Ä
=======
		map = new Block[maxY][maxX]; // Block 2¬˜ø¯ πËø≠ ª˝º∫
		blockList = new ArrayList<Block>();
		nextBlocks = new ArrayList<TetrisBlock>(); // ¥Ÿ¿Ωø° ≥™ø√ ≈◊∆Æ∏ÆΩ∫ ∫Ì∑∞µÈ¿ª ¥„æ∆≥ı¿ª ∫Øºˆ

		shap = getRandomTetrisBlock(); // ∑£¥˝¿∏∑Œ ∫Ì∑∞ ª˝º∫
		ghost = getBlockClone(shap, true); // ∞ÌΩ∫∆Æ ∫Ì∑∞ ª˝º∫
		hold = null;
		isHold = false;
		controller = new TetrisController(shap, maxX - 1, maxY - 1, map);
		controllerGhost = new TetrisController(ghost, maxX - 1, maxY - 1, map); // ¿Ãµø ∞¸∑√ ¡§¿«
		this.showGhost(); // ∞ÌΩ∫∆Æ ∫∏ø©¡÷±‚
		for (int i = 0; i < 5; i++) {
			nextBlocks.add(getRandomTetrisBlock()); // ∫Ì∑∞ 5∞≥ √ﬂ∞°
>>>>>>> create_LEVEL
		}

		isPlay = true;
		th = new Thread(this);
<<<<<<< HEAD
		th.start(); // run Î©îÏÜåÎìú ÏãúÏûë
=======
		th.start(); // run ∏ﬁº“µÂ Ω√¿€
>>>>>>> create_LEVEL
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.clearRect(0, 0, this.getWidth(), this.getHeight() + 1);

		g.setColor(new Color(0, 87, 102));
		g.fillRect(0, 0, (maxX + minX + 13) * BLOCK_SIZE + 1, BOARD_Y);

		g.setColor(new Color(92, 109, 129));
		g.fillRect(0, BOARD_Y, (maxX + minX + 13) * BLOCK_SIZE + 1, maxY * BLOCK_SIZE + 1);
		g.setColor(Color.WHITE);

		g.drawString("ip : " + ip + "     port : " + port, 20, 20);

		g.drawString("¥–≥◊¿” : " + nickName, 20, 40);

		Font font = g.getFont();
<<<<<<< HEAD
		g.setFont(new Font("Íµ¥Î¶º", Font.BOLD, 13));
		// g.drawString("ÏÜçÎèÑ", PANEL_WIDTH - BLOCK_SIZE * 10, 20); // ÏÜçÎèÑÏù¥Î¶Ñ ÏßÄÏõÄ
=======
		g.setFont(new Font("±º∏≤", Font.BOLD, 13));
		// g.drawString("º”µµ", PANEL_WIDTH - BLOCK_SIZE * 10, 20); // º”µµ¿Ã∏ß ¡ˆøÚ
		g.drawString("¡°ºˆ", PANEL_WIDTH - BLOCK_SIZE * 12, 20); // ¡°ºˆ
		g.drawString(String.valueOf(removeLineSum), PANEL_WIDTH - BLOCK_SIZE * 12, 40);
		g.drawString("LEVEL", PANEL_WIDTH - BLOCK_SIZE * 9, 20); // ∑π∫ß (1~10)
		g.drawString(String.valueOf(level), PANEL_WIDTH - BLOCK_SIZE * 8-5, 40); 
>>>>>>> create_LEVEL
		g.setFont(font);

		g.setColor(Color.BLACK);
		g.fillRect(BOARD_X + BLOCK_SIZE * minX, BOARD_Y, maxX * BLOCK_SIZE + 1, maxY * BLOCK_SIZE + 1);
		g.fillRect(BLOCK_SIZE * minX, BOARD_Y + BLOCK_SIZE, BLOCK_SIZE * 5, BLOCK_SIZE * 5);
		g.fillRect(BOARD_X + BLOCK_SIZE * minX + (maxX + 1) * BLOCK_SIZE + 1, BOARD_Y + BLOCK_SIZE, BLOCK_SIZE * 5,
				BLOCK_SIZE * 5);
		g.fillRect(BOARD_X + BLOCK_SIZE * minX + (maxX + 1) * BLOCK_SIZE + 1, BOARD_Y + BLOCK_SIZE + BLOCK_SIZE * 7,
				BLOCK_SIZE * 5, BLOCK_SIZE * 12);

		g.setFont(new Font(font.getFontName(), font.getStyle(), 20));
		g.drawString("H O L D", BLOCK_SIZE + 12, BOARD_Y + BLOCK_SIZE + BLOCK_SIZE * 5 + 20);
		g.drawString("N E X T", BOARD_X + BLOCK_SIZE + (maxX + 1) * BLOCK_SIZE + 1 + 12,
				BOARD_Y + BLOCK_SIZE + BLOCK_SIZE * 5 + 20);
		g.setFont(font);

		if (usingGrid) {
			g.setColor(Color.darkGray);
			for (int i = 1; i < maxY; i++)
				g.drawLine(BOARD_X + BLOCK_SIZE * minX, BOARD_Y + BLOCK_SIZE * (i + minY),
						BOARD_X + (maxX + minX) * BLOCK_SIZE, BOARD_Y + BLOCK_SIZE * (i + minY));
			for (int i = 1; i < maxX; i++)
				g.drawLine(BOARD_X + BLOCK_SIZE * (i + minX), BOARD_Y + BLOCK_SIZE * minY,
						BOARD_X + BLOCK_SIZE * (i + minX), BOARD_Y + BLOCK_SIZE * (minY + maxY));
			for (int i = 1; i < 5; i++)
				g.drawLine(BLOCK_SIZE * minX, BOARD_Y + BLOCK_SIZE * (i + 1), BLOCK_SIZE * (minX + 5) - 1,
						BOARD_Y + BLOCK_SIZE * (i + 1));
			for (int i = 1; i < 5; i++)
				g.drawLine(BLOCK_SIZE * (minY + i + 1), BOARD_Y + BLOCK_SIZE, BLOCK_SIZE * (minY + i + 1),
						BOARD_Y + BLOCK_SIZE * (minY + 6) - 1);
			for (int i = 1; i < 5; i++)
				g.drawLine(BOARD_X + BLOCK_SIZE * minX + (maxX + 1) * BLOCK_SIZE + 1, BOARD_Y + BLOCK_SIZE * (i + 1),
						BOARD_X + BLOCK_SIZE * minX + (maxX + 1) * BLOCK_SIZE + BLOCK_SIZE * 5,
						BOARD_Y + BLOCK_SIZE * (i + 1));
			for (int i = 1; i < 5; i++)
				g.drawLine(BOARD_X + BLOCK_SIZE * minX + (maxX + 1 + i) * BLOCK_SIZE + 1, BOARD_Y + BLOCK_SIZE,
						BOARD_X + BLOCK_SIZE * minX + BLOCK_SIZE + BLOCK_SIZE * (10 + i) + 1,
						BOARD_Y + BLOCK_SIZE * 6 - 1);
		}

		int x = 0, y = 0, newY = 0;
		if (hold != null) {
			x = 0;
			y = 0;
			newY = 3;
			x = hold.getPosX();
			y = hold.getPosY();
			hold.setPosX(-4 + minX);
			hold.setPosY(newY + minY);
			hold.drawBlock(g);
			hold.setPosX(x);
			hold.setPosY(y);
		}

		if (nextBlocks != null) {
			x = 0;
			y = 0;
			newY = 3;
			for (int i = 0; i < nextBlocks.size(); i++) {
				TetrisBlock block = nextBlocks.get(i);
				x = block.getPosX();
				y = block.getPosY();
				block.setPosX(13 + minX);
				block.setPosY(newY + minY);
				if (newY == 3)
					newY = 6;
				block.drawBlock(g);
				block.setPosX(x);
				block.setPosY(y);
				newY += 3;
			}
		}

		if (blockList != null) {
			x = 0;
			y = 0;
			for (int i = 0; i < blockList.size(); i++) {
				Block block = blockList.get(i);
				x = block.getPosGridX();
				y = block.getPosGridY();
				block.setPosGridX(x + minX);
				block.setPosGridY(y + minY);
				block.drawColorBlock(g);
				block.setPosGridX(x);
				block.setPosGridY(y);
			}
		}

		if (ghost != null) {

			if (usingGhost) {
				x = 0;
				y = 0;
				x = ghost.getPosX();
				y = ghost.getPosY();
				ghost.setPosX(x + minX);
				ghost.setPosY(y + minY);
				ghost.drawBlock(g);
				ghost.setPosX(x);
				ghost.setPosY(y);
			}
		}

		if (shap != null) {
			x = 0;
			y = 0;
			x = shap.getPosX();
			y = shap.getPosY();
			shap.setPosX(x + minX);
			shap.setPosY(y + minY);
			shap.drawBlock(g);
			shap.setPosX(x);
			shap.setPosY(y);
		}
	}

	@Override
	public void run() {
		int countMove = (21 - (int) comboSpeed.getSelectedItem()) * 5;
		/*
<<<<<<< HEAD
		 * Ïä§ÌîºÎìúÍ∞Ä 1Ïù¥Î©¥ 100 -> Í∞íÏù¥ Ïª§ÏßàÏàòÎ°ù moveDown() Ìò∏Ï∂ú Ï†ÅÏñ¥Ïßê -> Î∏îÎü≠ ÎÇ¥Î†§Ïò§Îäî ÏÜçÎèÑ ÎäêÎ¶º
		 */
		int countDown = 0; // Í∞íÏù¥ ÌÅ¥ÏàòÎ°ù Î∏îÎ°ùÏù¥ ÎÜìÏù¥Îäî ÏãúÍ∞ÑÏù¥ Ïò§Îûò Í±∏Î¶º
		int countUp = up; // Ï≤òÏùåÏóêÎäî 0

		while (isPlay) { // Í≤åÏûÑÏù¥ Ïã§Ìñâ Ï§ëÏù¥Î©¥
			try {
				Thread.sleep(10); // Ïû†Ïãú Î©àÏ∂§ -> ??
=======
		 * Ω∫««µÂ∞° 1¿Ã∏È 100 -> ∞™¿Ã ƒø¡˙ºˆ∑œ moveDown() »£√‚ ¿˚æÓ¡¸ -> ∫Ì∑∞ ≥ª∑¡ø¿¥¬ º”µµ ¥¿∏≤
		 */
		int countDown = 0; // ∞™¿Ã ≈¨ºˆ∑œ ∫Ì∑œ¿Ã ≥ı¿Ã¥¬ Ω√∞£¿Ã ø¿∑° ∞…∏≤
		int countUp = up; // √≥¿Ωø°¥¬ 0

		while (isPlay) { // ∞‘¿”¿Ã Ω««‡ ¡ﬂ¿Ã∏È
			try {
				Thread.sleep(10); // ¿·Ω√ ∏ÿ√„ -> ??
>>>>>>> create_LEVEL
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

<<<<<<< HEAD
			if (countDown != 0) { // Ï≤òÏùåÏóêÎäî ÎÑòÏñ¥Í∞ê -> 50Ïù¥ Ìï†ÎãπÎêòÍ≥† Ï≤òÏùåÏúºÎ°ú ÏßÑÏûÖ
				countDown--;
				if (countDown == 0) { // 0ÏúºÎ°ú ÎêòÎ©¥

					if (controller != null && !controller.moveDown()) // ÏïÑÎûòÎ°ú ÎÇ¥Î†§Í∞à Ïàò ÏóÜÍ≥† Ïª®Ìä∏Î°§Îü¨Í∞Ä Ìï†ÎãπÏù¥ ÏïàÎêòÏñ¥ÏûàÏúºÎ©¥
=======
			if (countDown != 0) { // √≥¿Ωø°¥¬ ≥—æÓ∞® -> 50¿Ã «“¥Áµ«∞Ì √≥¿Ω¿∏∑Œ ¡¯¿‘
				countDown--;
				if (countDown == 0) { // 0¿∏∑Œ µ«∏È

					if (controller != null && !controller.moveDown()) // æ∆∑°∑Œ ≥ª∑¡∞• ºˆ æ¯∞Ì ƒ¡∆Æ∑—∑Ø∞° «“¥Á¿Ã æ»µ«æÓ¿÷¿∏∏È
>>>>>>> create_LEVEL
						this.fixingTetrisBlock();
				}
				this.repaint();
				continue;
			}

			countMove--;
<<<<<<< HEAD
			if (countMove == 0) { // Ï≤òÏùåÏúºÎ°ú countMoveÍ∞Ä 0Ïù¥ ÎêòÎ©¥
				countMove = (21 - (int) comboSpeed.getSelectedItem()) * 5;
				if (controller != null && !controller.moveDown()) // ÏïÑÎûòÎ°ú ÎÇ¥Î†§Í∞à Ïàò ÏóÜÍ≥† Ïª®Ìä∏Î°§Îü¨Í∞Ä Ìï†ÎãπÏù¥ ÏïàÎêòÏñ¥ÏûàÏúºÎ©¥
					countDown = down; // 50ÏúºÎ°ú Ï¥àÍ∏∞Ìôî
				else
					this.showGhost(); // ÎÇ¥Î†§Í∞à Ïàò ÏûàÏúºÎ©¥ Í≥†Ïä§Ìä∏ Ï∂úÎ†•
			}

			if (countUp != 0) { // Ï≤òÏùåÏóêÎäî ÎÑòÏñ¥Í∞ê
=======
			if (countMove == 0) { // √≥¿Ω¿∏∑Œ countMove∞° 0¿Ã µ«∏È
				countMove = (21 - (int) comboSpeed.getSelectedItem()) * 5;
				if (controller != null && !controller.moveDown()) // æ∆∑°∑Œ ≥ª∑¡∞• ºˆ æ¯∞Ì ƒ¡∆Æ∑—∑Ø∞° «“¥Á¿Ã æ»µ«æÓ¿÷¿∏∏È
					countDown = down; // 50¿∏∑Œ √ ±‚»≠
				else
					this.showGhost(); // ≥ª∑¡∞• ºˆ ¿÷¿∏∏È ∞ÌΩ∫∆Æ √‚∑¬
			}

			if (countUp != 0) { // √≥¿Ωø°¥¬ ≥—æÓ∞®
>>>>>>> create_LEVEL
				countUp--;
				if (countUp == 0) {
					countUp = up;
					addBlockLine(1);
				}
			}

			this.repaint();
		} // while()
	}// run()

	public void dropBoard(int lineNumber, int num) {

		this.dropMap(lineNumber, num);

		this.changeTetrisBlockLine(lineNumber, num);

		this.checkMap();

		this.showGhost();
	}

	private void dropMap(int lineNumber, int num) {
		if (num == 1) {

			for (int i = lineNumber; i > 0; i--) {
				for (int j = 0; j < map[i].length; j++) {
					map[i][j] = map[i - 1][j];
				}
			}

			for (int j = 0; j < map[0].length; j++) {
				map[0][j] = null;
			}
		} else if (num == -1) {

			for (int i = 1; i <= lineNumber; i++) {
				for (int j = 0; j < map[i].length; j++) {
					map[i - 1][j] = map[i][j];
				}
			}

			for (int j = 0; j < map[0].length; j++) {
				map[lineNumber][j] = null;
			}
		}
	}

	private void changeTetrisBlockLine(int lineNumber, int num) {
		int y = 0, posY = 0;
		for (int i = 0; i < blockList.size(); i++) {
			y = blockList.get(i).getY();
			posY = blockList.get(i).getPosGridY();
			if (y <= lineNumber)
				blockList.get(i).setPosGridY(posY + num);
		}
	}

<<<<<<< HEAD
	private void fixingTetrisBlock() { // ÏΩ§Î≥¥ Ïó∞ÏÇ∞ -> ÏÉÅÎåÄÏóêÍ≤å Î∏îÎü≠ Ï∂îÍ∞Ä Î∞è ÏïÑÏù¥ÌÖú ÎûúÎç§ ÌöçÎìù
=======
	private void fixingTetrisBlock() { // ƒﬁ∫∏ ø¨ªÍ -> ªÛ¥Îø°∞‘ ∫Ì∑∞ √ﬂ∞° π◊ æ∆¿Ã≈€ ∑£¥˝ »πµÊ
>>>>>>> create_LEVEL
		synchronized (this) {
			if (stop) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		boolean isCombo = false;
		removeLineCount = 0;

		for (Block block : shap.getBlock()) {
<<<<<<< HEAD
			blockList.add(block); // Î∏îÎü≠ Ï∂îÍ∞Ä
=======
			blockList.add(block); // ∫Ì∑∞ √ﬂ∞°
>>>>>>> create_LEVEL
		}

		isCombo = checkMap();

		if (isCombo)
<<<<<<< HEAD
			removeLineCombo++; // ÏΩ§Î≥¥ Ï¶ùÍ∞Ä
		else
			removeLineCombo = 0; // Ïó∞ÏÜçÏúºÎ°ú Î™ª ÏßÄÏö∞Î©¥ ÏΩ§Î≥¥ 0ÏúºÎ°ú
=======
			removeLineCombo++; // ƒﬁ∫∏ ¡ı∞°
		else
			removeLineCombo = 0; // ø¨º”¿∏∑Œ ∏¯ ¡ˆøÏ∏È ƒﬁ∫∏ 0¿∏∑Œ
>>>>>>> create_LEVEL

		this.getFixBlockCallBack(blockList, removeLineCombo, removeLineCount);

		this.nextTetrisBlock();

		isHold = false;
	}// fixingTetrisBlock()

<<<<<<< HEAD
	private boolean checkMap() { // Î∏îÎü≠ÏùÑ ÏßÄÏö∞Í≥† ÏΩ§Î≥¥Î•º Îã¨ÏÑ±ÌñàÎäîÏßÄ ÏïåÎ†§Ï£ºÎäî Î©îÏÜåÎìú
=======
	private boolean checkMap() { // ∫Ì∑∞¿ª ¡ˆøÏ∞Ì ƒﬁ∫∏∏¶ ¥ﬁº∫«ﬂ¥¬¡ˆ æÀ∑¡¡÷¥¬ ∏ﬁº“µÂ
>>>>>>> create_LEVEL
		boolean isCombo = false;
		int count = 0;
		Block mainBlock;

		for (int i = 0; i < blockList.size(); i++) {
			mainBlock = blockList.get(i);

<<<<<<< HEAD
			if (mainBlock.getY() < 0 || mainBlock.getY() >= maxY) // Î≤óÏñ¥ÎÇòÎäî Í≤ΩÏö∞ forÎ¨∏ÏúºÎ°ú Îã§Ïãú ÎèåÏïÑÍ∞ê
				continue;

			if (mainBlock.getY() < maxY && mainBlock.getX() < maxX) // ÏïÑÎûòÏóê ÏûàÎçò Î∏îÎ°ùÏúÑÏóê Ïò¨Î¶º
				map[mainBlock.getY()][mainBlock.getX()] = mainBlock;

			if (mainBlock.getY() == 1 && mainBlock.getX() > 2 && mainBlock.getX() < 7) { // Í≤åÏûÑÏò§Î≤Ñ(Ï¢åÏÉÅÎã®Ïù¥ 0,0)
=======
			if (mainBlock.getY() < 0 || mainBlock.getY() >= maxY) // π˛æÓ≥™¥¬ ∞ÊøÏ forπÆ¿∏∑Œ ¥ŸΩ√ µπæ∆∞®
				continue;

			if (mainBlock.getY() < maxY && mainBlock.getX() < maxX) // æ∆∑°ø° ¿÷¥¯ ∫Ì∑œ¿ßø° ø√∏≤
				map[mainBlock.getY()][mainBlock.getX()] = mainBlock;

			if (mainBlock.getY() == 1 && mainBlock.getX() > 2 && mainBlock.getX() < 7) { // ∞‘¿”ø¿πˆ(¡¬ªÛ¥‹¿Ã 0,0)
>>>>>>> create_LEVEL
				this.gameEndCallBack();
				break;
			}

			count = 0;
			for (int j = 0; j < maxX; j++) {
<<<<<<< HEAD
				if (map[mainBlock.getY()][j] != null) // Í∞ÄÎ°úÏ§ÑÏù¥ Ï±ÑÏõåÏßÄÎäî Í≥ºÏ†ï Ï≤¥ÌÅ¨
=======
				if (map[mainBlock.getY()][j] != null) // ∞°∑Œ¡Ÿ¿Ã √§øˆ¡ˆ¥¬ ∞˙¡§ √º≈©
>>>>>>> create_LEVEL
					count++;

			}

<<<<<<< HEAD
			if (count == maxX) { // Í∞ÄÎ°úÏ§ÑÏù¥ Î™®Îëê Ï±ÑÏõåÏßÄÎ©¥
				removeLineCount++; // ÏßÄÏö¥ Ï§Ñ Ï∂îÍ∞Ä
				this.removeBlockLine(mainBlock.getY()); // Ï§Ñ ÏßÄÏõÄ
				isCombo = true; // ÏΩ§Î≥¥ true
=======
			if (count == maxX) { // ∞°∑Œ¡Ÿ¿Ã ∏µŒ √§øˆ¡ˆ∏È
				removeLineCount++; // ¡ˆøÓ ¡Ÿ √ﬂ∞°
				removeLineTemp += removeLineCount * 10; // º”µµ ¡°ºˆ ∞ËªÍ
				removeLineSum += removeLineCount * 10; // ¡°ºˆ ∞ËªÍ
				if(removeLineTemp >= 100) { // º”µµ ¡°ºˆ∞° 100¿Ã ≥—¿∏∏È ∑π∫ß æ˜
					tetris.changeSpeed(2*level++); // º”µµ¥¬ 2, 4, 6, 8 .. ∑Œ ø√∂Û∞®
					removeLineTemp = 0; // º”µµ ¡°ºˆ √ ±‚»≠
				}
				this.removeBlockLine(mainBlock.getY()); // ¡Ÿ ¡ˆøÚ
				isCombo = true; // ƒﬁ∫∏ true
>>>>>>> create_LEVEL
			}
		}
		return isCombo;
	}

<<<<<<< HEAD
	public void nextTetrisBlock() { // Îã§Ïùå Î∏îÎü≠ÏùÑ Î∂àÎü¨Ïò§Îäî Î©îÏÜåÎìú
=======
	public void nextTetrisBlock() { // ¥Ÿ¿Ω ∫Ì∑∞¿ª ∫“∑Øø¿¥¬ ∏ﬁº“µÂ
>>>>>>> create_LEVEL
		shap = nextBlocks.get(0);
		this.initController();
		nextBlocks.remove(0);
		nextBlocks.add(getRandomTetrisBlock());
	}

	private void initController() {
		controller.setBlock(shap);
		ghost = getBlockClone(shap, true);
		controllerGhost.setBlock(ghost);
	}

<<<<<<< HEAD
	public void removeBlockLine(int lineNumber) { // privateÏóêÏÑú publicÏúºÎ°ú Î≥ÄÍ≤Ω
=======
	public void removeBlockLine(int lineNumber) { // privateø°º≠ public¿∏∑Œ ∫Ø∞Ê
>>>>>>> create_LEVEL

		for (int j = 0; j < maxX; j++) {
			for (int s = 0; s < blockList.size(); s++) {
				Block b = blockList.get(s);
				if (b == map[lineNumber][j])
					blockList.remove(s);
			}
			map[lineNumber][j] = null;
		} // for(j)

		this.dropBoard(lineNumber, 1);
	}

	public void gameEndCallBack() {
		client.gameover();
		this.isPlay = false;
	}

	private void showGhost() {
		ghost = getBlockClone(shap, true);
		controllerGhost.setBlock(ghost);
		controllerGhost.moveQuickDown(shap.getPosY(), true);
	}

<<<<<<< HEAD
	public TetrisBlock getRandomTetrisBlock() { // ÎûúÎç§ÏúºÎ°ú Î∏îÎü≠ ÏÉùÏÑ±
=======
	public TetrisBlock getRandomTetrisBlock() { // ∑£¥˝¿∏∑Œ ∫Ì∑∞ ª˝º∫
>>>>>>> create_LEVEL
		switch ((int) (Math.random() * 7)) {
		case TetrisBlock.TYPE_CENTERUP:
			return new CenterUp(4, 1);
		case TetrisBlock.TYPE_LEFTTWOUP:
			return new LeftTwoUp(4, 1);
		case TetrisBlock.TYPE_LEFTUP:
			return new LeftUp(4, 1);
		case TetrisBlock.TYPE_RIGHTTWOUP:
			return new RightTwoUp(4, 1);
		case TetrisBlock.TYPE_RIGHTUP:
			return new RightUp(4, 1);
		case TetrisBlock.TYPE_LINE:
			return new Line(4, 1);
		case TetrisBlock.TYPE_NEMO:
			return new Nemo(4, 1);
		}
		return null;
	}

	public TetrisBlock getBlockClone(TetrisBlock tetrisBlock, boolean isGhost) {
		TetrisBlock blocks = null;
		switch (tetrisBlock.getType()) {
		case TetrisBlock.TYPE_CENTERUP:
			blocks = new CenterUp(4, 1);
			break;
		case TetrisBlock.TYPE_LEFTTWOUP:
			blocks = new LeftTwoUp(4, 1);
			break;
		case TetrisBlock.TYPE_LEFTUP:
			blocks = new LeftUp(4, 1);
			break;
		case TetrisBlock.TYPE_RIGHTTWOUP:
			blocks = new RightTwoUp(4, 1);
			break;
		case TetrisBlock.TYPE_RIGHTUP:
			blocks = new RightUp(4, 1);
			break;
		case TetrisBlock.TYPE_LINE:
			blocks = new Line(4, 1);
			break;
		case TetrisBlock.TYPE_NEMO:
			blocks = new Nemo(4, 1);
			break;
		}
		if (blocks != null && isGhost) {
			blocks.setGhostView(isGhost);
			blocks.setPosX(tetrisBlock.getPosX());
			blocks.setPosY(tetrisBlock.getPosY());
			blocks.rotation(tetrisBlock.getRotationIndex());
		}
		return blocks;
	}

	public void getFixBlockCallBack(ArrayList<Block> blockList, int removeCombo, int removeMaxLine) {
<<<<<<< HEAD
		// ÏùºÏ†ï Î∏îÎü≠ÏùÑ ÏßÄÏö∞Î©¥ ÏïÑÏù¥ÌÖúÏù¥ ÎûúÎç§ÏúºÎ°ú Îì±Ïû•
		if (removeCombo < 3) {
			if (removeMaxLine == 3) {
				client.addBlock(1);
				client.useItem(2/* (int) (Math.random() * MAX_ITEM_NUM + MIN_ITEM_NUM) */);// ÏïÑÏù¥ÌÖú ÎûúÎç§ÏúºÎ°ú ÏÉùÏÑ±, 1~4 ÎûúÎç§ÏúºÎ°ú ÎÑòÍ≤®Ï§å
			} else if (removeMaxLine == 4) {
				client.addBlock(3);
				client.useItem(2/* (int) (Math.random() * MAX_ITEM_NUM + MIN_ITEM_NUM) */);
=======
		// ¿œ¡§ ∫Ì∑∞¿ª ¡ˆøÏ∏È æ∆¿Ã≈€¿Ã ∑£¥˝¿∏∑Œ µÓ¿Â
		if (removeCombo < 3) {
			if (removeMaxLine == 3) {
				client.addBlock(1);
				client.useItem( (int) (Math.random() * MAX_ITEM_NUM + MIN_ITEM_NUM) );// æ∆¿Ã≈€ ∑£¥˝¿∏∑Œ ª˝º∫, 1~4 ∑£¥˝¿∏∑Œ ≥—∞‹¡‹
			} else if (removeMaxLine == 4) {
				client.addBlock(3);
				client.useItem( (int) (Math.random() * MAX_ITEM_NUM + MIN_ITEM_NUM) );
>>>>>>> create_LEVEL
			}

		} else if (removeCombo < 10) {
			if (removeMaxLine == 3)
				client.addBlock(2);
			else if (removeMaxLine == 4)
				client.addBlock(4);
			else
				client.addBlock(1);

		} else {
			if (removeMaxLine == 3)
				client.addBlock(3);
			else if (removeMaxLine == 4)
				client.addBlock(5);
			else
				client.addBlock(2);
		}
	}

	public void playBlockHold() {
		if (isHold)
			return;

		if (hold == null) {
			hold = getBlockClone(shap, false);
			this.nextTetrisBlock();
		} else {
			TetrisBlock tmp = getBlockClone(shap, false);
			shap = getBlockClone(hold, false);
			hold = getBlockClone(tmp, false);
			this.initController();
		}

		isHold = true;
	}

	boolean stop = false;

	public void addBlockLine(int numOfLine) {
		stop = true;

		Block block;
		int rand = (int) (Math.random() * maxX);
		for (int i = 0; i < numOfLine; i++) {
			this.dropBoard(maxY - 1, -1);
			for (int col = 0; col < maxX; col++) {
				if (col != rand) {
					block = new Block(0, 0, Color.GRAY, Color.GRAY);
					block.setPosGridXY(col, maxY - 1);
					blockList.add(block);
					map[maxY - 1][col] = block;
				}
			}

			boolean up = false;
			for (int j = 0; j < shap.getBlock().length; j++) {
				Block sBlock = shap.getBlock(j);
				if (map[sBlock.getY()][sBlock.getX()] != null) {
					up = true;
					break;
				}
			}
			if (up) {
				controller.moveDown(-1);
			}
		}

		this.showGhost();
		this.repaint();
		synchronized (this) {
			stop = false;
			this.notify();
		}
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			messageArea.requestFocus();
		}
		if (!isPlay)
			return;
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			controller.moveLeft();
			controllerGhost.moveLeft();
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			controller.moveRight();
			controllerGhost.moveRight();
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			controller.moveDown();
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			controller.nextRotationLeft();
			controllerGhost.nextRotationLeft();
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			controller.moveQuickDown(shap.getPosY(), true);
			this.fixingTetrisBlock();
		} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			playBlockHold();
		}
		this.showGhost();
		this.repaint();
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		this.requestFocus();
	}

	public void mouseReleased(MouseEvent e) {
	}

<<<<<<< HEAD
	public void actionPerformed(ActionEvent e) { // Í≤åÏûÑÏãúÏûë Î©îÏÜåÎìú
		if (e.getSource() == btnStart) { // ÏãúÏûëÌïòÍ∏∞ Î≤ÑÌäº ÎàÑÎ•º Ïãú
			if (client != null) { // ÌÅ¥ÎùºÏù¥Ïñ∏Ìä∏Í∞Ä Ï°¥Ïû¨ÌïòÎ©¥
				client.gameStart((int) comboSpeed.getSelectedItem()); // Ïä§ÌîºÎìú Ï†ïÎ≥¥Î•º Í∏∞Î∞òÏúºÎ°ú ÌÅ¥ÎùºÏù¥Ïñ∏Ìä∏Ïùò gameStart Î©îÏÜåÎìú Ïã§Ìñâ
			} else { // ÌÅ¥ÎùºÏù¥Ïñ∏Ìä∏Í∞Ä Ï°¥Ïû¨ÌïòÏßÄ ÏïäÏúºÎ©¥
=======
	public void actionPerformed(ActionEvent e) { // ∞‘¿”Ω√¿€ ∏ﬁº“µÂ
		if (e.getSource() == btnStart) { // Ω√¿€«œ±‚ πˆ∆∞ ¥©∏¶ Ω√
			if (client != null) { // ≈¨∂Û¿Ãæ∆Æ∞° ¡∏¿Á«œ∏È
				client.gameStart((int) comboSpeed.getSelectedItem()); // Ω∫««µÂ ¡§∫∏∏¶ ±‚π›¿∏∑Œ ≈¨∂Û¿Ãæ∆Æ¿« gameStart ∏ﬁº“µÂ Ω««‡
			} else { // ≈¨∂Û¿Ãæ∆Æ∞° ¡∏¿Á«œ¡ˆ æ ¿∏∏È
>>>>>>> create_LEVEL
				this.gameStart((int) comboSpeed.getSelectedItem());
			}
		} else if (e.getSource() == btnExit) {
			if (client != null) {
				if (tetris.isNetwork()) {
					client.closeNetwork(tetris.isServer());
				}
			} else {
				System.exit(0);
			}

		}
	}

	public boolean isPlay() {
		return isPlay;
	}

	public void setPlay(boolean isPlay) {
		this.isPlay = isPlay;
	}

	public JButton getBtnStart() {
		return btnStart;
	}

	public JButton getBtnExit() {
		return btnExit;
	}

	public void setClient(GameClient client) {
		this.client = client;
	}

	public void printSystemMessage(String msg) {
		systemMsg.printMessage(msg);
	}

	public void printMessage(String msg) {
		messageArea.printMessage(msg);
	}

	public GameClient getClient() {
		return client;
	}

	public void changeSpeed(Integer speed) {
		comboSpeed.setSelectedItem(speed);
	}

	public void clearMessage() {
		messageArea.clearMessage();
		systemMsg.clearMessage();
	}

}