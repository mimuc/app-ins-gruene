package de.lmu.treeapp.adapter.grouped;

public abstract class AbstractListItem {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ELEMENT = 1;

    public Object obj;

    abstract public int getType();
}
