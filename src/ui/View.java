package ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.wb.swt.SWTResourceManager;

public class View {
	
	private Shell shell;
	private final String GUI_TITLE = "Dotdotdot";
	protected final static String GUI_HINT = "< Input ? or help to show available commands >";
	protected final static String EMPTY_STRING = "";

	protected final static Color hintColor = Display.getDefault().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND);
	protected final static Color normalColor = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
	protected final static Color orangeColor = new Color (Display.getCurrent(), 251, 160, 38);
	
	protected final static int BORDER_WIDTH = 2;
	protected final static int SCROLL_AMOUNT = 5;
	
	protected final static int MSG_SIZE = 13;
	
	private StyledText input;
	private Label dayLabel;
	private Label dateLabel;
	private Label timeLabel;
	private ToolTip notification;
	private Table categoryTable;
	private Table mainTable;
	
	public View(){

		shell = new Shell(SWT.CLOSE | SWT.MIN | SWT.TITLE);
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		shell.setSize(725, 625);
		shell.setText(GUI_TITLE);
			
		categoryTable = new Table(shell, SWT.FULL_SELECTION);
		categoryTable.setBounds(10, 106, 180, 420);

		mainTable = new Table(shell, SWT.FULL_SELECTION);
		mainTable.setBounds(215, 14, 494, 512);

		input = new StyledText(shell, SWT.WRAP);
		input.setTopMargin(6);
		input.setRightMargin(10);
		input.setLeftMargin(10);
		input.setBounds(10, 532, 699, 39);
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
		dayLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		dayLabel.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
		dayLabel.setAlignment(SWT.CENTER);
		dayLabel.setBounds(10, 14, 180, 31);
		
		dateLabel = new Label(shell, SWT.NONE);
		dateLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		dateLabel.setAlignment(SWT.CENTER);
		dateLabel.setBounds(10, 45, 180, 25);
		
		timeLabel = new Label(shell, SWT.NONE);
		timeLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		timeLabel.setAlignment(SWT.CENTER);
		timeLabel.setBounds(10, 71, 180, 29);
		
		notification = new ToolTip(shell, SWT.TOOL | SWT.ICON_INFORMATION | SWT.RIGHT);
		
		Label seperator = new Label(shell, SWT.NONE);
		seperator.setBounds(196, 14, 2, 510);
		seperator.setBackground(orangeColor);
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
	
	public ToolTip getNotification(){
		return notification;
	}
}
