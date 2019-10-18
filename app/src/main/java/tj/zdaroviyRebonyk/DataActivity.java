package tj.zdaroviyRebonyk;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import tj.zdaroviyRebonyk.Fragments.SettingsDialogFragment;
import tj.zdaroviyRebonyk.Models.SubCategory;
import tj.zdaroviyRebonyk.Utils.DataServer;
import tj.zdaroviyRebonyk.Utils.WebAppInterface;
import tj.zdaroviyRebonyk.Utils.WebAppInterfaceHtml;

@RequiresApi(api = Build.VERSION_CODES.M)
public class DataActivity extends AppCompatActivity implements ActionMode.Callback, View.OnClickListener {
    WebView wb;
    SubCategory list;
    SharedPreferences pref;
    ActionMode actionMode = null;
    boolean mHasToRestoreState = false;
    float mProgressToRestore;
    String elemId;
    boolean saveState = false;
    String pos = "";


    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        String id = getIntent().getExtras().getString("id");
        pos = getIntent().getExtras().getString("pos", "");

        if (!pos.isEmpty()) {
            mHasToRestoreState = true;
            mProgressToRestore = Float.parseFloat(pos);
        }

        list = DataServer.getSubCategoryById(this, id);
        TextView author = (TextView) findViewById(R.id.author);
        author.setText("Автор: " + list.getAuthor());
        wb = (WebView) findViewById(R.id.web);
        wb.getSettings().setDomStorageEnabled(true);
        wb.getSettings().setJavaScriptEnabled(true);
        wb.addJavascriptInterface(new WebAppInterface(this), "Android");
        wb.addJavascriptInterface(new WebAppInterfaceHtml(this), "JsHtml");
        wb.setHorizontalScrollBarEnabled(false);

