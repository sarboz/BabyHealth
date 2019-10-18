package tj.zdaroviyRebonyk.Models;

public class Bookmark {
    private String id;
    private String text;
    private String idSubCategory;
    private String pos;
    private String img;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIdSubCategory() {
        return idSubCategory;
    }

    public void setIdSubCategory(String idSubCategory) {
        this.idSubCategory = idSubCategory;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }
}
