package tj.zdaroviyRebonyk.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteClosable;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import tj.zdaroviyRebonyk.Models.Bookmark;
import tj.zdaroviyRebonyk.Models.Category;
import tj.zdaroviyRebonyk.Models.SavedData;
import tj.zdaroviyRebonyk.Models.SubCategory;

public class DataServer {

    public static List<Category> getCategory(Context mctx) {
        MyDatabase db = new MyDatabase(mctx);
        SQLiteDatabase data = db.getReadableDatabase();
        List<Category> list = new ArrayList<>();
        Cursor c = data.rawQuery("Select * from Category group by sort", null);

        while (c.moveToNext()) {
            Category cat = new Category();
            cat.setId(c.getString(0));
            cat.setName(c.getString(1));
            cat.setImg(c.getString(2));
            list.add(cat);
        }
        return list;
    }


    public static List<SavedData> getSavedData(Context mctx) {
        MyDatabase db = new MyDatabase(mctx);
        SQLiteDatabase data = db.getReadableDatabase();
        List<SavedData> list = new ArrayList<>();
        Cursor c = data.rawQuery("Select * from Saved_data", null);

        while (c.moveToNext()) {
            SavedData s = new SavedData();
            s.setId(c.getString(0));
            s.setIdSubCat(c.getString(1));
            s.setText(c.getString(2));
            s.setComent(c.getString(3));
            s.setIdElem(c.getString(4));
            s.setPos(c.getString(5));
            list.add(s);
        }
        return list;
    }


    public static List<SubCategory> getSubCategory(Context mctx,String text) {
        MyDatabase db = new MyDatabase(mctx);
        SQLiteDatabase data = db.getReadableDatabase();
        List<SubCategory> list = new ArrayList<>();
        Cursor   c = data.rawQuery("select s.* ,(Select 1 from Saved_Html as sd where s.id=sd.idSubCategory) as status from Sub_Category as s where  s.name like '%" + text + "%'", null);
        while (c.moveToNext()) {
            SubCategory sub = new SubCategory();
            sub.setId(c.getString(0));
            sub.setId_cat(c.getString(1));
            sub.setName(c.getString(2));
            sub.setSrc(c.getString(3));
            sub.setImg(c.getString(4));
            sub.setFav(c.getString(5));
            sub.setStatus(c.getInt(7) == 1);
            sub.setAuthor(c.getString(6));
            list.add(sub);
        }
        return list;
    }


    public static List<SubCategory> getSubCategory(Context mctx, String id, String author) {
        MyDatabase db = new MyDatabase(mctx);
        SQLiteDatabase data = db.getReadableDatabase();
        List<SubCategory> list = new ArrayList<>();
        Cursor c;
        if (author.equals("Все"))
            c = data.rawQuery("select s.* ,(Select 1 from Saved_Html as sd where s.id=sd.idSubCategory) as status from Sub_Category as s where s.id_cat=" + id, null);
        else
            c = data.rawQuery("select s.* ,(Select 1 from Saved_Html as sd where s.id=sd.idSubCategory) as status from Sub_Category as s where s.id_cat=" + id + " and  s.author='" + author + "'", null);
        while (c.moveToNext()) {
            SubCategory sub = new SubCategory();
            sub.setId(c.getString(0));
            sub.setId_cat(c.getString(1));
            sub.setName(c.getString(2));
            sub.setSrc(c.getString(3));
            sub.setImg(c.getString(4));
            sub.setFav(c.getString(5));
            sub.setStatus(c.getInt(7) == 1);
            sub.setAuthor(c.getString(6));
            list.add(sub);
        }
        return list;
    }

