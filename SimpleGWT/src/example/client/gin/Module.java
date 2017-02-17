package example.client.gin;

import com.google.gwt.inject.client.AbstractGinModule;

import example.client.presenter.ContentPresenter;
import example.client.presenter.ContentPresenterImpl;
import example.client.presenter.MainPresenter;
import example.client.presenter.MainPresenterImpl;
import example.client.presenter.MenuBarPresenter;
import example.client.presenter.MenuBarPresenterImpl;
import example.client.presenter.ReportTreePresenter;
import example.client.presenter.ReportTreePresenterImpl;
import example.client.presenter.ReportsDialogPresenter;
import example.client.presenter.ReportsDialogPresenterImpl;
import example.client.view.ContentWidget;
import example.client.view.MainWidget;
import example.client.view.MenuBarWidget;
import example.client.view.ReportTreeWidget;
import example.client.view.ReportsDialogWidget;


/**
 * 
 * @author esnunes@gmail.com (Eduardo S. Nunes)
 * 
 */
public class Module extends AbstractGinModule {

	@Override
	protected void configure() {

		install(new org.enunes.gwt.mvp.client.gin.Module());

//		bind(IssueDisplayPresenter.class).to(IssueDisplayPresenterImpl.class);
//		bind(IssueDisplayPresenter.Display.class).to(IssueDisplayWidget.class);
//
//		bind(IssueEditPresenter.class).to(IssueEditPresenterImpl.class);
//		bind(IssueEditPresenter.Display.class).to(IssueEditWidget.class);
		
		bind(ReportsDialogPresenter.class).to(ReportsDialogPresenterImpl.class);
		bind(ReportsDialogPresenter.Display.class).to(ReportsDialogWidget.class);
		
		bind(ReportTreePresenter.class).to(ReportTreePresenterImpl.class);
		bind(ReportTreePresenter.Display.class).to(ReportTreeWidget.class);
		
		bind(ContentPresenter.class).to(ContentPresenterImpl.class);
		bind(ContentPresenter.Display.class).to(ContentWidget.class);

		bind(MenuBarPresenter.class).to(MenuBarPresenterImpl.class);
		bind(MenuBarPresenter.Display.class).to(MenuBarWidget.class);

		bind(MainPresenter.class).to(MainPresenterImpl.class);
		bind(MainPresenter.Display.class).to(MainWidget.class);
		

	}

}
