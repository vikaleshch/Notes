package by.iit.notes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import by.iit.notes.adapter.NoteAdapter;
import by.iit.notes.model.Note;

public class MainActivity extends AppCompatActivity {

    private List<Note> notes = new ArrayList<>();
    private NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((view) ->
                startActivity(new Intent(MainActivity.this, NewNoteActivity.class)));

        prepareNotes();

        RecyclerView noteRecycler = findViewById(R.id.notes);

        adapter = new NoteAdapter(notes);
        noteRecycler.setLayoutManager(new LinearLayoutManager(this));
        noteRecycler.setItemAnimator(new DefaultItemAnimator());
        noteRecycler.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem != null) {
            SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    adapter.getFilter().filter(s);
                    return true;
                }
            });
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void prepareNotes() {
        for (File file : getFilesDir().listFiles()) {
            try {
                notes.add(new Note(file.getName(), Files.lines(file.toPath()).collect(Collectors.joining())));
            } catch (IOException e) {
                Toast.makeText(this, "Exception: " + e.toString(), Toast.LENGTH_LONG).show();
            }
        }

    }
}
