package tj.zdaroviyRebonyk.Models;

public class SavedData {
    private String id;
    private String IdSubCat;
    private String text;
    private String coment;
    private String idElem;
    private String pos;

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdSubCat() {
        return IdSubCat;
    }

    public void setIdSubCat(String idSubCat) {
        IdSubCat = idSubCat;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getComent() {
        return coment;
    }

    public void setComent(String coment) {
        this.coment = coment;
    }

    public String getIdElem() {
        return idElem;
    }

    public void setIdElem(String idElem) {
        this.idElem = idElem;
    }
}
