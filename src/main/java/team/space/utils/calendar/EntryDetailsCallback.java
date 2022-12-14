package team.space.utils.calendar;

import com.calendarfx.model.Entry;
import com.calendarfx.view.DateControl;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.InputEvent;
import javafx.util.Callback;
import team.space.models.Meet;

/**
 * Custom implementation {@link DateControl#entryDetailsCallbackProperty}
 * to invoke the edit entry dialog.
 *
 * <h2>Code Example</h2>The code below shows setting callback for week page.
 *
 * <pre>
 * weekPage.setEntryDetailsCallback(param -> new EntryDetailsCallback().call(param));
 * </pre>
 */
public class EntryDetailsCallback implements Callback<DateControl.EntryDetailsParameter, Boolean> {
    @Override
    public Boolean call(DateControl.EntryDetailsParameter param) {
        InputEvent evt = param.getInputEvent();
        var entry = (Entry<Meet>) param.getEntry();

        if (evt instanceof ContextMenuEvent) {
            var dialog = new EntryDialog(entry);

            dialog.showAndWait();

            return true;
        }

        return true;
    }
}
