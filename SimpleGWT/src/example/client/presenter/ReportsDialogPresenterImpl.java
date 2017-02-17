package example.client.presenter;

import org.enunes.gwt.mvp.client.EventBus;
import org.enunes.gwt.mvp.client.presenter.BasePresenter;
import org.enunes.gwt.mvp.client.presenter.Presenter;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.inject.Inject;

import example.client.event.SelectReportEvent;
import example.client.view.ReportTreeWidget;

public class ReportsDialogPresenterImpl extends
		BasePresenter<ReportsDialogPresenter.Display> implements
		ReportsDialogPresenter {

	private Presenter<? extends org.enunes.gwt.mvp.client.view.Display> presenter;

	@Inject
	public ReportsDialogPresenterImpl(EventBus eventBus, Display display) {
		super(eventBus, display);
		generateContent();
	}

	private void generateContent() {
		ReportTreeWidget tree = new ReportTreeWidget();

		display.addContent(tree);
	}

	@Override
	public void bind() {
		super.bind();

		registerHandler(display.getContent().addSelectionHandler(
				new SelectionHandler<TreeItem>() {

					@Override
					public void onSelection(SelectionEvent<TreeItem> event) {
						String reportName = event.getSelectedItem().getText();
						eventBus.fireEvent(new SelectReportEvent(reportName));
					}
				}));
	}

	@Override
	public void unbind() {
		super.unbind();
		if (presenter != null) {
			presenter.unbind();
		}
	}

}
