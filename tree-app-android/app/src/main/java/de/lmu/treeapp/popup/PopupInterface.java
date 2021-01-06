package de.lmu.treeapp.popup;

/**
 * Interface to deliver click event to host activity
 */
public interface PopupInterface {
    void onPopupAction(PopupType type, PopupAction action);
}