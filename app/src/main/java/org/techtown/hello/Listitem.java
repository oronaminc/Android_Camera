package org.techtown.hello;

public class Adapter {
    private String id;
    private String path;
    private boolean isSelected;

    public Adapter(String id, String path) {
        this.id = id;
        this.path = path;
        this.isSelected = false;
    }

    public String getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


}
