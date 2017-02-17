package example.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import example.client.presenter.ReportsDialogPresenter;

public class ReportsDialogWidget extends Composite implements ReportsDialogPresenter.Display {

	private final DialogBox dialog;
	
	Button closeButton;
	VerticalPanel dialogContents;
	Tree content;

	public ReportsDialogWidget() {
		dialog = createDialogBox();
		dialog.ensureDebugId("cwDialogBox");
		dialog.setText("Reports");
		dialog.setGlassEnabled(true);
		dialog.setAnimationEnabled(true);
		dialog.center();
		dialog.hide();
		
	}

	private DialogBox createDialogBox() {
		// Create a dialog box and set the caption text
		final DialogBox dialogBox = new DialogBox();
		dialogBox.ensureDebugId("cwDialogBox");
		dialogBox.setText("Reports");

		// Create a table to layout the content
		dialogContents = new VerticalPanel();
		dialogBox.setWidget(dialogContents);

		// Add a close button at the bottom of the dialog
		closeButton = new Button("Close", new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
		dialogContents.add(closeButton);
		if (LocaleInfo.getCurrentLocale().isRTL()) {
			dialogContents.setCellHorizontalAlignment(closeButton,
					HasHorizontalAlignment.ALIGN_LEFT);

		} else {
			dialogContents.setCellHorizontalAlignment(closeButton,
					HasHorizontalAlignment.ALIGN_RIGHT);
		}

		// Return the dialog box
		return dialogBox;

	}
	
	@Override
	public void addContent(ReportTreeWidget tree) {
		this.content = tree.asTree();
		dialogContents.add(tree);
	}

	@Override
	public void removeContent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Widget asWidget() {
		// TODO Auto-generated method stub
		return null;
	}

	public DialogBox getDialog() {
		return dialog;
	}

	@Override
	public Tree getContent() {
		return content;
	}
	
}
