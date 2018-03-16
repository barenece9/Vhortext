package apps.lnsel.com.vhortexttest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.data.LanguageData;

/**
 * Created by apps2 on 7/31/2017.
 */
public class SelectLanguageAdapter extends BaseAdapter {
    Context context;
    private static LayoutInflater inflater=null;
    private List<LanguageData> language_list = null;
    private ArrayList<LanguageData> arraylist;


    public SelectLanguageAdapter(Context context, List<LanguageData> language_list) {
        this.context = context;
        this.language_list = language_list;
        this.arraylist = new ArrayList<LanguageData>();
        this.arraylist.addAll(language_list);

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return language_list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView inflater_select_language_item_tv_language;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.inflater_select_language_screen, null);
        holder.inflater_select_language_item_tv_language = (TextView) rowView.findViewById(R.id.inflater_select_language_item_tv_language);


        holder.inflater_select_language_item_tv_language.setText(language_list.get(position).getLngName());

        return rowView;
    }


    // Filter Class
    public void filter(String charText,View include_search_iv_cross) {
        charText = charText.toLowerCase(Locale.getDefault());
        language_list.clear();
        if (charText.length() == 0||charText.equalsIgnoreCase("")) {
            language_list.addAll(arraylist);
            include_search_iv_cross.setVisibility(View.GONE);
        }
        else
        {
            for (LanguageData wp : arraylist)
            {
                if (wp.getLngName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    language_list.add(wp);
                }
            }

            include_search_iv_cross.setVisibility(View.VISIBLE);
        }
        notifyDataSetChanged();
    }
}
