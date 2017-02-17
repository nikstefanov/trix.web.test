package example.client.presenter;

import org.enunes.gwt.mvp.client.presenter.Presenter;

import com.google.gwt.user.client.ui.DialogBox;

public interface MenuBarPresenter extends Presenter<MenuBarPresenter.Display>{

	interface Display extends org.enunes.gwt.mvp.client.view.Display {

		
		public void setDV(DialogBox dv);
	}
}
