package example.client.presenter;

import org.enunes.gwt.mvp.client.presenter.Presenter;

import com.google.gwt.user.client.ui.HTML;

public interface ContentPresenter extends Presenter<ContentPresenter.Display> {

	interface Display extends org.enunes.gwt.mvp.client.view.Display {
		
		void setReport(String report);

	}
	
	void setReport(String report);
}
