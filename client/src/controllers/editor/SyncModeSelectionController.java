package controllers.editor;

import views.editor.SyncModeSelectionView;

/**
 * Controller for the SyncModeSelectionView
 */
public class SyncModeSelectionController {

    private SyncModeSelectionView view;

    /**
     * Creates a controller for the SyncModeSelectionView
     *
     * @param view
     *            The corresponding view, created elsewhere
     */
    public SyncModeSelectionController(final SyncModeSelectionView view) {
        this.view = view;
    }

    /**
     * Activates several possible sync methods, depending on the user's
     * selection in the view
     */
    public void choiceDone() {
        String selection = this.view.getSelection();
        this.view.setSelection(selection);
        this.view.dispose();
    }
}
