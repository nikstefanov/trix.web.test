package example.client.presenter;

import org.enunes.gwt.mvp.client.EventBus;
import org.enunes.gwt.mvp.client.presenter.BasePresenter;
import org.enunes.gwt.mvp.client.presenter.Presenter;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.inject.Inject;

import example.client.BirtGeneratorServiceAsync;
import example.client.event.SelectReportEvent;
import example.client.view.ContentWidget;

public class ReportTreePresenterImpl extends BasePresenter<ReportTreePresenter.Display> implements ReportTreePresenter{
	
	private Presenter<? extends org.enunes.gwt.mvp.client.view.Display> presenter;

	@Inject
	public ReportTreePresenterImpl( EventBus eventBus, Display display) {
		super(eventBus, display);
	}

	@Override
	public void bind() {

		super.bind();

		registerHandler(display.getReportTree().addSelectionHandler(new SelectionHandler<TreeItem>() {
			
			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				String reportName = event.getSelectedItem().getText();
				System.out.println("item selected");
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
