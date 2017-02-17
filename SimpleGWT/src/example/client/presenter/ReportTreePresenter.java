package example.client.presenter;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;


public interface ReportTreePresenter {

	interface Display extends org.enunes.gwt.mvp.client.view.Display {

		void addContent(org.enunes.gwt.mvp.client.view.Display display);

		void removeContent();
		
		Tree getSelectionHandlers();
		
		HasSelectionHandlers<TreeItem> getReportTree();

	}
}
