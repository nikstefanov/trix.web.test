package example.client.view;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import example.client.presenter.ContentPresenter;

public class ContentWidget extends Composite implements
		ContentPresenter.Display {

	private final DecoratorPanel wrapper;
	private final VerticalPanel panel;
	private HorizontalPanel toolbar;
	private String report = "";

	public ContentWidget() {
		wrapper = new DecoratorPanel();
		panel = new VerticalPanel();
		panel.setStyleName("ContentWidgetWrapper");
		toolbar = createToolbar();
		panel.add(toolbar);
		panel.setSpacing(4);
		initWidget(wrapper);
		wrapper.add(panel);
	}

	private HorizontalPanel createToolbar() {
		HorizontalPanel toolbar = new HorizontalPanel();
		toolbar.add(new Button("Print"));
		toolbar.add(new Button("Back"));
		toolbar.add(new Button("Forward"));
		toolbar.add(new TextBox());
		return toolbar;
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	private void createContent() {
		HTML html = new HTML(report);
		VerticalPanel container = new VerticalPanel();
		container.add(html);
		panel.add(container);
	}

	@Override
	public void setReport(String report) {
		this.report = report;
		createContent();
	}

	public String getReport() {
		return report;
	}

}
