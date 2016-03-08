package ui;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

import org.apache.commons.lang3.text.WordUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.wb.swt.SWTResourceManager;

import logic.Logic;
import parser.Parser;;

public class Controller {
	
	private View view;
	private int borderSize;
    private Parser parser = new Parser();
    private Logic logic = new Logic();
    
	public Controller(){
		view = new View();
		timer();
		inputToHint();
		addKeyListener();
		view.getDateLabel().setText(getCurrentDate());
		view.getDayLabel().setText(getCurrentDay());
		view.getTimeLabel().setText(getCurrentTime());
		displayCategory();
		//displayList();
	}
	
	private void displayCategory(){
		view.getCategoryTable().removeAll();
		TableItem categoryItem;
/*
		// Call logic for list
		ArrayList<String> categories = logic.getListOfCategoriesWithCount();
		for(int i =0 ; i < categories.size(); i++){
			categoryItem = new TableItem(categoryTable, SWT.NONE);
			categoryItem.setText(categories.get(i));
		}
	*/	
	}
	
	private void displayList() {

		view.getMainTable().removeAll();
		TableItem mainItem;
		ArrayList<String> list = null;
		/*
		for (int i = 0; i < list.size(); i++) {
	
			String[] taskIDandDesc = getTaskIdAndDesc(list.get(i));
			String formattedOutput = WordUtils.wrap(taskIDandDesc[TASK_DESC], WRAP_AROUND, "\n", true);
			String outputArray[] = formattedOutput.split("\n");
			for (int j = 0; j < outputArray.length; j++) {

				mainItem = new TableItem(view.getMainTable(), SWT.NONE);

				if (j == 0) {
					mainItem.setText(taskIDandDesc[TASK_ID] + " " +outputArray[j]);
				} else {
					String whiteSpaces = "";
					for(int z = 0 ; z < taskIDandDesc[TASK_ID].length() + DEFAULT_WHITESPACES; z++){
						whiteSpaces += " ";
					}
					mainItem.setText(whiteSpaces + outputArray[j]);
				}
			}
			
		}
		*/

	}
	
	private void displayHelp() {

		Table mainTable = view.getMainTable();
		mainTable.removeAll();

		TableItem mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		mainItem.setText("Adding tasks");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText("add <TODO>");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText("add <TODO> (at | by | on | to) <date> [@category]");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.ITALIC));
		mainItem.setText("Eg. add do CS2103 tutorial by Sun");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.ITALIC));
		mainItem.setText("      add buy milk by 15Feb @shopping");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		mainItem.setText("Edit tasks");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText("edit <task_ID#> to <date>");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.ITALIC));
		mainItem.setText("Eg. edit 1 to 15Feb");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		mainItem.setText("Add priority");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText("set <task_ID#> to <priority#>");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.ITALIC));
		mainItem.setText("Eg. set 2 to 10");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		mainItem.setText("Complete Tasks");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText("do <TODO | task_ID#>");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.ITALIC));
		mainItem.setText("Eg. do receive quest");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.ITALIC));
		mainItem.setText("      do 1");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		mainItem.setText("View tasks");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText("view <category>");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.ITALIC));
		mainItem.setText("Eg. view shopping");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		mainItem.setText("Show help");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText("[? | help | h]");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		mainItem.setText("Undo previous command");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText("[u | undo]");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		mainItem.setText("Keyboard shortcuts");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText("Undo previous command: Ctrl+Z");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText("Scroll through command history: \u2191 or \u2193");

	}
	
	private void addKeyListener(){
		
		Text input = view.getInput();
		ToolTip tip = view.getNotification();
		input.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent event) {

				switch (event.keyCode) {

				case SWT.CR:
					// SWT.CR : when "ENTER" key is pressed
					String tempInput = input.getText();
					inputToHint();
					tip.setVisible(false);
					tip.setMessage(View.EMPTY_STRING);
					/*
					parser.input(tempInput);
				
					if(parser.getIsViewOrHelp()==Parser.HELP_VIEW){
						displayHelp();
					} else {
						list = parser.getList();
						tip.setText(parser.getNotifyTitle());
							
						String notifyMsg = parser.getNotifyMsg();
	
						if(!notifyMsg.equals(EMPTY_STRING)){
							tip.setMessage(notifyMsg);
						}
						tip.setLocation(new Point(shell.getLocation().x + shell.getSize().x - parser.getMsgSize() ,
									shell.getLocation().y + borderSize));
						tip.setVisible(true);
						displayList();
						displayCategory();
					}
					*/
					break;
				case SWT.ARROW_UP:
					view.getMainTable().setTopIndex(view.getMainTable().getTopIndex() - View.SCROLL_AMOUNT);
					event.doit = false;
					break;
				case SWT.ARROW_DOWN:
					view.getMainTable().setTopIndex(view.getMainTable().getTopIndex() + View.SCROLL_AMOUNT);
					event.doit = false;
					break;
				case SWT.ESC:
					System.out.println(SWT.ESC);
					break;
				case SWT.BS:
					if (isTextEmpty(input)) {
						inputToHint();
					} else if (input.getForeground().equals(View.hintColor)) {
						inputToNormal();
					}
					break;
				default:
					// removes hint and changes input back to normal
					if (input.getForeground().equals(View.hintColor)) {
						inputToNormal();
					}
					break;
				}
			}
		});
		
	}
	
	private void timer(){
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
		    @Override
		    public void run() {
		        // Task to be executed every second
		        try {
		            SwingUtilities.invokeAndWait(new Runnable() {

		                @Override
		                public void run() {
		                	Display.getDefault().asyncExec(new Runnable() {
		                	    public void run() {
		                	    	view.getTimeLabel().setText(getCurrentTime());
		                	    }
		                	});
		                }
		            });
		        } catch (InterruptedException e) {
		          
		        } catch (InvocationTargetException e) {
					
				}

		    }
		};

		// This will invoke the timer every second
		timer.scheduleAtFixedRate(task, 1000, 1000);
	}
	
	public String getCurrentDate(){
	    Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    return dateFormat.format(date);
	}
	
	public String getCurrentTime(){
	    Date date = new Date();
	    DateFormat timeFormat = new SimpleDateFormat("hh:mm a");
	    return timeFormat.format(date);
	}
	
	public String getCurrentDay(){
	    Date date = new Date();
		DateFormat dayFormat = new SimpleDateFormat("EEEEEEE");
	    return dayFormat.format(date);
	}
	
	private void inputToHint() {
		view.getInput().setText(View.GUI_HINT);
		view.getInput().setForeground(View.hintColor);
	}

	private void inputToNormal() {
		view.getInput().setText(View.EMPTY_STRING);
		view.getInput().setForeground(View.normalColor);
	}
	
	public void initBorderSize() {
		Rectangle outer = Display.getCurrent().getActiveShell().getBounds();
        Rectangle inner = Display.getCurrent().getActiveShell().getClientArea();
        borderSize = outer.height - inner.height - View.BORDER_WIDTH;
	}
	
	private boolean isTextEmpty(Text t) {
		return t.getText().length() == 1;
	}
	
	public View getView(){
		return view;
	}
}
