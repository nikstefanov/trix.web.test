package example.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * 
 * @author esnunes@gmail.com (Eduardo S. Nunes)
 * 
 */
public interface AddNewIssueHandler extends EventHandler {

	void onAddNewIssue(AddNewIssueEvent event);

}