        toolbar.setTitle(list.getName());
        setTitle(list.getName());
        if (savedInstanceState != null) {
            mHasToRestoreState = true;
            mProgressToRestore = savedInstanceState.getFloat("position");
        }
        if (list.isStatus()) {
            try {
                String sr = "/";
                for (int i = 0; i <= list.getSrc().length(); i++) {
                    if (list.getSrc().charAt(i) == '/')
                        break;
                    sr += (list.getSrc().charAt(i));
                }
                FileOutputStream os = new FileOutputStream(getFilesDir() + "/" + sr + "/file.html");
                os.write(DataServer.getHtml(this, list.getId()).getBytes());
                os.close();
                wb.loadUrl("file:///" + getFilesDir() + sr + "/file.html");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            wb.loadUrl("file:///" + getFilesDir() + "/" + list.getSrc());
            saveState = true;
        }
        wb.setWebViewClient(new MyWebViewClient());
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onActionModeStarted(ActionMode mode) {
        if (actionMode == null) {
            actionMode = startActionMode(this);
            super.onActionModeStarted(mode);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putFloat("position", calculateProgression(wb));
    }

    private float calculateProgression(WebView content) {
        float positionTopView = content.getTop();
        float contentHeight = content.getContentHeight();
        float currentScrollPosition = content.getScrollY();
        return (currentScrollPosition - positionTopView) / contentHeight;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setInputType(InputType.TYPE_CLASS_TEXT);
        ImageView searchClose = (ImageView) searchView.findViewById(R.id.search_close_btn);
        searchClose.setImageResource(R.drawable.ic_close_white_24dp);
        EditText text = (EditText) searchView.findViewById(R.id.search_src_text);
        text.setHintTextColor(Color.WHITE);
        text.setHint("Поиск");
        searchClose.setImageResource(R.drawable.ic_close_white_24dp);
        searchView.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                wb.findNext(true);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                wb.findAllAsync(s);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.bookmark) {
            if (DataServer.getBookmark(this, list.getId()) != -1) {
                DataServer.DelBookmark(this, list.getId());
                item.setIcon(R.drawable.ic_bookmark_border_black_24dp);
            } else {
                DataServer.InsertBookmark(this, list.getId(), list.getName(), String.valueOf(calculateProgression(wb)));
                item.setIcon(R.drawable.ic_bookmark_black_24dp);
            }
        } else if (item.getItemId() == R.id.setting) {
            SettingsDialogFragment s = SettingsDialogFragment.newInstance();
            s.show(getSupportFragmentManager().beginTransaction(), "Settings");
            s.shriftPlus(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wb.loadUrl("javascript:incFontSize();");
                }
            });
            s.shriftMinus(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wb.loadUrl("javascript:decFontSize();");
                }
            });
            s.backgroundMon(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wb.loadUrl("javascript:changeBackgroundColor('#313030','#fff');");
                }
            });
            s.backgroundSun(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wb.loadUrl("javascript:changeBackgroundColor('#fff','#000');");
                }
            });
        } else if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuB = menu.findItem(R.id.bookmark);
        if (DataServer.getBookmark(this, list.getId()) > 0.0) {
            menuB.setIcon(R.drawable.ic_bookmark_black_24dp);
        } else
            menuB.setIcon(R.drawable.ic_bookmark_border_black_24dp);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateActionMode(final ActionMode mode, Menu menu) {
        actionMode = mode;
        mode.getMenu().clear();

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customPopupView = layoutInflater.inflate(R.layout.custom_popup_layout, new LinearLayout(this), false);
        mode.setCustomView(customPopupView);
        ImageButton copy, coment, save, color, share;
        copy = customPopupView.findViewById(R.id.copy_);
        coment = customPopupView.findViewById(R.id.coment);
        save = customPopupView.findViewById(R.id.save);
        color = customPopupView.findViewById(R.id.color);
        share = customPopupView.findViewById(R.id.share);

        copy.setOnClickListener(this);
        coment.setOnClickListener(this);
        save.setOnClickListener(this);
        color.setOnClickListener(this);
        share.setOnClickListener(this);

        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        mode.getMenu().clear();
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        actionMode = null;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.color) {
            elemId = "id" + System.currentTimeMillis();
            wb.loadUrl("javascript:changeSelectedColor('#ff0','" + elemId + "');");
            wb.loadUrl("javascript:getHtmlText();");
            wb.postDelayed(new Runnable() {
                @Override
                public void run() {
                    DataServer.InsertSelectedText(getApplication(), list.getId(), WebAppInterface.text, elemId, "", String.valueOf(calculateProgression(wb)));
                    DataServer.InsertHtml(getApplication(), list.getId(), WebAppInterfaceHtml.html, list.isStatus());
                }
            }, 100);
            Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
            actionMode.finish();
        } else if (id == R.id.copy_) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("", WebAppInterface.text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Даные скопированы в буфер", Toast.LENGTH_SHORT).show();
            wb.loadUrl("javascript:getHtmlText();");
            actionMode.finish();

        } else if (id == R.id.coment) {
            showDialogComent();
            actionMode.finish();
        } else if (id == R.id.share) {
            Intent i = new Intent(Intent.ACTION_SEND)
                    .setType("text/plain")
                    .putExtra(Intent.EXTRA_TEXT, WebAppInterface.text);
            startActivity(Intent.createChooser(i, "Share to"));
            actionMode.finish();
        } else if (id == R.id.save) {

            elemId = "id" + System.currentTimeMillis();
            wb.loadUrl("javascript:changeSelectedColor('#ff0','" + elemId + "');");
            wb.loadUrl("javascript:getHtmlText();");
            wb.postDelayed(new Runnable() {
                @Override
                public void run() {
                    DataServer.InsertSelectedText(getApplication(), list.getId(), WebAppInterface.text, elemId, "", String.valueOf(calculateProgression(wb)));
                    DataServer.InsertHtml(getApplication(), list.getId(), WebAppInterfaceHtml.html, list.isStatus());
                }
            }, 100);
            actionMode.finish();
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return true;
        }

        @SuppressLint("AddJavascriptInterface")
        @Override
        public void onPageFinished(final WebView view, String url) {
            if (mHasToRestoreState) {
                mHasToRestoreState = false;
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        float webviewsize = wb.getContentHeight() - wb.getTop();
                        float positionInWV = webviewsize * mProgressToRestore;
                        int positionY = Math.round(wb.getTop() + positionInWV);
                        wb.scrollTo(0, positionY);
                    }
                }, 300);
            } else if (DataServer.getBookmark(getApplication(), list.getId()) != -1) {
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        float webviewsize = wb.getContentHeight() - wb.getTop();
                        float positionInWV = webviewsize * DataServer.getBookmark(getApplication(), list.getId());
                        int positionY = Math.round(wb.getTop() + positionInWV);
                        wb.scrollTo(0, positionY);
                    }
                }, 300);
            }
            if (saveState) {
                wb.loadUrl("javascript:getHtmlText();");
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (WebAppInterfaceHtml.html == null)
                            wb.loadUrl("javascript:getHtmlText();");
                        DataServer.InsertHtml(getApplication(), list.getId(), WebAppInterfaceHtml.html, list.isStatus());
                        list.setStatus(true);
                        saveState = false;
                    }
                }, 300);
            }
            super.onPageFinished(view, url);
        }
    }

    private void showDialogComent() {
        View view = getLayoutInflater().inflate(R.layout.coment_dialog, null);
        final Dialog mBottomSheetDialog = new Dialog(this, R.style.MaterialDialogSheet);

        Button up = view.findViewById(R.id.save);
        Button close = view.findViewById(R.id.close);
        ImageView cl = view.findViewById(R.id.closeImg);
        final EditText text = view.findViewById(R.id.text);
        TextView selectedText = view.findViewById(R.id.selectedText);
        selectedText.setText(WebAppInterface.text);
        selectedText.setMovementMethod(new ScrollingMovementMethod());
        cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!text.getText().toString().isEmpty()) {

                    elemId = "id" + System.currentTimeMillis();
                    wb.loadUrl("javascript:changeSelectedColor('#ff0','" + elemId + "');");
                    wb.loadUrl("javascript:getHtmlText();");
                    wb.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            DataServer.InsertSelectedText(getApplication(), list.getId(), WebAppInterface.text, elemId, text.getText().toString(), String.valueOf(calculateProgression(wb)));
                            DataServer.InsertHtml(getApplication(), list.getId(), WebAppInterfaceHtml.html, list.isStatus());
                        }
                    }, 100);
                    mBottomSheetDialog.dismiss();
                }
            }
        });
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
    }

}
