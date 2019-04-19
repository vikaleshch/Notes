package by.iit.notes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.time.LocalDateTime;

public class NewNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveNote();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveNote() {
        String titlePage = ((TextView)findViewById(R.id.titleText)).getText().toString();
        String title = !"".equals(titlePage) ? titlePage : LocalDateTime.now().toString();
        String content = ((EditText)findViewById(R.id.contentText)).getText().toString();

        try (FileOutputStream outputStream = openFileOutput(title, Context.MODE_PRIVATE)){
            outputStream.write(content.getBytes());
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Exception: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        startActivity(new Intent(NewNoteActivity.this, MainActivity.class));
    }
}
