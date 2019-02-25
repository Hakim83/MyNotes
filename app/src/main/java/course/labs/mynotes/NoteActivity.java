package course.labs.mynotes;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity {

    public static final String NOTE_ACTION = "course.labs.mynotes.NOTE_ACTION";
    final int NOTE_REQUEST = 1;
    ArrayAdapter<String> notesAdapter;
    ArrayList<String> titleList = new ArrayList<String>();
    ArrayList<String> textList = new ArrayList<String>();

    ListView notesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        notesListView = (ListView)findViewById(R.id.notesListView);

        notesAdapter = new ArrayAdapter<String>(this,R.layout.item_note,R.id.note_title,titleList);

        notesListView.setAdapter(notesAdapter);
        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewNote(position);
            }
        });
        registerForContextMenu(notesListView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_note:
                addNoteView();
                return true;
            case R.id.help:
                return  true;
            default:
                return  false;
        }
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_context_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //get clicked item index in listview
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;

        switch (item.getItemId()){
            case R.id.edit_note:
                modifyNoteView(position);
                return true;
            case R.id.delete_note:
                deleteNoteConfirm(position);
                return true;
            default:
                return false;
        }
    }

    private void addNoteView() {
        Intent newNodeIntent = new Intent(this, MakeNoteActivity.class);
        startActivityForResult(newNodeIntent, NOTE_REQUEST);
    }

    private void modifyNoteView(int position) {
        String title = titleList.get(position);
        String text = textList.get(position);
        Intent modifyIntent = new Intent(getApplicationContext(), MakeNoteActivity.class);
        modifyIntent.putExtra("title", title);
        modifyIntent.putExtra("text", text);
        modifyIntent.putExtra("index", position);
        startActivityForResult(modifyIntent, NOTE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NOTE_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                String title = extras.getString("title");
                String text = extras.getString("text");
                int index = extras.getInt("index");
                if (index < 0) {
                    //new item
                    addNote(title, text);
                } else {
                    //modify item
                    updateNote(title, text, index);
                }
            }
        }
    }

    private void updateNote(String title, String text, int index) {
        titleList.set(index, title);
        textList.set(index, text);
        notesAdapter.notifyDataSetChanged();
    }

    private void addNote(String title, String text) {
        titleList.add(title);
        textList.add(text);
        notesAdapter.notifyDataSetChanged();
    }

    private void viewNote(int position) {
        String title = titleList.get(position);
        String text = textList.get(position);

        Intent viewIntent = new Intent(getApplicationContext(), ViewNoteActivity.class);
        viewIntent.putExtra("title", title);
        viewIntent.putExtra("text", text);
        startActivity(viewIntent);

    }
    private void deleteNoteConfirm(int position) {
        ConfirmationDialog dialog = ConfirmationDialog.newInstance(position);
        dialog.show(getSupportFragmentManager(), "delete");
    }

    private void deleteNote(int position) {
        titleList.remove(position);
        textList.remove(position);
        notesAdapter.notifyDataSetChanged();
    }

    public static class ConfirmationDialog extends DialogFragment {
        public static ConfirmationDialog newInstance(int p) {
            position = p;
            return new ConfirmationDialog();
        }

        //used to hold position to be deleted
        static int position;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setMessage("Delete Note, Are You Sure?")
                    .setCancelable(false)
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing
                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //delete item
                            ((NoteActivity) getActivity()).deleteNote(position);
                        }
                    })
                    .create();
        }
    }



}
