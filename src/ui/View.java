package ui;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.wb.swt.SWTResourceManager;

import dotdotdot.Parser;

public class View {
	
	//private static Parser parser = new Parser();
	private static ArrayList<String> list;
	private static int borderSize;
	private Shell shell;
	private final String GUI_TITLE = "Dotdotdot";
	protected final static String GUI_HINT = "< Input ? or help to show available commands >";
	protected final static String EMPTY_STRING = "";

	protected final static Color hintColor = Display.getDefault().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND);
	protected final static Color normalColor = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);

	protected final static int BORDER_WIDTH = 2;
	protected static final int SCROLL_AMOUNT = 5;
	
	private Text input;
	private Label dayLabel;
	private Label dateLabel;
	private Label timeLabel;
	private ToolTip notification;
	private Table categoryTable;
	private Table mainTable;
	
	public View(){

		shell = new Shell(SWT.CLOSE | SWT.TITLE | SWT.MIN);
		shell.setSize(725, 605);
		shell.setText(GUI_TITLE);
			
		categoryTable = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		categoryTable.setBounds(10, 102, 180, 408);

		mainTable = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		mainTable.setBounds(203, 10, 506, 500);

		input = new Text(shell, SWT.BORDER);
		input.setBounds(10, 522, 699, 31);
		input.setFocus();
		
		dayLabel = new Label(shell, SWT.NONE);
		dayLabel.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
		dayLabel.setAlignment(SWT.CENTER);
		dayLabel.setBounds(10, 10, 180, 31);
		
		dateLabel = new Label(shell, SWT.NONE);
		dateLabel.setAlignment(SWT.CENTER);
		dateLabel.setBounds(10, 41, 180, 25);
		
		timeLabel = new Label(shell, SWT.NONE);
		timeLabel.setAlignment(SWT.CENTER);
		timeLabel.setBounds(10, 67, 180, 29);
		
		notification = new ToolTip(shell, SWT.TOOL | SWT.ICON_INFORMATION | SWT.RIGHT);
		
	}
	
	public Shell getShell(){
		return shell;
	}
	
	public Table getCategoryTable(){
		return categoryTable;
	}

	public Table getMainTable(){
		return mainTable;
	}

	public Label getDayLabel(){
		return dayLabel;
	}
	
	public Label getDateLabel(){
		return dateLabel;
	}
	
	public Label getTimeLabel(){
		return timeLabel;
	}
	
	public Text getInput(){
		return input;
	}
	
	public ToolTip getNotification(){
		return notification;
	}
}