    public static SubCategory getSubCategoryById(Context mctx, String idsub) {
        MyDatabase db = new MyDatabase(mctx);
        SQLiteDatabase data = db.getReadableDatabase();

        Cursor cc = data.rawQuery("Select * from Saved_Html as sd where sd.idSubCategory=" + idsub, null);
        cc.moveToFirst();
        Cursor c = data.rawQuery("select *  from Sub_Category  where id=" + idsub, null);
        c.moveToFirst();
        SubCategory sub = new SubCategory();
        sub.setId(c.getString(0));
        sub.setId_cat(c.getString(1));
        sub.setName(c.getString(2));
        sub.setSrc(c.getString(3));
        sub.setImg(c.getString(4));
        sub.setFav(c.getString(5));
        sub.setAuthor(c.getString(6));
        if (cc.getCount() > 0) {
            sub.setStatus(true);
        } else
            sub.setStatus(false);
        return sub;
    }


    public static void UpdateFav(Context mctx, String id, int status) {
        MyDatabase db = new MyDatabase(mctx);
        SQLiteDatabase data = db.getReadableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("fav", status);
        int i = data.update("Sub_Category", cv, "id=" + id, null);
    }


    public static boolean InsertHtml(Context mctx, String idSubCategory, String text,
                                     boolean status) {
        MyDatabase db = new MyDatabase(mctx);
        SQLiteDatabase data = db.getReadableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("idSubCategory", idSubCategory);
        cv.put("htmlData", text);
        long i;
        if (status) {
            i = data.update("Saved_Html", cv, "idSubCategory=" + idSubCategory, null);
            return i != -1;
        }
        i = data.insert("Saved_Html", null, cv);
        return i != -1;
    }


    public static boolean DelSavedData(Context mctx, String id) {
        MyDatabase db = new MyDatabase(mctx);
        SQLiteDatabase data = db.getReadableDatabase();

        long i;
        i = data.delete("Saved_data", "id=" + id, null);
        return i != -1;
    }


    public static void InsertSelectedText(Context mctx, String idSubCategory, String
            text, String idElement, String coment, String pos) {
        MyDatabase db = new MyDatabase(mctx);
        SQLiteDatabase data = db.getReadableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("idSubCategory", idSubCategory);
        cv.put("text", text);
        cv.put("coment", coment);
        cv.put("pos", pos);

        cv.put("idElement", idElement);
        data.insert("Saved_data", null, cv);
    }


    public static void InsertBookmark(Context mctx, String idSubCategory, String text, String
            pos) {
        MyDatabase db = new MyDatabase(mctx);
        SQLiteDatabase data = db.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("idSubCategory", idSubCategory);
        cv.put("text", text);
        cv.put("pos", pos);
        data.insert("Bookmark", null, cv);
    }

    public static void DelBookmark(Context mctx, String id) {
        MyDatabase db = new MyDatabase(mctx);
        SQLiteDatabase data = db.getReadableDatabase();

        data.delete("Bookmark", "idSubCategory=" + id, null);
    }

    public static float getBookmark(Context mctx, String id) {
        MyDatabase db = new MyDatabase(mctx);
        SQLiteDatabase data = db.getReadableDatabase();
        Cursor c = data.rawQuery("Select * from Bookmark where idSubCategory=" + id, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            return Float.parseFloat(c.getString(3));
        } else {
            return -1f;
        }
    }

    public static List<Bookmark> getAllBookmark(Context mctx) {
        MyDatabase db = new MyDatabase(mctx);
        SQLiteDatabase data = db.getReadableDatabase();
        Cursor c = data.rawQuery("Select * from Bookmark", null);

        List<Bookmark> list = new ArrayList<>();
        while (c.moveToNext()) {
            Cursor img = data.rawQuery("Select * from Sub_Category where id=" + c.getString(1), null);
            img.moveToFirst();
            Bookmark b = new Bookmark();
            b.setId(c.getString(0));
            b.setIdSubCategory(c.getString(1));
            b.setText(c.getString(2));
            b.setPos(c.getString(3));
            b.setImg(img.getString(4));
            list.add(b);
        }
        return list;
    }


    public static String getHtml(Context mctx, String id) {
        MyDatabase db = new MyDatabase(mctx);
        SQLiteDatabase data = db.getReadableDatabase();
        Cursor c = data.rawQuery("Select * from Saved_Html where idSubCategory=" + id, null);
        c.moveToFirst();
        return c.getString(3);
    }

}
