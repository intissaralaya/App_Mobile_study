package com.uni.study;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.uni.study.model.Product;

import java.util.ArrayList;

public class issat extends AppCompatActivity {
    private EditText etSearch;
    private ListView lvProducts;

    private ArrayList<Product> mProductArrayList = new ArrayList<>();
    private MyAdapter adapter1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issat);

        initializeViews();

        // Add Text Change Listener to EditText
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                if (adapter1 != null) {
                    adapter1.getFilter().filter(s.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initializeViews() {
        etSearch = findViewById(R.id.search1);
        lvProducts = findViewById(R.id.lvProducts);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Add sample products
        mProductArrayList.add(new Product("Developpement Web"));
        mProductArrayList.add(new Product("Developpement Mobile"));
        mProductArrayList.add(new Product("LanguagedeProgrammation"));

        adapter1 = new MyAdapter(this, mProductArrayList);
        lvProducts.setAdapter(adapter1);
    }

    // Adapter Class
    public class MyAdapter extends BaseAdapter implements Filterable {

        private ArrayList<Product> mOriginalValues; // Original Values
        private ArrayList<Product> mDisplayedValues; // Values to be displayed
        private LayoutInflater inflater;

        public MyAdapter(Context context, ArrayList<Product> mProductArrayList) {
            this.mOriginalValues = mProductArrayList;
            this.mDisplayedValues = mProductArrayList;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mDisplayedValues.size();
        }

        @Override
        public Object getItem(int position) {
            return mDisplayedValues.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            LinearLayout llContainer;
            TextView tvName ;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.row, parent, false);
                holder.llContainer = convertView.findViewById(R.id.llContainer);
                holder.tvName = convertView.findViewById(R.id.tvName);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final Product product = mDisplayedValues.get(position);

            holder.tvName.setText(product.getName());

            holder.llContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(issat.this, product.getName(), Toast.LENGTH_SHORT).show();
                }
            });

            return convertView;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    mDisplayedValues = (ArrayList<Product>) results.values;
                    notifyDataSetChanged();
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                    ArrayList<Product> filteredList = new ArrayList<>();

                    if (constraint == null || constraint.length() == 0) {
                        results.count = mOriginalValues.size();
                        results.values = mOriginalValues;
                    } else {
                        String filterPattern = constraint.toString().toLowerCase().trim();

                        for (Product product : mOriginalValues) {
                            if (product.getName().toLowerCase().contains(filterPattern)) {
                                filteredList.add(product);
                            }
                        }

                        results.count = filteredList.size();
                        results.values = filteredList;
                    }
                    return results;
                }
            };
        }
    }
}
