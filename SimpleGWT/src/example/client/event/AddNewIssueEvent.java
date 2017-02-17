package example.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * @author esnunes@gmail.com (Eduardo S. Nunes)
 * 
 */
public class AddNewIssueEvent extends GwtEvent<AddNewIssueHandler> {

	private static Type<AddNewIssueHandler> TYPE;

	public static Type<AddNewIssueHandler> getType() {
		return TYPE != null ? TYPE : (TYPE = new Type<AddNewIssueHandler>());
	}

	public AddNewIssueEvent() {
	}

	@Override
	public final Type<AddNewIssueHandler> getAssociatedType() {
		return getType();
	}

	@Override
	protected void dispatch(AddNewIssueHandler handler) {
		handler.onAddNewIssue(this);
	}

}
