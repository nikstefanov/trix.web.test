package example.client.presenter;

import org.enunes.gwt.mvp.client.presenter.Presenter;

import com.google.gwt.user.client.ui.Tree;

import example.client.view.ReportTreeWidget;


public interface ReportsDialogPresenter extends Presenter<ReportsDialogPresenter.Display>{

	interface Display extends org.enunes.gwt.mvp.client.view.Display {

		void addContent(ReportTreeWidget tree);
		
		Tree getContent();

		void removeContent();
		

	}
}
