package by.iit.notes.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import by.iit.notes.EditNoteActivity;
import by.iit.notes.R;
import by.iit.notes.model.Note;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> implements Filterable {

    private List<Note> notes;
    private List<Note> filteredNotes;

    public NoteAdapter(List<Note> notes) {
        this.notes = notes;
        this.filteredNotes = new ArrayList<>(notes);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_row, viewGroup, false);

        itemView.setOnLongClickListener((v) -> {
            showMenu(v);
            return true;
        });
        itemView.setOnClickListener((v) -> {
            Intent intent = new Intent(v.getContext(), EditNoteActivity.class);
            intent.putExtra("title", ((TextView) v.findViewById(R.id.title)).getText().toString());
            intent.putExtra("content", ((TextView) v.findViewById(R.id.content)).getText().toString());
            v.getContext().startActivity(intent);
        });

        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i) {
        Note note = filteredNotes.get(i);
        noteViewHolder.title.setText(note.getTitle());
        noteViewHolder.content.setText(note.getContent());
    }

    private void showMenu(final View view) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.menu_note);
        popupMenu.setOnMenuItemClickListener((item) -> {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    deleteNote(view);
                    return true;

                default:
                    return false;
            }
        });
        popupMenu.show();
    }

    private void deleteNote(View view) {
        String title = ((TextView) view.findViewById(R.id.title)).getText().toString();
        for (File file : view.getContext().getFilesDir().listFiles()) {
            if (title.equals(file.getName())) {
                file.delete();
                notes.removeIf(note -> title.equals(note.getTitle()));
                filteredNotes.removeIf(note -> title.equals(note.getTitle()));
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public int getItemCount() {
        return filteredNotes.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults filtered = new FilterResults();
                final List<Note> results = new ArrayList<>();
                if (constraint != null) {
                    for (Note note : notes) {
                        if (note.getTitle().toLowerCase().contains(constraint.toString())) {
                            results.add(note);
                        }
                    }
                    filtered.values = results;
                }
                return filtered;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredNotes = (List<Note>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView content;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
        }
    }
}
