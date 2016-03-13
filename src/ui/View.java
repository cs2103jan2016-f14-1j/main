package ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.wb.swt.SWTResourceManager;

public class View {
	
	private Shell shell;
	private final String GUI_TITLE = "Dotdotdot";
	protected final static String GUI_HINT = "< Input ? or help to show available commands >";
	protected final static String EMPTY_STRING = "";

	protected final static Color hintColor = Display.getDefault().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND);
	protected final static Color normalColor = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
	protected final static Color orangeColor = new Color (Display.getCurrent(), 255, 116, 23);
	//protected final static Color blackGrayColor = new Color (Display.getCurrent(), 36, 45, 62);
	protected final static Color blackGrayColor = new Color (Display.getCurrent(), 50, 55, 60);
	//protected final static Color blackGrayColor = new Color (Display.getCurrent(), 35, 40, 45);
	
	protected final static int BORDER_WIDTH = 2;
	protected final static int SCROLL_AMOUNT = 5;
	
	protected final static int MSG_SIZE = 13;
	
	private StyledText input;
	private Label dayLabel;
	private Label dateLabel;
	private Label notification;
	private Label timeLabel;
	private Table categoryTable;
	private Table mainTable;
	
	public View(){

		shell = new Shell(SWT.CLOSE | SWT.MIN | SWT.TITLE);
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		shell.setSize(725, 625);
		shell.setText(GUI_TITLE);
			
		categoryTable = new Table(shell, SWT.FULL_SELECTION);
		categoryTable.setFont(SWTResourceManager.getFont("Trebuchet MS", 9, SWT.NORMAL));
		categoryTable.setBounds(10, 106, 170, 479);
		categoryTable.setBackground(blackGrayColor);
		categoryTable.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		mainTable = new Table(shell, SWT.FULL_SELECTION);
		mainTable.setFont(SWTResourceManager.getFont("Trebuchet MS", 9, SWT.NORMAL));
		mainTable.setBounds(196, 14, 513, 485);

		input = new StyledText(shell, SWT.WRAP);
		input.setFont(SWTResourceManager.getFont("Trebuchet MS", 9, SWT.NORMAL));
		input.setTopMargin(8);
		input.setRightMargin(10);
		input.setLeftMargin(10);
		input.setBounds(196, 536, 513, 39);
		input.setFocus();
		
		input.addPaintListener(new PaintListener() {
	        @Override
	        public void paintControl(PaintEvent e) {
	            e.gc.setAntialias(SWT.ON);
	            e.gc.setForeground(View.orangeColor);
	            e.gc.setLineWidth(2);
	            e.gc.drawRoundRectangle(1, 1, input.getBounds().width-2, input.getBounds().height-2, 12, 12);
	        }
	    });
		
		dayLabel = new Label(shell, SWT.NONE);
		dayLabel.setFont(SWTResourceManager.getFont("Trebuchet MS", 11, SWT.BOLD));
		dayLabel.setAlignment(SWT.CENTER);
		dayLabel.setBounds(0, 14, 190, 31);
		dayLabel.setBackground(blackGrayColor);
		dayLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		dateLabel = new Label(shell, SWT.NONE);
		dateLabel.setFont(SWTResourceManager.getFont("Trebuchet MS", 9, SWT.NORMAL));
		dateLabel.setAlignment(SWT.CENTER);
		dateLabel.setBounds(0, 45, 190, 25);
		dateLabel.setBackground(blackGrayColor);
		dateLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		timeLabel = new Label(shell, SWT.NONE);
		timeLabel.setFont(SWTResourceManager.getFont("Trebuchet MS", 9, SWT.NORMAL));
		timeLabel.setAlignment(SWT.CENTER);
		timeLabel.setBounds(0, 71, 190, 29);
		timeLabel.setBackground(blackGrayColor);
		timeLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		Label seperator = new Label(shell, SWT.NONE);
		seperator.setBounds(0, 0, 190, 585);
		seperator.setBackground(blackGrayColor);
		
		notification = new Label(shell, SWT.NONE);
		notification.setFont(SWTResourceManager.getFont("Trebuchet MS", 9, SWT.NORMAL));
		notification.setAlignment(SWT.CENTER);
		notification.setForeground(orangeColor);
		notification.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		notification.setBounds(196, 505, 513, 25);
		
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
	
	public StyledText getInput(){
		return input;
	}
	
	public Label getNotification(){
		return notification;
	}
}
