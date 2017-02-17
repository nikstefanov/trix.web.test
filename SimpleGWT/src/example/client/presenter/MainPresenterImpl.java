package example.client.presenter;

import java.util.HashMap;
import java.util.Map;

import org.enunes.gwt.mvp.client.EventBus;
import org.enunes.gwt.mvp.client.presenter.BasePresenter;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Provider;

import example.client.BirtGeneratorServiceAsync;
import example.client.event.SelectReportEvent;
import example.client.event.SelectReportHandler;
import example.client.view.ContentWidget;

public class MainPresenterImpl extends BasePresenter<MainPresenter.Display> implements MainPresenter{

	private final Provider<ContentPresenter> contentProvider;
	private final Provider<ReportsDialogPresenter> dialogProvider;
	private ContentPresenter contentPresenter;
	private BirtGeneratorServiceAsync generator;
	
	@Inject
	public MainPresenterImpl(EventBus eventBus, Display display,
			MenuBarPresenter menuPresenter,
			Provider<ContentPresenter> contentProvider,
			Provider<ReportsDialogPresenter> dialogProvider) {

		super(eventBus, display);

		this.contentProvider = contentProvider;
		this.dialogProvider = dialogProvider;

		System.out.println(generator);
		menuPresenter.bind();
		display.addMenu(menuPresenter.getDisplay());
	}

	@Override
	public void bind() {

		super.bind();

		registerHandler(eventBus.addHandler(SelectReportEvent.getType(),
				new SelectReportHandler() {
					
					@Override
					public void onSelectReport(SelectReportEvent event) {
//						generateReport(event.getReportName());
						Map params = new HashMap();
						Double zahl = 4356453.0; 
						params.put("Cust", zahl);
						generateReport("test1.rptdesign", params);
					}
				}));
	}
	
	
	private void generateReport(String reportName, Map params) {
		generator.generateReport(reportName, params, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				setContent(result);
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public void setBirtService(BirtGeneratorServiceAsync birtService) {
		this.generator = birtService;
		
	}
	
	private void setContent(String report) {
		contentPresenter = new ContentPresenterImpl(eventBus, new ContentWidget());
		contentPresenter.setReport(report);
		contentPresenter.bind();
		display.addContentPanel(contentPresenter.getDisplay());
	}

}
