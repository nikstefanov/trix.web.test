package example.client.view;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Widget;

import example.client.presenter.MainPresenter;

public class MainWidget extends Composite implements MainPresenter.Display {
	
	private final DockPanel panel;
//	private final DockPanel panel;
	private Widget content;

	public MainWidget() {

		panel = new DockPanel();
		panel.setStyleName("main");
		initWidget(panel);
		panel.setWidth(Window.getClientWidth() + "px");
	}
	
	public void removeContent() {
		if (content != null) {
			panel.remove(content);
		}
	}

	public void addContentPanel(org.enunes.gwt.mvp.client.view.Display display) {
		removeContent();
		content = display.asWidget();
		panel.add(content, DockPanel.CENTER);
	}

	public void addMenu(org.enunes.gwt.mvp.client.view.Display display) {
		panel.add(display.asWidget(), DockPanel.NORTH);
	}

	@Override
	public Widget asWidget() {
		return this;
	}

}
