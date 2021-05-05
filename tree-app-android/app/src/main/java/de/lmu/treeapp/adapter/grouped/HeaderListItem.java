package de.lmu.treeapp.adapter.grouped;

public class HeaderListItem extends AbstractListItem {
    public HeaderListItem(String title) {
        this.obj = title;
    }

    @Override
    public int getType() {
        return AbstractListItem.TYPE_HEADER;
    }
}
