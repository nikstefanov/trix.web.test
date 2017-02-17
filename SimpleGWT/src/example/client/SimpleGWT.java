package example.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.RootPanel;

import example.client.gin.Injector;
import example.client.presenter.MainPresenter;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SimpleGWT implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		ReportListServiceAsync reportListService = GWT.create(ReportListService.class);
		BirtGeneratorServiceAsync birtService = GWT.create(BirtGeneratorService.class);
		
		ServiceDefTarget endpoint = (ServiceDefTarget) birtService;  
		endpoint.setServiceEntryPoint("/simplegwt/birt"); 
		
		HandlerManager eventBus = new HandlerManager(null);
		final Injector ginjector = GWT.create(Injector.class);

		final MainPresenter mainPresenter = ginjector.getMainPresenter();
		mainPresenter.setBirtService(birtService);

		mainPresenter.bind();

		RootPanel.get().add(mainPresenter.getDisplay().asWidget());

	}
}
