package example.client.gin;

import org.enunes.gwt.mvp.client.EventBus;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

import example.client.presenter.MainPresenter;

/**
 * 
 * @author esnunes@gmail.com (Eduardo S. Nunes)
 * 
 */
@GinModules(Module.class)
public interface Injector extends Ginjector {

	MainPresenter getMainPresenter();

	EventBus getEventBus();

}
