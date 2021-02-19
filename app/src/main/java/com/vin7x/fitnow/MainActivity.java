package com.vin7x.fitnow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //private View btnImc;
    private RecyclerView rvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvMain = findViewById(R.id.main_rv);

        List<MainItem> mainItems = new ArrayList<>();
        mainItems.add(new MainItem(1, R.drawable.ic_imc, R.string.label_imc, Color.GREEN));
        mainItems.add(new MainItem(2, R.drawable.ic_baseline_remove_red_eye_24, R.string.label_tmb, Color.BLUE));
        mainItems.add(new MainItem(3, R.drawable.ic_baseline_fitness_center_24, R.string.calorias, Color.BLACK));

        // btnImc = findViewById(R.id.btn_imc);
        /*Define o modo como a recyclerview é exibida
        Grid
        mosaic
        linear(vertical / horizontal)
         */
        rvMain.setLayoutManager(new GridLayoutManager(this, 3));
        MainAdapter adapter = new MainAdapter(mainItems);

        rvMain.setAdapter(adapter);
        adapter.setListener(id -> {
            switch (id) {
                case 1:
                    startActivity( new Intent(MainActivity.this, ImcActivity.class));
                    break;
                case 2:
                    startActivity( new Intent(MainActivity.this, TmbActivity.class));
                    break;
                case 3:
                    startActivity( new Intent(MainActivity.this, ImcActivity.class));
                    break;
            }
        });
    }

    private class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

        private List<MainItem> mainItens;
        private OnItemClickListener listener;

        public MainAdapter(List<MainItem> mainItens) {
            this.mainItens = mainItens;
        }

        public void setListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MainViewHolder(getLayoutInflater().inflate(R.layout.main_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
            MainItem mainItemCurrent = mainItens.get(position);
            holder.bind(mainItemCurrent);
        }

        @Override
        public int getItemCount() {
            return mainItens.size();
        }

        //View da célula q está dentro da Recycler
        private class MainViewHolder extends RecyclerView.ViewHolder {

            public MainViewHolder(@NonNull View itemView) {
                super(itemView);
            }

            public void bind(MainItem item) {
                TextView txtName = itemView.findViewById(R.id.item_txt_name);
                ImageView imageIcon = itemView.findViewById(R.id.item_img_icon);
                LinearLayout btnImc = (LinearLayout) itemView.findViewById(R.id.btn_imc);

                btnImc.setOnClickListener(view -> {
                    listener.onClick(item.getId());

                });

                txtName.setText(item.getTextStringId());
                imageIcon.setImageResource(item.getDrawableId());
                btnImc.setBackgroundColor(item.getColor());
            }
        }
    }

}