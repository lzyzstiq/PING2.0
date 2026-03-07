package com.ping.sleep;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView currentQuoteText;
    private Button startServiceBtn;
    private Button recordDreamBtn;
    private EditText dreamInput;
    private RecyclerView dreamHistoryView;
    private Switch nightModeSwitch;
    private DreamHistoryAdapter dreamAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupListeners();
        loadCurrentQuote();
        loadDreamHistory();
    }

    private void initViews() {
        currentQuoteText = findViewById(R.id.current_quote);
        startServiceBtn = findViewById(R.id.start_service);
        recordDreamBtn = findViewById(R.id.record_dream);
        dreamInput = findViewById(R.id.dream_input);
        dreamHistoryView = findViewById(R.id.dream_history);
        nightModeSwitch = findViewById(R.id.night_mode);

        dreamHistoryView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupListeners() {
        startServiceBtn.setOnClickListener(v -> {
            if (startServiceBtn.getText().equals("启动守护")) {
                startSleepService();
                startServiceBtn.setText("守护中");
                startServiceBtn.setBackgroundResource(R.drawable.button_rounded);
            } else {
                stopSleepService();
                startServiceBtn.setText("启动守护");
                startServiceBtn.setBackgroundResource(R.drawable.button_rounded);
            }
        });

        recordDreamBtn.setOnClickListener(v -> {
            String dream = dreamInput.getText().toString().trim();
            if (!dream.isEmpty()) {
                DreamRecorder.saveDream(this, dream);
                dreamInput.setText("");
                Toast.makeText(this, "✨ 平平已收到你的梦", Toast.LENGTH_SHORT).show();
                loadDreamHistory();
            }
        });

        nightModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                currentQuoteText.setTextColor(getColor(R.color.dim_gold));
            } else {
                currentQuoteText.setTextColor(getColor(R.color.warm_white));
            }
        });
    }

    private void loadCurrentQuote() {
        String quote = QuoteManager.getRandomQuote(this);
        currentQuoteText.setText("💤 " + quote);
    }

    private void loadDreamHistory() {
        List<String> dreams = DreamRecorder.getRecentDreams(this, 10);
        if (dreamAdapter == null) {
            dreamAdapter = new DreamHistoryAdapter(dreams);
            dreamHistoryView.setAdapter(dreamAdapter);
        } else {
            dreamAdapter.updateData(dreams);
        }
    }

    private void startSleepService() {
        Intent intent = new Intent(this, SleepService.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    private void stopSleepService() {
        Intent intent = new Intent(this, SleepService.class);
        stopService(intent);
    }

    private static class DreamHistoryAdapter extends RecyclerView.Adapter<DreamHistoryAdapter.ViewHolder> {
        private List<String> dreams;

        public DreamHistoryAdapter(List<String> dreams) {
            this.dreams = dreams;
        }

        public void updateData(List<String> newDreams) {
            this.dreams = newDreams;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            android.view.View view = android.view.LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.textView.setText(dreams.get(position));
        }

        @Override
        public int getItemCount() {
            return dreams == null ? 0 : dreams.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            ViewHolder(android.view.View itemView) {
                super(itemView);
                textView = itemView.findViewById(android.R.id.text1);
            }
        }
    }
}
