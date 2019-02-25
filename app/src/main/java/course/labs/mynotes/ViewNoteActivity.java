package course.labs.mynotes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        TextView titleTextView = (TextView)findViewById(R.id.titleTextView);
        TextView textTextView = (TextView)findViewById(R.id.textTextView);
        Button okButton = (Button)findViewById(R.id.okButton);

        Bundle extras = getIntent().getExtras();
        String title = extras.getString("title");
        String text = extras.getString("text");

        //I skipped checking for simplicity
        titleTextView.setText(title);
        textTextView.setText(text);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
