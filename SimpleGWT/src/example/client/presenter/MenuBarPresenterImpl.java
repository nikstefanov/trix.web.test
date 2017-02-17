package example.client.presenter;

import org.enunes.gwt.mvp.client.EventBus;
import org.enunes.gwt.mvp.client.presenter.BasePresenter;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.inject.Inject;

import example.client.presenter.MenuBarPresenter.Display;
import example.client.view.ReportsDialogWidget;



public class MenuBarPresenterImpl extends BasePresenter<Display> implements MenuBarPresenter{

	@Inject
	public MenuBarPresenterImpl(EventBus eventBus, Display display) {
		super(eventBus, display);
		ReportsDialogWidget dv = new ReportsDialogWidget();
		setDialog(dv.getDialog());
		ReportsDialogPresenter rdp = new ReportsDialogPresenterImpl(eventBus, dv);
		rdp.bind();
	}

	@Override
	public void bind() {

		super.bind();
	}
	
	private void setDialog(DialogBox dialog) {
		display.setDV(dialog);
	}
	
	
}
