package example.client.view;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;

import example.client.presenter.MenuBarPresenter;
import example.client.presenter.ReportsDialogPresenter;
import example.client.presenter.ReportsDialogPresenterImpl;

public class MenuBarWidget extends Composite implements MenuBarPresenter.Display{

	private final MenuBar mb;
	private DialogBox dv;

	public MenuBarWidget() {
		final FlowPanel panel = new FlowPanel();
		panel.setStyleName("menu");
		initWidget(panel);
		mb = createMenuBar();
		
		panel.add(mb);
	}
	
	private MenuBar createMenuBar() {
		MenuBar menu = new MenuBar();
		menu.setAutoOpen(true);
		menu.setAnimationEnabled(true);

		// Create a sub menu of recent documents
		MenuBar recentDocsMenu = new MenuBar(true);
		Command menuCommand = new Command() {

			public void execute() {
				dv.show();
			}
		};

		recentDocsMenu.addItem("Reports", menuCommand);

		// Create the file menu
		MenuBar fileMenu = new MenuBar(true);
		fileMenu.setAnimationEnabled(true);
		menu.addItem(new MenuItem("File", fileMenu));
		fileMenu.addSeparator();
		fileMenu.addItem("testing", recentDocsMenu);
		fileMenu.addSeparator();
		fileMenu.addItem("testing", menuCommand);

		// Create the edit menu
		MenuBar editMenu = new MenuBar(true);
		menu.addItem(new MenuItem("Edit", editMenu));
		editMenu.addItem(new MenuItem("Testing", menuCommand));

		/**
		 * Create a dynamic tree that will add a random number of children to
		 * each node as it is clicked.
		 * 
		 * @return the new tree
		 */
		return menu;
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	public DialogBox getDv() {
		return dv;
	}

	@Override
	public void setDV(DialogBox dv) {
		this.dv = dv;
	}

}
