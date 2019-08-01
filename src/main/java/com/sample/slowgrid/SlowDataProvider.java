package com.sample.slowgrid;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.server.SerializablePredicate;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * Proxy for {@link ListDataProvider} where every implemented method has some delay, to fake real database queries.
 */
public class SlowDataProvider<T> extends ListDataProvider<T> {

	/**
	 * How long every method waits before delegating the call to {@link ListDataProvider}.
	 */
	public static final int DELAY_IN_MILLISECONDS = 700;

	public SlowDataProvider(Collection<T> items) {
		super(items);
	}

	/**
	 * Fetches data from this DataProvider using given query, but with DELAY.
	 */
    @Override
    public Stream<T> fetch(Query<T, SerializablePredicate<T>> query) {
		try {
			TimeUnit.MILLISECONDS.sleep(DELAY_IN_MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	return super.fetch(query);
    }

    /**
     * Returns the underlying data items, but with DELAY.
     */
    @Override
    public Collection<T> getItems() {
			try {
				TimeUnit.MILLISECONDS.sleep(DELAY_IN_MILLISECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        return super.getItems();
    }
    
	private static final long serialVersionUID = 625484060868353088L;

}
