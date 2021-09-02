package com.roncoder.losted;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.roncoder.losted.adapters.TaskAdapter;
import com.roncoder.losted.databinding.ActivityMainBinding;
import com.roncoder.losted.models.Task;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding ui;
    RecyclerView task_recycle;
    ArrayList<Task> tasks = new ArrayList<>();
    TaskAdapter adapter;
    LinearLayout contentView;
    public static final String TASKS_EXTRA = "tasks_extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ui = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(ui.getRoot());

            if(savedInstanceState != null)
                tasks.addAll(savedInstanceState.getParcelableArrayList(TASKS_EXTRA));
            else
                getTaskContent();

        adapter = new TaskAdapter(tasks);

        adapter.setOnItemTaskClickListener((position, v) -> {
            Task task = tasks.get(position);
            task.setFinish(!task.isFinish());
            tasks.remove(position);
            tasks.add(position, task);
            adapter.notifyItemChanged(position);
        });

        initViews();

    }

    /**
     * Fonction qui pemret de charger tous les tâches.
     */
    private void getTaskContent() {
        tasks.add(new Task("Prevoir les notions de POO", false));
        tasks.add(new Task("Acheter les fruits pour maman", false));
        tasks.add(new Task("Intégrer FireStore à task app", false));
        tasks.add(new Task("Commander les beignets du vendredi", false));
        tasks.add(new Task("Réviser les boutons", true));
    }

    /**
     * Fonction qui pemret d'initialiser les view de l'activité.
     */
    @SuppressLint("InflateParams")
    private void initViews(){

        // Recycler view
        task_recycle = ui.taskRecyclerId;
        task_recycle.setLayoutManager(new LinearLayoutManager(this));
        task_recycle.hasFixedSize();
        task_recycle.setAdapter(adapter);

        contentView = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.layout_edit_task,
                null,
                false
        );

        // Fab click listener
        ui.addBtnId.setOnClickListener(v -> addNewTask());
    }

    /**
     * Fonction qui permet d'ajouter une nouvelle tache dans la liste de tachs.
     */
    private void addNewTask(){
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss());
        dialogBuilder.setView(contentView);
        dialogBuilder.setPositiveButton(R.string.save, (dialog, which) -> {
            TextInputLayout task_layout = contentView.findViewById(R.id.task_layout);
            TextInputEditText task_id = contentView.findViewById(R.id.task_ed_id);

            String content = Objects.requireNonNull(task_id.getText()).toString().trim();

            Toast.makeText(this, content, Toast.LENGTH_LONG).show();
            // Validation du contenu.
            if(content.equals("")){
                task_layout.setError(getString(R.string.set_task));
                return;
            }else{
                task_layout.setError(null);
            }

            // add to list.
            Task task = new Task();
            task.setMessage(task_id.getText().toString());
            task.setFinish(false);
            addToList(task);
            addToFirebase(task);
            dialogBuilder.setView(null);
        });
        dialogBuilder.setOnDismissListener(dialog -> {
            ((TextInputEditText)contentView.findViewById(R.id.task_ed_id)).setText("");
            ((ViewGroup)contentView.getParent()).removeView(contentView);
        });
        dialogBuilder.show();
    }

    /**
     * Fonction qui permet d'ajouter un nouveau élément dans la base de donnée de fireStore
     * @param task Nouvelle tâche à ajouter dans fireStore.
     */
    private void addToFirebase(Task task){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("tasks")
                .add(task)
                .addOnSuccessListener(documentReference -> Log.i("Main activity", "onSuccess: new task is added to fireStore database."))
                .addOnFailureListener(e -> Log.w("Main activity", "onFailure: error on saving new task.", e));
    }

    /**
     * Fonction qui permet d'ajouter un nouvel élément dans la liste des tâches.
     * @param task Nouvelle tâche.
     */
    private void addToList(Task task) {
        tasks.add(0, task);
        adapter.notifyItemInserted(0);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelableArrayList(TASKS_EXTRA, tasks);
    }
}