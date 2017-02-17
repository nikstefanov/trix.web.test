package example.client.presenter;

import org.enunes.gwt.mvp.client.presenter.Presenter;

import example.client.BirtGeneratorServiceAsync;

/**
 * 
 * @author esnunes@gmail.com (Eduardo S. Nunes)
 * 
 */
public interface MainPresenter extends Presenter<MainPresenter.Display> {

	interface Display extends org.enunes.gwt.mvp.client.view.Display {

		void addMenu(org.enunes.gwt.mvp.client.view.Display display);

		void addContentPanel(org.enunes.gwt.mvp.client.view.Display display);

		void removeContent();

	}

	void setBirtService(BirtGeneratorServiceAsync birtService);

}
