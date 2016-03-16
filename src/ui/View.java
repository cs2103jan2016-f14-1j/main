package ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.time.Month;
import java.util.logging.Level;
import java.util.logging.Logger;

public class View {
	
	private Shell shell;
	private final String GUI_TITLE = "Dotdotdot";
	protected final static String GUI_HINT = "< Input ? or help to show available commands >";
	protected final static String EMPTY_STRING = "";
    private final static String APP_ICON = "images/logo.png";
	protected final static Color hintColor = Display.getDefault().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND);
	protected final static Color normalColor = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
	protected final static Color orangeColor = new Color (Display.getCurrent(), 255, 116, 23);
	protected final static Color whiteColor = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
	//protected final static Color blackGrayColor = new Color (Display.getCurrent(), 36, 45, 62);
	protected final static Color blackGrayColor = new Color (Display.getCurrent(), 246, 246, 246);
	//protected final static Color blackGrayColor = new Color (Display.getCurrent(), 35, 40, 45);
	
	protected final static Font normalFont = SWTResourceManager.getFont("Trebuchet MS", 9, SWT.NORMAL);
	protected final static Font boldFont = SWTResourceManager.getFont("Trebuchet MS", 9, SWT.BOLD);
	protected final static Font headingFont = SWTResourceManager.getFont("Trebuchet MS", 12, SWT.BOLD);
	protected final static Font italicFont = SWTResourceManager.getFont("Trebuchet MS", 9, SWT.BOLD);
	
	protected final static int BORDER_WIDTH = 2;
	protected final static int SCROLL_AMOUNT = 5;
	
	private StyledText input;
	private Label dayLabel;
	private Label dateLabel;
	private Label notification;
	private Label timeLabel;
	private Table categoryTable;
	private Table mainTable;
	
	public View(){

		shell = new Shell(SWT.CLOSE | SWT.MIN | SWT.TITLE);
		
		shell.setBackground(whiteColor);
		shell.setSize(725, 625);
		shell.setText(GUI_TITLE);
		shell.setImage(new Image(Display.getCurrent(), Thread.currentThread().getContextClassLoader().getResourceAsStream(APP_ICON)));
		setCenterOfScreen();
		
		// For shortcut command
		shell.addListener(SWT.Close, new Listener() {
			
			@Override
			public void handleEvent(org.eclipse.swt.widgets.Event arg0) {
				// TODO Auto-generated method stub
				arg0.doit = false;
				shell.setVisible(false);
				try {
		            GlobalScreen.registerNativeHook();
		        }
		        catch (NativeHookException ex) {
		            System.exit(0);
		        }

		        GlobalScreen.addNativeKeyListener(new MainGUI());
			}
	    });
		
		categoryTable = new Table(shell, SWT.FULL_SELECTION);
		Button invisibleButton = new Button(shell, SWT.NONE);
		invisibleButton.setVisible(false);
		categoryTable.setFont(normalFont);
		categoryTable.setBounds(10, 125, 170, 459);
		categoryTable.setBackground(blackGrayColor);
		categoryTable.setForeground(normalColor);
		categoryTable.addListener(SWT.Selection, new Listener()
	    {
	        @Override
	        public void handleEvent(Event event)
	        {
	        	categoryTable.deselectAll();
	        	invisibleButton.setFocus();
	        	invisibleButton.forceFocus();
	        }
	    });
		
		mainTable = new Table(shell, SWT.FULL_SELECTION);
		mainTable.setFont(normalFont);
		mainTable.setBounds(196, 10, 513, 489);
		mainTable.addListener(SWT.Selection, new Listener()
	    {
	        @Override
	        public void handleEvent(Event event)
	        {
	        	mainTable.deselectAll();
	        	invisibleButton.setFocus();
	        	invisibleButton.forceFocus();
	        }
	    });
		
		input = new StyledText(shell, SWT.WRAP);
		input.setFont(normalFont);
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
		dayLabel.setFont(SWTResourceManager.getFont("Trebuchet MS", 14, SWT.BOLD));
		dayLabel.setAlignment(SWT.CENTER);
		dayLabel.setBounds(0, 15, 190, 45);
		dayLabel.setBackground(blackGrayColor);
		dayLabel.setForeground(normalColor);
		
		dateLabel = new Label(shell, SWT.NONE);
		dateLabel.setFont(normalFont);
		dateLabel.setAlignment(SWT.CENTER);
		dateLabel.setBounds(0, 60, 190, 30);
		dateLabel.setBackground(blackGrayColor);
		dateLabel.setForeground(normalColor);
		
		timeLabel = new Label(shell, SWT.NONE);
		timeLabel.setFont(normalFont);
		timeLabel.setAlignment(SWT.CENTER);
		timeLabel.setBounds(0, 90, 190, 30);
		timeLabel.setBackground(blackGrayColor);
		timeLabel.setForeground(normalColor);
		
		Label seperator = new Label(shell, SWT.NONE);
		seperator.setBounds(0, 0, 190, 585);
		seperator.setBackground(blackGrayColor);
		
		notification = new Label(shell, SWT.NONE);
		notification.setFont(normalFont);
		notification.setAlignment(SWT.CENTER);
		notification.setForeground(orangeColor);
		notification.setBackground(whiteColor);
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
	
	public void setCenterOfScreen(){
		Rectangle screenSize = Display.getCurrent().getPrimaryMonitor().getBounds();
		shell.setLocation((screenSize.width - shell.getBounds().width) / 2, (screenSize.height - shell.getBounds().height) / 2);
	}
}
