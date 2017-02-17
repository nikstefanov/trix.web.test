package example.client.presenter;

import org.enunes.gwt.mvp.client.EventBus;
import org.enunes.gwt.mvp.client.presenter.BasePresenter;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.inject.Inject;

import example.client.event.SelectReportEvent;
import example.client.presenter.ContentPresenter.Display;

public class ContentPresenterImpl extends BasePresenter<Display> implements ContentPresenter{
	
	private String report;

	@Inject
	public ContentPresenterImpl(EventBus eventBus, Display display) {
		super(eventBus, display);
	}

	@Override
	public void setReport(String report) {
		this.report = report;
		display.setReport(report);
		
	}
	
	@Override
	public void bind() {

		super.bind();
		
	}
	
	private void setWidgetContent() {
		display.setReport(report);
	}


}
