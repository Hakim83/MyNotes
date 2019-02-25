package course.labs.mynotes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MakeNoteActivity extends AppCompatActivity {


    EditText mTitleEditText;
    EditText mTextEditText;
    Button mOkButton;
    Button mCancelButton;

    int mNoteIndex; //index of note in the list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_note);

        mTitleEditText = (EditText) findViewById(R.id.titleEditText);
        mTextEditText = (EditText) findViewById(R.id.textEditText);
        mOkButton = (Button) findViewById(R.id.okButton);
        mCancelButton = (Button) findViewById(R.id.cancelButton);

        Bundle data = getIntent().getExtras();

        if (data == null){
            //new note
            setTitle("Add Note");
            mNoteIndex=-1;
        } else{
            //modify note
            setTitle("Edit Note");
            //update values
            String title= data.getString("title");
            if (title != null) {
                mTitleEditText.setText(title);
            }
            String text = data.getString("text");
            if (text != null) {
                mTextEditText.setText(text);
            }
            //index must have been passed
            mNoteIndex = data.getInt("index");
        }

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mTitleEditText.getText().toString();
                String text = mTextEditText.getText().toString();
                if(title.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please fill title",
                            Toast.LENGTH_SHORT).show();
                }else if(text.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please fill Text",
                            Toast.LENGTH_SHORT).show();
                }else {
                    Intent noteIntent = new Intent(NoteActivity.NOTE_ACTION);
                    noteIntent.putExtra("title",title);
                    noteIntent.putExtra("text",text);
                    noteIntent.putExtra("index",mNoteIndex);
                    setResult(RESULT_OK,noteIntent);
                    finish();
                }
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
}
